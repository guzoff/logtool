package org.guzoff.logtool.logio;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Proxy class providing paths to the log files.
 * Implementing classes can provide different filters and other functions
 * (e.g. file filters by attributes, getting files from the Internet).
 * @author guzoff
 */
public interface LogsProvider {

    /**
     * Method providing reading the source of the log files and returning
     * the {@link List} of paths to them.
     * @return list of paths to the log files
     * @throws IOException if it cannot read the source
     */
    List<Path> getLogs() throws IOException; 
    
}
