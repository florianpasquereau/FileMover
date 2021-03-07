package org.fpasquer.file_transfer;

import org.fpasquer.file_mover_config.FileMoverConfig;

import java.nio.file.Path;

public interface FileTransfer{

    Runnable newRunnable(final Path file);

    FileMoverConfig getFileMoverConfig();
}
