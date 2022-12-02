package com.lazyworking.sagupalgu.refreshToken.domain;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name="refresh_token")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RefreshToken {
    @Id
    private long userId;

    @Column(length = 300)
    @NotNull
    private String refreshToken;
}
