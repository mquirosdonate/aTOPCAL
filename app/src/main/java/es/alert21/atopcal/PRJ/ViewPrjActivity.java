package es.alert21.atopcal.PRJ;

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
import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.OBS.ObsActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class ViewPrjActivity extends AppCompatActivity {
    private PrjAdapter adapter;
    private ListView lstProjects;
    private FloatingActionButton btnNew;
    List<PRJ> prjList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_prj);
        btnNew = findViewById(R.id.floatingActionButton);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Proyectos(new PRJ());
            }
        });
        lstProjects = (ListView)findViewById(R.id.listViewPRJ);
        lstProjects.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Util.guardaConfiguracion(ViewPrjActivity.this,"Nombre Proyecto",prjList.get(position).getNombre());
                Proyectos(prjList.get(position));
                //Toast.makeText(getApplicationContext(), prjList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().setTitle("PRJ");

        prjList = getPrjList();

        //creating the adapter object
        adapter = new PrjAdapter(this,prjList);

        //adding the adapter to listview
        lstProjects.setAdapter(adapter);
        //Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
    }
    private List<PRJ> getPrjList(){
        ArrayList<PRJ> list = new ArrayList<PRJ>();
        File dir = Util.creaDirectorios(MainActivity.yo, "PROJECTS");
        File[] files = dir.listFiles();
        for(File file : files){
            if(!file.isDirectory())continue;
            File file1 = new File(file+"/"+"topcal.db");
            if (file1.exists()){
                Topcal topcal = new Topcal(file.toString());
                if(topcal != null)list.add(topcal.getPRJ());
            }
        }
        return list;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.prj_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addPRJ:
                Proyectos(new PRJ());
                return true;
            default:
                return true;
        }
    }
    private void Proyectos(PRJ prj){
        Intent intent = new Intent(MainActivity.yo, PrjActivity.class);
        intent.putExtra("PRJ",prj);
        startActivity(intent);
    }
}