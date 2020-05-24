package com.posthem.authorization.controller;

import com.posthem.authorization.common.exception.BadRequestException;
import com.posthem.authorization.common.payload.ApiResponse;
import com.posthem.authorization.common.payload.AuthResponse;
import com.posthem.authorization.common.payload.LoginRequest;
import com.posthem.authorization.common.payload.SignUpRequest;
import com.posthem.authorization.dao.UserRepository;
import com.posthem.authorization.entity.AuthProvider;
import com.posthem.authorization.entity.User;
import com.posthem.authorization.security.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsUserByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        Map<String, Object> resData = new HashMap<>();

        // Creating user's account
        User user = new User();
        user.setName(signUpRequest.getName());

        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setProvider(AuthProvider.local);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signUpRequest.getEmail(),
                        signUpRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.createToken(authentication);

        resData.put("id", result.getId());
        resData.put("email", result.getEmail());
        resData.put("name", result.getName());
        resData.put("provider", "local");
        resData.put("emailVerified", false);
        resData.put("imageUrl", "");
        resData.put("accessToken", token);



        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@", resData));
    }

}
