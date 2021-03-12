package es.alert21.atopcal.POLIG;

import es.alert21.atopcal.OBS.OBS;

public class Eje {
    private OBS obsDirecta;
    private OBS obsReciproca;
    public Eje(OBS directa,OBS reciproca){
        setDistancias(directa,reciproca);
        setObsDirecta(directa);
        setObsReciproca(reciproca);
    }
    public void setObsDirecta(OBS obs){
        if(!obs.isCD()){
            if (obs.getH()>200) {
                obs.setH(obs.getH() - 200);
            } else {
                obs.setH(obs.getH() + 200);
            }
            obs.setV(400-obs.getV());
        }
        obsDirecta = obs;
    }
    public void setObsReciproca(OBS obs){
        if(!obs.isCD()){
            if (obs.getH()>200) {
                obs.setH(obs.getH() - 200);
            } else {
                obs.setH(obs.getH() + 200);
            }
            obs.setV(400-obs.getV());
        }
        obsReciproca = obs;
    }
    private void setDistancias(OBS directa,OBS reciproca){
        if (directa.getD()==0)
            directa.setD(reciproca.getD());
        if (reciproca.getD()==0)
            reciproca.setD(directa.getD());
    }
}
