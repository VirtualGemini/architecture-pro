package com.velox.infrastructure.framework.file.core.client.sftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ssh.JschRuntimeException;
import cn.hutool.extra.ssh.Sftp;
import com.velox.infrastructure.framework.file.core.client.AbstractFileClient;
import com.jcraft.jsch.JSch;

import java.io.File;

public class SftpFileClient extends AbstractFileClient<SftpFileClientConfig> {

    private static final Long CONNECTION_TIMEOUT = 3000L;
    private static final Long SO_TIMEOUT = 10000L;

    static {
        JSch.setConfig("server_host_key", JSch.getConfig("server_host_key") + ",ssh-dss");
    }

    private Sftp sftp;

    public SftpFileClient(String id, SftpFileClientConfig config) {
        super(id, config);
    }

    @Override
    protected void doInit() {
        FtpConfig ftpConfig = new FtpConfig(config.getHost(), config.getPort(), config.getUsername(), config.getPassword(),
                CharsetUtil.CHARSET_UTF_8, null, null);
        ftpConfig.setConnectionTimeout(CONNECTION_TIMEOUT);
        ftpConfig.setSoTimeout(SO_TIMEOUT);
        this.sftp = new Sftp(ftpConfig);
    }

    @Override
    public String upload(byte[] content, String path, String type) {
        String filePath = getFilePath(path);
        String fileName = FileUtil.getName(filePath);
        String dir = StrUtil.removeSuffix(filePath, fileName);
        File file = createTempFile(content);
        try {
            reconnectIfTimeout();
            sftp.mkDirs(dir);
            boolean success = sftp.upload(filePath, file);
            if (!success) {
                throw new JschRuntimeException(StrUtil.format("上传文件到目标目录 ({}) 失败", filePath));
            }
            return super.formatFileUrl(config.getDomain(), path);
        } finally {
            FileUtil.del(file);
        }
    }

    @Override
    public void delete(String path) {
        String filePath = getFilePath(path);
        reconnectIfTimeout();
        sftp.delFile(filePath);
    }

    @Override
    public byte[] getContent(String path) {
        String filePath = getFilePath(path);
        File destFile = createTempFile();
        try {
            reconnectIfTimeout();
            sftp.download(filePath, destFile);
            return FileUtil.readBytes(destFile);
        } finally {
            FileUtil.del(destFile);
        }
    }

    private String getFilePath(String path) {
        return config.getBasePath() + StrUtil.SLASH + path;
    }

    private synchronized void reconnectIfTimeout() {
        sftp.reconnectIfTimeout();
    }

    private static File createTempFile() {
        try {
            return File.createTempFile("upload", ".tmp");
        } catch (java.io.IOException e) {
            throw new RuntimeException("创建临时文件失败", e);
        }
    }

    private static File createTempFile(byte[] content) {
        try {
            File tempFile = File.createTempFile("upload", ".tmp");
            FileUtil.writeBytes(content, tempFile);
            return tempFile;
        } catch (java.io.IOException e) {
            throw new RuntimeException("创建临时文件失败", e);
        }
    }
}
