package org.guzoff.logtool.logio;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper class for grouping log records depending on user defined parameters.
 * @author guzoff
 */
public class LogGroupers {
    /**
     * Factory method that chooses appropriate grouper depending on input
     * parameters.
     * @param grouper {@link GrouperPojo} object containing grouping parameters
     * @return appropriate {@link LogGrouper} for grouping {@link List} of 
     * {@link LogRecord} instances or {@link LogGrouper} that always returns
     * an empty {@link Map}
     */
    public static LogGrouper groupBy(GrouperPojo grouper) {
        boolean byUser = grouper.isByUsername();
        boolean byUnit = grouper.isByTimeUnit();
        Units unit = grouper.getUnit();
        LogGrouper logGrouper = (List<LogRecord> list) -> Collections.emptyMap();
        if (byUnit) {
            switch (unit) {
                case YEAR:
                    logGrouper = getGrouperMethod(byUser, 4);
                    break;
                case MONTH:
                    logGrouper = getGrouperMethod(byUser, 7);
                    break;
                case DAY:
                    logGrouper = getGrouperMethod(byUser, 10);
                    break;
            }
        } else if (byUser) {
            logGrouper = (List<LogRecord> list) -> list.stream()
                            .collect(Collectors.groupingBy((LogRecord rec) -> 
                                  rec.getUsername()));
        }
        return logGrouper;
    }

    private static LogGrouper getGrouperMethod(boolean byUser, int ind) {
        if (byUser) {
            return (List<LogRecord> list) -> list.stream()
                    .collect(Collectors.groupingBy((LogRecord rec) -> {
                        return rec.getUsername() + " "
                                + rec.getDateTime().toString().substring(0, ind);
                    }));
        } else {
            return (List<LogRecord> list) -> list.stream()
                            .collect(Collectors.groupingBy((LogRecord rec) -> 
                                rec.getDateTime().toString().substring(0, ind)));
        }
    }

}
