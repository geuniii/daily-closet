package com.megait.soir.repository;

import com.megait.soir.domain.Cody;
import com.megait.soir.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Query;
=======
>>>>>>> 92340efb077d430eafd5db9e35fa4c111bf0f4b6
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface CodyRepository extends JpaRepository<Cody, Long> {

    public List<Cody> findAllByMember(Member member);

    public List<Cody> findAll();

    public Optional<Cody> findById(Long id);

<<<<<<< HEAD
    @Query(value = "SELECT CODY_LIKES_ID FROM MEMBER_CODY_LIKES GROUP BY CODY_LIKES_ID ORDER BY COUNT(CODY_LIKES_ID) DESC", nativeQuery = true)
    List<String> findRankCodyLikes ();
=======
>>>>>>> 92340efb077d430eafd5db9e35fa4c111bf0f4b6
}
