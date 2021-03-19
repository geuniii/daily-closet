package com.megait.soir.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Answer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String answerTitle;
    private String answerContent;
    private LocalDateTime answerDate;

    // one to one Question
    @OneToMany(mappedBy = "question")
    private Question question;

}
