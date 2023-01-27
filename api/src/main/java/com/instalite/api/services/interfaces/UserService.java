package com.instalite.api.services.interfaces;

import com.instalite.api.dtos.requests.AuthRequest;
import com.instalite.api.dtos.requests.UserRequest;
import com.instalite.api.dtos.responses.AuthResponse;
import com.instalite.api.dtos.responses.UserResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    UserResponse createUser(UserRequest userRequest);

    AuthResponse authenticateUser(AuthRequest authRequest, Authentication authentication);

}
