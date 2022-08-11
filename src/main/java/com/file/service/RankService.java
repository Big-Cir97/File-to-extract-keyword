package com.file.service;

import com.file.domain.upload.FileUpload;
import com.file.domain.upload.FileUploadRepository;
import com.file.domain.word.RankEntity;
import com.file.domain.word.RankRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.LinkedMap;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RankService {

    private final RankRepository rankRepository;

    /**
     * 키워드를 검색 -> 키워드가 포함된 파일명 제시 로직
     * @param word
     * @return
     */
    public List<RankEntity> search(String word) {
        List<RankEntity> byVoca = rankRepository.findByVoca(word);
        if (!byVoca.isEmpty()) {
            return rankRepository.findFile(word);
        }
        return null;
    }

    /**
     * 업로드된 파일의 키워드들 숫자 카운팅, 상위 20개 DB 반영
     * @param s
     * @param fileUpload
     * @return
     */
    public Map<String, Integer> getVoca(String[] s , FileUpload fileUpload) {
        Map<String, Integer> map = new LinkedMap<>();

        for (String a : s) {
            Integer val = map.get(a);
            map.put(a, (val != null) ? val + 1 : 1);
        }

        List<Map.Entry<String, Integer>> entryList = new LinkedList<>(map.entrySet());
        entryList.sort(((o1, o2) -> map.get(o2.getKey()) - map.get(o1.getKey())));

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : entryList) {
            result.put(entry.getKey(), entry.getValue());
        }

        Object[] objects = result.keySet().toArray();
        for (int i = 0; i < 20; i++) {
            RankEntity rankEntity = new RankEntity();
            rankEntity.setVoca(objects[i].toString());
            rankEntity.setNum(result.get(objects[i]));
            rankEntity.setFileUpload(fileUpload);

            rankRepository.save(rankEntity);
        }

        // System.out.println(result.keySet().toString().split(" "));

        return result;
    }
}
