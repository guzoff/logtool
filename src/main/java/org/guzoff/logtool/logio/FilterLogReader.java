package org.guzoff.logtool.logio;

/**
 * Abstract decorator class for implementing filtering versions of {@link LogReader}
 * @author guzoff
 */
public abstract class FilterLogReader implements LogReader {

    protected final LogReader reader;

    public FilterLogReader(LogReader reader) {
        this.reader = reader;
    }

}
