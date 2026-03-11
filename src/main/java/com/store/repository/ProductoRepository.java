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

    List<Producto> findByActivoTrueOrderByNombreAsc();


    List<Producto> findByActivoTrueAndCategoriaIgnoreCaseOrderByNombreAsc(String categoria);


    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
           " LOWER(p.marca)  LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<Producto> buscarPorNombreOMarca(@Param("termino") String termino);

    @Query("SELECT DISTINCT p.categoria FROM Producto p WHERE p.activo = true AND p.categoria IS NOT NULL ORDER BY p.categoria")
    List<String> findCategoriasActivas();
}
