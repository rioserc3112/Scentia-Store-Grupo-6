package com.store.service;

import com.store.domain.Carrito;
import com.store.domain.CarritoItem;
import com.store.domain.EstadoPedido;
import com.store.domain.Pedido;
import com.store.domain.PedidoDetalle;
import com.store.domain.Producto;
import com.store.repository.PedidoRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;

    public PedidoService(PedidoRepository pedidoRepository, ProductoService productoService) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
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

    @Transactional
    public Pedido crearPedidoDesdeCarrito(Carrito carrito) {
        Pedido pedido = new Pedido();
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setSubtotal(carrito.getTotal());
        pedido.setTotal(carrito.getTotal());

        List<PedidoDetalle> detalles = new ArrayList<>();

        for (CarritoItem item : carrito.getItems()) {
            Producto producto = productoService.obtenerPorId(item.getProductoId());

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecio(item.getPrecioUnitario());

            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);

        return pedidoRepository.save(pedido);
    }
}