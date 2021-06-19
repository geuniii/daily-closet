package com.megait.soir.repository;

import com.megait.soir.domain.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentCategoryRepository extends JpaRepository<ParentCategory, Long> {

    /**
     * 상위 카테고리 조회
     * @param name : 카테고리 명
     * @return : 해당 카테고리
     */
    public ParentCategory findByName(String name);

}
