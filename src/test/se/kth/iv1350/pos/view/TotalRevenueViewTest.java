package se.kth.iv1350.pos.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.pos.model.Amount;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

public class TotalRevenueViewTest {
    private TotalRevenueView instanceToTest;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        instanceToTest = new TotalRevenueView();
        System.setOut(new PrintStream(outContent)); 
    }

    @AfterEach
    void tearDown() {
        instanceToTest = null;
        System.setOut(originalOut); 
        outContent.reset();
    }

    @Test
    void testNewSaleWasPaidUpdatesTotalRevenue() {
        Amount firstSaleAmount = new Amount(100.0);
        instanceToTest.newSaleWasPaid(firstSaleAmount);
        String output = outContent.toString();
        assertTrue(output.contains("Current Total Revenue: 100.00"), 
                   "Output should contain the updated total revenue after one sale.");

        outContent.reset(); 

        Amount secondSaleAmount = new Amount(50.50);
        instanceToTest.newSaleWasPaid(secondSaleAmount);
        output = outContent.toString();
        assertTrue(output.contains("Current Total Revenue: 150.50"), 
                   "Output should reflect the sum of both sales.");
    }

    @Test
    void testNewSaleWasPaidWithZeroAmount() {
        Amount zeroAmount = new Amount(0.0);
        instanceToTest.newSaleWasPaid(zeroAmount);
        String output = outContent.toString();
        assertTrue(output.contains("Current Total Revenue: 0.00"), 
                   "Output should show 0.00 if a zero amount sale is processed initially.");
        
        outContent.reset();
        Amount initialAmount = new Amount(25.0);
        instanceToTest.newSaleWasPaid(initialAmount); 
        outContent.reset(); 

        instanceToTest.newSaleWasPaid(zeroAmount); 
        output = outContent.toString();
        assertTrue(output.contains("Current Total Revenue: 25.00"), 
                   "Adding a zero amount sale should not change the existing total revenue.");
    }
}
