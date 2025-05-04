package pos.integration;

import pos.model.Amount;
import pos.model.SaleInfoDTO;

/*
 * Represents the external inventory system. (Placeholder)
 * Contains methods for accessing item data.
 */
public class InventorySystem {

    /*
     * Retrieves information about a specific item.
     * This simulates looking up an item ID and fetching its details.
     *
     * @param itemID The unique identifier for the item to retrieve.
     * @return An ItemDTO containing the item's information if found, otherwise null.
     */
    public ItemDTO getItemInfo(int itemID) {

        if (itemID == 101) {
            String description = "Coffee";
            Amount price = new Amount(15.00);
            int tax = 25; // 25%
            // System.out.println("InventorySystem: Found item " + itemID);
            return new ItemDTO(itemID, price, tax, description);

        } else if (itemID == 102) {
            String description = "Croissant";
            Amount price = new Amount(2.50);
            int tax = 12; // 12%
            // System.out.println("InventorySystem: Found item " + itemID);
            return new ItemDTO(itemID, price, tax, description);

        } else {
            // System.out.println("InventorySystem: Item ID " + itemID + " not found.");
            return null; 
        }
    }

    /*
     * Updates the external inventory system based on the completed sale.
     * (Placeholder implementation)
     *
     * @param saleInfoInventory A DTO containing information about the sold items.
     */
    public void updateInventory(SaleInfoDTO saleInfoInventory) {
        // System.out.println("InventorySystem: Received request to update inventory.");
        // System.out.println("InventorySystem: Items sold:");
        // saleInfoInventory.getItems().forEach(item -> System.out.println("  - " + item));
        // System.out.println("InventorySystem: Inventory update simulation complete.");
    }
}
