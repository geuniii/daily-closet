package com.megait.soir.form;

import com.megait.soir.domain.Item;
import com.megait.soir.domain.Member;
import com.megait.soir.domain.Orders;
import lombok.Data;

import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

/**
 * 상품 검색 내용
 */
@Data
public class SearchForm {

    private String option;
    private String keyword;

}
