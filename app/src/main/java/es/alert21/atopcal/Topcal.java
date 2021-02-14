package es.alert21.atopcal;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import es.alert21.atopcal.OBS.OBS;

public class Topcal {
    private String m_NombreTrabajo;
    private TopcalDB topcalDB;
    private SQLiteDatabase db;

    public Topcal(String nombreTrabajo)	{
        if(nombreTrabajo.equals(""))return;
        m_NombreTrabajo = nombreTrabajo;

        topcalDB = new TopcalDB(MainActivity.yo,m_NombreTrabajo+"/Topcal.db",null,1);
        db = topcalDB.getWritableDatabase();
    }
    public void setPRJ(String nombre,String titulo,String descripcion){
        if (db == null) return;
        ContentValues cv = new ContentValues();
        cv.put("Nombre",nombre);
        cv.put("Titulo",titulo);
        cv.put("Descripcion",descripcion);
        Cursor cur = db.rawQuery("SELECT Nombre FROM PRJ"  , null);
        if (cur.moveToFirst()) {
            // true; UPDATE
            db.update("PRJ",cv,null,null);
        } else {
            // false INSERT
            db.insert("PRJ", null, cv);
        }
    }
    public void setOBS(OBS obs){
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
        db.insert("OBS", null, cv);
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

}
