package com.megait.soir.service;

import com.megait.soir.domain.Review;
import com.megait.soir.form.CodyForm;
import com.megait.soir.domain.Cody;
import com.megait.soir.domain.Item;
import com.megait.soir.domain.Member;
import com.megait.soir.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

<<<<<<< HEAD
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

=======
>>>>>>> origin/ssu
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review createNewReview(Item item, String content) {

<<<<<<< HEAD
        Review review = Review.builder()
                .item(item)
                .title(reviewForm.getTitle())
                .content(reviewForm.getContents())
                .member(member)
                .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
=======
        Review review = new Review();
>>>>>>> origin/ssu

        review.setItem(item);
        review.setContent(content);
        reviewRepository.save(review);
        return review;


    }

    public Review createNewReview(Member member, Item item, long parentId, String title, String content) {

        Review review = new Review();

        review.setMember(member);
        review.setItem(item);
        review.setTitle(title);
        review.setContent(content);
        review.setParentId(parentId);
        reviewRepository.save(review);
        return review;

<<<<<<< HEAD
    public void updateReview(Review review, ReviewForm reviewForm) {
=======
    }
>>>>>>> origin/ssu

    public Review createNewReview(Member member, Item item, long parentId, String title, String content, String image) {

        if(updateReview!=null){
            review.setTitle(reviewForm.getTitle());
            review.setContent(reviewForm.getContents());
            review.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        review.setMember(member);
        review.setItem(item);
        review.setTitle(title);
        review.setContent(content);
        review.setParentId(parentId);
        review.setImg(image);
        reviewRepository.save(review);
        return review;

    }

    public Optional<Review> findById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if(review==null){
            new IllegalArgumentException("해당 리뷰가 없습니다. Id "+ reviewId);
        }
        return review;
    }
}
