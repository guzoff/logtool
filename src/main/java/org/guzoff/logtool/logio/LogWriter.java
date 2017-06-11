package org.guzoff.logtool.logio;

import java.io.IOException;
import java.util.List;

/**
 * Object writing the log records from the list of {@link LogRecord}s to the file.
 * @author guzoff
 */
public interface LogWriter {
    
    /**
     * This method converts the log records to strings and writes them to the file.
     * @param list list of the log records
     * @return number of records written
     * @throws IOException if it cannot write to the file
     */
    int writeLog(List<LogRecord> list) throws IOException;

}
