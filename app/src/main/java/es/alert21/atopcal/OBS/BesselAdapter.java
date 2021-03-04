package es.alert21.atopcal.OBS;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class BesselAdapter extends ArrayAdapter<OBSx2> {
    List<OBSx2> obsList;
    Context context;
    public BesselAdapter(Context context, List<OBSx2> obsList){
        super(context, R.layout.list_bessel, obsList);
        this.obsList = obsList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.list_bessel, null, false);
        OBSx2 obsx2 = obsList.get(position);

        TextView txtNE_NV = view.findViewById(R.id.textViewNE_NV);
        txtNE_NV.setText(obsx2.getObsCD().getNe() + " " + obsx2.getObsCD().getNv());
        if (!obsx2.getValid()){
            txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            //txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            //txtNE_NV.setPaintFlags(txtNE_NV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }


        TextView txtCD_H = view.findViewById(R.id.listObsCD_H);
        TextView txtCI_H = view.findViewById(R.id.listObsCI_H);
        txtCD_H.setText(obsx2.getObsCD().getHtoString());
        txtCI_H.setText(obsx2.getObsCI().getHtoString());

        TextView txtCD_V = view.findViewById(R.id.listObsCD_V);
        TextView txtCI_V = view.findViewById(R.id.listObsCI_V);
        txtCD_V.setText(obsx2.getObsCD().getVtoString());
        txtCI_V.setText(obsx2.getObsCI().getVtoString());

        TextView txtCD_D = view.findViewById(R.id.listObsCD_D);
        TextView txtCI_D = view.findViewById(R.id.listObsCI_D);
        txtCD_D.setText(obsx2.getObsCD().getDtoString());
        txtCI_D.setText(obsx2.getObsCI().getDtoString());

        TextView txtCD_M = view.findViewById(R.id.listObsCD_M);
        TextView txtCI_M = view.findViewById(R.id.listObsCI_M);
        txtCD_M.setText(obsx2.getObsCD().getMtoString());
        txtCI_M.setText(obsx2.getObsCI().getMtoString());

        TextView txtCD_I = view.findViewById(R.id.listObsCD_I);
        TextView txtCI_I = view.findViewById(R.id.listObsCI_I);
        txtCD_I.setText(obsx2.getObsCD().getItoString());
        txtCI_I.setText(obsx2.getObsCI().getItoString());

        TextView txtErrHz = view.findViewById(R.id.errHz);
        TextView txtErrV = view.findViewById(R.id.errV);
        TextView txtErrD = view.findViewById(R.id.errD);
        txtErrHz.setText(Util.doubleATexto(obsx2.errorHorizontal(),4));
        txtErrV.setText(Util.doubleATexto(obsx2.errorVertical(),4));
        txtErrD.setText(Util.doubleATexto(obsx2.errorDistancia(),3));
        return view;
    }
}