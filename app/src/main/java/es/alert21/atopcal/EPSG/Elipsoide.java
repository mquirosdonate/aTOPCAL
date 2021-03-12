package es.alert21.atopcal.EPSG;

public class Elipsoide {
    public double a = 6378137; //semieje mayor WGS84
    public double b = 6356752.31424; //Semieje menor WGS84
    public double f;  //aplanamiento
    public double e;  //excentricidad
    public double e1; //segunda excentricidad

    public Elipsoide(){
        f = (a-b)/a;
        e = Math.sqrt(2*f-f*f);
        e1 = Math.sqrt(e*e/(1-e*e));

    }
    //radio de curvatura en el meridiano
    double getR(double latitud){//En radianes
        double e2 = e*e;
        double sin2 = Math.sin(latitud)*Math.sin(latitud);
        return  a*(1-e2)/ Math.pow(1-e2*sin2,1.5);
    }
    //radius of curvature in the prime vertical
    double getV(double latitud){
        double e2 = e*e;
        double sin2 = Math.sin(latitud)*Math.sin(latitud);
        return a/Math.sqrt(1-e2*sin2);
    }
}


