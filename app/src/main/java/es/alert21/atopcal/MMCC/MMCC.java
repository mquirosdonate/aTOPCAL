package es.alert21.atopcal.MMCC;

import java.util.ArrayList;
import java.util.List;

import es.alert21.atopcal.BBDD.Topcal;
import es.alert21.atopcal.EPSG.Elipsoide;
import es.alert21.atopcal.EPSG.UTM;
import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;
import es.alert21.atopcal.TOPO.Topo;
import es.alert21.atopcal.Util;

import static es.alert21.atopcal.TOPO.Topo.normaliza;
import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

public class MMCC {
    StringBuilder html = new StringBuilder();
    private CFG cfg;
    private double RAD = PI / 200;
    double[]N;
    double[]X;
    double[]T;

    int nIncognitas = 0;  /* número de incognitas */
    int nIncogXY = 0;
    int nIncogZ = 0;
    int nIncogDes = 0;
    int nObsd = 0,nObsh = 0,nObsa = 0;
    int nfijosXY = 0;
    int nfijosZ = 0;

    private Elipsoide elipsoide = new Elipsoide();
    private es.alert21.atopcal.EPSG.UTM utm = new UTM(elipsoide);

    //Para los puntos de la red
    public List<PTSred> redList = new ArrayList<>();
    private List<Double> kred = new ArrayList<>();//Coeficientes de anamorfosis de cada punto

    //Para las observaciones de la red
    public List<OBS> obsList = new ArrayList<>();
    public MMCC(){}

    public void Run(){
        Topcal topcal = Util.getTopcal();
        cfg = topcal.getRED_CFG();
        preparaDatos();
        pideMemoria();
        ecuaciones_observacion();
        if (cfg.LIST_NOR > 0)
            imprime_matriz(N,T,nIncognitas,1);

        if (0 == invierte_matriz_cholesky()){
            //Se pudo invertir la matriz y tenemos los resultados en X
            if (cfg.LIST_NOR > 0)
                imprime_matriz(N,T,nIncognitas,8);
            guardar_vertices();
        }

    }

