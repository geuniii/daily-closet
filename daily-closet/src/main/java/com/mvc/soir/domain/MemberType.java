package com.megait.soir.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * 회원 분류
 */
public enum MemberType implements GrantedAuthority {
    ADMIN, USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
