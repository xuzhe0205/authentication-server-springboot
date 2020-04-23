package com.posthem.authorization.controller;


import com.posthem.authorization.common.exception.ResourceNotFoundException;
import com.posthem.authorization.dao.UserRepository;
import com.posthem.authorization.entity.User;
import com.posthem.authorization.security.CurrentUser;
import com.posthem.authorization.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User currUser = userRepository.findUserById(userPrincipal.getId());
        if (currUser == null){
            throw new ResourceNotFoundException("User", "id", userPrincipal.getId());
        }
        return currUser;
    }
}
