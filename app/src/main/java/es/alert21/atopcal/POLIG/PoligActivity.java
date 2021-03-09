package es.alert21.atopcal.POLIG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
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
    TextView textView;

    String sql = "SELECT * FROM PTS WHERE N IN (SELECT DISTINCT(ne) FROM OBS,PTS WHERE PTS.N=OBS.NE) ORDER BY N";
    String sql3 ="CREATE VIEW VIEWOBS AS " +
            "SELECT NE,D " +
            "FROM OBS " +
            "WHERE raw = 0 AND NV=%d";
    String sql4 ="SELECT NV FROM OBS,VIEWOBS " +
            "WHERE OBS.NE = %d " +
            "AND OBS.NV <> %d " +
            "AND raw = 0 AND OBS.NV = VIEWOBS.NE " +
            "AND (OBS.D > 0 OR VIEWOBS.D>0) " +
            "ORDER BY OBS.NV;";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polig);
        listViewNEs = (ListView) findViewById(R.id.leftListView);
        listViewPolig = (ListView) findViewById(R.id.rightListView);

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
        SetAdapterPolig();
    }


    private void EliminarUltimaDeListPolig(){
        if (listPolig.size() > 0){
            listPolig.remove(listPolig.size()-1);
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
    private void SetAdapterNEs(){
        textView.setText("Estaciones candidatas ("+neList.size()+")");
        adapterNEs = new PoligAdapter(PoligActivity.this,neList,R.layout.list_polig);
        listViewNEs.setAdapter(adapterNEs);
    }
    private void SetAdapterPolig(){
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