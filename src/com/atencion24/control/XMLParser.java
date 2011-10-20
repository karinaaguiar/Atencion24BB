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

/**
 * @author Karina
 *
 */
/**
 * @author Karina
 *
 */
public class XMLParser {
	
	String error = " ";

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


    /**
     * Método encargado de procesar el XML de respuesta para el Inicio de Sesión
     * @param xmlSource
     * @return Sesion con datos del usuario loggeado
     */
    public Sesion LeerXMLInicio(String xmlSource) 
    {        
    	Sesion usuario = new Sesion();
        Vector CodigosMedico = new Vector();
        CodigoPago codigoMedico = new CodigoPago(); 
        
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
            if(elemento.getChildNodes().item(0).getNodeValue().equals("3"))	
    			this.error = "Excedió el número de intentos. Su usuario será bloqueado por algunos minutos";
            if(elemento.getChildNodes().item(0).getNodeValue().equals("4"))	
    			this.error = "Su usuario está bloqueado por exceder el máximo número de intentos establecidos para iniciar sesión. Espere unos minutos";
            if(elemento.getChildNodes().item(0).getNodeValue().equals("502"))
            	this.error = "502";
            if(elemento.getChildNodes().item(0).getNodeValue().equals("503"))
            	this.error = "503";
            if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	
    	
    	NodeList nodoUsuario = elemento.getChildNodes();
    	System.out.println(xmlSource);
        //Nombre Usuario
    	usuario.setNombre(nodoUsuario.item(0).getChildNodes().item(0).getNodeValue());
    	
    	//Fecha actualizacion BD
    	usuario.setFechaAct(nodoUsuario.item(1).getChildNodes().item(0).getNodeValue());
    	
    	//Codigos de pago
    	if(nodoUsuario.getLength()==3)
    	{
	        NodeList nodoCodigos = nodoUsuario.item(2).getChildNodes();
	        for( int i =0 ; i < nodoCodigos.getLength(); i++ )
	        {
	        	NodeList nodoCodigo = nodoCodigos.item(i).getChildNodes();
	        	codigoMedico = new CodigoPago();
	        	codigoMedico.setCodigo(nodoCodigo.item(0).getChildNodes().item(0).getNodeValue());
	        	codigoMedico.setNombre(nodoCodigo.item(1).getChildNodes().item(0).getNodeValue());
	        	CodigosMedico.addElement(codigoMedico);
	        }	
	        usuario.setCodigoMedico(CodigosMedico);
    	}
    		
		return usuario;
    }
    
    /**
     * @param nodoPago
     * @return
     */
    public Pago AuxiliarHonorariosPagados(NodeList nodoPago)
    {
    	Pago pago = new Pago();
    	Vector Deducciones = new Vector();
    	Deduccion deduccion = new Deduccion();
    	
        //Monto Liberado
    	pago.setMontoLiberado(nodoPago.item(0).getChildNodes().item(0).getNodeValue());
    	
    	//Deducciones
    	if(nodoPago.getLength()==4)
    	{
	        NodeList nodoDeducciones = nodoPago.item(1).getChildNodes();
	        for( int i =0 ; i < nodoDeducciones.getLength(); i++ )
	        {
	        	NodeList nodoDeduccion = nodoDeducciones.item(i).getChildNodes();
	        	deduccion = new Deduccion();
	        	deduccion.setConcepto(nodoDeduccion.item(0).getChildNodes().item(0).getNodeValue());
	        	deduccion.setMonto(nodoDeduccion.item(1).getChildNodes().item(0).getNodeValue());
	        	Deducciones.addElement(deduccion);
	        }	
	        pago.setDeducciones(Deducciones);
	        
	        //Monto Neto
	        pago.setMontoNeto(nodoPago.item(2).getChildNodes().item(0).getNodeValue());
	        
	        //Fecha Pago 
	        pago.setFechaPago(nodoPago.item(3).getChildNodes().item(0).getNodeValue());
    	}
    	else
    	{
	        //Monto Neto
	        pago.setMontoNeto(nodoPago.item(1).getChildNodes().item(0).getNodeValue());
	        
	        //Fecha Pago 
	        pago.setFechaPago(nodoPago.item(2).getChildNodes().item(0).getNodeValue());
    	}		
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
    			this.error = "Aún no se han generado pagos a su nombre para la próxima nómina";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoPago = elemento.getChildNodes();
        pago = AuxiliarHonorariosPagados(nodoPago);
    	
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
    	Pago pago;
    	
    	System.out.println(xmlSource);
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	System.out.println(tag);
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "No existen pagos en el rango de fechas indicado";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("1"))
    			this.error = "Debe acotar el rango de fechas de la búsqueda";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoPagos = elemento.getChildNodes();
    	for( int i =0 ; i < nodoPagos.getLength(); i++ )
        {
    		NodeList nodoPago = nodoPagos.item(i).getChildNodes();
    		pago = new Pago();
    		pago = AuxiliarHonorariosPagados(nodoPago);
    		historicoPago.addElement(pago);
        }
    	
