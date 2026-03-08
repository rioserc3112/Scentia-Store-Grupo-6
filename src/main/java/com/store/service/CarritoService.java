package com.store.service;

import com.store.domain.Carrito;
import com.store.domain.Producto;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

/**
 * @author Emmanuel Rios
 * Servicio de carrito de compras.
 * El carrito se almacena en la sesión HTTP bajo la clave SESSION_KEY.
 * No persiste en base de datos; es adecuado para un proyecto académico.
 */
@Service
public class CarritoService {

    public static final String SESSION_KEY = "carrito";

    private final ProductoService productoService;

    public CarritoService(ProductoService productoService) {
        this.productoService = productoService;
    }

    // ─── Session helpers ────────────────────────────────────────────────────────

    /**
     * Obtiene el carrito de la sesión. Si no existe, crea uno nuevo y lo guarda.
     */
    public Carrito obtenerCarrito(HttpSession session) {
        Carrito carrito = (Carrito) session.getAttribute(SESSION_KEY);
        if (carrito == null) {
            carrito = new Carrito();
            session.setAttribute(SESSION_KEY, carrito);
        }
        return carrito;
    }

    // ─── Cart operations ────────────────────────────────────────────────────────

    /**
     * Agrega un producto al carrito.
     *
     * @return mensaje de éxito o error
     */
    public String agregar(HttpSession session, Long productoId, int cantidad) {
        if (cantidad < 1) {
            return "La cantidad debe ser al menos 1.";
        }

        Producto producto = productoService.buscarPorId(productoId).orElse(null);
        if (producto == null || !producto.isDisponibleParaCompra()) {
            return "El producto no está disponible.";
        }

        Carrito carrito = obtenerCarrito(session);
        boolean ok = carrito.agregar(producto, cantidad);

        if (!ok) {
            int stockDisponible = producto.getStock();
            int enCarrito = carrito.buscarItem(productoId)
                    .map(i -> i.getCantidad()).orElse(0);
            int puedePedir = stockDisponible - enCarrito;
            if (puedePedir <= 0) {
                return "Ya tienes el máximo disponible de \"" + producto.getNombre() + "\" en tu carrito.";
            }
            return "Stock insuficiente. Solo puedes agregar " + puedePedir + " unidad(es) más de \"" + producto.getNombre() + "\".";
        }

        return null; // null = éxito
    }

    /**
     * Actualiza la cantidad de un ítem del carrito.
     *
     * @return mensaje de error, o null si fue exitoso
     */
    public String actualizarCantidad(HttpSession session, Long productoId, int nuevaCantidad) {
        Carrito carrito = obtenerCarrito(session);

        if (carrito.buscarItem(productoId).isEmpty()) {
            return "El producto no está en el carrito.";
        }

        Producto producto = productoService.buscarPorId(productoId).orElse(null);
        int stockActual = (producto != null) ? producto.getStock() : 0;

        boolean ok = carrito.actualizarCantidad(productoId, nuevaCantidad, stockActual);
        if (!ok) {
            return "Stock insuficiente. Solo hay " + stockActual + " unidad(es) disponibles.";
        }

        return null; // null = éxito
    }

    /**
     * Elimina un ítem del carrito.
     */
    public void eliminar(HttpSession session, Long productoId) {
        obtenerCarrito(session).eliminar(productoId);
    }

    /**
     * Vacía completamente el carrito.
     */
    public void vaciar(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }
}
