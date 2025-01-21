package com.humam.security.auth;

import com.humam.security.config.JwtService;
import com.humam.security.email.EmailService;
import com.humam.security.token.Token;
import com.humam.security.token.TokenRepository;
import com.humam.security.token.TokenType;
import com.humam.security.user.ChangePasswordResponse;
import com.humam.security.user.Role;
import com.humam.security.user.User;
import com.humam.security.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.humam.security.email.EmailTemplateName.*;
import static com.humam.security.user.Role.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    public AuthenticationResponse register(RegisterRequest request) throws MessagingException {
        Optional<User> userOptional = repository.findByEmail(request.getEmail());

        if(userOptional.isPresent()){
            throw new UsernameNotFoundException("Email already in use");
        }
        var user = User.builder()
                .first_name(request.getFirst_name())
                .last_name(request.getLast_name())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(USER)
                .lastPasswordResetDate(LocalDateTime.now())
                .build();

        var savedUser = repository.save(user);

        var jwtToken = jwtService.generateToken(user);

        saveUserToken(savedUser, jwtToken);
        if(!user.getEmail().equals("admin@admin.com")) {
            sendValidationEmail(user);
        }
        return AuthenticationResponse.builder()
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .token(jwtToken)
                .build();

    }

    private void sendValidationEmail(User user) throws MessagingException {
        var activationCode = generateAndSaveActivationCode(user);
        activationUrl = activationUrl + "?code=" + activationCode;
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                ACTIVATE_ACCOUNT,
                activationUrl,
                activationCode,
                "Account Activation"

        );
    }

    private String generateAndSaveActivationCode(User user) {
        // generate a code
        String generatedCode = generateActivationCode(6);
        user.setActivationCode(generatedCode);
        repository.save(user);

        return generatedCode;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }


    public AuthenticationResponse auth(AuthenticateRequest request) {
            authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
            );

            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);

            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);

            boolean role = user.getRole().equals(Role.ADMIN);

        return AuthenticationResponse.builder()
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .token(jwtToken)
                .isAdmin(role)
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


    public AuthenticationResponse activateAccount(String code) {
        var user = repository.findByCode(code).orElseThrow(() -> new UsernameNotFoundException("Invalid activation code"));
        user.setEnabled(true);
        user.setActivationCode(null);
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail())
                .token(jwtToken)
                .build();
    }

    public RequestResetPasswordResponse requestPasswordReset(String email) throws MessagingException {
        var user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid email"));
        sendValidationEmail(user);

        user.setEnabled(false);
        repository.save(user);
        return RequestResetPasswordResponse.builder()
                .email(user.getEmail())
                .text("code sent to your email")
                .build();
    }

    public ChangePasswordResponse changePassword(ForgotPasswordRequest request) {
        var user = repository.findByCode(request.getCode()).orElseThrow(() -> new UsernameNotFoundException("Invalid code"));
        if(user.getEmail().equals(request.getEmail())) {
            if(request.getPassword().equals(request.getPasswordConfirm())) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
                user.setEnabled(true);
                user.setLastPasswordResetDate(LocalDateTime.now());
                user.setActivationCode(null);
                repository.save(user);

                return ChangePasswordResponse.builder()
                        .first_name(user.getFirst_name())
                        .last_name(user.getLast_name())
                        .email(user.getEmail())
                        .token(jwtService.generateToken(user))
                        .build();
            } else {
                throw new IllegalStateException("Password does not match");
            }
        } else {
            throw new UsernameNotFoundException("Invalid email");
        }
    }
}

