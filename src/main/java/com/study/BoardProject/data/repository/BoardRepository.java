package com.study.BoardProject.data.repository;

import com.study.BoardProject.data.entity.BoardEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    Page<BoardEntity> findByTitleContaining(String searchkeyword,Pageable pageable);
}
