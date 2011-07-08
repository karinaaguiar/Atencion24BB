package com.atencion24.ventanas;

import java.util.TimeZone;
import java.util.Vector;

import com.atencion24.control.ControlDates;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.GridFieldManager;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ConsultarHonorariosFacturados extends plantilla_screen_http implements FieldChangeListener {

	static String dateTime = "01/01/2008";
    DateField fechaInicial;
    DateField fechaFinal;
    CustomButtonField verRepor;
    
    String codSeleccionado;
    
	ConsultarHonorariosFacturados(String codSeleccionado) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Honorarios Facturados");
		super.changeTitulo();
		
		this.codSeleccionado = codSeleccionado;
		
		//Campos para rango de fechas
		ControlDates dates = new ControlDates();
        fechaInicial = new DateField("", System.currentTimeMillis(), new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT);                        
        fechaInicial.setTimeZone(TimeZone.getDefault());
        fechaInicial.setDate(dates.stringToDate(dateTime)); 
        LabelField fechaI = new LabelField("Desde: ", Field.FIELD_RIGHT);
            
        fechaFinal = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        LabelField fechaF = new LabelField("Hasta: ", Field.FIELD_RIGHT);
        
        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
	  	gridFieldManager.add(fechaI);
        gridFieldManager.add(fechaInicial);
        gridFieldManager.add(fechaF);
        gridFieldManager.add(fechaFinal);   
        add(gridFieldManager);
        
        //Boton de consultar
        verRepor = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xE38311, 0);
        verRepor.setChangeListener(this);
        VerticalFieldManager buttonManager = new VerticalFieldManager(FIELD_HCENTER);
        buttonManager.add(verRepor); 
        //foreground.
        add(buttonManager);
	}

	public void llamadaExitosa(String respuesta) 
	{
		//Con el String XML que recibo del servidor debo hacer llamada
		//a mi parser XML para que se encargue de darme el 
		//XML que me ha enviado el servidor procesado como 
		//un objeto de control. 
		XMLParser envioXml = new XMLParser();
	    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
	    final Vector facturadoUDN = envioXml.LeerHonorariosFacturados(respuesta); //xmlInterno
	    
	    //En caso de que el servidor haya enviado un error
	    //El medico no facturo honorarios en el rango de fechas indicado
	    if (facturadoUDN  == null)
	    {
	        final String mostrarError = envioXml.obtenerError();
	        UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					Dialog.alert(mostrarError);
				}
			});
	    }
	    else
	    {
	    	UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					HonorariosFacturados honorariosFacturados = new HonorariosFacturados(facturadoUDN);
			        UiApplication.getUiApplication().pushScreen(honorariosFacturados);
				}
			});
	    }
		
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub
		
	}
	
	private void consultarHonorariosFacturados() 
	{
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		//Comparo las fechas. Fecha Desde < Fecha Hasta
		if(fechaInicial.getDate() > fechaFinal.getDate() || fechaInicial.getDate() == fechaFinal.getDate()){
			Dialog.alert("Error al ingresar las fechas. Fecha 'Desde' debe ser menor que fecha 'Hasta'");
		}
		else{
			String fechaI = fechaInicial.toString();
			System.out.println(fechaI);
			String fechaF = fechaFinal.toString();
			System.out.println(fechaF);
			HttpConexion thread = new HttpConexion("/ConsultarHonorariosFacturados?medico_tb=" + codSeleccionado + "&fechaI_tb=" + fechaI + "&fechaF_tb=" + fechaF, "GET", this);
			thread.start();
		}*/
	}
	
	public void fieldChanged(Field field, int context) 
	{
	      if(field == verRepor) {
    		  consultarHonorariosFacturados();
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
