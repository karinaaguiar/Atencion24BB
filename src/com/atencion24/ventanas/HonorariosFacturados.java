package com.atencion24.ventanas;

//import java.util.Enumeration;
import java.util.Vector;

import com.atencion24.control.Deduccion;
//import com.atencion24.control.Facturado;
import com.atencion24.control.HttpConexion;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomButtonTableNotFocus;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.ListStyleButtonSet;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class HonorariosFacturados extends plantilla_screen_http {

	static Vector facturado;
	String fechaI; 
	String fechaF;
	
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    VerticalFieldManager fieldManager;
	Manager foreground = new ForegroundManager();
	
	public HonorariosFacturados(Vector facturadoUDN, String fechaI, String fechaF) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Honorarios Facturados");
		super.changeTitulo();
		super.setSubTitulo("(" + fechaI + " - " + fechaF + ")");
		super.changeSubTitulo();
		facturado = facturadoUDN;
		
		//Inserto los managers donde ir� el reporte.
		foreground.add(contenido);
        add(foreground);
		
        crearReporte();
		
	}
	
	public void crearReporte()
    {
        contenido.deleteAll();
        fieldManager = new VerticalFieldManager();
        
        try {
            FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
            Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
            
            CustomButtonTable botonS = null;
            CustomButtonTableNotFocus encabezado;
            
            System.out.println("Numero de cosas por udn al inicio " + facturado.size());
            
            Deduccion totalFact = (Deduccion) facturado.firstElement();
            facturado.removeElementAt(0);
            
            
            encabezado = new CustomButtonTableNotFocus(" Unidad de Negocio ", "Monto Bs " , Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            System.out.println("Numero de cosas por udn " + facturado.size());
            
            if(facturado.size()>0)
            {

            	//Primer detalle por udn 
	            botonS = new CustomButtonTable(" "+ ((Deduccion) facturado.elementAt(0)).getConcepto() +":", ((Deduccion) facturado.elementAt(0)).getMonto() + " ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
	            botonS.setFont(appFont);
	            fieldManager.add(botonS);
	            
	            System.out.println("Numero de cosas por udn " + facturado.size());
	            
	            if(facturado.size()>1)
	            {
	            	for(int i = 1; i <facturado.size(); i++)
	            	{
	            		System.out.println("Entre ");
		            	CustomButtonTable boton = new CustomButtonTable(" "+ ((Deduccion) facturado.elementAt(i)).getConcepto() +":", ((Deduccion) facturado.elementAt(i)).getMonto() + " ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
		                boton.setFont(appFont);
		                fieldManager.add(boton);
	            	}
	            }	
            }
            
            //Total facturado
            encabezado = new CustomButtonTableNotFocus(" "+ totalFact.getConcepto()+":", totalFact.getMonto()+ " ", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            contenido.add(fieldManager);
            if(botonS != null) botonS.setFocus();
        }         
        catch (ClassNotFoundException e) {}
    }
	
	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "�Est� seguro que desea cerrar sesi�n y salir?");
		if (dialog == Dialog.YES)
		{
			//Deber�a hacer cierre de sesion
			HttpConexion thread = new HttpConexion("/cerrarSesion", "GET", this, false);
			thread.start();
			Dialog.alert("Hasta luego!");
			System.exit(0);
		}
	}
	
	public void irInicio()
	{
		UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
		UiApplication.getUiApplication().popScreen(this);
	}
	
	public void irAtras()
	{
		UiApplication.getUiApplication().popScreen(this);
	}
	
	//Sobreescribes el metodo makeMenu y le agregas sus menuItems
	protected void makeMenu(Menu menu, int instance){
		//super.makeMenu(menu, instance);
		menu.add(new MenuItem("Ir atr�s", 20,10) {
			public void run(){
				irAtras();
			}
		});
		menu.add(new MenuItem("Ir a inicio", 20,10) {
			public void run(){
				irInicio();
			}
		});
		menu.add(new MenuItem("Cerrar Sesi�n", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub
		
	}

	public void llamadaFallada(final String respuesta) {
		// TODO Auto-generated method stub
	}

}
