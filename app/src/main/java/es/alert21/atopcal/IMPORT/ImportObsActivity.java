package es.alert21.atopcal.IMPORT;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.EditarObsActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class ImportObsActivity extends AppCompatActivity {
    Topcal topcal;
    static boolean EOF = false;
    int tipoFichero;
    BufferedReader reader = null;
    private List<String> tablasTemp = new ArrayList<>();
    boolean bVisual = false;
    int ne = -99;
    int nv = -99;
    double i = -99;
    double m = -99;
    int cod = -99;
    Integer countNE = 0;
    Integer countRegs = 0;
    TextView textView, txtRegs, txtNEs, txtFichero ;
    ProgressBar progressBar;
    Button button;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_obs);
        getSupportActionBar().setTitle("");

        topcal = Util.getTopcal();

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        textView = findViewById(R.id.textView16);
        txtRegs = findViewById(R.id.textView14);
        txtNEs = findViewById(R.id.textView13);
        txtFichero = findViewById(R.id.textView17);

        Bundle b = this.getIntent().getExtras();
        String nombreFichero = b.getString("FILE","");
        tipoFichero = b.getInt("TIPO",0);
        file = new File(nombreFichero);

        switch (tipoFichero){
            case 0:
                getSupportActionBar().setTitle("IMPORTAR DE LEICA");
                break;
            case 1:
                getSupportActionBar().setTitle("IMPORTAR DE GEODIMETER");
                break;
            case 2:
                getSupportActionBar().setTitle("IMPORTAR DE TXT o CSV");
                break;
        }
        if (topcal != null && file.exists()){
            try {
                reader = new BufferedReader(new FileReader(file));
            }catch (Exception e){}
        }

        txtFichero.setText(nombreFichero);

        txtNEs.setText("Número de estaciones leídas = ?");
        txtRegs.setText("Número de registros leídos = ?");
        button = findViewById(R.id.ImportarLeica);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Read();

            }
        });

    }
    private void Read(){
        EOF = false;
        List<OBS> list = new ArrayList<>();
        while (!EOF){
            switch (tipoFichero){
                case 0:
                    list = ReadLeica();
                    break;
                case 1:
                    list = ReadGeodimeter();
                    break;
                case 2:
                    list = ReadTXT();
                    break;
            }
            String tabla = analizaNE(list);
            if (!tabla.isEmpty())
                tablasTemp.add(tabla);
            list.clear();
        }
        //Ha terminado la importación
        txtNEs.setText("Número de estaciones leídas = "+countNE.toString());
        txtRegs.setText("Número de registros leídos = "+countRegs.toString());
        button.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        if (tablasTemp.size() > 0){
            fundirVueltaHorizonte(tablasTemp.get(0));
            tablasTemp.remove(0);
        }
    }
    private List<OBS> ReadGeodimeter(){
        String line = "";
        List<OBS> listObs = new ArrayList<>();
        OBS obs = new OBS();
        try {
            while ((line = reader.readLine()) != null) {
                countRegs++;
                String[] tokens = line.split("[=]");
                int key = Integer.parseInt(tokens[0]);
                switch (key){
                    case 2:
                        if (bVisual){
                            listObs.add(obs);
                            obs = new OBS();
                        }
                        obs.setNe(tokens[1]);
                        ne = obs.getNe();
                        bVisual = false;
                        if (listObs.size() >0)
                            return listObs;
                        break;
                    case 3:
                        obs.setI(tokens[1]);
                        i = obs.getI();
                        break;
                    //Bloque de medición
                    case 4:
                        //cod = Integer.parseInt(tokens[1]);
                        break;
                    case 5:
                        if (bVisual){
                            listObs.add(obs);
                            obs = new OBS();
                            obs.setNe(ne);
                            obs.setI(i);
                        }
                        obs.setNv(tokens[1]);
                        bVisual = true;
                        break;
                    case 6:
                        obs.setM(tokens[1]);
                        break;
                    case 7:
                        obs.setH(tokens[1]);
                        break;
                    case 8:
                        obs.setV(tokens[1]);
                        break;
                    case 9:
                        obs.setD(tokens[1]);
                        break;
                    default:
                        break;
                }
            }
        } catch (IOException e1) {}
        EOF = true;
        if (bVisual){
            listObs.add(obs);
        }
        return listObs;
    }
    private List<OBS> ReadLeica(){
        String line = "";
        List<OBS> listObs = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                countRegs++;
                line = line.substring(1);
                String[] tokens = line.split("[, ]");
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                double h = -999;
                double v = -99;
                double d = -99;
                int nv = -99;//puede ser el número de estación o visado (ne o nv)
                int valueCod41 = -99;
                for(String token:tokens){
                    int key = Integer.parseInt(token.substring(0,2));
                    int value = Integer.parseInt(token.substring(7));
                    switch (key){
                        case 11:
                            nv = value;
                            break;
                        case 21:
                            h = value/100000.0;
                            break;
                        case 22:
                            v = value/100000.0;
                            break;
                        case 31:
                            d = value/1000.0;
                            break;
                        case 41:
                            valueCod41 = value;
                            break;
                        case 42:
                            if (valueCod41 == 1){
                                ne = value;
                                if (listObs.size() >0){
                                    return listObs;
                                }
                            }else{
                                m = value/1000.0;
                            }
                            break;
                        case 43:
                            if (valueCod41 == 1){
                                i = value/1000.0;
                            }
                            break;
                        case 44:
                            if (valueCod41 == 1){
                                m = value/1000.0;
                            }
                            break;
                        case 84:
                            x = value/1000.0;
                            break;
                        case 85:
                            y = value/1000.0;
                            break;
                        case 86:
                            z = value/1000.0;
                            break;
                        case 87:
                            m = value/1000.0;
                            break;
                        case 88:
                            i = value/1000.0;
                            break;
                        default:
                            break;
                    }
                }
                if (h == -999){
                    ne = nv;
                    if (listObs.size() >0){
                        return listObs;
                    }
                } else {
                    OBS obs = new OBS();
                    obs.setNe(ne);
                    obs.setNv(nv);
                    obs.setH(h);
                    obs.setV(v);
                    obs.setD(d);
                    obs.setM(m);
                    obs.setI(i);
                    listObs.add(obs);
                }
            }
        }catch (Exception e){}
        EOF = true;
        return listObs;
    }
    private List<OBS> ReadTXT(){
        String line = "";
        int NE = 0;
        double I = 0.0;
        List<OBS> listObs = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                countRegs++;
                line = line.trim();
                String[] tokens = line.split(";|,| |\t");
                int i = 0;
                OBS obs = new OBS();
                for (String token:tokens){
                    token = token.trim();
                    if(token.isEmpty()) continue;
                    switch (i){
                        case 0:
                            obs.setNe(token);
                            break;
                        case 1:
                            obs.setNv(token);
                            break;
                        case 2:
                            obs.setH(token);
                            break;
                        case 3:
                            obs.setV(token);
                            break;
                        case 4:
                            obs.setD(token);
                            break;
                        case 5:
                            obs.setM(token);
                            break;
                        case 6:
                            obs.setI(token);
                            break;
                    }
                    i++;
                }
                if (obs.getNe() > 0) {
                    if (NE != obs.getNe() || I != obs.getI()){
                        countNE++;
                    }
                    listObs.add(obs);
                    I = obs.getI();
                    NE = obs.getNe();

                }
            }
        } catch (IOException e1) {}
        EOF = true;
        return listObs;
    }
    private static final int REQUEST_FUNDIR_VUELTA = 100;

    private String analizaNE(List<OBS> listObs) {
        if (listObs.size() == 0)
            return "";
        countNE++;
        String tablaTemp = "OBS_"+listObs.get(0).getNEtoString()+
                "_" + Util.dameFecha()+Util.dameHora();
        topcal.crearTabla(tablaTemp);
        topcal.insertOBS(listObs,tablaTemp);

        String sql1 = "SELECT * FROM OBS WHERE (NE,NV) IN (SELECT NE,NV FROM "+tablaTemp+") ORDER By NV,V";
        String sql2 = "SELECT * FROM "+tablaTemp+" WHERE (NE,NV) IN (SELECT NE,NV FROM OBS) ORDER By NV,V";
        List<OBS>list1 = topcal.getOBS(sql1);
        if (list1.size() == 0){
            //No hay visuales comunes
            topcal.insertOBS(listObs);
            //Borramos la tabla auxiliar
            topcal.borrarTabla(tablaTemp);
        }else{
            //Se ha vuelto ha estacionar en una estación existente
            return tablaTemp;
        }
        return "";
    }
    private void fundirVueltaHorizonte(String tabla){
        Intent intent = new Intent(MainActivity.yo, FundirVueltaDeHorizonteActivity.class);
        intent.putExtra("TABLA",tabla);
        startActivityForResult(intent,REQUEST_FUNDIR_VUELTA);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_FUNDIR_VUELTA:
                if (tablasTemp.size() > 0){
                    fundirVueltaHorizonte(tablasTemp.get(0));
                    tablasTemp.remove(0);
                }
                break;
        }
    }
}