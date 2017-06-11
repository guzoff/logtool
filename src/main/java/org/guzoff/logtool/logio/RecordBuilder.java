package org.guzoff.logtool.logio;

import java.time.LocalDateTime;

/**
 * Builder class for the creating of the {@link LogRecord} instances. Custom 
 * implementations might create complex {@link LogRecord} objects.
 * @author guzoff
 */
public abstract class RecordBuilder {
    
    //Init default values for testing or other purposes
    protected LocalDateTime dateTime = LocalDateTime.MIN;
    protected String username = "default_user";
    protected String message = "default_message";

    public RecordBuilder withDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public RecordBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public RecordBuilder withMessage(String message) {
        this.message = message;
        return this;
    }
    
    /**
     * This method should be implemented by subclasses in order to create
     * appropriate {@link LogRecord} objects.
     * @return 
     */
    public abstract LogRecord build();

}
