package es.alert21.atopcal.POLIG;

import es.alert21.atopcal.EPSG.Elipsoide;
import es.alert21.atopcal.EPSG.UTM;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
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
    double DRM;
    private Elipsoide elipsoide = new Elipsoide();
    private UTM utm;

    public Eje(OBS directa, OBS reciproca, PTS n1,PTS n2){
        this.n1 = n1;
        this.n2 = n2;
        utm = new UTM(elipsoide);

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
        ejeDirecta.Az = n1.getDes() + obsDirecta.getH();
        if (ejeDirecta.Az > 400) ejeDirecta.Az -= 400;

        ejeReciproca.Az = ejeDirecta.Az + 200;
        if (ejeReciproca.Az > 400) ejeReciproca.Az -= 400;
        n2.setDes(ejeReciproca.Az-obsReciproca.getH());

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
        DRM = (ejeDirecta.Dr + ejeReciproca.Dr) / 2;

        double tDirecta = ejeDirecta.Dr / tan(obsDirecta.getV()*RAD);
        double tReciproca = ejeReciproca.Dr / tan(obsReciproca.getV()*RAD);
        double incDes = 0.000000066*DRM*DRM;
        //incDes = 0.0;
        ejeDirecta.Desnivel = tDirecta + obsDirecta.getI() - obsDirecta.getM() + incDes;
        ejeReciproca.Desnivel = tReciproca + obsReciproca.getI() - obsReciproca.getM() + incDes;

        double Z2 = n1.getZ() + (ejeDirecta.Desnivel-ejeReciproca.Desnivel)/2;

        utm.UTM2GEO(n1.getY(),n1.getX(),30);

        double RT = elipsoide.a;
        double DIV = (1 + n1.getZ()/RT)*(1 + Z2/RT);

        if (obsDirecta.getD() == 0){
            ejeDirecta.Dr = sqrt((obsReciproca.getD()*obsReciproca.getD()-tReciproca*tReciproca)/DIV);
        } else {
            ejeDirecta.Dr = sqrt((obsDirecta.getD()*obsDirecta.getD()-tDirecta*tDirecta)/DIV);
        }
        double c4 = ejeDirecta.Dr*ejeDirecta.Dr*ejeDirecta.Dr/24/utm.N/utm.N;
        ejeDirecta.Dr += c4;
        ejeDirecta.Dr *= utm.k;

        if (obsReciproca.getD() == 0){
            ejeReciproca.Dr = sqrt((obsDirecta.getD()*obsDirecta.getD()-tDirecta*tDirecta)/DIV);
        } else {
            ejeReciproca.Dr = sqrt((obsReciproca.getD()*obsReciproca.getD()-tReciproca*tReciproca)/DIV);
        }
        ejeReciproca.Dr += c4;

        //double k = getK(n1.getX());
        ejeReciproca.Dr *= utm.k;

        DRM = (ejeDirecta.Dr + ejeReciproca.Dr) / 2;

        double X2 = n1.getX() + DRM * sin(ejeDirecta.Az*RAD);
        double Y2 = n1.getY() + DRM * cos(ejeDirecta.Az*RAD);

        n2.setX(X2);
        n2.setY(Y2);
        n2.setZ(Z2);
    }
    private double getK(double XUTM){
        //Para XUTM = 433267.61
        //Resultado con esta formula      0.99965486
        //Resultado utilizando la Latitud 0.999654815
        double Q = (XUTM - 500000 ) / 1000000;
        return 0.9996*(1+.012325*Q*Q);
    }
}
