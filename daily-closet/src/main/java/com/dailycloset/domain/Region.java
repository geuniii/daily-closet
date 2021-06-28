package com.dailycloset.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * 날씨 조회할 지역
 */
@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region {

    @Id @GeneratedValue
    private Long id;
    private String regionWeatherId;
    private String regionTemperatureId;
    private String city;
}
