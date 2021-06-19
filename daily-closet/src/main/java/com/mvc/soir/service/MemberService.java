package com.megait.soir.service;

import com.megait.soir.domain.*;
import com.megait.soir.repository.CodyRepository;
import com.megait.soir.repository.ItemRepository;
import com.megait.soir.repository.MemberRepository;
import com.megait.soir.user.MemberUser;
import com.megait.soir.user.SignUpForm;
import com.megait.soir.user.UpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder; // password encoding
    private final ItemRepository itemRepository;
    private final CodyRepository codyRepository;

    /**
     * 회원 정보 수정
     *
     * @param member     : 로그인한 사용자
     * @param updateForm : 수정할 내용
     * @return : 수정된 회원 정보
     */
    @Transactional
    public Member updateMember(Member member, UpdateForm updateForm) {
        Member member1 = memberRepository.getOne(member.getId());
        member1.getAddress().setZipcode(updateForm.getZipcode());
        member1.getAddress().setCity(updateForm.getCity());
        member1.getAddress().setStreet(updateForm.getStreet());
        return member1;
    }

    /**
     * 회원 탈퇴
     *
     * @param id : 탈퇴할 사용자 아이디
     */
    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(()
                -> new IllegalArgumentException("해당 아이디가 없습니다. id=" + id));
        memberRepository.delete(member);
    }

    /**
     * 이메일 검증
     *
     * @param email : 검증 토큰 보낼 이메일
     * @param token : 검증 토큰
     * @return : 검증 상태
     */
    @Transactional
    public EmailCheckStatus processSignUp(String email, String token) {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            return EmailCheckStatus.WRONG_EMAIL;
        }

        if (member.getEmailCheckToken().equals(token)) {

            if (member.isEmailVerified()) {
                return EmailCheckStatus.MODIFIED;
            }

            member.setEmailVerified(true);
            member.setJoinedAt(LocalDateTime.now());

            return EmailCheckStatus.COMPLETE;
        }

        return EmailCheckStatus.WRONG_TOKEN;
    }


    /**
     * 회원가입 확인 이메일
     *
     * @param member : 회원가입한 사용자
     */
    public void sendSignUpEmail(Member member) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(member.getEmail());
        mailMessage.setSubject("Soir: Sign up certification Mail");
        mailMessage.setText(

                "/check-email-token?email=" + member.getEmail() // 인증 링크를 첨부
                        + "&token=" + member.getEmailCheckToken()
        );

        javaMailSender.send(mailMessage);
    }


    /**
     * 신규 회원가입 등록
     *
     * @param signUpForm : 회원가입할 회원 정보
     * @return : 실행 결과
     */
    public Member createNewMember(SignUpForm signUpForm) {
        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .password(signUpForm.getPassword())
                .address(Address.builder()
                        .zipcode(signUpForm.getZipcode())
                        .city(signUpForm.getCity())
                        .street(signUpForm.getStreet())
                        .build())
                .type(MemberType.USER)
                .build();

        member.generateEmailCheckToken();
        member.encodePassword(passwordEncoder);
        memberRepository.save(member);
        return member;
    }

    /**
     * 자동 로그인
     *
     * @param member : 로그인할 회원
     */
    public void autologin(Member member) {
        MemberUser memberUser = new MemberUser(member);

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        memberUser, // username
                        memberUser.getMember().getPassword(), // password
                        memberUser.getAuthorities() // authorities(data type : Collection)
                );

        SecurityContext ctx = SecurityContextHolder.getContext();
        ctx.setAuthentication(token);
    }


    /**
     * 회원 정보 조회
     *
     * @param username : 로그인한 회원 이름
     * @return : 해당 회원
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username);

        if (member == null) {
            throw new UsernameNotFoundException(username);
        }

        return new MemberUser(member);
    }

    /**
     * 비밀번호 재등록을 위한 검증
     *
     * @param email : 토큰 검증 보낼 이메일
     */
    public void sendResetPasswordEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new IllegalArgumentException("잘못된 이메일");
        }

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(member.getEmail());  // 수신자
        mailMessage.setSubject("My Store - 비밀번호 재생성 메일");  // 제목
        mailMessage.setText(
                "/reset-password?email=" + member.getEmail()
                        + "&token=" + member.getEmailCheckToken()
        );

        javaMailSender.send(mailMessage);
    }


    /**
     * 이메일 확인 토큰 검증
     *
     * @param email : 토큰 검증 보낼 이메일
     * @param token : 검증용 토큰
     * @return : 검증 결과
     */
    public boolean checkEmailToken(String email, String token) {
        if (email == null || token == null) {
            return false;
        }

        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            return false;
        }

        return token.equals(member.getEmailCheckToken());
    }

    /**
     * 비밀번호 재설정
     *
     * @param email    : 회원 이메일
     * @param password : 재설정 비밀번호
     */
    @Transactional
    public void resetPassword(String email, String password) {
        Member member = memberRepository.findByEmail(email);
        member.setPassword(password);
        member.encodePassword(passwordEncoder);
    }

    /**
     * 찜 리스트 추가
     *
     * @param member : 로그인한 회원
     * @param itemId : 찜 리스트에 추가할 상품 아이디
     * @return : 실행 결과
     */
    @Transactional
    public boolean addLike(Member member, Long itemId) {
        if (member == null) {
            throw new UsernameNotFoundException("wrong user");
        }

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null) {
            throw new IllegalArgumentException("wrong item info!");
        }

        member = memberRepository.getOne(member.getId());
        List<Item> likeList = member.getLikes();

        if (likeList.contains(item)) {
            likeList.remove(item);
            return false;
        }

        likeList.add(item);
        return true;
    }

    /**
     * 찜 리스트 조회
     *
     * @param member: 로그인한 회원
     * @return : 해당 회원의 찜 리스트
     */
    public List<Item> getLikeList(Member member) {
        return memberRepository.findByEmail(member.getEmail()).getLikes();
    }


    /**
     * 코디 리스트 추가
     *
     * @param member : 로그인한 회원
     * @param codyId : 코디리스트에 추가할 코디 아이디
     * @return : 실행 결과
     */
    @Transactional
    public boolean addCodyLike(Member member, Long codyId) {
        if (member == null) {
            throw new UsernameNotFoundException("wrong user");
        }
        Optional<Cody> optional = codyRepository.findById(codyId);
        Cody cody = optional.get();

        if (cody == null) {
            throw new IllegalArgumentException("wrong cody info!");
        }

        member = memberRepository.getOne(member.getId());

        List<Cody> codyLikeList = member.getCodyLikes();

        if (codyLikeList.contains(cody)) {
            codyLikeList.remove(cody);
            return false;
        }

        codyLikeList.add(cody);
        return true;
    }
}
