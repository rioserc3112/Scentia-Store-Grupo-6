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

    public List<Producto> listarCatalogo() {
        return productoRepository.findByActivoTrueOrderByNombreAsc();
    }

    public List<Producto> listarPorCategoria(String categoria) {
        return productoRepository.findByActivoTrueAndCategoriaIgnoreCaseOrderByNombreAsc(categoria);
    }

    public List<Producto> buscar(String termino) {
        if (termino == null || termino.isBlank()) {
            return listarCatalogo();
        }
        return productoRepository.buscarPorNombreOMarca(termino.trim());
    }

    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    public Optional<Producto> buscarPorId(Long id) {
        return productoRepository.findById(id);
    }

    public List<String> listarCategorias() {
        return productoRepository.findCategoriasActivas();
    }
}
