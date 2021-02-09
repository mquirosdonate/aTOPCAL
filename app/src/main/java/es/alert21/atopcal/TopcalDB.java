package es.alert21.atopcal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TopcalDB extends SQLiteOpenHelper {
    private String CREATE_TABLE_OBS = "CREATE TABLE OBS (NE INTEGER,NV INTEGER ," +
            "H DOUBLE,V DOUBLE,D DOUBLE,M DOUBLE,I DOUBLE,raw INTEGER,Aparato INTEGER)";
    public TopcalDB(Context contexto, String nombre,SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(CREATE_TABLE_OBS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla


        //Se crea la nueva versión de la tabla
        //db.execSQL(CREATE_TABLE_OBS);
    }
}
