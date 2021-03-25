package es.alert21.atopcal.BBDD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PRJ.PRJ;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.DESORIENTACION.PTV_OBS;
import es.alert21.atopcal.Util;

public class Topcal {
    private String nombreTrabajo;
    private TopcalDB topcalDB;
    private SQLiteDatabase db;

    public Topcal(String nombreTrabajo)	{
        if(nombreTrabajo.equals(""))return;
        this.nombreTrabajo = nombreTrabajo;

        topcalDB = new TopcalDB(MainActivity.yo, this.nombreTrabajo +"/Topcal.db",null,1);
        db = topcalDB.getWritableDatabase();
    }
    public String getNombreTrabajo(){
        return nombreTrabajo;
    }
    public PRJ getPRJ(){
        PRJ prj = new PRJ();
        Cursor cur = db.rawQuery("SELECT * FROM PRJ WHERE id>0", null);
        if (cur.moveToFirst()) {
            prj.setId(cur.getInt(cur.getColumnIndex("id")));
            prj.setDescripcion(cur.getString(cur.getColumnIndex("Descripcion")));
            prj.setTitulo(cur.getString(cur.getColumnIndex("Titulo")));
        }
        cur.close();
        File file = new File(nombreTrabajo);
        String nombre = file.getName() ;
        prj.setNombre(nombre);
        return prj;
    }
    public void insertPRJ(PRJ prj){
        if (db == null) return;
        ContentValues cv = new ContentValues();

        cv.put("Titulo",prj.getTitulo());
        cv.put("Descripcion",prj.getDescripcion());
        if (prj.getId() == 0){
            db.insert("PRJ", null, cv);
        }else{
            db.update("PRJ",cv,"id="+prj.getId().toString(),null);
        }
    }
    public void insertHTML(String nombre,String html){
        if (db == null) return;
        ContentValues cv = new ContentValues();
        cv.put("Date", Util.dameFecha());
        cv.put("FileName",nombre);
        cv.put("HTML",html);
        db.insert("HTML", null, cv);
    }
    public void insertEXPORT(String nombre,String html){
        if (db == null) return;
        ContentValues cv = new ContentValues();
        cv.put("Date", Util.dameFecha());
        cv.put("FileName",nombre);
        cv.put("EXPORT",html);
        db.insert("EXPORT", null, cv);
    }


    public Integer getMinEstacion(){
        if (db == null) return -99;
        Cursor cur = db.rawQuery("SELECT MIN(NE) FROM OBS", null);
        if (cur.moveToFirst()) {
            Integer value = cur.getInt(0);
            cur.close();
            return value;
        }
        cur.close();
        return -99;
    }
    public Integer getMaxEstacion(){
        if (db == null) return -99;
        Cursor cur = db.rawQuery("SELECT MAX(NE) FROM OBS", null);
        if (cur.moveToFirst()) {
            Integer value = cur.getInt(0);
            cur.close();
            return value;
        }
        cur.close();
        return -99;
    }
    public Integer getMinPunto(){
        if (db == null) return -99;
        Cursor cur = db.rawQuery("SELECT MIN(N) FROM PTS", null);
        if (cur.moveToFirst()) {
            Integer value = cur.getInt(0);
            cur.close();
            return value;
        }
        cur.close();
        return -99;
    }
    public Integer getMaxPunto(){
        if (db == null) return -99;
        Cursor cur = db.rawQuery("SELECT MAX(N) FROM PTS", null);
        if (cur.moveToFirst()) {
            Integer value = cur.getInt(0);
            cur.close();
            return value;
        }
        cur.close();
        return -99;
    }

    public void insertOBS(List<OBS> listObs){
        insertOBS( listObs,"OBS");
    }
    public void insertOBS(List<OBS> listObs,String tabla){
        for(OBS obs:listObs){
            insertOBS(obs,tabla);
        }
    }
    public void insertOBS(OBS obs){
        insertOBS(obs,"OBS");
    }
    public void insertOBS(OBS obs,String tabla){
        if (db == null) return;
        ContentValues cv = new ContentValues();
        cv.put("NE",obs.getNe());
        cv.put("NV",obs.getNv());
        cv.put("H",obs.getH());
        cv.put("V",obs.getV());
        cv.put("D",obs.getD());
        cv.put("M",obs.getM());
        cv.put("I",obs.getI());
        cv.put("raw",obs.getRaw());
        cv.put("Aparato",obs.getAparato());
        if (obs.getId() == 0){
            db.insert(tabla, null, cv);
        }else{
            db.update(tabla,cv,"id="+obs.getIDtoString(),null);
        }
    }

    public void insertPTS(List<PTS> listPts){
        for(PTS pts:listPts){
            insertPTS(pts);
        }
    }
    public void insertPTS(PTS pts){
        if (db == null) return;
        ContentValues cv = new ContentValues();
        cv.put("N",pts.getN());
        cv.put("Nombre",pts.getNombre());
        cv.put("X",pts.getX());
        cv.put("Y",pts.getY());
        cv.put("Z",pts.getZ());
        cv.put("Des",pts.getDes());

        if (pts.getId() == 0){
            db.insert("PTS", null, cv);
        }else{
            db.update("PTS",cv,"id="+pts.getIDtoString(),null);
        }
    }
    public void borrarOBS(OBS obs) {
        if (db == null) return;
        db.delete("OBS", "id=" + obs.getIDtoString(), null);
    }
    public void borrarPTS(PTS pts) {
        if (db == null) return;
        db.delete("PTS", "id=" + pts.getIDtoString(), null);
    }

