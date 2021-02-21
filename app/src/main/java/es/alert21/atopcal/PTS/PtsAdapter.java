package es.alert21.atopcal.PTS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.PRJ.PRJ;
import es.alert21.atopcal.R;

public class PtsAdapter extends ArrayAdapter<PTS> {
    List<PTS> ptsList;
    Context context;
    public PtsAdapter(Context context,  List<PTS>  ptsList){
        super(context, R.layout.list_prj, ptsList);
        this.ptsList = ptsList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_pts, null, false);
        PTS pts = ptsList.get(position);
        TextView txtNombre = view.findViewById(R.id.textViewPtsNombre);
        txtNombre.setText(pts.getNombre());
        TextView txtN = view.findViewById(R.id.listPtsN);
        txtN.setText(pts.getN().toString());
        TextView txtX = view.findViewById(R.id.textViewPtsX);
        txtX.setText(pts.getX().toString());
        TextView txtY = view.findViewById(R.id.textViewPtsY);
        txtY.setText(pts.getY().toString());
        TextView txtZ = view.findViewById(R.id.textViewPtsZ);
        txtZ.setText(pts.getZ().toString());
        TextView txtDes = view.findViewById(R.id.textViewPtsDes);
        if (pts.getDes()>0) {
            txtDes.setText(pts.getDes().toString());
        }
        return view;
    }
}
