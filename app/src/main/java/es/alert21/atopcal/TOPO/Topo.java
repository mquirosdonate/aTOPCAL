package es.alert21.atopcal.TOPO;

public class Topo {
    public static double Azimut(double x1,double y1,double x2,double y2){
        double xx,yy;
        xx = x2-x1;
        yy = y2-y1;
        if (xx == 0.0 && yy == 0.0)
            return 0.0;
        if (yy == 0.0 && xx > 0.0)
            return 100.0;
        if (yy == 0.0 && xx < 0.0)
            return 300.0;
        return Math.atan2(xx,yy) * 200 / Math.PI;
    }
    public static double desorientacion(double obs1,double obs2){
        double des = obs1 - obs2;
        des = normaliza(des);
        if (des > 399)
            des = des - 400;
        return des;
    }
    public static double normaliza(double x){
        while (x < 0) x += 400;
        while (x > 400) x -= 400;
        return x;
    }
}
