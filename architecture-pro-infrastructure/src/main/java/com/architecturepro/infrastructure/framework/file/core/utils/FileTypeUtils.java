package com.architecturepro.infrastructure.framework.file.core.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tika.Tika;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FileTypeUtils {

    private static final Logger log = LoggerFactory.getLogger(FileTypeUtils.class);

    private static final Tika TIKA = new Tika();

    public static String getMineType(byte[] data) {
        return TIKA.detect(data);
    }

    public static String getMineType(String name) {
        return TIKA.detect(name);
    }

    public static String getMineType(byte[] data, String name) {
        return TIKA.detect(data, name);
    }

    public static String getExtension(String mineType) {
        try {
            return MimeTypes.getDefaultMimeTypes().forName(mineType).getExtension();
        } catch (MimeTypeException e) {
            log.warn("[getExtension][获取文件后缀({}) 失败]", mineType, e);
            return null;
        }
    }

    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) throws IOException {
        String mineType = getMineType(content, filename);
        response.setContentType(mineType);
        if (isImage(mineType)) {
            response.setHeader("Content-Disposition", "inline;filename=" + encodeUtf8(filename));
        } else {
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeUtf8(filename));
        }
        if (StrUtil.containsIgnoreCase(mineType, "video")) {
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Length", String.valueOf(content.length));
        }
        IoUtil.write(response.getOutputStream(), false, content);
    }

    public static boolean isImage(String mineType) {
        return StrUtil.startWith(mineType, "image/");
    }

    private static String encodeUtf8(String str) {
        try {
            return URLEncoder.encode(str, StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return str;
        }
    }
}
