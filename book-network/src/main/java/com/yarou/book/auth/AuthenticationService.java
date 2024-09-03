package com.yarou.book.auth;

import com.yarou.book.role.roleRepository;
import com.yarou.book.user.Token;
import com.yarou.book.user.TokenRepository;
import com.yarou.book.user.userRepository;

import com.yarou.book.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

     private final roleRepository RoleRepository;

     private final PasswordEncoder passwordEncoder;

     private final userRepository UserRepository;

    private final TokenRepository tokenRepository;
    public void register(RegistrationRequest request) {
       var userRole = RoleRepository.findByName("USER")
               //to do a better exception handling
               .orElseThrow(()-> new IllegalStateException("ROLE USER was not initialized"));

       var user = User.builder()
               .firstname(request.getFirstname())
               .lastname(request.getLastname())
               .email(request.getEmail())
               .password(passwordEncoder.encode(request.getPassword()))
               .accountLocked(false)
               .enabled(false)
               .roles(List.of(userRole))
               .build();
        UserRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) {
        var newToken = generateAndSaveActivationToken(user);
        // send email
    }

    private String generateAndSaveActivationToken(User user) {
        // generate a token
        String generatedToken = generateActivateCode(6);
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    // generate code 6 digits
    private String generateActivateCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i =0 ; i<length;i++){
            int randomIndex  = secureRandom.nextInt(characters.length()); // 0..9
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
