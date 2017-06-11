package org.guzoff.logtool.logio;

import java.time.LocalDateTime;

/**
 * Object that represents a log record. Any implementation should have following
 * properties: date and time, username, log message.
 * @author guzoff
 */
public interface LogRecord {
    
    LocalDateTime getDateTime();
    String getUsername();
    String getMessage();

}
