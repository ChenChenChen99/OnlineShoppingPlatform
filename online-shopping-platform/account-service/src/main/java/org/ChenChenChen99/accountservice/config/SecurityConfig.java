package org.ChenChenChen99.accountservice.config;


import org.ChenChenChen99.security.config.SecurityConfigBase;
import org.ChenChenChen99.security.filter.JwtAuthFilter;
import org.ChenChenChen99.security.util.JwtTokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends SecurityConfigBase {

    public SecurityConfig(JwtTokenUtil jwtTokenUtil) {
        super(jwtTokenUtil);
    }

    @Override
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/accounts/register", "/accounts/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthFilter(jwtTokenUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}