package se.kth.iv1350.pos.model;

/**
 * A listener interface for receiving notifications about completed sales.
 * Implementers of this interface are observers that will be updated when a sale is paid.
 */
public interface SaleObserver {
    /**
     * Invoked when a sale has been successfully paid.
     *
     * @param totalSaleAmount The total amount of the completed sale.
     */
    void newSaleWasPaid(Amount totalSaleAmount);
}
