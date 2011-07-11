package com.atencion24.ventanas;

import java.util.Hashtable;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.SpacerField;

public class ConsultarDetalleDeCaso extends plantilla_screen_http implements FieldChangeListener {

	BitmapField logoField;
	EditField apellidoField;
	CustomButtonField consultarButtom;
	
	String codSeleccionado;
	
	ConsultarDetalleDeCaso(String codSeleccionado) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Detalle de un caso");
		super.changeTitulo();
		
		this.codSeleccionado = codSeleccionado;
		
		add(new SpacerField());
		add(new SeparatorField());
		
		try{
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font boldFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt).derive(Font.BOLD);
			CustomLabelField label = new CustomLabelField("  Introduzca el apellido del paciente: " , 0x400000, Color.WHITE, 0);
			label.setFont(boldFont);
			add(label);
		}catch (ClassNotFoundException e){}
		
		add(new SeparatorField());
		add(new SpacerField());
        add(new SpacerField());
		//Campos Nombre de usuario y clave
		apellidoField = new EditField("", "", 20, EditField.NO_NEWLINE);
		LabelField apellidoLabel = new LabelField("Apellido: ", Field.FIELD_LEFT);

        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManager.add(apellidoLabel);
        gridFieldManager.add(apellidoField);
        add(gridFieldManager);
        
        add(new SpacerField());
        add(new SpacerField());
        add(new SpacerField());
		//**Botones
		consultarButtom = new CustomButtonField(" Consultar ", Color.WHITE, 0x990000 , Color.WHITE, 0xF77100, 0);
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
			HttpConexion thread = new HttpConexion("/consultarListadoDeCaso?medico_tb=" + codSeleccionado + "&apellido_tb=" + apellido, "GET", this);
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
