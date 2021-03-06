package com.dailycloset.form;

import lombok.Data;

/**
 * 상품 코디 조합 내용
 */
@Data
public class CodyForm {

    private Long outerId;
    private Long topId;
    private Long bottomId;
    private Long accId;
    private Long shoesId;

    private String backgroundId;
    private Long outerSize;
    private Long topSize;
    private Long bottomSize;
    private Long shoesSize;
    private Long accSize;

}
