package org.fpasquer.file_transfer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.fpasquer.file_mover_config.FileMoverConfig;
import org.fpasquer.file_transfer.adapter.FileTransferDataAdapter;
import org.fpasquer.file_transfer.data.FileTransferData;
import org.fpasquer.file_transfer.logger.GlobalLogger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class FileTransferImpl implements FileTransfer{
    private final Gson gson;
    private final FileMoverConfig fileMoverConfig;

    public FileTransferImpl(final FileMoverConfig fileMoverConfig) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FileTransferData.class, new FileTransferDataAdapter());
        this.gson = builder.create();
        this.fileMoverConfig = fileMoverConfig;
    }

    public static class FileTransferRunnable implements Runnable{

        protected final FileTransferData data;
        protected final Path destPath;

        public FileTransferRunnable(final FileTransferData data, final Path destPath) {
            this.data = data;
            this.destPath = destPath;
        }

        protected boolean isValid() {
            return !(this.data == null);
        }

        protected Path getDestPathBuild()
        {
            return Path.of(this.destPath + File.separator + data.getPathIso().getFileName());
        }

        @Override
        public void run() {
            if (this.isValid()) {
                GlobalLogger.logInfo("Starting process for file : '" + data.getPathIso().getFileName() + "'");
                Path dest = this.getDestPathBuild();
                if (Files.exists(dest)) {
                    GlobalLogger.logInfo("Updating of : '" + data.getPathIso().getFileName() + "'");
                }
                try {
                    Files.copy(data.getPathIso(), dest,
                            StandardCopyOption.COPY_ATTRIBUTES,
                            StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    GlobalLogger.log(Level.SEVERE, e.getMessage());
                }
                GlobalLogger.logInfo("Moved file : '" + data.getPathIso().getFileName() + "' successfully");
            } else {
                GlobalLogger.logInfo(data.getPathIso().getFileName() + " is not valid");
            }
        }
    }

    protected FileTransferData convertToData(final Path file) {
        FileTransferData data;
        try (BufferedReader bufferedReader = new BufferedReader((Files.newBufferedReader(file, StandardCharsets.UTF_8)))) {
            data = this.gson.fromJson(bufferedReader, FileTransferData.class);
            data.setPathIso(file);
        } catch (Exception e) {
            GlobalLogger.log(Level.WARNING, file.getFileName().toString() + " : " + e.getMessage());
            return null;
        }
        return this.addInit(data);
    }

    @Override
    public Runnable newRunnable(final Path file) {
        FileTransferData data = this.convertToData(file);
        return new FileTransferRunnable(data, this.getFileMoverConfig().getDestPath());
    }

    @Override
    public FileMoverConfig getFileMoverConfig() {
        return this.fileMoverConfig;
    }


    protected FileTransferData addInit(FileTransferData data) {
        return data;
    }
}
