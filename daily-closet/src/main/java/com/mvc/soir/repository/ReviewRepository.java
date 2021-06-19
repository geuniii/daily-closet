package com.megait.soir.repository;

import com.megait.soir.domain.Item;
import com.megait.soir.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 후기 조회
     * @param id : 후기 게시물 아이디
     * @return : 해당 후기
     */
    public Review findById(long id);

}
