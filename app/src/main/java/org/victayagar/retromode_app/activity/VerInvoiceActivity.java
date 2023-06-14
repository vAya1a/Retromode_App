package org.victayagar.retromode_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;

import org.victayagar.retromode_app.R;

/* la clase VerInvoiceActivity muestra un archivo PDF utilizando PdfViewer y
proporciona una barra de herramientas con una función de navegación que permite
al usuario volver atrás.
*/

public class VerInvoiceActivity extends AppCompatActivity {
    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_invoice);
        // Obtiene el archivo PDF como un array de bytes a través de los extras de la intención
        byte[] pdf = getIntent().getByteArrayExtra("pdf");
        // Encuentra la vista PDFView en el layout utilizando su id
        pdfView = findViewById(R.id.pdfView);
        // Carga el archivo PDF en la vista pdfView
        pdfView.fromBytes(pdf).load();
        // Llama al método init() para realizar la inicialización adicional
        init();
    }

    private void init() {
        // Encuentra la vista Toolbar en el layout utilizando su id
        Toolbar toolbar = this.findViewById(R.id.toolbar);
        // Establece un listener de clic para la vista de navegación del Toolbar
        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
            // Aplica una animación de transición personalizada
            this.overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
        });
    }
}