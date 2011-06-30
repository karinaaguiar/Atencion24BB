package com.atencion24.control;

import java.io.ByteArrayInputStream;
import java.util.Vector;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

public class XMLParser {
	
	String error = "";

    public XMLParser() {}
    
    public String obtenerError(){
        return this.error;    
    }
    
    public Document PreprocesarXML(String xmlSource) 
    {
    	 try 
         {   
    		 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder builder = factory.newDocumentBuilder();
	         ByteArrayInputStream stream = new ByteArrayInputStream(xmlSource.getBytes());
	         Document documento = builder.parse(stream);
	         return documento;
         }     
	     catch ( Exception e ) {return null;}
	}
    
    public String extraerCapaWebService( String xmlSource ){
           
            String result;
            Document documento = PreprocesarXML(xmlSource);
            Element rootElement = documento.getDocumentElement();
            rootElement.normalize();
            NodeList nodoDatos = rootElement.getChildNodes();
            Node Child = nodoDatos.item(0); 
            //Sustituimos los caracteres '[' ']' para obtener nuestro nuevo XML
            result = Child.getNodeValue().replace('[', '<');
            return result.replace(']', '>');     
    }


    public Sesion LeerXMLInicio(String xmlSource) {        
    	Sesion usu = new Sesion();
    	try 
        {   
            String nombre;
            String apellido;
            String CodigoMedico;
            String nombreUsuario;
            
        	Document documento = PreprocesarXML(xmlSource);
        	Element elemento = documento.getDocumentElement();
        	elemento.normalize();
        	String tag = elemento.getNodeName();
        	System.out.println(tag);
        	
        	if(tag.equals("error"))
        	{
        		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
        			this.error = "Clave inválida";
        		if(elemento.getChildNodes().item(0).getNodeValue().equals("1"))	
        			this.error = "El nombre de usuario que ingresó no existe";
                return null;
        	}	

        	System.out.println(elemento.getChildNodes().getLength());
            NodeList nodoUsuario = elemento.getChildNodes();
            nombre = nodoUsuario.item(0).getChildNodes().item(0).getNodeValue(); 
            System.out.println(nodoUsuario.item(0).getNodeName());
            System.out.println(nombre);
            apellido = nodoUsuario.item(1).getChildNodes().item(0).getNodeValue(); 
            System.out.println(nodoUsuario.item(1).getNodeName());
            System.out.println(apellido);
            CodigoMedico = nodoUsuario.item(2).getChildNodes().item(0).getNodeValue(); 
            System.out.println(nodoUsuario.item(2).getNodeName());
            System.out.println(CodigoMedico);
            nombreUsuario = nodoUsuario.item(3).getChildNodes().item(0).getNodeValue(); 
            System.out.println(nodoUsuario.item(3).getNodeName());
            System.out.println(nombreUsuario);
            usu = new Sesion(nombre,apellido,CodigoMedico,nombreUsuario);
        } 
        catch ( Exception e ) 
        {
            System.out.println( "Error: "+e.toString() );
        }
		return usu;
    }
    
    //Por ahora cableado
    public Pago LeerProximoPago(String xmlSource) {        
    	Deduccion deduccion1 = new Deduccion("Gastos Administrativos A", "120");
    	Deduccion deduccion2 = new Deduccion("Arrendamiento Consultorios", "560");
    	Deduccion deduccion3 = new Deduccion("Descuento Laboratorio", "890");
    	Vector deducciones = new Vector();
    	deducciones.addElement(deduccion1);
    	deducciones.addElement(deduccion2);
    	deducciones.addElement(deduccion3);
    	Pago pago = new Pago("1200", deducciones, "150" , "15/06/2011");
    	
		return pago;
    }
    
    //Por ahora cableado
    public Vector LeerHistoricoPagos(String xmlSource) {        
    	Vector historicoPago = new Vector();
    	Deduccion deduccion1 = new Deduccion("Gastos Administrativos A", "120");
    	Deduccion deduccion2 = new Deduccion("Arrendamiento Consultorios", "560");
    	Deduccion deduccion3 = new Deduccion("Descuento Laboratorio", "890");
    	Vector deducciones = new Vector();
    	deducciones.addElement(deduccion1);
    	deducciones.addElement(deduccion2);
    	deducciones.addElement(deduccion3);
    	Pago pago1 = new Pago("1200", deducciones, "150" , "15/06/2011");
    	Pago pago2 = new Pago("3000", deducciones, "2000", "30/06/2011");
    	historicoPago.addElement(pago1);
    	historicoPago.addElement(pago2);
    	
		return historicoPago;
    }
    
    //Por ahora cableado
    public Vector LeerListadoCasos (String xmlSource) 
    {  
    	Vector listadoCasos = new Vector();
    	Caso caso1= new Caso("Ramírez Ariana", "25/07/2009", 0);
    	Caso caso2= new Caso("Ramírez Antonio", "15/05/2011", 1);
    	Caso caso3= new Caso("Ramírez Carla", "01/12/2010", 2);
    	listadoCasos.addElement(caso1);
    	listadoCasos.addElement(caso2);
    	listadoCasos.addElement(caso3);
    	return listadoCasos;
    }
}
