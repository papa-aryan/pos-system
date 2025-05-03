package pos.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the InventorySystem class.
 */
public class InventorySystemTest {
    private InventorySystem instanceToTest;

    @BeforeEach
    public void setUp() {
        instanceToTest = new InventorySystem();
        System.out.println("InventorySystemTest: Set up complete."); // Optional setup log
    }

    @AfterEach
    public void tearDown() {
        instanceToTest = null;
        System.out.println("InventorySystemTest: Tear down complete."); // Optional teardown log
    }

    /*
     * Test case for getItemInfo with a valid item ID that exists (e.g., 101).
     * Verifies that a non-null ItemDTO is returned.
     */
    @Test
    public void testGetItemInfoFoundReturnsDTO() {
        int itemIDFound = 101;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "getItemInfo should return an ItemDTO for a valid ID.");
    }

    /*
     * Test case for getItemInfo with a valid item ID that exists (e.g., 101).
     * Verifies that the returned ItemDTO contains the correct item ID.
     * (Following "ideally one assertion" rule, separated from the null check).
     */
    @Test
    public void testGetItemInfoFoundHasCorrectID() {
        int itemIDFound = 101;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        // Assuming result is not null based on previous test passing
        assertEquals(itemIDFound, result.getItemID(), "The returned DTO should have the correct itemID.");
    }

     /*
     * Test case for getItemInfo with another valid item ID that exists (e.g., 102).
     * Verifies that the returned ItemDTO contains the correct item ID.
     */
    @Test
    public void testGetItemInfoFoundAnotherID() {
        int itemIDFound = 102;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "getItemInfo should return an ItemDTO for another valid ID.");
        assertEquals(itemIDFound, result.getItemID(), "The returned DTO should have the correct itemID for 102.");
        // Note: Could split into two tests like for ID 101 if strictly adhering to one assertion.
    }

    /*
     * Test case for getItemInfo with an item ID that does not exist (e.g., 999).
     * Verifies that null is returned, covering the failure condition.
     */
    @Test
    public void testGetItemInfoNotFoundReturnsNull() {
        int itemIDNotFound = 999;
        ItemDTO result = instanceToTest.getItemInfo(itemIDNotFound);
        assertNull(result, "getItemInfo should return null for an invalid ID.");
    }

    // Add more tests for specific fields if needed, e.g., checking price/description
    /*
     * Test case for getItemInfo with a valid item ID (e.g., 101).
     * Verifies that the returned ItemDTO contains the correct description.
     */
    @Test
    public void testGetItemInfoFoundHasCorrectDescription() {
        int itemIDFound = 101;
        String expectedDescription = "Coffee"; // Match the description in InventorySystem
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "Precondition failed: DTO should not be null.");
        assertEquals(expectedDescription, result.getDescription(), "The returned DTO should have the correct description.");
    }
}