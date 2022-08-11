package com.file.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

//    spring:
//    servlet:
//    multipart:
//    max-file-size: 100MB
//    max-request-size: 100MB

    @Value("${file.dir}")
    public String path;

    public String getPath() {
        return path;
    }

    @Transactional
    public String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(path);                                      // 다운로드 받을 경로

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = UUID.randomUUID().toString();                         // 중복되지 않도록 uuid 설정

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);      //  파일 저장 형식 ex) 1234-5678 + 원본파일명.png
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        //  파일 저장 형식 ex) 1234-5678 + 원본파일명.png 을 반환
        return fileCode;
    }
}
