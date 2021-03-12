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

//UTM South hemisphere
//Areas used: World wide north of 80ºS to equator
//Latitude of natural origin: Always 0º
//Longitude of natural origin: 6º intervals E of 177ºW
//CM Scale Factor: Always 0.9996
//Zone width: Always 6º
//False Easting: 500000m
//False Northing: 10000000m
public class GEO2UTM {
    double FE = 500000; // False easting
    double FN = 0; // False northing en hemisferio Norte
}
