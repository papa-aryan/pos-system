package pos.integration;

import pos.model.SaleInfoDTO;
import pos.model.Amount; 

public class DiscountDatabase {

    /*
     * Retrieves discount information based on customer ID and current sale state.
     *
     * @param customerID The ID of the customer requesting the discount.
     * @param saleInfo   A DTO containing information about the current sale.
     * @return A DiscountInfoDTO containing discount details, or null if no discount applies.
     */
    public DiscountInfoDTO getDiscount(int customerID, SaleInfoDTO saleInfo) {
        System.out.println("DiscountDatabase: Checking discounts for customer ID: " + customerID);
        
        if (customerID == 1234) {
            System.out.println("DiscountDatabase: Found 10% discount for customer " + customerID);
            return new DiscountInfoDTO(new Amount(0), 10, "Percentage");
        } else {
            System.out.println("DiscountDatabase: No discount found for customer " + customerID);
            return null; 
        }
        
        // Example for amount-based discount:
        // if (customerID == 5678) {
        //     return new DiscountInfoDTO(new Amount(5.00), 0, "Amount"); // 5.00 (amount) discount
        // }
    }

}