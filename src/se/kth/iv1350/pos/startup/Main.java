package se.kth.iv1350.pos.startup;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.view.View;

import se.kth.iv1350.pos.integration.AccountingSystem;
import se.kth.iv1350.pos.integration.DiscountDatabase;
import se.kth.iv1350.pos.integration.InventorySystem;
import se.kth.iv1350.pos.integration.Printer;

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
        DiscountDatabase discDB = new DiscountDatabase();
        AccountingSystem accSys = new AccountingSystem();
        Printer printer = new Printer();

        Controller  contr = new Controller(invSys, discDB, accSys, printer);
        View view = new View(contr);
        view.runFakeExecution();
    }
}
