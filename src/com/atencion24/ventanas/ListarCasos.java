package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;

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
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.atencion24.control.Caso;
import com.atencion24.control.ManejoArray;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.ListStyleButtonField;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.NegativeMarginVerticalFieldManager;

public class ListarCasos extends plantilla_screen implements FieldChangeListener {

	static Hashtable casos;
	
    Manager _bodyWrapper;
    Manager _currentBody;
  
    BitmapField bitmapField;
   
    ListStyleButtonField [] botones;
	
	ListarCasos(Hashtable listadoCasos) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		casos = listadoCasos;
		
		//Cambiar el font de la aplicaci�n
		try 
		{
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font appFont = familiaFont.getFont(Font.PLAIN, 6, Ui.UNITS_pt);
			setFont(appFont);

			//Logo CSS alineado al centro
			Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
			bitmapField = new BitmapField(logoBitmap);
			HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
	        hfmLabel.add(bitmapField);
	        add(hfmLabel);
			add(new SeparatorField());
			
			//**Label field simple**
			add(new CustomLabelField("Detalle de un caso" , Color.WHITE,  0x990000 , FIELD_HCENTER));
			add(new SeparatorField());
			
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
	        			caso.getFechaEmisionFactura() + "): ", arrowBitmap, 0);
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
	    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
	    final Caso caso = envioXml.LeerCaso(respuesta); //xmlInterno
	    
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
		//Por ahora llamo directo a llamadaExitosa luego ser�
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		String nroCaso = caso.getNroCaso();
		String udn = caso.getUnidadNegocio();
		HttpConexion thread = new HttpConexion("/consultarCaso?medico_tb="+ medico+"&caso_tb="+nroCaso+"&udn_tb="+udn, "GET", this);
		thread.start();*/
	}
	
	public void fieldChanged(Field field, int context) {
		for (int i = 0; i < botones.length; i++)
	    {
	        if (field == botones[i])
	        {
	        	//AQUI
	        	Integer id = new Integer(i);
	        	Caso caso  = (Caso) casos.get(id);
	        	consultarCaso(caso);
	            break;
	        }
	    }
		
	}

}
