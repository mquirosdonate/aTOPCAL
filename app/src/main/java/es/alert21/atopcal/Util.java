package es.alert21.atopcal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Environment;
import androidx.preference.PreferenceManager;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import es.alert21.atopcal.BBDD.Topcal;

/**
 * Created by Asus on 26/01/2016.
 */


public class Util {
    public static Topcal getTopcal(){
        Topcal topcal = null;
        String prj = Util.cargaConfiguracion(MainActivity.yo,"Nombre Proyecto","");
        if (prj.isEmpty())
            return topcal;

        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);
        topcal = new Topcal(path.toString());

        return  topcal;
    }

    public static void readCSV(File file) {
        String line ;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public static String cargaConfiguracion(Context context,String key,String defValue){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, defValue);
    }
    public static void guardaConfiguracion(Context context,String key, String value) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public static String quitaExtension(String fileName){
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        return fileName;
    }
    public static String HTMLBody(String m){
        return m.substring(m.indexOf("<body>")+6,m.indexOf("</body"));
    }
    public static String getVersionName(Context context) {
        String myVersionName = "not available"; // initialize String

        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return myVersionName;
    }


    @SuppressLint("DefaultLocale")
    public static String dameDateTimeUTC(long t){
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar calendario ;
        calendario = Calendar.getInstance(timeZone);
        calendario.setTimeInMillis(t);
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        int hora =calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        int segundos = calendario.get(Calendar.SECOND);

        return  String.format("<time>%04d-%02d-%02dT%02d:%02d:%02dZ</time>",
                year,month+1,day, hora,minutos,segundos);

    }

    public static String padRight(String s, Integer n) {
        if (n < 0)return s;
        return String.format("%1$-" + n.toString() + "s", s);
    }

    public static String padLeft(String s, Integer n) {
        if (n < 0) return s;
        return String.format("%1$" + n.toString() + "s", s);
    }

    public static boolean sdCardState() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    @SuppressLint("DefaultLocale")
    public static String dameExt(String s) {
        int i;

        for( i = s.length() -1 ; i >= 0 ; i--){
            char c = s.charAt(i);
            if (c == '.')
                break;
        }

        if (i >= 0 && i < s.length() -1)
            return s.substring(i + 1).toLowerCase();

        return "";
    }

    public static Date dameTime(String h1){
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(h1);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return date1;
    }

    @SuppressLint("DefaultLocale")
    public static String dameDateTime(Location loc){
        calendario = Calendar.getInstance();
        calendario.setTimeInMillis(loc.getTime());
        int year = calendario.get(Calendar.YEAR);
        int month = calendario.get(Calendar.MONTH);
        int day = calendario.get(Calendar.DAY_OF_MONTH);

        int hora =calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        int segundos = calendario.get(Calendar.SECOND);

        return  String.format("<time>%04d-%02d-%02dT%02d:%02d:%02dZ</time>",
                year,month+1,day, hora,minutos,segundos);

    }

    @SuppressLint("DefaultLocale")
    public static String dameTime(Location loc) {
        calendario = Calendar.getInstance();
        calendario.setTimeInMillis(loc.getTime());
        int hora =calendario.get(Calendar.HOUR_OF_DAY);
        int minutos = calendario.get(Calendar.MINUTE);
        int segundos = calendario.get(Calendar.SECOND);
        return  String.format("%02d%02d%02d",hora,minutos,segundos);
    }

    public static String dameTime2(Location loc){
        Date date = new Date(loc.getTime());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);

    }

    public static String doubleATexto(double v, Integer ndec) {
        String format = "%1$." + ndec.toString() + "f";
        String s = String.format(format, v);
        return s.replace(',', '.');
    }

    public static String doubleATexto(double v, Integer nEnt, Integer ndec){
        int n = nEnt + ndec + 1;
        String format = "%1$0" + Integer.toString(n) + "." + ndec.toString() + "f";
        String s = String.format(format, v);
        return s.replace(',','.');
    }

    @SuppressLint("DefaultLocale")
    public static String tiempoATexto(Long t) {
        double aux = t / 3600.0;
        int h = (int)aux;
        aux = (aux - h) * 60.0;
        int m = (int)aux;
        aux = (aux - m) * 60.0;
        int s = (int)aux;
        return String.format("%1$02d:%2$02d:%3$02d", h, m, s);
    }
    private static Calendar calendario;
    @SuppressLint("DefaultLocale")
    public static String dameFecha() {
        calendario = Calendar.getInstance();
        int year,month,day;
        year = calendario.get(Calendar.YEAR);
        month = calendario.get(Calendar.MONTH)+1;
        day = calendario.get(Calendar.DAY_OF_MONTH);
        return  String.format("%04d%02d%02d", year,month,day);
    }

    @SuppressLint("DefaultLocale")
    public static String dameHora() {
        calendario = Calendar.getInstance();
        int hora, minutos, segundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        Integer x = calendario.get(Calendar.MILLISECOND);
        return  String.format("%1$02d%2$02d%3$02d%4$03d", hora,minutos,segundos,x);
    }
    @SuppressLint("DefaultLocale")
    public static String dameHora2() {
        calendario = Calendar.getInstance();
        int hora, minutos, segundos;
        hora =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        return  String.format("%1$02d:%2$02d:%3$02d", hora,minutos,segundos);
    }

    public static void escribeFichero(String fileName,String chicha){
        File file = new File(fileName);
        OutputStreamWriter fout = null;
        try {
            fout = new OutputStreamWriter(new FileOutputStream(file, false));
            fout.write(chicha.toString());
            fout.flush();
            fout.close();
            //Toast.makeText(getApplicationContext(), "Se ha creado el fichero: "+nombreFicheroSalida, Toast.LENGTH_LONG).show();
        } catch(FileNotFoundException e){e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
    }
    public static File creaDirectorios(Context context, String nivel1) {
        File ruta = null;
        try {
            //Ruta de la sdCard
            File sdCard = Environment.getExternalStorageDirectory();
            //Ruta creada para la aplicacion
            String nameApp = (String)context.getString(R.string.app_name);
            File sdDir = new File(sdCard.getAbsolutePath() + "/" + nameApp);
            if(!sdDir.exists()) {
                sdDir.mkdir();
            }
            //Ruta para nivel1
            File n1 = new File(sdDir.getAbsolutePath() + "/" + nivel1 );
            if(!n1.exists())
                n1.mkdir();
            ruta = n1;
        }catch(Exception e){
            Toast.makeText(context, "Error al crear el fichero", Toast.LENGTH_LONG).show();
        }
        return ruta;
    }

    public static File creaDirectorios(Context context, String nivel1, String nivel2){
        File ruta = null;
        try {
            //Ruta de la sdCard
            File sdCard = Environment.getExternalStorageDirectory();
            //Ruta creada para la aplicacion
            String nameApp = (String)context.getString(R.string.app_name);
            File sdDir = new File(sdCard.getAbsolutePath() + "/" + nameApp);
            if(!sdDir.exists())
                sdDir.mkdir();
            //Ruta para los tracks
            File n1 = new File(sdDir.getAbsolutePath() + "/" + nivel1 );
            if(!n1.exists())
                n1.mkdir();
            //Ruta para los kml,gpx,etc..
            File n2 = new File(n1.getAbsolutePath() + "/" + nivel2 );
            if(!n2.exists())
                n2.mkdir();
            ruta = n2;
        }catch(Exception e){
            Toast.makeText(context, "Error al crear el fichero", Toast.LENGTH_LONG).show();
        }
        return ruta;
    }

    public static File creaDirectorios(Context context, String nivel1, String nivel2, String nivel3){
        File ruta = null;
        try {
            //Ruta de la sdCard
            File sdCard = Environment.getExternalStorageDirectory();
            //Ruta creada para la aplicacion
            String nameApp = context.getString(R.string.app_name);
            File sdDir = new File(sdCard.getAbsolutePath() + "/" + nameApp);
            if(!sdDir.exists()) sdDir.mkdir();
            //Ruta para los tracks
            File n1 = new File(sdDir.getAbsolutePath() + "/" + nivel1 );
            if(!n1.exists()) n1.mkdir();
            //Ruta para los kml,gpx,etc..
            File n2 = new File(n1.getAbsolutePath() + "/" + nivel2 );
            if(!n2.exists()) n2.mkdir();
            //Ruta para los tracks de una fcha
            File n3 = new File(n2.getAbsolutePath() + "/" + nivel3);
            if(!n3.exists()) n3.mkdir();
            ruta = n3;
            //Toast.makeText(m_context,trackFechaDir + m_fichero + "." + nivel2 ,Toast.LENGTH_LONG).show();
        }catch(Exception e){
            Toast.makeText(context, "Error al crear el fichero", Toast.LENGTH_LONG).show();
        }
        return ruta;
    }

}
