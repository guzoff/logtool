package org.guzoff.logtool;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.guzoff.logtool.logio.DefFilterLogReader;
import org.guzoff.logtool.logio.DefLogReader;
import org.guzoff.logtool.logio.FilterPojo;
import org.guzoff.logtool.logio.LogReader;
import org.guzoff.logtool.logio.LogRecord;

/**
 * Simple task for reading one log file, using instances of {@link DefLogReader}
 * and {@link DefFilterLogReader}.
 * @author guzoff
 */
public class LogReadTask implements Callable<List<LogRecord>> {

    private final Path path;
    private final FilterPojo filter;

    /**
     * Constructs a LogReadTask object given the path to the log file and
     * {@link FilterPojo} object with filter parameters.
     * @param path the path to the log file
     * @param filter filtering parameters in the POJO form
     */
    public LogReadTask(Path path, FilterPojo filter) {
        this.path = path;
        this.filter = filter;
    }

    /**
     * Implemented {@link Callable#call()} method. It reads a log file and returns
     * {@link List} of log records
     * @return list of {@link LogRecord} instances
     * @throws IOException if it cannot read the log file
     */
    @Override
    public List<LogRecord> call() throws IOException {
        List<LogRecord> list = Collections.emptyList();
        try {
            LogReader in = new DefFilterLogReader(new DefLogReader(path), filter);
            list = in.readLog();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }
    
}