    	return historicoPago;
    	
    }
    
    public Caso AuxiliarDetalleCaso(NodeList nodoCaso, int simple)
    {
    	Caso caso = new Caso();
    	Hashtable honorarios = new Hashtable();
    	Honorario honorario;
    	
        //Nombre del paciente 
    	caso.setNombrePaciente(nodoCaso.item(0).getChildNodes().item(0).getNodeValue());
    	
    	//Fecha emisión
    	caso.setFechaEmisionFactura(nodoCaso.item(1).getChildNodes().item(0).getNodeValue());
    	
    	//Número de caso  
    	caso.setNroCaso(nodoCaso.item(2).getChildNodes().item(0).getNodeValue());
    	
    	//Unidad de negocio 
    	caso.setUnidadNegocio(nodoCaso.item(3).getChildNodes().item(0).getNodeValue());
    	
    	if (simple == 1)
    	{
        	//ciPaciente
        	caso.setCiPaciente(nodoCaso.item(4).getChildNodes().item(0).getNodeValue());
        	
        	//responsablePago
        	caso.setResponsablePago(nodoCaso.item(5).getChildNodes().item(0).getNodeValue());
        	
        	//montoFacturado
        	caso.setMontoFacturado(nodoCaso.item(6).getChildNodes().item(0).getNodeValue());
        	
        	//montoExonerado
        	caso.setMontoExonerado(nodoCaso.item(7).getChildNodes().item(0).getNodeValue());
        	
        	//montoAbonado
        	caso.setMontoAbonado(nodoCaso.item(8).getChildNodes().item(0).getNodeValue());
        	
        	//totalDeuda
        	caso.setTotalDeuda(nodoCaso.item(9).getChildNodes().item(0).getNodeValue());
    		
    		//Honorarios
        	if (nodoCaso.getLength()==11)
        	{	
	            NodeList nodoHonorarios = nodoCaso.item(10).getChildNodes();
	            for( int i =0 ; i < nodoHonorarios.getLength(); i++ )
	            {
	            	NodeList nodoHonorario = nodoHonorarios.item(i).getChildNodes();
	            	honorario = new Honorario();
	            	honorario.setNombre(nodoHonorario.item(0).getChildNodes().item(0).getNodeValue());
	            	honorario.setMontoFacturado(nodoHonorario.item(1).getChildNodes().item(0).getNodeValue());
	            	honorario.setMontoExonerado(nodoHonorario.item(2).getChildNodes().item(0).getNodeValue());
	            	honorario.setMontoAbonado(nodoHonorario.item(3).getChildNodes().item(0).getNodeValue());
	            	honorario.setTotalDeuda(nodoHonorario.item(4).getChildNodes().item(0).getNodeValue());
	            	
	            	honorarios.put(new Integer(i), honorario);
	            }
	            caso.setHonorarios(honorarios); 
        	}
    	}
    	
		return caso;
    }
    
    //Listado de casos
    public Hashtable  LeerListadoCasos (String xmlSource) 
    {  
    	Hashtable listadoCasos = new Hashtable();
    	Caso caso;
    	
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	System.out.println(tag);
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "No existen casos asociados al nombre ingresado";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("1"))
    			this.error = "Debe refinar el criterio de búsqueda";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoCasos = elemento.getChildNodes();
    	for( int i =0 ; i < nodoCasos.getLength(); i++ )
        {
    		NodeList nodoCaso = nodoCasos.item(i).getChildNodes();
    		caso = new Caso();
    		caso = AuxiliarDetalleCaso(nodoCaso, 0);
    		listadoCasos.put((Integer) new Integer(i), caso);
        }

    	return listadoCasos;
    }

	public Caso LeerCaso(String xmlSource) 
	{
		Caso caso = new Caso();
    	
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	System.out.println(tag);
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "No existen casos asociados al apellido ingresado";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoCaso = elemento.getChildNodes();
    	caso = AuxiliarDetalleCaso(nodoCaso, 1);
    		
		return caso;
	}

    public Fianza AuxiliarFianzasPendientes(NodeList nodoFianza)
    {
    	Fianza fianza = new Fianza();

        //Nro de caso 
    	fianza.setNroCaso(nodoFianza.item(0).getChildNodes().item(0).getNodeValue());
    	
    	//Fecha emisión
    	fianza.setFechaEmisionFactura(nodoFianza.item(1).getChildNodes().item(0).getNodeValue());
    	
    	//Paciente  
    	fianza.setPaciente(nodoFianza.item(2).getChildNodes().item(0).getNodeValue());

    	//Monto a cobrar
    	fianza.setMontoACobrar(nodoFianza.item(3).getChildNodes().item(0).getNodeValue());
    	
    	//Monto Abonado 
		fianza.setMontoAbonado(nodoFianza.item(4).getChildNodes().item(0).getNodeValue());
    	
		//montoReintegro
		fianza.setMontoReintegro(nodoFianza.item(5).getChildNodes().item(0).getNodeValue());
    	
    	//montoNotasCred
		fianza.setMontoNotasCred(nodoFianza.item(6).getChildNodes().item(0).getNodeValue());
    	
    	//montoNotasDeb
		fianza.setMontoNotasDeb(nodoFianza.item(7).getChildNodes().item(0).getNodeValue());
		
		//Monto Deuda 
		fianza.setMontoNeto(nodoFianza.item(8).getChildNodes().item(0).getNodeValue());
    	
		return fianza;
    }
    
	public Hashtable LeerListadoFianzas(String xmlSource)
	{
		Hashtable fianzas = new Hashtable();
    	Fianza fianza;
    	
    	Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	System.out.println(tag);
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "Usted no posee fianzas pendientes";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoFianzas = elemento.getChildNodes();
    	for( int i =0 ; i < nodoFianzas.getLength(); i++ )
        {
    		NodeList nodoFianza = nodoFianzas.item(i).getChildNodes();
    		fianza = new Fianza();
    		fianza = AuxiliarFianzasPendientes(nodoFianza);
    		fianzas.put((Integer) new Integer(i), fianza);
        }

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
    			this.error = "La Clínica actualmente no tiene deudas con usted";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoEstadoCtaAS = elemento.getChildNodes();
    	
    	//Deuda A 30 días 
        edocta.setA30dias(nodoEstadoCtaAS.item(0).getChildNodes().item(0).getNodeValue()); 
        
        //Deuda a 60 días 
        edocta.setA60dias(nodoEstadoCtaAS.item(1).getChildNodes().item(0).getNodeValue());
        
        //Deuda a 90 días 
        edocta.setA90dias(nodoEstadoCtaAS.item(2).getChildNodes().item(0).getNodeValue());
        
        //Deuda a 180 días 
        edocta.setA180dias(nodoEstadoCtaAS.item(3).getChildNodes().item(0).getNodeValue());
        
        //Deuda a 360 días 
        edocta.setA360dias(nodoEstadoCtaAS.item(4).getChildNodes().item(0).getNodeValue());
        
        //Deuda a 720 días 
        edocta.setA720dias(nodoEstadoCtaAS.item(5).getChildNodes().item(0).getNodeValue());
        
        //Deuda a más 360 días 
        edocta.setMas720(nodoEstadoCtaAS.item(6).getChildNodes().item(0).getNodeValue());
        
        //Deuda total 
        edocta.setTotalDeuda(nodoEstadoCtaAS.item(7).getChildNodes().item(0).getNodeValue());
        
		return edocta;
	}

	public Vector LeerHonorariosFacturados(String xmlSource) 
	{
		Vector resumenFacturado = new Vector();
		
		Document documento = PreprocesarXML(xmlSource);
    	Element elemento = documento.getDocumentElement();
    	elemento.normalize();
    	String tag = elemento.getNodeName();
    	
    	if(tag.equals("error"))
    	{
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("0"))
    			this.error = "Usted no facturó honorarios en el rango de fechas ingresado";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("500"))	
    			this.error = "Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("505"))	
    			this.error = "La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente";
    		if(elemento.getChildNodes().item(0).getNodeValue().equals("600"))
            	this.error = "Actualmente se está actualizando la base de datos de la aplicación. Espere unos minutos mientras culmina el proceso";
            return null;
    	}	

    	NodeList nodoFacturado = elemento.getChildNodes();

    	//Total 
    	Deduccion fact = new Deduccion();
    	fact.setConcepto("Total facturado");
    	fact.setMonto(nodoFacturado.item(0).getChildNodes().item(0).getNodeValue());
    	
    	resumenFacturado.addElement(fact);
    	
        //Facturado por UDN 
        if(nodoFacturado.getLength()>1)
        {
        	for(int i = 1; i < nodoFacturado.getLength(); i++)
        	{
        		NodeList nodoFactUDN = nodoFacturado.item(i).getChildNodes();
        		fact = new Deduccion();
        		fact.setConcepto(nodoFactUDN.item(0).getChildNodes().item(0).getNodeValue());
        		fact.setMonto(nodoFactUDN.item(1).getChildNodes().item(0).getNodeValue());
        		resumenFacturado.addElement(fact);
        	}
        }
      
		return resumenFacturado;
	}
}


