package com.dailycloset.domain;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * 수령인 정보
 */
@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Receiver {
    private String name;
    private String phone;
    private String email;
}
