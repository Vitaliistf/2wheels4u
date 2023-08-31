package org.vitaliistf.twowheels4u.security;

import static org.springframework.security.core.userdetails.User.withUsername;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vitaliistf.twowheels4u.model.User;
import org.vitaliistf.twowheels4u.service.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);
        return withUsername(username)
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
