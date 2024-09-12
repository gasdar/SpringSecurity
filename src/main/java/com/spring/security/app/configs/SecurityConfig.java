package com.spring.security.app.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.security.app.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .csrf(crsf -> crsf.disable())
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .httpBasic(Customizer.withDefaults()) // Para realizar una validación básica con usuario y contraseña
//                .authorizeHttpRequests( httpConfig -> {
//                    // Configurar endpoints públicos
//                    httpConfig.requestMatchers(HttpMethod.GET, "/auth/hello").permitAll();
//
//                    // Configurar endpoints privados
//                    httpConfig.requestMatchers(HttpMethod.GET, "/auth/hello-secured").hasAuthority("CREATE");
//
//                    // Configurar otros endpoints - NO ESPECÍFICADOS
//                    httpConfig.anyRequest().denyAll();
//                })
//                .build();
//    }

    // CONFIGURACIÓN CON ANOTACIÓN @EnabledMethodSecurity y @PreAuthorize en controllers
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(crsf -> crsf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults()) // Para realizar una validación básica con usuario y contraseña
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    // Agregar Usuarios Manualmente para Realizar Pruebas
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     List<UserDetails> userDetailsList = new ArrayList<>();

    //     userDetailsList.add(User
    //             .withUsername("pepe")
    //             .password(passwordEncoder().encode("1234"))
    //             .roles("ADMIN")
    //             .authorities("READ", "CREATE")
    //             .build());

    //     userDetailsList.add(User
    //             .withUsername("maria")
    //             .password(passwordEncoder().encode("1234"))
    //             .roles("ADMIN")
    //             .authorities("READ", "CREATE")
    //             .build());

    //     userDetailsList.add(User
    //             .withUsername("rolan")
    //             .password(passwordEncoder().encode("1234"))
    //             .roles("USER")
    //             .authorities("READ")
    //             .build());

    //     return new InMemoryUserDetailsManager(userDetailsList);
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}