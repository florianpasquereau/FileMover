package org.fpasquer;

import org.fpasquer.file_mover_config.FileMoverConfig;
import org.fpasquer.file_mover_config.FileMoverConfigImpl;
import org.fpasquer.file_transfer.FileTransfer;
import org.fpasquer.file_transfer.FileTransferImpl;
import org.fpasquer.file_transfer.data.FileTransferData;
import org.fpasquer.file_transfer.logger.GlobalLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.stream.Stream;

public class FileMover {
    private static void moveFiles(final FileTransfer fileTransfer) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Config");
        ExecutorService executorService = Executors.newFixedThreadPool(Integer.parseInt(resourceBundle.getString("NUMBER_THREAD")));
        if (!fileTransfer.isValid()) {
            return ;
        }
        try {
            Stream<Path> pathStream = Files.list(fileTransfer.getSourcePath())
                    .filter((p) -> p.getFileName().toString().endsWith(".json"));
            pathStream.map(fileTransfer::newRunnable)
                .filter(Objects::nonNull)
                .forEach(executorService::execute);
        } catch (IOException e) {
            GlobalLogger.log(Level.SEVERE, e.getMessage());
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(Integer.parseInt(resourceBundle.getString("NUMBER_MINUTE_MAX_WAIT")), TimeUnit.MINUTES)) {
                executorService.shutdownNow();
            }
        }catch (InterruptedException e){
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] argv) {
        if (argv.length != 2) {
            GlobalLogger.logSever("File Lover need parameters : <source Folder> <dest folder>");
        } else {
            GlobalLogger.logInfo("Started");
            FileMover.moveFiles(new FileTransferImpl(new FileMoverConfigImpl(argv[0], argv[1])));
            GlobalLogger.logInfo("Ended");
        }
    }
}
