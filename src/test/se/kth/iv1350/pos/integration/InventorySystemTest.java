package se.kth.iv1350.pos.integration;

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
    }

    @AfterEach
    public void tearDown() {
        instanceToTest = null;
    }


    @Test
    public void testGetItemInfoFoundReturnsDTO() throws ItemNotFoundException {
        int itemIDFound = 101;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "getItemInfo should return an ItemDTO for a valid ID.");
    }

    @Test
    public void testGetItemInfoFoundHasCorrectID() throws ItemNotFoundException {
        int itemIDFound = 101;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertEquals(itemIDFound, result.getItemID(), "The returned DTO should have the correct itemID.");
    }

    @Test
    public void testGetItemInfoFoundAnotherID() throws ItemNotFoundException {
        int itemIDFound = 102;
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "getItemInfo should return an ItemDTO for another valid ID.");
        assertEquals(itemIDFound, result.getItemID(), "The returned DTO should have the correct itemID for 102.");
    }

    @Test
    public void testGetItemInfoNotFoundThrowsException() {
        int itemIDNotFound = 999;
        ItemNotFoundException thrown = assertThrows(ItemNotFoundException.class, () -> {
            instanceToTest.getItemInfo(itemIDNotFound);
        }, "getItemInfo should throw ItemNotFoundException for an invalid ID.");
        assertEquals(itemIDNotFound, thrown.getItemIDNotFound(), "The exception should contain the correct itemID.");
    }

    @Test
    public void testGetItemInfoSimulatedDatabaseFailureThrowsException() {
        int itemIDForDbFailure = 666;
        DatabaseFailureException thrown = assertThrows(DatabaseFailureException.class, () -> {
            instanceToTest.getItemInfo(itemIDForDbFailure);
        }, "getItemInfo should throw DatabaseFailureException for the simulated DB failure ID.");
        assertTrue(thrown.getMessage().contains("Could not connect to the item database"), "The exception message should indicate a DB connection issue.");
    }

    @Test
    public void testGetItemInfoFoundHasCorrectDescription() throws ItemNotFoundException {
        int itemIDFound = 101;
        String expectedDescription = "Coffee"; 
        ItemDTO result = instanceToTest.getItemInfo(itemIDFound);
        assertNotNull(result, "Precondition failed: DTO should not be null.");
        assertEquals(expectedDescription, result.getDescription(), "The returned DTO should have the correct description.");
    }
}