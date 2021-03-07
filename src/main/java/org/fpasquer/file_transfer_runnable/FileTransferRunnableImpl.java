package org.fpasquer.file_transfer_runnable;

import org.fpasquer.file_transfer.data.FileTransferData;
import org.fpasquer.file_transfer.data.FileTransferDataImpl;
import org.fpasquer.file_transfer.logger.GlobalLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;

public class FileTransferRunnableImpl implements FileTransferRunnable{
    protected final FileTransferData data;
    protected final Path destPath;

    public FileTransferRunnableImpl(final FileTransferData data, final Path destPath) {
        this.data = data;
        this.destPath = destPath;
    }

    protected boolean isValid() {
        int bytesCount = 0;
        byte[] byteArray = new byte[1024];
        StringBuilder hash = new StringBuilder();
        MessageDigest digest;

        if (this.data == null) {
            return false;
        }
        try (FileInputStream fileInputStream = new FileInputStream(this.data.getPathIso().toFile())) {
            digest = MessageDigest.getInstance("SHA-256");
            while ((bytesCount = fileInputStream.read(byteArray)) != -1) {
                digest.update(byteArray, 0, bytesCount);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            GlobalLogger.logSever(e.getMessage());
            return false;
        }
        byte[] bytes = digest.digest();

        for (byte aByte : bytes) {
            hash.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        if (hash.toString().compareToIgnoreCase(this.data.getSha256()) != 0) {
            GlobalLogger.log(Level.WARNING, data.getPathIso().getFileName().toString() + " : Hash does not match -> " + this.data.getSha256() + " != " + hash);
            return false;
        }
        return true;
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
