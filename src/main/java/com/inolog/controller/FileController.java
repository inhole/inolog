package com.inolog.controller;

import com.inolog.exception.common.InvalidRequest;
import com.inolog.exception.file.FileNotFound;
import com.inolog.response.FileResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam("image") MultipartFile[] files) {

        // 업로드된 파일들을 저장할 리스트 생성
        List<String> fileUrls = new ArrayList<>();

        // 1. 파일이 비어있는지 확인
        if (files.length == 0) throw new FileNotFound();

        try {
            // 2. 파일 저장 절대 경로 생성
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            // 3. upload 폴더 생성
            if (!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

            for (MultipartFile file : files) {
                // 4. 인코딩 파일명 생성
                String encodeFilename = encodeFilenameToBase64(file.getOriginalFilename());

                // 로컬환경으로 build 폴더에 저장함. 운영환경이라면 파일서버 구성
                // 5. 서버의 파일 시스템에 저장
                Path copyLocation = uploadPath.resolve(encodeFilename);
                Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

                // 6. 파일 URL 생성
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/uploads/")
                        .path(encodeFilename)
                        .toUriString();

                fileUrls.add(fileDownloadUri);
            }
            // 7. response
            FileResponse fileResponse = new FileResponse(String.join(",", fileUrls));
            return ResponseEntity.ok().body(fileResponse);

        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidRequest();
        }
    }

    /**
     * 고유한 파일 이름 생성 및 인코딩 Base64 ( UUID + 파일명 ) + 확장자
     * @param originalFilename
     * @return
     */
    private String encodeFilenameToBase64(String originalFilename) {
        String filename = originalFilename.substring(0, originalFilename.lastIndexOf(".")); // 파일명
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        String uniqueFilename = UUID.randomUUID() + filename;
        String encodeFilename = Base64.getEncoder().encodeToString(uniqueFilename.getBytes(StandardCharsets.UTF_8));

        return encodeFilename + "." +fileExtension;
    }
}
