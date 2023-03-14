package com.lazyworking.sagupalgu.global.security.webdetails;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

    private  String secretKey;

    public FormWebAuthenticationDetails(HttpServletRequest request) {
        super(request);
        secretKey = request.getParameter("secret_key");
    }

    public String getSecretKey() {

        return secretKey;
    }
}