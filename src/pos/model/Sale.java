package pos.model;

import java.time.LocalTime;
import pos.integration.ItemDTO;
import pos.integration.DiscountInfoDTO;

import java.util.ArrayList; 
import java.util.List;   

/*
 * One single sale made by one customer and payed with one payment.
 */
public class Sale {
    private LocalTime saleTime;
    private Receipt receipt;
    private Amount runningTotal; 
    private List<SaleItem> items; // List to store SaleItem objects

    private static class SaleItem {
        ItemDTO itemInfo;
        int quantity;

        SaleItem(ItemDTO itemInfo, int quantity) {
            this.itemInfo = itemInfo;
            this.quantity = quantity;
        }
        // Optional: Add getters if needed later
        // ItemDTO getItemInfo() { return itemInfo; }
        // int getQuantity() { return quantity; }
    }

    // TODO: Add fields to store items (e.g., Map<Integer, SaleItem>), applied discount, etc.
    // private DiscountInfoDTO appliedDiscount = null;



    /*
     * Creates a new instance and saves the time of the sale.
     */
    public Sale() {
        setTimeOfSale();
        this.runningTotal = new Amount(0); 
        this.items = new ArrayList<>(); 

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
                           ", Qty=" + quantity);

        // TODO: Later, implement storing this itemInfo and quantity (e.g., in a List or Map).
        // TODO: Later, update running totals using itemInfo.getPrice() and itemInfo.getTax().
        
        // Store the item in a SaleItem object
        SaleItem saleItem = new SaleItem(itemInfo, quantity);
        this.items.add(saleItem);

        // TODO: Later, update running totals using itemInfo.getPrice() and itemInfo.getTax().

        
        Amount itemTotal = itemInfo.getPrice().multiply(quantity);
        this.runningTotal = this.runningTotal.plus(itemTotal);

        System.out.println("Sale: Updated running total: " + this.runningTotal);

        // TODO: Later, update total VAT as well.
    }

     /*
     * Gets a snapshot of the current sale state for discount calculation.
     *
     * @return A SaleInfoDTO containing relevant sale data.
     */
    public SaleInfoDTO getSaleInfoForDiscount() {
        System.out.println("Sale: Generating SaleInfoDTO for discount check. Current total: " + this.runningTotal);
        // TODO: Populate with real saleID, items, VAT later
        return new SaleInfoDTO(this.runningTotal); // Use the actual runningTotal
    }

    /*
     * Applies a discount to the sale.
     * (Placeholder implementation for calculation logic)
     *
     * @param discountInfo The DTO containing the discount details to apply.
     */
    public void applyDiscount(DiscountInfoDTO discountInfo) {
        if (discountInfo != null) {
            System.out.println("Sale: Applying discount: Type=" + discountInfo.getDiscountType() +
                               ", Percentage=" + discountInfo.getDiscountPercentage() + "%" +
                               ", Amount=" + discountInfo.getDiscountAmount()); 

        // Calculate the total *after* discount
        Amount totalAfterDiscount = calculateTotalAfterDiscount(discountInfo);
        System.out.println("Sale: Original running total: " + this.runningTotal);
        System.out.println("Sale: Calculated total after discount: " + totalAfterDiscount);

        // Actually update the running total
        this.runningTotal = totalAfterDiscount;

        // TODO: Store the applied discount details if needed for receipt/logging
        // this.appliedDiscount = discountInfo;

        } else {
            System.out.println("Sale: No discount to apply.");
        }
    }

    // Placeholder for discount calculation logic
    private Amount calculateTotalAfterDiscount(DiscountInfoDTO discountInfo) {
        
        if (discountInfo == null) {
            return this.runningTotal;
        }

        if ("Percentage".equals(discountInfo.getDiscountType())) {
            int discountPercentage = discountInfo.getDiscountPercentage();
            // Careful with integer division. Calculate discount amount first.
            // amount * percentage / 100
            double discountAmount = (this.runningTotal.getAmount() * discountPercentage) / 100;
            Amount discount = new Amount(discountAmount);
            return this.runningTotal.minus(discount);
        } else if ("Amount".equals(discountInfo.getDiscountType())) {
            return this.runningTotal.minus(discountInfo.getDiscountAmount());
        }
        // Default if type unknown or other types exist
        return this.runningTotal;
    }

}

