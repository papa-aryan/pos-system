package se.kth.iv1350.pos.model;

import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter;
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
     * @param items A list representing the items sold (using format from SaleItem.formatForDTO).
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

        builder.append("Time of Sale: ").append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n\n");

        for (String itemLine : items) {
            builder.append(formatItemLine(itemLine)); 
        }
        builder.append("\n");

        builder.append("Total: ").append(totalAmount).append("\n");
        builder.append("\n");
        builder.append("Cash: ").append(amountPaid).append("\n"); 
        builder.append("Change: ").append(change).append("\n");

        return builder.toString();
    }

    private String formatItemLine(String itemLine) {
        // TODO: add try...catch in seminar 4

        String name = itemLine.substring(0, itemLine.indexOf(" (Qty:"));
        String qtyStr = itemLine.substring(itemLine.indexOf("Qty: ") + 5, itemLine.indexOf(", Price:"));
        String priceStr = itemLine.substring(itemLine.indexOf("Price: ") + 7, itemLine.indexOf(", Tax:"));

        Amount price = new Amount(Double.parseDouble(priceStr.replace(",", ".")));
        int qty = Integer.parseInt(qtyStr);
        Amount lineTotal = price.multiply(qty);

        return String.format("%s %d x %s %s\n", name, qty, price, lineTotal);
    }

}