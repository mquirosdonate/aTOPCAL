package es.alert21.atopcal.MMCC;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.POLIG.PoligActivity;
import es.alert21.atopcal.POLIG.PoligAdapter;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class MMCCActivity extends AppCompatActivity {
    StringBuilder html = new StringBuilder();
    Topcal topcal;
    ListView listViewRed;
    List<PTSred> redList = new ArrayList<>();
    RedAdapter adapterRed;
    Button buttonAnterior,buttonSiguiente,buttonSalir;

    TextView textView;
    String sql = "SELECT * FROM PTS ORDER BY N DESC";
    String sqlGet = "SELECT PTS.id,PTS.N,PTS.X,PTS.Y,PTS.Z,PTS.DES,PTS.Nombre,MMCC.fijoPlani,MMCC.fijoAlti FROM PTS,MMCC WHERE MMCC.id_N=PTS.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_m_c_c);
        getSupportActionBar().setTitle("MMCC");

        buttonAnterior = findViewById(R.id.buttonAnterior);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        buttonSalir = findViewById(R.id.buttonSalir);
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRed();
                finish();
            }
        });
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRed();
            }
        });
        listViewRed = (ListView) findViewById(R.id.rightListView);
        textView = findViewById(R.id.textView19);
        topcal = Util.getTopcal();
        getUsados();
        if (redList.size()==0)
            getTodos();
        SetAdapterRed();
    }

    private void getTodos(){
        List<PTS> neList = topcal.getPTS(sql);
        for(PTS n:neList){
            PTSred p = new PTSred(n,false,false);
            redList.add(p);
        }
    }
    private void getUsados(){
        redList = topcal.getRed(sqlGet);
    }


    private void guardarRed(){
        topcal.insertRed(redList);
    }

    private void SetAdapterRed(){
        adapterRed = new RedAdapter(MMCCActivity.this,redList,R.layout.list_red);
        listViewRed.setAdapter(adapterRed);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // botón al pulsar ir a atrás

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}