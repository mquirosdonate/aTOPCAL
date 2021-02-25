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
/*
INSERT INTO PTS (N, X, Y, Z, Nombre) VALUES
    (4717, 446365.350,  4481956.403,  696.426, ''),
    (4883, 445989.875,  4481733.157,  698.562, ''),
    (4930, 446090.716,  4481905.983,  699.862, ''),
    (4991, 445973.450,  4481673.632,  699.097, ''),
    (6694, 446310.064,  4481887.646,  693.818, ''),
    (9001, 446053.585,  4481705.019,  698.244, ''),
    (9002, 446169.375,  4481757.418,  694.808, ''),
    (9003, 446221.839,  4481781.016,  693.312, 'E 9003'),
    (9004, 446341.515,  4481835.235,  691.061, 'E 9904');

INSERT INTO OBS (NE, NV, H, V, D, M, I) VALUES
    (4397, 2700, 13.2149, 102.3786, 72.943, 1.300, 1.550),
    (4397, 2701, 13.7490, 102.4291, 66.245, 1.300, 1.550),
    (4397, 2702, 14.4193, 102.4080, 59.586, 1.300, 1.550),
    (4397, 2703, 13.3054, 102.7775, 50.112, 1.300, 1.550),
    (4397, 2704, 12.4978, 102.9759, 52.289, 1.300, 1.550);
*/


//INSERT INTO OBS (id, NE, NV, H, V, D, M, I, raw, Aparato) VALUES (19, 6673, 6672, 233.6809, 98.2795, 272.086, 1.3, 1.577, 0, 0);
public class SQLexport {
    private OutputStreamWriter fout = null;
    File file = null;
    String line = "";
    String sqlOBSInsert = "INSERT INTO OBS (NE, NV, H, V, D, M, I) VALUES \n";
    String sqlOBSValues = "(%d, %d, %.4f, %.4f, %.3f, %.3f, %.3f)";
    String sqlPTInset = "INSERT INTO PTS (N, X, Y, Z, Nombre) VALUES \n";
    String sqlPTValues = "(%d, %.3f,  %.3f,  %.3f, '%s')";
    Topcal topcal;
    public SQLexport(Topcal topcal){
        this.topcal = topcal;
    }
    public String exportar(String tabla, Integer max, Integer min){
        String nombreFicheroSalida = "";
        String sql = "";
        switch (tabla.toUpperCase()){
            case "OBS":
                sql = "SELECT * FROM OBS WHERE NE >="+ min.toString()+" AND NE <="+ max.toString() +" Order by NE,NV,id,raw";
                ArrayList<OBS> listOBS = topcal.getOBS(sql);
                nombreFicheroSalida = "OBS-"+min.toString()+"-"+max.toString()+".sql";
                file = new File(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida);
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(file,false));
                    fout.write(sqlOBSInsert);
                    for(OBS obs:listOBS){
                        if(!line.isEmpty()) line = ",\n";
                        line += String.format(sqlOBSValues,obs.getNe(),obs.getNv(),obs.getH(),obs.getV(),obs.getD(),obs.getM(),obs.getI());
                        fout.write(line);
                    }
                    fout.write(";");
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
                nombreFicheroSalida = "PTS-"+min.toString()+"-"+max.toString()+".sql";
                file = new File(topcal.getNombreTrabajo()+"/"+nombreFicheroSalida);
                try {
                    fout = new OutputStreamWriter(new FileOutputStream(file,false));
                    fout.write(sqlPTInset);
                    for(PTS pts:listPTS){
                        if(!line.isEmpty()) line = ",\n";
                        line += String.format(sqlPTValues,pts.getN(),pts.getX(),pts.getY(),pts.getZ(),pts.getNombre());
                        fout.write(line);
                    }
                    fout.write(";");
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
