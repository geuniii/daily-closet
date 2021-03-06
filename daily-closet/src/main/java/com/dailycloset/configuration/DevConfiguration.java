package com.dailycloset.configuration;

import com.dailycloset.domain.Member;
import com.dailycloset.domain.MemberType;
import com.dailycloset.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

/**
 * Test 계정 생성
 */
@Profile("dev")
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DevConfiguration {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @PostConstruct
    public void createTestUser() {
        Member member = Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1111"))
                .type(MemberType.USER)
                .build();
        memberRepository.save(member);
    }
}
