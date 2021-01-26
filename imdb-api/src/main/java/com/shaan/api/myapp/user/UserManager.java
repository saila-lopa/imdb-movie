package com.shaan.api.myapp.user;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.shaan.api.myapp.BaseComponent;
import com.shaan.api.myapp.algorithm.HashGeneratorAlgorithm;
import com.shaan.api.myapp.domain.Roles;
import com.shaan.api.myapp.domain.User;
import com.shaan.api.myapp.exceptions.ApiNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.shaan.api.myapp.constant.AppConstant.ROLE_USER;

@Component
public class UserManager extends BaseComponent {
    static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;

    public User createUser(User user) throws Exception {
        int noOfTry = 0;
        String password = "";
        boolean isRegistered = false;

        if (Objects.isNull(user))
            throw new Exception("No user provided");
        try {
            User existingUser = userService.findByEmail(user.getEmail());
            throw new Exception(getMessage("user.email.exist"));
        } catch (ApiNotFoundException e) {
        }
        String encryptedPasswordHash = null;
        do {
            encryptedPasswordHash = HashGeneratorAlgorithm.generateHash(user.getPassword());
            noOfTry++;
        } while (Strings.isNullOrEmpty(encryptedPasswordHash) && noOfTry <= 5);
        user.setPassword(encryptedPasswordHash);
        user.setEnabled(true);
        user.setRoles(ImmutableList.of(roleService.getRoleByName(ROLE_USER)));
        return userRepository.save(user);
    }

}
