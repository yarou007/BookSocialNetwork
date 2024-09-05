package com.yarou.book.auth;

import com.yarou.book.email.EmailService;
import com.yarou.book.email.EmailTemplateName;
import com.yarou.book.role.roleRepository;
import com.yarou.book.security.JwtService;
import com.yarou.book.user.Token;
import com.yarou.book.user.TokenRepository;
import com.yarou.book.user.userRepository;

import com.yarou.book.user.User;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

     private final roleRepository RoleRepository;

     private final PasswordEncoder passwordEncoder;

     private final userRepository UserRepository;

    private final TokenRepository tokenRepository;

    private final EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public void register(RegistrationRequest request) throws MessagingException {
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

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        // send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation"
        );
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullname",user.fullName());
         var jwtToken = jwtService.generateToken(claims,user);
          return AuthenticationResponse.builder().token(jwtToken).build();
    }

  //  @Transactional
    public void activateAccount(String token) throws MessagingException {
      Token savedToken = tokenRepository.findByToken(token)
              //todo exception has to be defined
              .orElseThrow(()-> new RuntimeException("invalid token"));
      if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
          sendValidationEmail(savedToken.getUser());
          throw new RuntimeException("Activation token has expried, A new token has been sent to same email address");
      }
      var user = UserRepository.findById(savedToken.getUser().getId())
              .orElseThrow(()-> new UsernameNotFoundException("User not found"));
      user.setEnabled(true);
      UserRepository.save(user);
      savedToken.setValidatedAt(LocalDateTime.now());
      tokenRepository.save(savedToken);
    }
}
