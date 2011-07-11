package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;

import com.atencion24.control.CodigoPago;
import com.atencion24.control.EstadoCuentaAS;
import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;

//import com.atencion24.interfaz.CustomObjectChoiceField;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.CustomObjectChoiceField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.NegativeMarginVerticalFieldManager;
import com.atencion24.interfaz.ListStyleButtonField;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.SpacerField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
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
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;

public class LoginSuccessScreen extends plantilla_screen_http implements FieldChangeListener {
	
	static Sesion sesion;
	String codSeleccionado;
	
	int reporteElegido;
	
    Manager _bodyWrapper;
    Manager _currentBody;
  
    BitmapField bitmapField;
    
	CustomObjectChoiceField codPagos;
    //CustomObjectChoiceField codPagos;
    
    ListStyleButtonField edoCta;
    ListStyleButtonField honPagados;
    ListStyleButtonField honFact;
    ListStyleButtonField detCaso;
    ListStyleButtonField listFianzas;
	
	
	public LoginSuccessScreen(Sesion ses) 
	{
		//ojo con NO_VERTICAL_SCROLL
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		sesion = ses;
		super.setTitulo("Bienvenido " +sesion.getNombre() + " " + sesion.getApellido());
		super.changeTitulo();
		
		codSeleccionado = (String) ((CodigoPago)sesion.getCodigoMedico().elementAt(0)).getCodigo();

		Manager foreground = new ForegroundManager();
		//Si el médico que ingresó al sistema tiene más de un código de pago 
		if(sesion.getCodigoMedico().size()>1)
		{
			foreground.add(new SpacerField());
			foreground.add(new SeparatorField());
			
			try{
				FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
				Font boldFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt).derive(Font.BOLD);
				CustomLabelField label = new CustomLabelField("  Elija el código de pago a consultar:" , 0x400000, Color.WHITE, 0);
				label.setFont(boldFont);
				foreground.add(label);
			}catch (ClassNotFoundException e){}
			
			foreground.add(new SeparatorField());
			foreground.add(new SpacerField());
			foreground.add(new SpacerField());
			
			Enumeration codpagos = sesion.getCodigoMedico().elements();
			String [] nombresCodigosAsociados = new String[sesion.getCodigoMedico().size()];
			int i = 0;
			while (codpagos.hasMoreElements())
			{
				nombresCodigosAsociados[i] = (String) ((CodigoPago)codpagos.nextElement()).getNombre();
				i++;
			}	
			codPagos = new CustomObjectChoiceField("Códigos de pago:", nombresCodigosAsociados,0);
			codPagos.setChangeListener(this);
			foreground.add(codPagos);
			foreground.add(new SpacerField());
			foreground.add(new SpacerField());
			
		}
		foreground.add(new SpacerField());
		foreground.add(new SeparatorField());
		//**Label field simple**
		try{
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font boldFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt).derive(Font.BOLD);
			CustomLabelField label = new CustomLabelField("  Elija el reporte a consultar:" , 0x400000, Color.WHITE, 0);
			label.setFont(boldFont);
			foreground.add(label);
		}catch (ClassNotFoundException e){}
		
		foreground.add(new SeparatorField());
		
        _bodyWrapper = new NegativeMarginVerticalFieldManager(USE_ALL_WIDTH);
        _currentBody = new ListStyleButtonSet();
        
        Bitmap arrowBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/gray-arrow.png");
        
        try {
        	FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
        	Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
       
	        edoCta = new ListStyleButtonField( "Estado de Cuenta", arrowBitmap , 0 );
	        edoCta.setFont(appFont);
	        edoCta.setChangeListener(this);
	        _currentBody.add(edoCta);
	        honPagados = new ListStyleButtonField( "Honorarios pagados en un período", arrowBitmap , 0 );
	        honPagados.setFont(appFont);
	        honPagados.setChangeListener(this);
	        _currentBody.add(honPagados);
	        honFact =  new ListStyleButtonField( "Honorarios facturados en un período", arrowBitmap , 0 );
	        honFact.setFont(appFont);
	        honFact.setChangeListener(this);
	        _currentBody.add(honFact);
	        detCaso =  new ListStyleButtonField( "Detalle administrativo de un caso", arrowBitmap , 0 );
	        detCaso.setFont(appFont);
	        detCaso.setChangeListener(this);
	        _currentBody.add(detCaso);
	        listFianzas =  new ListStyleButtonField( "Listado de fianzas pendientes", arrowBitmap , 0 );
	        listFianzas.setFont(appFont);
	        listFianzas.setChangeListener(this);
	        _currentBody.add(listFianzas);
        
        } catch (ClassNotFoundException e){}
        _bodyWrapper.add(_currentBody);  
        foreground.add(_bodyWrapper);
        add(foreground);
	}
	
	public void edoCta(){
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		String medico = sesion.getCodigoMedico();
		HttpConexion thread = new HttpConexion("/edoCtaAntiguedadSaldo?medico_tb=" + codSeleccionado, "GET", this);
		thread.start();*/
    }
	
	public void honPagados(){
        ConsultarHonorariosPagados consultarHonPagados = new ConsultarHonorariosPagados(codSeleccionado);
        UiApplication.getUiApplication().pushScreen(consultarHonPagados);
    }
	
	public void honFact(){
        ConsultarHonorariosFacturados consultarHonFacturados = new ConsultarHonorariosFacturados(codSeleccionado);
        UiApplication.getUiApplication().pushScreen(consultarHonFacturados);
    }
	
	public void detCaso(){
        ConsultarDetalleDeCaso consultarDetCaso = new ConsultarDetalleDeCaso(codSeleccionado);
        UiApplication.getUiApplication().pushScreen(consultarDetCaso);
    }
	
	public void listFianzas(){
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		String medico = sesion.getCodigoMedico();
		HttpConexion thread = new HttpConexion("/listFianzas?medico_tb=" + codSeleccionado, "GET", this);
		thread.start();*/
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

	public void setCodSeleccionado(){
		codSeleccionado = (String) ((CodigoPago)sesion.getCodigoMedico().elementAt(codPagos.getSelectedIndex())).getCodigo();
	}
	
	 public void fieldChanged(Field field, int context) {
	        if (field == edoCta) {
	        	reporteElegido = 1;	
	        	edoCta();
	            }
	        else if (field == honPagados) {
	        		honPagados();
	            }
	        else if (field == honFact) {
	        		honFact();
	            }
	        else if (field == detCaso) {
	        		detCaso();
	            }
	        else if (field == listFianzas) {
	        		reporteElegido = 5;
	        		listFianzas();
	            }
	        else if (field == codPagos){
	        		setCodSeleccionado();
	        }
	    }

	public void llamadaExitosa(String respuesta) {
		//Si el usuario eligio consultar el reporte Listado de Fianzas
		if (reporteElegido ==5)
		{
			//Con el String XML que recibo del servidor debo hacer llamada
    		//a mi parser XML para que se encargue de darme el 
    		//XML que me ha enviado el servidor procesado como 
    		//un objeto de control. 
			XMLParser envioXml = new XMLParser();
		    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
		    final Hashtable fianzas = envioXml.LeerListadoFianzas(respuesta); //xmlInterno
		    
		    //En caso de que el servidor haya enviado un error
		    //El medico no tiene asociada fianzas pendientes
		    if (fianzas == null)
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
						ReporteListadoFianzas reporteFianzas = new ReporteListadoFianzas(fianzas);
				        UiApplication.getUiApplication().pushScreen(reporteFianzas);
					}
				});
		    }
		}
		//Si el usuario eligio consultar el reporte Estado de Cuenta
		if (reporteElegido ==1)
		{
			//Con el String XML que recibo del servidor debo hacer llamada
    		//a mi parser XML para que se encargue de darme el 
    		//XML que me ha enviado el servidor procesado como 
    		//un objeto de control. 
			XMLParser envioXml = new XMLParser();
		    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
		    final EstadoCuentaAS edoCta = envioXml.LeerEstadoCtaAntiguedadSaldo(respuesta); //xmlInterno
		    
		    //En caso de que el servidor haya enviado un error
		    //La Clínica no posee deuda con el médico
		    if (edoCta == null)
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
						EstadoDeCuentaAntiguedadSaldo EdoCta = new EstadoDeCuentaAntiguedadSaldo(edoCta);
				        UiApplication.getUiApplication().pushScreen(EdoCta);
					}
				});
		    }
		}
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub
		
	}
	
	//Sobreescribes el metodo makeMenu y le agregas sus menuItems
	protected void makeMenu(Menu menu, int instance){
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Estado de Cuenta", 20,10) {
			public void run(){
				reporteElegido = 1;	
		     	edoCta();
			}
		});
		menu.add(new MenuItem("Honorarios Pagados", 20,10) {
			public void run(){
				honPagados();
			}
		});
		menu.add(new MenuItem("Honorarios Facturados", 20,10) {
			public void run(){
				honFact();
			}
		});
		menu.add(new MenuItem("Detalle de un Caso", 20,10) {
			public void run(){
				detCaso();
			}
		});
		menu.add(new MenuItem("Fianzas Pendientes", 20,10) {
			public void run(){
	     		reporteElegido = 5;
	     		listFianzas();
			}
		});
		menu.add(new MenuItem("Cerrar Sesion", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}
}
 
       
       
        
        
 
    

   