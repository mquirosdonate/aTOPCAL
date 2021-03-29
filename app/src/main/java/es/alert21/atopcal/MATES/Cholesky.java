package es.alert21.atopcal.MATES;

import static java.lang.Math.sqrt;

public class Cholesky {
    double[] N;
    double[] X;
    double[] T;
    int n;
    /* ============================================================== */
    /* CHOLESKY    inversi¢n de matrices y soluci¢n */
    /*  N(n,n) es la matriz de los coeficientes, se vuelve inversa */
    /*  N  solo contiene la matriz triangular superior */
    /*  T(n) es el vector de constantes */
    /*  X(n) es el vector de las soluciones del sistema */
    /*  n     numero de ecuaciones */
    /* ============================================================== */
    public Cholesky(double[] N,double[] X,double[] T,int n){
        this.N = N;
        this.X = X;
        this.T = T;
        this.n = n;
    }

    public int invierte_matriz_cholesky()
    {
        int i,j,k;
        int e1,e2;
        int col,fil;
        double sum;

        if (N[0] <= 0) {
        //sprintf(szCadena,"La incognita 1 no tiene suficientes observaciones");
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
        return(0);
    }

}
