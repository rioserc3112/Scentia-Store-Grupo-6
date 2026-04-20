package com.store.controller;

import com.store.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nuevo")
    public String mostrarRegistro() {
        return "registro/nuevo";
    }

    @PostMapping("/nuevo")
    public String registrar(@RequestParam String nombre,
                            @RequestParam String correo,
                            @RequestParam String password,
                            Model model) {

        String error = usuarioService.registrar(nombre, correo, password);

        if (error != null) {
            model.addAttribute("mensajeError", error);
            return "registro/nuevo";
        }

        model.addAttribute("mensajeExito",
            "Registro exitoso. Por favor revise su correo " + correo + " para activar su cuenta.");
        return "auth/login";
    }

    @GetMapping("/activacion/{correo}/{token}")
    public String activar(@PathVariable String correo,
                          @PathVariable String token,
                          RedirectAttributes redirectAttributes) {

        boolean ok = usuarioService.activarCuenta(correo, token);

        if (ok) {
            redirectAttributes.addFlashAttribute("mensajeExito",
                "Cuenta activada correctamente. Ya puede iniciar sesión.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError",
                "El enlace de activación no es válido o ya fue usado.");
        }
        return "redirect:/login";
    }

    @GetMapping("/recordar")
    public String mostrarRecordar() {
        return "registro/recordar";
    }

    @PostMapping("/recordar")
    public String recordar(@RequestParam String correo, Model model) {

        boolean enviado = usuarioService.enviarRecordatorio(correo);

        if (enviado) {
            model.addAttribute("mensajeExito",
                "Por favor revise su correo " + correo + " para recuperar su contraseña.");
        } else {
            model.addAttribute("mensajeError",
                "No se encontró una cuenta con ese correo.");
        }
        return "registro/recordar";
    }

    @GetMapping("/reset/{correo}/{token}")
    public String mostrarReset(@PathVariable String correo,
                               @PathVariable String token,
                               Model model) {
        model.addAttribute("correo", correo);
        model.addAttribute("token", token);
        return "registro/reset";
    }

    @PostMapping("/reset")
    public String resetear(@RequestParam String correo,
                           @RequestParam String token,
                           @RequestParam String password,
                           RedirectAttributes redirectAttributes) {

        boolean ok = usuarioService.resetearPassword(correo, token, password);

        if (ok) {
            redirectAttributes.addFlashAttribute("mensajeExito",
                "Contraseña actualizada correctamente. Ya puede iniciar sesión.");
        } else {
            redirectAttributes.addFlashAttribute("mensajeError",
                "El enlace de recuperación no es válido o ya fue usado.");
        }
        return "redirect:/login";
    }
}
