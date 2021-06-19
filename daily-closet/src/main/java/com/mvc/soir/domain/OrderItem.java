package com.megait.soir.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 주문 상품
 */
@Entity
@Getter @Setter
public class OrderItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private int orderPrice;

    private int count;
}
