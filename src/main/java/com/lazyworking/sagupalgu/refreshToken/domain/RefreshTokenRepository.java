package com.lazyworking.sagupalgu.refreshToken.domain;

import com.lazyworking.sagupalgu.refreshToken.repository.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(long userId);
}
