package com.store.service;

import com.store.domain.Usuario;
import com.store.repository.UsuarioRepository;
import java.util.Optional;
// PENDIENTE: import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    // PENDIENTE: private final CorreoService correoService;

    public UsuarioService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder/* PENDIENTE: , CorreoService correoService */) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        // PENDIENTE: this.correoService = correoService;
    }

    @Transactional
    public String registrar(String nombre, String correo, String password) {
        if (nombre == null || nombre.isBlank()) {
            return "El nombre es obligatorio.";
        }
        if (correo == null || correo.isBlank()) {
            return "El correo es obligatorio.";
        }
        if (password == null || password.isBlank()) {
            return "La contraseña es obligatoria.";
        }
        if (usuarioRepository.existsByCorreo(correo)) {
            return "Ya existe una cuenta con ese correo.";
        }

        Usuario usuario = new Usuario(nombre, correo, passwordEncoder.encode(password), "USER");
        usuarioRepository.save(usuario);

        /* PENDIENTE: descomentar cuando existan columnas activo/token en BD
        String token = UUID.randomUUID().toString();
        usuario.setActivo(false);
        usuario.setToken(token);
        try {
            correoService.enviarActivacion(correo, nombre, token);
        } catch (Exception e) {
            // Si el correo falla, el usuario queda registrado pero inactivo
        }
        */

        return null;
    }

    /* PENDIENTE: descomentar cuando existan columnas activo/token en BD
    @Transactional
    public boolean activarCuenta(String correo, String token) {
        Optional<Usuario> opt = usuarioRepository.findByCorreo(correo);
        if (opt.isEmpty()) return false;

        Usuario usuario = opt.get();
        if (token == null || !token.equals(usuario.getToken())) return false;

        usuario.setActivo(true);
        usuario.setToken(null);
        usuarioRepository.save(usuario);
        return true;
    }

    @Transactional
    public boolean enviarRecordatorio(String correo) {
        Optional<Usuario> opt = usuarioRepository.findByCorreo(correo);
        if (opt.isEmpty()) return false;

        String token = UUID.randomUUID().toString();
        Usuario usuario = opt.get();
        usuario.setToken(token);
        usuarioRepository.save(usuario);

        try {
            correoService.enviarRecordatorio(correo, usuario.getNombre(), token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Transactional
    public boolean resetearPassword(String correo, String token, String nuevaPassword) {
        Optional<Usuario> opt = usuarioRepository.findByCorreo(correo);
        if (opt.isEmpty()) return false;

        Usuario usuario = opt.get();
        if (token == null || !token.equals(usuario.getToken())) return false;

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setToken(null);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        return true;
    }
    */

    @Transactional
    public boolean activarCuenta(String correo, String token) { return false; }

    @Transactional
    public boolean enviarRecordatorio(String correo) { return false; }

    @Transactional
    public boolean resetearPassword(String correo, String token, String nuevaPassword) { return false; }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
