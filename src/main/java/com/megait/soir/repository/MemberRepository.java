package com.megait.soir.repository;

<<<<<<< HEAD
import com.megait.soir.domain.Cody;
=======
>>>>>>> 92340efb077d430eafd5db9e35fa4c111bf0f4b6
import com.megait.soir.domain.Item;
import com.megait.soir.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public boolean existsByEmail(String email); // email 검색용 method

    public Member findByEmail(String email);

}
