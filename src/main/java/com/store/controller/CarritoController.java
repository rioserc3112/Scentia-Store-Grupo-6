package com.store.controller;

import com.store.domain.Carrito;
import com.store.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Emmanuel Rios
 */

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    /**
     * HU-05 (vista carrito) — Muestra el contenido del carrito.
     * GET /carrito
     */
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Carrito carrito = carritoService.obtenerCarrito(session);
        model.addAttribute("carrito", carrito);
        model.addAttribute("cantidadCarrito", carrito.getCantidadTotal());
        return "carrito/listado";
    }

    /**
     * HU-05 — Agrega un producto al carrito.
     * POST /carrito/agregar
     */
    @PostMapping("/agregar")
    public String agregar(
            @RequestParam Long productoId,
            @RequestParam(defaultValue = "1") int cantidad,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String error = carritoService.agregar(session, productoId, cantidad);

        if (error != null) {
            redirectAttributes.addFlashAttribute("mensajeError", error);
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "Producto agregado al carrito.");
        }

        return "redirect:/catalogo";
    }

    /**
     * HU-06 — Modifica la cantidad de un producto en el carrito.
     * POST /carrito/actualizar
     */
    @PostMapping("/actualizar")
    public String actualizar(
            @RequestParam Long productoId,
            @RequestParam int cantidad,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String error = carritoService.actualizarCantidad(session, productoId, cantidad);

        if (error != null) {
            redirectAttributes.addFlashAttribute("mensajeError", error);
        } else {
            redirectAttributes.addFlashAttribute("mensajeExito", "Cantidad actualizada.");
        }

        return "redirect:/carrito";
    }

    /**
     * HU-07 — Elimina un producto del carrito.
     * POST /carrito/eliminar
     */
    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Long productoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        carritoService.eliminar(session, productoId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado del carrito.");
        return "redirect:/carrito";
    }
    
     /**
     * HU-11 — Confirmación visual del pedido.
     * POST /carrito/confirmar
     */
    @PostMapping("/confirmar")
    public String confirmarPedido(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Carrito carrito = carritoService.obtenerCarrito(session);

        if (carrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No puedes confirmar un pedido con el carrito vacío.");
            return "redirect:/carrito";
        }

        model.addAttribute("total", carrito.getTotal());
        carritoService.vaciar(session);

        return "pedido/confirmacion";
    }
}
