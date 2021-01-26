package com.shaan.api.myapp.user;

import com.google.common.collect.ImmutableMap;
import com.shaan.api.myapp.BaseComponent;
import com.shaan.api.myapp.domain.User;
import com.shaan.api.myapp.exceptions.ApiBadRequestException;
import com.shaan.api.myapp.exceptions.ApiNotFoundException;
import com.shaan.api.myapp.exceptions.ApiSystemException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.shaan.api.myapp.constant.AppConstant.SC_OK;
import static com.shaan.api.myapp.constant.CustomJsonTagName.*;

@RestController
@RequestMapping({ "/api/v1/user"})
@Api(value = "/users", description = "Endpoint for creating a user")
public class UserController extends BaseComponent {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserService userService;

    /*******************************
     * Create user
     * EndPoint:/api/v1/users
     **********************************/
    @ApiOperation(
            value = "Inserts a user record",
            notes = "Inserts a user record"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Registration successful"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createUser(@Validated @RequestBody UserPayload payload) throws Exception {
        User user = new User();
        try {
            user.setFirstName(payload.getFirstName());
            user.setLastName(payload.getLastName());
            user.setEmail(payload.getEmail());
            user.setPassword(payload.getPassword());
            user = userManager.createUser(user);
            logger.info("user created successfully for the new user : {}", payload.getEmail());
            return getResponse(SUCCESS, getMessage("user.created.successfully"), user, SC_OK);
        } catch (ApiSystemException ex) {
            logger.error("Users were not created {} ", payload.getEmail());
            throw new ApiSystemException(ex.getMessage());
        }
    }

    /*******************************
     * Retrieve user details
     * EndPoint:/api/v1/users/{id}
     * Method: PUT
     **********************************/
    @ApiOperation(
            value = "Retrieve a user information",
            notes = "Retrieve a user information"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of user's record"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserDetailsById(@PathVariable(value = "id") Integer userId)
            throws ApiNotFoundException {
        User user = userService.findById(userId);
        return getResponse(SUCCESS, getMessage("user.details.get.successfully"),
                ImmutableMap.of(FIRST_NAME, user.getFirstName(), LAST_NAME, user.getLastName(),EMAIL, user.getEmail()),
                SC_OK);
    }
}
