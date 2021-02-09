package es.alert21.atopcal;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;

public class Topcal {
    private String m_NombreTrabajo;
    private TopcalDB topcalDB;
    private SQLiteDatabase db;

    public Topcal(String nombreTrabajo)	{
        if(nombreTrabajo.equals(""))return;
        m_NombreTrabajo = nombreTrabajo;
        File path = Util.creaDirectorios(MainActivity.yo, m_NombreTrabajo);
        topcalDB = new TopcalDB(MainActivity.yo,path.toString()+"/Topcal.db",null,1);
        db = topcalDB.getWritableDatabase();
    }
}
