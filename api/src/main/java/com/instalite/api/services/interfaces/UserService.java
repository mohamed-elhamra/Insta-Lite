package com.instalite.api.services.interfaces;

import com.instalite.api.dtos.requests.AuthRequest;
import com.instalite.api.dtos.requests.UserCreationRequest;
import com.instalite.api.dtos.requests.UserModificationRequest;
import com.instalite.api.dtos.responses.AuthResponse;
import com.instalite.api.dtos.responses.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserResponse createUser(UserCreationRequest userCreationRequest);

    UserResponse updateUser(String publicId, UserModificationRequest userModificationRequest, Authentication authenticatedUser) throws AccessDeniedException;

    UserResponse getUserByPublicId(String publicId);

    List<UserResponse> getAllUsers();

    void deleteUser(String publicId);

    void createFirstAdmin();

    AuthResponse authenticateUser(AuthRequest authRequest, Authentication authentication);

}
