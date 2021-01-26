package com.shaan.api.myapp.authentication;

import com.shaan.api.myapp.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenAuthenticationService {

    private static final String AUTH_NAME = "accessToken";

    @Autowired
    private TokenHandler tokenHandler;

    public Authentication getAuthentication(final HttpServletRequest request) {
        String token = !request.getRequestURI().contains("download") ? request.getHeader(AUTH_NAME) :
                request.getParameter(AUTH_NAME);

        if (token != null) {
            final User user = tokenHandler.parseUserFromToken(token);
            if (user != null) {
                user.setAuthorities();
                return new UserAuthentication(user);
            }
        }

        return null;
    }
}
