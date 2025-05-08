package se.kth.iv1350.pos.integration;

import se.kth.iv1350.pos.model.SaleInfoDTO;
import se.kth.iv1350.pos.model.Amount; 

public class DiscountDatabase {

    /*
     * Retrieves discount information based on customer ID and current sale state.
     *
     * @param customerID The ID of the customer requesting the discount.
     * @param saleInfo   A DTO containing information about the current sale.
     * @return A DiscountInfoDTO containing discount details, or null if no discount applies.
     */
    public DiscountInfoDTO getDiscount(int customerID, SaleInfoDTO saleInfo) {
        
        if (customerID == 1234) {
            return new DiscountInfoDTO(new Amount(0), 10, "Percentage");
        } else {
            // No discount found for customer
            return null; 
        }
    }

}