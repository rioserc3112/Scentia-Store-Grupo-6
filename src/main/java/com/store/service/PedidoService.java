package com.store.service;

import com.store.domain.EstadoPedido;
import com.store.domain.Pedido;
import com.store.repository.PedidoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> getPedido(Integer idPedido) {
        return pedidoRepository.findById(idPedido);
    }

    @Transactional
    public Pedido save(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public void cambiarEstado(Integer idPedido, EstadoPedido estado) {

        var pedido = pedidoRepository.findById(idPedido);

        if (pedido.isPresent()) {

            Pedido p = pedido.get();
            p.setEstado(estado);

            pedidoRepository.save(p);
        }
    }
}