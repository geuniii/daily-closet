package com.megait.soir.repository;

import com.megait.soir.domain.Category;
import com.megait.soir.domain.ChildCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChildCategoryRepository extends JpaRepository<ChildCategory, Long> {

    public ChildCategory findOneByName(String name);


}
