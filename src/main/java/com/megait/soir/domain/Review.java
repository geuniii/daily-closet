package com.megait.soir.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter @Getter
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reviewTitle;
    private String reviewContent;
    private String reviewImage;
    private LocalDateTime reviewDate;

    // many to one Orders
    @ManyToOne
    private Orders order;
}
