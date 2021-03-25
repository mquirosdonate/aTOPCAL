package es.alert21.atopcal.RADIAR;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.EPSG.Elipsoide;
import es.alert21.atopcal.EPSG.UTM;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.POLIG.PoligActivity;
import es.alert21.atopcal.POLIG.PoligAdapter;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.TOPO.Visual;
import es.alert21.atopcal.Util;

public class RadiarActivity extends AppCompatActivity {
    static  StringBuilder html = new StringBuilder();
    Topcal topcal;
    ListView listViewNEs;
    List<NE> neList = new ArrayList<>();
    RadiarAdapter adapterNEs;
    String sql = "SELECT * FROM PTS WHERE N IN \n" +
            "(SELECT  DISTINCT NE FROM OBS,PTS WHERE OBS.raw = 0 AND V>0 AND D>0 AND NV NOT IN (SELECT N FROM PTS))";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radiar);
        getSupportActionBar().setTitle("RADIACIÓN");
        Button calcular;
        calcular = findViewById(R.id.radiar);
        calcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radiar();
            }
        });
        listViewNEs = (ListView) findViewById(R.id.listViewRadiar);

        topcal = Util.getTopcal();
        List<PTS> ptsList = topcal.getPTS(sql);
        for(PTS n:ptsList){
            neList.add(new NE(n));
        }
        SetAdapterNEs();

    }
    private void Radiar(){
        if (neList.size()==0){
            Toast.makeText(getApplicationContext(), "NO HAS SELECCIONADO NINGUNA ESTACIÓN", Toast.LENGTH_LONG).show();
            return;
        }

        Elipsoide elipsoide = new Elipsoide();
        UTM utm = new UTM(elipsoide);

        String nombreFicheroSalida = "";
        nombreFicheroSalida = "RADIAR";
        for(NE ne:neList){
            if (ne.checked) nombreFicheroSalida += "-"+ne.n.getN();
        }
        nombreFicheroSalida += ".html";

        html.setLength(0);
        html.append(es.alert21.atopcal.HTML.Util.getHead(nombreFicheroSalida));
        for(NE ne:neList){
            if (ne.checked){
                PTS n = ne.n;
                html.append("<table><tbody>\n");
                html.append(n.toStringTH("ESTACIÓN",true));
                html.append(n.toStringTD(true));
                html.append("</tbody></table>");
                utm.UTM2GEO(n.getY(),n.getX(),30);//Da igual el huso que pongamos
                String sql = "SELECT * FROM OBS WHERE OBS.raw = 0 AND V>0 AND D>0 AND NV NOT IN (SELECT N FROM PTS) AND NE="+n.getNtoString();
                List<OBS> obsList = topcal.getOBS(sql);

                html.append("<table><tbody>");
                html.append(new Visual().toStringTH(false));
                List<PTS> ptsList = new ArrayList<>();
                for(OBS obs:obsList){
                    PTS nv = new PTS();
                    nv.setN(obs.getNv());
                    Visual v = new Visual(obs,n,nv,utm.k);
                    ptsList.add(nv);
                    html.append(v.toString(false));
                    topcal.insertPTS(nv);
                }
                html.append("</tbody></table>");

                html.append("<table><tbody>\n");
                html.append(n.toStringTH("Puntos radiados",false));
                for(PTS p:ptsList){
                    html.append(p.toStringTD(false));
                }
                html.append("</tbody></table>");
            }
        }
        html.append(es.alert21.atopcal.HTML.Util.getFinal());

        topcal.insertHTML(nombreFicheroSalida,html.toString());

        Util.escribeFichero(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida,html.toString());

        Toast.makeText(getApplicationContext(), "Se ha creado el fichero: "+nombreFicheroSalida, Toast.LENGTH_LONG).show();

        finish();
    }
    private void SetAdapterNEs(){
        adapterNEs = new RadiarAdapter(RadiarActivity.this,neList,R.layout.list_radiar);
        listViewNEs.setAdapter(adapterNEs);
    }
}