package com.kani.multiplefileuploaddemo.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IFileUploadService {
    void init();
    void save(MultipartFile multipartFile);
    Resource getFileByName(String fileName);
    void deleteAll();
    Stream<Path> loadAllFiles();
}
