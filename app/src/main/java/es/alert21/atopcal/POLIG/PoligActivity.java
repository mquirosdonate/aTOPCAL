package es.alert21.atopcal.POLIG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class PoligActivity extends AppCompatActivity {

    Topcal topcal;
    ListView listViewNEs;
    List<PTS> neList = new ArrayList<>();
    PoligAdapter adapterNEs;

    ListView listViewPolig;
    List<PTS> listPolig = new ArrayList<>();
    PoligAdapter adapterPolig;
    List<Eje> ejeList = new ArrayList<>();

    LinearLayout linearLayout1;
    LinearLayout linearLayout2;
    LinearLayout.LayoutParams param1;
    LinearLayout.LayoutParams param2;
    TextView textView,textViewErrX,textViewErrY,textViewErrZ,textViewErrAz;
    Button calcular;
    boolean bCalcular = true;

    CheckBox chkXY,chkZ,chkAz;
    String sql = "SELECT * FROM PTS WHERE N IN (SELECT DISTINCT(ne) FROM OBS,PTS WHERE Des>0 AND PTS.N=OBS.NE) ORDER BY N";
    String sql3 ="CREATE VIEW VIEWOBS AS " +
            "SELECT NE,D FROM OBS WHERE raw = 0 AND NV=%d";
    String sql4 ="SELECT NV FROM OBS,VIEWOBS " +
            "WHERE OBS.NE = %d AND OBS.NV <> %d " +
            "AND raw = 0 AND OBS.NV = VIEWOBS.NE " +
            "AND (OBS.D > 0 OR VIEWOBS.D>0) " +
            "ORDER BY OBS.NV;";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polig);

        linearLayout1 = findViewById(R.id.polig1);
        linearLayout2 = findViewById(R.id.polig2);

        listViewNEs = (ListView) findViewById(R.id.leftListView);
        listViewPolig = (ListView) findViewById(R.id.rightListView);

        chkXY = findViewById(R.id.checkBoxCompensaXY);
        chkZ = findViewById(R.id.checkBoxCompensaZ);
        chkAz = findViewById(R.id.checkBoxCompensaAz);

        chkXY.setVisibility(View.GONE);
        chkZ.setVisibility(View.GONE);
        chkAz.setVisibility(View.GONE);

        calcular = findViewById(R.id.poligCalcular);
        calcular.setVisibility(View.GONE);
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bCalcular) {
                    setCalcular();
                } else {
                    //compensar
                }
            }
        });
        textViewErrX = findViewById(R.id.textViewErrX);
        textViewErrY = findViewById(R.id.textViewErrY);
        textViewErrZ = findViewById(R.id.textViewErrZ);
        textViewErrAz = findViewById(R.id.textViewErrAz);

        textView = findViewById(R.id.textView19);
        topcal = Util.getTopcal();
        neList = topcal.getPTS(sql);
        SetAdapterNEs();

        listViewNEs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PTS ne = neList.get(position);

                AddListPolig(ne);
                //Toast.makeText(getApplicationContext(), siguienteEstacion.toString(), Toast.LENGTH_LONG).show();
            }
        });
        listViewPolig.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EliminarUltimaDeListPolig();
            }
        });
    }
    private void setSeleccionar(){
        chkXY.setVisibility(View.GONE);
        chkZ.setVisibility(View.GONE);
        chkAz.setVisibility(View.GONE);
        textViewErrX.setVisibility(View.VISIBLE);
        textViewErrY.setVisibility(View.VISIBLE);
        textViewErrZ.setVisibility(View.VISIBLE);
        textViewErrAz.setVisibility(View.VISIBLE);
        calcular.setText("Calcular");
        bCalcular = true;
    }
    private void setCalcular(){
        bCalcular = false;
        chkXY.setVisibility(View.VISIBLE);
        chkZ.setVisibility(View.VISIBLE);
        chkAz.setVisibility(View.VISIBLE);

        chkXY.setText("compensar "+textViewErrX.getText().toString()+" "+textViewErrY.getText().toString());
        chkZ.setText("compensar "+textViewErrZ.getText().toString());
        chkAz.setText("compensar "+textViewErrAz.getText().toString());

        textViewErrX.setVisibility(View.GONE);
        textViewErrY.setVisibility(View.GONE);
        textViewErrZ.setVisibility(View.GONE);
        textViewErrAz.setVisibility(View.GONE);
        calcular.setText("OK");

        neList.clear();
        SetAdapterNEs();

        SetAdapterEjes();
    }

    private void AddEje(){
        if(listPolig.size()<2) {
            ejeList.clear();
            return;
        }
        PTS n1 = listPolig.get(listPolig.size()-2);
        PTS n2 = listPolig.get(listPolig.size()-1);

        List<OBS> n1_2 = topcal.getOBS("SELECT * FROM OBS WHERE NE="+n1.getNtoString()+" AND NV="+n2.getNtoString());
        List<OBS> n2_1 = topcal.getOBS("SELECT * FROM OBS WHERE NE="+n2.getNtoString()+" AND NV="+n1.getNtoString());

        Eje eje = new Eje(n1_2.get(0),n2_1.get(0),n1,n2);
        ejeList.add(eje);

    }
    private void AddListPolig(PTS siguienteEstacion){
        Integer ultimaEstacion = 0;
        if(listPolig.size()>0){
            ultimaEstacion = listPolig.get(listPolig.size()-1).getN();
        }
        String sqlView = String.format(sql3,siguienteEstacion.getN());
        String sqlSelect = String.format(sql4,siguienteEstacion.getN(),ultimaEstacion);
        List<Integer> listNV = topcal.getNVs(sqlView,sqlSelect,"DROP VIEW VIEWOBS");
        neList.clear();
        for(Integer n:listNV){
            neList.add(topcal.getPTS(n)) ;
        }
        SetAdapterNEs();

        listPolig.add(siguienteEstacion);

        AddEje();
        SetAdapterPolig();
    }

    private void EliminarUltimaDeListPolig(){
        setSeleccionar();
        if (listPolig.size() > 0){
            listPolig.remove(listPolig.size()-1);
            if (ejeList.size()>0){
                ejeList.remove(ejeList.size()-1);
            }
            SetAdapterPolig();
            if(listPolig.size()==0){
                neList = topcal.getPTS(sql);
            }else {
                PTS aux = listPolig.get(listPolig.size() - 1);
                Integer ultimaEstacion = 0;
                if (listPolig.size() > 1) {
                    ultimaEstacion = listPolig.get(listPolig.size() - 2).getN();
                }
                String sqlView = String.format(sql3, aux.getN());
                String sqlSelect = String.format(sql4, aux.getN(), ultimaEstacion);
                List<Integer> listNV = topcal.getNVs(sqlView, sqlSelect, "DROP VIEW VIEWOBS");
                neList.clear();
                for (Integer n : listNV) {
                    neList.add(topcal.getPTS(n));
                }
            }
            SetAdapterNEs();
        }else{
            showDialogOK("Salir?",(dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        // proceed with logic by disabling the related features or quit the app.
                        break;
                }
            });
        }
    }
    private void SetAdapterEjes(){
        textView.setText("Ejes de la poligonal");
        PoligAdapterEjes poligAdapter = new PoligAdapterEjes(PoligActivity.this,ejeList,R.layout.list_eje);
        listViewNEs.setAdapter(poligAdapter);
    }
    private void SetAdapterNEs(){
        textView.setText("Estaciones candidatas ("+neList.size()+")");
        adapterNEs = new PoligAdapter(PoligActivity.this,neList,R.layout.list_polig);
        listViewNEs.setAdapter(adapterNEs);
    }
    private void SetAdapterPolig(){
        if(listPolig.size()==0){
            param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,6.0f );
            linearLayout1.setLayoutParams(param1);

            param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f );
            linearLayout2.setLayoutParams(param2);
        }else{
            param1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,1.0f );
            linearLayout1.setLayoutParams(param1);

            param2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,2.0f );
            linearLayout2.setLayoutParams(param2);
        }
        textViewErrX.setText("");
        textViewErrY.setText("");
        textViewErrZ.setText("");
        textViewErrAz.setText("");
        if (ejeList.size() > 0) {
            Eje eje = ejeList.get(ejeList.size() - 1);
            PTS n2 = eje.n2;
            if (n2.getId() > 0) {
                List<PTS> list = topcal.getPTS("SELECT * FROM PTS WHERE Id="+n2.getId());
                if (list.size()>0) {
                    PTS aux = list.get(0);
                    double errX = n2.getX() - aux.getX();
                    double errY = n2.getY() - aux.getY();
                    double errZ = n2.getZ() - aux.getZ();
                    double errAz = n2.getDes() - aux.getDes();
                    textViewErrX.setText("eX:"+ Util.doubleATexto(errX, 3));
                    textViewErrY.setText("eY:"+ Util.doubleATexto(errY, 3));
                    textViewErrZ.setText("eZ:"+ Util.doubleATexto(errZ, 3));
                    textViewErrAz.setText("eΣ:"+ Util.doubleATexto(errAz, 4));
                }
            }
            calcular.setVisibility(View.VISIBLE);
        } else {
            calcular.setVisibility(View.GONE);
        }

        getSupportActionBar().setTitle("ESTACIONES DE LA POLIGONAL ("+listPolig.size()+")");
        adapterPolig = new PoligAdapter(PoligActivity.this,listPolig,R.layout.list_polig);
        listViewPolig.setAdapter(adapterPolig);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // botón al pulsar ir a atrás
            EliminarUltimaDeListPolig();
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