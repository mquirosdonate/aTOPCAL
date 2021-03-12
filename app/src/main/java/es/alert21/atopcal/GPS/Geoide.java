package es.alert21.atopcal.GPS;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;

/*
 * Created by Asus on 25/01/2016.
 */
public class Geoide {
    int nRcols ;
    int nRrows ;
    double dd ;
    byte[] b;
    protected Context m_context;

    Geoide(Context context){
        m_context = context;
        dd = 15.0 / 60.0;
        nRcols = (int)(360 / dd);
        nRrows = (int)(180 / dd) + 1;
        b = new byte[nRrows * nRcols];

        //Obtenemos la referencia al fichero  de entrada
        AssetManager assetMan = context.getAssets();

        try {
            InputStream is = assetMan.open("egm2008_15x15_WGS84.geo");
            int  l = is.read(b, 0, nRrows * nRcols);

        } catch(Exception e){
        }

    }

    public byte GetN(double lat,double lon)
    {
        if (lon < 0)
            lon += 360;

        int fila = (int) Math.round( (90 -lat) / dd );

        int col = (int) Math.round(lon / dd);
        if (col == nRcols) col = 0; //360º no existe es 0º

        byte N = b[fila * nRcols + col];
        //if (N > 127) N = N - 256;
        return N;
    }

}
