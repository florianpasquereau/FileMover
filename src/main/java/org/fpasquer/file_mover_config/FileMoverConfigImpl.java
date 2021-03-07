package org.fpasquer.file_mover_config;

import org.fpasquer.exception.PathFolderException;
import org.fpasquer.file_transfer.logger.GlobalLogger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileMoverConfigImpl implements FileMoverConfig{

    private final Path sourcePath;
    private final Path destPath;
    private boolean valid = true;

    public FileMoverConfigImpl(final String sourcePath, final String destPath){
        this.sourcePath = Path.of(sourcePath);
        this.destPath = Path.of(destPath);
        try {
            this.checkPathFolder(this.sourcePath);
            this.checkPathFolder(this.destPath);
        } catch (PathFolderException e) {
            GlobalLogger.log(Level.SEVERE, e.getMessage());
            this.valid = false;
        }
    }

    @Override
    public Path getSourcePath() {
        return this.sourcePath;
    }

    @Override
    public Path getDestPath() {
        return this.destPath;
    }

    public boolean isValid() {
        return valid;
    }

    private void checkPathFolder(final Path path) throws PathFolderException
    {
        if (path == null) {
            throw new PathFolderException(Path.of(""), "cannot be null");
        } else if (!Files.isDirectory(path)) {
            throw new PathFolderException(path, "Must be a directory");
        } else if (!Files.isReadable(path)) {
            throw new PathFolderException(path, "Must be readable");
        } else if (!Files.isWritable(path)) {
            throw new PathFolderException(path, "Must be writable");
        }
    }
}
