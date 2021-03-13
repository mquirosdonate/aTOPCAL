package es.alert21.atopcal.EPSG;
//(EPSG Dataset coordinate operation method code 9807)
//UTM North hemisphere
//Areas used: World wide equator to 84ºN
//Latitude of natural origin: Always 0º
//Longitude of natural origin: 6º intervals E of 177ºW
//CM Scale Factor: Always 0.9996
//Zone width: Always 6º
//False Easting: 500000m
//False Northing: 0m

import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

//UTM South hemisphere
//Areas used: World wide north of 80ºS to equator
//Latitude of natural origin: Always 0º
//Longitude of natural origin: 6º intervals E of 177ºW
//CM Scale Factor: Always 0.9996
//Zone width: Always 6º
//False Easting: 500000m
//False Northing: 10000000m
public class UTM {
    double FE = 500000.0; // False easting
    double FN = 0.0; // False northing en hemisferio Norte
    double FS = 10000000.0; // False northing en hemisferio Sur
    double k0 = 0.9996;

    Elipsoide elip;
    public double X,Y;
    public double gamma;

    public UTM(Elipsoide elipsoide){
        this.elip = elipsoide;
    }

    public void GEO2UTM(double lat, double lon,int huso){
        // Make sure the longitude is between -180.00 .. 179.9
        lon = lon + 180 - (int)((lon+180)/360)*360-180;
        double RAD =  Math.PI / 180;
        lat *= RAD;
        lon *= RAD;
        double T = tan(lat)* tan(lat);
        double T2 = T*T;
        double cosLat = cos(lat);
        double sinLat = sin(lat);
        double C = elip.e_2 * cosLat * cosLat / (1 - elip.e_2);
        double lon0 = ((huso - 30) * 6 - 3) * RAD;
        double A = (lon - lon0) * cosLat;
        double A2 = A*A;
        double A3 = A2*A;
        double A4 = A3*A;
        double A5 = A4*A;
        double A6 = A5*A;
        double N =  elip.a/sqrt(1 - elip.e_2*sinLat*sinLat);
        double M = getM(lat);

        X = FE + k0*N*(A+(1-T+C)*A3/6 + (5 - 18*T + T2 + 72*C - 58*elip.e1_2)*A5/120);
        Y = FN + k0*(M + N* tan(lat)*(A2/2 + (5-T+9*C+4*C*C)*A4/24 +
                (61-58*T+T2+600*C-330*elip.e1_2 )*A6/720));
        if (lat < 0){
            Y += FS;
        }

        gamma = atan(tan(lon-lon0)*sin(lat)) / RAD;
    }
    private double getM(double lat){

        return elip.a*((1-elip.e_2/4 - 3*elip.e_4/64 - 5*elip.e_6/256)*lat
                       - (3*elip.e_2/8 + 3*elip.e_4/32 + 45*elip.e_6/1024)* sin(2*lat)
                      + (15*elip.e_4/256 + 45*elip.e_6/1024)* sin(4*lat)
                      -  (35*elip.e_6/3072)* sin(6*lat));
    }

}
