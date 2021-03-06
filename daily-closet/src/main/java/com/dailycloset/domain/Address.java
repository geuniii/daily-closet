package com.dailycloset.domain;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * νμ μ£Όμ
 */
@Embeddable
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;
}