package es.alert21.atopcal.DESORIENTACION;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.TOPO.Topo;

import static es.alert21.atopcal.TOPO.Topo.normaliza;

public class Estacion extends OBS{
    PTS estacion;
    public List<PTV_OBS> obsList = new ArrayList<>();

    public Estacion(PTS pte){
        estacion = pte;
    }

    public void addVisado(PTV_OBS ptv_obs){
        if(ptv_obs.obs.getNe() == estacion.getN()) {
            ptv_obs.azimut = Topo.Azimut2(estacion.getX(),
                    estacion.getY(),
                    ptv_obs.vis.getX(),
                    ptv_obs.vis.getY());
            ptv_obs.azimut = normaliza(ptv_obs.azimut );

            double H = ptv_obs.obs.getH();
            if (!ptv_obs.obs.isCD()) H += 200;

            ptv_obs.desorientacion = Topo.desorientacion( ptv_obs.azimut , H);
            obsList.add(ptv_obs);
        }
    }
    public void addVisados(List<PTV_OBS> ptv_obsList){
        for(PTV_OBS ptv_obs:ptv_obsList){
            addVisado(ptv_obs);
        }
    }
}
