package com.store.controller;

import com.store.service.UsuarioService;
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
        return "redirect:/auth/login?exito";
    }
}