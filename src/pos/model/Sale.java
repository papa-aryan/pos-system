package pos.model;

import java.time.LocalTime;
import java.time.LocalDateTime; 
import pos.integration.ItemDTO;
import pos.integration.DiscountInfoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 

/*
 * One single sale made by one customer and payed with one payment.
 */
public class Sale {
    private LocalTime saleStartTime; 
    private Amount runningTotalBeforeTax; 
    private Amount totalVAT;
    private List<SaleItem> items;
    private Amount amountPaidByCustomer; 
    private Amount changeToCustomer; 
    private Amount finalTotalWithTax; 

    private static class SaleItem {
        ItemDTO itemInfo;
        int quantity;

        SaleItem(ItemDTO itemInfo, int quantity) {
            this.itemInfo = itemInfo;
            this.quantity = quantity;
        }

        // Helper method to format item for DTOs
        String formatForDTO() {
            return String.format("%s (Qty: %d, Price: %s, Tax: %d%%)",
                                 itemInfo.getDescription(),
                                 quantity,
                                 itemInfo.getPrice(), 
                                 (int)itemInfo.getTax()); 
        }
    }

    /*
     * Creates a new instance and saves the time of the sale start.
     */
    public Sale() {
        this.saleStartTime = LocalTime.now();
        this.runningTotalBeforeTax = new Amount(0);
        this.totalVAT = new Amount(0);
        this.items = new ArrayList<>();
        this.amountPaidByCustomer = new Amount(0);
        this.changeToCustomer = new Amount(0);
        this.finalTotalWithTax = null; 
    }

    /*
     * Adds an item to the sale. This method is called by the Controller.
     *
     * @param itemInfo The ItemDTO containing item information.
     * @param quantity The quantity of the item to add.
     */
    public void addItem(ItemDTO itemInfo, int quantity) {
        System.out.println("Sale: Received call to add item: ID=" + itemInfo.getItemID() +
                           ", Desc='" + itemInfo.getDescription() + "'" +
                           ", Qty=" + quantity);

        SaleItem saleItem = new SaleItem(itemInfo, quantity);
        this.items.add(saleItem);

        updateTotalsForItem(itemInfo, quantity); 
    }

    private void updateTotalsForItem(ItemDTO itemInfo, int quantity) {
        Amount itemPriceBeforeTax = itemInfo.getPrice();
    
        // Update running total
        Amount itemsTotalPrice = itemPriceBeforeTax.multiply(quantity);
        this.runningTotalBeforeTax = this.runningTotalBeforeTax.plus(itemsTotalPrice);
        System.out.println("Sale: Updated running total (pre-tax, pre-discount): " + this.runningTotalBeforeTax);
    
        // Update VAT
        calculateAndAddVAT(itemPriceBeforeTax, itemInfo.getTax(), quantity);
        System.out.println("Sale: Updated total VAT: " + this.totalVAT);
    }

    private void calculateAndAddVAT(Amount itemPriceBeforeTax, double taxPercentage, int quantity) {
        double taxRate = taxPercentage / 100.0;
        Amount itemVAT = new Amount(itemPriceBeforeTax.getAmount() * taxRate);
        Amount itemsTotalVAT = itemVAT.multiply(quantity);
        this.totalVAT = this.totalVAT.plus(itemsTotalVAT);
    }


    /*
     * Gets a snapshot of the current sale state for discount calculation or external updates.
     *
     * @return A SaleInfoDTO containing relevant sale data.
     */
    public SaleInfoDTO getSaleInfoForDiscount() { // Renamed for clarity, used by multiple callers now
        System.out.println("Sale: Generating SaleInfoDTO. Current running total: " + this.runningTotalBeforeTax + ", VAT: " + this.totalVAT);
        return createSaleInfoDTO(); // Use helper method
    }

    // Helper method to create SaleInfoDTO
    private SaleInfoDTO createSaleInfoDTO() {
        List<String> itemStrings = getFormattedItemStrings(); 

        return new SaleInfoDTO(this.runningTotalBeforeTax, itemStrings, this.totalVAT);
    }


    /*
     * Applies a discount to the sale based on the provided DiscountInfoDTO.
     * This method is called by the Controller after checking for applicable discounts.
     *
     * @param discountInfo The DiscountInfoDTO containing discount details.
     */
    public void applyDiscount(DiscountInfoDTO discountInfo) {
        if (discountInfo != null) {
            System.out.println("Sale: Applying discount: Type=" + discountInfo.getDiscountType() +
                               ", Percentage=" + discountInfo.getDiscountPercentage() + "%" +
                               ", Amount=" + discountInfo.getDiscountAmount());

            Amount totalBeforeDiscount = this.runningTotalBeforeTax; 
            Amount totalAfterDiscount = calculateTotalAfterDiscount(discountInfo);
            System.out.println("Sale: Original running total: " + totalBeforeDiscount);
            System.out.println("Sale: Calculated total after discount: " + totalAfterDiscount);

            this.runningTotalBeforeTax = totalAfterDiscount;

        } else {
            System.out.println("Sale: No discount to apply.");
        }
    }


    private Amount calculateTotalAfterDiscount(DiscountInfoDTO discountInfo) {

        if (discountInfo == null) {
            return this.runningTotalBeforeTax;
        }

        return applySpecificDiscount(discountInfo);
    }

