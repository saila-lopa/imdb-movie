package com.shaan.api.myapp.authentication;

import com.google.common.collect.ImmutableMap;
import com.shaan.api.myapp.controller.BaseController;
import com.shaan.api.myapp.domain.User;
import com.shaan.api.myapp.exceptions.ApiAuthorizationException;
import com.shaan.api.myapp.exceptions.ApiBadRequestException;
import com.shaan.api.myapp.exceptions.ApiNotFoundException;
import com.shaan.api.myapp.exceptions.ApiSystemException;
import com.shaan.api.myapp.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.shaan.api.myapp.constant.AppConstant.SC_OK;
import static com.shaan.api.myapp.constant.CustomJsonTagName.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RestController
@RequestMapping("/api/v1/authentication")
@Api(value = "/api/v1/authentication", description = "Endpoint for Authentication")
public class AuthenticationController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    /**
     * Authenticate using the username and password and generates a secured access token..
     * EndPoint:/api/v1/authentication/doAuth
     *
     * @param userAuthBean
     * @return
     */
    @ApiOperation(
            value = "Authentication checker",
            notes = "Authentication checker"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authentication successfully done!"),
            @ApiResponse(code = 401, message = "Authentication failed miserably!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(
            value = "/login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> doAuthentication(HttpServletRequest requestContext,
                                              @Validated @RequestBody UserAuthBean userAuthBean)
            throws ApiAuthorizationException, ApiSystemException {
        try {
            User user = userService.findByEmail(userAuthBean.getUsername());
            boolean passwordMatched = authenticationService.passwordValidationForLogin(userAuthBean.getPassword(),
                    user.getPassword());
            if (passwordMatched) {
                Authentication auth = authenticationService.springSecurityLogin(requestContext, user.getUsername(),
                        user.getPassword());
                if (auth.getPrincipal() != null) {
                    return getResponse(SUCCESS, getMessage("log.in.success"),
                            ImmutableMap.of(ID, user.getId(), EMAIL, user.getEmail(),
                                    ACCESS_TOKEN, user.getAccessToken()), SC_OK);
                }
            }

            logger.warn("Bad Credentials combination has been provided : ", passwordMatched);
            throw new ApiAuthorizationException(getMessage("bad.credentials"));
        } catch (ApiNotFoundException | ApiAuthorizationException ex) {
            logger.warn("Bad Credentials combination has been provided");
            throw new ApiAuthorizationException(getMessage("bad.credentials"));
        } catch (ApiSystemException ex) {
            logger.error("error", ex.getLocalizedMessage());
            throw new ApiSystemException(ex.getMessage());
        }
    }

    /**
     * Access token revoke providing access token in the header
     * EndPoint:/api/v1/authentication/logout
     *
     * @param accessToken
     * @return
     */
    @ApiOperation(
            value = "Access token revoke",
            notes = "Access token revoke"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Access token successfully revoked!"),
            @ApiResponse(code = 400, message = "Access token cannot be revoked!"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(value = "/logout", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> revokeToken(HttpServletRequest requestContext,
                                         @RequestHeader("accessToken") String accessToken) throws ApiBadRequestException, ApiSystemException {
        try {
            User securityContextUser = (User) SecurityContextHolder.getContext().getAuthentication().getDetails();

            if (isEmpty(accessToken)) {
                logger.info("Logging out the user , no access token token provided :" + accessToken);
                return getResponse(SUCCESS, getMessage("log.out.success"), SC_OK);
            }

            if (!authenticationService.logoutByAccessToken(securityContextUser)) {
                logger.info("Logging out the user , no valid auth token provided :" + accessToken);
                throw new ApiBadRequestException(getMessage("log.out.error"));
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null) {
                authentication.setAuthenticated(false);
                SecurityContextHolder.clearContext();
                HttpSession session = requestContext.getSession();

                if (session != null)
                    session.invalidate();
            }
            logger.info("Logout is successful.");
            return getResponse(SUCCESS, getMessage("log.out.success"), SC_OK);
        } catch (ApiBadRequestException ex) {
            throw new ApiBadRequestException(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ApiSystemException(ex.getMessage());
        }
    }

}
