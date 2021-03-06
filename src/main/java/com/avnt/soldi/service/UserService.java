package com.avnt.soldi.service;

import com.avnt.soldi.model.authorization.Authority;
import com.avnt.soldi.model.authorization.User;
import com.avnt.soldi.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toMap;

/**
 * Class UserService is service, that handle UserController
 * Use @Autowired for connect to necessary repositories
 *
 * @version 1.1
 * @author Dmitry
 * @since 21.01.2016
 */

@Service
public class UserService implements UserDetailsService {

    @Autowired UserRepository userRepository;

    /**
     * Method passwordEncoder return new BCrypt password encoder
     * @return new BCrypt password encoder
     */
    public PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder();}

    /**
     * Method loadUserByUsername load user by username
     * @param username by that user should be loaded
     * @return UserDetails for current user
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(username);
        if(user == null) throw new UsernameNotFoundException("No user found for username '" + username +"'.");
        return new UserDetailsImpl(user);
    }

    /**
     * Method getUserList return list of users
     * @return list of users
     */
    public List<User> getUserList() {return userRepository.findAll();}

    /**
     * Method saveUser save new user to DB
     * @param user that should be saved to DB
     */
    public void saveUser(User user) {

        if (userRepository.exists(user.getUsername())) {

            User userFromDB = userRepository.findOne(user.getUsername());

            if (user.getNewPassword() == null) {
                user.setPassword(userFromDB.getPassword());
            }
            else if (user.getAdminPassword() != null &&
                    passwordEncoder().matches(user.getAdminPassword(), userRepository.findOne("parinov_a").getPassword())) {
                String password = user.getNewPassword();
                user.setPassword(passwordEncoder().encode(password));
            }
        } else {
            String password = user.getNewPassword();
            user.setPassword(passwordEncoder().encode(password));
        }

        userRepository.save(user);
    }

    private final static class UserDetailsImpl extends User implements UserDetails {

        private UserDetailsImpl(User user) {super(user);}

        public String getFullname() {return super.getFullname();}
        @Override public String getPassword() {return super.getPassword();}
        @Override public String getUsername() {return super.getUsername();}
        @Override public boolean isAccountNonExpired() {return true;}
        @Override public boolean isAccountNonLocked() {return true;}
        @Override public boolean isCredentialsNonExpired() {return true;}
        @Override public boolean isEnabled() {return super.isEnabled();}
        @Override public Set<Authority> getAuthorities() {return super.getAuthorities();}
    }

    public Map<String, String> getUsernameFullnameMap() {
        return userRepository.findAll()
                .stream()
                .collect(toMap(User::getUsername, User::getFullname));
    }
}