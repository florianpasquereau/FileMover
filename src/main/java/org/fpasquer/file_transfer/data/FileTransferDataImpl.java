package org.fpasquer.file_transfer.data;

import java.nio.file.Path;

public class FileTransferDataImpl implements FileTransferData {
    private String sha256 = null;
    private Path pathIso = null;

    @Override
    public void setSha256(String sha256) {
        this.sha256 = sha256;
    }

    @Override
    public void setPathIso(Path pathIso) {
        this.pathIso = Path.of(pathIso.toAbsolutePath().toString().replace(".json", ""));
    }

    @Override
    public String getSha256() {
        return sha256;
    }

    @Override
    public Path getPathIso() {
        return pathIso;
    }
}
