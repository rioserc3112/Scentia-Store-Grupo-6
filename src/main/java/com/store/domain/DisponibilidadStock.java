package com.store.domain;

/**
 * * Author: Emmanuel Rios
 * Representa el estado de disponibilidad de un producto en el catálogo.
 */
public enum DisponibilidadStock {

    DISPONIBLE("Disponible", "success"),
    POCO_STOCK("Poco stock", "warning"),
    AGOTADO("Agotado", "danger");

    private final String etiqueta;
    private final String cssColor; // Bootstrap color class suffix

    DisponibilidadStock(String etiqueta, String cssColor) {
        this.etiqueta = etiqueta;
        this.cssColor = cssColor;
    }

    public String getEtiqueta() { return etiqueta; }
    public String getCssColor() { return cssColor; }
}
