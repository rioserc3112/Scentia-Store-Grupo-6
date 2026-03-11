package com.store.controller;

import com.store.domain.EstadoPedido;
import com.store.service.PedidoService;
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

    @GetMapping("/listado")
    public String listado(Model model) {

        var pedidos = pedidoService.getPedidos();

        model.addAttribute("pedidos", pedidos);

        return "/pedido/listado";
    }

    @PostMapping("/confirmar")
    public String confirmar(@RequestParam Integer idPedido) {

        pedidoService.cambiarEstado(idPedido, EstadoPedido.PAGADO);

        return "redirect:/pedido/listado";
    }

    @PostMapping("/cancelar")
    public String cancelar(@RequestParam Integer idPedido) {

        pedidoService.cambiarEstado(idPedido, EstadoPedido.CANCELADO);

        return "redirect:/pedido/listado";
    }
}