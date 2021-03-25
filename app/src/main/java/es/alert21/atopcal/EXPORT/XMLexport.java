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
import es.alert21.atopcal.Util;

public class XMLexport {
    private OutputStreamWriter fout = null;
    StringBuilder sb = new StringBuilder();
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
                sb.setLength(0);
                sql = "SELECT * FROM OBS WHERE NE >="+ min.toString()+" AND NE <="+ max.toString() +" Order by NE,NV,id,raw";
                ArrayList<OBS> listOBS = topcal.getOBS(sql);
                nombreFicheroSalida = "OBS-"+min.toString()+"-"+max.toString()+".xml";
                sb.append(cab);
                sb.append("<observaciones>\n");
                for(OBS obs:listOBS){
                    sb.append(obs.toXML());
                }
                sb.append("</observaciones>\n");
                Util.escribeFichero(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida,sb.toString());
                topcal.insertEXPORT(nombreFicheroSalida,sb.toString());

                break;
            case "PTS":
                sb.setLength(0);
                sql = "SELECT * FROM PTS WHERE N >="+ min.toString()+" AND N <="+ max.toString() +" Order by N,id";
                ArrayList<PTS> listPTS = topcal.getPTS(sql);
                nombreFicheroSalida = "PTS-"+min.toString()+"-"+max.toString()+".xml";

                sb.append(cab);
                sb.append("<puntos>\n");
                for(PTS pts:listPTS){
                    sb.append(pts.toXML());
                }
                sb.append("</puntos>");
                Util.escribeFichero(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida,sb.toString());
                topcal.insertEXPORT(nombreFicheroSalida,sb.toString());

                break;
            default:
                break;
        }
        return nombreFicheroSalida;
    }
}
