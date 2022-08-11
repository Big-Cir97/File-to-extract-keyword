package com.file.controller;

import com.file.domain.upload.DoubleResponseDto;
import com.file.service.RankService;
import com.file.domain.upload.FileUpload;
import com.file.domain.upload.FileUploadRepository;
import com.file.domain.upload.FileUploadResponse;
import com.file.service.FileUploadService;
import kr.dogfoot.hwplib.object.HWPFile;
import kr.dogfoot.hwplib.reader.HWPReader;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractMethod;
import kr.dogfoot.hwplib.tool.textextractor.TextExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final FileUploadRepository fileUploadRepository;
    private final RankService rankService;

    // localhost:8080/upload
    @PostMapping(value = "/upload", produces = "application/json; charset=UTF-8")
    public ResponseEntity<?> uploadFile(
            @RequestPart(value="file",required = false) MultipartFile multipartFile)
            throws Exception {

        DoubleResponseDto doubleResponseDto = new DoubleResponseDto();

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());       // fileName = 원래 파일 이름.png
        String filecode = fileUploadService.saveFile(fileName, multipartFile);              // uuid + 파일명 반환

        long size = multipartFile.getSize();
        
        /**     json 응답 코드
         *      setFileName : 파일 업로드 시 파일 이름
         *      setDownloadUrl : 다운로드 받는 경로
         */
        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(fileName);
        response.setSavedUrl(fileUploadService.getPath() + filecode);
        response.setFileSize(size);

        FileUpload entity = FileUpload.builder()
                .filePath(response.getSavedUrl()).fileName(response.getFileName()).build();

        FileUpload save = fileUploadRepository.save(entity);


        /**
         * 1. 파일 경로 읽어서
         * 2. 내용 read
         * 3. String 형식의 내용을 split 으로 키워드 분해
         * 4. getVoca() 메서드로 키워드의 개수 카운팅 해서 Map 에 저장
         */
        if (fileName.contains(".hwp")) {
            HWPFile hwpFile = HWPReader.fromFile("/Users/daewon/desktop/new/" + filecode + "-" +fileName);
            String body = TextExtractor.extract(hwpFile, TextExtractMethod.InsertControlTextBetweenParagraphText);
            System.out.println(body);

            String[] s = body.split("게|에| ");
            Map<String, Integer> voca = rankService.getVoca(s, save);
            doubleResponseDto.setMap(voca);
        } else if (fileName.contains(".pdf")) {
            File file = new File("/Users/daewon/desktop/new/" + filecode + "-" +fileName);
            PDDocument document = PDDocument.load(file);       // pdf 텍스트 추출
            PDFTextStripper stripper = new PDFTextStripper();
            String body = stripper.getText(document);
            String[] s = body.split("게|에| ");
            Map<String, Integer> voca = rankService.getVoca(s, save);
            doubleResponseDto.setMap(voca);
        } else if (fileName.contains(".docx")) {
            FileInputStream docx = new FileInputStream("/Users/daewon/desktop/new/" + filecode + "-" +fileName);
            XWPFDocument file = new XWPFDocument(OPCPackage.open(docx));
            XWPFWordExtractor ext = new XWPFWordExtractor(file);
            String body = ext.getText();
            String[] s = body.split("게|에| ");
            Map<String, Integer> voca = rankService.getVoca(s, save);
            doubleResponseDto.setMap(voca);
        } else {
            InputStream in = new FileInputStream("/Users/daewon/desktop/new/" + filecode + "-" + fileName);
            String body = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
            String[] s = body.split("게|에| ");
            Map<String, Integer> voca = rankService.getVoca(s, save);
            doubleResponseDto.setMap(voca);
        }

        // dto 에 두개의 body 정보를 담아 반환
        doubleResponseDto.setFileUploadResponse(response);

        return new ResponseEntity<>(doubleResponseDto, HttpStatus.OK);
    }
}
