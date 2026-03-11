package com.store.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * @author Emmanuel Rios
 * Carrito de compras manejado en sesión HTTP.
 * Contiene la lista de ítems y expone el total calculado.
 */
public class Carrito implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<CarritoItem> items = new ArrayList<>();

    public Optional<CarritoItem> buscarItem(Long productoId) {
        return items.stream()
                .filter(i -> i.getProductoId().equals(productoId))
                .findFirst();
    }

    /**
     * Agrega un producto al carrito.
     * Si ya existe, suma la cantidad. Si no, crea un nuevo ítem.
     *
     * @return false si la cantidad resultante supera el stock disponible.
     */
    public boolean agregar(Producto producto, int cantidadAgregar) {
        Optional<CarritoItem> existente = buscarItem(producto.getId());

        int cantidadActual = existente.map(CarritoItem::getCantidad).orElse(0);
        int nuevaCantidad = cantidadActual + cantidadAgregar;

        if (nuevaCantidad > producto.getStock()) {
            return false; // stock insuficiente
        }

        if (existente.isPresent()) {
            existente.get().setCantidad(nuevaCantidad);
        } else {
            items.add(new CarritoItem(producto, cantidadAgregar));
        }
        return true;
    }

    /**
     * Actualiza la cantidad de un ítem existente.
     * Si la cantidad es <= 0 elimina el ítem.
     *
     * @return false si la cantidad supera el stock disponible.
     */
    public boolean actualizarCantidad(Long productoId, int nuevaCantidad, int stockDisponible) {
        if (nuevaCantidad <= 0) {
            eliminar(productoId);
            return true;
        }
        if (nuevaCantidad > stockDisponible) {
            return false; // stock insuficiente
        }
        buscarItem(productoId).ifPresent(i -> i.setCantidad(nuevaCantidad));
        return true;
    }

    /**
     * Elimina un ítem del carrito por productoId.
     */
    public void eliminar(Long productoId) {
        items.removeIf(i -> i.getProductoId().equals(productoId));
    }

    /**
     * Calcula el total general del carrito.
     */
    public BigDecimal getTotal() {
        return items.stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int getCantidadTotal() {
        return items.stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    public List<CarritoItem> getItems() { return items; }
    public void setItems(List<CarritoItem> items) { this.items = items; }
}
