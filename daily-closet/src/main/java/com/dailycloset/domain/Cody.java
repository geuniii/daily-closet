package com.dailycloset.domain;

import lombok.*;

import javax.persistence.*;


/**
 * 상품 코디 조합
 */
@Entity
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Cody {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Member member;

    private long outerId;

    private long topId;

    private long bottomId;

    private long accId;

    private long shoesId;

    private String backgroundId;

    private long outerSize;

    private long topSize;

    private long bottomSize;

    private long shoesSize;

    private long accSize;
//
//    @ManyToMany(mappedBy = "codyLikes",cascade = CascadeType.ALL)
//    private List<Member> likeMember  = new ArrayList<>();

}
