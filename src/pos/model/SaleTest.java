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

}