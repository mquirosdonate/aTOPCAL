package es.alert21.atopcal.IMPORT;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.OBS.OBSx2;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class OBSx2Adapter extends ArrayAdapter<OBSx2> {
    List<OBSx2> obsList;
    Context context;
    public OBSx2Adapter(Context context,  List<OBSx2> obsList){
        super(context, R.layout.list_item, obsList);
        this.obsList = obsList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.list_item, null, false);
        OBSx2 obsx2 = obsList.get(position);
        TextView textViewItem = view.findViewById(R.id.TextViewItem);
        TextView textViewResult = view.findViewById(R.id.textViewResult);

        String txtItem = obsx2.getObs1().getNVtoString() + " " +
                obsx2.getObs1().getHtoString() + " " +
                obsx2.getObs2().getHtoString() ;

        textViewItem.setText(txtItem);
        textViewResult.setText(Util.doubleATexto(obsx2.desorientacion(),4));
        return view;
    }

    }
