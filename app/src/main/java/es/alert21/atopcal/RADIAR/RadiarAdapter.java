package es.alert21.atopcal.RADIAR;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class RadiarAdapter extends ArrayAdapter<NE> {
    List<NE> ptsList;
    Context context;
    int layout;
    public RadiarAdapter(Context context, List<NE>  ptsList, int layout){
        super(context, layout, ptsList);
        this.ptsList = ptsList;
        this.context = context;
        this.layout = layout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(layout, null, false);
        PTS pts = ptsList.get(position).n;
        CheckBox cs = view.findViewById(R.id.checkBox);
        boolean b = ptsList.get(position).checked;
        cs.setChecked(b);
        cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ptsList.get(position).checked = cs.isChecked();
            }
        });
        TextView txtN = view.findViewById(R.id.listPtsN);
        txtN.setText(pts.getNtoString());
        TextView txtX = view.findViewById(R.id.textViewPtsX);
        TextView txtY = view.findViewById(R.id.textViewPtsY);
        TextView txtZ = view.findViewById(R.id.textViewPtsZ);
        TextView txtDes = view.findViewById(R.id.textViewPtsDes);
        TextView txtNombre = view.findViewById(R.id.textViewPtsNombre);

        txtX.setText(Util.doubleATexto(pts.getX(),2));
        txtY.setText(Util.doubleATexto(pts.getY(),2));
        txtZ.setText(Util.doubleATexto(pts.getZ(),2));
        txtDes.setText(Util.doubleATexto(pts.getDes(),4));
        txtNombre.setText(pts.getNombre());
        return view;
    }
}
