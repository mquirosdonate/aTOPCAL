package es.alert21.atopcal.OBS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;

//'ASI ERA EN TOPCAL MSDOS
//'subrutina de transferencia de datos de gre4 a topcal
//'code=1 para cambio de estacion  41=1 42=ne 43=i 44=m
//'code=0 para cambiar solo la mira 41=0 42=m
//'los bloques de medicion tendran 11=np 21=h 22=v 31=d 90=ncodigo
//'el resto de los codigos se ignora
//'-------------------------------------------------------------------

public class Leica extends OBS{
    public List<OBS> listObs = new ArrayList<>();
    Leica(File file, Topcal topcal){
        if (!file.exists())
            return;
        Read(file);
        if (topcal != null)topcal.insertOBS(listObs);
    }
    private void Read(File file){
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int ne = -99;
            double i = -99;
            double m = -99;
            while ((line = reader.readLine()) != null) {
                line = line.substring(1);
                String[] tokens = line.split("[, ]");
                double x = 0.0;
                double y = 0.0;
                double z = 0.0;
                double h = -99;
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
                if (h == -99){
                    ne = nv;
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
        } catch (IOException e1) {}
    }
}
