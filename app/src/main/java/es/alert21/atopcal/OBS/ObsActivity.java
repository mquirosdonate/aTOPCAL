package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.Util;

public class ObsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView txtRaw;
    private EditText neEditText,nvEditText,hEditText,vEditText,dEditText,mEditText,iEditText;
    private ImageButton imageButtonOK, imageButtonDelete;
    private OBS obs;
    Topcal topcal;
    Boolean bEditar = false;
    Spinner spinner;
    String[] raws = { "sin borrar", "borrada"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs);
        txtRaw = findViewById(R.id.textViewRaw);
        spinner = findViewById(R.id.rawObs);


        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,raws);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        imageButtonOK = findViewById(R.id.ObsActivitiOK);
        imageButtonDelete = findViewById(R.id.ObsActivitiCancel);
        neEditText = findViewById(R.id.ObsActivityNE);
        nvEditText = findViewById(R.id.ObsActivityNV);
        hEditText = findViewById(R.id.ObsActivityH);
        vEditText = findViewById(R.id.ObsActivityV);
        dEditText = findViewById(R.id.ObsActivityD);
        mEditText  = findViewById(R.id.ObsActivityM);
        iEditText = findViewById(R.id.ObsActivityI);
        getSupportActionBar().setTitle("NUEVA OBS");

        obs = (OBS) getIntent().getSerializableExtra("OBS");
        if (obs.getId()>0){
            bEditar = true;
            spinner.setVisibility(View.VISIBLE);
            txtRaw.setVisibility(View.VISIBLE);
            spinner.setSelection(obs.getRaw());
            setOBS(obs);
        }else{
            spinner.setVisibility(View.INVISIBLE);
            txtRaw.setVisibility(View.INVISIBLE);
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
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),raws[position] , Toast.LENGTH_LONG).show();
        obs.setRaw(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void CancelOrDelete(){
        if(bEditar){
            if (topcal != null)topcal.borrarOBS(obs);
            finish();
        }else{
            newOBS();
        }
    }
    private void OK(){
        getOBS();
        if (topcal != null)topcal.insertOBS(obs);
        if(bEditar){
            finish();
        }else {
            newOBS();
        }
    }
    private void setOBS(OBS obs){
        neEditText.setText(obs.getNe().toString());
        nvEditText.setText(obs.getNv().toString());
        hEditText.setText(obs.getH().toString());
        vEditText.setText(obs.getV().toString());
        dEditText.setText(obs.getD().toString());
        mEditText.setText(obs.getM().toString());
        iEditText.setText(obs.getI().toString());

        getSupportActionBar().setTitle("EDITAR OBS");
        //imageButtonDelete.setVisibility(View.VISIBLE);

    }
    private void getOBS(){
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
        getSupportActionBar().setTitle(obs.toString());
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