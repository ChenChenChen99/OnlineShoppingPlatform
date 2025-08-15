package org.ChenChenChen99.security.config;

import org.ChenChenChen99.security.filter.JwtAuthFilter;
import org.ChenChenChen99.security.util.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public abstract class SecurityConfigBase {

    protected final JwtTokenUtil jwtTokenUtil;

    public SecurityConfigBase(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(new JwtAuthFilter(jwtTokenUtil),
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
