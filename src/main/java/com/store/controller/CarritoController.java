package com.store.controller;

import com.store.domain.Carrito;
import com.store.domain.Pedido;
import com.store.service.CarritoService;
import com.store.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    public CarritoController(CarritoService carritoService, PedidoService pedidoService) {
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Carrito carrito = carritoService.obtenerCarrito(session);
        model.addAttribute("carrito", carrito);
        model.addAttribute("cantidadCarrito", carrito.getCantidadTotal());
        return "carrito/listado";
    }

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

    @PostMapping("/eliminar")
    public String eliminar(
            @RequestParam Long productoId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        carritoService.eliminar(session, productoId);
        redirectAttributes.addFlashAttribute("mensajeExito", "Producto eliminado del carrito.");
        return "redirect:/carrito";
    }

    @PostMapping("/confirmar")
    public String confirmarPedido(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Carrito carrito = carritoService.obtenerCarrito(session);

        if (carrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("mensajeError", "No puedes confirmar un pedido con el carrito vacío.");
            return "redirect:/carrito";
        }

        Pedido pedidoGuardado = pedidoService.crearPedidoDesdeCarrito(carrito);

        model.addAttribute("pedido", pedidoGuardado);
        model.addAttribute("total", pedidoGuardado.getTotal());

        carritoService.vaciar(session);

        return "pedido/confirmacion";
    }
}