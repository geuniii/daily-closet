package com.dailycloset.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 배송
 */
@Entity
@Setter @Getter
public class Delivery {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ShippingInfo shippingInfo;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @OneToOne
    private Orders order;
}
