package es.alert21.atopcal.PTS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.EditarObsActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class EditarPtsActivity extends AppCompatActivity {
    private EditText nEditText,xEditText,yEditText,zEditText,desEditText,nombreEditText;
    Topcal topcal;
    Boolean bEditar = false;

    private ImageButton imageButtonOK, imageButtonDelete;
    private PTS pts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pts);

        imageButtonOK = findViewById(R.id.PtsActivityOK);
        imageButtonDelete = findViewById(R.id.PtsActivityCancel);
        nEditText = findViewById(R.id.editarPtsN);
        xEditText = findViewById(R.id.editarPtsX);
        yEditText = findViewById(R.id.editarPtsY);
        zEditText = findViewById(R.id.editarPtsZ);
        desEditText = findViewById(R.id.editarPtsDes);
        nombreEditText = findViewById(R.id.editarPtsNombre);
        getSupportActionBar().setTitle("NUEVO PTS");
        pts = (PTS) getIntent().getSerializableExtra("PTS");

        if (pts.getId()>0){
            bEditar = true;
            setPTS(pts);
        }

        imageButtonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OK();
            }
        });

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CancelOrDelete();
            }
        });
    }
    private void OK(){
        getPTS();
        if (topcal != null)topcal.insertPTS(pts);
        if(bEditar){
            finish();
        }else {
            newPTS();
        }
    }
    private void CancelOrDelete(){
        if(bEditar){
            if (topcal != null)topcal.borrarPTS(pts);
            finish();
        }else{
            newPTS();
        }
    }
    private void setPTS(PTS pts){
        nEditText.setText(pts.getN().toString());
        xEditText.setText(pts.getX().toString());
        yEditText.setText(pts.getY().toString());
        zEditText.setText(pts.getZ().toString());
        desEditText.setText(pts.getDes().toString());
        nombreEditText.setText(pts.getNombre().toString());
        getSupportActionBar().setTitle("EDITAR PTS");
    }
    private void newPTS(){
        nEditText.setText("");
        xEditText.setText("");
        yEditText.setText("");
        zEditText.setText("");
        desEditText.setText("");
        nombreEditText.setText("");
    }
    private void getPTS(){
        String n = nEditText.getText().toString();
        if (n.isEmpty()){
            // Indicar que hay un error, quizás con un sonido
            return;
        }
        pts.setN(n);
        pts.setX(xEditText.getText().toString());
        pts.setY(yEditText.getText().toString());
        pts.setZ(zEditText.getText().toString());
        pts.setDes(desEditText.getText().toString());
        pts.setNombre(nombreEditText.getText().toString());
        getSupportActionBar().setTitle(pts.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String prj = Util.cargaConfiguracion(EditarPtsActivity.this,"Nombre Proyecto","");
        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);
        topcal = new Topcal(path.toString());
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
}