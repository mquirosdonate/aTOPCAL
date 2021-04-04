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
            double RT = 6378137;
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
