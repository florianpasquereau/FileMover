package org.fpasquer.file_transfer;

import org.fpasquer.file_mover_config.FileMoverConfig;
import org.fpasquer.file_transfer_runnable.FileTransferRunnable;

import java.nio.file.Path;

public interface FileTransfer extends FileMoverConfig {

    FileTransferRunnable newRunnable(final Path file);
}
