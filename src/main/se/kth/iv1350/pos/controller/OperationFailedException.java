package se.kth.iv1350.pos.controller;

/**
 * Thrown when an operation fails due to an underlying issue, such as a database error.
 * This is a checked exception, forcing the caller to handle or declare it.
 */
public class OperationFailedException extends Exception {

    /**
     * Creates a new instance with a specified message and cause.
     *
     * @param message The detail message.
     * @param cause The underlying cause of this exception.
     */
    public OperationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
