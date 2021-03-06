package es.alert21.atopcal.EPSG;


import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

//https://www.movable-type.co.uk/scripts/latlong-utm-mgrs.html


//(EPSG Dataset coordinate operation method code 9807)
//UTM North hemisphere
//Areas used: World wide equator to 84ºN
//Latitude of natural origin: Always 0º
//Longitude of natural origin: 6º intervals E of 177ºW
//CM Scale Factor: Always 0.9996
//Zone width: Always 6º
//False Easting: 500000m
//False Northing: 0m
//UTM South hemisphere
//Areas used: World wide north of 80ºS to equator
//Latitude of natural origin: Always 0º
//Longitude of natural origin: 6º intervals E of 177ºW
//CM Scale Factor: Always 0.9996
//Zone width: Always 6º
//False Easting: 500000m
//False Northing: 10000000m

//k = k0*(1 + (1 + C)*A^2/2 + (5-4*T + 42*C + 13*C^2-28*e'^2)*A^4/24
//+ (61-148*T+16*T^2)*A^6/720)

//Tambié se puede obtener en funcion de la X
//k = k0*(1 + (1 + e'^2 * Cos^2(lat))* X^2 / (2 * k0^2 * N^2))

//        utmgeo(coor_e->x, coor_e->y, &k1);
//        utmgeo(coor_v->x, coor_v->y, &k3);
//        utmgeo((coor_e->x + coor_v->x) / 2, (coor_e->y + coor_v->y) / 2, &k2);
//        k = (k1 + 4 * k2 + k3) / 6;

public class UTM {

    double FE = 500000.0; // False easting
    double FN = 0.0; // False northing en hemisferio Norte
    double FS = 10000000.0; // False northing en hemisferio Sur
    double k0 = 0.9996;

    Elipsoide elip;
    public double X,Y;
    public double gamma;
    public double k;
    public double N;
    public double Lat,Lon ;

    public UTM(Elipsoide elipsoide){
        this.elip = elipsoide;
    }

    public void GEO2UTM(double lat, double lon,int huso){
        // Make sure the longitude is between -180.00 .. 179.9
        lon = lon + 180 - (int)((lon+180)/360)*360-180;

        lat = toRadians(lat);
        lon = toRadians(lon);
        double T = getT(lat);

        double cosLat = cos(lat);

        double C = elip.e_2 * cosLat * cosLat / (1 - elip.e_2);
        double lon0 = LongOrigin(huso);
        lon0 = toRadians(lon0);

        double A = (lon - lon0) * cosLat;
        double A2 = A*A;
        double A3 = A2*A;
        double A4 = A3*A;
        double A5 = A4*A;
        double A6 = A5*A;
        N =  getN(lat);
        double M = getM(lat);
        double M0 = getM(0.0);
        X = FE + k0*N*(A+(1-T+C)*A3/6 + (5 - 18*T + T*T + 72*C - 58*elip.e1_2)*A5/120);
        Y = FN + k0*(M-M0 + N* tan(lat)*(A2/2 + (5-T+9*C+4*C*C)*A4/24 +
                (61-58*T+T*T+600*C-330*elip.e1_2 )*A6/720));
        if (lat < 0){
            Y += FS;
        }

        gamma = atan(tan(lon-lon0)*sin(lat)) ;
        gamma = toDegrees(gamma);

        //k = k0*(1 + (1 + C)*A2/2 + (5-4*T + 42*C + 13*C*C-28*elip.e1_2)*A4/24
        //    + (61-148*T+16*T*T)*A6/720);

        k = getK(lat,X);
    }


    public void UTM2GEO(double YUTM, double XUTM,int huso) {
        double e1 = (1 - sqrt(1 - elip.e_2)) / (1 + sqrt(1 - elip.e_2));
        double M = YUTM / k0;
        double mu = M / (elip.a * (1 - elip.e_2/4 - 3*elip.e_4/64 - 5*elip.e_6/256));
        double phi1Rad = mu + (3 * e1 / 2 - 27 * e1 * e1 * e1 / 32) * sin(2 * mu)
                + (21 * e1 * e1 / 16 - 55 * e1 * e1 * e1 * e1 / 32) * sin(4 * mu)
                + (151 * e1 * e1 * e1 / 96) * sin(6 * mu);

        double N1 = getN(phi1Rad);
        double T1 = getT(phi1Rad);
        double C1 = elip.e_2 * cos(phi1Rad) * cos(phi1Rad);
        double R1 = elip.a * (1 - elip.e_2) / pow(1 - elip.e_2 * sin(phi1Rad) * sin(phi1Rad), 1.5);
        double D = (XUTM - FE) / (N1 * k0);
        double LatRad = phi1Rad - (N1 * tan(phi1Rad) / R1) * (D * D / 2 - (5 + 3 * T1 + 10 * C1 - 4 * C1 * C1 - 9 * elip.e_2) * D * D * D * D / 24
                + (61 + 90 * T1 + 298 * C1 + 45 * T1 * T1 - 252 * elip.e_2 - 3 * C1 * C1) * D * D * D * D * D * D / 720);
        Lat = this.toDegrees(LatRad);
        Lon = (D - (1 + 2 * T1 + C1) * D * D * D / 6 + (5 - 2 * C1 + 28 * T1 - 3 * C1 * C1 + 8 * elip.e_2 + 24 * T1 * T1)
                * D * D * D * D * D / 120) / cos(phi1Rad);
        double lon0 = LongOrigin(huso);
        Lon = lon0 + toDegrees(Lon);

        k = getK(LatRad,XUTM);
        N = getN(LatRad);
    }

