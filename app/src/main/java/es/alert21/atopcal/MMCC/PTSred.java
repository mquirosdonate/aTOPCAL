package es.alert21.atopcal.MMCC;

import es.alert21.atopcal.PTS.PTS;

public class PTSred {
    private PTS n;
    private boolean fijoPlani = false;
    private boolean fijoAlti = false;
    public boolean valido = false;

    public PTSred(PTS n,boolean fijoPlani,boolean fijoAlti){
        this.n = n;
        this.fijoAlti = fijoAlti;
        this.fijoPlani = fijoPlani;
    }
    public PTS getN(){
        return n;
    }
    public  boolean getFijoPlani(){
        return fijoPlani;
    }
    public  boolean getFijoAlti(){
        return fijoAlti;
    }
    public void setN(PTS n){
        this.n = n;
    }
    public void setFijoPlani(boolean fijoPlani){
        this.fijoPlani = fijoPlani;
    }
    public void setFijoAlti(boolean fijoAlti){
        this.fijoAlti = fijoAlti;
    }
}
