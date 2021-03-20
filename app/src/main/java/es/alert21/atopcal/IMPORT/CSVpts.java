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
                String[] tokens = line.split("\";|,| |\\t\"");
                int i = 0;
                PTS pts = new PTS();
                for (String token:tokens){
                    if(token.isEmpty()) continue;
                    switch (i){
                        case 0:
                            pts.setN(token);
                            break;
                        case 1:
                            pts.setX(token);
                            break;
                        case 2:
                            pts.setY(token);
                            break;
                        case 3:
                            pts.setZ(token);
                            break;
                        case 4:
                            pts.setDes(token);
                            break;
                        case 5:
                            pts.setNombre(token);
                            break;

                    }
                    i++;
                }
                if (pts.getN() > 0)
                    listPts.add(pts);

            }
        } catch (IOException e1) {}
    }
}
