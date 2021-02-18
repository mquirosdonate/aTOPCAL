package es.alert21.atopcal.OBS;

import java.io.Serializable;

import es.alert21.atopcal.Util;

public class OBS implements Serializable {
    private Integer id=0;
    private Integer ne=0,nv=0;
    private Double h=0.0,v=0.0,d=0.0,m=0.0,i=0.0;
    private Integer raw =0 ;
    private Integer aparato=0;//generico
    public OBS(){}
    public OBS(int ne,int nv,double m,double i){
        this.ne = ne;
        this.nv = nv;
        this.m = m;
        this.i = i;
    }
    public Integer getId(){
        return id;
    }
    public Integer getNe(){
        return ne;
    }
    public Integer getNv(){
        return nv;
    }
    public Double getH(){
        return h;
    }
    public Double getV(){
        return v;
    }
    public Double getD(){
        return d;
    }
    public Double getM(){
        return m;
    }
    public Double getI(){
        return i;
    }
    public Integer getRaw(){
        return raw;
    }
    public Integer getAparato(){
        return aparato;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public void setAparato(Integer aparato) {
        this.aparato = aparato;
    }
    public void setAparato(String s) {
        try{
            this.aparato = Integer.parseInt(s);
        }catch (Exception e){ }
    }
    public void setD(Double d) {
        this.d = d;
    }
    public void setD(String s) {
        try{
            this.d = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public void setH(Double h) {
        this.h = h;
    }
    public void setH(String s) {
        try{
            this.h = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public void setI(Double i) {
        this.i = i;
    }
    public void setI(String s) {
        try{
            this.i = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public void setM(Double m) {
        this.m = m;
    }
    public void setM(String s) {
        try{
            this.m = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public void setNe(Integer ne) {
        this.ne = ne;
    }
    public void setNe(String s) {
        try{
            this.ne = Integer.parseInt(s);
        }catch (Exception e){ }
    }
    public void setNv(Integer nv) {
        this.nv = nv;
    }
    public void setNv(String s) {
        try{
            this.nv = Integer.parseInt(s);
        }catch (Exception e){ }
    }
    public void setRaw(Integer raw) {
        this.raw = raw;
    }
    public void setRaw(String s) {
        try{
            this.raw = Integer.parseInt(s);
        }catch (Exception e){ }
    }
    public void setV(Double v) {
        this.v = v;
    }
    public void setV(String s) {
        try{
            this.v = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public String toString(){
        String s = String.format("%5d ",ne) +
                String.format("%5d ",nv) +
                String.format("%.4f ",h) +
                String.format("%.4f ",v) +
                String.format("%.3f ",d) +
                String.format("%.3f ",m) +
                String.format("%.3f",i) ;
        return s;
    }
}