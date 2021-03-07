package org.fpasquer.file_transfer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.fpasquer.file_mover_config.FileMoverConfig;
import org.fpasquer.file_transfer.adapter.FileTransferDataAdapter;
import org.fpasquer.file_transfer.data.FileTransferData;
import org.fpasquer.file_transfer.data.FileTransferDataImpl;
import org.fpasquer.file_transfer.logger.GlobalLogger;
import org.fpasquer.file_transfer_runnable.FileTransferRunnable;
import org.fpasquer.file_transfer_runnable.FileTransferRunnableImpl;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class FileTransferImpl implements FileTransfer{
    private final Gson gson;
    private final FileMoverConfig fileMoverConfig;

    public FileTransferImpl(final FileMoverConfig fileMoverConfig) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(FileTransferDataImpl.class, new FileTransferDataAdapter());
        this.gson = builder.create();
        this.fileMoverConfig = fileMoverConfig;
    }

    /**
     * Convert Path json file into data object.
     * data object must store all detail about the file to transfer.
     * */
    protected FileTransferData convertToData(final Path file) {
        FileTransferDataImpl data;
        try (BufferedReader bufferedReader = new BufferedReader((Files.newBufferedReader(file, StandardCharsets.UTF_8)))) {
            data = this.gson.fromJson(bufferedReader, FileTransferDataImpl.class);
            data.setPathIso(file);
        } catch (Exception e) {
            GlobalLogger.log(Level.WARNING, file.getFileName().toString() + " : " + e.getMessage());
            return null;
        }
        return this.addInit(data);
    }

    @Override
    public FileTransferRunnable newRunnable(final Path file) {
        FileTransferData data = this.convertToData(file);
        return new FileTransferRunnableImpl(data, this.fileMoverConfig.getDestPath());
    }


    @Override
    public Path getSourcePath() {
        return this.fileMoverConfig.getSourcePath();
    }

    @Override
    public Path getDestPath() {
        return this.fileMoverConfig.getDestPath();
    }

    @Override
    public boolean isValid() {
        return this.fileMoverConfig.isValid();
    }


    /**
     * Add actions to the default behaviour.
     * */
    protected FileTransferData addInit(FileTransferData data) {
        return data;
    }
}
