package com.megait.soir.domain;

import lombok.*;

<<<<<<< HEAD
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
=======
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
>>>>>>> 92340efb077d430eafd5db9e35fa4c111bf0f4b6


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
<<<<<<< HEAD
//
//    @ManyToMany(mappedBy = "codyLikes",cascade = CascadeType.ALL)
//    private List<Member> likeMember  = new ArrayList<>();
=======
>>>>>>> 92340efb077d430eafd5db9e35fa4c111bf0f4b6

}
