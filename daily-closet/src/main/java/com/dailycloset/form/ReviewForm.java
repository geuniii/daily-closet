package com.dailycloset.form;

import lombok.Data;

/**
 * 후기 글 내용
 */
@Data
public class ReviewForm {
    private Long itemId;
    private Long reviewId; //수정일 경우 존재
    private String title;
    private String contents;
}
