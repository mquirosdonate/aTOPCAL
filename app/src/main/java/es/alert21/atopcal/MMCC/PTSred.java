package es.alert21.atopcal.MMCC;

import es.alert21.atopcal.PTS.PTS;

public class PTSred {
    private PTS p;
    private boolean fijoPlani = false;
    private boolean fijoAlti = false;
    public boolean valido = false;
    public int ix = 0;
    public int iy = 0;
    public int iz = 0;
    public int id = 0;

    public PTSred(PTS p,boolean fijoPlani,boolean fijoAlti){
        this.p = p;
        this.fijoAlti = fijoAlti;
        this.fijoPlani = fijoPlani;
    }
    public PTS getP(){
        return p;
    }
    public  boolean getFijoPlani(){
        return fijoPlani;
    }
    public  boolean getFijoAlti(){
        return fijoAlti;
    }
    public void setP(PTS p){
        this.p = p;
    }
    public void setFijoPlani(boolean fijoPlani){
        this.fijoPlani = fijoPlani;
    }
    public void setFijoAlti(boolean fijoAlti){
        this.fijoAlti = fijoAlti;
    }
}
