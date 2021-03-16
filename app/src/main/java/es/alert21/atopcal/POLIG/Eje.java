package es.alert21.atopcal.POLIG;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.tan;

public class Eje {
    private class Poli{
        public double Az,Dr, Desnivel,Cf;
    }
    private Poli ejeDirecta = new Poli();
    private Poli ejeReciproca = new Poli();
    private OBS obsDirecta;
    private OBS obsReciproca;
    private PTS n1,n2;
    double RAD = PI / 200;

    public Eje(OBS directa, OBS reciproca, PTS n1,PTS n2){
        this.n1 = n1;
        this.n2 = n2;

        setObsDirecta(directa);
        setObsReciproca(reciproca);
        setPoli();
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
    private void setPoli(){
        if (obsDirecta.getD() == 0){
            ejeDirecta.Dr = obsReciproca.getD()*sin(obsReciproca.getV()*RAD);
        } else {
            ejeDirecta.Dr = obsDirecta.getD()*sin(obsDirecta.getV()*RAD);
        }
        if (obsReciproca.getD() == 0){
            ejeReciproca.Dr = obsDirecta.getD()*sin(obsDirecta.getV()*RAD);
        } else {
            ejeReciproca.Dr = obsReciproca.getD()*sin(obsReciproca.getV()*RAD);
        }
        double DRM = (ejeDirecta.Dr + ejeReciproca.Dr) / 2;
        double incDes = 0.000000066*DRM*DRM;
        ejeDirecta.Desnivel = ejeDirecta.Dr / tan(obsDirecta.getV()*RAD) + obsDirecta.getI() - obsDirecta.getM() + incDes;
        ejeReciproca.Desnivel = ejeReciproca.Dr / tan(obsReciproca.getV()*RAD) + obsReciproca.getI() - obsReciproca.getM() + incDes;

        ejeDirecta.Az = n1.getDes() + obsDirecta.getH();
        if (ejeDirecta.Az > 400) ejeDirecta.Az -= 400;

        ejeReciproca.Az = ejeDirecta.Az + 200;
        if (ejeReciproca.Az > 400) ejeReciproca.Az -= 400;

        n2.setDes(ejeReciproca.Az-obsReciproca.getH());
        double X2 = n1.getX() + DRM * sin(ejeDirecta.Az*RAD);
        double Y2 = n1.getY() + DRM * cos(ejeDirecta.Az*RAD);
        double Z2 = n1.getZ() + (ejeDirecta.Desnivel-ejeReciproca.Desnivel)/2;
        n2.setX(X2);
        n2.setY(Y2);
        n2.setZ(Z2);
    }
}
