package pos.model;

import java.time.LocalTime;
import pos.integration.ItemDTO;

/*
 * One single sale made by one customer and payed with one payment.
 */
public class Sale {
    private LocalTime saleTime;
    private Receipt receipt;

    /*
     * Creates a new instance and saves the time of the sale.
     */
    public Sale() {
        setTimeOfSale();
    }

    private void setTimeOfSale() {
        saleTime = LocalTime.now();
        receipt = new Receipt();
    }

    /*
     * Adds an item to the current sale.
     *
     * @param itemInfo The DTO containing information about the item to add.
     *                 This object holds the data retrieved by InventorySystem.
     * @param quantity The quantity of the item to add.
     */
    public void addItem(ItemDTO itemInfo, int quantity) {
        // Use getters from ItemDTO to access the data passed from the Controller.
        System.out.println("Sale: Received call to add item: ID=" + itemInfo.getItemID() +
                           ", Desc='" + itemInfo.getDescription() + "'" +
                           ", Qty=" + quantity +
                           ". (Actual storage/calculation not implemented yet)");

        // TODO: Later, implement storing this itemInfo and quantity (e.g., in a List or Map).
        // TODO: Later, update running totals using itemInfo.getPrice() and itemInfo.getTax().
    }

    // Other methods from class diagram (getSaleInfo, applyDiscount, etc.) are not needed yet.
}

