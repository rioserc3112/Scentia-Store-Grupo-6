package com.store.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Author: Emmanuel Rios
 * Ítem del carrito de compras.
 * Se maneja en sesión HTTP (no es una entidad JPA).
 * Implementa Serializable para que pueda almacenarse en sesión.
 */
public class CarritoItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long productoId;
    private String nombreProducto;
    private String marcaProducto;
    private String presentacionProducto;
    private BigDecimal precioUnitario;
    private Integer cantidad;
    private String imagenUrl;

    public CarritoItem() {}

    public CarritoItem(Producto producto, int cantidad) {
        this.productoId = producto.getId();
        this.nombreProducto = producto.getNombre();
        this.marcaProducto = producto.getMarca();
        this.presentacionProducto = producto.getPresentacion();
        this.precioUnitario = producto.getPrecio();
        this.cantidad = cantidad;
        this.imagenUrl = producto.getImagenUrl();
    }

    public BigDecimal getSubtotal() {
        if (precioUnitario == null || cantidad == null) return BigDecimal.ZERO;
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public String getMarcaProducto() { return marcaProducto; }
    public void setMarcaProducto(String marcaProducto) { this.marcaProducto = marcaProducto; }

    public String getPresentacionProducto() { return presentacionProducto; }
    public void setPresentacionProducto(String presentacionProducto) { this.presentacionProducto = presentacionProducto; }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }
}
