package pos.startup;

import pos.controller.Controller;
import pos.view.View;

import pos.integration.AccountingSystem;
import pos.integration.DiscountDatabase;
import pos.integration.InventorySystem;
import pos.integration.Printer;

/*
 * This class starts the application. 
 */
public class Main {
    /*
     * The main method used to start the entire application.
     * 
     * @param args The application does not take any command line arguments
     */
    public static void main(String[] args) {
         InventorySystem invSys = new InventorySystem();
        // Step 2: Create DiscountDatabase
        DiscountDatabase discDB = new DiscountDatabase();
        // Step 3: Create AccountingSystem
        AccountingSystem accSys = new AccountingSystem();
        // Step 4: Create Printer
        Printer printer = new Printer();

        Controller  contr = new Controller(invSys, discDB, accSys, printer);
        new View(contr);
    }
}
