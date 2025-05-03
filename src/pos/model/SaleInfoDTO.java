package pos.model;

import pos.model.Amount;

// TODO: Import necessary types for items list later (e.g., java.util.List<ItemDTO>)

/*
 * Represents a snapshot of the sale's state, used for discount calculation.
 * Contains immutable information about the sale.
 */
public final class SaleInfoDTO {
    private final Amount runningTotal; // Placeholder type
    // private final int saleID; // Sale ID not implemented yet
    // private final List<SomeItemRepresentation> items; // Item list not implemented yet
    // private final Amount totalVAT; // VAT calculation not implemented yet

    /*
     * Creates a new instance.
     *
     * @param runningTotal The current total price before discount.
     */
    public SaleInfoDTO(Amount runningTotal) {
        this.runningTotal = runningTotal;
    }

    // Getter Metohds

    public Amount getRunningTotal() {
        return runningTotal;
    }

    // Add getters for other fields later (saleID, items, totalVAT)
}