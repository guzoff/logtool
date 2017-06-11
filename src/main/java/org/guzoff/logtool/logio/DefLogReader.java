package org.guzoff.logtool.logio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Default implementation of {@link LogReader}.
 * @author guzoff
 */
public class DefLogReader implements LogReader {

    private final Path path;

    /**
     * Constructs a DefLogReader object given a path to the log file.
     * @param path the path to the log file
     */
    public DefLogReader(Path path) {
        this.path = path;
    }
    
    /**
     * Default implementation of {@link LogReader#readLog()} method providing
     * bare reading and converting log file into list of {@link LogRecord} objects.
     * @return list of log records
     * @throws IOException if it cannot read the log file
     */
    @Override
    public List<LogRecord> readLog() throws IOException {
        List<LogRecord> list;
        try (Stream<String> stream = Files.lines(path)) {
            list = stream.map(line -> {
                Pattern pattern = Pattern.compile(", ");
                String dtpattern = "yyyy/MM/dd HH:mm:ss";
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(dtpattern);
                String[] fields = pattern.split(line);
                return new DefRecordBuilder()
                        .withDateTime(LocalDateTime.parse(fields[0], dtf))
                        .withUsername(fields[1])
                        .withMessage(fields[2])
                        .build();
                })
                .collect(Collectors.toList());
        }
        return list;
    }

}
