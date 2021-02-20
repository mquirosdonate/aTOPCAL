package es.alert21.atopcal.PTS;

import java.io.Serializable;

public class PTS implements Serializable {
    private Integer id=0;
    private Integer n=0;
    private Double x=0.0,y=0.0,z=0.0,des=0.0;
    private Integer sref =0 ;
    private String nombre="";
    public PTS(){}
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return id;
    }
    public Integer getN(){
        return n;
    }
    public void setN(Integer n){
        this.n = n;
    }
    public void setN(String s){
        try{
            this.n = Integer.parseInt(s);
        }catch (Exception e){ }
    }
    public void setSref(Integer n){
        this.sref = n;
    }
    public void setSref(String s){
        try{
            this.sref = Integer.parseInt(s);
        }catch (Exception e){ }
    }
    public Double getX() {
        return x;
    }
    public void setX(Double n){
        this.x = n;
    }
    public void setX(String s){
        try{
            this.x = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public Double getY() {
        return y;
    }
    public void setY(Double n){
        this.y = n;
    }
    public void setY(String s){
        try{
            this.y = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public Double getZ() {
        return z;
    }
    public void setZ(Double n){
        this.z = n;
    }
    public void setZ(String s){
        try{
            this.z = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public Double getDes() {
        return des;
    }
    public void setDes(Double n){
        this.des = n;
    }
    public void setDes(String s){
        try{
            this.des = Double.parseDouble(s);
        }catch (Exception e){ }
    }
    public void setNombre(String n){
        nombre = n;
    }
    public String getNombre(){
        return nombre;
    }
    public String toString(){
        String s = String.format("%5d ",n) +
                String.format("%.3f ",x) +
                String.format("%.3f ",y) +
                String.format("%.3f ",z) +
                String.format("%.4f ",des) +
                String.format("%s",nombre) ;
        return s;
    }
}
