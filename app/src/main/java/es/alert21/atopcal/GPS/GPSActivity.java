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
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.EPSG.Elipsoide;
import es.alert21.atopcal.EPSG.UTM;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class GPSActivity extends AppCompatActivity {
    Topcal topcal;
    private Elipsoide elipsoide = new Elipsoide();
    private UTM utm;
    private Geoide geoide = null;
    public int N;
    private float MIN_DISTANCE = 0.0f;
    private int MIN_TIME = 100;
    ToggleButton toggleButton;
    Button Geo2UTM;
    ImageButton Guardar;
    EditText editTextLongitud,editTextLatitud,editTextAltitud,
            editTextN,editTextHuso,editTextNP,editTextNombre;
    TextView textACC, textX, textY, textZ;
    boolean bHuso = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s);
        geoide = new Geoide(this);
        utm = new UTM(elipsoide);

        getSupportActionBar().setTitle("GPS");
        editTextNP = findViewById(R.id.editTextNP);
        editTextNombre = findViewById(R.id.editTextNombre);

        Guardar = findViewById(R.id.imageButtonOK);
        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = editTextNP.getText().toString();
                if (n.isEmpty()){
                    // Indicar que hay un error, quizás con un sonido
                    return;
                }else{
                    PTS pts = new PTS();
                    pts.setN(n);
                    pts.setX(textX.getText().toString());
                    pts.setY(textY.getText().toString());
                    pts.setZ(textZ.getText().toString());

                    pts.setNombre(editTextNombre.getText().toString());

                    topcal.insertPTS(pts);

                    Integer np = pts.getN() + 1;
                    editTextNP.setText(np.toString());
                }
            }
        });

        Geo2UTM = findViewById(R.id.Geo2UTM);
        Geo2UTM.setVisibility(View.INVISIBLE);
        Geo2UTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer huso = Integer.parseInt(editTextHuso.getText().toString());
                double Longitud = Double.parseDouble(editTextLongitud.getText().toString());
                double Latitud = Double.parseDouble(editTextLatitud.getText().toString());
                double Altitud = Double.parseDouble(editTextAltitud.getText().toString());
                double N = Double.parseDouble(editTextN.getText().toString());
                double Z = Altitud - N;

                utm.GEO2UTM(Latitud,Longitud,huso);
                textZ.setText(Util.doubleATexto(Z,2));
                textX.setText(Util.doubleATexto(utm.X,3));
                textY.setText(Util.doubleATexto(utm.Y,3));
            }
        });

        toggleButton = findViewById(R.id.toggleButtonGPS);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    GPS();
                    Geo2UTM.setVisibility(View.INVISIBLE);
                } else {
                    // The toggle is disabled
                    stopGPS();
                    Geo2UTM.setVisibility(View.VISIBLE);
                    //editTextLongitud.setText("");
                    //editTextLatitud.setText("");
                    //editTextAltitud.setText("");
                    //textACC.setText("");
                    //editTextN.setText("");
                }

            }
        });
        textX = findViewById(R.id.editTextXUTM);
        textY = findViewById(R.id.editTextYUTM);
        editTextLongitud = findViewById(R.id.editTextLongitud);
        editTextLatitud = findViewById(R.id.editTextLatitud);
        editTextAltitud = findViewById(R.id.editTextAltitud);
        editTextN = findViewById(R.id.editTextN);
        textACC = findViewById(R.id.textACC);
        textZ = findViewById(R.id.editTextZ);
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
    @Override
    protected void onResume() {
        super.onResume();
        topcal = Util.getTopcal();
    }
    private void updateUI(Location loc) {
        if (loc == null) return;
        N = geoide.GetN(loc.getLatitude(), loc.getLongitude());
        double Longitud = loc.getLongitude();
        double Latitud = loc.getLatitude();
        double Altitud = loc.getAltitude();
        double Acc = loc.getAccuracy();

        double Z = Altitud - N;
        Integer huso ;
        if (bHuso) {
            huso = (int)(Longitud / 6);
            if(Longitud < 0)
                huso += 30;
            else
                huso += 31;
            editTextHuso.setText(huso.toString());
        } else {
            huso = Integer.parseInt(editTextHuso.getText().toString());
        }

        editTextLongitud.setText(Util.doubleATexto(Longitud,7));
        editTextLatitud.setText(Util.doubleATexto(Latitud,7));
        editTextAltitud.setText(Util.doubleATexto(Altitud,1));
        textACC.setText(Util.doubleATexto(Acc,1));
        editTextN.setText(Util.doubleATexto(N,1));
        textZ.setText(Util.doubleATexto(Z,1));
        utm.GEO2UTM(Latitud,Longitud,huso);
        textX.setText(Util.doubleATexto(utm.X,3));
        textY.setText(Util.doubleATexto(utm.Y,3));
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