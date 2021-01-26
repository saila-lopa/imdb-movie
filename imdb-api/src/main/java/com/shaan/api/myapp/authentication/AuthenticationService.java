package com.shaan.api.myapp.authentication;

import com.shaan.api.myapp.SecurityConfig;
import com.shaan.api.myapp.algorithm.HashGeneratorAlgorithm;
import com.shaan.api.myapp.domain.User;
import com.shaan.api.myapp.exceptions.ApiAuthorizationException;
import com.shaan.api.myapp.exceptions.ApiBadRequestException;
import com.shaan.api.myapp.exceptions.ApiNotFoundException;
import com.shaan.api.myapp.exceptions.ApiSystemException;
import com.shaan.api.myapp.user.UserService;
import com.shaan.api.myapp.utils.security.TokenManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;

import static com.shaan.api.myapp.authentication.AuthenticationAndAuthorizationConstant.ACCESS_TOKEN_EXPIRED_MINUTES;

@Service
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest request;

    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();

    public Authentication springSecurityLogin(HttpServletRequest requestContext, String userName, String password) throws ApiAuthorizationException {
        try {
            // Authenticate using AuthenticationManager configured on SecurityContext
            UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(userName, password);
            authReq.setDetails(authenticationDetailsSource.buildDetails(requestContext));
            Authentication authResp = securityConfig.authenticationManagerBean().authenticate(authReq);

            logger.info("User name :" + authResp.getName());
            logger.info("User principle :" + authResp.getPrincipal());

            return authResp;
        } catch (ApiSystemException | AuthenticationException ex) {
            throw new ApiAuthorizationException("bad.credentials");
        }
    }

    User authenticateUser(final String email, final String password) {
        try {
            User user = userService.findByUsernameAndPassword(email, password);
            if(user == null)
                return null;
            user.setAccessTokenCreationTime(LocalDateTime.now());
            user.setAuthorities();
            return updateUserAccessDetails(user);
        } catch (ApiNotFoundException e) {
            logger.info("User cannot be fetched due to : '{}'", e);
            return null;
        }
    }

    private User updateUserAccessDetails(User user) {
        try {
            TokenManager generateToken = new TokenManager();
            String accessToken = generateToken.getToken();
            if (accessToken == null) {
                return null;
            }
            user.setAccessToken(accessToken);
            user.setLoginDateTime(LocalDateTime.now());
            LocalDateTime accessTokenExpiryTime = LocalDateTime.now().plusMinutes(
                    ACCESS_TOKEN_EXPIRED_MINUTES.getValue());
            user.setAccessTokenCreationTime(LocalDateTime.now());
            user.setAccessTokenExpiryTime(accessTokenExpiryTime);
            user.setUserLogged(true);
            logger.info("The acces token expire time is " + accessTokenExpiryTime);
            userService.update(user);

        } catch (ApiBadRequestException ex) {
            logger.error("Updating user details failed :" + ex);
        }

        return user;
    }

    public Boolean logoutByAccessToken(User user) throws ApiBadRequestException {
        try {
            logger.debug(">> logout()");
            logger.info("Retrieving user by session token");
            if (user != null) {
                user.setLogoutDateTime(LocalDateTime.now());
                user.setAccessTokenExpiryTime(null);
                user.setUserLogged(false);
                logger.info("Updating user.");
                User logoutUser = userService.update(user);
                logger.info("User Updated an logout");
                return logoutUser != null ;
            }
        } catch (ApiBadRequestException e) {
            logger.error("Logging out the user , no auth token provided :" + e);
            throw e;
        }
        return false;
    }

    public Boolean passwordValidationForLogin(String checkPassword, String password) throws ApiSystemException {
        try {
            return HashGeneratorAlgorithm.validateHash(checkPassword, password);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Error while validating Password", e);
            throw new ApiSystemException("error validating password");
        }
    }
}