    private void preparaDatos() {
        nIncognitas = 0;
        nIncogXY = 0;
        nIncogZ = 0;
        nIncogDes = 0;
        nfijosXY = 0;
        nfijosZ = 0;
        nObsd = nObsh = nObsa = 0;
        kred.clear();

        //Calcula los coeficientes de anamorfosis lineal en los puntos
        for (PTSred pred : redList) {
            Double k = utm.utmgeo(pred.getP().getX(), pred.getP().getY());
            kred.add(k);
        }

        //Sustituir los números de estación y visado por sus índices
        //en la lista de puntos
        for (OBS o : obsList) {
            int ine = getPTS(o.getNe());
            int inv = getPTS(o.getNv());
            if (ine < 0 || inv < 0) continue; //No puede pasar nunca
            o.setNe(ine);
            o.setNv(inv);

            if(!o.isCD()){
                if (o.getH()>200) {
                    o.setH(o.getH() - 200);
                } else {
                    o.setH(o.getH() + 200);
                }
                o.setV(400-o.getV());
            }

        }

        //Numeración de las incognitas
        if (cfg.SIMULACION > 0) cfg.incog_k = 0;
        if (cfg.DIST > 0) {
            if (cfg.incog_k > 0) nIncognitas = 1;
        } else {
            cfg.incog_k = 0;
        }

        for (int i = 0; i < redList.size(); i++) {
            redList.get(i).ix = 0;
            redList.get(i).iy = 0;
            redList.get(i).iz = 0;
            redList.get(i).id = 0;
            if (cfg.DIST > 0 || cfg.DIRE > 0) {
                if (redList.get(i).getFijoPlani()) {
                    nfijosXY++;
                } else {
                    redList.get(i).ix = ++nIncognitas;
                    redList.get(i).iy = ++nIncognitas;
                    nIncogXY += 2;
                }
            }
            if (cfg.ALTI > 0) {
                if (redList.get(i).getFijoAlti()) {
                    nfijosZ++;
                } else {
                    redList.get(i).iz = ++nIncognitas;
                    nIncogZ++;
                }
            }
        }
        //Incognitas de desorientación
        if (cfg.DIRE > 0) {
            for (int i = 0; i < redList.size(); i++) {
                PTS p = redList.get(i).getP();
                redList.get(i).id = ++nIncognitas;
                nIncogDes++;
                if (p.getDes()==0){
                    //No tiene desorientación
                    calculaDesorientacion(p);
                }
            }
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
        N = new double[(nIncognitas*nIncognitas + nIncognitas )/2];
        T = new double[nIncognitas];
        X = new double[nIncognitas];
    }
    private void calculaDesorientacion(PTS pe){
        Double des = 0.0;
        int n = 0;
        for(OBS o:obsList){
            PTS pv = redList.get(o.getNv()).getP();
            if (pe.getN() == pv.getN()){
                double azimut = Topo.Azimut2(pe.getX(),
                        pe.getY(),
                        pv.getX(),
                        pv.getY());
                azimut = normaliza(azimut );
                double desorientacion = Topo.desorientacion( azimut , o.getH());
                des += desorientacion;
                n++;
            }
        }
        if (n > 0) des /= n;
        pe.setDes(des);
    }
    private void ecuaciones_observacion(){
        int n;
        double x21, y21, z21, d21, az21;
        double[] coef = new double[8];
        int[] ncoef = new int[8];
        double factor21;
        double azimut_o;
        double Md, peso_dist, peso_dire;
        double peso_alti;
        double erd, err_a;
        for(OBS o:obsList){
            PTSred ne = redList.get(o.getNe());
            PTSred nv = redList.get(o.getNv());
            x21 = nv.getP().getX() - ne.getP().getX();
            y21 = nv.getP().getY() - ne.getP().getY();
            z21 = nv.getP().getZ() - ne.getP().getZ();
            d21 = sqrt(x21 * x21 + y21 * y21);

            erd = atan(cfg.ERR_AD / d21 / 1000) * 200 / PI * 10000;
            err_a = sqrt(erd * erd + cfg.ERR_A * cfg.ERR_A);

            if (err_a == 0) {
                peso_dist = 1;
                peso_dire = 1;
            } else {
                Md = cfg.ERR_D1 / 1000.0 + cfg.ERR_D2 * o.getD() / 1000000.0;
                peso_dist = d21 / Md / 636620.0;
                peso_dire = 1.0 / err_a; // Raiz del peso
            }

            /*DISTANCIA*/
            if (cfg.DIST > 0 && o.getD() > 0.00001 && (ne.ix > 0 || ne.ix > 0)) {
                factor21 = 636620.0 / d21 / d21;
                n = 4;
                coef[0] = -(x21 * factor21);
                coef[1] = -(y21 * factor21);
                coef[2] = (x21 * factor21);
                coef[3] = (y21 * factor21);
                if (cfg.SIMULACION > 0) {
                    coef[4] = 0;
                } else {
                    double dr = reduce_distancia(o);
                    factor21 *= d21;
                    if (cfg.incog_k > 0) {
                        coef[4] = -(dr * factor21);
                        ncoef[4] = cfg.incog_k - 1;
                        n++;
                    }
                    coef[n] = (d21 - dr) * factor21;
                }
                ncoef[0] = ne.ix - 1;
                ncoef[1] = ne.iy - 1;
                ncoef[2] = nv.ix - 1;
                ncoef[3] = nv.iy - 1;
                aporta_ecuacion_normal(ncoef, coef, n, peso_dist);
                nObsd++;
            }
            /* DIRECCION */
            if (cfg.DIRE > 0 && o.getH() >= 0.0) {
                factor21 = 636620.0 / d21 / d21;
                az21 = azimut(x21, y21);
                azimut_o = ne.getP().getDes() + o.getH();
                while (azimut_o < 0.0) azimut_o += 400;
                while (azimut_o > 400) azimut_o -= 400;
                ncoef[0] = ne.ix - 1;
                ncoef[1] = ne.iy - 1;
                ncoef[2] = nv.ix - 1;
                ncoef[3] = nv.iy - 1;
                ncoef[4] = ne.id - 1;
                coef[0] = -y21 * factor21;
                coef[1] = x21 * factor21;
                coef[2] = y21 * factor21;
                coef[3] = -x21 * factor21;
                coef[4] = -1;
                if (cfg.SIMULACION > 0) {
                    coef[5] = 0;
                } else {
                    coef[5] = az21 - azimut_o * 10000L;
                }
                aporta_ecuacion_normal(ncoef, coef, 5, peso_dire);
                nObsh++;
            }
            /* ALTIMETRIA */
            if (abs(o.getV()) < 0.000001) continue;
            if (ne.iz == 0 && nv.iz == 0) continue; // Dos puntos fijos
            d21 = subir_distancia(d21, o);
            if (cfg.ERR_K == 0) peso_alti = 1;
            else peso_alti = 1.0 / (cfg.ERR_K * d21 / 1000);
            ncoef[1] = ne.iz - 1;
            ncoef[0] = nv.iz - 1;

            coef[0] = 1;
            coef[1] = -1;
            if (cfg.SIMULACION > 0) {
                coef[2] = 0;
            } else {
                coef[2] = z21 - (d21 / tan(o.getV() * RAD) + o.getI() - o.getM() + (6.6E-8) * d21 * d21);
            }
            aporta_ecuacion_normal(ncoef, coef, 2, peso_alti);
            nObsa++;
        }
    }
    private double azimut(double xx,double yy){
        double az;
        if (xx==0 && yy==0) return(0);
        az=atan2(xx,yy);
        if (az < 0) az+=PI*2;
        return(az*2000000L/PI); /* AZIMUT EN SEGUNDOS */
    }
    private double reduce_distancia(OBS obs){
        double k, k1, k2, k3, desn;
        double z1, z2;
        double DR ;
        double RT = elipsoide.a;
        int ine = obs.getNe();
        int inv = obs.getNv();
        PTS pe = redList.get(ine).getP();
        PTS pv = redList.get(inv).getP();
        k1 = kred.get(ine);
        k3 = kred.get(inv);

        double xm = (pe.getX() + pv.getX())/2;
        double ym = (pe.getY() + pv.getY())/2;

        k2 = utm.utmgeo(xm, ym);

        k = (k1 + 4 * k2 + k3) / 6;

        desn = pv.getZ() - pe.getZ() + obs.getM() - obs.getI();

        z1 = 1 + pe.getZ() / RT;
        z2 = 1 + (pv.getZ() + obs.getM() -obs.getI()) / RT;

        DR = sqrt((obs.getD() * obs.getD() - desn * desn) / (z1 * z2));

        DR += (DR) * (DR) / RT * (DR) / RT / 24;

        DR *= k;

        //DR += DR * this.PPM / 1000000L;// ?????????????

        return DR;
    }
    private double subir_distancia(double dist,OBS obs){
        double RT = elipsoide.a;
        double k,k1,k2,k3;
        double z1,z2;
        int ine = obs.getNe();
        int inv = obs.getNv();
        PTS pe = redList.get(ine).getP();
        PTS pv = redList.get(inv).getP();
        k1 = kred.get(ine);
        k3 = kred.get(inv);

        double xm = (pe.getX() + pv.getX())/2;
        double ym = (pe.getY() + pv.getY())/2;

        k2 = utm.utmgeo(xm, ym);
        k = (k1 + 4 * k2 + k3) / 6;

        dist = dist / k;
        z1 = 1 + pe.getZ() / RT;
        z2 = 1 + pv.getZ() / RT;

        dist = sqrt( dist * dist * z1 * z2);
        return dist;
    }
    private void aporta_ecuacion_normal(int[] ncoef,double[] coef,int n, double peso){
        /* Los ncoef de los fijos son negativos */
        int m1,m2;
        int i,j,l;
        for (m1 = 0; m1 < n; m1++) {
            if (ncoef[m1] < 0) continue ;
            i = ncoef[m1];
            T[i] -= coef[m1] * coef[n] * peso * peso;
            for (m2 = m1; m2 < n; m2++) {
                if (ncoef[m2] < 0) continue ;
                j = ncoef[m2];
                if (j < i) {
                    l = i + j*nIncognitas - j*(j+1)/2;
                } else {
                    l = j + i*nIncognitas - i*(i+1)/2;
                }
                N[l] += coef[m1]*coef[m2] * peso * peso;
            }
        }
    }
    private void imprime_matriz(double[]N,double[]X,int nIncognitas,int dec){
        int i,j,l;
        html.append("<table><tbody>");
        for (i = 0; i < nIncognitas; i++) {
            html.append(String.format("<tr><td>fila '%2d'</td>",i));
            for (j = i; j < nIncognitas; j++) {
                l = j + i * nIncognitas - i * (i + 1) / 2;
                html.append("<td>");
                html.append(Util.doubleATexto(N[l],dec));
                html.append("</td>");
            }
            html.append("</tr>");
        }
        html.append("</tbody></table>");
        html.append("<table><tbody>");
        for (j = 0; j < nIncognitas; j++) {
            html.append("<tr><td>"+Util.doubleATexto(X[j],2)+"</td></tr>");
        }
        html.append("</tbody></table>");
    }
    void cab_vertice_aprox(){
        html.append("<table><tbody>");
        html.append("<tr><th colspan=\"9\"><COORDENADAS  APROXIMADAS</th></tr>\n");
        html.append("<tr><th>N</th><th>- X -</th><th>- Y -</th><th>- Z -</th><th>dΣ</th><th>dx</th><th>dy</th><th>dz</th><th>NOMBRE</th></tr>\n");
    }
    void cab_vertice_comp() {
        html.append("<table><tbody>");
        html.append("<tr><th colspan=\"5\"><COORDENADAS  COMPENSADAS</th></tr>\n");
        html.append("<tr><th>N</th><th>- X -</th><th>- Y -</th><th>- Z -</th><th>NOMBRE</th></tr>\n");
    }

    private int invierte_matriz_cholesky(){
        int n = nIncognitas;
        int i,j,k;
        int e1,e2;
        int col,fil;
        double sum;

        if (N[0] <= 0) {
            //La incognita 1 no tiene suficientes observaciones
            return 1;
        }

        N[0] = sqrt(N[0]);

        // CALCULO DE LA MATRIZ TRIANGULAR   Q
        // calculo de la primera fila
        for (i = 1; i < n; i++) N[i] = N[i] / N[0];

        // calculo de las restantes filas
        for (i = 1; i < n; i++) {
            for (j = i; j < n; j++) {
                sum = 0.0;
                for (k = 0; k <= i-1 ; k++) {
                    e1 = i + k*n - k*(k+1)/2 ;
                    e2 = j + k*n - k*(k+1)/2 ;
                    sum += N[e1] * N[e2] ;
                }
                e1 = i + i*n - i*(i+1)/2 ;
                if (i == j) {
                    N[e1] = N[e1]-sum;
                    if (N[e1] <= 0) {
                        //sprintf(szCadena,"La incognita %ld no tiene suficientes observaciones", i+1);
                        return 1;
                    }
                    N[e1] = sqrt(N[e1] );
                } else {
                    e2 = j + i*n - i*(i+1)/2 ;
                    N[e2] = (N[e2] - sum) / N[e1] ;
                }
            }
        }

        //"CALCULO DE LA INVERSA DE Q"
        for (i = 0; i < n; i++) {
            e1 = i + i*n - i*(i+1)/2 ;
            N[e1] = 1.0 / N[e1];
            for (j = i+1; j < n; j++) {
                sum = 0.0;
                for (k = i; k <= j-1 ; k++) {
                    e1 = k + i*n - i*(i+1)/2 ;
                    e2 = j + k*n - k*(k+1)/2 ;
                    sum += N[e1] * N[ e2] ;
                }
                e1 = j + i*n - i*(i+1)/2 ;
                e2 = j + j*n - j*(j+1)/2 ;
                N[e1] = (-sum) / N[e2] ;
            }
        }

        //"CALCULO DE LA INVERSA DE N"
        for (i = 0; i < n; i++) {
            for (j = i; j < n; j++) {
                sum = 0.0;
                for (k = j; k < n; k++) {
                    e1 = k + i*n - i*(i+1)/2 ;
                    e2 = k + j*n - j*(j+1)/2 ;
                    sum += N[e1] * N[e2] ;
                }
                e1 = j + i*n - i*(i+1)/2 ;
                N[e1] = sum;
            }
        }

        // calculo de las incognitas
        for (i = 0; i < n; i++) {
            sum = 0.0;
            for (j = 0; j < n; j++) {
                if (i > j) {
                    col = j;
                    fil = i;
                } else {
                    col = i;
                    fil = j;
                }
                e1 = fil + col*n - col*(col+1)/2 ;
                sum += N[e1] * T[j];
            }
            X[i] = sum;
        }
        return 0;
    }
    private void guardar_vertices(){
        for (PTSred p:redList){
            if (p.ix > 0) {
                double x = p.getP().getX();
                x += X[p.ix-1];
                p.getP().setX(x);
            }
            if (p.iy > 0) {
                double y = p.getP().getY();
                y += X[p.iy-1];
                p.getP().setY(y);
            }
            if (p.iz > 0) {
                double z = p.getP().getZ();
                z += X[p.iz-1];
                p.getP().setZ(z);
            }
            if (p.id > 0) {
                double d = p.getP().getDes();
                d += X[p.id-1]/10000;
                p.getP().setDes(d);
            }
        }
    }
}
