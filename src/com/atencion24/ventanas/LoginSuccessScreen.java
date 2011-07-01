package com.atencion24.ventanas;

import java.util.Hashtable;

import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;

import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.NegativeMarginVerticalFieldManager;
import com.atencion24.interfaz.ListStyleButtonField;
import com.atencion24.interfaz.ListStyleButtonSet;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class LoginSuccessScreen extends plantilla_screen implements FieldChangeListener {
	
	static Sesion sesion;
	
	int reporteElegido;
	
    Manager _bodyWrapper;
    Manager _currentBody;
  
    BitmapField bitmapField;
   
    ListStyleButtonField edoCta;
    ListStyleButtonField honPagados;
    ListStyleButtonField honFact;
    ListStyleButtonField detCaso;
    ListStyleButtonField listFianzas;
	
	public LoginSuccessScreen(Sesion ses) 
	{
		//ojo con NO_VERTICAL_SCROLL
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		
		/*add(new LabelField("Logged in!"));
		add(new LabelField("Nombre: " + ses.getNombre()));
		add(new LabelField("Apellido: " + ses.getApellido()));
		add(new LabelField("Codigo medico: " + ses.getCodigoMedico()));
		add(new LabelField("NombreUsuario: " + ses.getNombreUsuario()));
		add(new LabelField("Hey"));*/
		
		//Cambiar el font de la aplicación
		try {
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
			setFont(appFont);
		}catch (ClassNotFoundException e){}

		sesion = ses;
		
		//Logo CSS alineado al centro
		Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
		bitmapField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
        hfmLabel.add(bitmapField);
        add(hfmLabel);
		add(new SeparatorField());
		
		//**Label field simple**
		add(new CustomLabelField("Bienvenido " +sesion.getNombre() + " " + sesion.getApellido() , Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));
		add(new SeparatorField());
		
		//**Label field simple**
		add(new CustomLabelField("Elija el reporte a consultar:" , Color.WHITE,  0x581B00 , Field.USE_ALL_WIDTH));
		add(new SeparatorField());
		
		Manager foreground = new ForegroundManager();
        _bodyWrapper = new NegativeMarginVerticalFieldManager(USE_ALL_WIDTH);
        _currentBody = new ListStyleButtonSet();
        
        Bitmap arrowBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/gray-arrow.png");
        edoCta = new ListStyleButtonField( "Estado de Cuenta", arrowBitmap , 0 );
        edoCta.setChangeListener(this);
        _currentBody.add(edoCta);
        honPagados = new ListStyleButtonField( "Honorarios pagados en un período", arrowBitmap , 0 );
        honPagados.setChangeListener(this);
        _currentBody.add(honPagados);
        honFact =  new ListStyleButtonField( "Honorarios facturados en un período", arrowBitmap , 0 );
        honFact.setChangeListener(this);
        _currentBody.add(honFact);
        detCaso =  new ListStyleButtonField( "Detalle administrativo de un caso", arrowBitmap , 0 );
        detCaso.setChangeListener(this);
        _currentBody.add(detCaso);
        listFianzas =  new ListStyleButtonField( "Listado de fianzas pendientes", arrowBitmap , 0 ); 
        listFianzas.setChangeListener(this);
        _currentBody.add(listFianzas);
        
        _bodyWrapper.add(_currentBody);  
        foreground.add(_bodyWrapper);
        add(foreground);
	}
	
	public void edoCta(){
        ConsultarEstadoDeCuenta consultarEdoCta = new ConsultarEstadoDeCuenta();
        UiApplication.getUiApplication().pushScreen(consultarEdoCta);
    }
	
	public void honPagados(){
        ConsultarHonorariosPagados consultarHonPagados = new ConsultarHonorariosPagados(sesion);
        UiApplication.getUiApplication().pushScreen(consultarHonPagados);
    }
	
	public void honFact(){
        ConsultarHonorariosFacturados consultarHonFacturados = new ConsultarHonorariosFacturados();
        UiApplication.getUiApplication().pushScreen(consultarHonFacturados);
    }
	
	public void detCaso(){
        ConsultarDetalleDeCaso consultarDetCaso = new ConsultarDetalleDeCaso(sesion);
        UiApplication.getUiApplication().pushScreen(consultarDetCaso);
    }
	
	public void listFianzas(){
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		String medico = sesion.getCodigoMedico();
		HttpConexion thread = new HttpConexion("/listFianzas?medico_tb=" + medico, "GET", this);
		thread.start();*/
    }
	
	 public void fieldChanged(Field field, int context) {
	        if (field == edoCta) {
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
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub
		
	}
}
 
       
       
        
        
 
    

   