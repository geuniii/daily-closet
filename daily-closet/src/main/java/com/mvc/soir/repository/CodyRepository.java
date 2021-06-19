package com.megait.soir.repository;

import com.megait.soir.domain.Cody;
import com.megait.soir.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CodyRepository extends JpaRepository<Cody, Long> {

    /**
     * 회원 별 코디 리스트 조회
     * @param member : 로그인한 회원
     * @return : 회원이 만든 모든 코디 리스트
     */
    public List<Cody> findAllByMember(Member member);

    /**
     * 모든 코디 리스트 조회
     * @return : 모든 코디 리스트
     */
    public List<Cody> findAll();

    /**
     * 해당 코디 조회
     * @param id :코디 아이디
     * @return 해당 코디
     */
    public Optional<Cody> findById(Long id);

    /**
     * 코디 찜 랭킹 조회
     * @return 코디 순위
     */
    @Query(value = "SELECT CODY_LIKES_ID FROM MEMBER_CODY_LIKES GROUP BY CODY_LIKES_ID ORDER BY COUNT(CODY_LIKES_ID) DESC", nativeQuery = true)
    List<String> findRankCodyLikes ();
}
