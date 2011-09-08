package com.atencion24.ventanas;

import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.atencion24.control.ControlDates;
import com.atencion24.control.HttpConexion;
import com.atencion24.control.Pago;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.PleaseWaitPopUpScreen;
import com.atencion24.interfaz.SpacerField;

public class ConsultarHonorariosPagados extends plantilla_screen_http implements FieldChangeListener{
	
	static String dateTime = "31/12/2002";
	RadioButtonField reciente;
	RadioButtonField historico;
	
    DateField fechaInicial;
    DateField fechaFinal;
    DateField fechaAux;
    DateField fechaAux1;
    
    CustomButtonField verRepor;
	boolean cerrarSesion = false;
    
    String codSeleccionado;
    String fechaAct;
    int tipoConsulta = 0; //Si vale 0 ->pago en proceso. Si vale 1 -> historico de pagos
    
    BitmapField bitmapField;
    
    int insertIndex;
    GridFieldManager gridFieldManager;
    SpacerField nulo;
    
    PleaseWaitPopUpScreen wait = new PleaseWaitPopUpScreen();
    boolean estaWait = false; 
    
    Manager foreground = new ForegroundManager();
    
	ConsultarHonorariosPagados(String codSeleccionado, String fechaAct) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Honorarios Pagados");
		super.changeTitulo();
		super.changeSubTitulo();
		
		this.codSeleccionado = codSeleccionado;
		this.fechaAct = fechaAct;
		
		foreground.add(new SpacerField());
		foreground.add(new SpacerField());
        //RadioButton para escoger el tipo de consulta a realizar
		RadioButtonGroup tipoConsulta = new RadioButtonGroup();
        reciente = new RadioButtonField("Pago en Proceso ",tipoConsulta,true);
        historico = new RadioButtonField("Histórico ",tipoConsulta,false);	
        //foreground.
        foreground.add(reciente);
        //foreground.
        foreground.add(historico);
        tipoConsulta.setChangeListener(this);
        
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
      
	  	gridFieldManager = new GridFieldManager(2, 0);
	  	gridFieldManager.add(fechaI);
        gridFieldManager.add(fechaInicial);
        gridFieldManager.add(fechaF);
        gridFieldManager.add(fechaFinal);
        
        nulo = new SpacerField();
        foreground.add(nulo);
        insertIndex = this.getFieldCount();
        
        //foreground.add(gridFieldManager);
        foreground.add(new SpacerField());
        foreground.add(new SpacerField());
        
