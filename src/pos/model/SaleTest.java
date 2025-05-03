package pos.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pos.integration.DiscountInfoDTO; // Import necessary classes
import pos.integration.ItemDTO;
import static org.junit.jupiter.api.Assertions.*;

public class SaleTest {
    private Sale instanceToTest;
    private ItemDTO coffeeDTO;
    private ItemDTO croissantDTO;

    @BeforeEach
    void setUp() {
        instanceToTest = new Sale();
        // Create some dummy ItemDTOs to use in tests
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
        Amount expectedTotal = new Amount(15.0 * quantity); // 30.0
        // Use getSaleInfoForDiscount to check the running total state
        Amount actualTotal = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Adding 2 coffees should result in a total of 30.0.");
    }

     @Test
    void testAddMultipleItemsUpdatesTotal() {
        instanceToTest.addItem(coffeeDTO, 1);    // 15.0
        instanceToTest.addItem(croissantDTO, 2); // 2.5 * 2 = 5.0
        Amount expectedTotal = new Amount(15.0 + 5.0); // 20.0
        Amount actualTotal = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotal, actualTotal, "Adding 1 coffee and 2 croissants should result in a total of 20.0.");
    }

    @Test
    void testApplyPercentageDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); // Total is 30.0
        DiscountInfoDTO percentageDiscount = new DiscountInfoDTO(new Amount(0), 10, "Percentage"); // 10% off

        instanceToTest.applyDiscount(percentageDiscount);

        Amount expectedTotalAfterDiscount = new Amount(30.0 * 0.9); // 27.0
        Amount actualTotalAfterDiscount = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotalAfterDiscount, actualTotalAfterDiscount, "Applying 10% discount to 30.0 should result in 27.0.");
    }

    @Test
    void testApplyAmountDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); // Total is 30.0
        Amount discountValue = new Amount(5.0);
        DiscountInfoDTO amountDiscount = new DiscountInfoDTO(discountValue, 0, "Amount"); // 5.0 off

        instanceToTest.applyDiscount(amountDiscount);

        Amount expectedTotalAfterDiscount = new Amount(30.0 - 5.0); // 25.0
        Amount actualTotalAfterDiscount = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(expectedTotalAfterDiscount, actualTotalAfterDiscount, "Applying 5.0 amount discount to 30.0 should result in 25.0.");
    }


    @Test
    void testApplyNullDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); // Total is 30.0
        Amount initialTotal = instanceToTest.getSaleInfoForDiscount().getRunningTotal();

        instanceToTest.applyDiscount(null); // Apply null discount

        Amount actualTotalAfterDiscount = instanceToTest.getSaleInfoForDiscount().getRunningTotal();
        assertEquals(initialTotal, actualTotalAfterDiscount, "Applying null discount should not change the running total.");
    }

    /* tax calculation tests
     */

    @Test
    void testAddItemUpdatesVAT() {
        instanceToTest.addItem(coffeeDTO, 1); // Price 15.0, Tax 25%
        Amount expectedVAT = new Amount(15.0 * 0.25); // 3.75
        // Access totalVAT indirectly for verification if no getter exists,
        // or add a temporary getter/test-specific method if absolutely necessary,
        // but ideally test via getTotalWithTax. Let's test via getTotalWithTax.
        Amount expectedTotalWithTax = new Amount(15.0).plus(expectedVAT); // 15.0 + 3.75 = 18.75
        Amount actualTotalWithTax = instanceToTest.getTotalWithTax();
        // This assertion indirectly checks if VAT was added correctly to the total
        assertEquals(expectedTotalWithTax, actualTotalWithTax, "Adding one coffee should result in correct total including VAT.");
    }

    @Test
    void testAddMultipleItemsUpdatesVATCorrectly() {
        instanceToTest.addItem(coffeeDTO, 1);     // Price 15.0, Tax 25% -> VAT 3.75
        instanceToTest.addItem(croissantDTO, 2); // Price 2.5, Tax 12% -> VAT 0.30 per item * 2 = 0.60
        Amount expectedTotalVAT = new Amount(15.0 * 0.25).plus(new Amount(2.5 * 0.12 * 2)); // 3.75 + 0.60 = 4.35
        Amount expectedRunningTotal = new Amount(15.0).plus(new Amount(2.5 * 2)); // 15.0 + 5.0 = 20.0
        Amount expectedTotalWithTax = expectedRunningTotal.plus(expectedTotalVAT); // 20.0 + 4.35 = 24.35

        Amount actualTotalWithTax = instanceToTest.getTotalWithTax();
        // This assertion indirectly checks if combined VAT was added correctly
        assertEquals(expectedTotalWithTax, actualTotalWithTax, "Adding multiple items should result in correct total including combined VAT.");
    }

    @Test
    void testGetTotalWithTaxNoDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); // RunningTotal 30.0, VAT 7.50
        Amount expectedTotal = new Amount(30.0 + 7.50); // 37.50
        Amount actualTotal = instanceToTest.getTotalWithTax();
        assertEquals(expectedTotal, actualTotal, "Total with tax should be running total + VAT when no discount applied.");
    }

    @Test
    void testGetTotalWithTaxWithPercentageDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); // RunningTotal 30.0, VAT 7.50
        DiscountInfoDTO percentageDiscount = new DiscountInfoDTO(new Amount(0), 10, "Percentage"); // 10% off
        instanceToTest.applyDiscount(percentageDiscount); // RunningTotal becomes 27.0

        Amount expectedTotal = new Amount(27.0 + 7.50); // Discounted running total + original VAT = 34.50
        Amount actualTotal = instanceToTest.getTotalWithTax();
        assertEquals(expectedTotal, actualTotal, "Total with tax should be discounted running total + original VAT.");
    }

     @Test
    void testGetTotalWithTaxWithAmountDiscount() {
        instanceToTest.addItem(coffeeDTO, 2); // RunningTotal 30.0, VAT 7.50
        Amount discountValue = new Amount(5.0);
        DiscountInfoDTO amountDiscount = new DiscountInfoDTO(discountValue, 0, "Amount"); // 5.0 off
        instanceToTest.applyDiscount(amountDiscount); // RunningTotal becomes 25.0

        Amount expectedTotal = new Amount(25.0 + 7.50); // Discounted running total + original VAT = 32.50
        Amount actualTotal = instanceToTest.getTotalWithTax();
        assertEquals(expectedTotal, actualTotal, "Total with tax should be discounted running total + original VAT after amount discount.");
    }

}