package com.store.controller;

import com.store.domain.Carrito;
import com.store.service.CarritoService;
import com.store.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Emmanuel Rios
 */

@Controller
@RequestMapping("/catalogo")
public class CatalogoController {

    private final ProductoService productoService;
    private final CarritoService carritoService;

    public CatalogoController(ProductoService productoService, CarritoService carritoService) {
        this.productoService = productoService;
        this.carritoService = carritoService;
    }

    @GetMapping
    public String listado(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String buscar,
            HttpSession session,
            Model model) {

        if (buscar != null && !buscar.isBlank()) {
            model.addAttribute("productos", productoService.buscar(buscar));
            model.addAttribute("buscar", buscar);
        } else if (categoria != null && !categoria.isBlank()) {
            model.addAttribute("productos", productoService.listarPorCategoria(categoria));
            model.addAttribute("categoriaActiva", categoria);
        } else {
            model.addAttribute("productos", productoService.listarCatalogo());
        }

        model.addAttribute("categorias", productoService.listarCategorias());

        Carrito carrito = carritoService.obtenerCarrito(session);
        model.addAttribute("cantidadCarrito", carrito.getCantidadTotal());

        return "catalogo/listado";
    }
}
