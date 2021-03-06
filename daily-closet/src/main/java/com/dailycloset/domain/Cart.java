package com.dailycloset.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 장바구니
 */
@Entity
@Getter @Setter
public class Cart {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cartCount;

    @ManyToOne
    private Member member;
}
