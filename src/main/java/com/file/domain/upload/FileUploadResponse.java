package com.file.domain.upload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileUploadResponse {

    private String fileName;

    private String savedUrl;

    private long fileSize;
}
