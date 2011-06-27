/*
 * V_Inicio_Sesion.java
 */
package com.atencion24.ventanas;

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
 * Ventana de inicio de sesión 
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
	 * Constructor de la ventana inicio de sesión
	 */
	public V_InicioSesion() { 
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		
		//Cambiar el font de la aplicación
		try {
				FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
				Font appFont = familiaFont.getFont(Font.PLAIN, 9, Ui.UNITS_pt);
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
		add(new CustomLabelField("Por favor introduzca sus datos de acceso:", Color.WHITE, Color.BROWN, Field.USE_ALL_WIDTH));
		add(new SeparatorField());
		
		//Campos Nombre de usuario y clave
		nombreusuarioField = new EditField();
		nombreusuarioLabel = new LabelField("Usuario: ", Field.FIELD_LEFT);
		
		passwordField = new PasswordEditField("", "", MAX_CLAVE, Field.EDITABLE);
		passwordLabel = new LabelField("Contraseña: ", Field.FIELD_LEFT);
		
        GridFieldManager gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManager.add(nombreusuarioLabel);
        gridFieldManager.add(nombreusuarioField);
        gridFieldManager.add(passwordLabel);
        gridFieldManager.add(passwordField);
        add(gridFieldManager);
        
		
		//**Botones
		accederButtom = new CustomButtonField(" Acceder ", Color.WHITE, Color.ORANGE, Color.WHITE, Color.CORNFLOWERBLUE , 0);
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
			LoginSuccessScreen loginSuccessScreen = 
				new LoginSuccessScreen(usuario, clave);
			UiApplication.getUiApplication().pushScreen(loginSuccessScreen);
			//Connection coneccion = new Connection("/InicioSesion?usuario_tb=" + usuario + "&clave_tb=" + clave, this);
		    //coneccion.start(); 
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

	/*public void llamadaExitosa( String result){
	
	    synchronized (UiApplication.getEventLock()) {
	        XMLCreator envioXml = new XMLCreator();
	        String xmlInterno = envioXml.extraerCapaWebService(result);
	        //String xmlInterno = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Datos><Usuario><nombreValido>Global</nombreValido><apellidoValido>Consultants</apellidoValido><cedulaValido>0</cedulaValido><keySetOperValido>53</keySetOperValido><loginValido>global</loginValido></Usuario><Permisos><Modulos><Modulo0><Nombre>Reportes</Nombre><KeyMod>1</KeyMod></Modulo0><Modulo1><Nombre>Administracion</Nombre><KeyMod>2</KeyMod></Modulo1><Modulo2><Nombre>Descargas</Nombre><KeyMod>4</KeyMod></Modulo2></Modulos><Transacciones><Transacciones0><KeyMod>1</KeyMod><Nombre>Master Gaming Reports</Nombre></Transacciones0><Transacciones1><KeyMod>1</KeyMod><Nombre>Control de Caja</Nombre></Transacciones1><Transacciones2><KeyMod>1</KeyMod><Nombre>Cheques Ajustados</Nombre></Transacciones2><Transacciones3><KeyMod>1</KeyMod><Nombre>Forecast</Nombre></Transacciones3><Transacciones4><KeyMod>1</KeyMod><Nombre>Bitacora</Nombre></Transacciones4><Transacciones5><KeyMod>1</KeyMod><Nombre>Produccion Consolidado</Nombre></Transacciones5><Transacciones6><KeyMod>1</KeyMod><Nombre>Excepciones</Nombre></Transacciones6><Transacciones7><KeyMod>1</KeyMod><Nombre>Relacion IP</Nombre></Transacciones7><Transacciones8><KeyMod>2</KeyMod><Nombre>Usuarios</Nombre></Transacciones8><Transacciones9><KeyMod>2</KeyMod><Nombre>Roles</Nombre></Transacciones9><Transacciones10><KeyMod>2</KeyMod><Nombre>Aplicaciones</Nombre></Transacciones10><Transacciones11><KeyMod>2</KeyMod><Nombre>Modulos</Nombre></Transacciones11><Transacciones12><KeyMod>2</KeyMod><Nombre>Transacciones</Nombre></Transacciones12><Transacciones13><KeyMod>2</KeyMod><Nombre>Acciones</Nombre></Transacciones13><Transacciones14><KeyMod>2</KeyMod><Nombre>Operaciones</Nombre></Transacciones14><Transacciones15><KeyMod>4</KeyMod><Nombre>Produccion Mesa</Nombre></Transacciones15><Transacciones16><KeyMod>4</KeyMod><Nombre>ForeCast vs. MGR</Nombre></Transacciones16><Transacciones17><KeyMod>4</KeyMod><Nombre>Prod. Máq. Individuales</Nombre></Transacciones17><Transacciones18><KeyMod>4</KeyMod><Nombre>Prod. Máq. Modulares</Nombre></Transacciones18><Transacciones19><KeyMod>4</KeyMod><Nombre>Rel. Cont. Individuales</Nombre></Transacciones19><Transacciones20><KeyMod>4</KeyMod><Nombre>Rel. Cont. Modulares</Nombre></Transacciones20></Transacciones><Establecimientos><Establecimiento0><KeyOper>16</KeyOper><Nombre>Bingo Caribbean</Nombre></Establecimiento0><Establecimiento1><KeyOper>32</KeyOper><Nombre>Casino Tamanaco</Nombre></Establecimiento1><Establecimiento2><KeyOper>1</KeyOper><Nombre>Fiesta Casino Guayana</Nombre></Establecimiento2><Establecimiento3><KeyOper>4</KeyOper><Nombre>SunWay Casino</Nombre></Establecimiento3></Establecimientos></Permisos></Datos>";
	        Sesion usu = envioXml.LeerXMLInicio(xmlInterno);
	        if (usu == null){
	            final String mostrarError = envioXml.obtenerError();
	            System.out.println(mostrarError);
	            UiApplication.getUiApplication().invokeLater(new Runnable() {
	                public void run() {
	                    Dialog.alert(mostrarError);
	                }
	            });
	        }else{
	            usu.guardarClave(clave_usu);
	            LoginSuccessScreen loginSuccessScreen = new LoginSuccessScreen(usu);
	            UiApplication.getUiApplication().pushScreen(loginSuccessScreen);
	        }
	    }
	
	}*/

	public void llamadaFallada(final String error){
		UiApplication.getUiApplication().invokeLater(new Runnable() {
		    public void run() {
		    Dialog.alert("Error de conección: " + error);
		    }
		});
	}

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub
		
	}
}   




