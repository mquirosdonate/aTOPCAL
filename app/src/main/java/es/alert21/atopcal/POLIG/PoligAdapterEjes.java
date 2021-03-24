package es.alert21.atopcal.POLIG;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class PoligAdapterEjes extends ArrayAdapter<Eje> {
    List<Eje> ejeList;
    Context context;
    int layout;
    public PoligAdapterEjes(Context context, List<Eje>  ejeList, int layout){
        super(context, layout, ejeList);
        this.ejeList = ejeList;
        this.context = context;
        this.layout = layout;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(layout, null, false);
        Eje eje = ejeList.get(position);
        TextView textViewEje12 = view.findViewById(R.id.textViewEje12);
        TextView textViewEje21 = view.findViewById(R.id.textViewEje21);
        textViewEje12.setText(eje.Directa.ne.getN()+"-"+eje.Reciproca.ne.getN());
        textViewEje21.setText(eje.Reciproca.ne.getN()+"-"+eje.Directa.ne.getN());

        TextView textViewAz12 = view.findViewById(R.id.textViewAz12);
        TextView textViewAz21 = view.findViewById(R.id.textViewAz21);
        textViewAz12.setText(Util.doubleATexto(eje.Directa.Az,4));
        textViewAz21.setText(Util.doubleATexto(eje.Reciproca.Az,4));

        TextView textViewDr12 = view.findViewById(R.id.textViewDr12);
        TextView textViewDr21 = view.findViewById(R.id.textViewDr21);
        textViewDr12.setText(Util.doubleATexto(eje.Directa.Dr,3));
        textViewDr21.setText(Util.doubleATexto(eje.Reciproca.Dr,3));

        TextView textViewDes12 = view.findViewById(R.id.textViewDes12);
        TextView textViewDes21 = view.findViewById(R.id.textViewDes21);
        textViewDes12.setText(Util.doubleATexto(eje.Directa.Desnivel,3));
        textViewDes21.setText(Util.doubleATexto(eje.Reciproca.Desnivel,3));
        return view;
    }
}
