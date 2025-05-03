package pos.controller;

import pos.integration.AccountingSystem;
import pos.integration.DiscountDatabase;
import pos.integration.InventorySystem;
import pos.integration.Printer;
import pos.integration.ItemDTO;
import pos.model.Sale;
import pos.model.SaleInfoDTO;
import pos.integration.DiscountInfoDTO;
import pos.model.Amount;


/*
 * This is the application's controller class. All calls to the model pass through here.
 */
public class Controller {
    private InventorySystem invSys;
    private DiscountDatabase discDB;
    private AccountingSystem accSys;
    private Printer printer;

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
        System.out.println("Controller: Sale started.");
    }

    /*
     * Enters an item into the current sale.
     *
     * @param itemID The unique identifier of the item to enter.
     * @param quantity The quantity of the item to enter.
     */
    public void enterItem(int itemID, int quantity) {

        // Check if sale has been started
        if (sale == null) {
            System.err.println("Controller ERROR: Cannot enter item before starting a sale."); // Good practice to log error
            return;
        }
    
        // Add quantity validation
        if (quantity <= 0) {
             System.err.println("Controller ERROR: Quantity must be positive. Item ID " + itemID + " NOT added.");
             return; // Stop processing if quantity is invalid
        }
        
        ItemDTO itemInfo = invSys.getItemInfo(itemID);

        if (itemInfo != null) {
            sale.addItem(itemInfo, quantity);
        } else {
            System.err.println("Controller ERROR: Item ID " + itemID + " not found in inventory. Item NOT added to sale.");
        }
    }

    /*
     * Requests a discount for the specified customer ID.
     * Retrieves sale info, checks for discounts, and applies it to the sale.
     *
     * @param customerID The ID of the customer requesting the discount.
     */
    public void requestDiscount(int customerID) {
        System.out.println("Controller: Received request for discount for customer ID: " + customerID);
        if (sale == null) {
            System.err.println("Controller ERROR: Cannot request discount before starting a sale.");
            return;
        }

        // 1.1: Get Sale Info from Sale
        SaleInfoDTO saleInfo = sale.getSaleInfoForDiscount();

        // 1.2: Get Discount from DiscountDatabase
        DiscountInfoDTO discountInfo = discDB.getDiscount(customerID, saleInfo);

        // 1.3: Apply Discount to Sale
        sale.applyDiscount(discountInfo);

        // 1.4: Get Total After Discount (This step might be internal to Sale or called later)
        // The diagram shows Sale calling this on itself, which is unusual.
        // Often, the Controller would ask the Sale for the updated total *after* applying the discount.
        // For now, the application logic is handled within sale.applyDiscount's placeholder.
        System.out.println("Controller: Discount request processed.");
    }

    /*
     * Ends the current sale and retrieves the final total amount including tax.
     * (Further steps like payment and receipt printing would follow).
     */
    public void endSale() {
        System.out.println("Controller: Received request to end sale.");
        // IS THIS CONDITION EVEN POSSIBLE? SALE MUST BE STARTED BEFORE ENDING?!
        if (sale == null) {
            System.err.println("Controller ERROR: Cannot end sale before starting one.");
            return;
        }

        Amount finalTotal = sale.getTotalWithTax();

        System.out.println("Controller: Sale ended. Final total (incl. tax): " + finalTotal);
        // TODO: Implement payment processing and receipt generation later
    }

}

