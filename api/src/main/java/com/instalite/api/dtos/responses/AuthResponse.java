package com.instalite.api.dtos.responses;

import com.instalite.api.commons.utils.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String publicId;
    private String email;
    private ERole role;
    private String jwt;

}
