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
    Topcal topcal;
    ListView listViewNEs;
    List<NE> neList = new ArrayList<>();
    RadiarAdapter adapterNEs;
    String sql = "SELECT * FROM PTS WHERE N IN \n" +
            "(SELECT  DISTINCT NE FROM OBS,PTS WHERE V>0 AND D>0 AND NV NOT IN (SELECT N FROM PTS))";
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
        File file = new File(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida);
        OutputStreamWriter fout = null;

        try {
            fout = new OutputStreamWriter(new FileOutputStream(file, false));
            fout.write(es.alert21.atopcal.HTML.Util.getHead(nombreFicheroSalida));

            for(NE ne:neList){
                if (ne.checked){
                    PTS n = ne.n;
                    fout.write("<table><tbody>\n");
                    fout.write(n.toStringTH("ESTACIÓN",true));
                    fout.write(n.toStringTD(true));
                    fout.write("</tbody></table>");
                    utm.UTM2GEO(n.getY(),n.getX(),30);//Da igual el huso que pongamos
                    String sql = "SELECT * FROM OBS WHERE V>0 AND D>0 AND NV NOT IN (SELECT N FROM PTS) AND NE="+n.getNtoString();
                    List<OBS> obsList = topcal.getOBS(sql);

                    fout.write("<table><tbody>");
                    fout.write(new Visual().toStringTH(false));
                    List<PTS> ptsList = new ArrayList<>();
                    for(OBS obs:obsList){
                        PTS nv = new PTS();
                        nv.setN(obs.getNv());
                        Visual v = new Visual(obs,n,nv,utm.k);
                        ptsList.add(nv);
                        fout.write(v.toString(false));
                        topcal.insertPTS(nv);
                    }
                    fout.write("</tbody></table>");

                    fout.write("<table><tbody>\n");
                    fout.write(n.toStringTH("Puntos radiados",false));
                    for(PTS p:ptsList){
                        fout.write(p.toStringTD(false));
                    }
                    fout.write("</tbody></table>");
                }
            }

            fout.write(es.alert21.atopcal.HTML.Util.getFinal());
            fout.flush();
            fout.close();
            Toast.makeText(getApplicationContext(), "Se ha creado el fichero: "+nombreFicheroSalida, Toast.LENGTH_LONG).show();
        } catch(FileNotFoundException e){e.printStackTrace();} catch (IOException e) {e.printStackTrace();}

        finish();
    }
    private void SetAdapterNEs(){
        adapterNEs = new RadiarAdapter(RadiarActivity.this,neList,R.layout.list_radiar);
        listViewNEs.setAdapter(adapterNEs);
    }
}