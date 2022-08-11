package com.file.domain.word;

import com.file.domain.word.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RankRepository extends JpaRepository<RankEntity, Long> {

    List<RankEntity> findByVoca(String voca);

    @Query(" select r " +
            " from RankEntity r " +
            " join fetch r.fileUpload f " +
            " where r.voca like %:word%")
    List<RankEntity> findFile(@Param("word") String word);
}
