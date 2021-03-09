package es.alert21.atopcal.POLIG;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class PoligAdapter extends ArrayAdapter<PTS> {
    List<PTS> ptsList;
    Context context;
    int layout;
    public PoligAdapter(Context context, List<PTS>  ptsList,int layout){
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
        PTS pts = ptsList.get(position);
        TextView txtN = view.findViewById(R.id.listPtsN);
        txtN.setText(pts.getNtoString());
        TextView txtX = view.findViewById(R.id.textViewPtsX);
        TextView txtY = view.findViewById(R.id.textViewPtsY);
        TextView txtZ = view.findViewById(R.id.textViewPtsZ);
        TextView txtDes = view.findViewById(R.id.textViewPtsDes);
        if (pts.getX() != 0.0) {
            txtX.setText(Util.doubleATexto(pts.getX(),2));
            txtY.setText(Util.doubleATexto(pts.getY(),2));
            txtZ.setText(Util.doubleATexto(pts.getZ(),2));
        }
        if (pts.getDes()>0) {
            txtDes.setText(Util.doubleATexto(pts.getDes(),4));
        }
        return view;
    }
}
