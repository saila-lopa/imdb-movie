package com.shaan.api.myapp.user;


import com.shaan.api.myapp.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ekansh
 * @since 2/4/16
 */
@Transactional
@Service
public class UserSecurityDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSecurityDetailsService.class);

    private UserRepository userRepository;

    public UserSecurityDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(username);
            if (user == null) {
                LOGGER.debug("user not found with the provided username");
                return null;
            }
            LOGGER.debug(" user from username " + user.toString());
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public User loadUserByAccessToken(String accessToken) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepository.findByAccessToken(accessToken);
            if (user == null) {
                LOGGER.debug("user not found with the provided username");
                return null;
            }
            LOGGER.debug(" user from username " + user.toString());
            return user;
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        } finally {
            LOGGER.debug(" user is = " + user);
        }
    }

    private Set<GrantedAuthority> getAuthorities(User user) {
        return user.getRoles()
                .stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toSet());
    }


}
