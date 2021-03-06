package es.alert21.atopcal.PTS;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.TOPO.Topo;

public class Estacion extends OBS{
    PTS estacion;
    public List<PTV_OBS> obsList = new ArrayList<>();

    public Estacion(PTS pte){
        estacion = pte;
    }

    public void addVisado(PTV_OBS ptv_obs){
        if(ptv_obs.obs.getNe() == estacion.getN()) {
            ptv_obs.azimut = Topo.Azimut(ptv_obs.vis.getX(),estacion.getX(),ptv_obs.vis.getY(),estacion.getY());
            ptv_obs.desorientacion = Topo.desorientacion( ptv_obs.azimut , ptv_obs.obs.getH());
            obsList.add(ptv_obs);
        }
    }
    public void addVisados(List<PTV_OBS> ptv_obsList){
        for(PTV_OBS ptv_obs:ptv_obsList){
            addVisado(ptv_obs);
        }
    }
}
