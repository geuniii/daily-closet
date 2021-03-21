package com.megait.soir.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Like {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int likeCount;

    // many to one Member
    @ManyToOne
    private Member member;

    @ManyToOne
    private Item item;
}
