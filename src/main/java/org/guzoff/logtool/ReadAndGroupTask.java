package org.guzoff.logtool;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javafx.concurrent.Task;
import org.guzoff.logtool.logio.DefLogWriter;
import org.guzoff.logtool.logio.DefLogsProvider;
import org.guzoff.logtool.logio.GrouperPojo;
import org.guzoff.logtool.logio.FilterPojo;
import org.guzoff.logtool.logio.LogGrouper;
import org.guzoff.logtool.logio.LogGroupers;
import org.guzoff.logtool.logio.LogRecord;
import org.guzoff.logtool.logio.LogWriter;
import org.guzoff.logtool.logio.LogsProvider;

/**
 * Parent task for {@link LogReadTask}s. Directory scanning and filtering happens
 * here along with filtered log records grouping by specified parameters.
 * @author guzoff
 */
public class ReadAndGroupTask extends Task<String> {
    
    private final FilterPojo filter;
    private final GrouperPojo grouper;
    private final File saveTo;
    private final ExecutorService executor;
    
    /**
     * Constructs a ReadAndGroupTask object given filtering parameters, grouping
     * parameters, reference to the file for saving resulting list of log records
     * and {@code executor}.
     * @param filter POJO that holds filtering parameters
     * @param group POJO that holds grouping parameters
     * @param saveTo reference to {@link File} for saving filtered logs
     * @param executor {@link ExecutorService} instance
     */
    public ReadAndGroupTask(FilterPojo filter, GrouperPojo group, 
                            File saveTo, ExecutorService executor) {
        this.filter = filter;
        this.grouper = group;
        this.saveTo = saveTo;
        this.executor = executor;
    }
    
    /**
     * Implemented {@link Task#call()} method. Log files from directory are processed here.
     * @return statistics string, formed according to grouping method
     */
    @Override
    protected String call() {
        this.updateMessage("Task started...");
        long logFilesCount;
        long fileCounter = 0;
        List<LogRecord> allRecordsList = new ArrayList<>();
        LogsProvider logsProvider = new DefLogsProvider();
        LogWriter out = new DefLogWriter(saveTo);
        List<Future<List<LogRecord>>> futuresList = new ArrayList<>();
        String statistics = "";
        try {
            List<Path> pathsList = logsProvider.getLogs();
            pathsList.stream().map((path) -> executor.submit(new LogReadTask(path, filter)))
                        .forEach((recordsList) -> futuresList.add(recordsList)
            );
            logFilesCount = futuresList.size();
            for (Future<List<LogRecord>> future : futuresList) {
                if (this.isCancelled()) {
                    break;
                }
                allRecordsList.addAll(future.get());
                fileCounter++;
                this.updateProgress(fileCounter, logFilesCount);
            }
            out.writeLog(allRecordsList);
        } catch (IOException | InterruptedException | ExecutionException ex) {
            this.updateMessage(ex.getMessage());
        } finally {
            executor.shutdown();
        }
        LogGrouper logGrouper = LogGroupers.groupBy(grouper);
        Map<?, List<LogRecord>> map = logGrouper.group(allRecordsList);
        TreeSet<String> set = new TreeSet(map.keySet());
        for (String key : set) {
            if (this.isCancelled()) break;
            statistics += String.format("%-20s%-10d\n", key, map.get(key).size());
            this.updateValue(statistics);
        }
        return statistics;
    }
    
    @Override
    protected void cancelled() {
        super.cancelled();
        this.updateMessage("The task was cancelled");
    }
    
    @Override
    protected void failed() {
        super.failed();
        this.updateMessage("The task failed");
    }
    
    @Override
    protected void succeeded() {
        super.succeeded();
        this.updateMessage("The task finished successfully");
    }

}
