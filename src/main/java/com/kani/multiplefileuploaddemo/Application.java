package com.kani.multiplefileuploaddemo;

import com.kani.multiplefileuploaddemo.service.IFileUploadService;
import jakarta.annotation.Resource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Resource
    private IFileUploadService fileUploadService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        fileUploadService.init();
    }
}
