package pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pos.integration.DiscountInfoDTO; 
import pos.integration.ItemDTO;
import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {
    private Sale instanceToTest;
    private ItemDTO coffeeDTO;
    private ItemDTO croissantDTO;

    @BeforeEach
    void setUp() {
        instanceToTest = new Sale();
        coffeeDTO = new ItemDTO(101, new Amount(15.0), 25, "Coffee");
        croissantDTO = new ItemDTO(102, new Amount(2.5), 12, "Croissant");
    }

    @AfterEach
    void tearDown() {
        instanceToTest = null;
        coffeeDTO = null;
        croissantDTO = null;
    }

    @Test
    void testAddItemUpdatesTotal() {
        int quantity = 2;
        instanceToTest.addItem(coffeeDTO, quantity);
        Amount expectedTotal = new Amount(15.0 * quantity);
        Amount actualTotal = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Adding 2 coffees should result in a total of 30.0.");
    }

     @Test
    void testAddMultipleItemsUpdatesTotal() {
        instanceToTest.addItem(coffeeDTO, 1);
        instanceToTest.addItem(croissantDTO, 2); 
        Amount expectedTotal = new Amount(15.0 + 5.0); 
        Amount actualTotal = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Adding 1 coffee and 2 croissants should result in a total of 20.0.");
    }

    @Test
    void testApplyPercentageDiscount() {
        instanceToTest.addItem(coffeeDTO, 2);
        DiscountInfoDTO percentageDiscount = new DiscountInfoDTO(new Amount(0), 10, "Percentage"); // 10% off

        instanceToTest.applyDiscount(percentageDiscount);

        Amount expectedTotalAfterDiscount = new Amount(30.0 * 0.9);
        Amount actualTotalAfterDiscount = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotalAfterDiscount, actualTotalAfterDiscount, "Applying 10% discount to 30.0 should result in 27.0.");
    }

    @Test
    void testApplyAmountDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); 
        Amount discountValue = new Amount(5.0);
        DiscountInfoDTO amountDiscount = new DiscountInfoDTO(discountValue, 0, "Amount"); // 5.0 off

        instanceToTest.applyDiscount(amountDiscount);

        Amount expectedTotalAfterDiscount = new Amount(30.0 - 5.0); 
        Amount actualTotalAfterDiscount = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotalAfterDiscount, actualTotalAfterDiscount, "Applying 5.0 amount discount to 30.0 should result in 25.0.");
    }


    @Test
    void testApplyNullDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); 
        Amount initialTotal = instanceToTest.getSaleInfoForDiscount().getRunningTotal();

        instanceToTest.applyDiscount(null); // Apply null discount

        Amount actualTotalAfterDiscount = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, actualTotalAfterDiscount, "Applying null discount should not change the running total.");
    }

    /* tax calculation tests */

    @Test
    void testAddItemUpdatesVAT() {
        instanceToTest.addItem(coffeeDTO, 1); 
        Amount expectedVAT = new Amount(15.0 * 0.25); 
        Amount expectedTotalWithTax = new Amount(15.0).plus(expectedVAT); 
        
        instanceToTest.calculateAndGetFinalTotal();
        Amount actualTotalWithTax = instanceToTest.getFinalTotalWithTax();
        assertEquals(expectedTotalWithTax, actualTotalWithTax, "Adding one coffee should result in correct total including VAT.");
    }

    @Test
    void testAddMultipleItemsUpdatesVATCorrectly() {
        instanceToTest.addItem(coffeeDTO, 1); 
        instanceToTest.addItem(croissantDTO, 2);
        Amount expectedTotalVAT = new Amount(15.0 * 0.25).plus(new Amount(2.5 * 0.12 * 2));
        Amount expectedRunningTotal = new Amount(15.0).plus(new Amount(2.5 * 2));
        Amount expectedTotalWithTax = expectedRunningTotal.plus(expectedTotalVAT); 

        instanceToTest.calculateAndGetFinalTotal();
        Amount actualTotalWithTax = instanceToTest.getFinalTotalWithTax();
        
        assertEquals(expectedTotalWithTax, actualTotalWithTax, "Adding multiple items should result in correct total including combined VAT.");
    }

    @Test
    void testGetTotalWithTaxNoDiscount() {
        instanceToTest.addItem(coffeeDTO, 2);
        Amount expectedTotal = new Amount(30.0 + 7.50);

        instanceToTest.calculateAndGetFinalTotal();
        Amount actualTotal = instanceToTest.getFinalTotalWithTax();
        
        assertEquals(expectedTotal, actualTotal, "Total with tax should be running total + VAT when no discount applied.");
    }

    @Test
    void testGetTotalWithTaxWithPercentageDiscount() {
        instanceToTest.addItem(coffeeDTO, 2);
        DiscountInfoDTO percentageDiscount = new DiscountInfoDTO(new Amount(0), 10, "Percentage"); // 10% off
        instanceToTest.applyDiscount(percentageDiscount); 

        Amount expectedTotal = new Amount(27.0 + 7.50); 
        
        instanceToTest.calculateAndGetFinalTotal();
        Amount actualTotal = instanceToTest.getFinalTotalWithTax();
        
        assertEquals(expectedTotal, actualTotal, "Total with tax should be discounted running total + original VAT.");
    }

     @Test
    void testGetTotalWithTaxWithAmountDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); 
        Amount discountValue = new Amount(5.0);
        DiscountInfoDTO amountDiscount = new DiscountInfoDTO(discountValue, 0, "Amount"); // 5.0 off
        instanceToTest.applyDiscount(amountDiscount); 

        Amount expectedTotal = new Amount(25.0 + 7.50); 
        
        instanceToTest.calculateAndGetFinalTotal();
        Amount actualTotal = instanceToTest.getFinalTotalWithTax();
        
        assertEquals(expectedTotal, actualTotal, "Total with tax should be discounted running total + original VAT after amount discount.");
    }

    // Tests for Payment Processing

    @Test
    void testProcessPaymentCalculatesCorrectChange() {
        instanceToTest.addItem(coffeeDTO, 1); 
        instanceToTest.calculateAndGetFinalTotal(); 
        Amount paidAmount = new Amount(20.0);
        Amount expectedChange = new Amount(20.0 - 18.75); 

        ReceiptDTO receipt = instanceToTest.processPaymentAndGetReceiptDetails(paidAmount);

        assertEquals(expectedChange, receipt.getChange(), "Change should be correctly calculated.");
    }

    @Test
    void testProcessPaymentReturnsReceiptWithCorrectTotals() {
        instanceToTest.addItem(coffeeDTO, 1); 
        Amount finalTotal = instanceToTest.calculateAndGetFinalTotal(); 
        Amount paidAmount = new Amount(20.0);
        Amount change = paidAmount.minus(finalTotal);

        ReceiptDTO receipt = instanceToTest.processPaymentAndGetReceiptDetails(paidAmount);

        assertEquals(finalTotal, receipt.getTotalAmount(), "Receipt total amount should match calculated final total.");
        assertEquals(paidAmount, receipt.getAmountPaid(), "Receipt amount paid should match the paid amount.");
        assertEquals(change, receipt.getChange(), "Receipt change should match calculated change.");
        assertNotNull(receipt.getItems(), "Receipt should contain a list of items.");
        assertNotNull(receipt.getDateTime(), "Receipt should have a date/time.");
    }

    @Test
    void testProcessPaymentThrowsExceptionIfPaidAmountIsInsufficient() {
        instanceToTest.addItem(coffeeDTO, 1); 
        instanceToTest.calculateAndGetFinalTotal(); 
        Amount insufficientPaidAmount = new Amount(10.0);

        assertThrows(IllegalArgumentException.class, () -> {
            instanceToTest.processPaymentAndGetReceiptDetails(insufficientPaidAmount);
        }, "Should throw IllegalArgumentException if paid amount is less than total.");
    }

    @Test
    void testProcessPaymentThrowsExceptionIfNotEnded() {
        instanceToTest.addItem(coffeeDTO, 1);
        Amount paidAmount = new Amount(20.0);

        assertThrows(IllegalStateException.class, () -> {
            instanceToTest.processPaymentAndGetReceiptDetails(paidAmount);
        }, "Should throw IllegalStateException if payment processed before sale ended.");
    }

    @Test
    void testGetSaleInfoForAccountingReturnsDTO() {
        instanceToTest.addItem(coffeeDTO, 1);
        SaleInfoDTO saleInfo = instanceToTest.getSaleInfoForAccounting();
        assertNotNull(saleInfo, "getSaleInfoForAccounting should return a non-null DTO.");
        assertEquals(new Amount(15.0), saleInfo.getRunningTotal(), "DTO running total should match sale state.");
    }

    @Test
    void testGetSaleInfoForInventoryReturnsDTO() {
        instanceToTest.addItem(coffeeDTO, 1);
        SaleInfoDTO saleInfo = instanceToTest.getSaleInfoForInventory();
        assertNotNull(saleInfo, "getSaleInfoForInventory should return a non-null DTO.");
        assertEquals(new Amount(15.0), saleInfo.getRunningTotal(), "DTO running total should match sale state.");
    }

}