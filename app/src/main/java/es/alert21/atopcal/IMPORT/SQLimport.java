package es.alert21.atopcal.IMPORT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import es.alert21.atopcal.BBDD.Topcal;

public class SQLimport {
    public SQLimport(File file, Topcal topcal){
        if (!file.exists())
            return;
        String sql="";
        try {
            FileInputStream fin = new FileInputStream(file);
            sql = convertStreamToString(fin);
            //Make sure you close all streams.
            fin.close();
        }catch (Exception e){}

        if (topcal != null)topcal.sqlExec(sql);
    }
    private String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        return sb.toString();
    }
}
