package es.alert21.atopcal.PRJ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.R;

public class PrjAdapter extends ArrayAdapter<PRJ> {
    List<PRJ> prjList;
    Context context;

    public PrjAdapter(Context context,  List<PRJ> prjList){
        super(context, R.layout.list_prj, prjList);
        this.prjList = prjList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_prj, null, false);
        PRJ prj = prjList.get(position);
        TextView txtNombre = view.findViewById(R.id.listPrjNombre);
        TextView txtTitulo = view.findViewById(R.id.ListPrjTitulo);
        TextView txtDescripcion = view.findViewById(R.id.listPrjDescripcion);
        txtNombre.setText(prj.getNombre());
        txtTitulo.setText(prj.getTitulo());
        txtDescripcion.setText(prj.getDescripcion());
        return view;
    }
}