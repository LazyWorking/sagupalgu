package com.lazyworking.sagupalgu.global.security;

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
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt_key}")
    private String secretKey;

    private final UsersRepository userRepository;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String memberId, List<String> roles, String tokenType){
        Claims claims = Jwts.claims().setSubject(memberId);
        claims.put("roles", roles);
        Date now = new Date();

        long tokenValidTime;
        if (tokenType.equals("AccessToken")){
            tokenValidTime = 30 * 60 * 1000L; // 30분
        }else{
            tokenValidTime = 14 * 24 * 60 * 60 * 1000L; //2주
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token){
        Users user = this.findUser(token);
        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    public Users findUser(String token){
        String oauthId = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

        return userRepository.findByOauthId(oauthId).orElseThrow(() ->{
            throw new UsernameNotFoundException("User not Found");
        });
    }

    public String resolveToken(HttpServletRequest request, String tokenType){
        return request.getHeader(tokenType);
    }

    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }
}
