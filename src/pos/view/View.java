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

    /*
     * Simulates the flow of a sale by making hardcoded calls to the controller.
     */
    public void runFakeExecution() {

        System.out.println("View: Starting fake execution of sale...");
        contr.startSale(); 

        // Simulate entering item 101 (should be found)
        enterItem(101, 2);

        // Simulate entering item 102 (should be found)
        enterItem(102, 1);

        // Simulate entering item 999 (should NOT be found)
        enterItem(999, 1);

        // Simulate entering item with invalid quantity
        enterItem(102, 0);
        enterItem(101, -1);

        // Simulate requesting a discount for customer 1234 (should get 10%)
        requestDiscount(1234);

        // Simulate requesting a discount for customer 5678 (should get none)
        requestDiscount(5678);

        contr.endSale(); 
        System.out.println("View: Fake sale execution finished.");

        // TODO: Add calls for discounts, payment, and printing receipt later
    }

    /*
     * Private helper method to simulate a user entering item details.
     * Calls the controller's enterItem method.
     *
     * @param itemID The ID of the item entered.
     * @param quantity The quantity of the item entered.
     */
    private void enterItem(int itemID, int quantity) {
        System.out.println("-------------------------------------"); // Separator
        System.out.println("View: Attempting to enter item ID: " + itemID + ", Quantity: " + quantity);
        contr.enterItem(itemID, quantity);
    }

    /*
     * Private helper method to simulate a user requesting a discount.
     * Calls the controller's requestDiscount method.
     *
     * @param customerID The ID of the customer.
     */
    private void requestDiscount(int customerID) {
        System.out.println("-------------------------------------"); // Separator
        System.out.println("View: Attempting to request discount for customer ID: " + customerID);
        contr.requestDiscount(customerID);
        // TODO: Display discount application result from Controller later
    }

}