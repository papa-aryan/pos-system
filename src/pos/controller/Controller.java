package pos.controller;

import pos.integration.AccountingSystem;
import pos.integration.DiscountDatabase;
import pos.integration.InventorySystem;
import pos.integration.Printer;
import pos.integration.ItemDTO;
import pos.model.Sale;

/*
 * This is the application's controller class. All calls to the model pass through here.
 */
public class Controller {
    private InventorySystem invSys;
    private DiscountDatabase discDB;
    private AccountingSystem accSys;
    private Printer printer;

    private Sale sale;

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
            return; 
        }
        
        ItemDTO itemInfo = invSys.getItemInfo(itemID);

        if (itemInfo != null) {
            sale.addItem(itemInfo, quantity);
        } else {
            System.err.println("Controller ERROR: Item ID " + itemID + " not found in inventory. Item NOT added to sale.");
        }
    }

    // Other methods from class diagram (endSale, requestDiscount, etc.) are not needed yet.
}

