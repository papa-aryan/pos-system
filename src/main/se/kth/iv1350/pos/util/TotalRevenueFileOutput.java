package se.kth.iv1350.pos.util;

import se.kth.iv1350.pos.model.Amount;
import se.kth.iv1350.pos.model.SaleObserver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes the total income from all sales to a file.
 * This class is an observer that updates when a sale is paid.
 */
public class TotalRevenueFileOutput implements SaleObserver {
    private static final String LOG_FILE_NAME = "total-revenue-log.txt";
    private Amount totalRevenue;
    private PrintWriter logStream;

    /**
     * Creates a new instance, initializing total revenue to zero and setting up the log file.
     */
    public TotalRevenueFileOutput() {
        this.totalRevenue = new Amount(0);
        try {
            logStream = new PrintWriter(new FileWriter(LOG_FILE_NAME, true), true);
        } catch (IOException ioe) {
            throw new RuntimeException("CRITICAL ERROR: CANNOT INITIALIZE TOTAL REVENUE FILE LOGGER.", ioe);
        }
    }

    /**
     * Called when a sale is paid. Adds the sale's total amount to the running total revenue
     * and writes the updated total revenue to the log file with a timestamp.
     *
     * @param paidSaleAmount The total amount of the completed sale.
     */
    @Override
    public void newSaleWasPaid(Amount paidSaleAmount) {
        this.totalRevenue = this.totalRevenue.plus(paidSaleAmount);
        if (logStream != null) {
            StringBuilder logEntry = new StringBuilder();
            logEntry.append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            logEntry.append(": Total Revenue: ");
            logEntry.append(this.totalRevenue.toString());
            logStream.println(logEntry.toString());
        } else {
            System.err.println("Total Revenue File Logger not initialized. Cannot log revenue: " + this.totalRevenue);
        }
    }

    /**
     * Closes the log stream. Is called when the application is shutting down.
     */
    public void closeLogger() {
        if (logStream != null) {
            logStream.close();
        }
    }
}
