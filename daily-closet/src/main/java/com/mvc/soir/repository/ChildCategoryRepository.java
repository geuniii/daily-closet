package com.megait.soir.repository;

import com.megait.soir.domain.ChildCategory;
import com.megait.soir.domain.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildCategoryRepository extends JpaRepository<ChildCategory, Long> {

    /**
     * 하위 카테고리 명으로 찾기
     * @param name : 하위 카테고리 명
     * @return : 하위 카테고리
     */
    public ChildCategory findByname(String name);

}
