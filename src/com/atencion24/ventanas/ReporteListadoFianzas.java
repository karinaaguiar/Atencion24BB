package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atencion24.control.Descuento;
import com.atencion24.control.Fianza;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.control.InformacionNivel;
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
import net.rim.device.api.ui.container.MainScreen;

public class ReporteListadoFianzas extends MainScreen implements FieldChangeListener {

	static Hashtable fianzas;
	
	private int nivel = 2;
	int posBotonPresionado = 0;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
    
    Bitmap plus = Bitmap.getBitmapResource("com/atencion24/imagenes/plus_alt_12x12.png");
    Bitmap minus = Bitmap.getBitmapResource("com/atencion24/imagenes/minus_alt_12x12.png");
    
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
    		info = new InformacionNivel(plus, " Caso:", fianza.getNroCaso(), nivel, new int[] {size-count});
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
	
    /**
     * crearParteMenu(). Metodo encargado de desplegar el reporte por pantalla, una vez que ha sido presionado 
     * algún botón desplegable. Además esta funcion invoca al metodo mostrarBotones
     * para crear los CustomButtonField de acuerdo a la información del vector informacionNivelSuperior (incluyendo los
     * CustomButtonField) de los hijos. A todos lo CustomButtonField creados le setea el ChangeListener y los agrega
     * al contenido (ListStyleButtonSet)
     */
    public void crearParteMenu()
    {
        try{
            // Los botones que vienen despues del boton presionado son eliminados
             for (int i = posBotonPresionado + 1 ; i < botonesF.length + 1; i++){
                 
                     contenido.delete(botonesF[i-1]);
                 }
            // Busco los nuevos botones
            CustomButtonTable[] aux;
     	    Enumeration listadoInfoNivel = informacionNivelSuperior.elements();
     	    botonesF = new CustomButtonTable[1];
     	    botonesF = ((InformacionNivel) listadoInfoNivel.nextElement()).mostrarBotones();
     	    
     	    //Por cada infoNivel en el nivel 1 debo crear un CustomButtonTable  
     	    while (listadoInfoNivel.hasMoreElements()) 
         	{
     	    	InformacionNivel info = (InformacionNivel) listadoInfoNivel.nextElement();
     	    	aux = info.mostrarBotones();
     	    	botonesF = info.mezclarArray(botonesF,aux);
         	}
            
     	     //Coloco el boton presionado
     	     botonesF[posBotonPresionado].setChangeListener(this);
             contenido.add(botonesF[posBotonPresionado]);
             botonesF[posBotonPresionado].setFocus();
             // Coloco en la pantalla los botones nuevos, ignorando los que ya tengo 
             // guardados (el boton presionado y los que vienen antes de el)
             for (int i = posBotonPresionado + 1  ; i < botonesF.length; i++){
                     botonesF[i].setChangeListener(this);
                     contenido.add(botonesF[i]);
             }
            
             //Guardo en mi arreglo los botones que no fueron borrados,
             //para poder hacer bien la comparacion en la función fieldChanged
             for (int i = 0; i < posBotonPresionado + 1; i++){
                 botonesF[i] = (CustomButtonTable)contenido.getField(i + 1);
                 }
         }catch(IndexOutOfBoundsException e){
             System.out.println("Error: " + e);
         }
        
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
				if (info.isMostrar()) 
	            {
	            	info.setMostrar(false);
	            	info.setIcono(plus);
	            }
		        else 
		        {
		        	//Asociarle hijos a info para convertirlo en menu desplegable
		        	info.setMostrar(true);
		        	info.setIcono(minus);
		        	if(info.getHijo() == null)
		        	{
		        		info.setHijo(new Hashtable());
			            InformacionNivel infohijo;
			            Fianza fianza = (Fianza) fianzas.get(new Integer(pos[0]));
			            
			            //Fecha Emision
			            infohijo = new InformacionNivel(" Fecha emisión:", fianza.getFechaEmisionFactura(), 1, new int[] {pos[0],0});
		                info.getHijo().put(new Integer(0), infohijo);
		                
		                //Paciente
			            infohijo = new InformacionNivel(" Paciente:", fianza.getPaciente(), 1, new int[] {pos[0],1});
		                info.getHijo().put(new Integer(1), infohijo);
		                
		                //Monto a cobrar
			            infohijo = new InformacionNivel(" Monto a cobrar:", fianza.getMontoACobrar(), 1, new int[] {pos[0],2});
		                info.getHijo().put(new Integer(2), infohijo);
		        		
		                //Monto abonado
			            infohijo = new InformacionNivel(plus, " Monto abonado:", fianza.getMontoAbonado(), 1, new int[] {pos[0],3});
		                info.getHijo().put(new Integer(3), infohijo);
		                
		                //Monto neto
			            infohijo = new InformacionNivel(" Monto neto:", fianza.getMontoNeto(), 1, new int[] {pos[0],4});
		                info.getHijo().put(new Integer(4), infohijo);
		        	}
		        }	
		        informacionNivelSuperior.setElementAt(info,pos[0]);
		        crearParteMenu();  
    		}
    		else if (nivelPulsado == 1) //Si presionaste algun boton del primer nivel
    		{
    			int[] pos = botonPulsado.obtenerPosicion();
    			int posicionNivel = pos[1];
    			
    			if (posicionNivel == 3) //Si presionaste el monto abonado de alguna fianza
    			{
        			int idFianza = pos[0];
        			
        			InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(idFianza);
        			InformacionNivel hijoPresionado = (InformacionNivel) info.getHijo().get(new Integer(3)); 
        			if (hijoPresionado.isMostrar()) 
    	            {
        				hijoPresionado.setMostrar(false);
        				hijoPresionado.setIcono(plus);
    	            }
    		        else 
    		        {
    		        	//Asociarle hijos a info para convertirlo en menu desplegable
    		        	hijoPresionado.setMostrar(true);
    		        	hijoPresionado.setIcono(minus);
    		        	if(hijoPresionado.getHijo() == null)
    		        	{
    		        		hijoPresionado.setHijo(new Hashtable());
    			            InformacionNivel infohijo;
    			            Enumeration descuentos = ((Fianza)fianzas.get(new Integer(idFianza))).getDescuentos().elements();
    			            int count = 0; 
    			            while(descuentos.hasMoreElements())
    			            {
    			            	Descuento descuento = (Descuento) descuentos.nextElement();
    			            	infohijo = new InformacionNivel(descuento.getFecha(), descuento.getMonto(), 0, new int[] {idFianza,posicionNivel,count});
        		        		hijoPresionado.getHijo().put(new Integer(count), infohijo);
        		        		count ++;
    			            }
    		        	}
    		        }
        			info.getHijo().remove(new Integer(3));
        			info.getHijo().put(new Integer(3), hijoPresionado);
    		        informacionNivelSuperior.setElementAt(info,idFianza);
    		        crearParteMenu();  
    				
    			}
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
