package com.megait.soir.repository;

import com.megait.soir.domain.Item;
import com.megait.soir.domain.Review;
import com.megait.soir.domain.ReviewDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

//    public List<Review> findAll(Item item);

    public Review findById(long id);

    public Review findByItem(Item item);

//    @Query("INSERT INTO Review VALUES(review)")
//    public void insert(Review review);
//
//    @Query("UPDATE Review SET contents=  where id=#{id}")
//    public void update(Review review);

//    @Query("DELETE FROM Review r where r.id = id")
//    public void delete(long id);


}
