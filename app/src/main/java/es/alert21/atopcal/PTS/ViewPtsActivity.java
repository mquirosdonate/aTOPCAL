package es.alert21.atopcal.PTS;

import androidx.appcompat.app.AppCompatActivity;

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

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.EditarObsActivity;
import es.alert21.atopcal.OBS.NEAdapter;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class ViewPtsActivity extends AppCompatActivity {
    Topcal topcal;
    ListView listViewPts;
    List<PTS> ptsList;
    PtsAdapter adapter;
    private FloatingActionButton btnNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pts);
        btnNew = findViewById(R.id.floatingActionButton3);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Puntos(new PTS());
            }
        });
        listViewPts = (ListView) findViewById(R.id.listViewPTS);
        listViewPts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Visuales(neList.get(position));
                Toast.makeText(getApplicationContext(), ptsList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("PUNTOS");
        String prj = Util.cargaConfiguracion(MainActivity.yo,"Nombre Proyecto","");
        File path = Util.creaDirectorios(MainActivity.yo,"PROJECTS",prj);
        topcal = new Topcal(path.toString());
        ptsList = topcal.getPTS("SELECT * FROM PTS");

        //creating the adapter object
        adapter = new PtsAdapter(this,ptsList);

        //adding the adapter to listview
        listViewPts.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pts_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPTS:
                Puntos(new PTS());
                return true;
            case R.id.importCSV:
                //ImportarCSV();
                return true;
            default:
                return true;
        }
    }
    private void Puntos(PTS pts){
        Intent intent = new Intent(MainActivity.yo, EditarPtsActivity.class);
        intent.putExtra("PTS",pts);
        startActivity(intent);
    }
}