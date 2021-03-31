package es.alert21.atopcal.MMCC;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.EPSG.Elipsoide;
import es.alert21.atopcal.EPSG.UTM;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;

public class MMCC {
    public int LIST_OBS =  1;
    public int LIST_NOR =  1;
    public int UTM      =  1;
    public int DIRE     =  1;
    public int DIST     =  1;
    public int ALTI     =  1;
    public int COMPENSACION = 1;
    public int SIMULACION   = 1;
    public int TEST_OBS     = 1;

    double[]N;
    int nN;
    double[]X;
    double[]T;

    int nIncognitas = 0;  /* número de incognitas */
    int nIncogxy = 0;
    int nIncogz = 0;
    int nIncogdes = 0;
    int nObsd = 0,nObsh = 0,nObsa = 0;
    int nfijosxy = 0;
    int nfijosz = 0;
    int incog_k ;     /* incognita de escala */
    int  ERR_A,ERR_AD,ERR_D1,ERR_D2;
    float ERR_K;
    int PPM;
    String szPPM = "?";

    private Elipsoide elipsoide = new Elipsoide();
    private es.alert21.atopcal.EPSG.UTM utm = new UTM(elipsoide);

    //Para los puntos de la red
    public List<PTSred> redList = new ArrayList<>();
    private List<Double> kred = new ArrayList<Double>();//Coeficientes de anamorfosis de cada punto

    //Para las observaciones de la red
    public List<OBS> obsList = new ArrayList<OBS>();


    private void preparaDatos() {
        nIncognitas  = 0;
        nIncogxy = 0;
        nIncogz = 0;
        nIncogdes = 0;
        nfijosxy = 0;
        nfijosz = 0;
        nObsd=nObsh=nObsa=0 ;
        kred.clear();

        for (PTSred pred:redList){
            Double k = utm.utmgeo(pred.getP().getX(),pred.getP().getY());
            kred.add(k);
        }

        for (OBS o : obsList) {
            int ine = getPTS(o.getNe());
            int inv = getPTS(o.getNv());
            if (ine < 0 || inv < 0) continue; //No puede pasar nunca
            o.setNe(ine);
            o.setNv(inv);
        }


    }


    private int getPTS(int n){
        for (int i = 0;i < redList.size();i++){
            if (n == redList.get(i).getP().getN())
                return i;
        }
        return -1;
    }
    private void pideMemoria(){
        nN = (nIncognitas*nIncognitas + nIncognitas ) / 2;
        N = new double[nN];
        T = new double[nIncognitas];
        X = new double[nIncognitas];
    }
}
