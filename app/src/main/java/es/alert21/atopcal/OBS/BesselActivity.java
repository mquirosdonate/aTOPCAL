package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class BesselActivity extends AppCompatActivity {
    private String sql = "SELECT * FROM OBS " +
            "WHERE (NE,NV) IN " +
            "(SELECT NE,NV " +
            "FROM OBS " +
            "GROUP BY NE,NV " +
            "HAVING COUNT(*)>1)";
    Topcal topcal;
    List<OBS> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("REGLA DE BESSEL");
        setContentView(R.layout.activity_bessel);
        topcal = Util.getTopcal();
        list = topcal.getOBS(sql);
    }
}