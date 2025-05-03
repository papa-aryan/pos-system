package pos.integration; // Keeping it in integration as per your current structure

import pos.model.Amount;

/*
 * Represents a DTO for one specific item.
 * Defined by the integration layer to transfer item data.
 * Contains immutable information about an item retrieved from the inventory system.
 */
public final class ItemDTO {
    private final int itemID;
    private final Amount price; // SHOULD BE Amount TYPE NOT double
    private final int tax;      // Tax rate as percentage points (e.g., 25 for 25%)
    private final String description;

    /*
     * Creates a new instance representing a specific item.
     * (Constructor parameters based on class diagram fields)
     *
     * @param itemID The unique id for the item.
     * @param price The price of the item (excluding tax).
     * @param tax The tax rate applicable to the item as percentage points (e.g., 25 for 25%).
     * @param description A description of the item.
     */
    public ItemDTO(int itemID, Amount price, int tax, String description) {
        this.itemID = itemID;
        this.price = price;
        this.tax = tax;
        this.description = description;
    }

     // Getter methods

    public int getItemID() {
        return itemID;
    }

    public Amount getPrice() {
        return price;
    }

    public double getTax() {
        return tax;
    }

    public String getDescription() {
        return description;
    }
}