package com.instalite.api.configurations;

import com.instalite.api.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@AllArgsConstructor
public class GlobalConfiguration implements CommandLineRunner {

    private UserService userService;

    @Override
    public void run(String... args) {
        userService.createAdmin();
    }

}
