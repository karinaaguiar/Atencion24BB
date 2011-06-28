/*
 * LoginSuccessScreen.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */

package com.atencion24.ventanas;

import com.atencion24.control.ReporteForecast;
import com.atencion24.control.Sesion;
import com.atencion24.interfaz.CustomButtonField;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.GridFieldManager;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.ListStyleLabelField;

import net.rim.device.api.ui.component.Dialog;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.system.Bitmap;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.FieldChangeListener;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.NullField;

import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.container.HorizontalFieldManager;


public class ConsultarHonorariosFacturados extends plantilla_screen implements FieldChangeListener  {
    
	Sesion sesion;
	
	Manager foreground;
	GridFieldManager gridFieldManager, gridFieldManagerSalJugMes;
    VerticalFieldManager buttonManagerV;
    HorizontalFieldManager buttonManager;

    ListStyleButtonSet contenido   = new ListStyleButtonSet();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
    VerticalFieldManager mainVerticalManager = new VerticalFieldManager(Field.FIELD_HCENTER);

	BitmapField bitmapField;
	CustomButtonField verReporCom;
	
	private int boton = 0;
    private int nivel = 0;
    
    //Posicion del boton pulsado
    int[] pos;
    
    ReporteForecast reporteF = new ReporteForecast();
    
    HorizontalFieldManager mainHorizontalManager = new HorizontalFieldManager();

    int posBotonPresionado = 0;
    
    
    public ConsultarHonorariosFacturados(Sesion usu) {
        super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
        
        this.sesion = usu;
        
        //Cambiar el font de la aplicación
		try {
				FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
				Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
				setFont(appFont);
			}catch (ClassNotFoundException e){}
		
		//Logo CSS alineado al centro
		Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
		bitmapField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
        hfmLabel.add(bitmapField);
        add(hfmLabel);
       
        //**Label field simple**
		add(new CustomLabelField("Honorarios Pagados", Color.WHITE, 0x600808, FIELD_HCENTER));
		add(new SeparatorField());
		add(new CustomLabelField("Bienvenido " +sesion.getNombre() + " " + sesion.getApellido() , Color.WHITE,  0x600808 , Field.USE_ALL_WIDTH));

		foreground = new ForegroundManager();
		
        gridFieldManager = new GridFieldManager(2, 0);
        gridFieldManagerSalJugMes = new GridFieldManager(2, 0);

        //Espacio en blanco antes de desplegar campos de búsqueda	
        LabelField espacio = new LabelField("", Field.FIELD_RIGHT);
        gridFieldManager.add(espacio);
        gridFieldManager.add(new NullField());
        
        foreground.add(gridFieldManager);
        //Agregamos el manager a pesar de que por los momentos no tiene nada
        foreground.add(gridFieldManagerSalJugMes);
        
        verReporCom = new CustomButtonField("Ver Reporte", Color.WHITE, Color.DARKGRAY, Color.WHITE, Color.CORNFLOWERBLUE , 0);
        verReporCom.setChangeListener(this);
        
        //Espacio en blanco antes del boton (ojo para meter otro boton luego)
        LabelField espacioLinea = new LabelField(" ", Field.FIELD_RIGHT);
        buttonManagerV = new VerticalFieldManager(FIELD_HCENTER);
        buttonManagerV.add(espacioLinea);
        foreground.add(buttonManagerV);
        
        //Coloco el boton
        buttonManager = new HorizontalFieldManager(FIELD_HCENTER);
        buttonManager.add(verReporCom); 
        foreground.add(buttonManager);
        
        //Espacio en blanco despues del boton
        LabelField espacio2 = new LabelField("", Field.FIELD_RIGHT);
        foreground.add(espacio2);
        
        foreground.add(new SeparatorField());

        foreground.add(contenido);
        foreground.add(mainVerticalManager);
        
        add( foreground );
    }
    
   public void crearMenu(){
       contenido.deleteAll();
       mainVerticalManager.deleteAll();
       ListStyleLabelField Titulo = new ListStyleLabelField( "Honorarios Pagados", DrawStyle.HCENTER , 0x400000, Color.WHITE);
       contenido.add(Titulo);
       botonesF = reporteF.mostrarBotones();
       for (int i = 0; i < botonesF.length; i++){
            botonesF[i].setChangeListener(this);
            contenido.add(botonesF[i]);
       }
    }
    
