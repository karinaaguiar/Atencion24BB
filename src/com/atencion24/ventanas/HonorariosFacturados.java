package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Vector;

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
		
		//Inserto los managers donde irá el reporte.
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
            
            CustomButtonTable boton;
            CustomButtonTableNotFocus encabezado;
            Enumeration detallefact = facturado.elements();
            
            //Encabezado
            encabezado = new CustomButtonTableNotFocus(" Unidad de Negocio ", "Monto" , Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            CustomButtonTable botonS = new CustomButtonTable(" Hospitalización:", detallefact.nextElement() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            botonS.setFont(appFont);
            fieldManager.add(botonS);
            
            boton = new CustomButtonTable(" Emergencia:", detallefact.nextElement() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            boton = new CustomButtonTable(" Cirugía Ambulatoria:", detallefact.nextElement() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            boton = new CustomButtonTable(" Convenios:", detallefact.nextElement() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            encabezado = new CustomButtonTableNotFocus(" Total facturado:", detallefact.nextElement()+ " Bs ", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            contenido.add(fieldManager);
            botonS.setFocus();
        }         
        catch (ClassNotFoundException e) {}
    }
	
	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "¿Está seguro que desea cerrar sesión y salir?");
		if (dialog == Dialog.YES)
		{
			//Debería hacer cierre de sesion
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
		menu.add(new MenuItem("Ir atrás", 20,10) {
			public void run(){
				irAtras();
			}
		});
		menu.add(new MenuItem("Ir a inicio", 20,10) {
			public void run(){
				irInicio();
			}
		});
		menu.add(new MenuItem("Cerrar Sesión", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub
		
	}

	public void llamadaFallada(String respuesta) {
		// TODO Auto-generated method stub
		
	}

}
