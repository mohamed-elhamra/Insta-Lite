package com.instalite.api.dtos.responses;

import com.instalite.api.commons.utils.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String publicId;
    private String firstName;
    private String lastName;
    private String email;
    private ERole role;

}
