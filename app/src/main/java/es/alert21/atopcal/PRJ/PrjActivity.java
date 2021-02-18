package es.alert21.atopcal.PRJ;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.OBS.ObsActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.Util;

public class PrjActivity extends AppCompatActivity {
    private ImageButton OK;
    private EditText Nombre,Titulo,Descripcion;
    private PRJ prj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prj);
        getSupportActionBar().setTitle("NUEVO PRJ");
        OK = findViewById(R.id.OK);
        Nombre = findViewById(R.id.nombreProyecto);
        Titulo = findViewById(R.id.tituloProyecto);
        Descripcion = findViewById(R.id.descripcionProyecto);

        prj = (PRJ) getIntent().getSerializableExtra("PRJ");
        if (prj.getId()>0)setPRJ(prj);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OK();
            }
        });
    }
    private void OK(){
        Topcal topcal=null;
        if(prj.getId() == 0){//Nuevo proyecto
            //Cargamos del formulario los datos del nuevo proyecto
            getPRJ();
            //Creamos la capeta del nuevo proyecto
            File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj.getNombre());
            topcal=new Topcal(path.toString());//La base de datos nueva
        } else {//Update
            //Utilizamos la carpeta donde está el pryecto que se quiere actualizar
            File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj.getNombre());
            topcal=new Topcal(path.toString());//La base de datos existente
            //Cargamos del formulario los datos actualizados del proyecto
            getPRJ();
        }
        Util.guardaConfiguracion(PrjActivity.this,"Nombre Proyecto",prj.getNombre());
        if (topcal != null)topcal.insertPRJ(prj);
        finish();
    }
    private void setPRJ(PRJ prj){
        Nombre.setText(prj.getNombre());
        Titulo.setText(prj.getTitulo());
        Descripcion.setText(prj.getDescripcion());
        getSupportActionBar().setTitle("EDITAR PRJ");
    }
    private void getPRJ(){
        prj.setNombre(Nombre.getText().toString().trim().toUpperCase());
        prj.setTitulo(Titulo.getText().toString().trim());
        prj.setDescripcion(Descripcion.getText().toString().trim());
    }
}