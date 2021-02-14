package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Topcal;
import es.alert21.atopcal.Util;

public class ObsActivity extends AppCompatActivity {
    private EditText neEditText,nvEditText,hEditText,vEditText,dEditText,mEditText,iEditText;
    private TextView titulo;
    private ImageButton OK,Cancel;
    private OBS obs;
    Topcal topcal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs);
        titulo = findViewById(R.id.textViewTitulo);
        OK = findViewById(R.id.ObsActivitiOK);
        Cancel = findViewById(R.id.ObsActivitiCancel);
        neEditText = findViewById(R.id.ObsActivityNE);
        nvEditText = findViewById(R.id.ObsActivityNV);
        hEditText = findViewById(R.id.ObsActivityH);
        vEditText = findViewById(R.id.ObsActivityV);
        dEditText = findViewById(R.id.ObsActivityD);
        mEditText  = findViewById(R.id.ObsActivityM);
        iEditText = findViewById(R.id.ObsActivityI);
        obs = new OBS();
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOBS();
                newOBS();
            }
        });
        Cancel.setVisibility(View.INVISIBLE);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newOBS();
            }
        });
    }
    private void getOBS(){
        obs = new OBS();
        String ne = neEditText.getText().toString();
        String nv = nvEditText.getText().toString();
        if (ne.isEmpty() || nv.isEmpty()){
            // Indicar que hay un error, quizás con un sonido
            return;
        }
        obs.setNe(ne);
        obs.setNv(nv);
        obs.setH(hEditText.getText().toString());
        obs.setV(vEditText.getText().toString());
        obs.setD(dEditText.getText().toString());
        obs.setM(mEditText.getText().toString());
        obs.setI(iEditText.getText().toString());
        titulo.setText(obs.toString());
        if (topcal != null)topcal.setOBS(obs);
    }
    private void newOBS(){
        neEditText.setText(obs.getNe().toString());
        iEditText.setText(obs.getI().toString());
        mEditText.setText(obs.getM().toString());
        Integer nv = obs.getNv() + 1;
        nvEditText.setText(nv.toString());
        hEditText.setText("");
        hEditText.requestFocus();
        vEditText.setText("");
        dEditText.setText("");
    }
    @Override
    protected void onResume() {
        super.onResume();
        String prj = Util.cargaConfiguracion(ObsActivity.this,"Nombre Proyecto","");
        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);
        topcal = new Topcal(path.toString());
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
}