package pos.integration;

import pos.model.ReceiptDTO;

/*
 * Represents the printer hardware.
 */
public class Printer {

    /*
     * Prints the specified receipt data.
     *
     * @param receiptData The DTO containing all information for the receipt.
     */
    public void printReceipt(ReceiptDTO receiptData) {
        System.out.println("Printing receipt...");
        System.out.println("\n-------------------- RECEIPT START --------------------\n");
        System.out.println(receiptData.toString());
        System.out.println("-------------------- RECEIPT END ----------------------\n");
    }
}