package org.fpasquer.file_transfer.data;

import java.nio.file.Path;

public class FileTransferData {
    private String sha256 = null;
    private String nameFormatted = null;
    private String action = null;
    private Path pathIso = null;

    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    public void setNameFormatted(String nameFormatted) {
        this.nameFormatted = nameFormatted;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setPathIso(Path pathIso) {
        this.pathIso = Path.of(pathIso.toAbsolutePath().toString().replace(".json", ""));
    }

    public String getSha256() {
        return sha256;
    }

    public String getNameFormatted() {
        return nameFormatted;
    }

    public String getAction() {
        return action;
    }

    public Path getPathIso() {
        return pathIso;
    }
}
