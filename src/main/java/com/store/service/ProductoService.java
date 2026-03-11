package com.store.service;

import com.store.domain.Producto;
import com.store.repository.ProductoRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Emmanuel Rios
 */

@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final FirebaseStorageService firebaseStorageService;

    public ProductoService(ProductoRepository productoRepository, FirebaseStorageService firebaseStorageService) {
        this.productoRepository = productoRepository;
        this.firebaseStorageService = firebaseStorageService;
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

    /**
     * Verifica que hay stock suficiente para la cantidad solicitada.
     */
    public boolean hayStockSuficiente(Long productoId, int cantidad) {
        return productoRepository.findById(productoId)
                .map(p -> p.isDisponibleParaCompra() && p.getStock() >= cantidad)
                .orElse(false);
    }
    

    // ─────────────────────────────────────────
    // Listar productos
    // ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<Producto> getProductos(boolean activo) {

        if (activo) {
            return productoRepository.findByActivoTrue();
        }

        return productoRepository.findAll();
    }

    // ─────────────────────────────────────────
    // Buscar producto por ID
    // ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public Optional<Producto> getProducto(Long id) {

        return productoRepository.findById(id);
    }

    // ─────────────────────────────────────────
    // Guardar producto
    // ─────────────────────────────────────────
    @Transactional
    public void save(Producto producto) {

        productoRepository.save(producto);
    }

    // ─────────────────────────────────────────
    // Eliminar producto
    // ─────────────────────────────────────────
    @Transactional
    public void delete(Long id) {

        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "El producto con ID " + id + " no existe."
            );
        }

        try {
            productoRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {

            throw new IllegalStateException(
                    "No se puede eliminar el producto. Tiene datos asociados.", e
            );
        }
    }

  
    // ─────────────────────────────────────────
    // Buscar por rango de precio (consulta derivada)
    // ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<Producto> consultaDerivada(double precioInf, double precioSup) {

        return productoRepository
                .findByPrecioBetweenOrderByPrecioAsc(precioInf, precioSup);
    }


}
