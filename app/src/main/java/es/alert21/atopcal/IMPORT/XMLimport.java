package es.alert21.atopcal.IMPORT;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import es.alert21.atopcal.OBS.OBS;
import es.alert21.atopcal.PTS.PTS;

public class XMLimport {
    String mFileXML;
    static  StringBuilder xml = new StringBuilder();
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    List<PTS> listPts = new ArrayList<>();
    List<OBS> listObs = new ArrayList<>();
    public XMLimport(String xmlfile){
        xml.setLength(0);
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        Document documento = null;
        mFileXML = xmlfile;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            documento = builder.parse(new File( xmlfile ));
        }catch (Exception e){    }
        creaKeycodeList(documento);
    }
    private void creaKeycodeList(Document documento){
        Element root = documento.getDocumentElement();
        NodeList listaDeNodos = documento.getElementsByTagName(root.getNodeName());
        Node nodoRaiz = listaDeNodos.item(0);
        String TAG = getTAGyAtributos(nodoRaiz);
        if(TAG.compareToIgnoreCase("observaciones")==0) {
            List hijos = dameNodosHijos(nodoRaiz);
            for(Object hijo:hijos){
                List keycodes = dameNodosHijos((Node)hijo);
                String orden = getTAGyAtributos((Node)hijo).toLowerCase();
                for(Object keycode:keycodes){
                    Node nodo = (Node)keycode;
                    String tag = getTAGyAtributos(nodo);
                    String value = getTextoDelNodo(nodo);
                    if(tag.compareToIgnoreCase("keyCode")==0){
//                        keyCodesList.add(new KeyCode(orden,Integer.parseInt(value)));
                    }
                }
            }
        }
        if(TAG.compareToIgnoreCase("puntos")==0) {
            List hijos = dameNodosHijos(nodoRaiz);
            for(Object hijo:hijos){
                List keycodes = dameNodosHijos((Node)hijo);
                String orden = getTAGyAtributos((Node)hijo).toLowerCase();
                for(Object keycode:keycodes){
                    Node nodo = (Node)keycode;
                    String tag = getTAGyAtributos(nodo);
                    String value = getTextoDelNodo(nodo);
                    if(tag.compareToIgnoreCase("keyCode")==0){
//                        keyCodesList.add(new KeyCode(orden,Integer.parseInt(value)));
                    }
                }
            }
        }

    }
    private String getTAGyAtributos(Node nodo){
        String TAG = nodo.getNodeName();
        xml.append("<"+TAG);
        if (nodo.hasAttributes()) {
            NamedNodeMap nodoMap = nodo.getAttributes();
            for (int i = 0; i < nodoMap.getLength(); i++) {
                Node tempNodo = nodoMap.item(i);
                String nombreAtributo = tempNodo.getNodeName();
                String valorAtributo = tempNodo.getNodeValue();
                xml.append(" " +nombreAtributo+ "=\"" + valorAtributo+"\"");
            }
        }
        xml.append(">");
        return TAG;
    }
    private List dameNodosHijos(Node nodoPadre){
        List listaDeNodosHijos = new ArrayList() ;
        NodeList listaDeNodos = nodoPadre.getChildNodes();
        for (int i = 0; i < listaDeNodos.getLength(); i++) {
            Node nodo = listaDeNodos.item(i);
            if (nodo.getNodeType() != Node.ELEMENT_NODE) continue;
            listaDeNodosHijos.add(nodo);
        }
        return listaDeNodosHijos;
    }
    private String getTextoDelNodo(Node node) {
        String txt = "";
        NodeList list = node.getChildNodes();
        StringBuilder textContent = new StringBuilder();
        for (int i = 0; i < list.getLength(); ++i) {
            Node child = list.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                txt = child.getTextContent();
                txt = txt.replace("\n", " ");
                if (txt.trim().length() > 0)  textContent.append(txt);
            }
        }
        txt = textContent.toString().trim();
        return txt;
    }
}
