package pos.model;

import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.List;

/*
 * Represents the data needed to print a receipt. Immutable.
 */
public final class ReceiptDTO {
    private final LocalDateTime dateTime;
    private final List<String> items; 
    private final Amount totalAmount; 
    private final Amount amountPaid;
    private final Amount change;

    /*
     * Creates a new instance of ReceiptDTO.
     *
     * @param dateTime The date and time of the sale completion.
     * @param items A list representing the items sold.
     * @param totalAmount The final total amount for the sale.
     * @param amountPaid The amount paid by the customer.
     * @param change The change given back to the customer.
     */
    public ReceiptDTO(LocalDateTime dateTime, List<String> items, Amount totalAmount, Amount amountPaid, Amount change) {
        this.dateTime = dateTime;
        this.items = new ArrayList<>(items); 
        this.totalAmount = totalAmount;
        this.amountPaid = amountPaid;
        this.change = change;
    }

    // Getters

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<String> getItems() {
        return items;
    }

    public Amount getTotalAmount() {
        return totalAmount;
    }

    public Amount getAmountPaid() {
        return amountPaid;
    }

    public Amount getChange() {
        return change;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Receipt Time: ").append(dateTime).append("\n");
        builder.append("Total: ").append(totalAmount).append("\n");
        builder.append("Paid: ").append(amountPaid).append("\n");
        builder.append("Change: ").append(change).append("\n");
        builder.append("Items:\n");
        items.forEach(item -> builder.append("  ").append(item).append("\n"));
        return builder.toString();
    }
}