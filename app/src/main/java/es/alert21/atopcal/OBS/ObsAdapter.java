package es.alert21.atopcal.OBS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.R;

public class ObsAdapter extends ArrayAdapter<OBS> {
    List<OBS> obsList;
    //activity context
    Context context;

    public ObsAdapter(Context context,  List<OBS> obsList){
        super(context, R.layout.list_obs, obsList);
        this.obsList = obsList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_obs, null,false);
        //getting obs of the specified position
        OBS obs = obsList.get(position);
        TextView ne = view.findViewById(R.id.listObsNe);
        TextView nv = view.findViewById(R.id.listObsNv);
        TextView h = view.findViewById(R.id.listObsH);
        TextView v = view.findViewById(R.id.listObsV);
        TextView d = view.findViewById(R.id.listObsD);
        TextView m = view.findViewById(R.id.listObsM);
        TextView i = view.findViewById(R.id.listObsI);
        ne.setText(obs.getNe().toString());
        nv.setText(obs.getNv().toString());
        h.setText(obs.getH().toString());
        v.setText(obs.getV().toString());
        d.setText(obs.getD().toString());
        m.setText(obs.getM().toString());
        i.setText(obs.getI().toString());
        return view;
    }
}
