package com.store;

import com.store.domain.Ruta;
import com.store.service.RutaService;
import com.store.service.UsuarioDetailsService;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

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

    // Requerido para que maximumSessions funcione correctamente
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RutaService rutaService) throws Exception {

        final List<Ruta> rutas;
        List<Ruta> rutasTemp;
        try {
            rutasTemp = rutaService.getRutas();
        } catch (Exception e) {
            rutasTemp = List.of();
        }
        rutas = rutasTemp;

        http.authorizeHttpRequests(auth -> {
            // Recursos estáticos y rutas de autenticación siempre públicos
            auth.requestMatchers(
                "/login", "/registro/**", "/acceso_denegado",
                "/css/**", "/js/**", "/images/**", "/webjars/**"
            ).permitAll();

            if (rutas.isEmpty()) {
                // Fallback en primer arranque (antes de que InicializadorDatos siembre la BD)
                auth.requestMatchers("/producto/**").hasRole("ADMIN");
            } else {
                // Rutas dinámicas cargadas desde la base de datos
                for (Ruta ruta : rutas) {
                    if (ruta.getRolName() != null && !ruta.getRolName().isBlank()) {
                        auth.requestMatchers(ruta.getPatron()).hasRole(ruta.getRolName());
                    } else {
                        auth.requestMatchers(ruta.getPatron()).permitAll();
                    }
                }
            }

            auth.anyRequest().authenticated();
        });

        http.formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/catalogo", true)
            .failureUrl("/login?error=true")
            .permitAll()
        );

        http.logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout=true")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        );

        // Sesión única por usuario: la nueva sesión expulsa a la anterior
        http.sessionManagement(session -> session
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
        );

        // Redirige a /acceso_denegado cuando un usuario no tiene permisos
        http.exceptionHandling(ex -> ex
            .accessDeniedPage("/acceso_denegado")
        );

        return http.build();
    }
}
