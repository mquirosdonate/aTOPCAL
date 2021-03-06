package es.alert21.atopcal.PRJ;

import java.io.Serializable;

public class PRJ implements Serializable {
    private Integer id=0;
    private String nombre="",titulo="",descripcion="";
    public PRJ(String nombre,String titulo,String descripcion){
        setNombre(nombre);
        setTitulo(titulo);
        setDescripcion(descripcion);
    }
    public PRJ(){}
    public String getNombre(){
        return nombre;
    }
    public String getTitulo(){
        return titulo;
    }
    public String getDescripcion(){
        return descripcion;
    }
    public void setNombre(String nombre){
        this.nombre = nombre.trim().toUpperCase();;
    }
    public void setTitulo(String titulo){
        if (titulo.isEmpty())titulo=this.nombre;
        this.titulo = titulo.trim();
    }
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion.trim();
    }
    public void setId(Integer id){
        this.id = id;
    }
    public Integer getId(){
        return id;
    }
    public String toString(){
        String s = nombre +"\n" + titulo + "\n" + descripcion;
        return s;
    }
}
