package es.alert21.atopcal.IMPORT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class ImportObsActivity extends AppCompatActivity {
    Topcal topcal;
    static boolean EOF = false;
    int tipoFichero;
    BufferedReader reader = null;
    public static List<OBS> listObs = new ArrayList<>();
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
        getSupportActionBar().setTitle("IMPORTAR DE LEICA");
        String prj = Util.cargaConfiguracion(MainActivity.yo,"Nombre Proyecto","");
        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);

        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);

        textView = findViewById(R.id.textView16);
        txtRegs = findViewById(R.id.textView14);
        txtNEs = findViewById(R.id.textView13);
        txtFichero = findViewById(R.id.textView17);
        topcal = new Topcal(path.toString());

        Bundle b = this.getIntent().getExtras();
        String nombreFichero = b.getString("FILE","");
        tipoFichero = b.getInt("TIPO",0);
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

        txtFichero.setText(nombreFichero);
        file = new File(nombreFichero);
        txtNEs.setText("Número de estaciones leídas = ?");
        txtRegs.setText("Número de registros leídos = ?");
        button = findViewById(R.id.ImportarLeica);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topcal == null)
                    return;
                if (!file.exists())
                    return;
                try {
                    reader = new BufferedReader(new FileReader(file));
                }catch (Exception e){}
                switch (tipoFichero){
                    case 0:
                        ReadLeica();
                        break;
                    case 1:
                        ReadGeodimeter();
                        break;
                }

            }
        });

    }
    private void ReadGeodimeter(){
        String line = "";
        try {
            double h = -99;
            double v = -99;
            double d = -99;
            while ((line = reader.readLine()) != null) {
                countRegs++;
                String[] tokens = line.split("[=]");
                int key = Integer.parseInt(tokens[0]);
                switch (key){
                    case 2:
                        if (bVisual){
                            Llena(ne,nv,cod,h,v,d,m,i);
                        }
                        ne = Integer.parseInt(tokens[1]);
                        bVisual = false;
                        if (listObs.size() >0)
                            analizaNE(); //Empieza una estación
                        break;
                    case 3:
                        i = Double.parseDouble(tokens[1]);
                        break;
                    //Bloque de medición
                    case 4:
                        cod = Integer.parseInt(tokens[1]);
                        break;
                    case 5:
                        if (bVisual){
                            Llena(ne,nv,cod,h,v,d,m,i);
                        }
                        nv = Integer.parseInt(tokens[1]);
                        bVisual = true;
                        break;
                    case 6:
                        m = Double.parseDouble(tokens[1]);
                        break;
                    case 7:
                        h = Double.parseDouble(tokens[1]);
                        break;
                    case 8:
                        v = Double.parseDouble(tokens[1]);
                        break;
                    case 9:
                        d = Double.parseDouble(tokens[1]);
                        break;
                    default:
                        break;
                }
            }
            EOF = true;
            if (bVisual){
                Llena(ne,nv,cod,h,v,d,m,i);
                if (listObs.size() >0)
                    analizaNE(); //Empieza una estación
            }
        } catch (IOException e1) {}
    }
    private void ReadLeica(){
        String line = "";
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
                                if (listObs.size() >0)
                                    analizaNE(); //Empieza una estación
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
                    if (listObs.size() >0)
                        analizaNE(); //Empieza una estación
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
        if (listObs.size() >0)
            analizaNE();
    }
    private void ReadTXT(){
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[, ]");
                if (tokens.length == 7){
                    OBS obs = new OBS();
                    obs.setNe(tokens[0]);
                    obs.setNv(tokens[1]);
                    obs.setH(tokens[2]);
                    obs.setV(tokens[3]);
                    obs.setD(tokens[4]);
                    obs.setM(tokens[5]);
                    obs.setI(tokens[6]);
                    listObs.add(obs);
                }
            }
        } catch (IOException e1) {}
        EOF = true;
        if (listObs.size() >0)
            analizaNE();
    }
    private void analizaNE(){
        countNE++;
        String tablaTemp = "OBS_"+listObs.get(0).getNe().toString()+
                "_" + Util.dameFecha()+Util.dameHora();
        topcal.CrearTabla(tablaTemp);
        topcal.insertOBS(listObs,tablaTemp);
        //Aquí hay que analizar si NE estaba en la BBDD
        /* OBSERVACIONES COMUNES ORIGINALES DE LA TABLA OBS
        SELECT * FROM OBS WHERE (NE,NV) IN (SELECT NE,NV FROM TEMP) ORDER By NV,V
        */
        /* OBSERVACIONES COMUNES DE LA TABLA TEMPORAL PARA CALCULAR LA DESORIENTACIÓN
        SELECT * FROM TEMP WHERE (NE,NV) IN (SELECT NE,NV FROM OBS) ORDER BY NV ,V
        */
        String sql1 = "SELECT * FROM OBS WHERE (NE,NV) IN (SELECT NE,NV FROM "+tablaTemp+") ORDER By NV,V";
        String sql2 = "SELECT * FROM "+tablaTemp+" WHERE (NE,NV) IN (SELECT NE,NV FROM OBS) ORDER By NV,V";
        List<OBS>list1 = topcal.getOBS(sql1);
        List<OBS>list2 = topcal.getOBS(sql2);
        topcal.insertOBS(listObs);
        listObs.clear();
        if (!EOF){
            switch (tipoFichero){
                case 0:
                    ReadLeica();
                    break;
                case 1:
                    ReadGeodimeter();
                    break;
                case 2:
                    ReadTXT();
                    break;
            }
        }else{
            //Ha terminado la importación
            txtNEs.setText("Número de estaciones leídas = "+countNE.toString());
            txtRegs.setText("Número de registros leídos = "+countRegs.toString());
            button.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void Llena(int ne ,int nv, int cod , double h , double v , double d , double m , double i  ){
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