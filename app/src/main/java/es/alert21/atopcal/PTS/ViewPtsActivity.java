package es.alert21.atopcal.PTS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.EXPORT.TXTexport;
import es.alert21.atopcal.EXPORT.XMLexport;
import es.alert21.atopcal.FILES.FileChooser;
import es.alert21.atopcal.IMPORT.CSVpts;
import es.alert21.atopcal.IMPORT.SQLimport;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.EXPORT.SQLexport;
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
                Puntos(ptsList.get(position));
                //Toast.makeText(getApplicationContext(), ptsList.get(position).toString(), Toast.LENGTH_LONG).show();
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
        ptsList = topcal.getPTS("SELECT * FROM PTS Order by N,id");

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
                ImportarCSV();
                return true;
            case R.id.importSQL:
                ImportarSQL();
                return true;
            case R.id.exportarSQL:
                showDialogExportar(R.id.exportarSQL);
                return true;
            case R.id.exportarXML:
                showDialogExportar(R.id.exportarXML);
                return true;
            case R.id.exportarTXT:
                showDialogExportar(R.id.exportarTXT);
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
    private static final int REQUEST_PATH_CSV = 102;
    private static final int REQUEST_PATH_SQL = 103;
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
            case REQUEST_PATH_CSV:
                CSVpts csv = new CSVpts(file,topcal);
                break;
            case REQUEST_PATH_SQL:
                SQLimport importSQL = new SQLimport(file,topcal);
                break;
            default:
                break;
        }
    }
    private void ImportarSQL(){
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("DIR",topcal.getNombreTrabajo());
        startActivityForResult(intent,REQUEST_PATH_SQL);
    }
    private void ImportarCSV(){
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("DIR",topcal.getNombreTrabajo());
        startActivityForResult(intent,REQUEST_PATH_CSV);
    }
    private void showDialogExportar(final int idExport) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.datos_exportar, null);
        dialogBuilder.setView(dialogView);
        final TextView maximo = dialogView.findViewById(R.id.textViewMax);
        final TextView minimo = dialogView.findViewById(R.id.textViewMin);
        final EditText maxValue = dialogView.findViewById(R.id.editTextMax);
        maxValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText minValue = dialogView.findViewById(R.id.editTextMin);
        minValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        maxValue.requestFocus();

        final Integer MAX = topcal.getMaxPunto();
        final Integer MIN = topcal.getMinPunto();
        maximo.setText(MAX.toString());
        minimo.setText(MIN.toString());

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Integer max = MAX;
                Integer min = MIN;
                if (!maxValue.getText().toString().isEmpty())
                    max = Integer.parseInt(maxValue.getText().toString());

                if (!minValue.getText().toString().isEmpty())
                    min = Integer.parseInt(minValue.getText().toString());

                if (max < min){
                    Integer aux = max;
                    max = min;
                    min = aux;
                }
                switch (idExport) {
                    case R.id.exportarSQL:
                        SQLexport sqlClass = new SQLexport(topcal);
                        sqlClass.exportar("PTS",max,min);
                        break;
                    case R.id.exportarXML:
                        XMLexport XMLexportClass = new XMLexport(topcal);
                        XMLexportClass.exportar("PTS",max,min);
                        break;
                    case R.id.exportarTXT:
                        TXTexport TXTexportClass = new TXTexport(topcal);
                        TXTexportClass.exportar("PTS",max,min);
                        break;
                }
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}