    private Amount applySpecificDiscount(DiscountInfoDTO discountInfo) {
        if ("Percentage".equals(discountInfo.getDiscountType())) {
            return calculateAfterPercentageDiscount(discountInfo.getDiscountPercentage());
        } else if ("Amount".equals(discountInfo.getDiscountType())) {
            return calculateAfterAmountDiscount(discountInfo.getDiscountAmount());
        } else {
            System.err.println("Sale WARNING: Unknown discount type encountered: " + discountInfo.getDiscountType());
            return this.runningTotalBeforeTax; 
        }
    }

    private Amount calculateAfterPercentageDiscount(int percentage) {
        double discountFactor = (double) percentage / 100.0;
        Amount discountAmount = new Amount(this.runningTotalBeforeTax.getAmount() * discountFactor);
        return this.runningTotalBeforeTax.minus(discountAmount);
    }

    private Amount calculateAfterAmountDiscount(Amount discountAmount) {
        return this.runningTotalBeforeTax.minus(discountAmount); 
    }


    /*
     * Calculates and stores the final total price for the sale, including tax.
     * This should be called by the Controller's endSale method.
     *
     * @return The final total amount including VAT.
     */
    public Amount calculateAndGetFinalTotal() {
        this.finalTotalWithTax = this.runningTotalBeforeTax.plus(this.totalVAT);
        System.out.println("Sale: Calculating final total: Running Total (after discount) " + this.runningTotalBeforeTax +
                           " + Total VAT " + this.totalVAT + " = Final Total " + this.finalTotalWithTax);
        return this.finalTotalWithTax;
    }


    /*
     * Processe the payment, calculate change, and generate receipt data.
     *
     * @param paidAmount The amount of money received from the customer.
     * @return A ReceiptDTO containing all details for the receipt.
     * @throws IllegalStateException if the final total hasn't been calculated yet (endSale not called).
     * @throws IllegalArgumentException if the paid amount is less than the total amount.
     */
    public ReceiptDTO processPaymentAndGetReceiptDetails(Amount paidAmount) {
        ensureSaleIsEnded();
        System.out.println("Sale: Processing payment. Amount paid: " + paidAmount + ", Total due: " + this.finalTotalWithTax);

        this.amountPaidByCustomer = paidAmount;
        this.changeToCustomer = calculateChange(paidAmount); 

        System.out.println("Sale: Calculated change: " + this.changeToCustomer);

        return createReceiptDTO(); 
    }

    private void ensureSaleIsEnded() {
        if (this.finalTotalWithTax == null) {
            throw new IllegalStateException("Cannot process payment before endSale is called and final total is calculated.");
        }
    }

    /*
     * Calculates the change to be given back to the customer.
     * Assumes finalTotalWithTax has been calculated.
     *
     * @param paidAmount The amount paid by the customer.
     * @return The calculated change.
     * @throws IllegalArgumentException if paidAmount is less than finalTotalWithTax.
     */
    private Amount calculateChange(Amount paidAmount) {
        if (isPaymentInsufficient(paidAmount)) { 
             throw new IllegalArgumentException("Paid amount (" + paidAmount + ") is less than the total amount due (" + this.finalTotalWithTax + ").");
        }
        return paidAmount.minus(this.finalTotalWithTax);
    }

    private boolean isPaymentInsufficient(Amount paidAmount) {
        return paidAmount.getAmount() < this.finalTotalWithTax.getAmount();
    }

    /*
     * Creates the ReceiptDTO containing all necessary information.
     */
    private ReceiptDTO createReceiptDTO() {
        LocalDateTime saleCompleteTime = LocalDateTime.now(); 
        List<String> itemStrings = getFormattedItemStrings(); 


        // TODO: Add discount info to receipt if needed

        return new ReceiptDTO(saleCompleteTime,
                              itemStrings,
                              this.finalTotalWithTax,
                              this.amountPaidByCustomer,
                              this.changeToCustomer);
    }

    private List<String> getFormattedItemStrings() {
        return this.items.stream()
                         .map(SaleItem::formatForDTO)
                         .collect(Collectors.toList());
    }

    /*
     * Gets the sale information needed for updating the accounting system.
     * Corresponds to step 1.4 in the sequence diagram.
     *
     * @return A SaleInfoDTO containing relevant accounting data.
     */
    public SaleInfoDTO getSaleInfoForAccounting() {
        System.out.println("Sale: Generating SaleInfoDTO for Accounting update.");
        return createSaleInfoDTO(); 
    }

    /*
     * Gets the sale information needed for updating the inventory system.
     * Corresponds to step 1.6 in the sequence diagram.
     *
     * @return A SaleInfoDTO containing relevant inventory data (e.g., items sold).
     */
    public SaleInfoDTO getSaleInfoForInventory() {
        System.out.println("Sale: Generating SaleInfoDTO for Inventory update.");
        return createSaleInfoDTO(); 
    }

    /*
     * Gets the final total amount including tax, after it has been calculated by endSale.
     *
     * @return The final total amount, or null if endSale has not been called.
     */
    public Amount getFinalTotalWithTax() {
        return finalTotalWithTax;
    }

}