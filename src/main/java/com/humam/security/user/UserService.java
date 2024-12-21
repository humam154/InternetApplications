package com.humam.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("Current password does not match password provided");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new IllegalStateException("New password does not match confirm password provided");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void updateProfile(UpdateProfileRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        user.setFirst_name(request.getFirst_name() != null ? request.getFirst_name() : user.getFirst_name());
        user.setLast_name(request.getLast_name() != null ? request.getLast_name() : user.getLast_name());
        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());

        userRepository.save(user);
    }
}
