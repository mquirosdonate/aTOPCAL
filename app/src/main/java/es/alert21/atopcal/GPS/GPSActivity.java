package es.alert21.atopcal.GPS;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class GPSActivity extends AppCompatActivity {
    public Geoide geoide = null;
    public int N;
    private float MIN_DISTANCE = 0.0f;
    private int MIN_TIME = 100;
    ToggleButton toggleButton;
    EditText editTextLongitud,editTextLatitud,editTextAltitud,editTextN,editTextACC,editTextZ,editTextHuso;
    boolean bHuso = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s);
        geoide = new Geoide(this);
        getSupportActionBar().setTitle("GPS");
        toggleButton = findViewById(R.id.toggleButtonGPS);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    GPS();
                } else {
                    // The toggle is disabled
                    stopGPS();
                    editTextLongitud.setText("");
                    editTextLatitud.setText("");
                    editTextAltitud.setText("");
                    editTextACC.setText("");
                    editTextN.setText("");
                }

            }
        });
        editTextLongitud = findViewById(R.id.editTextLongitud);
        editTextLatitud = findViewById(R.id.editTextLatitud);
        editTextAltitud = findViewById(R.id.editTextAltitud);
        editTextN = findViewById(R.id.editTextN);
        editTextACC = findViewById(R.id.editTextACC);
        editTextZ = findViewById(R.id.editTextZ);
        editTextHuso = findViewById(R.id.editTextHuso);
        editTextHuso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bHuso = false;
            }
        });
        stopGPS();
        GPS();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        stopGPS();
    }

    private void updateUI(Location loc) {
        if (loc == null) return;
        N = geoide.GetN(loc.getLatitude(), loc.getLongitude());
        double Longitud = loc.getLongitude();
        double Latitud = loc.getLatitude();
        double Altitud = loc.getAltitude();
        double Acc = loc.getAccuracy();
        double XUTM = 0.0;
        double YUTM = 0.0;
        double Z = Altitud - N;

        if (bHuso) {
            Integer huso = (int)(Longitud / 6);
            if(Longitud < 0)
                huso += 30;
            else
                huso += 31;
            editTextHuso.setText(huso.toString());
        }

        editTextLongitud.setText(Util.doubleATexto(Longitud,6));
        editTextLatitud.setText(Util.doubleATexto(Latitud,6));
        editTextAltitud.setText(Util.doubleATexto(Altitud,1));
        editTextACC.setText(Util.doubleATexto(Acc,1));
        editTextN.setText(Util.doubleATexto(N,1));
        editTextZ.setText(Util.doubleATexto(Z,1));
    }
    private LocationManager locManager;
    private LocationListener locListener = null;
    private void GPS() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locListener = new LocationListener() {
            public void onStatusChanged(String provider, int status, Bundle bundle) {
                gpsStatus(provider, status, bundle);
            }
            public void onProviderEnabled(String provider) {
                gpsON();
            }
            public void onProviderDisabled(String provider) {
                gpsOFF();
            }
            public void onLocationChanged(Location loc) {
                updateUI(loc);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }

        try{
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME,MIN_DISTANCE,locListener);
        }catch (Exception e){
            Toast.makeText(this, "ERROR al activar GPS", Toast.LENGTH_LONG).show();
        }

    }
    private void stopGPS() {
        if (locListener != null) {
            locManager.removeUpdates(locListener);
            locListener = null;
        }
    }
    private void gpsStatus(String provider, int status, Bundle bundle) {
        String message = "";
        switch (status) {
            case GpsStatus.GPS_EVENT_FIRST_FIX:
                message = "FIX";
                break;
            case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
                return;
            //break;
            case GpsStatus.GPS_EVENT_STARTED:
                message = "ON";
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                message = "OFF";
                break;

        }

    }
    private void gpsON() {
        Toast.makeText(this, "GPS ON", Toast.LENGTH_LONG).show();
    }
    private void gpsOFF() {
        Toast.makeText(this, "GPS OFF", Toast.LENGTH_LONG).show();
    }
}