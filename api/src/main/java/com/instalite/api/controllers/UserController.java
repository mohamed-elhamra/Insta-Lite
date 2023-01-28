package com.instalite.api.controllers;

import com.instalite.api.dtos.requests.AuthRequest;
import com.instalite.api.dtos.requests.UserRequest;
import com.instalite.api.dtos.responses.AuthResponse;
import com.instalite.api.dtos.responses.UserResponse;
import com.instalite.api.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        return ResponseEntity.ok(userService.authenticateUser(authRequest, authentication));
    }

    @PatchMapping("/{publicId}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable String publicId,
                                                   @RequestBody @Valid UserRequest userRequest,
                                                   Authentication authentication) throws AccessDeniedException {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.updateUser(publicId, userRequest, authentication));
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<UserResponse> getUserByPublicId(@PathVariable String publicId){
        return ResponseEntity.ok(userService.getUserByPublicId(publicId));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteUser(@PathVariable String publicId){
        userService.deleteUser(publicId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
