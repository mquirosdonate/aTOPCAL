package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.FILES.FileChooser;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

public class ViewNeActivity extends AppCompatActivity {
    Topcal topcal;
    ListView listViewNEs;
    List<Integer> neList;
    NEAdapter adapter;
    private FloatingActionButton btnNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ne);
        btnNew = findViewById(R.id.floatingActionButton2);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observaciones(new OBS());
            }
        });
        listViewNEs = (ListView) findViewById(R.id.listViewNE);
        listViewNEs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Visuales(neList.get(position));
                Toast.makeText(getApplicationContext(), neList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("ESTACIONES");
        String prj = Util.cargaConfiguracion(MainActivity.yo,"Nombre Proyecto","");
        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);
        topcal = new Topcal(path.toString());
        neList = topcal.getNEs();

        //creating the adapter object
        adapter = new NEAdapter(this,neList);

        //adding the adapter to listview
        listViewNEs.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
    private void Visuales(Integer ne){
        Intent intent = new Intent(MainActivity.yo, ViewObsActivity.class);
        intent.putExtra("NE",ne);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.obs_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addOBS:
                Observaciones(new OBS());
                return true;
            case R.id.importLeica:
                ImportarLeica();
                return true;
            case R.id.importGeodimeter:
                ImportarGeodimeter();
                return true;
            case R.id.importCSV:
                ImportarCSV();
                return true;
            default:
                return true;
        }
    }
    private static final int REQUEST_PATH_LEICA = 100;
    private static final int REQUEST_PATH_GEODIMETER = 101;
    private static final int REQUEST_PATH_CSV = 102;
    private void ImportarLeica(){
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("DIR",topcal.getNombreTrabajo());
        startActivityForResult(intent,REQUEST_PATH_LEICA);
    }
    private void ImportarGeodimeter(){
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("DIR",topcal.getNombreTrabajo());
        startActivityForResult(intent,REQUEST_PATH_GEODIMETER);
    }
    private void ImportarCSV(){
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("DIR",topcal.getNombreTrabajo());
        startActivityForResult(intent,REQUEST_PATH_CSV);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK){
            Toast.makeText(getApplicationContext(), "No has selecionado NADA", Toast.LENGTH_LONG);
            return;
        }
        String curPath = data.getStringExtra("GetPath");
        String curFileName = data.getStringExtra("GetFileName");
        String fileName = curPath + "/" + curFileName;
        File file = new File(fileName);
        switch (requestCode) {
            case REQUEST_PATH_LEICA:
                Leica leica = new Leica(file,topcal);
                break;
            case REQUEST_PATH_GEODIMETER:
                Geodimeter geodimeter = new Geodimeter(file,topcal);
                break;
            case REQUEST_PATH_CSV:
                CSV csv = new CSV(file,topcal);
                break;
            default:
                break;
        }
    }


    private void Observaciones(OBS obs){
        Intent intent = new Intent(MainActivity.yo, EditarObsActivity.class);
        intent.putExtra("OBS",obs);
        startActivity(intent);
    }
}