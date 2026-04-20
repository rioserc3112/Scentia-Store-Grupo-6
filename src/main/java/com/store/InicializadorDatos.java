package com.store;

import com.store.domain.Ruta;
import com.store.repository.RutaRepository;
import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class InicializadorDatos {

    private final RutaRepository rutaRepository;

    public InicializadorDatos(RutaRepository rutaRepository) {
        this.rutaRepository = rutaRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void inicializar() {
        try {
            if (rutaRepository.count() == 0) {
                rutaRepository.saveAll(List.of(
                    new Ruta("/producto/**", "ADMIN"),
                    new Ruta("/catalogo/**", null),
                    new Ruta("/carrito/**", null),
                    new Ruta("/pedido/**", null)
                ));
            }
        } catch (Exception e) {
            // La tabla ruta aún no existe en la BD; debe crearse manualmente
        }
    }
}
