package org.guzoff.logtool.logio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link LogsProvider}.
 * @author guzoff
 */
public class DefLogsProvider implements LogsProvider {
    
    /**
     * This implementation of {@link LogsProvider#getLogs()} method reads the
     * log folder, placed in the root directory of the application. It filters
     * files by "*.log" extension.
     * @return list of paths to the log files in the local directory
     * @throws IOException if it cannot read the directory
     */
    @Override
    public List<Path> getLogs() throws IOException {
        Path dir = Paths.get(System.getProperty("user.dir"), "logs");
        List<Path> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.log")) {
            for (Path p : stream) {
                list.add(p);
            }
        }
        return list;
    }

}
