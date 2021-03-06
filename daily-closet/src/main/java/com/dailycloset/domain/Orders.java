package com.dailycloset.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * 주문 내역
 */
@Entity
@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class Orders {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private Status status;

}
