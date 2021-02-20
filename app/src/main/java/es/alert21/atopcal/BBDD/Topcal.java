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
    public void insertOBS(List<OBS> listObs){
        for(OBS obs:listObs){
            insertOBS(obs);
        }
    }
    public void insertOBS(OBS obs){
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
            db.insert("OBS", null, cv);
        }else{
            db.update("OBS",cv,"id="+obs.getId().toString(),null);
        }
    }
    public void borrarOBS(OBS obs) {
        if (db == null) return;
        db.delete("OBS", "id=" + obs.getId().toString(), null);
    }

    public ArrayList<Integer> getNEs(){
        ArrayList<Integer> list = new ArrayList<Integer>();
        Cursor cur = db.rawQuery("SELECT DISTINCT(ne) FROM OBS Order by NE", null);
        if (cur.moveToFirst()) {
            do {
                list.add(cur.getInt(cur.getColumnIndex("NE")));
            }while(cur.moveToNext());
        }
        return list;
    }
    public ArrayList<OBS> getOBS(String sql){
        ArrayList<OBS> list = new ArrayList<OBS>();
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
        return list;
    }
    public ArrayList<PTS> getPTS(String sql){
        ArrayList<PTS> list = new ArrayList<PTS>();
        Cursor cur = db.rawQuery(sql, null);
        if (cur.moveToFirst()) {
            do {
                PTS pts = new PTS();
                pts.setId(cur.getInt(cur.getColumnIndex("id")));
                pts.setN(cur.getInt(cur.getColumnIndex("N")));
                pts.setNombre(cur.getString(cur.getColumnIndex("Nombre")));
                pts.setX(cur.getDouble(cur.getColumnIndex("X")));
                pts.setY(cur.getDouble(cur.getColumnIndex("Y")));
                pts.setZ(cur.getDouble(cur.getColumnIndex("Z")));
                pts.setDes(cur.getDouble(cur.getColumnIndex("Des")));
                list.add(pts);
            }while(cur.moveToNext());
        }
        return list;
    }

}
