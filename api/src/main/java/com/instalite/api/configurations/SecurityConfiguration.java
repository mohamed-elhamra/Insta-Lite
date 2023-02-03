package com.instalite.api.configurations;

import com.instalite.api.commons.utils.enums.ERole;
import com.instalite.api.filters.AuthorizationFilter;
import com.instalite.api.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private AuthorizationFilter authorizationFilter;

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users").hasAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.GET, "/users").hasAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.GET, "/users/**").hasAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.POST, "/users/authenticate").permitAll()
                //.antMatchers(HttpMethod.GET, "/images/download/**").hasAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.PATCH, "/images/**").hasAuthority(ERole.ROLE_ADMIN.name())
                .antMatchers(HttpMethod.GET, "/images").permitAll()
                .antMatchers(HttpMethod.GET, "/images/download/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService(userService));
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


}
