package com.atencion24.ventanas;

import java.util.Hashtable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.atencion24.control.HttpConexion;
import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.GridFieldManager;

public class ConsultarDetalleDeCaso extends plantilla_screen implements FieldChangeListener {

	BitmapField logoField;
	EditField apellidoField;
	CustomButtonField consultarButtom;
	
	ConsultarDetalleDeCaso(Sesion sesion) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		
		//Cambiar el font de la aplicación
		try {
				FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
				Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
				setFont(appFont);
			}catch (ClassNotFoundException e){}
		
		//Logo CSS alineado al centro
		Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
		logoField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
        hfmLabel.add(logoField);
        add(hfmLabel);
		add(new SeparatorField());
		
        //**Label field simple**
		add(new CustomLabelField("Detalle de un caso", Color.WHITE, 0x990000, FIELD_HCENTER));
		add(new SeparatorField());
		
		//**Label field simple**
		add(new CustomLabelField("Introduzca el apellido del paciente:", Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));
		add(new SeparatorField());
		
		//Campos Nombre de usuario y clave
		apellidoField = new EditField("", "", 20, Field.FIELD_LEFT);
		LabelField apellidoLabel = new LabelField("Apellido: ", Field.FIELD_LEFT);

        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManager.add(apellidoLabel);
        gridFieldManager.add(apellidoField);
        add(gridFieldManager);
        
		
		//**Botones
		consultarButtom = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xE38311, 0);
		consultarButtom.setChangeListener(this);
		HorizontalFieldManager buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.add(consultarButtom);
		add(buttonManager);
		
	}
	
	public void consultarListadoDeCaso()
	{
		//Por ahora llamo directo a llamadaExitosa luego será
		//el hilo de la conexion quien se encargue
		//Cuando implemente el web service utilizar codigo de abajo
		llamadaExitosa("");
		/*
		 if (apellidoField.getTextLength() == 0){
			Dialog.alert("Debe colocar algún apellido para poder realizar la consulta");
		}
		else{
			String apellido = apellidoField.getText();
			HttpConexion thread = new HttpConexion("/consultarListadoDeCaso?apellido_tb" + apellido, "GET", this);
			thread.start();
		}*/
	}
	
	public void llamadaExitosa(String respuesta) 
	{
		//Con el String XML que recibo del servidor debo hacer llamada
		//a mi parser XML para que se encargue de darme el 
		//XML que me ha enviado el servidor procesado como 
		//un objeto de control. 
		XMLParser envioXml = new XMLParser();
	    //String xmlInterno = envioXml.extraerCapaWebService(respuesta);
	    final Hashtable listadoCasos = envioXml.LeerListadoCasos(respuesta); //xmlInterno
	    
	    //En caso de que el servidor haya enviado un error
	    //No hay casos asociados al apellido y al medico 
	    if (listadoCasos == null)
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
					ListarCasos listarCasos = new ListarCasos(listadoCasos);
			        UiApplication.getUiApplication().pushScreen(listarCasos);
				}
			});
	    }
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

	public void fieldChanged(Field field, int context) {
		if (field == consultarButtom){
			consultarListadoDeCaso();
		}	
		
	}

}
