package com.dailycloset.domain;

import org.springframework.security.core.GrantedAuthority;

/**
 * νμ λΆλ₯
 */
public enum MemberType implements GrantedAuthority {
    ADMIN, USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
