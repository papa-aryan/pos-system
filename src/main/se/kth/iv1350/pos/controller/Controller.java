package se.kth.iv1350.pos.controller;

import se.kth.iv1350.pos.integration.AccountingSystem;
import se.kth.iv1350.pos.integration.DatabaseFailureException;
import se.kth.iv1350.pos.integration.DiscountDatabase;
import se.kth.iv1350.pos.integration.InventorySystem;
import se.kth.iv1350.pos.integration.Printer;
import se.kth.iv1350.pos.integration.ItemDTO;
import se.kth.iv1350.pos.integration.ItemNotFoundException;
import se.kth.iv1350.pos.model.Sale;
import se.kth.iv1350.pos.model.SaleInfoDTO;
import se.kth.iv1350.pos.integration.DiscountInfoDTO;
import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.ReceiptDTO;
import se.kth.iv1350.pos.model.SaleObserver; 
import java.util.ArrayList; 
import java.util.List; 


/*
 * This is the application's controller class. All calls to the model pass through here.
 */
public class Controller {
    private InventorySystem invSys;
    private DiscountDatabase discDB;
    private AccountingSystem accSys;
    private Printer printer;
    private List<SaleObserver> saleObservers = new ArrayList<>(); 

    // Package private to allow access from test files
    Sale sale;

    /*
     * The constructor initializes the controller with the necessary components.
     * 
     * @param invSys The inventory system used for managing products
     * @param discDB The discount database used for managing discounts
     * @param accSys The accounting system used for managing transactions
     * @param printer The printer used for printing receipts
     */
    public Controller(InventorySystem invSys, DiscountDatabase discDB, 
                        AccountingSystem accSys, Printer printer) {
        this.invSys = invSys;
        this.discDB = discDB;
        this.accSys = accSys;
        this.printer = printer;
    }

    /*
     * Starts a new sale. This method must be called before any other methods.
     *   *cant register an item before a sale is started for example.*
     */
    public void startSale() {
        sale = new Sale();
        // Register observers with the new Sale instance
        for (SaleObserver obs : saleObservers) {
            sale.addSaleObserver(obs);
        }
    }

    /*
     * Enters an item into the current sale.
     *
     * @param itemID The unique identifier of the item to enter.
     * @param quantity The quantity of the item to enter.
     * @throws ItemNotFoundException If the itemID does not correspond to a known item.
     */
    public void enterItem(int itemID, int quantity) throws ItemNotFoundException, OperationFailedException {
        if (!isSaleStarted()) {
            // System.err.println("Error: Sale has not been started. Cannot enter item.");
            return;
        }

        if (!isValidQuantity(quantity, itemID)) {
             // System.err.println("Error: Invalid quantity (" + quantity + ") for item ID " + itemID + ".");
             return;
        }

        try {
            ItemDTO itemInfo = invSys.getItemInfo(itemID);

            sale.addItem(itemInfo, quantity);
            // System.out.println("Successfully added " + quantity + " of item " + itemID); // For debugging
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (DatabaseFailureException e) {
            // This is the new catch block to handle DatabaseFailureException
            // and wrap it in an OperationFailedException.
            throw new OperationFailedException("Operation failed due to a database error.", e);
        }
    }

    /*
     * Requests a discount for the specified customer ID.
     * Retrieves sale info, checks for discounts, and applies it to the sale.
     *
     * @param customerID The ID of the customer requesting the discount.
     */
    public void requestDiscount(int customerID) {
        if (!isSaleStarted()) {
            return;
        }

        SaleInfoDTO saleInfo = sale.getSaleInfoForDiscount();

        DiscountInfoDTO discountInfo = discDB.getDiscount(customerID, saleInfo);

        sale.applyDiscount(discountInfo);
    }

    /*
     * Ends the current sale and retrieves the final total amount including tax.
     * (Further steps like payment and receipt printing would follow).
     */
    public void endSale() {
        if (!isSaleStarted()) {
            return;
        }

        sale.calculateAndGetFinalTotal();
    }

     /*
     * Processes the payment received from the customer, generates and prints a receipt,
     * and updates external systems (accounting, inventory).
     * This method assumes endSale() has already been called.
     * Corresponds to sequence diagram for makePayment.
     *
     * @param paidAmount The amount of money paid by the customer.
     */
    public void makePayment(Amount paidAmount) {
        if (!isSaleStarted()) {
            System.err.println("Controller: Sale has not been started. Cannot make payment.");
            return;
        }
        if (!isSaleEnded()) {
            System.err.println("Controller: Sale has not been ended. Cannot make payment.");
             return;
        }

        try {
            ReceiptDTO receiptData = sale.processPaymentAndGetReceiptDetails(paidAmount); 

            printer.printReceipt(receiptData);

            SaleInfoDTO saleInfoAccounting = sale.getSaleInfoForAccounting(); 

            accSys.updateAccounting(saleInfoAccounting);

            SaleInfoDTO saleInfoInventory = sale.getSaleInfoForInventory(); 

            invSys.updateInventory(saleInfoInventory);
        } catch (IllegalArgumentException e) {
            System.err.println("Controller: Payment failed - " + e.getMessage());
            // In a real application, a more robust logging mechanism would be used.
        }
    }

    private boolean isSaleStarted() {
        if (sale == null) {
            return false;
        }
        return true;
    }

    private boolean isValidQuantity(int quantity, int itemID) {
        if (quantity <= 0) {
             return false;
        }
        return true;
    }
    
    private boolean isSaleEnded() {
        if (sale.getFinalTotalWithTax() == null) {
            return false;
       }
       return true;
   }

    /**
     * Adds a SaleObserver to be notified of completed sales.
     * @param observer The observer to add.
     */
    public void addSaleObserver(SaleObserver observer) {
        if (observer != null) {
            this.saleObservers.add(observer);
        }
    }

    /*
     * Gets the calculated change amount from the current sale.
     *
     * @return The change amount, or zero if sale is not available or change not calculated.
     */
    public Amount getChange() {
        if (isSaleStarted()) {
            return sale.getChange();
        } else {
            return new Amount(0);
        }
    }

}

