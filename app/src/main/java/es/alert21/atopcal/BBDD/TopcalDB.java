package es.alert21.atopcal.BBDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TopcalDB extends SQLiteOpenHelper {
    private String CREATE_TABLE_PRJ = "CREATE TABLE PRJ (id INTEGER NOT NULL CONSTRAINT prj_pk PRIMARY KEY AUTOINCREMENT,Titulo TEXT ,Descripcion TEXT)";
    private String CREATE_TABLE_OBS = "CREATE TABLE OBS ( id INTEGER NOT NULL CONSTRAINT obs_pk PRIMARY KEY AUTOINCREMENT,NE INTEGER,NV INTEGER ," +
            "H DOUBLE,V DOUBLE,D DOUBLE,M DOUBLE,I DOUBLE,raw INTEGER,Aparato INTEGER)";
    private String CREATE_TABLE_PTS = "CREATE TABLE PTS (id INTEGER NOT NULL CONSTRAINT pts_pk PRIMARY KEY AUTOINCREMENT,N INTEGER," +
            "X DOUBLE,Y DOUBLE,Z DOUBLE,DES DOUBLE,Nombre TEXT,SREF INTEGER)";

    private String CREATE_TABLE_HTML = "CREATE TABLE HTML (id INTEGER  PRIMARY KEY AUTOINCREMENT,Date DATETIME,FileName TEXT,HTML TEXT);";

    private String CREATE_TABLE_EXPORT = "CREATE TABLE EXPORT (id INTEGER  PRIMARY KEY AUTOINCREMENT,Date DATETIME,FileName TEXT,EXPORT TEXT);";
    private String CREATE_TABLE_IMPORT = "CREATE TABLE IMPORT (id INTEGER  PRIMARY KEY AUTOINCREMENT,Date DATETIME,FileName TEXT,IMPORT TEXT);";
    private String CREATE_TABLE_MMCC = "CREATE TABLE MMCC (id INTEGER PRIMARY KEY AUTOINCREMENT,id_N INTEGER REFERENCES PTS (id),fijoPlani BOOLEAN DEFAULT (false),fijoAlti  BOOLEAN DEFAULT (false));";

    public TopcalDB(Context contexto, String nombre,SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRJ);
        db.execSQL(CREATE_TABLE_OBS);
        db.execSQL(CREATE_TABLE_PTS);
        db.execSQL(CREATE_TABLE_HTML);
        db.execSQL(CREATE_TABLE_EXPORT);
        db.execSQL(CREATE_TABLE_IMPORT);
        db.execSQL(CREATE_TABLE_MMCC);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //Se elimina la versión anterior de la tabla
        //Se crea la nueva versión de la tabla

        /*
        PRAGMA foreign_keys = 0;
        CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM OBS;
        DROP TABLE OBS;

        CREATE TABLE OBS (NE INTEGER,NV INTEGER,H DOUBLE,V DOUBLE,D DOUBLE,M DOUBLE,I DOUBLE,raw INTEGER,Aparato INTEGER,id INTEGER PRIMARY KEY AUTOINCREMENT);

        INSERT INTO OBS (NE,NV,H,V,D,M,I,raw,Aparato)
                SELECT NE,NV,H,V,D,M,I,raw,Aparato FROM sqlitestudio_temp_table;

        DROP TABLE sqlitestudio_temp_table;

        PRAGMA foreign_keys = 1;
         */
    }
}
