package com.kani.multiplefileuploaddemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class FileResponse {
    private String fileName;
    private String url;
}
