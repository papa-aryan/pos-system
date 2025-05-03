package pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pos.model.Amount;
import pos.model.SaleInfoDTO; // Import necessary DTOs
import static org.junit.jupiter.api.Assertions.*;

public class DiscountDatabaseTest {
    private DiscountDatabase instanceToTest;
    private SaleInfoDTO dummySaleInfo;

    @BeforeEach
    void setUp() {
        instanceToTest = new DiscountDatabase();
        // Create a dummy SaleInfoDTO needed for the getDiscount method signature
        dummySaleInfo = new SaleInfoDTO(new Amount(100.0)); // Content doesn't matter for current logic
    }

    @AfterEach
    void tearDown() {
        instanceToTest = null;
        dummySaleInfo = null;
    }

    @Test
    void testGetDiscountFound() {
        int customerIDWithDiscount = 1234;
        DiscountInfoDTO result = instanceToTest.getDiscount(customerIDWithDiscount, dummySaleInfo);
        assertNotNull(result, "Discount should be found for customer ID 1234.");
    }

    @Test
    void testGetDiscountFoundCorrectPercentage() {
        int customerIDWithDiscount = 1234;
        DiscountInfoDTO result = instanceToTest.getDiscount(customerIDWithDiscount, dummySaleInfo);
        assertEquals(10, result.getDiscountPercentage(), "Discount percentage should be 10 for customer 1234.");
    }

     @Test
    void testGetDiscountFoundCorrectType() {
        int customerIDWithDiscount = 1234;
        DiscountInfoDTO result = instanceToTest.getDiscount(customerIDWithDiscount, dummySaleInfo);
        // Assuming result is not null
        assertEquals("Percentage", result.getDiscountType(), "Discount type should be 'Percentage' for customer 1234.");
    }

    @Test
    void testGetDiscountNotFound() {
        int customerIDWithoutDiscount = 5678;
        DiscountInfoDTO result = instanceToTest.getDiscount(customerIDWithoutDiscount, dummySaleInfo);
        assertNull(result, "Discount should not be found (should be null) for customer ID 5678.");
    }
}