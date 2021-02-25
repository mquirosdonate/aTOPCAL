package es.alert21.atopcal.EXPORT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;

public class XMLexport {
    private OutputStreamWriter fout = null;
    File file = null;
    Topcal topcal;
    String cab = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";

    public XMLexport(Topcal topcal){
        this.topcal = topcal;
    }
    public String exportar(String tabla, Integer max, Integer min) {
        String nombreFicheroSalida = "";
        String sql = "";
        switch (tabla.toUpperCase()){
            case "OBS":
                sql = "SELECT * FROM OBS WHERE NE >="+ min.toString()+" AND NE <="+ max.toString() +" Order by NE,NV,id,raw";
                ArrayList<OBS> listOBS = topcal.getOBS(sql);
                nombreFicheroSalida = "OBS-"+min.toString()+"-"+max.toString()+".xml";
                file = new File(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida);
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(file,false));
                    fout.write(cab);
                    fout.write("<observaciones>\n");
                    for(OBS obs:listOBS){
                        fout.write(obs.toXML());
                    }
                    fout.write("</observaciones>\n");
                    fout.flush();
                    fout.close();
                } catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "PTS":
                sql = "SELECT * FROM PTS WHERE N >="+ min.toString()+" AND N <="+ max.toString() +" Order by N,id";
                ArrayList<PTS> listPTS = topcal.getPTS(sql);
                nombreFicheroSalida = "PTS-"+min.toString()+"-"+max.toString()+".xml";
                file = new File(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida);
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(file,false));
                    fout.write(cab);
                    fout.write("<puntos>\n");
                    for(PTS pts:listPTS){
                        fout.write(pts.toXML());
                    }
                    fout.write("</puntos>");
                    fout.flush();
                    fout.close();
                } catch(FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        return nombreFicheroSalida;
    }
}
