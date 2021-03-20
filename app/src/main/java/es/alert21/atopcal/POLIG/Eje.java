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
    public class Poli{
        public double Az,Dr=0, Desnivel=0;
    }

    public Poli ejeDirecta = new Poli();
    public Poli ejeReciproca = new Poli();
    public OBS obsDirecta;
    public OBS obsReciproca;
    public PTS n1,n2;
    double RAD = PI / 200;
    public double DRM;
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
        utm.UTM2GEO(n1.getY(),n1.getX(),30);//Da igual el huso que pongamos
        double RT = elipsoide.a;


        ejeDirecta.Az = n1.getDes() + obsDirecta.getH();
        if (ejeDirecta.Az > 400) ejeDirecta.Az -= 400;

        ejeReciproca.Az = ejeDirecta.Az + 200;
        if (ejeReciproca.Az > 400) ejeReciproca.Az -= 400;
        n2.setDes(ejeReciproca.Az-obsReciproca.getH());

        if (obsDirecta.getD() > 0 && obsDirecta.getV() > 0) {
            ejeDirecta.Dr = obsDirecta.getD() * sin(obsDirecta.getV() * RAD);
        }
        if (obsReciproca.getD() > 0 && obsReciproca.getV() > 0){
            ejeReciproca.Dr = obsReciproca.getD()*sin(obsReciproca.getV()*RAD);
        }
        if (ejeDirecta.Dr == 0)
            ejeDirecta.Dr = ejeReciproca.Dr;

        if (ejeReciproca.Dr == 0)
            ejeReciproca.Dr = ejeDirecta.Dr;

        DRM = (ejeDirecta.Dr + ejeReciproca.Dr) / 2;

        //Reducción de la cuerda al arco: del orden de 10e-6
        double c4 = DRM*DRM*DRM/24/RT/RT;
        double incDes = 0.37*DRM*DRM/RT;
        double tReciproca = 0;
        double tDirecta = 0;
        if (obsDirecta.getV() > 0) {
            tDirecta = DRM / tan(obsDirecta.getV() * RAD) + incDes;
            ejeDirecta.Desnivel = tDirecta + obsDirecta.getI() - obsDirecta.getM();
        }

        if (obsReciproca.getV() > 0) {
            tReciproca = DRM / tan(obsReciproca.getV() * RAD) + incDes;
            ejeReciproca.Desnivel = tReciproca + obsReciproca.getI() - obsReciproca.getM();
        }

        if (ejeDirecta.Desnivel == 0){
            ejeDirecta.Desnivel = -ejeReciproca.Desnivel;
            tDirecta = ejeDirecta.Desnivel - obsDirecta.getI() + obsDirecta.getM();
        }

        if (ejeReciproca.Desnivel == 0) {
            ejeReciproca.Desnivel = -ejeDirecta.Desnivel;
            tReciproca = ejeReciproca.Desnivel - obsReciproca.getI() + obsReciproca.getM();
        }

        double Z2 = n1.getZ() + (ejeDirecta.Desnivel-ejeReciproca.Desnivel)/2;


        double DIV = (1 + n1.getZ()/RT)*(1 + Z2/RT);

        ejeDirecta.Dr = 0;
        if (obsDirecta.getD() > 0){
            ejeDirecta.Dr = sqrt((obsDirecta.getD()*obsDirecta.getD()-tDirecta*tDirecta)/DIV);
            ejeDirecta.Dr += c4;
            ejeDirecta.Dr *= utm.k;
        }

        ejeReciproca.Dr = 0;
        if (obsReciproca.getD() > 0){
            ejeReciproca.Dr = sqrt((obsReciproca.getD()*obsReciproca.getD()-tReciproca*tReciproca)/DIV);
            ejeReciproca.Dr += c4;
            ejeReciproca.Dr *= utm.k;
        }

        if (ejeDirecta.Dr == 0)
            ejeDirecta.Dr = ejeReciproca.Dr;

        if (ejeReciproca.Dr == 0)
            ejeReciproca.Dr = ejeDirecta.Dr;

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
