package es.alert21.atopcal.IMPORT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.OBS.OBS;

// NE  NV   -H-  -V-  -D-  -M-  -I-
public class CSVobs {
    public List<OBS> listObs = new ArrayList<>();
    public CSVobs(File file, Topcal topcal){
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
            int nv = -99;
            double i = -99;
            double m = -99;
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            double h = -99;
            double v = -99;
            double d = -99;
            int valueCod = -99;
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
    }
}
