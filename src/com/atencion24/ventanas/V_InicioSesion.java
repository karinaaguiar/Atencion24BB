/*
 * V_Inicio_Sesion.java
 */
package com.atencion24.ventanas;

import com.atencion24.control.HttpConexion;
import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.GridFieldManager;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;


/**
 * @author Karina Aguiar 
 * Ventana de inicio de sesión 
 *
 */
public class V_InicioSesion extends plantilla_screen_http implements FieldChangeListener {

	final int MAX_CLAVE = 6;
	EditField nombreusuarioField;
	LabelField nombreusuarioLabel;
	PasswordEditField passwordField;
	LabelField passwordLabel;
	CustomButtonField accederButtom;
	
	/**
	 * Constructor de la ventana inicio de sesión
	 */
	public V_InicioSesion() { 
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Introduzca sus datos de acceso:");
		super.changeTitulo();
		
		//Campos Nombre de usuario y clave
		nombreusuarioField = new EditField("Usuario: ", "", 20, Field.FIELD_LEFT);//new EditField();
		//nombreusuarioLabel = new LabelField("Usuario: ", Field.FIELD_LEFT);
		
		passwordField = new PasswordEditField("", "", MAX_CLAVE, Field.EDITABLE);
		passwordLabel = new LabelField("Contraseña: ", Field.FIELD_LEFT);
		
        //gridFieldManager.add(nombreusuarioLabel);
        add(nombreusuarioField);
        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManager.add(passwordLabel);
        gridFieldManager.add(passwordField);
        add(gridFieldManager);
        
		//**Botones
		accederButtom = new CustomButtonField(" Acceder ", Color.WHITE, 0x990000 , Color.WHITE, 0xE38311, 0);
		accederButtom.setChangeListener(this);
		HorizontalFieldManager buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
		buttonManager.add(accederButtom);
		add(buttonManager);
		
	}

	/**
	 * Esta función verifica que el usuario haya ingresado todos los campos.
	 * De ser así se comunica con el servidor para verificar que los datos 
	 * introducidos sean válidos
	 */
	private void iniciarSesion(){
		if (nombreusuarioField.getTextLength() == 0 || passwordField.getTextLength() == 0){
			//UiApplication.getUiApplication().pushModalScreen(new CustomDialog("Debe completar ambos campos para poder acceder al sistema"));
			Dialog.alert("Debe completar todos los campos para poder acceder al sistema");
		}
		else{
			String usuario = nombreusuarioField.getText();
			String clave = passwordField.getText();
			HttpConexion thread = new HttpConexion("/InicioSesion?usuario_tb=" + usuario + "&clave_tb=" + clave, "GET", this);
			thread.start();
		}
			
	}
	/* (non-Javadoc)
	 * @see net.rim.device.api.ui.FieldChangeListener#fieldChanged(net.rim.device.api.ui.Field, int)
	 */
	public void fieldChanged(Field field, int context) {
		if (field == accederButtom){
			iniciarSesion();
		}	
	}

	public void llamadaExitosa( String result)
	{
		System.out.println("stoy en llamada exitosa");
		System.out.println("Respuesta servidor orignal "+ result);
		XMLParser envioXml = new XMLParser();
	    String xmlInterno = envioXml.extraerCapaWebService(result);
	    System.out.println("stoy en llamada exitosa dspues de extraer capa Web services " +xmlInterno);
	    final Sesion usu = envioXml.LeerXMLInicio(xmlInterno);
	    System.out.println("stoy en llamada exitosa dspues de leer XML Inicio");
	    if (usu == null)
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
					LoginSuccessScreen loginSuccessScreen = new LoginSuccessScreen(usu);
			        UiApplication.getUiApplication().pushScreen(loginSuccessScreen);
				}
			});
	    }
	}

	public void llamadaFallada(final String error){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog.alert("Error de conexión: " + error);
			}
		}); 
	}
}   




