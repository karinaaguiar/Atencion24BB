/*
 * V_Inicio_Sesion.java
 */
package com.atencion24.ventanas;

import com.atencion24.control.HttpConexion;
import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.SpacerField;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
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
	boolean primeraVez = true;
	
	
	/**
	 * Constructor de la ventana inicio de sesión
	 */
	public V_InicioSesion() { 
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Introduzca sus datos de acceso:");
		super.changeTitulo();
		super.changeSubTitulo();
		
		add(new SpacerField());
        add(new SpacerField());
		//Campos Nombre de usuario y clave
		nombreusuarioField = new EditField("Usuario: ", "", 20, EditField.NO_NEWLINE);//new EditField();
		//nombreusuarioLabel = new LabelField("Usuario: ", Field.FIELD_LEFT);
		
		passwordField = new PasswordEditField("", "", MAX_CLAVE, Field.EDITABLE);
		passwordLabel = new LabelField("Contraseña: ", Field.FIELD_LEFT);
		
        //gridFieldManager.add(nombreusuarioLabel);
        add(nombreusuarioField);
        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManager.add(passwordLabel);
        gridFieldManager.add(passwordField);
        add(gridFieldManager);
        
        add(new SpacerField());
        add(new SpacerField());
        add(new SpacerField());
        
		//**Botones
		accederButtom = new CustomButtonField(" Acceder ", Color.WHITE, 0x990000 , Color.WHITE, 0xF77100, 0); //0xE38311
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
			if(primeraVez)
			{
				HttpConexion thread = new HttpConexion("/InicioSesion?usuario_tb=" + usuario + "&clave_tb=" + clave, "GET", this, true);
				thread.start();
			}
			else
			{
				HttpConexion thread = new HttpConexion("/InicioSesion?usuario_tb=" + usuario + "&clave_tb=" + clave, "GET", this);
				thread.start();
			}
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
		primeraVez = false;
		System.out.println("stoy en llamada exitosa");
		System.out.println("Respuesta servidor orignal "+ result);
		XMLParser envioXml = new XMLParser();
	    String xmlInterno = envioXml.extraerCapaWebService(result);
	    System.out.println("stoy en llamada exitosa dspues de extraer capa Web services " +xmlInterno);
	    final Sesion usu = envioXml.LeerXMLInicio(xmlInterno);
	    final String cookie = this.getcookie();
	    System.out.println("El cookie de la ventana inicio de sesion es " + cookie);
	    System.out.println("stoy en llamada exitosa dspues de leer XML Inicio");
	    if (usu == null)
	    {
	        final String mostrarError = envioXml.obtenerError();
	        if(!(mostrarError.equals("502")))
	        {	
		        UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert(mostrarError);
					}
				});
	        }
	        else 
	        {
	        	nombreusuarioField.setText("");
				passwordField.setText("");
				primeraVez = true;
				setcookie("");
	        	UiApplication.getUiApplication().invokeLater(new Runnable() {
					public void run() {
						Dialog.alert("Su usuario ha sido desbloqueado. Intente iniciar sesión nuevamente");
						
					}
				});
	        	
	        }
	    }
	    else
	    {
	    	UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					LoginSuccessScreen loginSuccessScreen = new LoginSuccessScreen(usu);
					loginSuccessScreen.setcookie(cookie); 
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
	
	//Sobreescribes el metodo makeMenu y le agregas sus menuItems
	protected void makeMenu(Menu menu, int instance){
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Iniciar Sesion", 20,10) {
			public void run(){
				iniciarSesion();
			}
		});
	}
}   




