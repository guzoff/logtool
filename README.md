# logtool
Simple JavaFX application for log files processing

**ATTENTION!**

This tool scans the log folder in the root app directory. 
So if you want to test it, you should unpack [**Logs.zip**](https://github.com/guzoff/logtool/blob/master/Logs.zip) 
archive into the folder where **Logtool-0.1.0.jar** file will be, after you have built it 
(by default it will have been built in **_target/_** folder).

This app lets you filter log records by specified criteria and save the result into the file.
It also prints aggregated statistics depending on grouping parameters set.
Additionally you can choose the number of threads for log files reading.

A small log I/O library is available in the main package of the app.
You can use it to implement your own versions of log files.
There are some interfaces for that purpose there: 
- [_LogRecord_](https://github.com/guzoff/logtool/blob/master/src/main/java/org/guzoff/logtool/logio/LogRecord.java) 
- [_LogReader_](https://github.com/guzoff/logtool/blob/master/src/main/java/org/guzoff/logtool/logio/LogReader.java)
- [_LogWriter_](https://github.com/guzoff/logtool/blob/master/src/main/java/org/guzoff/logtool/logio/LogWriter.java)
- [_LogsProvider_](https://github.com/guzoff/logtool/blob/master/src/main/java/org/guzoff/logtool/logio/LogsProvider.java)
- [_LogGrouper_](https://github.com/guzoff/logtool/blob/master/src/main/java/org/guzoff/logtool/logio/LogGrouper.java)

All you need to do after implementing one or more of them is to edit corresponding lines
in files _LogReadTask.java_ and _ReadAndGroupTask.java_ (or just create your own version
of Task and wrap it into the Service object in _MainApp.java_ file).
