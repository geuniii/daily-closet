package com.megait.soir.repository;

import com.megait.soir.domain.Member;
import com.megait.soir.domain.Orders;
import com.megait.soir.domain.Status;
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
