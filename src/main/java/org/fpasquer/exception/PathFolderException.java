package org.fpasquer.exception;

import java.nio.file.Path;

public class PathFolderException extends Exception{

    public PathFolderException(final Path path, final String message) {
        super("'" + path.toAbsolutePath() + "' is not a valid path : " + message);
    }
}
