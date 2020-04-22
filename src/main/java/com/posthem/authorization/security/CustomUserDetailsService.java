package com.posthem.authorization.security;

import com.posthem.authorization.common.exception.ResourceNotFoundException;
import com.posthem.authorization.dao.UserRepository;
import com.posthem.authorization.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new UsernameNotFoundException("User not found with email : " + email);
        }


        return UserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(String id) {
        User user = userRepository.findUserById(id);
        if (user == null){
            throw new ResourceNotFoundException("User", "id", id);
        }

        return UserPrincipal.create(user);
    }
}
