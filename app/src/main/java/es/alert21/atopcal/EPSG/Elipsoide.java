package es.alert21.atopcal.EPSG;

public class Elipsoide {
    public double a = 6378137; //semieje mayor WGS84
    public double b = 6356752.31424; //Semieje menor WGS84
    public double f;  //aplanamiento
    public double e;  //excentricidad
    public double e_2;  //excentricidad e^2
    public double e_4;  //excentricidad e^4
    public double e_6;  //excentricidad e^6
    public double e1; //segunda excentricidad
    public double e1_2; //segunda excentricidad e'^2

    public Elipsoide(){
        f = (a-b)/a;
        e = Math.sqrt(2*f-f*f);
        e1 = Math.sqrt(e*e/(1-e*e));
        e_2 = e*e;
        e_4 = e_2 * e_2;
        e_6 = e_4 * e_2;
        e1_2 = e1 * e1;
    }
}


