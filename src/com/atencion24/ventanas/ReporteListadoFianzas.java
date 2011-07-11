package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atencion24.control.Descuento;
import com.atencion24.control.Fianza;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.control.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

public class ReporteListadoFianzas extends plantilla_screen implements FieldChangeListener {

	static Hashtable fianzas;
	
	int posBotonPresionado = 0;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
    
    Bitmap plus = Bitmap.getBitmapResource("com/atencion24/imagenes/plus_blanco.png");
    Bitmap minus = Bitmap.getBitmapResource("com/atencion24/imagenes/minus_blanco.png");
    Bitmap arrow1 = Bitmap.getBitmapResource("com/atencion24/imagenes/arrow_down_vino.png");
    Bitmap arrow = Bitmap.getBitmapResource("com/atencion24/imagenes/arrow_vino.png");
    Bitmap check = Bitmap.getBitmapResource("com/atencion24/imagenes/check_vinotinto.png");
    
	ReporteListadoFianzas(Hashtable listaFianzas) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Fianzas Pendientes");
		super.changeTitulo();
		fianzas = listaFianzas;
		
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
    		info = new InformacionNivel(plus, " Caso", fianza.getNroCaso(), 3, new int[] {size-count});
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
			contenido.add(new SeparatorField()); 
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
        	int posicionEnManagerDelPresionado = contenido.getFieldWithFocus().getIndex();
            int numeroBotonesABorrar = contenido.getFieldCount() - posicionEnManagerDelPresionado;
            contenido.deleteRange(posicionEnManagerDelPresionado, numeroBotonesABorrar); 
        	
        	
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
                     if(botonesF[i].getNivel() != 0)  contenido.add(new SeparatorField());
                     contenido.add(botonesF[i]);
             }
            
             //Guardo en mi arreglo los botones que no fueron borrados,
             //para poder hacer bien la comparacion en la función fieldChanged
             int count = 0; 
             for (int i = 0; i < posicionEnManagerDelPresionado; i++){
            	 if(contenido.getField(i).getClass().equals(new CustomButtonTable().getClass()))
                 {
                	 botonesF[count] = (CustomButtonTable)contenido.getField(i);
                	 count++;
                 }
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
    		if(nivelPulsado == 3)
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
			            infohijo = new InformacionNivel(check, "Fecha emisión:", fianza.getFechaEmisionFactura(), 0, new int[] {pos[0],0});
		                info.getHijo().put(new Integer(0), infohijo);
		                
		                //Paciente
			            infohijo = new InformacionNivel(check, "Paciente:", fianza.getPaciente(), 0, new int[] {pos[0],1});
		                info.getHijo().put(new Integer(1), infohijo);
		                
		                //Monto a cobrar
			            infohijo = new InformacionNivel(check, "Monto a cobrar:", fianza.getMontoACobrar()+ " Bs", 0, new int[] {pos[0],2});
		                info.getHijo().put(new Integer(2), infohijo);
		        		
		                //Monto abonado
			            infohijo = new InformacionNivel(arrow, "Monto abonado:", fianza.getMontoAbonado()+ " Bs", 2, new int[] {pos[0],3});
		                info.getHijo().put(new Integer(3), infohijo);
		                
		                //Monto neto
			            infohijo = new InformacionNivel(check, "Monto neto:", fianza.getMontoNeto() + " Bs", 0, new int[] {pos[0],4});
		                info.getHijo().put(new Integer(4), infohijo);
		        	}
		        }	
		        informacionNivelSuperior.setElementAt(info,pos[0]);
		        crearParteMenu();  
    		}
    		else if (nivelPulsado == 2) //Si presionaste algun boton del primer nivel
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
        				hijoPresionado.setIcono(arrow);
    	            }
    		        else 
    		        {
    		        	//Asociarle hijos a info para convertirlo en menu desplegable
    		        	hijoPresionado.setMostrar(true);
    		        	hijoPresionado.setIcono(arrow1);
    		        	if(hijoPresionado.getHijo() == null)
    		        	{
    		        		hijoPresionado.setHijo(new Hashtable());
    			            InformacionNivel infohijo;
    			            Enumeration descuentos = ((Fianza)fianzas.get(new Integer(idFianza))).getDescuentos().elements();
    			            int count = 0; 
    			            while(descuentos.hasMoreElements())
    			            {
    			            	Descuento descuento = (Descuento) descuentos.nextElement();
    			            	infohijo = new InformacionNivel(descuento.getFecha(), descuento.getMonto()+ " Bs", 0, new int[] {idFianza,posicionNivel,count},1);
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
	
	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "¿Está seguro que desea salir?");
		if (dialog == Dialog.YES)
		{
			//Debería hacer cierre de sesion
			Dialog.alert("Hasta luego!");
			System.exit(0);
		}
	}
	
	public void irInicio()
	{
		UiApplication.getUiApplication().popScreen(this);
	}
	
	//Sobreescribes el metodo makeMenu y le agregas sus menuItems
	protected void makeMenu(Menu menu, int instance){
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Ir a inicio", 20,10) {
			public void run(){
				irInicio();
			}
		});
		menu.add(new MenuItem("Cerrar Sesion", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}

}
