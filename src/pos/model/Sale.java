package pos.model;

import java.time.LocalTime;

/*
 * One single sale made by one customer and payed with one payment.
 */
public class Sale {
    private LocalTime saleTime;
    private Receipt receipt;

    /*
     * Creates a new instance and saves the time of the sale.
     */
    public Sale() {
        setTimeOfSale();
    }

    private void setTimeOfSale() {
        saleTime = LocalTime.now();
        receipt = new Receipt();
    }
}
