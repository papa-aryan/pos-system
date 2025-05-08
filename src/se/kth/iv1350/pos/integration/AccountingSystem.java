package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleInfoDTO; 

/*
 * Represents the external accounting system.
 */
public class AccountingSystem {

    /*
     * Updates the external accounting system with details of the completed sale.
     *
     * @param saleInfoAccounting A DTO containing financial information about the sale.
     */
    public void updateAccounting(SaleInfoDTO saleInfoAccounting) {
        // System.out.println("AccountingSystem: Received request to update accounting.");
        // System.out.println("AccountingSystem: Sale details:");
        // System.out.println("  - Running Total (at time of logging): " + saleInfoAccounting.getRunningTotal());
        // System.out.println("  - Total VAT: " + saleInfoAccounting.getTotalVAT());
        // // In a real system, this would record the transaction details.
        // System.out.println("AccountingSystem: Accounting update simulation complete.");
    }
}