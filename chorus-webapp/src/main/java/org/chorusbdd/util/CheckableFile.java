package org.chorusbdd.util;

import org.apache.commons.codec.digest.DigestUtils;

import javax.annotation.concurrent.ThreadSafe;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static org.apache.commons.lang3.Validate.notNull;

@ThreadSafe
public class CheckableFile {
    private final Path path;
    private final Charset charset;
    private final Object monitor = new Object();
    private volatile String contents;
    private volatile String md5;

    public CheckableFile(final Path path, final Charset charset) {
        this.path = notNull(path);
        this.charset = notNull(charset);
    }

    public String contents() {
        loadFile();
        return contents;
    }

    public String md5() {
        calculateMd5();
        return md5;
    }

    private void calculateMd5() {
        if (md5 == null) {
            synchronized (monitor) {
                md5 = DigestUtils.md5Hex(contents());
            }
        }
    }

    private void loadFile() {
        if (contents == null) {
            synchronized (monitor) {
                if (md5 == null) {
                    if (contents == null) {
                        contents = FileUtils.readFile(path, charset);
                    }
                }
            }
        }
    }
}