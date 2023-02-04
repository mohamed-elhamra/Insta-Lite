package com.instalite.api.dtos.requests;

import com.instalite.api.commons.utils.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModificationRequest {

    @NotBlank(message = "First name should not be empty")
    @Size(min = 5, max = 20, message = "First name should be between 5 and 20 character")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Size(min = 5, max = 20, message = "Last name should be between 5 and 20 character")
    private String lastName;

    private ERole role;

}
