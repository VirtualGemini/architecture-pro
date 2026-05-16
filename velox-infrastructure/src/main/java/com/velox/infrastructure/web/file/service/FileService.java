package com.velox.infrastructure.web.file.service;

import com.velox.common.result.PageResult;
import com.velox.infrastructure.web.file.vo.FileCreateReqVO;
import com.velox.infrastructure.web.file.vo.FilePageReqVO;
import com.velox.infrastructure.web.file.vo.FilePresignedUrlRespVO;
import com.velox.infrastructure.web.file.vo.FileRespVO;

import java.util.List;

public interface FileService {

    PageResult<FileRespVO> getFilePage(FilePageReqVO pageReqVO);

    String createFile(byte[] content, String name, String directory, String type) throws Exception;

    FilePresignedUrlRespVO presignPutUrl(String name, String directory) throws Exception;

    String presignGetUrl(String url, Integer expirationSeconds);

    String createFile(FileCreateReqVO createReqVO);

    FileRespVO getFile(String id);

    void deleteFile(String id) throws Exception;

    void deleteFileList(List<String> ids) throws Exception;

    byte[] getFileContent(String configId, String path) throws Exception;
}
