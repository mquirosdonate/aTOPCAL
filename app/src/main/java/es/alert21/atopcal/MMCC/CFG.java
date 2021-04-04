package es.alert21.atopcal.MMCC;

public class CFG {
    public int id = 0;
    public int COMPENSACION = 1;
    public int SIMULACION   = 0;
    public int TEST_OBS     = 0;
    public int DIRE     =  1;
    public int DIST     =  1;
    public int ALTI     =  1;
    public int LIST_OBS =  1;
    public int LIST_NOR =  1;
    public int ERR_A=15;    //Error angular en segundos
    public int ERR_AD=10;   //Error angular en mm
    public int ERR_D1=15;   //Error distancias en mm
    public int ERR_D2=5;    //Error distancias en ppm
    public double ERR_K=0.05; //Error altimétrico kilometrico metro por raiz de km
    public int incog_k = 1 ;     // incognita de escala
}
