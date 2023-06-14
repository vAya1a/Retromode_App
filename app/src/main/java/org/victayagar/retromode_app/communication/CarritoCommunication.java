package org.victayagar.retromode_app.communication;

/*
La interfaz define un método eliminarDetalle que acepta
un parámetro idP de tipo int. La implementación de esta
interfaz proporcionará la lógica para eliminar el detalle
del carrito correspondiente al ID de producto proporcionado.
*/
public interface CarritoCommunication {
    // Método para eliminar un detalle del carrito dado su ID de producto
    void eliminarDetalle(int idP);
}
