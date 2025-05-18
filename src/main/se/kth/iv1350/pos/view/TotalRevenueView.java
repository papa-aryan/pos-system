package se.kth.iv1350.pos.view;

import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.SaleObserver;

/**
 * Shows the total income from all sales on the user interface (System.out).
 * This class is an observer that updates when a sale is paid.
 */
public class TotalRevenueView implements SaleObserver {
    private Amount totalRevenue;

    /**
     * Creates a new instance, initializing total revenue to zero.
     */
    public TotalRevenueView() {
        this.totalRevenue = new Amount(0);
    }

    /**
     * Called when a sale is paid. Adds the sale's total amount to the running total revenue
     * and prints the updated total revenue to standard output.
     *
     * @param paidSaleAmount The total amount of the completed sale.
     */
    @Override
    public void newSaleWasPaid(Amount paidSaleAmount) {
        this.totalRevenue = this.totalRevenue.plus(paidSaleAmount);
        System.out.println("--- Total Revenue Update ---");
        System.out.println("Current Total Revenue: " + this.totalRevenue);
        System.out.println("--------------------------");
    }
}
