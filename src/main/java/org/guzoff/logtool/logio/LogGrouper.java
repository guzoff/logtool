package org.guzoff.logtool.logio;

import java.util.List;
import java.util.Map;

/**
 * Classes implementing this interface can group logs in different ways.
 * @author guzoff
 */
@FunctionalInterface
public interface LogGrouper {

    /**
     * Method {@code group()} implements grouping of {@link LogRecord} objects
     * into the {@link Map} with keys, reflecting grouping parameters.
     * @param list list of log records
     * @return map of grouped log records
     */
    Map<? extends Comparable, List<LogRecord>> group(List<LogRecord> list);
    
}
