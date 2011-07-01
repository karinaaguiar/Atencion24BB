package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atencion24.control.Deduccion;
import com.atencion24.control.Fianza;
import com.atencion24.control.Honorario;
import com.atencion24.control.Pago;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.ListStyleLabelField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class ReporteListadoFianzas extends plantilla_screen implements FieldChangeListener {

	static Hashtable fianzas;
	
	private int nivel = 2;
	int posBotonPresionado = 0;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
    
	ReporteListadoFianzas(Hashtable listaFianzas) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		fianzas = listaFianzas;
		
		//Cambiar el font de la aplicación
		try {
				FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
				Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
				setFont(appFont);
			}catch (ClassNotFoundException e){}
		
		//Logo CSS alineado al centro
		Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
		BitmapField bitmapField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
        hfmLabel.add(bitmapField);
        add(hfmLabel);
       
        //**Label field simple**
		add(new CustomLabelField("Fianzas Pendientes", Color.WHITE, 0x400000, FIELD_HCENTER));
		add(new SeparatorField());
		
		//Inserto los managers donde irá el reporte.
		foreground.add(contenido);
        add(foreground);
        
        llenarVectorInformacionNivelSup();
	}
	
	/**
	 * llenarVectorInformacionNivelSup(). Por cada fianza (en el hash de fianzas)
	 * se crea una instancia de la clase InformacionNivel el cual contendrá toda la información de los
	 * elementos expandibles del reporte del nivel superior.
	 */
	public void llenarVectorInformacionNivelSup()
	{
		int size = fianzas.size();
    	int count = size;
    	Enumeration listadoFianzas = fianzas.elements();
    	Fianza fianza;
    	InformacionNivel info;
    	//Por cada fianza en listadoFianzas, debo crear un InformacionNivel y agregarlo al Vector informacionNivel
    	while (listadoFianzas.hasMoreElements()) 
    	{
    		fianza = (Fianza) listadoFianzas.nextElement();
    		info = new InformacionNivel(fianza.getNroCaso(), "", nivel, new int[] {size-count});
    		informacionNivelSuperior.addElement(info);
    		count --;
    	}	
    	crearReporte();
	}

	   /**
     * crearReporte(). Método encargado de mostrar en el dispositivo BB el reporte listado de fianzas pendientes
     * sin ningún elemento expandido. Este método debe crear un CustomButtonTable 
     * por cada elemento expandible del reporte del nivel superior y asociarle su información respectiva, la 
     * cual se obtiene del Vector informacionNivelSuperior. Todos los CustomButtonTable creados son almacenados 
     * en el vector botonesF.
     */
    public void crearReporte()
    {
    	contenido.deleteAll();
    	
    	//Agregar la cabecera al reporte
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Fianzas Pendientes", DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    contenido.add(Titulo);
	    
	    CustomButtonTable[] aux;
	    Enumeration listadoInfoNivel = informacionNivelSuperior.elements();
	    botonesF = ((InformacionNivel) listadoInfoNivel.nextElement()).mostrarBotones();
	    
	    //Por cada infoNivel en el nivel superior debo crear un CustomButtonTable  
	    //y agregarlo al Vector botonesF
	    while (listadoInfoNivel.hasMoreElements()) 
    	{
	    	InformacionNivel info = (InformacionNivel) listadoInfoNivel.nextElement();
	    	aux = info.mostrarBotones();
	    	botonesF = info.mezclarArray(botonesF,aux);
    	}
	    
	    //Set ChangeListener y agregarlos a la interfaz
	    for (int i = 0; i < botonesF.length; i++)
	    {
	         botonesF[i].setChangeListener(this);
	         contenido.add(botonesF[i]);
	    }
    }
    
	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}
	
	  /**
     * desplegarMenu. Método encargado de según el nivel del boton presionado (en este caso si es algun boton del nivel
     * superior, o su hijo Monto Abonado, crearle sus hijos (InformacionNivel), si no se le han creado, en base a la 
     * información del hash de fianzas y settear el campo mostrar del InformacionNivel correspondiente al boton presionado 
     * en True o False dependendo de si ya estaba desplegado o no.   
     * @param botonPulsado
     */
    public void desplegarMenu(CustomButtonTable botonPulsado){
        
    	int nivelPulsado = botonPulsado.obtenerNivel();
    	if (nivelPulsado != 0)
    	{
    		//Si presione algun boton del nivel superior
    		if(nivelPulsado == 2)
    		{
    			int[] pos = botonPulsado.obtenerPosicion();
				InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(pos[0]);
		        if (info.isMostrar()) info.setMostrar(false);
		        else 
		        {
		        	//Asociarle hijos a info para convertirlo en menu desplegable
		        	info.setMostrar(true);
		        	if(info.hijo == null)
		        	{
		        		info.hijo = new Hashtable();
			            InformacionNivel infohijo;
			            Fianza fianza = (Fianza) fianzas.get(new Integer(pos[0]));
			            
			            //Fecha Emision
			            infohijo = new InformacionNivel(" Fecha emisión:", fianza.getFechaEmisionFactura(), 1, new int[] {pos[0],0});
		                info.hijo.put(new Integer(0), infohijo);
		                
		                //Paciente
			            infohijo = new InformacionNivel(" Paciente:", fianza.getPaciente(), 1, new int[] {pos[0],1});
		                info.hijo.put(new Integer(0), infohijo);
		                
		                //Monto a cobrar
			            infohijo = new InformacionNivel(" Monto a cobrar:", fianza.getMontoACobrar(), 1, new int[] {pos[0],2});
		                info.hijo.put(new Integer(0), infohijo);
		        		
		                //Monto abonado
			            infohijo = new InformacionNivel(" Monto abonado:", fianza.getMontoAbonado(), 1, new int[] {pos[0],3});
		                info.hijo.put(new Integer(0), infohijo);
		                
		                //Monto neto
			            infohijo = new InformacionNivel(" Monto neto:", fianza.getMontoNeto(), 1, new int[] {pos[0],4});
		                info.hijo.put(new Integer(0), infohijo);
		                //AQUI!!
		        	}
		        }	
		        informacionNivelSuperior.setElementAt(info,pos[0]);
		        crearParteMenu();  
    		}
    		else if (nivelPulsado == 1) //Si presionaste algun honorario en particular
    		{
    			int[] pos = botonPulsado.obtenerPosicion();
    			int posicionNivel = pos[1];
    			Integer posNivel  = new Integer(posicionNivel); 
    			
    			InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(3);
    			InformacionNivel hijoPresionado = (InformacionNivel) info.hijo.get(posNivel); 
    			if (hijoPresionado.isMostrar()) hijoPresionado.setMostrar(false);
		        else 
		        {
		        	//Asociarle hijos a info para convertirlo en menu desplegable
		        	hijoPresionado.setMostrar(true);
		        	if(hijoPresionado.hijo == null)
		        	{
		        		hijoPresionado.hijo = new Hashtable();
			            InformacionNivel infohijo;
		        		Honorario honorario = (Honorario) caso.getHonorarios().get(posNivel);
		        	    
		        		//Monto Facturado
		        		infohijo = new InformacionNivel(" Monto Facturado: ", honorario.getMontoFacturado(), 0, new int[] {3,posicionNivel,0});
		        		hijoPresionado.hijo.put(new Integer(0), infohijo);
		        		//Monto Exonerado
		        		infohijo = new InformacionNivel(" Monto Exonerado: ", honorario.getMontoExonerado(), 0, new int[] {3,posicionNivel,1});
		        		hijoPresionado.hijo.put(new Integer(1), infohijo);
		        		//Monto Abonado
		        		infohijo = new InformacionNivel(" Monto Abonado: ", honorario.getMontoAbonado(), 0, new int[] {3,posicionNivel,2});
		        		hijoPresionado.hijo.put(new Integer(2), infohijo);
		        		//Deuda
		        		infohijo = new InformacionNivel(" Deuda: ", honorario.getTotalDeuda(), 0, new int[] {3,posicionNivel,3});
		        		hijoPresionado.hijo.put(new Integer(3), infohijo);
		        	}
		        }
    			info.hijo.remove(posNivel);
    			info.hijo.put(posNivel, hijoPresionado);
		        informacionNivelSuperior.setElementAt(info,3);
		        crearParteMenu();  
    		}
    	}	
    }
    
	public void fieldChanged(Field field, int context) 
	{
		for (int i = 0; i < botonesF.length; i++)
	    {
	        if (field == botonesF[i])
	        {
	            posBotonPresionado = i;
	            desplegarMenu(botonesF[i]);
	            break;
	        }
	    }
		
	}

}
