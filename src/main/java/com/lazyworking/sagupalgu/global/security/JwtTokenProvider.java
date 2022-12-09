package com.lazyworking.sagupalgu.global.security;

import com.lazyworking.sagupalgu.refreshToken.repository.RefreshTokenRepository;
import com.lazyworking.sagupalgu.user.domain.Users;
import com.lazyworking.sagupalgu.user.repository.UsersRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt_key}")
    private String secretKey;

    private final UsersRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    long accessTokenValidTime = 30 * 60 * 1000L; // 30분
    long refreshTokenValidTime = 14 * 24 * 60 * 60 * 1000L; //2주

    public String createAccessToken(String memberId, List<String> roles){
        return createToken(memberId, roles, accessTokenValidTime);
    }

    public String createRefreshToken(String memberId, List<String> roles){
        return createToken(memberId, roles, refreshTokenValidTime);
    }
    public String createToken(String memberId, List<String> roles, long tokenValidTime){
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("roles", roles);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getOauthId(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public Authentication getAuthentication(String token){
        Users user = userRepository.findByOauthId(this.getOauthId(token)).orElseThrow(() -> {
            throw new UsernameNotFoundException("User is not exist");
        });

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public String resolveAccessToken(HttpServletRequest request){
        if(request.getHeader("Authorization") != null) {
//            return request.getHeader("accessToken");
            return request.getHeader("Authorization").split(" ")[1];
        }
        return null;
    }
    public String resolveRefreshToken(HttpServletRequest request){
        if(request.getHeader("refreshToken") != null) {
//            return request.getHeader("refreshToken");
            return request.getHeader("refreshToken").split(" ")[1];
        }
        return null;
    }


    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader("Authorization", "bearer "+ accessToken);
    }

    // 리프레시 토큰 헤더 설정
    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }

    public boolean existsRefreshToken(String refreshToken){
        return refreshTokenRepository.existsByRefreshToken(refreshToken);
    }

    public List<String> getRoles(String oauthId) {
        return userRepository.findByOauthId(oauthId).orElseThrow(() -> {
           throw new UsernameNotFoundException("User is not exist");
        }).getRoles();
    }
}
