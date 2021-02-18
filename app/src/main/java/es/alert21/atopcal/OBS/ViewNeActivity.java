package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.PRJ.PRJ;
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
            default:
                return true;
        }
    }
    private void Observaciones(OBS obs){
        Intent intent = new Intent(MainActivity.yo, ObsActivity.class);
        intent.putExtra("OBS",obs);
        startActivity(intent);
    }
}