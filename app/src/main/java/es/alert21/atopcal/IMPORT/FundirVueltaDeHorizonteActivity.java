package es.alert21.atopcal.IMPORT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.NEAdapter;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.OBS.OBSx2;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class FundirVueltaDeHorizonteActivity extends AppCompatActivity {
    Topcal topcal;
    List<OBSx2> obSx2List = new ArrayList<>();
    OBSx2Adapter adapter;
    ListView listView;
    EditText desorientacion;
    Button ok,cancel;
    Integer NE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fundir_vuelta_de_horizonte);
        ok = findViewById(R.id.fundirOK);
        cancel = findViewById(R.id.fundirCancelar);
        desorientacion = findViewById(R.id.editTextTextDesorientacion);
        listView = findViewById(R.id.listViewOBSx2);

        Bundle b = this.getIntent().getExtras();
        String tablaTemp = b.getString("TABLA","");

        topcal = Util.getTopcal();

        String sql1 = "SELECT * FROM OBS WHERE (NE,NV) IN (SELECT NE,NV FROM "+tablaTemp+") ORDER By NV,V";
        String sql2 = "SELECT * FROM "+tablaTemp+" WHERE (NE,NV) IN (SELECT NE,NV FROM OBS) ORDER By NV,V";
        List<OBS> list1 = topcal.getOBS(sql1);
        List<OBS> list2 = topcal.getOBS(sql2);
        NE = list1.get(0).getNe();
        getSupportActionBar().setTitle("Fundir vuelta de horizonte: "+NE.toString());

        for (int i = 0; i < list1.size();i++){
            OBSx2 obSx2 = new OBSx2(list1.get(i),list2.get(i));
            obSx2List.add(obSx2);
        }
        CalculaDesorientacion();

        adapter = new OBSx2Adapter(this,obSx2List);
        //adding the adapter to listview
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {

            obSx2List.get(position).setValid(!obSx2List.get(position).getValid());
            CalculaDesorientacion();

            TextView info = view.findViewById(R.id.TextViewItem);
            TextView result = view.findViewById(R.id.textViewResult);

            if (!obSx2List.get(position).getValid()){
                result.setPaintFlags(result.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                info.setPaintFlags(info.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                result.setPaintFlags(result.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                info.setPaintFlags(info.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
        ok.setOnClickListener(v -> {
            fundirOK(tablaTemp);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });
        cancel.setOnClickListener(v -> fundirCancel());
    }
    private void CalculaDesorientacion(){
        Double des = 0.0;
        int n = 0;
        for (OBSx2 obSx2:obSx2List){
            if (obSx2.getValid()){
                des += obSx2.desorientacion();
                n++;
            }
        }
        if (n > 0) {
            des /= n;
        } else {
            des = 0.0;
        }
        desorientacion.setText(Util.doubleATexto(des,4));
    }
    private void fundirOK(String tablaTemp){
        double des = Double.parseDouble(desorientacion.getText().toString());
        String sql = "SELECT * FROM "+tablaTemp;
        List<OBS> list = topcal.getOBS(sql);
        for(OBS obs:list){
            obs.setId(0);//para poderlas insertar en lugar de update
            int nv = obs.getNv();
            double h = obs.getH();
            for(OBSx2 obSx2:obSx2List){
                int NV = obSx2.getObs2().getNv();
                double H = obSx2.getObs2().getH();
                boolean valid = obSx2.getValid();
                if ((!valid) && (NV == nv) && (H == h)){
                    obs.setRaw(1);
                }
            }
            obs.setH(obs.getH()+des);
        }
        topcal.insertOBS(list);
        topcal.borrarTabla(tablaTemp);
    }
    private void fundirCancel(){
        showDialogOK("PELIGRO has cancelado la importación de esta ESTACIÓN " + NE.toString() +
                "\n¿Estás seguro de que quieres salir?",
                (dialog, which) -> {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Intent intent = new Intent();
                            setResult(RESULT_CANCELED, intent);
                            finish();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            // proceed with logic by disabling the related features or quit the app.

                            break;
                    }
                });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // botón al pulsar ir a atrás
            Toast.makeText(getApplicationContext(), "Utiliza el botón de Aceptar o Cancelar para salir.",Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", okListener)
                .create()
                .show();
    }
}