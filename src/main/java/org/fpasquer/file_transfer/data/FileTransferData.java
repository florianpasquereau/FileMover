package org.fpasquer.file_transfer.data;

import java.nio.file.Path;

public interface FileTransferData {

    public void setSha256(String sha256);

    public void setPathIso(Path pathIso);

    public String getSha256();

    public Path getPathIso();
}
