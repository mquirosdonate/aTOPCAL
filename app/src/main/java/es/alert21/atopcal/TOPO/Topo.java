package es.alert21.atopcal.TOPO;

public class Topo {
    public static double Azimut(double x1,double y1,double x2,double y2){
        double xx,yy;
        double az = 0.0;
        xx = x2-x1;
        yy = y2-y1;
        if (xx == 0.0 && yy == 0.0)
            return 0.0;
        if (yy == 0.0 && xx > 0.0)
            return 100.0;
        if (yy == 0.0 && xx < 0.0)
            return 300.0;
        az = Math.atan(xx/yy) * 200 / Math.PI;
        //az = Math.atan2(yy,xx) * 200 / Math.PI;
        if (az < 0.0) az += 400;
        return az;
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
