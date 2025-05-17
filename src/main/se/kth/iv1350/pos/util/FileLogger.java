package se.kth.iv1350.pos.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Prints log messages to a file. The log file will be in the
 * current directory and will be called log.txt.
 */
public class FileLogger {
    private static final String LOG_FILE_NAME = "pos_error_log.txt";
    private PrintWriter logStream;

    /**
     * Creates a new instance and also creates/appends to a new log file.
     */
    public FileLogger() {
        try {
            logStream = new PrintWriter(new FileWriter(LOG_FILE_NAME, true), true);
        } catch (IOException ioe) {
            System.err.println("CRITICAL ERROR: CANNOT INITIALIZE FILE LOGGER.");
            ioe.printStackTrace();
        }
    }

    /**
     * Prints the specified message to the log file, prepended with a timestamp.
     *
     * @param message The string that will be printed to the log file.
     */
    public void log(String message) {
        if (logStream == null) {
            System.err.println("Logger not initialized. Cannot log message: " + message);
            return;
        }
        StringBuilder logEntry = new StringBuilder();
        logEntry.append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        logEntry.append(": ");
        logEntry.append(message);
        logStream.println(logEntry.toString());
    }

    /**
     * Logs an exception's message and stack trace.
     * @param exception The exception to log.
     */
    public void logException(Exception exception) {
        if (logStream == null) {
            System.err.println("Logger not initialized. Cannot log exception: " + exception.getMessage());
            return;
        }
        StringBuilder logEntry = new StringBuilder();
        logEntry.append(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        logEntry.append(": Exception caught: ");
        logEntry.append(exception.getMessage());
        logStream.println(logEntry.toString());
        exception.printStackTrace(logStream); 
        logStream.println(); 
    }
}