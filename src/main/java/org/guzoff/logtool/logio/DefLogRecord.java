package org.guzoff.logtool.logio;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Default {@link LogRecord} implementation.
 * @author guzoff
 */
public class DefLogRecord implements LogRecord {
    
    private final LocalDateTime dateTime;
    private final String username;
    private final String message;

    /**
     * Constructs a DefLogRecord object given {@code dateTime}, {@code username}
     * and log {@code message}.
     * @param dateTime date and time of log creation
     * @param username name of the user
     * @param message the log message
     */
    public DefLogRecord(LocalDateTime dateTime, String username, String message) {
        this.dateTime = dateTime;
        this.username = username;
        this.message = message;
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getMessage() {
        return message;
    }
    
    /**
     * This implementation of {@code toString()} method is needed for writing
     * log records from such objects to the file.
     * @return 
     */
    @Override
    public String toString() {
        String dtpattern = "yyyy/MM/dd HH:mm:ss";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dtpattern);
        String dtString = dateTime.format(dtf);
        return dtString + ", " + username + ", " + message;
    }

}
