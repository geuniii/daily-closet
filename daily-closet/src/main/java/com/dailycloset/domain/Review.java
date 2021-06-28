package com.dailycloset.domain;

import lombok.*;

import javax.persistence.*;

/**
 * 후기 글
 */
@Entity
@Setter @Getter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String img;

    private long parentId;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Member member;

    // many to one Orders
    @ManyToOne
    private Orders order;

    private String createDate;

    private String updateDate;

}
