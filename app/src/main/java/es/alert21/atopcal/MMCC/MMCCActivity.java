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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.OBS.OBS;
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
    private FloatingActionButton btnNew;

    TextView textView;
    String sql = "SELECT * FROM PTS ORDER BY N DESC";
    String sqlGet = "SELECT PTS.id,PTS.N,PTS.X,PTS.Y,PTS.Z,PTS.DES,PTS.Nombre,MMCC.fijoPlani,MMCC.fijoAlti FROM PTS,MMCC WHERE MMCC.id_N=PTS.id";
    String sqlGetRed = "SELECT N FROM PTS,MMCC WHERE MMCC.id_N=PTS.id";
    String sqlGetPtsNotInRed = "SELECT * FROM PTS WHERE N NOT IN ("+sqlGetRed+") ORDER BY N DESC;";
    String sqlGetObsRed = "SELECT * FROM OBS WHERE NE IN ("+sqlGetRed+") AND NV IN ("+sqlGetRed+");";

    int nIncognitas = 0;  /* número de incognitas */
    int nIncogxy = 0;
    int nIncogz = 0;
    int nIncogdes = 0;
    int nObsd = 0,nObsh = 0,nObsa = 0;
    int nfijosxy = 0;
    int nfijosz = 0;

    int LIST_OBS =  1;
    int LIST_NOR =  1;
    int UTM      =  1;
    int DIRE     =  1;
    int DIST     =  1;
    int ALTI     =  1;
    int COMPENSACION = 1;
    int SIMULACION   = 1;
    int TEST_OBS     = 1;

    double RT ;       /* Radio de la tierra */
    double ALFA ;     /* aplanamiento */
    int  ERR_A,ERR_AD,ERR_D1,ERR_D2;
    float ERR_K;
    int PPM;
    int incog_k ;     /* incognita de escala */
    String szPPM = "?";

    int PASO = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_m_c_c);
        getSupportActionBar().setTitle("MMCC");
        btnNew = findViewById(R.id.floatingActionButton3);
        btnNew.setVisibility(View.GONE);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPtsNotInRed();
                SetAdapterRed();
            }
        });
        buttonAnterior = findViewById(R.id.buttonAnterior);
        buttonSiguiente = findViewById(R.id.buttonSiguiente);
        buttonSalir = findViewById(R.id.buttonSalir);
        buttonSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guardarRed();
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

    private void FiltrarPuntosDeRed(){
        for(int i = 0; i < redList.size();i++) {
            if (!redList.get(i).valido) {
                redList.remove(i);
                i--;
            }
        }
        }
    private void CargaRed(){
        nIncognitas  = 0;
        nIncogxy = 0;
        nIncogz = 0;
        nIncogdes = 0;
        nfijosxy = 0;
        nfijosz = 0;
        nObsd=nObsh=nObsa=0 ;

        if (SIMULACION == 1) incog_k = 0;
        if (DIST == 1) {
            if (incog_k == 1) nIncognitas = 1;
        } else {
            incog_k = 0;
        }

        for(int i = 0; i < redList.size();i++){
            redList.get(i).ix = 0;
            redList.get(i).iy = 0;
            redList.get(i).iz = 0;
            redList.get(i).id = 0;
            if ( DIST == 1 || DIRE ==1 ) {
                if (redList.get(i).getFijoPlani()) {
                    nfijosxy++;
                } else {
                    redList.get(i).ix = ++nIncognitas;
                    redList.get(i).iy = ++nIncognitas;
                    nIncogxy += 2;
                }
            }
            if (ALTI == 1 ){
                    if (redList.get(i).getFijoAlti()) {
                        nfijosz++;
                    } else {
                        redList.get(i).iz = ++nIncognitas;
                        nIncogz++;
                    }
                }
        }

        List<OBS> obsList = topcal.getOBS(sqlGetObsRed);

    }

    private void getTodos(){
        //Todos los puntos de la tabla PTS
        redList.clear();
        List<PTS> neList = topcal.getPTS(sql);
        for(PTS n:neList){
            PTSred p = new PTSred(n,false,false);
            redList.add(p);
        }
        btnNew.setVisibility(View.GONE);
    }
    private void getUsados(){
        //Los puntos usados la vez anterior y guardados en la tabla MMCC
        redList = topcal.getRed(sqlGet);
        btnNew.setVisibility(View.VISIBLE);
    }
    private void GetPtsNotInRed(){
        List<PTS> neList = topcal.getPTS(sqlGetPtsNotInRed);
        for(PTS n:neList){
            PTSred p = new PTSred(n,false,false);
            redList.add(p);
        }
    }

    private void guardarRed(){
        FiltrarPuntosDeRed();
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