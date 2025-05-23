package se.kth.iv1350.pos.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.integration.*; 
import se.kth.iv1350.pos.model.Amount; 
import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Controller instanceToTest;
    private InventorySystem invSys;
    private DiscountDatabase discDb;
    private AccountingSystem accSys;
    private Printer printer;

    @BeforeEach
    void setUp() {
        invSys = new InventorySystem();
        discDb = new DiscountDatabase();
        accSys = new AccountingSystem();
        printer = new Printer();
        instanceToTest = new Controller(invSys, discDb, accSys, printer);
    }

    @AfterEach
    void tearDown() {
        instanceToTest = null;
        invSys = null;
        discDb = null;
        accSys = null;
        printer = null;
    }

    // Tests for enterItem

    @Test
    void testEnterItemValidUpdatesSaleTotal() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        int itemID = 101;
        int quantity = 2;
        instanceToTest.enterItem(itemID, quantity);
        Amount expectedTotal = new Amount(15.0 * quantity); // Coffee price * quantity
        Amount actualTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Entering valid item should update sale total.");
    }

    @Test
    void testEnterItemInvalidQuantityDoesNotUpdateSaleTotal() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        Amount initialTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        int itemID = 101;
        int invalidQuantity = 0;
        instanceToTest.enterItem(itemID, invalidQuantity);
        Amount totalAfterInvalid = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, totalAfterInvalid, "Entering item with quantity 0 should not change sale total.");

        instanceToTest.enterItem(itemID, -1); 
        totalAfterInvalid = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, totalAfterInvalid, "Entering item with negative quantity should not change sale total.");
    }

     @Test
    void testEnterItemBeforeStartSaleDoesNothing() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.enterItem(101, 1);
        // Check that the internal sale object is still null (or wasn't accessed causing NullPointerException)
        assertNull(instanceToTest.sale, "Sale object should be null if enterItem called before startSale.");
    }

    @Test
    void testEnterItemInvalidIDPassesAlongExceptionToView() {
        instanceToTest.startSale();
        int itemIDNotFound = 999;
        int quantity = 1;

        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class, () -> {
            instanceToTest.enterItem(itemIDNotFound, quantity);
        }, "enterItem should propagate ItemNotFoundException when InventorySystem throws it.");
        assertEquals(itemIDNotFound, thrown.getItemIDNotFound(), "The propagated exception should contain the correct non-existing itemID.");
    }

    @Test
    void testEnterItemDatabaseFailureWrapsAndThrowsOperationFailedException() {
        instanceToTest.startSale();
        int itemIDForDbFailure = 666;
        int quantity = 1;

        OperationFailedException thrown = assertThrows(OperationFailedException.class, () -> {
            instanceToTest.enterItem(itemIDForDbFailure, quantity);
        }, "enterItem should throw OperationFailedException when InventorySystem simulates a DB error.");

        assertNotNull(thrown.getCause(), "OperationFailedException should have a cause.");
        assertTrue(thrown.getCause() instanceof DatabaseFailureException, "The cause of OperationFailedException should be DatabaseFailureException.");
        assertTrue(thrown.getMessage().contains("Operation failed due to a database error."), "The exception message should indicate an operation failure due to DB.");
    }

    // Tests for requestDiscount

    @Test
    void testRequestDiscountAppliesDiscountToSale() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 2); 
        int customerIDWithDiscount = 1234;

        instanceToTest.requestDiscount(customerIDWithDiscount);

        Amount expectedTotal = new Amount(30.0 * 0.9); 
        Amount actualTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Requesting discount for customer 1234 should apply 10% discount.");
    }

    @Test
    void testRequestDiscountDoesNotApplyDiscountToSale() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 2); 
        Amount initialTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        int customerIDWithoutDiscount = 5678;

        instanceToTest.requestDiscount(customerIDWithoutDiscount); 

        Amount actualTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, actualTotal, "Requesting discount for customer 5678 should not change sale total.");
    }

    @Test
    void testRequestDiscountBeforeStartSaleDoesNothing() {
        instanceToTest.requestDiscount(1234);
        assertNull(instanceToTest.sale, "Sale object should be null if requestDiscount called before startSale.");
    }

    // Tests for endSale

    @Test
    void testEndSaleCalculatesFinalTotal() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 1);
        instanceToTest.endSale();
        // Verify that the final total was calculated and stored in the Sale object
        assertNotNull(instanceToTest.sale.getFinalTotalWithTax(), "Final total should be calculated by endSale.");
        assertEquals(new Amount(18.75), instanceToTest.sale.getFinalTotalWithTax(), "Final total calculation should be correct.");
    }

    @Test
    void testEndSaleBeforeStartSaleDoesNothing() {
        // Calling endSale before startSale should ideally not throw an error due to the check
        // and the sale object should remain null.
        assertDoesNotThrow(() -> {
             instanceToTest.endSale();
        }, "endSale before startSale should not throw an exception.");
        assertNull(instanceToTest.sale, "Sale object should be null if endSale called before startSale.");
    }


    // Tests for makePayment

    @Test
    void testMakePaymentSuccessfulFlow() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 1); 
        instanceToTest.endSale();
        Amount paidAmount = new Amount(20.0);

        assertDoesNotThrow(() -> {
            instanceToTest.makePayment(paidAmount);
        }, "Successful payment flow should not throw exceptions.");
    }

    @Test
    void testMakePaymentBeforeStartSaleDoesNothing() {
        Amount paidAmount = new Amount(20.0);
        assertDoesNotThrow(() -> {
             instanceToTest.makePayment(paidAmount);
        }, "makePayment before startSale should not throw an exception.");
        assertNull(instanceToTest.sale, "Sale object should be null if makePayment called before startSale.");
    }

    @Test
    void testMakePaymentBeforeEndSale() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 1);
        Amount paidAmount = new Amount(20.0);

        assertDoesNotThrow(() -> {
             instanceToTest.makePayment(paidAmount);
        }, "makePayment before endSale should log an error but not crash.");
    }

     @Test
    void testMakePaymentWithInsufficientAmount() throws ItemNotFoundException, OperationFailedException {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 1); 
        instanceToTest.endSale();
        Amount insufficientPaidAmount = new Amount(10.0);

        assertDoesNotThrow(() -> {
             instanceToTest.makePayment(insufficientPaidAmount);
        }, "makePayment with insufficient amount should log an error but not crash.");
    }

}