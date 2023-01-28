package com.instalite.api.services;

import com.instalite.api.commons.exceptions.InstaLiteException;
import com.instalite.api.commons.mappers.UserMapper;
import com.instalite.api.commons.utils.Constants;
import com.instalite.api.commons.utils.ERole;
import com.instalite.api.commons.utils.IDGenerator;
import com.instalite.api.dtos.requests.AuthRequest;
import com.instalite.api.dtos.requests.UserRequest;
import com.instalite.api.dtos.responses.AuthResponse;
import com.instalite.api.dtos.responses.UserResponse;
import com.instalite.api.entities.UserEntity;
import com.instalite.api.repositories.UserRepository;
import com.instalite.api.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private IDGenerator idGenerator;
    private JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + email));

        Collection<GrantedAuthority> authorities =
                Stream.of(user.getRole().name())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new User(user.getEmail(), user.getEncryptedPassword(), authorities);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<UserEntity> userByEmail = userRepository.findByEmail(userRequest.getEmail());

        userByEmail.ifPresent(user -> {
            throw new InstaLiteException("User already exits with this email");
        });

        UserEntity createdUser = userMapper.toUserEntity(userRequest);
        createdUser.setPublicId(idGenerator.generateStringId());
        createdUser.setEncryptedPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        createdUser.setRole(ERole.ROLE_USER);

        return userMapper.toUserResponse(userRepository.save(createdUser));
    }

    @Override
    public void createAdmin() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<UserEntity> user = userRepository.findByRole(ERole.ROLE_ADMIN);

        if (user.isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setPublicId(idGenerator.generateStringId());
            admin.setFirstName(Constants.ADMIN_FIRST_NAME);
            admin.setLastName(Constants.ADMIN_LAST_NAME);
            admin.setEmail(Constants.ADMIN_EMAIL);
            admin.setEncryptedPassword(bCryptPasswordEncoder.encode(Constants.ADMIN_PASSWORD));
            admin.setRole(ERole.ROLE_ADMIN);

            userRepository.save(admin);
        }
    }

    @Override
    public AuthResponse authenticateUser(AuthRequest authRequest, Authentication authentication) {
        UserEntity user;

        if (authentication.isAuthenticated()) {
            user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with this email: " + authRequest.getEmail()));
            String jwt = jwtService.generateToken(user);
            return AuthResponse.builder()
                    .publicId(user.getPublicId())
                    .email(user.getEmail())
                    .jwt(jwt)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
