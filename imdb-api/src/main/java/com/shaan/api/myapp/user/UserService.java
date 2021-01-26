package com.shaan.api.myapp.user;

import com.google.gson.Gson;
import com.shaan.api.myapp.BaseComponent;
import com.shaan.api.myapp.domain.User;
import com.shaan.api.myapp.exceptions.ApiBadRequestException;
import com.shaan.api.myapp.exceptions.ApiNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService extends BaseComponent {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private Gson gson;
    @Autowired
    private UserRepository userDao;


    UserService(UserRepository userDao) {
        this.userDao = userDao;
    }

    public User findByEmail(String email) throws ApiNotFoundException {
        try {
            User user = userDao.findByEmail(email);
            if (user == null) {
                throw new ApiNotFoundException("user.not.found");
            }

            return user;
        } catch (Exception ex) {
            throw new ApiNotFoundException("user.not.found");
        }
    }
    public User findById(Integer id) throws ApiNotFoundException {
        try {
            Optional<User> user = userDao.findById(id);
            if (!user.isPresent()) {
                throw new ApiNotFoundException("user.not.found");
            }
            return user.get();
        } catch (Exception ex) {
            throw new ApiNotFoundException("user.not.found");
        }
    }

    public User update(User user) throws ApiBadRequestException {
        try {
            return userDao.save(user);
        } catch (Exception ex) {
            throw new ApiBadRequestException("user.update.failed");
        }
    }

    public User findByUsernameAndPassword(String email, String password) throws ApiNotFoundException {
        try {
            User user = userDao.findByEmailAndPassword(email, password);

            if (Objects.isNull(user))
                throw new ApiNotFoundException("user.not.found");

            return user;
        } catch (Exception ex) {
            throw new ApiNotFoundException("user.not.found");
        }
    }
}
