package com.file.domain.upload;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.file.domain.word.RankEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "upload")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class FileUpload {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    @JsonIgnore
    private Long id;

    @Column(insertable = true, updatable = true)
    private String filePath;

    private String fileName;

//    @OneToMany(mappedBy = "fileUpload")
//    private List<RankEntity> rankEntity;

    @Builder
    public FileUpload(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }


}
