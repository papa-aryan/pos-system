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
        // In a real system, this would record the transaction details.
    }
}