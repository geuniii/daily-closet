package com.dailycloset.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 로그인 유지 상태
 */
@Entity
@Getter @Setter
public class PersistentLogins {

    @Id @Column(length = 64)
    private String series;

    @Column(length = 64)
    private String username;

    @Column(length = 64)
    private String token;

    @Column(nullable = false)
    private LocalDateTime lastUsed;
}
