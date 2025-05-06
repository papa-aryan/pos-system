package pos.model;

import java.util.Objects;

/*
 * Represents an amount of money. This is a value object.
 */
public final class Amount {
    private final double amount;

    /*
     * Creates a new instance representing the specified amount.
     *
     * @param amount The amount in the smallest monetary unit (e.g., cents, öre).
     */
    public Amount(double amount) {
        this.amount = amount;
    }

    /*
     * Gets the numerical value of this amount.
     *
     * @return The amount in the smallest monetary unit.
     */
    public double getAmount() {
        return amount;
    }

    // Arithmetic Operations

    /*
     * Subtracts another amount from this amount.
     *
     * @param other The amount to subtract.
     * @return A new Amount instance representing the difference.
     */
    public Amount minus(Amount other) {
        return new Amount(this.amount - other.amount);
    }

    /*
     * Adds another amount to this amount.
     *
     * @param other The amount to add.
     * @return A new Amount instance representing the sum.
     */
    public Amount plus(Amount other) {
        return new Amount(this.amount + other.amount);
    }

    /*
     * Multiplies this amount by a specified factor.
     * Useful for calculating price based on quantity.
     *
     * @param factor The multiplication factor.
     * @return A new Amount instance representing the product.
     */
    public Amount multiply(int factor) {
        return new Amount(this.amount * factor);
    }

    /*
     * Returns a string representation of the amount.
     *
     * @return The amount formatted as a string (e.g., "15.70").
     */
    public String toString() {
        return String.format("%.2f", amount);
    }


    /* 
     * Checks if this amount is equal to another object.
     * 
     * @param The object to compare with.
     * @return true if the object is an Amount with the same value, false otherwise.
     */
    public boolean equals(Object obj) {
        if (isSameValue(obj)) {
            return true;
        }
        if (isDifferentTypeOrNull(obj)) {
            return false;
        }        
        Amount other = (Amount) obj;
        return hasSameNumericValue(other);
    }

    private boolean isSameValue(Object obj) {
        return obj == this;
    }
    
    private boolean isDifferentTypeOrNull(Object obj) {
        return obj == null || getClass() != obj.getClass();
    }

    private boolean hasSameNumericValue(Amount other) {
        // 'other' is guaranteed to be non-null and of type Amount here
        return Double.compare(other.amount, this.amount) == 0;
    }

    /*
     * Returns a hash code for this amount.
     * 
     * @return The hash code based on the amount value.
     */
    public int hashCode() {
        return Objects.hash(amount);
    }
}