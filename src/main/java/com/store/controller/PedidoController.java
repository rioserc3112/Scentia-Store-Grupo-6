package com.store.controller;

import com.store.domain.EstadoPedido;
import com.store.domain.Usuario;
import com.store.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    private boolean esAdmin(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        return usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol());
    }

    @GetMapping("/listado")
    public String listado(Model model, HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

        var pedidos = pedidoService.getPedidos();
        model.addAttribute("pedidos", pedidos);

        return "/pedido/listado";
    }

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam Integer idPedido, HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

        pedidoService.cambiarEstado(idPedido, EstadoPedido.PAGADO);

        return "redirect:/pedido/listado";
    }

    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idPedido, HttpSession session) {

        if (!esAdmin(session)) {
            return "redirect:/catalogo";
        }

        pedidoService.cambiarEstado(idPedido, EstadoPedido.CANCELADO);

        return "redirect:/pedido/listado";
    }
}