package com.dailycloset.service;

import com.dailycloset.domain.Item;
import com.dailycloset.domain.Member;
import com.dailycloset.domain.Review;
import com.dailycloset.form.ReviewForm;
import com.dailycloset.repository.ReviewRepository;
import com.megait.soir.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 후기 글 작성
     * @param member : 로그인한 사용자
     * @param item : 후기 쓸 상품
     * @param reviewForm : 후기 글 제목, 내용
     */
    public void createReview(Member member, Item item, ReviewForm reviewForm) {

        Review review = Review.builder()
                .item(item)
                .title(reviewForm.getTitle())
                .content(reviewForm.getContents())
                .member(member)
                .createDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();

        reviewRepository.save(review);
    }

    /**
     * 후기 글 삭제
     * @param reviewId : 후기 글 아이디
     */
    public void deleteReview(long reviewId) {

        Review review = reviewRepository.findById(reviewId);

        if(reviewRepository.findById(reviewId)==null){
            new IllegalArgumentException("해당 리뷰가 없습니다. Id "+ reviewId);
        }
        reviewRepository.delete(review);
    }

    /**
     * 후기 글 수정
     * @param review : 후기 글
     * @param reviewForm : 수정할 제목, 내용
     */
    public void updateReview(Review review, ReviewForm reviewForm) {

        Optional<Review> updateReview = reviewRepository.findById(review.getId());

        if(updateReview!=null){
            review.setTitle(reviewForm.getTitle());
            review.setContent(reviewForm.getContents());
            review.setUpdateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        reviewRepository.save(review);
    }

    /**
     * 후기 글 찾기
     * @param reviewId : 찾을 후기 글 아이디
     * @return : 해당 후기 글
     */
    public Optional<Review> findById(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        if(review==null){
            new IllegalArgumentException("해당 리뷰가 없습니다. Id "+ reviewId);
        }
        return review;
    }
}
