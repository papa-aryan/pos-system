package pos.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pos.integration.*; // Import integration package
import pos.model.Amount; // Import model package
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
    void testEnterItemValidUpdatesSaleTotal() {
        instanceToTest.startSale();
        int itemID = 101;
        int quantity = 2;
        instanceToTest.enterItem(itemID, quantity);
        Amount expectedTotal = new Amount(15.0 * quantity); // Coffee price * quantity
        Amount actualTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Entering valid item should update sale total.");
    }

    @Test
    void testEnterItemInvalidQuantityDoesNotUpdateSaleTotal() {
        instanceToTest.startSale();
        Amount initialTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        int itemID = 101;
        int invalidQuantity = 0;
        instanceToTest.enterItem(itemID, invalidQuantity);
        Amount totalAfterInvalid = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, totalAfterInvalid, "Entering item with quantity 0 should not change sale total.");

        instanceToTest.enterItem(itemID, -1); // Test negative quantity
        totalAfterInvalid = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, totalAfterInvalid, "Entering item with negative quantity should not change sale total.");
    }

    @Test
    void testEnterItemNotFoundDoesNotUpdateSaleTotal() {
        instanceToTest.startSale();
        Amount initialTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        int itemIDNotFound = 999;
        int quantity = 1;
        instanceToTest.enterItem(itemIDNotFound, quantity);
        Amount totalAfterNotFound = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, totalAfterNotFound, "Entering non-existent item should not change sale total.");
    }

     @Test
    void testEnterItemBeforeStartSaleDoesNothing() {
        instanceToTest.enterItem(101, 1);
        // Check that the internal sale object is still null (or wasn't accessed causing NullPointerException)
        assertNull(instanceToTest.sale, "Sale object should be null if enterItem called before startSale.");
        // No exception should be thrown due to the null check in Controller.
    }

    // Tests for requestDiscount

    @Test
    void testRequestDiscountAppliesDiscountToSale() {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 2); // Sale total = 30.0
        int customerIDWithDiscount = 1234;

        instanceToTest.requestDiscount(customerIDWithDiscount);

        Amount expectedTotal = new Amount(30.0 * 0.9); // 27.0 (10% discount)
        Amount actualTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Requesting discount for customer 1234 should apply 10% discount.");
    }

    @Test
    void testRequestDiscountDoesNotApplyDiscountToSale() {
        instanceToTest.startSale();
        instanceToTest.enterItem(101, 2); // Sale total = 30.0
        Amount initialTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        int customerIDWithoutDiscount = 5678;

        instanceToTest.requestDiscount(customerIDWithoutDiscount); // Should find no discount

        Amount actualTotal = instanceToTest.sale.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, actualTotal, "Requesting discount for customer 5678 should not change sale total.");
    }

    @Test
    void testRequestDiscountBeforeStartSaleDoesNothing() {
        instanceToTest.requestDiscount(1234);
        assertNull(instanceToTest.sale, "Sale object should be null if requestDiscount called before startSale.");
    }
}