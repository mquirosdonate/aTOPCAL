package es.alert21.atopcal.IMPORT;

import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.R;

public class Geodimeter {
    Topcal topcal = null;
    public List<OBS> listObs = new ArrayList<>();
    public Geodimeter(File file, Topcal topcal){
        if (!file.exists())
            return;
        if (topcal == null)
            return;
        this.topcal = topcal;
        Read(file);
    }
    private void Read(File file){
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int ne = -99;
            int nv = -99;
            int cod = -99;
            double h = -99;
            double v = -99;
            double d = -99;
            double m = -99;
            double i = -99;
            boolean bVisual = false;
            while ((line = reader.readLine()) != null) {
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
            if (bVisual){
                Llena(ne,nv,cod,h,v,d,m,i);
                if (listObs.size() >0)
                    analizaNE(); //Empieza una estación
            }
        } catch (IOException e1) {}
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
    private void analizaNE(){
        if (listObs.size() >0 ){
            //Aquí hay que analizar si NE estaba en la BBDD
            topcal.insertOBS(listObs);
            listObs.clear();
        }
    }

}
