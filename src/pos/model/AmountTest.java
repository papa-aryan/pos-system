package pos.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AmountTest {

    // Using a small tolerance for double comparisons in assertions
    private static final double TOLERANCE = 0.001;

    @Test
    void testGetAmount() {
        double expectedAmount = 10.50;
        Amount instance = new Amount(expectedAmount);
        double result = instance.getAmount();
        assertEquals(expectedAmount, result, TOLERANCE, "getAmount should return the value passed to constructor.");
    }

    @Test
    void testMinus() {
        Amount amount1 = new Amount(10.50);
        Amount amount2 = new Amount(5.20);
        Amount expected = new Amount(5.30);
        Amount result = amount1.minus(amount2);
        assertEquals(expected, result, "10.50 minus 5.20 should be 5.30.");
    }

    @Test
    void testMultiply() {
        Amount amount = new Amount(10.50);
        int factor = 3;
        Amount expected = new Amount(31.50);
        Amount result = amount.multiply(factor);
        assertEquals(expected, result, "10.50 multiplied by 3 should be 31.50.");
    }

    @Test
    void testPlus() {
        Amount amount1 = new Amount(10.50);
        Amount amount2 = new Amount(5.20);
        Amount expected = new Amount(15.70);
        Amount result = amount1.plus(amount2);
        assertEquals(expected, result, "10.50 plus 5.20 should be 15.70.");
    }

    @Test
    void testEqualsSameValue() {
        Amount amount1 = new Amount(15.70);
        Amount amount2 = new Amount(15.70);
        assertTrue(amount1.equals(amount2), "Amounts with the same value should be equal.");
    }

    @Test
    void testEqualsDifferentValue() {
        Amount amount1 = new Amount(15.70);
        Amount amount2 = new Amount(15.71);
        assertFalse(amount1.equals(amount2), "Amounts with different values should not be equal.");
    }

     @Test
    void testEqualsNull() {
        Amount amount1 = new Amount(15.70);
        assertFalse(amount1.equals(null), "Amount should not be equal to null.");
    }

     @Test
    void testEqualsDifferentClass() {
        Amount amount1 = new Amount(15.70);
        Object other = new Object();
        assertFalse(amount1.equals(other), "Amount should not be equal to an object of a different class.");
    }
}