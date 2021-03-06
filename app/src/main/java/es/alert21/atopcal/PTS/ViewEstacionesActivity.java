package es.alert21.atopcal.PTS;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class ViewEstacionesActivity extends AppCompatActivity {
    Topcal topcal;
    ListView listViewPts;
    List<PTS> ptsList;
    PtsAdapter adapter;
    String sql = "SELECT DISTINCT N,PTS.Id,X,Y,Z,Des FROM PTS,OBS\n" +
            "WHERE Des = 0 AND OBS.raw = 0 AND PTS.N=OBS.NE AND OBS.NV IN (SELECT N FROM PTS) ORDER BY N";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_estaciones);
        listViewPts = (ListView) findViewById(R.id.listViewEstaciones);
        listViewPts.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Estacion(ptsList.get(position));
                //Toast.makeText(getApplicationContext(), ptsList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void Estacion(PTS pts){
        Intent intent = new Intent(this, DesorientacionesActivity.class);
        intent.putExtra("PTS",pts);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("ESTACIONES");
        topcal = Util.getTopcal();
        ptsList = topcal.getPTS(sql);

        //creating the adapter object
        adapter = new PtsAdapter(this,ptsList);

        //adding the adapter to listview
        listViewPts.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
}