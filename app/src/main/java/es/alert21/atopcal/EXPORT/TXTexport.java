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

public class TXTexport {
    StringBuilder sb = new StringBuilder();
    private OutputStreamWriter fout = null;
    Topcal topcal;
    public TXTexport(Topcal topcal){
        this.topcal = topcal;
    }
    public String exportar(String tabla,Integer max,Integer min) {
        String nombreFicheroSalida = "";
        String sql = "";
        switch (tabla.toUpperCase()){
            case "OBS":
                sb.setLength(0);
                sql = "SELECT * FROM OBS WHERE NE >="+ min.toString()+" AND NE <="+ max.toString() +" Order by NE,NV,id,raw";
                ArrayList<OBS> listOBS = topcal.getOBS(sql);
                nombreFicheroSalida = "OBS-"+min.toString()+"-"+max.toString()+".txt";
                for(OBS obs:listOBS){
                    sb.append(obs.toString()+"\n");
                }
                Util.escribeFichero(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida,sb.toString());
                topcal.insertEXPORT(nombreFicheroSalida,sb.toString());
                break;
            case "PTS":
                sb.setLength(0);
                sql = "SELECT * FROM PTS WHERE N >="+ min.toString()+" AND N <="+ max.toString() +" Order by N,id";
                ArrayList<PTS> listPTS = topcal.getPTS(sql);
                nombreFicheroSalida = "PTS-"+min.toString()+"-"+max.toString()+".txt";
                for(PTS pts:listPTS){
                    sb.append(pts.toString()+"\n");
                }
                Util.escribeFichero(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida,sb.toString());
                topcal.insertEXPORT(nombreFicheroSalida,sb.toString());
            default:
                break;
        }


        return nombreFicheroSalida;
    }
}
