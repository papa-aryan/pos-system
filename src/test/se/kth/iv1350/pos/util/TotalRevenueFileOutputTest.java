package se.kth.iv1350.pos.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.model.Amount;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class TotalRevenueFileOutputTest {
    private TotalRevenueFileOutput instanceToTest;
    private static final String ACTUAL_LOG_FILE_NAME = "total-revenue-log.txt";

    @BeforeEach
    void setUp() throws IOException {
        Files.deleteIfExists(Paths.get(ACTUAL_LOG_FILE_NAME));
        instanceToTest = new TotalRevenueFileOutput(); 
    }

    @AfterEach
    void tearDown() throws IOException {
        if (instanceToTest != null) {
            instanceToTest.closeLogger(); 
        }
        Files.deleteIfExists(Paths.get(ACTUAL_LOG_FILE_NAME));
    }

    @Test
    void testNewSaleWasPaidWritesToFileAndAccumulates() throws IOException {
        // First sale
        Amount firstSaleAmount = new Amount(120.50);
        instanceToTest.newSaleWasPaid(firstSaleAmount);

        File logFile = new File(ACTUAL_LOG_FILE_NAME);
        assertTrue(logFile.exists(), "Log file should be created after first sale.");

        String contentAfterFirstSale = readFileContent(logFile);
        assertTrue(contentAfterFirstSale.contains("Total Revenue: 120.50"), 
                   "Log file should contain the correct total revenue after one sale. Content:\n" + contentAfterFirstSale);

        // Second sale (accumulation on the same instance)
        Amount secondSaleAmount = new Amount(75.25);
        instanceToTest.newSaleWasPaid(secondSaleAmount);

        String contentAfterSecondSale = readFileContent(logFile);
        assertTrue(contentAfterSecondSale.contains("Total Revenue: 195.75"), 
                   "Log file should contain the accumulated total revenue after two sales. Content:\n" + contentAfterSecondSale);
        
        // Verify that the second entry is new and doesn't just overwrite or match the first one if amounts were same
        long lineCount = Files.lines(Paths.get(ACTUAL_LOG_FILE_NAME)).count();
        assertEquals(2, lineCount, "There should be two log entries for two sales.");
    }

    @Test
    void testFileLoggingWithZeroAmount() throws IOException {
        instanceToTest.newSaleWasPaid(new Amount(0.00));

        File logFile = new File(ACTUAL_LOG_FILE_NAME);
        assertTrue(logFile.exists(), "Log file should be created even for zero amount.");
        String content = readFileContent(logFile);
        assertTrue(content.contains("Total Revenue: 0.00"), 
                   "Log file should correctly log zero total revenue. Content:\n" + content);
    }

    private String readFileContent(File file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file.getPath())));
    }
}
