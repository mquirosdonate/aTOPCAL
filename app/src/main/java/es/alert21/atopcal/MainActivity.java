package es.alert21.atopcal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.GPS.GPSActivity;
import es.alert21.atopcal.MMCC.MMCCActivity;
import es.alert21.atopcal.MMCC.MainMMCCActivity;
import es.alert21.atopcal.OBS.BesselActivity;
import es.alert21.atopcal.OBS.ViewNeActivity;
import es.alert21.atopcal.POLIG.PoligActivity;
import es.alert21.atopcal.PRJ.ViewPrjActivity;
import es.alert21.atopcal.PTS.EditarPtsActivity;
import es.alert21.atopcal.PTS.ViewEstacionesActivity;
import es.alert21.atopcal.PTS.ViewPtsActivity;
import es.alert21.atopcal.RADIAR.RadiarActivity;

public class MainActivity extends AppCompatActivity {
    public static MainActivity yo;
    public String nombreProyecto="";
    private TextView textViewNombreProyecto;
    private ImageView imageViewOBS,imageViewBessel,imageViewPTS,imageViewPRJ,imageViewDesorientacion,
                        imageViewPolig,imageViewRadiar,imageViewGPS,imageViewMMCC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yo = this;
        if(!checkAndRequestPermissions()) return;

        textViewNombreProyecto = findViewById(R.id.MainActivityNombreProyecto);

        imageViewGPS = findViewById(R.id.imageViewGPS);
        imageViewGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gps();
            }
        });
        imageViewRadiar = findViewById(R.id.imageViewRadiar);
        imageViewRadiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radiacion();
            }
        });
        imageViewMMCC = findViewById(R.id.imageViewMMCC);
        imageViewMMCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MMCC();
            }
        });
        imageViewPolig = findViewById(R.id.imageViewPolig);
        imageViewPolig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poligonales();
            }
        });
        imageViewDesorientacion = findViewById(R.id.imageViewDesorientacion);
        imageViewDesorientacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Desorientaciones();
            }
        });
        imageViewOBS = findViewById(R.id.imageViewOBS);
        imageViewOBS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observaciones();
            }
        });
        imageViewBessel = findViewById(R.id.imageViewBessel);
        imageViewBessel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bessel();
            }
        });
        imageViewPTS = findViewById(R.id.imageViewPTS);
        imageViewPTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Puntos();
            }
        });
        imageViewPRJ = findViewById(R.id.imageViewPRJ);
        imageViewPRJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proyectos();
            }
        });
        getRawFiles();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Observaciones:
                Observaciones();
                return true;
            case R.id.Puntos:
                Puntos();
                return true;
            case R.id.Proyectos:
                Proyectos();
                return true;
            case R.id.menuBessel:
                Bessel();
                return true;
            case R.id.Desorientaciones:
                Desorientaciones();
                return true;
            case R.id.Poligonales:
                Poligonales();
                return true;
            case R.id.Radiacion:
                Radiacion();
            case R.id.MMCC:
                MMCC();
                return true;
            default:
                return true;
        }
    }
    public void getRawFiles(){
        Topcal topcal = Util.getTopcal();
        Field[] fields = R.raw.class.getFields();
        // loop for every file in raw folder
        for(int count=0; count < fields.length; count++){
            String filename = fields[count].getName();
            try {
                String newFileName = Environment.getExternalStorageDirectory()+"/"+filename+".css";
                File file = new File(topcal.getNombreTrabajo()+"/"+filename+".css");
                if (file.exists()) continue;

                OutputStream out = new FileOutputStream(file, false);

                int rid = fields[count].getInt(fields[count]);
                Resources res = getResources();
                InputStream in = res.openRawResource(rid);

                byte[] b = new byte[in.available()];
                in.read(b);
                out.write(b);

                in.close();
                out.flush();
                out.close();
            } catch (Exception e) {
                // log error
            }
        }
    }
    private void Proyectos(){
        Intent intent = new Intent(MainActivity.this, ViewPrjActivity.class);
        startActivity(intent);
    }
    private void Observaciones(){
        Intent intent = new Intent(MainActivity.this, ViewNeActivity.class);
        startActivity(intent);
    }
    private void Puntos(){
        Intent intent = new Intent(MainActivity.this, ViewPtsActivity.class);
        startActivity(intent);
    }
    private void Bessel(){
        Intent intent = new Intent(MainActivity.this, BesselActivity.class);
        startActivity(intent);
    }
    private void Desorientaciones(){
        Intent intent = new Intent(this, ViewEstacionesActivity.class);
        startActivity(intent);
    }
    private void Poligonales(){
        Intent intent = new Intent(this, PoligActivity.class);
        startActivity(intent);
    }
    private void Radiacion(){
        Intent intent = new Intent(this, RadiarActivity.class);
        startActivity(intent);
    }
    private void MMCC(){
        Intent intent = new Intent(this, MainMMCCActivity.class);
        startActivity(intent);
    }
    private void Gps(){
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("TOPCAL");
        nombreProyecto = Util.cargaConfiguracion(MainActivity.this,"Nombre Proyecto","");
        textViewNombreProyecto.setText(nombreProyecto);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private  boolean checkAndRequestPermissions() {

        int permissionLeer = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int locationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionLeer != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        //Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  ) {
                        //Log.d(TAG, "sms & location services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        finish();
                        startActivity(getIntent());
                    } else {
                        //Log.d(TAG, "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_FINE_LOCATION) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE) )
                        {
                            showDialogOK("WRITE_STORAGE  and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //proceed with logic by disabling the related features or quit the app.
                            finish();
                        }
                    }
                }
            }
        }

    }
    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.Aceptar), okListener)
                .setNegativeButton(getResources().getString(R.string.Cancelar), okListener)
                .create()
                .show();
    }
}