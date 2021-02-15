package es.alert21.atopcal.PRJ;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.R;

public class PrjAdapter extends ArrayAdapter<PRJ> {
    List<PRJ> prjList;
    //activity context
    Context context;

    public PrjAdapter(Context context,  List<PRJ> prjList){
        super(context, R.layout.list_prj, prjList);
        this.prjList = prjList;
        this.context = context;
    }

}