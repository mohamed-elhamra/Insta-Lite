package com.instalite.api.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String publicId;
    private String email;
    private String jwt;

}
