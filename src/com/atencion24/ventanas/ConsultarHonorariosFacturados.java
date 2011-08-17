package com.atencion24.ventanas;

import java.util.TimeZone;
import java.util.Vector;

import com.atencion24.control.ControlDates;
import com.atencion24.control.HttpConexion;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.PleaseWaitPopUpScreen;
import com.atencion24.interfaz.SpacerField;

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

	static String dateTime = "31/12/2002";
	
	DateField fechaInicial;
    DateField fechaFinal;
    DateField fechaAux;
    DateField fechaAux1;
    
    CustomButtonField verRepor;
    boolean cerrarSesion = false;
    
    String codSeleccionado;
    String fechaAct;
    
    PleaseWaitPopUpScreen wait = new PleaseWaitPopUpScreen();
    
	ConsultarHonorariosFacturados(String codSeleccionado, String fechaAct) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Honorarios Facturados");
		super.changeTitulo();
		super.changeSubTitulo();
		
		this.codSeleccionado = codSeleccionado;
		this.fechaAct = fechaAct;
		
		add(new SpacerField());
        add(new SpacerField());
		//Campos para rango de fechas
		ControlDates dates = new ControlDates();
        fechaInicial = new DateField("", System.currentTimeMillis(), new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT);                        
        fechaInicial.setTimeZone(TimeZone.getDefault());
        fechaInicial.setDate(dates.primerDiaMes(this.fechaAct)); 
        LabelField fechaI = new LabelField("Desde: ", Field.FIELD_RIGHT);
            
        fechaFinal = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        fechaFinal.setTimeZone(TimeZone.getDefault());
        fechaFinal.setDate(dates.stringToDate(this.fechaAct)); 
        LabelField fechaF = new LabelField("Hasta: ", Field.FIELD_RIGHT);
        
        fechaAux = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        fechaAux.setTimeZone(TimeZone.getDefault());
        fechaAux.setDate(dates.stringToDate(this.fechaAct)); 
        
        fechaAux1 = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        fechaAux1.setTimeZone(TimeZone.getDefault());
        fechaAux1.setDate(dates.stringToDate(dateTime)); 
        
        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
	  	gridFieldManager.add(fechaI);
        gridFieldManager.add(fechaInicial);
        gridFieldManager.add(fechaF);
        gridFieldManager.add(fechaFinal);   
        add(gridFieldManager);
        
        add(new SpacerField());
        add(new SpacerField());
        add(new SpacerField());
        //Boton de consultar
        verRepor = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xF77100, 0);
        verRepor.setChangeListener(this);
        VerticalFieldManager buttonManager = new VerticalFieldManager(FIELD_HCENTER);
        buttonManager.add(verRepor); 
        //foreground.
        add(buttonManager);
	}

	public void llamadaExitosa(String respuesta) 
	{
		
		if(cerrarSesion == false)
		{

			//a mi parser XML para que se encargue de darme el 
			//XML que me ha enviado el servidor procesado como 
			//un objeto de control. 
			XMLParser envioXml = new XMLParser();
		    String xmlInterno = envioXml.extraerCapaWebService(respuesta);
		    final Vector facturadoUDN = envioXml.LeerHonorariosFacturados(xmlInterno); //xmlInterno
		    final String cookie = this.getcookie();
		    //En caso de que el servidor haya enviado un error
		    //El medico no facturo honorarios en el rango de fechas indicado
		    if (facturadoUDN  == null)
		    {
		        final String mostrarError = envioXml.obtenerError();
		        UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						UiApplication.getUiApplication().popScreen(wait);
						Dialog.alert(mostrarError);
						if(mostrarError.equals("Sobrepas� el tiempo de inactividad permitido. Debe volver a iniciar sesi�n") || 
						   mostrarError.equals("La sesi�n ha expirado. Para seguir utilizando la aplicaci�n debe iniciar sesi�n nuevamente"))
						{	
							UiApplication.getUiApplication().popScreen((UiApplication.getUiApplication().getActiveScreen().getScreenBelow()).getScreenBelow());
							UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
							irInicio();
							V_InicioSesion loginpage = new V_InicioSesion();
							UiApplication.getUiApplication().pushScreen(loginpage);
						}
					}
				});
		    }
		    else
		    {
		    	UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						UiApplication.getUiApplication().popScreen(wait);
						HonorariosFacturados honorariosFacturados = new HonorariosFacturados(facturadoUDN, fechaInicial.toString(), fechaFinal.toString());
						honorariosFacturados.setcookie(cookie);
						UiApplication.getUiApplication().pushScreen(honorariosFacturados);
					}
				});
		    }
		}
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub
		
	}
	
	private void consultarHonorariosFacturados() 
	{
		//Por ahora llamo directo a llamadaExitosa luego ser�
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		//llamadaExitosa("");
		
		//Comparo las fechas. Fecha Desde < Fecha Hasta
		if (fechaInicial.getDate() < fechaAux1.getDate() ){
			Dialog.alert("Error al ingresar la fecha 'Desde'. No existe informaci�n de honorarios facturados en fechas anteriores al 01/01/2003" );}
		else if(fechaFinal.getDate() > fechaAux.getDate() ){
			Dialog.alert("Error al ingresar la fecha 'Hasta'. No existe informaci�n de honorarios facturados en fechas posteriores al " + fechaAct);}
		else if (fechaInicial.getDate() > fechaFinal.getDate()){
			Dialog.alert("Error al ingresar las fechas. Fecha 'Desde' debe ser menor que fecha 'Hasta'");}
		else
		{
			String fechaI = fechaInicial.toString();
			System.out.println(fechaI);
			String fechaF = fechaFinal.toString();
			System.out.println(fechaF);
			HttpConexion thread = new HttpConexion("/ConsultarHonorariosFacturados?medico_tb=" + codSeleccionado + "&fechaI_tb=" + fechaI + "&fechaF_tb=" + fechaF, "GET", this, false);
			thread.start();
			UiApplication.getUiApplication().pushModalScreen(wait);
		}
	}
	
	public void fieldChanged(Field field, int context) 
	{
	      if(field == verRepor) {
    		  consultarHonorariosFacturados();
	      }
	}
	
	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "�Est� seguro que desea cerrar sesi�n y salir?");
		if (dialog == Dialog.YES)
		{
			//Deber�a hacer cierre de sesion
			HttpConexion thread = new HttpConexion("/cerrarSesion", "GET", this, false);
			cerrarSesion = true;
			thread.start();
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
		//super.makeMenu(menu, instance);
		menu.add(new MenuItem("Consultar", 20,10) {
			public void run(){
				consultarHonorariosFacturados();
			}
		});
		menu.add(new MenuItem("Ir a inicio", 20,10) {
			public void run(){
				irInicio();
			}
		});
		menu.add(new MenuItem("Cerrar Sesi�n", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}
}
