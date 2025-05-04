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
        System.out.println("InventorySystemTest: Set up complete."); 
    }

    @AfterEach
    public void tearDown() {
        instanceToTest = null;
        System.out.println("InventorySystemTest: Tear down complete."); 
    }


    @Test
    public void testGetItemInfoFoundReturnsDTO() {
        int itemIDFound = 101;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "getItemInfo should return an ItemDTO for a valid ID.");
    }

    @Test
    public void testGetItemInfoFoundHasCorrectID() {
        int itemIDFound = 101;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertEquals(itemIDFound, result.getItemID(), "The returned DTO should have the correct itemID.");
    }

    @Test
    public void testGetItemInfoFoundAnotherID() {
        int itemIDFound = 102;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "getItemInfo should return an ItemDTO for another valid ID.");
        assertEquals(itemIDFound, result.getItemID(), "The returned DTO should have the correct itemID for 102.");
    }

    @Test
    public void testGetItemInfoNotFoundReturnsNull() {
        int itemIDNotFound = 999;
        ItemDTO result = instanceToTest.getItemInfo(itemIDNotFound);
        assertNull(result, "getItemInfo should return null for an invalid ID.");
    }

    @Test
    public void testGetItemInfoFoundHasCorrectDescription() {
        int itemIDFound = 101;
        String expectedDescription = "Coffee"; 
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "Precondition failed: DTO should not be null.");
        assertEquals(expectedDescription, result.getDescription(), "The returned DTO should have the correct description.");
    }
}