package com.instalite.api.dtos.requests;

import lombok.Data;

@Data
public class AuthRequest {

    private String email;
    private String password;

}
