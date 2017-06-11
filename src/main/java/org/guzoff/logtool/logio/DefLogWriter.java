package org.guzoff.logtool.logio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Default {@link LogWriter} implementation.
 * @author guzoff
 */
public class DefLogWriter implements LogWriter {

    private final File file;

    /**
     * Constructs a DefLogWriter object given the file to write to.
     * @param file file for saving log records
     */
    public DefLogWriter(File file) {
        this.file = file;
    }
    
    /**
     * Writes the log records from the list to the file.
     * @param list list of log records
     * @return number of records written
     * @throws IOException if it cannot write to the file
     */
    @Override
    public int writeLog(List<LogRecord> list) throws IOException {
        int count = 0;
        try (Writer out = new BufferedWriter(new FileWriter(file))) {
            for (LogRecord rec : list) {
                count++;
                out.append(rec.toString() + "\n");
            }
        }
        return count;
    }

}
