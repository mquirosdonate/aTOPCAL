package es.alert21.atopcal.PTS;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import es.alert21.atopcal.OBS.OBSx2;
import es.alert21.atopcal.R;
import es.alert21.atopcal.Util;

public class EstacionAdapter extends ArrayAdapter<PTV_OBS> {
    List<PTV_OBS> ptsList;
    Context context;
    public EstacionAdapter(@NonNull Context context, List<PTV_OBS> ptsList) {
        super(context, R.layout.list_prj, ptsList);
        this.ptsList = ptsList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.list_estacion, null, false);
        PTV_OBS ptv_obs = ptsList.get(position);

        TextView txtNV = view.findViewById(R.id.listObsNV);
        TextView txtH = view.findViewById(R.id.ListObsH);
        TextView txtAz = view.findViewById(R.id.listAz);
        TextView txtDes = view.findViewById(R.id.listDes);
        txtNV.setText(ptv_obs.obs.getNVtoString());
        txtH.setText(ptv_obs.obs.getHtoString());
        txtAz.setText(Util.doubleATexto(ptv_obs.azimut,4));
        txtDes.setText(Util.doubleATexto(ptv_obs.desorientacion,4));

        if (!ptv_obs.valid){
            txtNV.setPaintFlags(txtNV.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtH.setPaintFlags(txtH.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtAz.setPaintFlags(txtAz.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            txtDes.setPaintFlags(txtDes.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            txtNV.setPaintFlags(txtNV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            txtH.setPaintFlags(txtH.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            txtAz.setPaintFlags(txtAz.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            txtDes.setPaintFlags(txtDes.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        return view;
    }
}
