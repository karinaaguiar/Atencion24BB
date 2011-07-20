package com.atencion24.control;

import java.io.ByteArrayInputStream;
import java.util.Hashtable;
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
            Vector CodigoMedico = new Vector();
            String nombreUsuario;
            
        	Document documento = PreprocesarXML(xmlSource);
        	Element elemento = documento.getDocumentElement();
        	elemento.normalize();
        	String tag = elemento.getNodeName();
        	System.out.println(tag);
        	
        	if(tag.equals("error"))
        	{
        		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
        			this.error = "Clave inv�lida";
        		if(elemento.getChildNodes().item(0).getNodeValue().equals("1"))	
        			this.error = "El nombre de usuario que ingres� no existe";
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
            
            CodigoPago codpago;
            codpago = new CodigoPago(nodoUsuario.item(2).getChildNodes().item(0).getNodeValue(), "Karina Aguiar");
            CodigoMedico.addElement(codpago);
            codpago = new CodigoPago("123", "Sociedad medica X");
            CodigoMedico.addElement(codpago);
            codpago = new CodigoPago("563", "Anestesiologos");
            CodigoMedico.addElement(codpago);      
            
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
    
    public Pago AuxiliarHonorariosPagados(NodeList nodoPago, Pago pago)
    {
    	Vector Deducciones = new Vector();
    	Deduccion deduccion = new Deduccion();
    	
        //Monto Liberado
        pago.setMontoLiberado(nodoPago.item(0).getChildNodes().item(0).getNodeValue());
        
        //Deducciones
        NodeList nodoDeducciones = nodoPago.item(1).getChildNodes();
        for( int i =0 ; i < nodoDeducciones.getLength(); i++ )
        {
        	NodeList nodoDeduccion = nodoDeducciones.item(i).getChildNodes();
        	deduccion.setConcepto(nodoDeduccion.item(0).getNodeValue());
        	deduccion.setMonto(nodoDeduccion.item(1).getNodeValue());
        	Deducciones.addElement(deduccion);
        }	
        pago.setDeducciones(Deducciones);
        
        //Monto Neto
        pago.setMontoNeto(nodoPago.item(2).getChildNodes().item(0).getNodeValue());
        
        //Fecha Pago 
        pago.setFechaPago(nodoPago.item(3).getChildNodes().item(0).getNodeValue());
        
		return pago;
    }
    
    /**
     * @param xmlSource
     * @return
     */
    public Pago LeerProximoPago(String xmlSource) 
    {        
    	Pago pago = new Pago();
    	
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	System.out.println(tag);
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "A�n no se han generado pagos a su nombre para la pr�xima n�mina";
            return null;
    	}	

    	NodeList nodoPago = elemento.getChildNodes();
        pago = AuxiliarHonorariosPagados(nodoPago, pago);
    	
		return pago;
        
        /*Deduccion deduccion1 = new Deduccion("Gastos Administrativos A", "120");
    	Deduccion deduccion2 = new Deduccion("Arrendamiento Consultorios", "560");
    	Deduccion deduccion3 = new Deduccion("Descuento Laboratorio", "890");
    	Vector deducciones = new Vector();
    	deducciones.addElement(deduccion1);
    	deducciones.addElement(deduccion2);
    	deducciones.addElement(deduccion3);
    	Pago pago = new Pago("1200", deducciones, "150" , "15/06/2011");*/
    }
    
    
    /**
     * @param xmlSource
     * @return
     */
    public Vector LeerHistoricoPagos(String xmlSource) 
    {        
    	Vector historicoPago = new Vector();
    	Pago pago = new Pago();
    	
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	System.out.println(tag);
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "No existen pagos en el rango de fechas indicado";
            return null;
    	}	

    	NodeList nodoPagos = elemento.getChildNodes();
    	
    	for( int i =0 ; i < nodoPagos.getLength(); i++ )
        {
    		NodeList nodoPago = nodoPagos.item(i).getChildNodes();
    		pago = AuxiliarHonorariosPagados(nodoPago, pago);
    		historicoPago.addElement(pago);
        }
    	
    	return historicoPago;
    	
        /*Deduccion deduccion1 = new Deduccion("Gastos Administrativos A", "120");
    	Deduccion deduccion2 = new Deduccion("Arrendamiento Consultorios", "560");
    	Deduccion deduccion3 = new Deduccion("Descuento Laboratorio", "890");
    	Vector deducciones = new Vector();
    	deducciones.addElement(deduccion1);
    	deducciones.addElement(deduccion2);
    	deducciones.addElement(deduccion3);
    	Pago pago1 = new Pago("1200", deducciones, "150" , "15/06/2011");
    	Pago pago2 = new Pago("3000", deducciones, "2000", "30/06/2011");
    	historicoPago.addElement(pago1);
    	historicoPago.addElement(pago2);*/
    	
    }
    
    //Por ahora cableado
    public Hashtable  LeerListadoCasos (String xmlSource) 
    {  
    	Hashtable listadoCasos = new Hashtable();
    	Caso caso1= new Caso("Ram�rez Ariana", "25/07/2009", "2009533", "4" );
    	Caso caso2= new Caso("Ram�rez Antonio", "15/05/2011", "2011932", "4");
    	Caso caso3= new Caso("Ram�rez Carla", "01/12/2010", "20103432", "4");
    	listadoCasos.put((Integer) new Integer(0), caso1);
    	listadoCasos.put((Integer) new Integer(1), caso2);
    	listadoCasos.put((Integer) new Integer(2), caso3);
    	return listadoCasos;
    }

	public Caso LeerCaso(String respuesta) {
		Hashtable honorarios = new Hashtable();
		Honorario honorario1 = new Honorario("Cirugia1", "2500", "1000", "500", "1000");
		Honorario honorario2 = new Honorario("Cirugia2", "1000", "0", "0", "1000");
		honorarios.put((Integer) new Integer(0),honorario1);
		honorarios.put((Integer) new Integer(1),honorario2);
		Caso caso = new Caso("Ram�rez Ariana", "25/07/2009", "2009533", "19162868", "Seguros Mercantil", "3500", "1000", "500", "2000", honorarios); 
		return caso;
	}

	public Hashtable LeerListadoFianzas(String respuesta) {
		Hashtable fianzas = new Hashtable();
		
		Vector descuentos = new Vector();
		Descuento descuento1 = new Descuento("30/09/2010", "300");
		Descuento descuento2 = new Descuento("15/10/2010", "200");
		descuentos.addElement(descuento1);
		descuentos.addElement(descuento2);
		Fianza fianza1 = new Fianza("20106921", "25/08/2010", "Gonz�lez Jes�s", "20.500" , "500", "20.000", descuentos);
		
		descuentos = new Vector();
		descuento1 = new Descuento("15/06/2009", "2000");
		descuento2 = new Descuento("30/06/2009", "500");
		descuentos.addElement(descuento1);
		descuentos.addElement(descuento2);
		Fianza fianza2 = new Fianza("20112321", "2/05/2009", "Gonz�lez Ana", "12.500" , "2.500", "10.000", descuentos);

		fianzas.put(new Integer(0), fianza1);
		fianzas.put(new Integer(1), fianza2);
		return fianzas;
	}

	/**
	 * @param respuesta
	 * @return
	 */
	public EstadoCuentaAS LeerEstadoCtaAntiguedadSaldo(String xmlSource) 
	{
		EstadoCuentaAS edocta = new EstadoCuentaAS();
        
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "La Cl�nica actualmente no tiene deudas con usted";
            return null;
    	}	

    	NodeList nodoEstadoCtaAS = elemento.getChildNodes();
    	
    	//Deuda A 30 d�as 
        edocta.setA30dias(nodoEstadoCtaAS.item(0).getChildNodes().item(0).getNodeValue()); 
        
        //Deuda a 60 d�as 
        edocta.setA60dias(nodoEstadoCtaAS.item(1).getChildNodes().item(0).getNodeValue());
        
        //Deuda a 90 d�as 
        edocta.setA90dias(nodoEstadoCtaAS.item(2).getChildNodes().item(0).getNodeValue());
        
        //Deuda a 180 d�as 
        edocta.setA180dias(nodoEstadoCtaAS.item(3).getChildNodes().item(0).getNodeValue());
        
        //Deuda a m�s 180 d�as 
        edocta.setMas180(nodoEstadoCtaAS.item(4).getChildNodes().item(0).getNodeValue());
        
        //Deuda total 
        edocta.setTotalDeuda(nodoEstadoCtaAS.item(5).getChildNodes().item(0).getNodeValue());
        
		return edocta;
	}

	public Vector LeerHonorariosFacturados(String respuesta) 
	{
		Vector resumenFacturado = new Vector();
		String hospitalizacion = "8.000";
		String emergencia = "1500";
		String cirugia = "500";
		String convenios = "0";
		String total = "20.000";
		resumenFacturado.addElement(hospitalizacion);
		resumenFacturado.addElement(emergencia);
		resumenFacturado.addElement(cirugia);
		resumenFacturado.addElement(convenios);
		resumenFacturado.addElement(total);
		return resumenFacturado;
	}
}


