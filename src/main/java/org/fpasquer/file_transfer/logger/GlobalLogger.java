package org.fpasquer.file_transfer.logger;

import org.fpasquer.file_transfer.FileTransferImpl;
import org.fpasquer.formatter.FtpLogFormatter;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GlobalLogger {
    private Logger logger;
    private static GlobalLogger LOGGER = null;

    private GlobalLogger() throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("Config");
        String pathFileFtpLog = bundle.getString("FTP_LOG_FILE");
        this.logger = Logger.getLogger(FileTransferImpl.class.getName());
        FileHandler fileHandler;
        fileHandler = new FileHandler(MessageFormat.format(pathFileFtpLog, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH.mm.ss"))));
        fileHandler.setFormatter(new FtpLogFormatter());
        this.logger.addHandler(fileHandler);
    }

    public static void logSever(String message) {
        GlobalLogger.log(Level.SEVERE, message);
    }

    public static void logInfo(String message) {
        GlobalLogger.log(Level.INFO, message);
    }

    public static void log(final Level level, final String message){
        if (GlobalLogger.LOGGER == null) {
            try {
                GlobalLogger.LOGGER = new GlobalLogger();
            } catch (IOException e) {
                return;
            }
        }
        GlobalLogger.LOGGER.logger.log(level, message);

    }

}
