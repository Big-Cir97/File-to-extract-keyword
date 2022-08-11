package com.file.domain.upload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class DoubleResponseDto {

    private FileUploadResponse fileUploadResponse;
    private Map<String, Integer> map;
}
