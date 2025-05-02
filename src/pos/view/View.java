package pos.view;

import pos.controller.Controller;

/*
 * This is a placeholder for the View class in the POS system. It contains hardcoded 
 * execution calls to all system operations in the controller. 
 */
public class View {
    private Controller contr;

    /*
     * Creates a new instance that uses constructor for calls to other layers.
     * 
     * @param contr The controller used for all calls to other layers.
     */
    public View(Controller contr) {
        this.contr = contr;
    }

    public void runFakeExecution() {
        // Step 1: Create Controller
        contr.startSale(); // Start a new sale
        // Step 2: Register items, discounts, and payment (hardcoded for now)
        // contr.registerItem("item1", 10.0, 2); // Example item registration
        // contr.registerDiscount("discount1", 5.0); // Example discount registration
        // contr.processPayment(20.0); // Example payment processing
        // Step 3: Print receipt (hardcoded for now)
        // contr.printReceipt(); // Example receipt printing
        System.out.println("A new sale has been started!!");
    }
}
