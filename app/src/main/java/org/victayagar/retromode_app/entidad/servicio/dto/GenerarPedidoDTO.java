package org.victayagar.retromode_app.entidad.servicio.dto;

import org.victayagar.retromode_app.entidad.servicio.Cliente;
import org.victayagar.retromode_app.entidad.servicio.DetallePedido;
import org.victayagar.retromode_app.entidad.servicio.Pedido;

import java.util.ArrayList;
import java.util.List;

public class GenerarPedidoDTO {
    private Pedido pedido;
    private ArrayList<DetallePedido> detallePedidos;
    private Cliente cliente;

    public GenerarPedidoDTO() {
        this.pedido = new Pedido();
        this.detallePedidos = new ArrayList<>();
        this.cliente = new Cliente();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<DetallePedido> getDetallePedidos() {
        return detallePedidos;
    }

    public void setDetallePedidos(ArrayList<DetallePedido> detallePedidos) {
        this.detallePedidos = detallePedidos;
    }
}
