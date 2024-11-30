package com.humam.security.config;

import com.humam.security.token.TokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        jwt = authHeader.substring(7);

        var token = tokenRepository.findByToken(jwt);
        if(!isValidToken(jwt) || token.get().isExpired() || token.get().isRevoked()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);

        if(storedToken != null){
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }

    private boolean isValidToken(String token){
        try{
            var validToken = tokenRepository.findByToken(token);
            if(validToken.isPresent()){
                return true;
            }
            return false;
        }
        catch(JwtException e){
            return false;
        }
    }
}
