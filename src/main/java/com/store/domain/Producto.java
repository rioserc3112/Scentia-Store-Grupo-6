package com.store.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 *
 * @author Emmanuel Rios
 */

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombre;

    @Size(max = 500)
    @Column(length = 500)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "imagen_url", length = 300)
    private String imagenUrl;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    @Size(max = 100)
    @Column(length = 100)
    private String categoria;

    /**
     * Presentación o volumen del perfume/decant, ej: "10ml", "50ml", "100ml", "Decant 5ml"
     */
    @Size(max = 50)
    @Column(length = 50)
    private String presentacion;

    @Size(max = 100)
    @Column(length = 100)
    private String marca;

    @Column(nullable = false)
    private Boolean activo = true;

    // ─── Constructors ───────────────────────────────────────────────────────────

    public Producto() {}

    // ─── Business methods ───────────────────────────────────────────────────────

    /**
     * Retorna el estado de disponibilidad para la vista.
     * AGOTADO   → stock == 0
     * POCO_STOCK → 1 <= stock <= 5
     * DISPONIBLE → stock > 5
     */
    public DisponibilidadStock getDisponibilidad() {
        if (this.stock == null || this.stock == 0) {
            return DisponibilidadStock.AGOTADO;
        } else if (this.stock <= 5) {
            return DisponibilidadStock.POCO_STOCK;
        } else {
            return DisponibilidadStock.DISPONIBLE;
        }
    }

    public boolean isDisponibleParaCompra() {
        return this.stock != null && this.stock > 0 && Boolean.TRUE.equals(this.activo);
    }

    // ─── Getters & Setters ──────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getPresentacion() { return presentacion; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}
