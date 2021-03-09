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
                obsDirecta.setH(obs.getH() - 200);
            } else {
                obsDirecta.setH(obs.getH() + 200);
            }
            obsDirecta.setV(400-obs.getV());
        }

    }
    public void setObsReciproca(OBS obs){
        if(!obs.isCD()){
            if (obs.getH()>200) {
                obsReciproca.setH(obs.getH() - 200);
            } else {
                obsReciproca.setH(obs.getH() + 200);
            }
            obsReciproca.setV(400-obs.getV());
        }
    }
    private void setDistancias(OBS directa,OBS reciproca){
        if (directa.getD()==0)
            directa.setD(reciproca.getD());
        if (reciproca.getD()==0)
            reciproca.setD(directa.getD());
    }
}
