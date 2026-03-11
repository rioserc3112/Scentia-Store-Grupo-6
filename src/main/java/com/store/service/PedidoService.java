package com.store.service;

import com.store.domain.Carrito;
import com.store.domain.CarritoItem;
import com.store.domain.EstadoPedido;
import com.store.domain.Pedido;
import com.store.domain.PedidoDetalle;
import com.store.domain.Producto;
import com.store.repository.PedidoRepository;
import com.store.repository.ProductoRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
            ProductoService productoService,
            ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<Pedido> getPedidos() {
        return pedidoRepository.findAll();
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

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException(
                        "Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());

            if (producto.getStock() < 0) {
                producto.setStock(0);
            }

            productoRepository.save(producto);

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());
            detalle.setSubtotal(item.getSubtotal());

            detalles.add(detalle);
        }

        pedido.setDetalles(detalles);

        return pedidoRepository.save(pedido);
    }
}
