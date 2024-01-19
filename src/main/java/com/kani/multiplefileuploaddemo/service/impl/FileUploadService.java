package com.kani.multiplefileuploaddemo.service.impl;

import com.kani.multiplefileuploaddemo.service.IFileUploadService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;


@Service
public class FileUploadService implements IFileUploadService {
    private final Path rootDirectory = Paths.get("uploads");

    @Override
    public void init() {
        try{
            File tempDir = new File(rootDirectory.toUri());
            boolean dirExists = tempDir.exists();
            if(!dirExists){
                Files.createDirectory(rootDirectory);
            }
        }catch (IOException exception){
            throw new RuntimeException("Error Creating Directory");
        }
    }

    @Override
    public void save(MultipartFile multipartFile) {
        try{
            Files.copy(multipartFile.getInputStream(),
                    this.rootDirectory.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        }catch (IOException exception){
            throw new RuntimeException("Error Uploading Files");
        }
    }

    @Override
    public Resource getFileByName(String fileName) {
        try{
            Path filePath = rootDirectory.resolve(fileName);
           Resource resource = new UrlResource(filePath.toUri());
           if(resource.exists() && resource.isReadable()){
               return resource;
           }else{
               throw new RuntimeException("Could Not Read File");
           }
        }catch (MalformedURLException exception){
            throw new RuntimeException("Error: " + exception.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootDirectory.toFile());
    }

    @Override
    public Stream<Path> loadAllFiles() {
        try{
           return Files.walk(this.rootDirectory,1)
                   .filter(path -> !path.equals(this.rootDirectory))
                   .map(this.rootDirectory :: relativize);
        }catch (IOException exception){
            throw new RuntimeException("Could Not Load Files");
        }
    }

}
