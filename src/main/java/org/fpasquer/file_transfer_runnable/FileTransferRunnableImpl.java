package org.fpasquer.file_transfer_runnable;

import org.fpasquer.file_transfer.data.FileTransferData;
import org.fpasquer.file_transfer.logger.GlobalLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;

public class FileTransferRunnableImpl implements FileTransferRunnable{
    protected final FileTransferData data;
    protected final Path destPath;

    public FileTransferRunnableImpl(final FileTransferData data, final Path destPath) {
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
            this.runValid();
            GlobalLogger.logInfo("Moved file : '" + data.getPathIso().getFileName() + "' successfully");
        } else {
            GlobalLogger.logInfo(data.getPathIso().getFileName() + " is not valid");
            this.runUnavailable();
        }
    }

    protected void runValid()
    {
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
    }

    protected void runUnavailable()
    {

    }
}
