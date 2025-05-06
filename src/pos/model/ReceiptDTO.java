package pos.model;

import java.time.LocalDateTime; 
import java.time.format.DateTimeFormatter; // Import formatter
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
        // Format date and time like the example
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
        // Example itemLine: "Coffee (Qty: 2, Price: 15,00, Tax: 25%)"
        // TODO: try...catch saved seminar 4
//        try {
            String name = itemLine.substring(0, itemLine.indexOf(" (Qty:"));
            String qtyStr = itemLine.substring(itemLine.indexOf("Qty: ") + 5, itemLine.indexOf(", Price:"));
            String priceStr = itemLine.substring(itemLine.indexOf("Price: ") + 7, itemLine.indexOf(", Tax:"));
            // Assuming priceStr uses comma decimal separator, replace with dot for parsing
            Amount price = new Amount(Double.parseDouble(priceStr.replace(",", ".")));
            int qty = Integer.parseInt(qtyStr);
            Amount lineTotal = price.multiply(qty);
            // Format: Item Name Qty x PricePerUnit LineTotal
            return String.format("%s %d x %s %s\n", name, qty, price, lineTotal);
//        } catch (Exception e) {
            // Fallback if parsing fails, print the original string
            // Optionally log the parsing error: System.err.println("Error parsing receipt item line: " + itemLine + " - " + e.getMessage());
            // return itemLine + "\n"; // Return original line with newline in case of error
//        }
    }

}