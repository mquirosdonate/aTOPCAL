package es.alert21.atopcal.PTS;

import android.annotation.SuppressLint;

import java.io.Serializable;

import es.alert21.atopcal.Util;

public class PTS implements Serializable {
    private int id=0;
    private int n=0;
    private double x=0.0,y=0.0,z=0.0,des=0.0;
    private int sref =0 ;
    private String nombre="";
    public PTS(){}
    public PTS(PTS pts){
        this.id = pts.id;
        this.n = pts.n;
        this.x = pts.x;
        this.y = pts.y;
        this.z = pts.z;
        this.des = pts.des;
        this.sref = pts.sref;
        this.nombre = pts.nombre;
    }
    public void setId(Integer id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    @SuppressLint("DefaultLocale")
    public String getIDtoString(){
        return String.format("%d",id);
    }
    public int getN(){
        return n;
    }
    @SuppressLint("DefaultLocale")
    public String getNtoString(){
        return String.format("%d",n);
    }
    public void setN(int n){
        this.n = n;
    }
    public void setN(String s){
        try{
            this.n = Integer.parseInt(s);
        }catch (Exception ignored){ }
    }
    public void setSref(int n){
        this.sref = n;
    }
    public void setSref(String s){
        try{
            this.sref = Integer.parseInt(s);
        }catch (Exception ignored){ }
    }
    public double getX() {
        return x;
    }
    public String getXtoString(){
        return Util.doubleATexto(x,1,3);
    }
    public void setX(double n){
        this.x = n;
    }
    public void setX(String s){
        try{
            this.x = Double.parseDouble(s);
        }catch (Exception ignored){ }
    }
    public double getY() {
        return y;
    }
    public String getYtoString(){
        return Util.doubleATexto(y,1,3);
    }
    public void setY(double n){
        this.y = n;
    }
    public void setY(String s){
        try{
            this.y = Double.parseDouble(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double getZ() {
        return z;
    }
    public String getZtoString(){
        return Util.doubleATexto(z,1,3);
    }
    public void setZ(double n){
        this.z = n;
    }
    public void setZ(String s){
        try{
            this.z = Double.parseDouble(s);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public double getDes() {
        return des;
    }
    public String getDestoString(){
        return Util.doubleATexto(des,3,4);
    }
    public void setDes(double n){
        this.des = normaliza(n);
    }
    public void setDes(String s){
        try{
            this.des = normaliza(Double.parseDouble(s));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setNombre(String n){
        nombre = n;
    }
    public String getNombre(){
        return nombre;
    }
    public String toString(){
        return getNtoString()+" "+
                getXtoString()+" "+
                getYtoString()+" "+
                getZtoString()+" "+
                getDestoString()+" "+
                getNombre() ;
    }
    @SuppressLint("DefaultLocale")
    public String toXML(){
        @SuppressLint("DefaultLocale") String s = String.format("<obs n='%d'>\n",n) +
                String.format("<x>%.3f</x>\n",x) +
                String.format("<y>%.3f</y>\n",y) +
                String.format("<z>%.3f</z>\n",z) ;
        if (des != 0.0)
            s += String.format("<des>%.4f</des>\n",des) ;
        if (!nombre.isEmpty())
            s += String.format("<nombre>%s</nombre>\n",nombre) ;
        return s;
    }
    private double normaliza(double x){
        while (x < 0) x += 400;
        while (x > 400) x -= 400;
        return x;
    }
    public String toStringTH(String titulo,boolean bDes){
        String s = "<tr><th colspan=\"10\">"+titulo+"</th></tr>";
        s += "<tr>";
        s += "<th>N</th>";
        s += "<th>X</th>";
        s += "<th>Y</th>";
        s += "<th>Z</th>";
        if (bDes)  s += "<th>Σ</th>";
        s += "<th>Nombre</th>";
        s += "</tr>";
        return s;
    }
    public String toStringTD(boolean bDes){
        String s = "<tr><td class=\"centrado\">" + n + "</td>";
        s += "<td>" + Util.doubleATexto(x,3) + "</td>";
        s += "<td>" + Util.doubleATexto(y,3) + "</td>";
        s += "<td>" + Util.doubleATexto(z,3) + "</td>";
        if (bDes) s += "<td>" + Util.doubleATexto(des,4) + "</td>";
        s += "<td class=\"centrado\">" + nombre + "</td></tr>";
        return s;
    }
}
