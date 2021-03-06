package es.alert21.atopcal.PTS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.OBS.OBSx2;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class DesorientacionesActivity extends AppCompatActivity {
    Topcal topcal;
    ListView listView;
    EditText desorientacion;
    Button ok;
    EstacionAdapter adapter;
    Estacion est;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desorientaciones);
        ok = findViewById(R.id.calcular);

        desorientacion = findViewById(R.id.editTextTextDesorientacion2);
        listView = findViewById(R.id.listViewDesorientaciones);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            est.obsList.get(position).valid = !est.obsList.get(position).valid;
            CalculaDesorientacion();
            TextView txtNV = view.findViewById(R.id.listObsNV);
            TextView txtH = view.findViewById(R.id.ListObsH);
            TextView txtAz = view.findViewById(R.id.listAz);
            TextView txtDes = view.findViewById(R.id.listDes);
            if (!est.obsList.get(position).valid){
                txtNV.setPaintFlags(txtNV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                txtH.setPaintFlags(txtH.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                txtAz.setPaintFlags(txtAz.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                txtDes.setPaintFlags(txtDes.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                txtNV.setPaintFlags(txtNV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                txtH.setPaintFlags(txtH.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                txtAz.setPaintFlags(txtAz.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                txtDes.setPaintFlags(txtDes.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        });
        ok.setOnClickListener(v -> {
            est.estacion.setDes(desorientacion.getText().toString());
            topcal.insertPTS(est.estacion);
            finish();
        });
    }
    private void CalculaDesorientacion(){
        Double des = 0.0;
        int n = 0;
        for (PTV_OBS ptv_obs:est.obsList){
            if (ptv_obs.valid){
                des += ptv_obs.desorientacion;
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
    @Override
    protected void onResume() {
        super.onResume();
         topcal = Util.getTopcal();
        PTS pto = (PTS) getIntent().getSerializableExtra("PTS");
        getSupportActionBar().setTitle("ESTACIÓN "+ pto.getN());

        String sql = "SELECT * FROM PTS,OBS\n" +
                "WHERE OBS.raw = 0" +
                " AND NE = " + pto.getN()+
                " AND NV = N" +
                " ORDER BY NV";

        est = new Estacion(pto);
        est.addVisados(topcal.getPTV_OBS(sql));

        adapter = new EstacionAdapter(this,est.obsList);
        listView.setAdapter(adapter);
        CalculaDesorientacion();
    }
}