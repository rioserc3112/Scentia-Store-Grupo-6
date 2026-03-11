package com.store.service;

import com.store.domain.Usuario;
import com.store.repository.UsuarioRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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

        // Crear usuario con rol por defecto
        Usuario usuario = new Usuario(nombre, correo, password, "USER");

        usuarioRepository.save(usuario);
        return null;
    }

    public Usuario autenticar(String correo, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }
}