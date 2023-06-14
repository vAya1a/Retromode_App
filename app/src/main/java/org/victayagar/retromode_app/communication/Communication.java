package org.victayagar.retromode_app.communication;

import android.content.Intent;

public interface Communication {
    // Método para mostrar los detalles de un objeto a través de un Intent
    void showDetails(Intent i);

    // Método para exportar una factura
    void exportInvoice(int idCli, int idOrden, String fileName);
}
