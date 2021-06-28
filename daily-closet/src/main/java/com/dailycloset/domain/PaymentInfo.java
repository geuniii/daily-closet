package com.dailycloset.domain;

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * 결제 정보
 */
@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}
