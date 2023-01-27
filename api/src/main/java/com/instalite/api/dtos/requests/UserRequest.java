package com.instalite.api.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @NotBlank(message = "First name should not be empty")
    @Size(min = 5, max = 20, message = "First name should be between 5 and 20 character")
    private String firstName;

    @NotBlank(message = "Last name should not be empty")
    @Size(min = 5, max = 20, message = "Last name should be between 5 and 20 character")
    private String lastName;

    @Email(message = "Email format is incorrect")
    @NotNull(message = "Email should not be null")
    private String email;

    @NotNull(message = "Password name should not be null")
    @Size(min = 8, max = 20, message = "Size should be between 8 and 20 character")
    private String password;

}
