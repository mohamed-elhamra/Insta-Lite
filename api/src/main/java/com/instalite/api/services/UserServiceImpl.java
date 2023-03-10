package com.instalite.api.services;

import com.instalite.api.commons.exceptions.InstaLiteException;
import com.instalite.api.commons.mappers.UserMapper;
import com.instalite.api.commons.utils.Constants;
import com.instalite.api.commons.utils.enums.ERole;
import com.instalite.api.commons.utils.IDGenerator;
import com.instalite.api.dtos.requests.AuthRequest;
import com.instalite.api.dtos.requests.UserCreationRequest;
import com.instalite.api.dtos.requests.UserModificationRequest;
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

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
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
    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        Optional<UserEntity> userByEmail = userRepository.findByEmail(userCreationRequest.getEmail());

        userByEmail.ifPresent(user -> {
            throw new InstaLiteException("User already exits with this email: " + userByEmail.get().getEmail());
        });

        UserEntity createdUser = userMapper.toUserEntity(userCreationRequest);
        createdUser.setPublicId(idGenerator.generateStringId());
        createdUser.setEncryptedPassword(bCryptPasswordEncoder.encode(userCreationRequest.getPassword()));
        createdUser.setRole(userCreationRequest.getRole());

        return userMapper.toUserResponse(userRepository.save(createdUser));
    }

    @Override
    public UserResponse updateUser(String publicId, UserModificationRequest userModificationRequest, Authentication authenticatedUser) throws AccessDeniedException {
        UserEntity user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this id: " + publicId));
        boolean isEmailsEquals = user.getEmail().equals(authenticatedUser.getName());
        boolean isAdmin = authenticatedUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals(ERole.ROLE_ADMIN.name()));

        if(isEmailsEquals || isAdmin){
            user.setFirstName(userModificationRequest.getFirstName());
            user.setLastName(userModificationRequest.getLastName());
            if (isAdmin) {
                if (userModificationRequest.getRole() == null) {
                    throw new InstaLiteException("Role could not be null.");
                }
                user.setRole(userModificationRequest.getRole());
            }
        }else{
            throw new AccessDeniedException("You are not allowed to update this user.");
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public UserResponse getUserByPublicId(String publicId) {
        UserEntity user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this id: " + publicId));
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userMapper.toUserResponseList(userRepository.findAll());
    }

    @Override
    public void deleteUser(String publicId) {
        UserEntity user = userRepository.findByPublicId(publicId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with this id: " + publicId));
        userRepository.delete(user);
    }

    @Override
    public void createFirstAdmin() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<UserEntity> users = userRepository.findAll();

        if (users.isEmpty()) {
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
                    .role(user.getRole())
                    .jwt(jwt)
                    .build();
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }
}
