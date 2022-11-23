package com.lazyworking.sagupalgu.service;

import com.lazyworking.sagupalgu.domain.RefreshToken;
import com.lazyworking.sagupalgu.domain.Users;
import com.lazyworking.sagupalgu.repository.RefreshTokenRepository;
import com.lazyworking.sagupalgu.repository.UsersRepository;
import com.lazyworking.sagupalgu.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {
    private final UsersRepository usersRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-url}")
    private String kakaoRedirectUrl;
    @Value("${spring.security.oauth2.client.provider.kakao.token-url}")
    private String kakaoTokenHostUrl;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-url}")
    private String kakaoUserInfoUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String kakaoAuthorizationType;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-url}")
    private String naverRedirectUrl;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
    @Value("${spring.security.oauth2.client.provider.naver.token-url}")
    private String naverTokenHostUrl;
    @Value("${spring.security.oauth2.client.registration.naver.authorization-grant-type}")
    private String naverAuthorizationType;
    @Value("${spring.security.oauth2.client.provider.naver.user-info-url}")
    private String naverUserInfoUrl;

    public String getStringFromAPI(String url){
        try{
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

            ResponseHandler<String> handler = new BasicResponseHandler();
            httpClient.close();
            httpResponse.close();

            return handler.handleResponse(httpResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getAccessToken(String tokenUrl){
        try {
            String body = getStringFromAPI(tokenUrl);
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(body);

            return String.valueOf(jsonObject.get("access_token"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getUserInfo(String accessToken, String url){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        String res = restTemplate.postForObject(
                url,
                request,
                String.class
        );

        JSONParser jsonParser = new JSONParser();
        try {
            return (JSONObject) jsonParser.parse(res);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public HashMap<String, String> returnTokens(Users user){
        HashMap<String, String> tokens = new HashMap<>();
        
        String accessToken = jwtTokenProvider.createToken(user.getEmail(), user.getRoles(), "AccessToken");
        String refreshToken = jwtTokenProvider.createToken(user.getEmail(), user.getRoles(), "RefreshToken");
        
        RefreshToken userRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(userRefreshToken);
        
        tokens.put("AccessToken", accessToken);
        tokens.put("RefreshToken", refreshToken);
        
        return tokens;
    }

    public HashMap<String, String> kakaoLogin(String code){
        String authorizationUrl = kakaoTokenHostUrl
                + "?grant_type=" + kakaoAuthorizationType
                + "&client_id=" + kakaoClientId
                + "&redirect_url=" + kakaoRedirectUrl
                + "&code=" + code;
        String accessToken = getAccessToken(authorizationUrl);

        JSONObject userInfo = getUserInfo(accessToken, kakaoUserInfoUrl);
        String oauthId = String.valueOf(userInfo.get("id"));

        Users user = usersRepository.findByOauthId(oauthId).orElseGet(() ->{
            JSONObject kakao_account = (JSONObject) userInfo.get("kakao_account");
            String email = String.valueOf(kakao_account.get("email"));
            String gender = String.valueOf(kakao_account.get("gender"));
            if (gender.equals("male")){
                gender = "M";
            }else{
                gender = "F";
            }

            return Users.builder()
                    .email(email)
                    .joinType("K")
                    .gender(gender)
                    .joinDate(new Date())
                    .oauthId(oauthId)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        });

        usersRepository.save(user);
        return returnTokens(user);
    }

    public HashMap<String, String> naverLogin(String code){
        String authorizationUrl = naverTokenHostUrl
                + "?grant_type=" + naverAuthorizationType
                + "&client_id=" + naverClientId
                + "&client_secret=" + naverClientSecret
                + "&code=" + code
                + "&state=";
        String accessToken = getAccessToken(authorizationUrl);

        JSONObject userInfo = (JSONObject) getUserInfo(accessToken, naverUserInfoUrl).get("response");
        String oauthId = String.valueOf(userInfo.get("id"));

        Users user = usersRepository.findByOauthId(oauthId).orElseGet(() -> {
//            String nickname = String.valueOf(userInfo.get("name"));
            String email = String.valueOf(userInfo.get("email"));
            String gender = String.valueOf(userInfo.get("gender"));

            return Users.builder()
                    .email(email)
                    .joinType("N")
                    .gender(gender)
                    .joinDate(new Date())
                    .oauthId(oauthId)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        });

        usersRepository.save(user);
        return returnTokens(user);
    }
}
