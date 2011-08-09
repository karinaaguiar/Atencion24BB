package com.atencion24.ventanas;

import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.atencion24.control.ControlDates;
import com.atencion24.control.HttpConexion;
import com.atencion24.control.Pago;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomRadioButtom;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.PleaseWaitPopUpScreen;
import com.atencion24.interfaz.SpacerField;

public class ConsultarHonorariosPagados extends plantilla_screen_http implements FieldChangeListener{
	
	static String dateTime = "01/01/2008";
	CustomRadioButtom reciente;
	CustomRadioButtom historico;
    DateField fechaInicial;
    DateField fechaFinal;
    CustomButtonField verRepor;
	boolean cerrarSesion = false;
    
    String codSeleccionado;
    int tipoConsulta = 0; //Si vale 0 ->pago en proceso. Si vale 1 -> historico de pagos
    
    BitmapField bitmapField;
    
    int insertIndex;
    GridFieldManager gridFieldManager;
    SpacerField nulo;
    
    PleaseWaitPopUpScreen wait = new PleaseWaitPopUpScreen();
    
	ConsultarHonorariosPagados(String codSeleccionado) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Honorarios Pagados");
		super.changeTitulo();
		super.changeSubTitulo();
		
		this.codSeleccionado = codSeleccionado;
		
        add(new SpacerField());
        add(new SpacerField());
        //RadioButton para escoger el tipo de consulta a realizar
		RadioButtonGroup tipoConsulta = new RadioButtonGroup();
        reciente = new CustomRadioButtom("Pago en Proceso ",tipoConsulta,true);
        historico = new CustomRadioButtom("Histórico ",tipoConsulta,false);	
        //foreground.
        add(reciente);
        //foreground.
        add(historico);
        tipoConsulta.setChangeListener(this);
        
        ControlDates dates = new ControlDates();
        fechaInicial = new DateField("", System.currentTimeMillis(), new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT);                        
        fechaInicial.setTimeZone(TimeZone.getDefault());
        fechaInicial.setDate(dates.stringToDate(dateTime)); 
        LabelField fechaI = new LabelField("Desde: ", Field.FIELD_RIGHT);
            
        fechaFinal = new DateField("",  System.currentTimeMillis() , new SimpleDateFormat("dd/MM/yyyy"), DrawStyle.LEFT); 
        LabelField fechaF = new LabelField("Hasta: ", Field.FIELD_RIGHT);
        
	  	gridFieldManager = new GridFieldManager(2, 0);
	  	gridFieldManager.add(fechaI);
        gridFieldManager.add(fechaInicial);
        gridFieldManager.add(fechaF);
        gridFieldManager.add(fechaFinal);
        
        nulo = new SpacerField();
        add(nulo);
        insertIndex = this.getFieldCount();
        
        //foreground.add(gridFieldManager);
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
			    final Pago pagoEnProceso = envioXml.LeerProximoPago(xmlInterno);
			    final String cookie = this.getcookie();
			    //En caso de que el servidor haya enviado un error
			    //No hay pago en proceso (tabla pagoshonorarios vacia)
			    if (pagoEnProceso == null)
			    {
			        final String mostrarError = envioXml.obtenerError();
			        UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							UiApplication.getUiApplication().popScreen(wait);
							Dialog.alert(mostrarError);
							if(mostrarError.equals("Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión"))
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
							HonorariosPagadosEnProceso honorariosPagadosEnProceso = new HonorariosPagadosEnProceso(pagoEnProceso);
							honorariosPagadosEnProceso.setcookie(cookie);
							UiApplication.getUiApplication().pushScreen(honorariosPagadosEnProceso);
						}
					});
			    }
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
			    //En caso de que el servidor haya enviado un error
			    //No hay datos (pagos de nomina) entre las fechas indicadas
			    if (historicoPagos == null)
			    {
			        final String mostrarError = envioXml.obtenerError();
			        UiApplication.getUiApplication().invokeLater(new Runnable() {
						public void run() {
							UiApplication.getUiApplication().popScreen(wait);
							Dialog.alert(mostrarError);
							if(mostrarError.equals("Sobrepasó el tiempo de inactividad permitido. Debe volver a iniciar sesión"))
							{	
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
							HonorariosPagadosHistorico honorariosPagadosHistorico = new HonorariosPagadosHistorico (historicoPagos, fechaInicial.toString(), fechaFinal.toString());
							honorariosPagadosHistorico.setcookie(cookie);
							UiApplication.getUiApplication().pushScreen(honorariosPagadosHistorico);
						}
					});
			    }
			}
		}
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

	private void ConsultarHistoricoPagos(){
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		//llamadaExitosa("");
		
		//Comparo las fechas. Fecha Desde < Fecha Hasta
		if(fechaInicial.getDate() > fechaFinal.getDate() || fechaInicial.getDate() == fechaFinal.getDate()){
			Dialog.alert("Error al ingresar las fechas. Fecha 'Desde' debe ser menor que fecha 'Hasta'");
		}
		else{
			String fechaI = fechaInicial.toString();
			System.out.println(fechaI);
			String fechaF = fechaFinal.toString();
			System.out.println(fechaF);
			HttpConexion thread = new HttpConexion("/ConsultarHistoricoPagos?medico_tb=" + codSeleccionado + "&fechaI_tb=" + fechaI + "&fechaF_tb=" + fechaF, "GET", this, false);
			thread.start();
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
		UiApplication.getUiApplication().pushModalScreen(wait);
	}
	
	
	public void fieldChanged(Field field, int context) 
	{
	      if (field == historico) 
	      {
	    	  if (historico.isSelected()){	
	    		  	replace(nulo, gridFieldManager);
	    		  	//insert(gridFieldManager, insertIndex);
	    	  }
	    	  else 
	    	  {
		    	   	replace(gridFieldManager, nulo); 
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