   public void crearParteMenu(){
       try{
           // Los botones que vienen despues del boton presionado son eliminados
            for (int i = posBotonPresionado + 2; i < botonesF.length + 1; i++){
                
                    contenido.delete(botonesF[i-1]);
                }
            // Busco los nuevos botones
            botonesF = reporteF.mostrarBotones();
            // Coloco en la pantalla los botones nuevos, ignorando los que ya tengo 
            // guardados (el boton presionado y los que vienen antes de el)
            for (int i = posBotonPresionado + 1; i < botonesF.length; i++){
                    botonesF[i].setChangeListener(this);
                    contenido.add(botonesF[i]);
            }
            //Guardo en mi arreglo los botones que no fueron borrados,
            //para poder hacer bien la comparacion en la función fieldChanged
            for (int i = 0; i < posBotonPresionado + 1; i++){
                botonesF[i] = (CustomButtonTable)contenido.getField(i + 1);
                }
        }catch(IndexOutOfBoundsException e){
            System.out.println("Error: " + e);
        }
       
    }
    
    public void llamadaExitosa(String xml){
    		//Con el String XML que recibo del servidor debo hacer llamada
    		//a mi parser XML para que se encargue de darme el 
    		//XML que me ha enviado el servidor procesado como 
    		//un objeto de control. 
    		//El parser XML debe distinguir si recibió una respuesta de 
    		//ConsultarProximoPago o de consultar ConsultarHistoricoPagos
    		//o puedo tener una variable global que según la opción que marque el usuario en el
    		//radiobuton me diga si estoy en el caso de pago en proceso o historico de pagos. 
    		
    		//Si estoy en el caso del historico debo crear mis info_nivel
    		//Y crear el menu 
            synchronized (UiApplication.getEventLock()) {
                if (boton == 2)
                {
                    String[] sals = {"1", "2","3", "4","5", "6","7", "8","9", "10","11", "12","13", "14", "15", "16"};
                    reporteF.valoresReporte(sals, nivel, "SalaX", "JuegoX", "MesaX");
                    crearMenu();                        
                }
                if (boton == 3){
                    String[][] sals = {{"sala5"}, {"sala6"}};
                    reporteF.guardarNivel( pos, sals, "fechaIn", "turnoS", "Inst");
                    crearParteMenu();  
                }
            }
        }
        
    public void llamadaFallada(final String error){
        UiApplication.getUiApplication().invokeLater(new Runnable() {
            public void run() {
                Dialog.alert("Error de conección: " + error);
            }
        });
    }
        
    public void buscarReporteCom(){ 
        boton = 2;
        nivel = 1;
        llamadaExitosa("hola");
    }
    
    public void desplegarMenu(CustomButtonTable botonPulsado){
        if (botonPulsado.obtenerNivel() != 0){
            pos = botonPulsado.obtenerPosicion();
            String p = reporteF.desplegarBotones( pos, "Hoy", "TurnoX", "IntitucionX", false);
            
            if (p != null)
            {
                boton = 3;
                llamadaExitosa("hola");
            }
            else
            {	
                crearParteMenu();                        
            }
        }
    }
    
    public void fieldChanged(Field field, int context) {
        if (field == verReporCom)
            buscarReporteCom();
        else {
            for (int i = 0; i < botonesF.length; i++){
                if (field == botonesF[i]){
                    posBotonPresionado = i;
                    desplegarMenu(botonesF[i]);
                    break;
                }
            }
        }
    }
}
/*
package com.atencion24.ventanas;

import net.rim.device.api.ui.component.LabelField;

import com.atencion24.control.Sesion;

public class ConsultarHonorariosFacturados extends plantilla_screen {

	ConsultarHonorariosFacturados(Sesion sesion) {
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		add(new LabelField("Honorarios Facturados"));
		// TODO Auto-generated constructor stub
	}

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub

	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub

	}

}*/
