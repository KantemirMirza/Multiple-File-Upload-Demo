package com.kani.multiplefileuploaddemo.controller;

import com.kani.multiplefileuploaddemo.entity.FileResponse;
import com.kani.multiplefileuploaddemo.entity.FileResponseMessage;
import com.kani.multiplefileuploaddemo.service.IFileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileUploadController {
    private final IFileUploadService fileUploadService;

    @PostMapping("/save")
    public ResponseEntity<FileResponseMessage> uploadFiles(@RequestParam("file") MultipartFile[] files){
        String message = null;
        try{
           List<String> fileList = new ArrayList<>();
            Arrays.stream(files).forEach(file-> {
                fileUploadService.save(file);
                fileList.add(file.getOriginalFilename());
            });
            message = "File(s) Uploaded Successfully: " + fileList;
            return ResponseEntity.status(OK).body(new FileResponseMessage(message));
        }catch (Exception exception){
            return ResponseEntity.status(EXPECTATION_FAILED).body(new FileResponseMessage(exception.getMessage()));
        }
    }

    @GetMapping("/file/{fileName}")
    public ResponseEntity<Resource> getByFileName(@PathVariable String fileName){
        Resource resource = fileUploadService.getFileByName(fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; FileName=\"" + resource.getFilename() + "\"").body(resource);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FileResponse>> loadAllFiles(){
        List<FileResponse> files = fileUploadService.loadAllFiles()
                .map(path->{
                    String fileName = path.getFileName().toString();
                    String url = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                            "getByFileName", path.getFileName().toString()).build().toString();
               return new FileResponse(fileName, url);
                }).toList();
        return ResponseEntity.status(OK).body(files);
    }
}
