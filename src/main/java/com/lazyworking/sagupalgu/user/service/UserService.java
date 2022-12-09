package com.lazyworking.sagupalgu.user.service;

import com.lazyworking.sagupalgu.refreshToken.repository.RefreshToken;
import com.lazyworking.sagupalgu.user.domain.Users;
import com.lazyworking.sagupalgu.refreshToken.domain.RefreshTokenRepository;
import com.lazyworking.sagupalgu.user.repository.UsersRepository;
import com.lazyworking.sagupalgu.global.security.JwtTokenProvider;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    public void returnUserInfo(HttpServletResponse response, Users user){
        String accessToken = jwtTokenProvider.createAccessToken(user.getOauthId(), user.getRoles());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getOauthId(), user.getRoles());
        
        RefreshToken userRefreshToken = RefreshToken.builder()
                .userId(user.getId())
                .refreshToken(refreshToken)
                .build();
        refreshTokenRepository.save(userRefreshToken);

        jwtTokenProvider.setHeaderAccessToken(response, accessToken);
        jwtTokenProvider.setHeaderRefreshToken(response, refreshToken);
    }

    public HashMap<String, Boolean> kakaoLogin(HttpServletResponse response, String code){
        HashMap<String, Boolean> hashMap = new HashMap<>();
        hashMap.put("newUser", false);

        String authorizationUrl = kakaoTokenHostUrl
                + "?grant_type=" + kakaoAuthorizationType
                + "&client_id=" + kakaoClientId
                + "&redirect_url=" + kakaoRedirectUrl
                + "&code=" + code;
        String accessToken = getAccessToken(authorizationUrl);

        JSONObject userInfo = getUserInfo(accessToken, kakaoUserInfoUrl);
        String oauthId = String.valueOf(userInfo.get("id"));

        Users user = usersRepository.findByOauthId(oauthId).orElseGet(() ->{
            hashMap.put("newUser", true);

            JSONObject kakao_account = (JSONObject) userInfo.get("kakao_account");
            String nickname = String.valueOf(kakao_account.get("nickname"));
            String email = String.valueOf(kakao_account.get("email"));
            String gender = String.valueOf(kakao_account.get("gender"));
            if (gender.equals("male")){
                gender = "M";
            }else{
                gender = "F";
            }

            Users newUser = Users.builder()
                    .email(email)
                    .joinType("K")
                    .gender(gender)
                    .joinDate(new Date())
                    .oauthId(oauthId)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            return usersRepository.save(newUser);
        });

        returnUserInfo(response, user);
        return hashMap;
    }

    public HashMap<String, Boolean> naverLogin(HttpServletResponse response, String code){
        HashMap<String, Boolean> hashMap = new HashMap<>();
        hashMap.put("newUser", false);

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
            hashMap.put("newUser", true);

            String nickname = String.valueOf(userInfo.get("name"));
            String email = String.valueOf(userInfo.get("email"));
            String gender = String.valueOf(userInfo.get("gender"));

            Users newUser = Users.builder()
                    .email(email)
                    .joinType("N")
                    .gender(gender)
                    .joinDate(new Date())
                    .oauthId(oauthId)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
            return usersRepository.save(newUser);
        });

        returnUserInfo(response, user);
        return hashMap;
    }

    public void setNickName(HttpServletRequest request, String nickName){
        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String oauthId = jwtTokenProvider.getOauthId(accessToken);

        Users user = usersRepository.findByOauthId(oauthId).orElseThrow(() -> {
            throw new RuntimeException("회원가입 안됐으니까 다시 고치셈 ㅋㅋ");
        });
        user.setName(nickName);
    }
}
