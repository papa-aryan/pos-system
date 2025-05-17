package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.SaleInfoDTO;

/*
 * Represents the external inventory system. (Placeholder)
 * Contains methods for accessing item data.
 */
public class InventorySystem {

    private static final int SIMULATED_DB_FAILURE_ITEM_ID = 666;

    /*
     * Retrieves information about a specific item.
     * This simulates looking up an item ID and fetching its details.
     *
     * @param itemID The unique identifier for the item to retrieve.
     * @return An ItemDTO containing the item's information if found.
     * @throws ItemNotFoundException If the itemID does not correspond to a known item (and is not the DB failure ID).
     * @throws DatabaseFailureException If the itemID matches the hardcoded ID for simulating database failure.
     */
    public ItemDTO getItemInfo(int itemID) throws ItemNotFoundException {
        if (itemID == SIMULATED_DB_FAILURE_ITEM_ID) {
            throw new DatabaseFailureException("Could not connect to the item database.");
        }

        if (itemID == 101) {
            String description = "Coffee";
            Amount price = new Amount(15.00);
            int tax = 25; 
            return new ItemDTO(itemID, price, tax, description);

        } else if (itemID == 102) {
            String description = "Croissant";
            Amount price = new Amount(2.50);
            int tax = 12; 
            return new ItemDTO(itemID, price, tax, description);

        } else {
            // Item not found in the inventory system.
            throw new ItemNotFoundException(itemID);
        }
    }

    /*
     * Updates the external inventory system based on the completed sale.
     * (Placeholder implementation)
     *
     * @param saleInfoInventory A DTO containing information about the sold items.
     */
    public void updateInventory(SaleInfoDTO saleInfoInventory) {
    }
}
