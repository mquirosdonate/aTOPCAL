package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class BesselActivity extends AppCompatActivity {
    private String sql = "SELECT * FROM OBS\n" +
            " WHERE raw = 0 AND (NE,NV) IN \n" +
            " (SELECT NE,NV  \n" +
            " FROM OBS WHERE raw = 0" +
            " GROUP BY NE,NV\n" +
            " HAVING  COUNT(*)>1)\n" +
            " ORDER BY NE,NV,id";
    Topcal topcal;
    List<OBS> list = new ArrayList<>();
    List<OBSx2> obSx2List = new ArrayList<>();
    BesselAdapter adapter;
    ListView listView;
    Button ok,cancel;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("REGLA DE BESSEL");
        setContentView(R.layout.activity_bessel);
        ok = findViewById(R.id.calcular);
        cancel = findViewById(R.id.cancelar);
        listView = findViewById(R.id.listViewBessel);
        checkBox = findViewById(R.id.corregir);
        topcal = Util.getTopcal();
        list = topcal.getOBS(sql);
        for (int i = 0;i < list.size(); i += 2){
            if (i+1 == list.size())
                break;
            OBS obs1 = new OBS (list.get(i)) ;
            OBS obs2 = new OBS (list.get(i+1)) ;
            if (obs1.isCD() && obs2.isCD())//Las dos visuales son CD, NO nos vales
                continue;
            if (!obs1.isCD() && !obs2.isCD())//Las dos visuales son CI, NO nos valen
                continue;
            OBSx2 obSx2 = new OBSx2(obs1,obs2);
            obSx2List.add(obSx2);
        }

        if(obSx2List.size()== 0){
            ok.setVisibility(View.INVISIBLE);
            checkBox.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("NO HAY VISUALES PARA HACER REGLA DE BESSEL");
        }

        adapter = new BesselAdapter(this,obSx2List);
        listView.setAdapter(adapter);
        /*
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View view, int position, long id)
            {
                obSx2List.get(position).setValid(!obSx2List.get(position).getValid());
                TextView txtNE_NV = view.findViewById(R.id.textViewNE_NV);
                if (!obSx2List.get(position).getValid()){
                    txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                }
                return false;
            }
        });
        */
        listView.setOnItemClickListener((parent, view, position, id) -> {
            obSx2List.get(position).setValid(!obSx2List.get(position).getValid());
            TextView txtNE_NV = view.findViewById(R.id.textViewNE_NV);
            if (!obSx2List.get(position).getValid()){
                txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                //txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                //txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
        ok.setOnClickListener(v -> {
            OK();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        });
        cancel.setOnClickListener(v -> Cancel());

    }
    private void OK(){
        for(OBSx2 obSx2:obSx2List){
            if (!obSx2.getValid())
                continue;
            OBS aux = new OBS(obSx2.obsCorregida());
            aux.setId(0);
            topcal.insertOBS(aux);

            aux = new OBS(obSx2.getObs1());
            aux.setRaw(1);
            topcal.insertOBS(aux);
            aux = new OBS(obSx2.getObs2());
            aux.setRaw(1);
            topcal.insertOBS(aux);
        }
        finish();
    }
    private void Cancel(){
        if (obSx2List.size()== 0){
            finish();
        } else {
            showDialogOK("NO se aplicará la regla de Bessel a ninguna OBSERVACIÓN" +
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
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // botón al pulsar ir a atrás
            Toast.makeText(getApplicationContext(), "Utiliza el botón de Calcular o Cancelar para salir.",Toast.LENGTH_LONG).show();
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