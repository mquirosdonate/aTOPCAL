package es.alert21.atopcal.MMCC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.POLIG.Eje;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class RedAdapter extends ArrayAdapter<PTSred> {
    List<PTSred> redList;
    Context context;
    int layout;
    public RedAdapter(Context context, List<PTSred>  redList, int layout){
        super(context, layout, redList);
        this.redList = redList;
        this.context = context;
        this.layout = layout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(layout, null, false);
        PTSred p = redList.get(position);

        TextView txtN = view.findViewById(R.id.listPtsN2);
        TextView txtX = view.findViewById(R.id.textViewPtsX2);
        TextView txtY = view.findViewById(R.id.textViewPtsY2);
        TextView txtZ = view.findViewById(R.id.textViewPtsZ2);
        TextView txtDes = view.findViewById(R.id.textViewPtsDes2);
        CheckBox csPlani = view.findViewById(R.id.checkBoxFijoPlani);
        CheckBox csAlti = view.findViewById(R.id.checkBoxFijoAlti);
        CheckBox csValido = view.findViewById(R.id.checkBoxValido);
        txtN.setText(p.getN().getNtoString());
        txtX.setText(Util.doubleATexto(p.getN().getX(),2));
        txtY.setText(Util.doubleATexto(p.getN().getY(),2));
        txtZ.setText(Util.doubleATexto(p.getN().getZ(),2));
        txtDes.setText(Util.doubleATexto(p.getN().getDes(),4));


        csPlani.setChecked(p.getFijoPlani());
        csAlti.setChecked(p.getFijoAlti());
        csValido.setChecked(p.valido);

        csPlani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redList.get(position).setFijoPlani(csPlani.isChecked());
                if (csPlani.isChecked()){
                    csValido.setChecked(true);
                    redList.get(position).valido = true;
                }
            }
        });

        csAlti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redList.get(position).setFijoAlti(csAlti.isChecked());
                if (csAlti.isChecked()){
                    csValido.setChecked(true);
                    redList.get(position).valido = true;
                }
            }
        });
        csValido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redList.get(position).valido = csValido.isChecked();
            }
        });

        return view;
    }
}
