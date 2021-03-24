package es.alert21.atopcal.POLIG;

import es.alert21.atopcal.EPSG.Elipsoide;
import es.alert21.atopcal.EPSG.UTM;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.TOPO.Visual;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Eje {
    public Visual Directa;
    public Visual Reciproca;

    double RAD = PI / 200;
    public double DRM;
    private Elipsoide elipsoide = new Elipsoide();
    private UTM utm;

    public Eje(OBS directa, OBS reciproca, PTS n1,PTS n2){
        utm = new UTM(elipsoide);
        utm.UTM2GEO(n1.getY(),n1.getX(),30);//Da igual el huso que pongamos
        Directa = new Visual(directa,n1,n2,utm.k);
        Reciproca = new Visual(reciproca,n2,new PTS(n1),utm.k);
        setPoli();
    }


    private void setPoli(){
        Reciproca.Az = Directa.Az + 200;
        if (Reciproca.Az > 400) Reciproca.Az -= 400;
        Reciproca.ne.setDes(Reciproca.Az-Reciproca.obs.getH());

        if (Directa.Dr == 0)
            Directa.Dr = Reciproca.Dr;

        if (Reciproca.Dr == 0)
            Reciproca.Dr = Directa.Dr;

        DRM = (Directa.Dr + Reciproca.Dr) / 2;

        if (Directa.Desnivel == 0){
            Directa.Desnivel = -Reciproca.Desnivel;
        }

        if (Reciproca.Desnivel == 0) {
            Reciproca.Desnivel = -Directa.Desnivel;
        }

        double Z2 = Directa.ne.getZ() + (Directa.Desnivel-Reciproca.Desnivel)/2;
        double X2 = Directa.ne.getX() + DRM * sin(Directa.Az*RAD);
        double Y2 = Directa.ne.getY() + DRM * cos(Directa.Az*RAD);

        Reciproca.ne.setX(X2);
        Reciproca.ne.setY(Y2);
        Reciproca.ne.setZ(Z2);
    }
    private double getK(double XUTM){
        //Para XUTM = 433267.61
        //Resultado con esta formula      0.99965486
        //Resultado utilizando la Latitud 0.999654815
        double Q = (XUTM - 500000 ) / 1000000;
        return 0.9996*(1+.012325*Q*Q);
    }
}
