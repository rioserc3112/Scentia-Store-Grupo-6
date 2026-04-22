package com.store;

import com.store.domain.Ruta;
import com.store.service.RutaService;
import com.store.service.UsuarioDetailsService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            UsuarioDetailsService usuarioDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, @Lazy RutaService rutaService) throws Exception {

        http.authorizeHttpRequests(auth -> {
            // 1. RUTAS SIEMPRE PÚBLICAS
            auth.requestMatchers(
                "/auth/**", "/acceso_denegado", "/login",
                "/css/**", "/js/**", "/images/**", "/webjars/**"
            ).permitAll();

            // 2. RUTAS DINÁMICAS DESDE BD
            try {
                List<Ruta> rutas = rutaService.getRutas();
                if (rutas != null) {
                    for (Ruta r : rutas) {
                        if (r.getPatron() != null && !r.getPatron().isBlank()) {
                            if (r.getRolName() != null && !r.getRolName().isBlank()) {
                                auth.requestMatchers(r.getPatron()).hasRole(r.getRolName());
                            } else {
                                auth.requestMatchers(r.getPatron()).permitAll();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar rutas: " + e.getMessage());
            }

            // 3. TODO LO DEMÁS PROTEGIDO
            auth.anyRequest().authenticated();
        });

        http.formLogin(form -> form
            .loginPage("/auth/login")
            .loginProcessingUrl("/login") // Spring Security escucha este POST
            .defaultSuccessUrl("/catalogo", true)
            .permitAll()
        );

        http.logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/auth/login?logout=true")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

        // Deshabilitar CSRF para que el logout POST funcione sin problemas
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }
}