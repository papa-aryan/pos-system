package se.kth.iv1350.pos.integration;

/**
 * Thrown to indicate that a call to the database has failed.
 * This is an unchecked exception as it typically represents an environmental
 * issue (e.g., database server is down) from which the immediate caller
 * might not be able to recover.
 */
public class DatabaseFailureException extends RuntimeException {

    /**
     * Creates a new instance with a specified message.
     *
     * @param message The detail message.
     */
    public DatabaseFailureException(String message) {
        super(message);
    }

    /**
     * Creates a new instance with a specified message and cause.
     *
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public DatabaseFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
