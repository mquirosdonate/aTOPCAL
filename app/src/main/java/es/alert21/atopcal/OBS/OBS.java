package es.alert21.atopcal.OBS;

import android.annotation.SuppressLint;

import java.io.Serializable;

import es.alert21.atopcal.Util;

public class OBS implements Serializable {
    private int id=0;
    private int ne=0,nv=0;
    private double h=0.0,v=0.0,d=0.0,m=0.0,i=0.0;
    private int raw =0 ;
    private int aparato=0;//generico
    public OBS(){}
    public OBS(int ne,int nv,double m,double i){
        this.ne = ne;
        this.nv = nv;
        this.m = m;
        this.i = i;
    }
    public int getId(){
        return id;
    }
    @SuppressLint("DefaultLocale")
    public String getIDtoString(){
        return String.format("%d",id);
    }
    public int getNe(){
        return ne;
    }
    public int getNv(){
        return nv;
    }
    public double getH(){
        return h;
    }
    public double getV(){
        return v;
    }
    public double getD(){
        return d;
    }
    public double getM(){
        return m;
    }
    public Double getI(){
        return i;
    }
    public int getRaw(){
        return raw;
    }
    public int getAparato(){
        return aparato;
    }

    @SuppressLint("DefaultLocale")
    public String getNEtoString(){
        return String.format("%5d",ne);
    }
    @SuppressLint("DefaultLocale")
    public String getNVtoString(){
        return String.format("%5d",nv);
    }
    public String getHtoString(){
        return Util.doubleATexto(h,3,4);
    }
    public String getVtoString(){
        return Util.doubleATexto(v,3,4);
    }
    public String getDtoString(){
        return Util.doubleATexto(d,1,3);
    }
    public String getMtoString(){
        return Util.doubleATexto(m,1,3);
    }
    public String getItoString(){
        return Util.doubleATexto(i,1,3);
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNe(int ne) {
        this.ne = ne;
    }
    public void setNe(String s) {
        try{
            this.ne = Integer.parseInt(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setNv(int nv) {
        this.nv = nv;
    }
    public void setNv(String s) {
        try{
            this.nv = Integer.parseInt(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setD(double d) {
        this.d = d;
    }
    public void setD(String s) {
        try{
            this.d = Double.parseDouble(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setH(double h) {
        this.h = normaliza(h);
    }
    public void setH(String s) {
        try{
            double x = Double.parseDouble(s);
            this.h = normaliza(x);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setV(double v) {
        this.v = normaliza(v);
    }
    public void setV(String s) {
        try{
            double x = Double.parseDouble(s);
            this.v = normaliza(x);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setI(double i) {
        this.i = i;
    }
    public void setI(String s) {
        try{
            this.i = Double.parseDouble(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setM(double m) {
        this.m = m;
    }
    public void setM(String s) {
        try{
            this.m = Double.parseDouble(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setRaw(int raw) {
        this.raw = raw;
    }
    public void setRaw(String s) {
        try{
            this.raw = Integer.parseInt(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setAparato(int aparato) {
        this.aparato = aparato;
    }
    public void setAparato(String s) {
        try{
            this.aparato = Integer.parseInt(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString(){
        return getNEtoString() + " " +
                getNVtoString() + " " +
                getHtoString() + " " +
                getVtoString() + " " +
                getDtoString() + " " +
                getMtoString() + " " +
                getItoString() ;
    }
    @SuppressLint("DefaultLocale")
    public String toXML(){
        return String.format("<obs ne='%d' ",ne) +
                String.format("nv='%d'>\n",nv) +
                String.format("<h>%.4f</h>\n",h) +
                String.format("<v>%.4f</v>\n",v) +
                String.format("<d>%.3f</d>\n",d) +
                String.format("<m>%.3f</m>\n",m) +
                String.format("<i>%.3f</i>\n</obs>\n",i) ;
    }
    public boolean isCD(){
        return v > 0.001 && v < 199.999;
    }
    public double normaliza(double x){
        while (x < 0) x += 400;
        while (x > 400) x -= 400;
        return x;
    }
}
