package org.victayagar.retromode_app.communication;

/*
La interfaz define un método anularPedido que acepta
un parámetro id de tipo int y devuelve un resultado
de tipo String. La implementación de esta interfaz
proporcionará la lógica para anular el pedido en función del ID proporcionado.
*/

public interface AnularPedidoCommunication {
    // Método para anular un pedido dado su ID
    String anularPedido(int id);
}
