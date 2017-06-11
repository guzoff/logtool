package org.guzoff.logtool.logio;

/**
 * Default {@link RecordBuilder} implementation.
 * @author guzoff
 */
public class DefRecordBuilder extends RecordBuilder {

    /**
     * This implementation creates a {@link DefLogRecord} instance.
     * @return simple log record object
     */
    @Override
    public LogRecord build() {
        return new DefLogRecord(dateTime, username, message);
    }

}
