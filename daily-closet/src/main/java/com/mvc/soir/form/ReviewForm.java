package com.megait.soir.form;

import com.megait.soir.domain.Item;
import com.megait.soir.domain.Member;
import com.megait.soir.domain.Orders;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

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
