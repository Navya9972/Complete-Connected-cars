package com.connected.car.user.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfigKeycloak {

    @Autowired
    public JwtAuthConverter jwtAuthConverter;
    @Autowired
    private JWTAuthenticationEntrypoint entryPoint;
    @Autowired
    private CustomAccessDeniedHandler customaccessdenied;

        @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl ;

        @Value("${keycloak.admin.realm}")
    private String realm;

        @Value("${keycloak.admin.client-id}")
    private String clientId;

        @Value("${keycloak.admin.client-secret}")
    private String clientSecret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public Keycloak keycloakBean(){
        System.out.println(keycloakServerUrl);
        Keycloak keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakServerUrl)
                .realm(realm)
                .username("darshanmcr888@gmail.com")
                .password("Darshan@123")
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
        return keycloak;
    }


  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
      http.csrf((csrf)->csrf.disable());
      http.authorizeHttpRequests(authorize ->{
          authorize
                  .requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                  .requestMatchers("/v1/api/users/create").permitAll()
                  .requestMatchers("/v1/api/users/login/**").permitAll()
                  .requestMatchers("v1/api/users/update/**").hasRole("admin")
                  .requestMatchers("v1/api/users/delete/**").hasRole("admin")
                  .requestMatchers("v1/api/users/getAllUsers").hasRole("admin")
                  .requestMatchers("v1/api/users/getUser/**").hasRole("user")
                  .requestMatchers("v1/api/users/getStatus/**").hasRole("user")
                  .requestMatchers("v1/api/users/InactiveStatus/**").hasRole("user")
                  .requestMatchers("v1/api/users/getAllActiveUsers").hasRole("admin")
                  .requestMatchers("v1/api/users/getAllInactiveUsers").hasRole("admin")
                  .anyRequest().authenticated();
      });

      http.oauth2ResourceServer(t ->{
          t.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter));
      });

      http.sessionManagement(
              t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      );
      http.exceptionHandling((exception)->exception.authenticationEntryPoint(entryPoint))
      .exceptionHandling((exception1)->exception1.accessDeniedHandler(customaccessdenied))
;
      return http.build();


  }
}
