package com.atencion24.ventanas;

import java.util.TimeZone;
import java.util.Vector;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.atencion24.control.ControlDates;
import com.atencion24.control.Pago;
import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.GridFieldManager;

public class ConsultarHonorariosPagados extends plantilla_screen_http implements FieldChangeListener{
	
	static String dateTime = "01/01/2008";
	RadioButtonField reciente;
	RadioButtonField historico;
    DateField fechaInicial;
    DateField fechaFinal;
    CustomButtonField verRepor;
    
    static Sesion sesion;
    int tipoConsulta = 0; //Si vale 0 ->pago en proceso. Si vale 1 -> historico de pagos
    
    BitmapField bitmapField;
    
    int insertIndex;
    GridFieldManager gridFieldManager;
    NullField nulo;
    
   
	ConsultarHonorariosPagados(Sesion ses) {
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Honorarios Pagados");
		super.changeTitulo();
		
		sesion = ses;
		
		add(new CustomLabelField("Bienvenido " +sesion.getNombre() + " " + sesion.getApellido() , Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));
      
		//Manager foreground = new ForegroundManager();        
        
        //RadioButton para escoger el tipo de consulta a realizar
		RadioButtonGroup tipoConsulta = new RadioButtonGroup();
        reciente = new RadioButtonField("Pago en Proceso ",tipoConsulta,true);
        historico = new RadioButtonField("Histórico",tipoConsulta,false);	
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
        
        nulo = new NullField();
        add(nulo);
        insertIndex = this.getFieldCount();
        
        //foreground.add(gridFieldManager);
        
        //Boton de consultar
        verRepor = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xE38311, 0);
        verRepor.setChangeListener(this);
        VerticalFieldManager buttonManager = new VerticalFieldManager(FIELD_HCENTER);
        buttonManager.add(verRepor); 
        //foreground.
        add(buttonManager);
	}

	public void llamadaExitosa(String respuesta) {
		//Consulta Pago en proceso
		if (tipoConsulta==0)
		{
    		//Con el String XML que recibo del servidor debo hacer llamada
    		//a mi parser XML para que se encargue de darme el 
    		//XML que me ha enviado el servidor procesado como 
    		//un objeto de control. 
			XMLParser envioXml = new XMLParser();
		    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
		    final Pago pagoEnProceso = envioXml.LeerProximoPago(respuesta); //xmlInterno
		    
		    //En caso de que el servidor haya enviado un error
		    //No hay pago en proceso (tabla pagoshonorarios vacia)
		    if (pagoEnProceso == null)
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
						HonorariosPagadosEnProceso honorariosPagadosEnProceso = new HonorariosPagadosEnProceso(pagoEnProceso);
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
		    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
		    final Vector historicoPagos = envioXml.LeerHistoricoPagos(respuesta); //xmlInterno
		    
		    //En caso de que el servidor haya enviado un error
		    //No hay datos (pagos de nomina) entre las fechas indicadas
		    if (historicoPagos == null)
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
						HonorariosPagadosHistorico honorariosPagadosHistorico = new HonorariosPagadosHistorico (historicoPagos);
				        UiApplication.getUiApplication().pushScreen(honorariosPagadosHistorico);
					}
				});
		    }
		}

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

	private void ConsultarHistoricoPagos(String medico){
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
			HttpConexion thread = new HttpConexion("/ConsultarHistoricoPagos?medico_tb=" + medico + "&fechaI_tb=" + fechaI + "&fechaF_tb=" + fechaF, "GET", this);
			thread.start();
		}*/
			
	}
	
	private void ConsultarProximoPago(String medico)
	{
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		HttpConexion thread = new HttpConexion("/ConsultarProximoPago?medico_tb=" + medico, "GET", this);
		thread.start();*/
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
	    	  String medico = sesion.getCodigoMedico();
	    	  if(historico.isSelected()){
	    		  tipoConsulta = 1;
	    		  ConsultarHistoricoPagos(medico);
	    	  }
	    	  else if (reciente.isSelected()){
	    		  tipoConsulta = 0;
	    		  ConsultarProximoPago(medico);
	    	  } 
	      }
	}

}
