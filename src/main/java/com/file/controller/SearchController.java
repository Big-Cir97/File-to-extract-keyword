package com.file.controller;

import com.file.domain.word.RankEntity;
import com.file.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final RankService rankService;

    @GetMapping("/search")
    public ResponseEntity search(@RequestBody RankEntity rankEntity) {
        List<RankEntity> search = rankService.search(rankEntity.getVoca());
        return ResponseEntity.ok().body(search);
    }
}
