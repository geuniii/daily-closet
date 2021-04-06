package com.megait.soir.repository;

import com.megait.soir.domain.Cody;
import com.megait.soir.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodyRepository extends JpaRepository<Cody, Long> {

    public List<Cody> findAllByMember(Member member);
}

