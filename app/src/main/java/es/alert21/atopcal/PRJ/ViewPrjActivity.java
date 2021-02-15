package es.alert21.atopcal.PRJ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.OBS.ObsAdapter;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Topcal;
import es.alert21.atopcal.Util;

public class ViewPrjActivity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ListView lstProjects;
    List<OBS> prjList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prj);
        lstProjects = (ListView)findViewById(R.id.listViewPRJ);
        lstProjects.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //Observaciones(obsList.get(position));
                //Toast.makeText(getApplicationContext(), obsList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("PRJ");

        //obsList = topcal.getOBS("SELECT * FROM OBS Order by NE,NV,raw");

        //creating the adapter object
        //adapter = new ObsAdapter(this,prjList);

        //adding the adapter to listview
        lstProjects.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
}