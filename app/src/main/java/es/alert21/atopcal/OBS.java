package es.alert21.atopcal;

public class OBS {
    int ne,nv;
    double h,v,d,m,i;
    int raw =0 ,aparato=0;
    public OBS(int ne,double m,double i){
        this.ne = ne;
        this.m = m;
        this.i = i;
    }
    public int getNe(){
        return ne;
    }
    public int getNv(){
        return nv;
    }
    public int getRaw(){
        return raw;
    }
    public int getAparato(){
        return aparato;
    }
}
