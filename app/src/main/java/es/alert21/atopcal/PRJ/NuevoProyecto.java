package es.alert21.atopcal.PRJ;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Topcal;
import es.alert21.atopcal.Util;

public class NuevoProyecto extends AppCompatActivity {
    private ImageButton OK,Cancel;
    private EditText Nombre,Titulo,Descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_proyecto);
        OK = findViewById(R.id.OK);
        Cancel = findViewById(R.id.Cancel);
        Nombre = findViewById(R.id.nombreProyecto);
        Titulo = findViewById(R.id.tituloProyecto);
        Descripcion = findViewById(R.id.descripcionProyecto);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = Nombre.getText().toString().trim().toUpperCase();
                String titulo = Titulo.getText().toString().trim();
                String descripcion = Descripcion.getText().toString().trim();
                File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",nombre);
                Topcal topcal = new Topcal(path.toString());
                topcal.setPRJ(nombre,titulo,descripcion);
                Util.guardaConfiguracion(NuevoProyecto.this,"Nombre Proyecto",nombre);
                finish();
            }
        });
    }
}