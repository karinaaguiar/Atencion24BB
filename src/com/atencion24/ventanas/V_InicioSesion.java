/*
 * V_Inicio_Sesion.java
 */
package com.atencion24.ventanas;

import com.atencion24.control.HttpConexion;
import com.atencion24.control.Sesion;
import com.atencion24.control.XMLParser;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.GridFieldManager;

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
import net.rim.device.api.ui.component.PasswordEditField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;


/**
 * @author Karina Aguiar 
 * Ventana de inicio de sesi�n 
 *
 */
public class V_InicioSesion extends plantilla_screen implements FieldChangeListener {

	final int MAX_CLAVE = 6;
	BitmapField logoField;
	EditField nombreusuarioField;
	LabelField nombreusuarioLabel;
	PasswordEditField passwordField;
	LabelField passwordLabel;
	CustomButtonField accederButtom;
	
	/**
	 * Constructor de la ventana inicio de sesi�n
	 */
	public V_InicioSesion() { 
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		
		//Cambiar el font de la aplicaci�n
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
		add(new CustomLabelField("Introduzca sus datos de acceso:", Color.WHITE,  0x990000 , Field.USE_ALL_WIDTH));
		add(new SeparatorField());
		
		//Campos Nombre de usuario y clave
		nombreusuarioField = new EditField("Usuario: ", "", 20, Field.FIELD_LEFT);//new EditField();
		//nombreusuarioLabel = new LabelField("Usuario: ", Field.FIELD_LEFT);
		
		passwordField = new PasswordEditField("", "", MAX_CLAVE, Field.EDITABLE);
		passwordLabel = new LabelField("Contrase�a: ", Field.FIELD_LEFT);
		
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
	 * Esta funci�n verifica que el usuario haya ingresado todos los campos.
	 * De ser as� se comunica con el servidor para verificar que los datos 
	 * introducidos sean v�lidos
	 */
	private void iniciarSesion(){
		if (nombreusuarioField.getTextLength() == 0 || passwordField.getTextLength() == 0){
			//UiApplication.getUiApplication().pushModalScreen(new CustomDialog("Debe completar ambos campos para poder acceder al sistema"));
			Dialog.alert("Debe completar todos los campos para poder acceder al sistema");
		}
		else{
			String usuario = nombreusuarioField.getText();
			String clave = passwordField.getText();
			//LoginSuccessScreen loginSuccessScreen = new LoginSuccessScreen(usuario, clave);
			//UiApplication.getUiApplication().pushScreen(loginSuccessScreen);
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
				Dialog.alert("Error de conexi�n: " + error);
			}
		}); 
	}
}   




