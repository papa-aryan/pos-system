package se.kth.iv1350.pos.integration;

/**
 * Thrown when an attempt is made to find an item in the inventory system
 * with an identifier that does not exist.
 */
public class ItemNotFoundException extends Exception {
    private final int itemIDNotFound;

    /**
     * Creates a new instance with a message specifying which item ID could not be found.
     *
     * @param itemIDNotFound The item identifier that could not be found.
     */
    public ItemNotFoundException(int itemIDNotFound) {
        super("Item with ID '" + itemIDNotFound + "' was not found in the inventory system.");
        this.itemIDNotFound = itemIDNotFound;
    }

    /**
     * Gets the item identifier that could not be found.
     *
     * @return The item ID that was not found.
     */
    public int getItemIDNotFound() {
        return itemIDNotFound;
    }
}
