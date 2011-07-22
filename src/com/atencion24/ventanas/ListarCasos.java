package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;

import com.atencion24.control.Caso;
import com.atencion24.control.HttpConexion;
import com.atencion24.control.ManejoArray;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.ListStyleButtonField;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.NegativeMarginVerticalFieldManager;

public class ListarCasos extends plantilla_screen_http implements FieldChangeListener {

	static Hashtable casos;
	String codSeleccionado;
	
    Manager _bodyWrapper;
    Manager _currentBody;
  
    BitmapField bitmapField;
   
    ListStyleButtonField [] botones;
	
	ListarCasos(Hashtable listadoCasos, String codSeleccionado) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Detalle de un caso");
		super.changeTitulo();
		super.changeSubTitulo();
		
		casos = listadoCasos;
		this.codSeleccionado = codSeleccionado;
		
		//Cambiar el font de la aplicación
		try 
		{
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
			setFont(appFont);

			Manager foreground = new ForegroundManager();
	        _bodyWrapper = new NegativeMarginVerticalFieldManager(USE_ALL_WIDTH);
	        _currentBody = new ListStyleButtonSet();
	
	        ManejoArray manejoArray = new ManejoArray();
	        ListStyleButtonField [] aux = new ListStyleButtonField[1];
	        Bitmap arrowBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/gray-arrow.png");
	        ListStyleButtonField boton;
	        int i = 0;
	        Caso caso; 
	        Enumeration listaCasos = casos.elements();
	        botones = new ListStyleButtonField[0];
	        while(listaCasos.hasMoreElements())
	        {
	        	caso = (Caso) listaCasos.nextElement();
	        	boton = new ListStyleButtonField(" " + caso.getNombrePaciente() + " (" + 
	        			caso.getFechaEmisionFactura() + ") ", arrowBitmap, 0);
	        	boton.setFont(appFont);
	        	aux[0] = boton;
	        	botones = manejoArray.mezclarArray(botones, aux);
	        	//botones[i] = boton;
	        	botones[i].setChangeListener(this);
	        	_currentBody.add(botones[i]);
	        	i++;
	        }	
	        _currentBody.setFont(appFont);
	        _bodyWrapper.add(_currentBody);
	        _bodyWrapper.setFont(appFont);  
	        foreground.add(_bodyWrapper);
	        foreground.setFont(appFont);
	        add(foreground);
        
		}catch (ClassNotFoundException e){}
	}

	public void llamadaExitosa(String respuesta) 
	{
		//Con el String XML que recibo del servidor debo hacer llamada
		//a mi parser XML para que se encargue de darme el 
		//XML que me ha enviado el servidor procesado como 
		//un objeto de control. 
		XMLParser envioXml = new XMLParser();
	    String xmlInterno = envioXml.extraerCapaWebService(respuesta);
	    final Caso caso = envioXml.LeerCaso(xmlInterno); //xmlInterno
	    
	    //En este caso el servidor no puede enviar error
    	UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				DetalleDeCaso ventanaCaso = new DetalleDeCaso(caso);
		        UiApplication.getUiApplication().pushScreen(ventanaCaso);
			}
		});
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}
	
	public void consultarCaso(Caso caso)
	{
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		//llamadaExitosa("");
		
		String nroCaso = caso.getNroCaso();
		String udn = caso.getUnidadNegocio();
		HttpConexion thread = new HttpConexion("/consultarCaso?medico_tb=" + codSeleccionado + "&caso_tb="+nroCaso+"&udn_tb="+udn, "GET", this);
		thread.start();
	}
	
	public void fieldChanged(Field field, int context) {
		for (int i = 0; i < botones.length; i++)
	    {
	        if (field == botones[i])
	        {
	        	Integer id = new Integer(i);
	        	Caso caso  = (Caso) casos.get(id);
	        	consultarCaso(caso);
	            break;
	        }
	    }
		
	}
	
	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "¿Está seguro que desea cerrar sesión y salir?");
		if (dialog == Dialog.YES)
		{
			//Debería hacer cierre de sesion
			Dialog.alert("Hasta luego!");
			System.exit(0);
		}
	}
	
	public void irInicio()
	{
		UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
		UiApplication.getUiApplication().popScreen(this);	
	}
	
	public void irAtras()
	{
		UiApplication.getUiApplication().popScreen(this);
	}
	
	//Sobreescribes el metodo makeMenu y le agregas sus menuItems
	protected void makeMenu(Menu menu, int instance){
		//super.makeMenu(menu, instance);
		menu.add(new MenuItem("Ir atrás", 20,10) {
			public void run(){
				irAtras();
			}
		});
		menu.add(new MenuItem("Ir a inicio", 20,10) {
			public void run(){
				irInicio();
			}
		});
		menu.add(new MenuItem("Cerrar Sesión", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}

}
