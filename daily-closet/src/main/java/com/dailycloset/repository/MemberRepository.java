package com.dailycloset.repository;

import com.dailycloset.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일 존재 조회
     * @param email : 조회할 이메일
     * @return : 존재 여부
     */
    public boolean existsByEmail(String email); // email 검색용 method

    /**
     * 사용자 조회
     * @param email : 조회할 이메일
     * @return : 해당 이메일의 사용자
     */
    public Member findByEmail(String email);

}
