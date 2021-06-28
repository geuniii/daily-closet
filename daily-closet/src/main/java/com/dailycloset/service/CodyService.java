package com.dailycloset.service;

import com.dailycloset.domain.Cody;
import com.dailycloset.domain.Member;
import com.dailycloset.form.CodyForm;
import com.dailycloset.repository.CodyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodyService {

    private final CodyRepository codyRepository;

    /**
     * 새 코디 조합 추가
     * @param member : 로그인한 사용자
     * @param codyForm : 코디 조합
     * @return : 새 코디 조합
     */
    public Cody createNewCody(Member member, CodyForm codyForm) {

        if (codyForm.getTopId() == null) {
            codyForm.setTopId(0L);
        }

        Cody cody = Cody.builder()
                .member(member)
                .topId(codyForm.getTopId())
                .bottomId(codyForm.getBottomId())
                .shoesId(codyForm.getShoesId())
                .backgroundId(codyForm.getBackgroundId())
                .topSize(codyForm.getTopSize())
                .bottomSize(codyForm.getBottomSize())
                .shoesSize(codyForm.getShoesSize())
                .build();

        if (codyForm.getOuterId() != null) {
            cody.setOuterId(codyForm.getOuterId());
            cody.setOuterSize(codyForm.getOuterSize());
        }

        if (codyForm.getAccId() != null) {
            cody.setAccId(codyForm.getAccId());
            cody.setAccSize(codyForm.getAccSize());
        }

        codyRepository.save(cody);

        return cody;

    }

    /**
     * 사용자의 코디 조합 리스트 조회
     * @param member : 로그인한 사용자
     * @return : 사용자의 코디 조합 리스트
     */
    public List<Cody> getCodyList(Member member) {
        List<Cody> codyList = codyRepository.findAllByMember(member);
        return codyList;
    }

    /**
     * 모든 코디 조합 리스트 조회
     * @return : 모든 코디 조합 리스트
     */
    public List<Cody> getAllList() {
        List<Cody> codyList = codyRepository.findAll();
        return codyList;
    }

    /**
     * 코디 조회
     * @param codyId : 조회할 코디 아이디
     * @return : 해당 코디
     */
    public Cody getOne(Long codyId) {
        return codyRepository.getOne(codyId);
    }

    /**
     * 코디 랭킹 조회
     * @return : 순위별 코디 조합
     */
    public List<String> codyLikeRank() {
        List<String> codyList = codyRepository.findRankCodyLikes();
        return codyList;
    }
}
