package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

import es.alert21.atopcal.MainActivity;

import es.alert21.atopcal.R;
import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.Util;

public class ViewObsActivity extends AppCompatActivity {
    Topcal topcal;
    List<OBS> obsList;
    ListView listViewObs;
    ObsAdapter adapter;
    private FloatingActionButton btnNew;
    Integer ne=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_obs);
        btnNew = findViewById(R.id.floatingActionButton3);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Observaciones(new OBS());
            }
        });
        ne = getIntent().getIntExtra("NE",0);
        listViewObs = (ListView) findViewById(R.id.listViewObs);
        listViewObs.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Observaciones(obsList.get(position));
                //Toast.makeText(getApplicationContext(), obsList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("OBS");
        topcal = Util.getTopcal();
        obsList = topcal.getOBS("SELECT * FROM OBS WHERE NE="+ne.toString()+" Order by NV,id,raw");

        //creating the adapter object
        adapter = new ObsAdapter(this,obsList);

        //adding the adapter to listview
        listViewObs.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(MainActivity.yo, EditarObsActivity.class);
        intent.putExtra("OBS",obs);
        startActivity(intent);
    }

}