    public ArrayList<Integer> getNEs(String sql){
        ArrayList<Integer> list = new ArrayList<>();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                list.add(cur.getInt(cur.getColumnIndex("NE")));
            }while(cur.moveToNext());
        }
        cur.close();
        return list;
    }
    public ArrayList<Integer> getNVs(String sqlView,String sqlSelect,String sqlDrop){
        ArrayList<Integer> list = new ArrayList<>();
        sqlExec(sqlDrop);
        sqlExec(sqlView);
        Cursor cur = db.rawQuery(sqlSelect, null);
        if (cur.moveToFirst()) {
            do {
                list.add(cur.getInt(cur.getColumnIndex("NV")));
            }while(cur.moveToNext());
        }
        cur.close();
        sqlExec(sqlDrop);
        return list;
    }
    public ArrayList<OBS> getOBS(String sql){
        ArrayList<OBS> list = new ArrayList<>();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                OBS obs = new OBS();
                obs.setId(cur.getInt(cur.getColumnIndex("id")));
                obs.setNe(cur.getInt(cur.getColumnIndex("NE")));
                obs.setNv(cur.getInt(cur.getColumnIndex("NV")));
                obs.setH(cur.getDouble(cur.getColumnIndex("H")));
                obs.setV(cur.getDouble(cur.getColumnIndex("V")));
                obs.setD(cur.getDouble(cur.getColumnIndex("D")));
                obs.setM(cur.getDouble(cur.getColumnIndex("M")));
                obs.setI(cur.getDouble(cur.getColumnIndex("I")));
                obs.setRaw(cur.getInt(cur.getColumnIndex("raw")));
                obs.setAparato(cur.getInt(cur.getColumnIndex("Aparato")));
                list.add(obs);
            }while(cur.moveToNext());
        }
        cur.close();
        return list;
    }
    public ArrayList<PTS> getPTS(String sql){
        ArrayList<PTS> list = new ArrayList<>();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                PTS pts = new PTS();
                try{
                    pts.setId(cur.getInt(cur.getColumnIndex("id")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setN(cur.getInt(cur.getColumnIndex("N")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setNombre(cur.getString(cur.getColumnIndex("Nombre")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setX(cur.getDouble(cur.getColumnIndex("X")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setY(cur.getDouble(cur.getColumnIndex("Y")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setZ(cur.getDouble(cur.getColumnIndex("Z")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setDes(cur.getDouble(cur.getColumnIndex("DES")));
                }catch (Exception e) {
                    e.printStackTrace();
                }

                list.add(pts);
            }while(cur.moveToNext());
        }
        cur.close();
        return list;
    }
    public PTS getPTS(Integer n){
        PTS pts = new PTS();
        pts.setN(n);
        String sql = "SELECT * FROM PTS WHERE N="+n.toString();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                try{
                    pts.setId(cur.getInt(cur.getColumnIndex("id")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setN(cur.getInt(cur.getColumnIndex("N")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setNombre(cur.getString(cur.getColumnIndex("Nombre")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setX(cur.getDouble(cur.getColumnIndex("X")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setY(cur.getDouble(cur.getColumnIndex("Y")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setZ(cur.getDouble(cur.getColumnIndex("Z")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
                try{
                    pts.setDes(cur.getDouble(cur.getColumnIndex("DES")));
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }while(cur.moveToNext());
        }
        cur.close();
        return pts;
    }

    public ArrayList<PTV_OBS> getPTV_OBS(String sql){
        ArrayList<PTV_OBS> list = new ArrayList<>();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                PTV_OBS ptv_obs = new PTV_OBS();
                ptv_obs.vis.setN(cur.getInt(cur.getColumnIndex("N")));
                ptv_obs.vis.setNombre(cur.getString(cur.getColumnIndex("Nombre")));
                ptv_obs.vis.setX(cur.getDouble(cur.getColumnIndex("X")));
                ptv_obs.vis.setY(cur.getDouble(cur.getColumnIndex("Y")));
                ptv_obs.vis.setZ(cur.getDouble(cur.getColumnIndex("Z")));
                ptv_obs.vis.setDes(cur.getDouble(cur.getColumnIndex("DES")));

                ptv_obs.obs.setNe(cur.getInt(cur.getColumnIndex("NE")));
                ptv_obs.obs.setNv(cur.getInt(cur.getColumnIndex("NV")));
                ptv_obs.obs.setH(cur.getDouble(cur.getColumnIndex("H")));
                ptv_obs.obs.setV(cur.getDouble(cur.getColumnIndex("V")));
                ptv_obs.obs.setD(cur.getDouble(cur.getColumnIndex("D")));
                ptv_obs.obs.setM(cur.getDouble(cur.getColumnIndex("M")));
                ptv_obs.obs.setI(cur.getDouble(cur.getColumnIndex("I")));
                ptv_obs.obs.setRaw(cur.getInt(cur.getColumnIndex("raw")));
                ptv_obs.obs.setAparato(cur.getInt(cur.getColumnIndex("Aparato")));
                list.add(ptv_obs);
            }while(cur.moveToNext());
        }
        cur.close();
        return list;
    }
    public boolean sqlExec(String sql){
        boolean ret = false;
        if (db == null) return ret;
        try {
            db.execSQL(sql);
            ret = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public boolean crearTabla(String tabla){
        String CREATE_TABLE_OBS = "CREATE TABLE " + tabla +
         " ( id INTEGER NOT NULL CONSTRAINT obs_pk PRIMARY KEY AUTOINCREMENT,NE INTEGER,NV INTEGER ," +
                "H DOUBLE,V DOUBLE,D DOUBLE,M DOUBLE,I DOUBLE,raw INTEGER,Aparato INTEGER)";

        return sqlExec(CREATE_TABLE_OBS);
    }
    public boolean borrarTabla(String tabla){
        return sqlExec("DROP TABLE " + tabla);
    }
}
