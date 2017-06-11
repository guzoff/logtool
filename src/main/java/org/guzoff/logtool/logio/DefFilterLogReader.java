package org.guzoff.logtool.logio;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link FilterLogReader}.
 * @author guzoff
 */
public class DefFilterLogReader extends FilterLogReader {

    private final FilterPojo filter;

    /**
     * Constructs a DefFilterLogReader object given {@link LogReader} object
     * reference and filtering parameters in the POJO form.
     * @param reader decorated {@link LogReader} instance
     * @param filter filtering parameters in the POJO form
     */
    public DefFilterLogReader(LogReader reader, FilterPojo filter) {
        super(reader);
        this.filter = filter;
    }
    
    /**
     * Implemented {@link LogReader#readLog()} method. It provides custom
     * filtering of log records depending on user input parameters specified
     * in {@link FilterPojo} object.
     * @return list of {@link LogRecord} objects
     * @throws IOException if it cannot read the log file
     */
    @Override
    public List<LogRecord> readLog() throws IOException {
        List<LogRecord> list = reader.readLog()
                .stream().filter((LogRecord rec) -> {
                    return rec.getDateTime().compareTo(filter.getPeriodEnd()) <= 0
                            && rec.getDateTime().compareTo(filter.getPeriodStart()) >= 0;
                }).filter((LogRecord rec) -> {
                    if (filter.getUsername().isEmpty()) return true;
                    return rec.getUsername().equals(filter.getUsername());
                }).filter((LogRecord rec) -> {
                    if (filter.getMsgPattern().isEmpty()) return true;
                    return rec.getMessage().toLowerCase()
                            .contains(filter.getMsgPattern().toLowerCase());
                }).collect(Collectors.toList());
        return list;
    }

}
