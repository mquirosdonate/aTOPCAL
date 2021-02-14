package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.OBS.ObsActivity;
import es.alert21.atopcal.OBS.ObsAdapter;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Topcal;
import es.alert21.atopcal.Util;

public class ViewObsActivity extends AppCompatActivity {
    Topcal topcal;
    List<OBS> obsList;
    ListView listViewObs;
    ObsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_obs);
        listViewObs = (ListView) findViewById(R.id.listViewObs);
        listViewObs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Toast.makeText(getApplicationContext(), obsList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("OBS");
        String prj = Util.cargaConfiguracion(MainActivity.yo,"Nombre Proyecto","");
        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);
        topcal = new Topcal(path.toString());
        obsList = topcal.getOBS("SELECT * FROM OBS Order by NE,NV,raw");

        //creating the adapter object
        adapter = new ObsAdapter(this,obsList);

        //adding the adapter to listview
        listViewObs.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.obs_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.addOBS:
                Observaciones();
                return true;
            default:
                return true;
        }
    }
    private void Observaciones(){
        Intent intent = new Intent(MainActivity.yo, ObsActivity.class);
        startActivity(intent);
    }

}