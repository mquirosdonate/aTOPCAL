package es.alert21.atopcal.TOPO;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.Util;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class Visual {
    public double Az,Dr=0, Desnivel=0;
    public double Z =0;
    public PTS ne;
    public PTS nv;
    public OBS obs;
    double k;
    double RAD = PI / 200;

    public int neix = 0;
    public int neiy = 0;
    public int neiz = 0;
    public int neid = 0;
    public int nvix = 0;
    public int nviy = 0;
    public int nviz = 0;
    public int nvid = 0;
    public double ERR_AD = 0;
    public double ERR_A = 0;
    public double ERR_D1 = 0;
    public double ERR_D2 = 0;
    public double[] coef = new double[7];
    public int[] ncoef = new int[7];
    public int countCoef ;

    public Visual(){
    }
    public Visual(OBS obs, PTS ne,PTS nv,double k){
        this.ne = ne;
        this.nv = nv;

        this.k = k;
        setObs(obs);

        Az = ne.getDes() + obs.getH();
        if (Az > 400) Az -= 400;

        if (obs.getD() > 0 && obs.getV() > 0) {
            double Dg = obs.getD();
            double RT = 6378000.0;
            double incDes = 0.37*Dg*Dg/RT;
            double t = Dg * cos(obs.getV() * RAD) + incDes;
            Desnivel = t + obs.getI() - obs.getM();
            double DIV = 1;
            if (ne.getZ() > 0){
                Z = ne.getZ() + Desnivel;
                DIV = (1 + ne.getZ()/RT)*(1 + Z /RT);
            }else {
                Z = nv.getZ() - Desnivel;
                DIV = (1 + nv.getZ()/RT)*(1 + Z /RT);
            }

            Dr = sqrt((Dg*Dg-t*t)/DIV);
            Dr *= k;
            nv.setX( ne.getX() + Dr * sin(Az*RAD));
            nv.setY( ne.getY() + Dr * cos(Az*RAD));
            nv.setZ(Z);
        }
    }
    public void setObs(OBS obs){
        if(!obs.isCD()){
            if (obs.getH()>200) {
                obs.setH(obs.getH() - 200);
            } else {
                obs.setH(obs.getH() + 200);
            }
            obs.setV(400-obs.getV());
        }
        this.obs = obs;
    }
    public void ecuacion_observacion(int DIST,int DIRE,int ALTI,int SIMULACION,int incog_k){
        double x21, y21, z21, d21;
        double Md, peso_dist, peso_dire, peso_alti;
        double factor21;
        double angulo_o;

        x21 = nv.getX() - ne.getX();
        y21 = nv.getY() - ne.getY();
        z21 = nv.getZ() - ne.getZ();
        d21 = sqrt(x21 * x21 + y21 * y21);

        double erd = Math.atan(ERR_AD / d21 / 1000) * 200 / PI * 10000;
        double err_a = sqrt(erd * erd + ERR_A * ERR_A);

        if (err_a == 0) {
            peso_dist = 1;
            peso_dire = 1;
        }else {
            Md = ERR_D1 / 1000.0 + ERR_D2 * obs.getD() / 1000000.0;
            peso_dist = d21 / Md / 636620.0;
            peso_dire = 1.0 / err_a; // Raiz del peso
        }

        /*DISTANCIA*/
        if (DIST == 1 && obs.getD() > 0.00001 && (neix > 0 || nvix > 0)) {
            factor21 = 636620.0 / d21 / d21;
            countCoef = 4;
            coef[0] = -(x21 * factor21);
            coef[1] = -(y21 * factor21);
            coef[2] = (x21 * factor21);
            coef[3] = (y21 * factor21);
            if (1 == SIMULACION) {
                coef[4] = 0;
            }else {
                //reduce_distancia(obs, datos.coor + ie, datos.coor + iv);
                factor21 *= d21;
                if (incog_k > 0) {
                    coef[4] = -(Dr * factor21);
                    ncoef[4] = incog_k - 1;
                    countCoef++;
                }
                coef[countCoef] = (d21 - Dr) * factor21;
            }
            ncoef[0] = neix - 1;
            ncoef[1] = neiy - 1;
            ncoef[2] = nvix - 1;
            ncoef[3] = nviy - 1;
        }
    }
    public String toString(boolean todo){
        String s = "<tr><td class=\"centrado\">" + ne.getNtoString() + "</td>";
        s += "<td class=\"centrado\">" + nv.getNtoString() + "</td>";
        s += "<td>" + Util.doubleATexto(obs.getH(),4) + "</td>";
        s += "<td>" + Util.doubleATexto(obs.getV(),4) + "</td>";
        s += "<td>" + Util.doubleATexto(obs.getD(),3) + "</td>";
        s += "<td>" + Util.doubleATexto(obs.getM(),3) + "</td>";
        s += "<td>" + Util.doubleATexto(obs.getI(),3) + "</td>";
        s += "<td>" + Util.doubleATexto(Az,4) + "</td>";
        s += "<td>" + Util.doubleATexto(Dr,3) + "</td>";
        s += "<td>" + Util.doubleATexto(Desnivel,3) + "</td>";
        if (todo){
            s += "<td>"+Util.doubleATexto(nv.getX(),3) +"</td>";
            s += "<td>"+Util.doubleATexto(nv.getY(),3) +"</td>";
            s += "<td>"+Util.doubleATexto(nv.getZ(),3) +"</td>";
        }
        s += "</tr>\n";
        return s;
    }
    public String toStringTH(boolean todo){
        String s = "<tr><th colspan=\"10\">VISUALES</th></tr>";
        s += "<tr>";
        s += "<th>NE</th>";
        s += "<th>NV</th>";
        s += "<th>H</th>";
        s += "<th>V</th>";
        s += "<th>Dg</th>";
        s += "<th>M</th>";
        s += "<th>I</th>";
        s += "<th>Az</th>";
        s += "<th>Dr</th>";
        s += "<th>Desnivel</th>";
        if (todo){
            s += "<th>-X-</th>";
            s += "<th>-Y-</th>";
            s += "<th>-Z-</th>";
        }
        s += "</tr>\n";
        return s;
    }
}
