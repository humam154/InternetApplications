package com.humam.security.user;

import com.humam.security.config.JwtService;
import com.humam.security.token.Token;
import com.humam.security.token.TokenRepository;
import com.humam.security.token.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    public UpdateProfileResponse getProfile(Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        return UpdateProfileResponse.builder()
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .build();
    }
    
    public ChangePasswordResponse changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("Current password does not match password provided");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new IllegalStateException("New password does not match confirm password provided");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setLastPasswordResetDate(LocalDateTime.now());
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return ChangePasswordResponse.builder()
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }

    public UpdateProfileResponse updateProfile(UpdateProfileRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        user.setFirst_name(request.getFirst_name() != null ? request.getFirst_name() : user.getFirst_name());
        user.setLast_name(request.getLast_name() != null ? request.getLast_name() : user.getLast_name());
        user.setEmail(request.getEmail() != null ? request.getEmail() : user.getEmail());

        userRepository.save(user);

        return UpdateProfileResponse.builder()
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .build();
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validUserTokens.isEmpty()){
            return;
        }
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });

        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }


    public List<SearchUserResponse> searchUsers(String query, Integer groupId) {

        var users = userRepository.searchUsersNotInGroup(query, groupId);

        return users.stream()
                .map(user -> SearchUserResponse.builder()
                        .id(user.getId())
                        .first_name(user.getFirst_name())
                        .last_name(user.getLast_name())
                        .email(user.getEmail())
                        .build()
                )
                .collect(Collectors.toList());

    }
}
