package com.megait.soir.domain;


/**
 * 배송 정보
 */

import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShippingInfo {

    // EnumType일 경우
    @Enumerated(EnumType.STRING)
    private Receiver receiver;

    @Enumerated(EnumType.STRING)
    private Address address;

}
