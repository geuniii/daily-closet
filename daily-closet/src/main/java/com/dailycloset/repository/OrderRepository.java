package com.dailycloset.repository;

import com.dailycloset.domain.Member;
import com.dailycloset.domain.Orders;
import com.dailycloset.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Orders, Long> {

    /**
     * 주문 조회
     * @param member : 로그인한 회원
     * @param status : 주문 상태
     * @return : 주문
     */
    Orders findByMemberAndStatus(Member member, Status status);
}
