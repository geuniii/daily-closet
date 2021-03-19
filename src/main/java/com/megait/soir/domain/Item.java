package com.megait.soir.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Item {

    @Id
    private Long id;

    private String brand;
    private String name;
    private long price;
    private String code;
    private int liked;

//    @ManyToMany(mappedBy = "sizes")
//    private List<String> size; // TODO : 이거 List말고 그냥 배열로 넣으면 안되까

    private String img_name;

    @Transient
    private  ArrayList<String> urlArr;

    // TODO : 이거 List<String>으로 해도되지않을까..???
    @Column(name="image1")
    private String image1;

    @Column(name="image2")
    private String image2;

    @Column(name="image3")
    private String image3;

    @Column(name="image4")
    private String image4;

    @Column(name="image5")
    private String image5;

    @Column(name="image6")
    private String image6;

    @Column(name="image7")
    private String image7;

    @Column(name="image8")
    private String image8;

    @Column(name="image9")
    private String image9;

    @Column(name="image10")
    private String image10;



    //    @OneToMany(mappedBy = "item")
//    private List<String> imageUrl;

//    @ManyToMany(mappedBy = "items")
//    private List<Category> categories;


}
