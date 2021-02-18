package es.alert21.atopcal.OBS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import es.alert21.atopcal.R;

public class NEAdapter extends ArrayAdapter<Integer> {
    List<Integer> neList;
    Context context;

    public NEAdapter(Context context, List<Integer> neList){
        super(context, R.layout.list_obs, neList);
        this.neList = neList;
        this.context = context;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_obs, null,false);
        //getting obs of the specified position
        TextView ne = view.findViewById(R.id.listObsNE);
        ne.setText(neList.get(position).toString());
        return view;
    }
}
