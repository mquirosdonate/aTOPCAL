package es.alert21.atopcal.HTML;

public class Util {
    public static String getHead(String titulo){
        String s = "<!DOCTYPE html><html lang=\"es\">"
                +"<head>"+
                "<meta charset=\"utf-8\">"+
                "<link href=\"imprimir.css\" rel=\"stylesheet\" media=\"print\">"+
                "<link href=\"w3.css\" rel=\"stylesheet\">"+
                "<title>"+titulo+
                "</title>"+
                "</head>"+
                "<body>";
        return s;
    }
    public static String getFinal(){
        String s ="</body>"+
                "</html>";
        return s;
    }

}
