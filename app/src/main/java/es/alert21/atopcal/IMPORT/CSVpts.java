package es.alert21.atopcal.IMPORT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;

// NUMERO DE PUNTO --X--  --Y--  --Z--
public class CSVpts {
    public List<PTS> listPts = new ArrayList<>();
    public CSVpts(File file, Topcal topcal){
        if (!file.exists())
            return;
        Read(file);
        if (topcal != null)topcal.insertPTS(listPts);
    }
    private void Read(File file){
        String line = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("[, ]");
                if (tokens.length > 3){
                    PTS pts = new PTS();
                    pts.setN(tokens[0]);
                    pts.setX(tokens[1]);
                    pts.setY(tokens[2]);
                    pts.setZ(tokens[3]);
                    listPts.add(pts);
                }
            }
        } catch (IOException e1) {}
    }
}
