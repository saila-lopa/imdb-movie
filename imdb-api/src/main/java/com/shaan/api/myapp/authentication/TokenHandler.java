package com.shaan.api.myapp.authentication;

import com.shaan.api.myapp.domain.Roles;
import com.shaan.api.myapp.domain.User;
import com.shaan.api.myapp.exceptions.ApiSystemException;
import com.shaan.api.myapp.user.UserSecurityDetailsService;
import com.shaan.api.myapp.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.shaan.api.myapp.authentication.AuthenticationAndAuthorizationConstant.ACCESS_TOKEN_EXPIRED_MINUTES;
import static com.shaan.api.myapp.constant.AppConstant.ROLE_ADMIN;
import static com.shaan.api.myapp.constant.AppConstant.ROLE_USER;

@Service
public final class TokenHandler {
    private static final Logger logger = LoggerFactory.getLogger(TokenHandler.class);

    @Autowired
    private UserSecurityDetailsService userSecurityService;

    @Autowired
    private UserService userService;

    User parseUserFromToken(final String token) {
        try {
            User user = userSecurityService.loadUserByAccessToken(token);

            if (user == null) {
                return null;
            }

            LocalDateTime currentDate = LocalDateTime.now();

            if (!currentDate.isAfter(user.getAccessTokenExpiryTime())) {
                updateTokenExpiryTime(user);
                return user;
            }
        } catch (UsernameNotFoundException | ApiSystemException e) {
            logger.info("Exception occurred due to : ", e.getMessage());
        }

        return null;
    }

    private User updateTokenExpiryTime(User user) throws ApiSystemException {
        try {
            long expireMins = ACCESS_TOKEN_EXPIRED_MINUTES.getValue();
            LocalDateTime newAccessTokenExpiryTime = LocalDateTime.now().plusMinutes(expireMins);
            user.setAccessTokenExpiryTime(newAccessTokenExpiryTime);
            userService.update(user);
        } catch (Exception ex) {
            logger.error("Major exception occurred during updating token expiry time", ex);
            throw new ApiSystemException("Major Exception occurred during updating token expiry time");
        }

        return user;
    }

    private Set<Roles> setAdminRoles() {
        Set<Roles> adminRoles = new HashSet<>();
        adminRoles.addAll(setRoles(ROLE_ADMIN));
        adminRoles.addAll(setRoles(ROLE_USER));
        return adminRoles;
    }

    private Set<Roles> setUserRoles() {
        return setRoles(ROLE_ADMIN);
    }

    private Set<Roles> setRoles(String role) {
        Set<Roles> userRoles = new HashSet<>();
        Roles roles = new Roles();
        roles.setRole(role);
        userRoles.add(roles);
        return userRoles;
    }
}
