package com.store.repository;

import com.store.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Emmanuel Rios
 */

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    public List<Producto> findByActivoTrue();
    /**
     * Lista todos los productos activos para el catálogo público.
     */
    List<Producto> findByActivoTrueOrderByNombreAsc();

    /**
     * Lista productos activos filtrados por categoría.
     */
    List<Producto> findByActivoTrueAndCategoriaIgnoreCaseOrderByNombreAsc(String categoria);

    /**
     * Búsqueda por nombre o marca (catálogo con filtro).
     */
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           " LOWER(p.marca)  LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Producto> buscarPorNombreOMarca(@Param("termino") String termino);

    /**
     * Obtiene categorías distintas de productos activos (para filtro en catálogo).
     */
    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.activo = true AND p.categoria IS NOT NULL ORDER BY p.categoria")
    List<String> findCategoriasActivas();
    
      //1.Consulta deribada para recuperar los productos de un rango de precios, ordenados por precio ascendentemente
    public List<Producto> findByPrecioBetweenOrderByPrecioAsc(double precioInf, double precioSup);
    
    //2.Consulta JPQL para recuperar los productos de un rango de precios, ordenados por precio ascendentemente
    @Query(value = "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioInf and :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaJPQL(double precioInf, double precioSup);
    
    //3.Consulta SQL para recuperar los productos de un rango de precios, ordenados por precio ascendentemente
    @Query(nativeQuery = true,
            value = "SELECT * FROM producto p WHERE p.precio BETWEEN :precioInf and :precioSup ORDER BY p.precio ASC")
    public List<Producto> consultaSQL(double precioInf, double precioSup);
    
}
