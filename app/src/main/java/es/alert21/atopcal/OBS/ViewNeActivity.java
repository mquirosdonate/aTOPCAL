package es.alert21.atopcal.OBS;

import androidx.appcompat.app.AppCompatActivity;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.EXPORT.TXTexport;
import es.alert21.atopcal.EXPORT.XMLexport;
import es.alert21.atopcal.FILES.FileChooser;
import es.alert21.atopcal.IMPORT.ImportObsActivity;
import es.alert21.atopcal.IMPORT.SQLimport;
import es.alert21.atopcal.MainActivity;
import es.alert21.atopcal.R;
import es.alert21.atopcal.EXPORT.SQLexport;
import es.alert21.atopcal.Util;

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

public class ViewNeActivity extends AppCompatActivity {
    Topcal topcal;
    ListView listViewNEs;
    List<Integer> neList;
    NEAdapter adapter;
    String sql = "SELECT DISTINCT(ne) FROM OBS Order by NE";
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
        getSupportActionBar().setTitle("SELECCIONA UNA ESTACIÓN");
        topcal = Util.getTopcal();
        neList = topcal.getNEs(sql);

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
            case R.id.menuBessel:
                Bessel();
                return true;
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

    private static final int REQUEST_PATH_LEICA = 100;
    private static final int REQUEST_PATH_GEODIMETER = 101;
    private static final int REQUEST_PATH_CSV = 102;
    private static final int REQUEST_PATH_SQL = 103;
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
    private void ImportarSQL(){
        Intent intent = new Intent(this, FileChooser.class);
        intent.putExtra("DIR",topcal.getNombreTrabajo());
        startActivityForResult(intent,REQUEST_PATH_SQL);
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
        Intent intent = new Intent(this, ImportObsActivity.class);
        Bundle b = new Bundle();
        switch (requestCode) {

            case REQUEST_PATH_LEICA:
                //Leica leica = new Leica(file,topcal);
                b.putString("FILE",file.getAbsolutePath().toString());
                b.putInt("TIPO",0);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case REQUEST_PATH_GEODIMETER:
                //Geodimeter geodimeter = new Geodimeter(file,topcal);
                b.putString("FILE",file.getAbsolutePath().toString());
                b.putInt("TIPO",1);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case REQUEST_PATH_CSV:
                //CSVobs csv = new CSVobs(file,topcal);
                b.putString("FILE",file.getAbsolutePath().toString());
                b.putInt("TIPO",2);
                intent.putExtras(b);
                startActivity(intent);
                break;
            case REQUEST_PATH_SQL:
                SQLimport importSQL = new SQLimport(file,topcal);
                break;
            default:
                break;
        }
    }

    private void Bessel(){
        Intent intent = new Intent(ViewNeActivity.this, BesselActivity.class);
        startActivity(intent);
    }
    private void Observaciones(OBS obs){
        Intent intent = new Intent(ViewNeActivity.this, EditarObsActivity.class);
        intent.putExtra("OBS",obs);
        startActivity(intent);
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

        final Integer MAX = topcal.getMaxEstacion();
        final Integer MIN = topcal.getMinEstacion();
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
                        sqlClass.exportar("OBS",max,min);
                        break;
                    case R.id.exportarXML:
                        XMLexport XMLexportClass = new XMLexport(topcal);
                        XMLexportClass.exportar("OBS",max,min);
                        break;
                    case R.id.exportarTXT:
                        TXTexport TXTexportClass = new TXTexport(topcal);
                        TXTexportClass.exportar("OBS",max,min);
                        break;
                }
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }
}