    private double getK(double LatRad,double X){
        X -= FE;
        double N =  getN(LatRad);
        return k0*(1 + (1 + elip.e1_2 * cos(LatRad) * cos(LatRad)) * X*X / (2 * k0*k0 * N*N));
    }
    private double getT(double lat){
        return tan(lat)* tan(lat);
    }
    public double getN(double lat){
        double sinLat = sin(lat);
        return   elip.a/sqrt(1 - elip.e_2*sinLat*sinLat);
    }
    private double getM(double lat){
        return elip.a*((1-elip.e_2/4 - 3*elip.e_4/64 - 5*elip.e_6/256)*lat
                - (3*elip.e_2/8 + 3*elip.e_4/32 + 45*elip.e_6/1024)* sin(2*lat)
                + (15*elip.e_4/256 + 45*elip.e_6/1024)* sin(4*lat)
                -  (35*elip.e_6/3072)* sin(6*lat));
    }
    private double toRadians(double grad){
        return grad * Math.PI / 180;
    }
    private double toDegrees (double rad) {
        return rad / Math.PI* 180;
    }
    private double LongOrigin(int huso){
       return  (huso - 30) * 6 - 3 ;
    }

    public double  utmgeo(double x,double y){
        double ep4,a0,c2,c4,c6,c8,c10,alfa ;
        double ypc,xpc,ti,fi,fiac,delfi,sinfi,cosfi,s2fi,c2fi,c4fi ;
        double tgfi,t2fi,t4fi,secfi,gn,vii,viii,d6,cix,z,e5 ;
        double xpc2,xpc3,xpc4,xpc5,xpc6,dm,d6a,d6b ;
        double cfi,e2,ep2;
        double yr;

        e2=1.0  - (1 - 1.0/elip.alfa)*(1 - 1.0/elip.alfa) ;
        ep2=1.0 / ((1 -1.0/elip.alfa)*(1 - 1.0/elip.alfa) ) - 1.0 ;
        ep4=ep2*ep2 ;

        a0=1+3*e2/4+45*pow(e2,2)/64+175*pow(e2,3)/256+11025*pow(e2,4)/16384+43659.0*pow(e2,5)/65536L;
        a0=a0*(1-e2);
        alfa=elip.a*a0;

        c2=3*e2/4+15*pow(e2,2)/16+525*pow(e2,3)/512+2205*pow(e2,4)/2048+72765.0*pow(e2,5)/65536L ;
        c2=(c2*elip.a*(1-e2)/2)/alfa;
        c4=15*pow(e2,2)/64+105*pow(e2,3)/256+2205*pow(e2,4)/4096+10395*pow(e2,5)/16384;
        c4=(c4*elip.a*(1-e2)/4)/alfa;
        c6=35*pow(e2,3)/512+315*pow(e2,4)/2048+31185*pow(e2,5)/131072L;
        c6=(c6*elip.a*(1-e2)/6)/alfa;
        c8=315*pow(e2,4)/16384+3465*pow(e2,5)/65536L;
        c8=(c8*elip.a*(1-e2)/8)/alfa;
        c10=639*pow(e2,5)/131072.0;
        c10=(c10*elip.a*(1-e2)/10)/alfa;
        ypc=y/.9996;
        xpc=(x-500000L)/.9996;
        ti=ypc/alfa;
        fi=ti;
        do
        {
            fiac=fi;
            fi=ti+c2*sin(2*fi)-c4*sin(4*fi)+c6*sin(6*fi)-c8*sin(8*fi)+c10*sin(10*fi);
            delfi=Math.abs(fiac-fi);
        } while((delfi-.00000000001E0)>0);
        sinfi=sin(fi);
        cosfi=cos(fi);
        s2fi=pow(sinfi,2);
        c2fi=pow(cosfi,2);
        c4fi=pow(c2fi,2);
        tgfi=sinfi/cosfi;
        t2fi=pow(tgfi,2);
        t4fi=pow(t2fi,2);
        secfi=1/cosfi;
        gn=elip.a/sqrt(1-e2*s2fi);
        vii=tgfi*(1+ep2*c2fi)*.5/pow(gn,2);
        viii=tgfi*(5+3*t2fi+6*ep2*c2fi-6*ep2*s2fi-3*ep4*c4fi-9*ep4*c2fi*s2fi)/(24*pow(gn,4));
        d6a=tgfi/pow(gn,3);
        d6b=(61+90*t2fi+45*t4fi+107*ep2*c2fi-162*ep2*s2fi-45*ep2*t2fi*s2fi)/(720*pow(gn,3));
        d6=d6a*d6b;
        cix=secfi/gn;
        z=secfi*(1+2*t2fi+ep2*c2fi)/(6*pow(gn,3));
        e5=secfi*(5+28*t2fi+24*t4fi+6*ep2*c2fi+8*ep2*s2fi)/(120*pow(gn,5));
        xpc2=xpc*xpc;
        xpc3=xpc2*xpc;
        xpc4=xpc3*xpc;
        xpc5=xpc4*xpc;
        xpc6=xpc5*xpc;
        yr=fi-vii*xpc2+viii*xpc4-d6*xpc6;
        dm=cix*xpc-z*xpc3+xpc5*e5;
        sinfi=sin((yr));
        cfi=cos((yr));
        c2fi=pow(cfi,2);
        c4fi=pow(cfi,4);
        k=.9996E0*(1.0E0+.5E0*dm*dm*(c2fi+ep2*c4fi));
        return k;
    }

}
