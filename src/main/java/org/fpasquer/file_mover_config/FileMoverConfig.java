package org.fpasquer.file_mover_config;

import java.nio.file.Path;

public interface FileMoverConfig {

    public Path getSourcePath();

    public Path getDestPath();

    public boolean isValid();
}
