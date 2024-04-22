package org.example.resigter_project.auth;

import lombok.RequiredArgsConstructor;
import org.example.resigter_project.config.JwtService;
import org.example.resigter_project.repo.UserRepo;
import org.example.resigter_project.user.Role;
import org.example.resigter_project.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user= User.builder()
                .Username(request.getUsername())
                .Password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(Role.STUDENT)
                .build();
        userRepo.save(user);
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {


            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()

                    )
            );
            var user = userRepo.findByEmail(request.getEmail())
                    .orElseThrow();
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }catch (AuthenticationException e){
            throw  new RuntimeException("authentication failed "+e.getMessage());
        }

    }
}

