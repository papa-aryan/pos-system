package se.kth.iv1350.pos.model;

import java.util.ArrayList;
import java.util.List;


/*
 * Represents a snapshot of the sale's state, used for discount calculation.
 * Contains immutable information about the sale.
 */
public final class SaleInfoDTO {
    private final Amount runningTotal; 
    private final List<String> items; 
    private final Amount totalVAT; 

    /*
     * Creates a new instance.
     *
     * @param runningTotal The current total price before discount.
     */
    public SaleInfoDTO(Amount runningTotal, List<String> items, Amount totalVAT) {
        this.runningTotal = runningTotal;
        this.items = new ArrayList<>(items);


        this.totalVAT = totalVAT;
    }

    // Getter methods

    public Amount getRunningTotal() {
        return runningTotal;
    }

    public List<String> getItems() {
        return items; 
    }

    public Amount getTotalVAT() {
        return totalVAT;
    }
}