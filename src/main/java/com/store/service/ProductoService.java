package com.store.service;

import com.store.domain.Producto;
import com.store.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Emmanuel Rios
 */

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Retorna todos los productos activos para el catálogo.
     */
    public List<Producto> listarCatalogo() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    /**
     * Retorna productos activos filtrados por categoría.
     */
    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByActivoTrueAndCategoriaIgnoreCaseOrderByNombreAsc(categoria);
    }

    /**
     * Búsqueda de productos activos por nombre o marca.
     */
    public List<Producto> buscar(String termino) {
        if (termino == null || termino.isBlank()) {
            return listarCatalogo();
        }
        return productoRepository.buscarPorNombreOMarca(termino.trim());
    }

    /**
     * Retorna un producto por ID. Lanza excepción si no existe.
     */
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    /**
     * Retorna un producto si existe (para validaciones en carrito).
     */
    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Retorna la lista de categorías activas para el filtro del catálogo.
     */
    public List<String> listarCategorias() {
        return productoRepository.findCategoriasActivas();
    }

    /**
     * Verifica que hay stock suficiente para la cantidad solicitada.
     */
    public boolean hayStockSuficiente(Long productoId, int cantidad) {
        return productoRepository.findById(productoId)
                .map(p -> p.isDisponibleParaCompra() && p.getStock() >= cantidad)
                .orElse(false);
    }
}
