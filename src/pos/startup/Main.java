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
        System.out.println("Starting the application...");
        InventorySystem invSys = new InventorySystem();
        DiscountDatabase discDB = new DiscountDatabase();
        AccountingSystem accSys = new AccountingSystem();
        Printer printer = new Printer();

        Controller  contr = new Controller(invSys, discDB, accSys, printer);
        View view = new View(contr);
        view.runFakeExecution();
    }
}
