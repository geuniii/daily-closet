package com.megait.soir.domain;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * 회원 주소
 */
@Embeddable
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;
}