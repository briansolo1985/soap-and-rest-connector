package com.ferenckis.tutorial.client.rest;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.google.common.io.ByteStreams.copy;

@Component
public class FileDownloader {

    private static final String WORKDAY_FILE_NAME = "wd-master-file.tsv";

    @Autowired
    private FileRequestFactory requestFactory;

    @Autowired
    private FileSystem fileSystem;

    @Value("${master.file.base.folder}")
    private String workdayMasterFileBaseFolder;

    @Value("#{workdayMasterFilePartitionTag}")
    private String workdayMasterFilePartitionTag;

    public void download(String workdayMasterFileId) {
        try {
            Path targetPath = getFilePath(initDirectory(workdayMasterFileBaseFolder, workdayMasterFilePartitionTag));
            copy(requestFactory.newRequest(workdayMasterFileId).openStream(),
                    fileSystem.create(targetPath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path initDirectory(String parent, String child) throws IOException {
        Path folder = new Path(parent, child);
        fileSystem.mkdirs(folder);
        clearDirectory(folder);
        return folder;
    }

    private void clearDirectory(Path folder) throws IOException {
        RemoteIterator<LocatedFileStatus> it = fileSystem.listFiles(folder, false);
        while (it.hasNext()) {
            fileSystem.delete(it.next().getPath(), false);
        }
    }

    private Path getFilePath(Path parent) {
        return new Path(parent, WORKDAY_FILE_NAME);
    }
}
