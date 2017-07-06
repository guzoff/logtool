package org.guzoff.logtool.logio;

import java.io.IOException;
import java.util.List;

/**
 * Object that provides ability to read log files. Custom implementations
 * might support custom formats of logs and {@link LogRecord}s.
 * @author guzoff
 */
public interface LogReader {
    
    /**
     * This method provides reading and converting a log file into the
     * list of log records.
     * @return list of {@link LogRecord} objects
     * @throws IOException if it cannot read the log file
     */
    List<LogRecord> readLog() throws IOException;

}
