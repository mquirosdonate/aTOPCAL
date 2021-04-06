package es.alert21.atopcal.MMCC;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class MainMMCCActivity extends AppCompatActivity {
    Button buttonSiguiente,buttonSalir;
    CheckBox INCOG_K,DIRE,DIST,ALTI,LIST_OBS,LIST_NOR;
    RadioButton COMPENSACION,SIMULACION,TEST_OBS;
    EditText ERR_K,ERR_D2,ERR_D1,ERR_AD,ERR_A;
    private CFG cfg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_m_m_c_c);
        getSupportActionBar().setTitle("MMCC cfg");

        INCOG_K = findViewById(R.id.INCOG_K);
        DIRE = findViewById(R.id.DIRE);
        DIST = findViewById(R.id.DIST);
        ALTI = findViewById(R.id.ALTI);
        LIST_OBS = findViewById(R.id.LIST_OBS);
        LIST_NOR = findViewById(R.id.LIST_NOR);
        COMPENSACION = findViewById(R.id.COMPENSACION);
        SIMULACION = findViewById(R.id.SIMULACION);
        TEST_OBS = findViewById(R.id.TEST_OBS);
        ERR_K = findViewById(R.id.ERR_K);
        ERR_D2 = findViewById(R.id.ERR_D2);
        ERR_D1 = findViewById(R.id.ERR_D1);
        ERR_AD = findViewById(R.id.ERR_AD);
        ERR_A = findViewById(R.id.ERR_A);

        Topcal topcal = Util.getTopcal();
        cfg = topcal.getRED_CFG();
        setControles();
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        buttonSalir = findViewById(R.id.buttonSalir);
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getControles();
                topcal.insertRED_CFG(cfg);
                Intent intent = new Intent(MainMMCCActivity.this, MMCCActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setControles(){
        INCOG_K.setChecked(cfg.incog_k > 0 ? true:false);
        DIRE.setChecked(cfg.DIRE > 0 ? true:false);
        DIST.setChecked(cfg.DIST > 0 ? true:false);
        ALTI.setChecked(cfg.ALTI > 0 ? true:false);
        LIST_NOR.setChecked(cfg.LIST_NOR > 0 ? true:false);
        LIST_OBS.setChecked(cfg.LIST_OBS > 0 ? true:false);
        TEST_OBS.setChecked(cfg.TEST_OBS > 0 ? true:false);
        SIMULACION.setChecked(cfg.SIMULACION > 0 ? true:false);
        COMPENSACION.setChecked(cfg.COMPENSACION > 0 ? true:false);
        ERR_K.setText(Util.doubleATexto(cfg.ERR_K,2));
        ERR_A.setText(String.valueOf(cfg.ERR_A));
        ERR_AD.setText(String.valueOf(cfg.ERR_AD));
        ERR_D1.setText(String.valueOf(cfg.ERR_D1));
        ERR_D2.setText(String.valueOf(cfg.ERR_D2));
    }
    private void getControles(){
        cfg.incog_k = INCOG_K.isChecked()?1:0;
        cfg.DIRE = DIRE.isChecked()?1:0;
        cfg.DIST = DIST.isChecked()?1:0;
        cfg.ALTI = ALTI.isChecked()?1:0;
        cfg.LIST_NOR = LIST_NOR.isChecked()?1:0;
        cfg.LIST_OBS = LIST_OBS.isChecked()?1:0;
        cfg.TEST_OBS = TEST_OBS.isChecked()?1:0;
        cfg.SIMULACION = SIMULACION.isChecked()?1:0;
        cfg.COMPENSACION = COMPENSACION.isChecked()?1:0;
        cfg.ERR_A = Integer.parseInt(ERR_A.getText().toString());
        cfg.ERR_AD = Integer.parseInt(ERR_AD.getText().toString());
        cfg.ERR_D1 = Integer.parseInt(ERR_D1.getText().toString());
        cfg.ERR_D2 = Integer.parseInt(ERR_D2.getText().toString());
        cfg.ERR_K = Double.parseDouble(ERR_K.getText().toString());
    }
}