        //Boton de consultar
        verRepor = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xF77100, 0);
        verRepor.setChangeListener(this);
        VerticalFieldManager buttonManager = new VerticalFieldManager(FIELD_HCENTER);
        buttonManager.add(verRepor); 
        //foreground.
        foreground.add(buttonManager);
        SpacerField nulo1 = new SpacerField();
        foreground.add(nulo1);
        SpacerField nulo2 = new SpacerField();
        foreground.add(nulo2);
        SpacerField nulo3 = new SpacerField();
        foreground.add(nulo3);
        add(foreground);
	}

	public void llamadaExitosa(String respuesta) {
		if(cerrarSesion == false)
		{
			//Consulta Pago en proceso
			if (tipoConsulta==0)
			{
				//Con el String XML que recibo del servidor debo hacer llamada
	    		//a mi parser XML para que se encargue de darme el 
	    		//XML que me ha enviado el servidor procesado como 
	    		//un objeto de control. 
				XMLParser envioXml = new XMLParser();
			    String xmlInterno = envioXml.extraerCapaWebService(respuesta);
			    System.out.println("Este es el xml " + xmlInterno);
			    final Pago pagoEnProceso = envioXml.LeerProximoPago(xmlInterno);
			    final String cookie = this.getcookie();
			    
			    final String mostrarError = envioXml.obtenerError();
			    UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() 
					{
						if(estaWait)
						{
							UiApplication.getUiApplication().popScreen(wait);
							estaWait = false;
						}
						
						//En caso de que el servidor haya enviado un error
					    //No hay pago en proceso (tabla pagoshonorarios vacia)
						if (pagoEnProceso == null)
					    {
							Dialog.alert(mostrarError);
							if(mostrarError.equals("Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión") || 
							   mostrarError.equals("La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente"))
							{	
								UiApplication.getUiApplication().popScreen((UiApplication.getUiApplication().getActiveScreen().getScreenBelow()).getScreenBelow());
								UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
								irInicio();
								V_InicioSesion loginpage = new V_InicioSesion();
								UiApplication.getUiApplication().pushScreen(loginpage);
							}
					    }
					    else
					    {
							HonorariosPagadosEnProceso honorariosPagadosEnProceso = new HonorariosPagadosEnProceso(pagoEnProceso, fechaAct);
							honorariosPagadosEnProceso.setcookie(cookie);
							UiApplication.getUiApplication().pushScreen(honorariosPagadosEnProceso);
					    }
					}
				});
			}
			//Consulta historico de pagos
			else if (tipoConsulta==1)
			{
	    		//Con el String XML que recibo del servidor debo hacer llamada
	    		//a mi parser XML para que se encargue de darme el 
	    		//XML que me ha enviado el servidor procesado como 
	    		//un objeto de control. 
				XMLParser envioXml = new XMLParser();
			    String xmlInterno = envioXml.extraerCapaWebService(respuesta);
			    final Vector historicoPagos = envioXml.LeerHistoricoPagos(xmlInterno); 
			    final String cookie = this.getcookie();
			    
			    final String mostrarError = envioXml.obtenerError();
			    UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() 
					{
						if(estaWait)
						{
							UiApplication.getUiApplication().popScreen(wait);
							estaWait = false;
						}
						
						//En caso de que el servidor haya enviado un error
					    //No hay datos (pagos de nomina) entre las fechas indicadas
						if (historicoPagos == null)
					    {
							Dialog.alert(mostrarError);
							if(mostrarError.equals("Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión") || 
							   mostrarError.equals("La sesión ha expirado. Para seguir utilizando la aplicación debe iniciar sesión nuevamente"))
							{	
								UiApplication.getUiApplication().popScreen((UiApplication.getUiApplication().getActiveScreen().getScreenBelow()).getScreenBelow());
								UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
								irInicio();
								V_InicioSesion loginpage = new V_InicioSesion();
								UiApplication.getUiApplication().pushScreen(loginpage);
							}
					    }
					    else
					    {
							HonorariosPagadosHistorico honorariosPagadosHistorico = new HonorariosPagadosHistorico (historicoPagos, fechaInicial.toString(), fechaFinal.toString());
							honorariosPagadosHistorico.setcookie(cookie);
							UiApplication.getUiApplication().pushScreen(honorariosPagadosHistorico);
					    }
					}
				});
			}
		}
	}

	public void llamadaFallada(final String respuesta) {
		UiApplication.getUiApplication().invokeLater(new Runnable() 
		{
		    public void run() {
			    if(estaWait)
				{
					UiApplication.getUiApplication().popScreen(wait);
					estaWait =false; 
				}
		    	Dialog.alert("Error de conexión: " + respuesta);
		    }
		});
	}

	private void ConsultarHistoricoPagos(){
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		//llamadaExitosa("");
		
		//Comparo las fechas. Fecha Desde < Fecha Hasta
		if (fechaInicial.getDate() < fechaAux1.getDate() ){
			Dialog.alert("Error al ingresar la fecha 'Desde'. No existe información de honorarios facturados en fechas anteriores al 01/01/2003 ");}
		else if(fechaFinal.getDate() > fechaAux.getDate() ){
			Dialog.alert("Error al ingresar la fecha 'Hasta'. No existe información de honorarios facturados en fechas posteriores al " + fechaAct);}
		else if (fechaInicial.getDate() > fechaFinal.getDate()){
			Dialog.alert("Error al ingresar las fechas. Fecha 'Desde' debe ser menor que fecha 'Hasta'");
		}
		else{
			String fechaI = fechaInicial.toString();
			System.out.println(fechaI);
			String fechaF = fechaFinal.toString();
			System.out.println(fechaF);
			HttpConexion thread = new HttpConexion("/ConsultarHistoricoPagos?medico_tb=" + codSeleccionado + "&fechaI_tb=" + fechaI + "&fechaF_tb=" + fechaF, "GET", this, false);
			thread.start();
			estaWait  = true;
			UiApplication.getUiApplication().pushModalScreen(wait);
		}
			
	}
	
	private void ConsultarProximoPago()
	{
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		//llamadaExitosa("");
		HttpConexion thread = new HttpConexion("/ConsultarProximoPago?medico_tb=" + codSeleccionado, "GET", this, false);
		thread.start();
		estaWait  = true;
		UiApplication.getUiApplication().pushModalScreen(wait);
	}
	
	
	public void fieldChanged(Field field, int context) 
	{
	      if (field == historico) 
	      {
	    	  if (historico.isSelected()){	
	    		  	foreground.replace(nulo, gridFieldManager);
	    		  	//insert(gridFieldManager, insertIndex);
	    	  }
	    	  else 
	    	  {
				  	foreground.replace(gridFieldManager, nulo); 
		    	   	//delete(gridFieldManager);
	    	  }
          }
	      if(field == verRepor) {
	    	  if(historico.isSelected()){
	    		  tipoConsulta = 1;
	    		  ConsultarHistoricoPagos();
	    	  }
	    	  else if (reciente.isSelected()){
	    		  tipoConsulta = 0;
	    		  ConsultarProximoPago();
	    	  } 
	      }
	}
	
	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "¿Está seguro que desea cerrar sesión y salir?");
		if (dialog == Dialog.YES)
		{
			//Debería hacer cierre de sesion
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
				if(historico.isSelected()){
		    		  tipoConsulta = 1;
		    		  ConsultarHistoricoPagos();
		    	  }
		    	  else if (reciente.isSelected()){
		    		  tipoConsulta = 0;
		    		  ConsultarProximoPago();
		    	  } 
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
