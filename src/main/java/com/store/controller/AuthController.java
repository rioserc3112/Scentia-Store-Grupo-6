package com.store.controller;

import com.store.domain.Usuario;
import com.store.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String correo,
                                @RequestParam String password,
                                HttpSession session,
                                Model model) {

        Usuario usuario = usuarioService.autenticar(correo, password);

        if (usuario == null) {
            model.addAttribute("mensajeError", "Correo o contraseña incorrectos.");
            return "auth/login";
        }

        session.setAttribute("usuarioLogueado", usuario);
        return "redirect:/catalogo";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrar(@RequestParam String nombre,
                            @RequestParam String correo,
                            @RequestParam String password,
                            Model model) {

        String error = usuarioService.registrar(nombre, correo, password);

        if (error != null) {
            model.addAttribute("mensajeError", error);
            return "auth/registro";
        }

        model.addAttribute("mensajeExito", "Usuario registrado correctamente.");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }
}