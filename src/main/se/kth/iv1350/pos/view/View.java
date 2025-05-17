package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.controller.Controller;
import se.kth.iv1350.pos.integration.ItemNotFoundException; // Added import
import se.kth.iv1350.pos.model.Amount; 


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

        Amount amountPaid = new Amount(50.00);
        makePayment(amountPaid); 
        System.out.println("Change to give the customer: " + contr.getChange());
    }

    /*
     * Private helper method to simulate a user entering item details.
     * Calls the controller's enterItem method and handles ItemNotFoundException.
     */
    private void enterItem(int itemID, int quantity) {
        System.out.println(); 
        System.out.println("Add " + quantity + " item(s) with item id " + itemID + ":");
        try {
            contr.enterItem(itemID, quantity);
        } catch (ItemNotFoundException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }

    /*
     * Private helper method to simulate a user requesting a discount.
     * Calls the controller's requestDiscount method.
     */
    private void requestDiscount(int customerID) {
        System.out.println();
        System.out.println("Request discount for customer ID: " + customerID);
        contr.requestDiscount(customerID);
    }

    /*
     * Private helper method to simulate a user making a payment.
     * Calls the controller's makePayment method.
     *
     * @param paidAmount The amount paid by the customer.
     */
    private void makePayment(Amount paidAmount) {
        System.out.println();
        System.out.println("Pay: " + paidAmount);
        contr.makePayment(paidAmount);
    }

}