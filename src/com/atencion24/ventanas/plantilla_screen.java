package com.atencion24.ventanas;

import com.atencion24.interfaz.CustomLabelField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

/**
 * Esta clase abstracta define las pantallas (screen)
 * de la aplicación.  
 */
public abstract class plantilla_screen extends MainScreen {
	
	private String cookie="";
	private String titulo;
	private String subtitulo = null;
	private static Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
	
	/**
	 * Constructor de la clase 
	 * @param style define el estilo de la nueva pantalla 
	 */
	plantilla_screen (long style) 
	{
		super(style); 
		try {
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
			setFont(appFont);
		}catch (ClassNotFoundException e){}	
		
		//Logo alineado al centro
		BitmapField logoField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
	    hfmLabel.add(logoField);
	    add(hfmLabel);
		add(new SeparatorField());
	}	
	
	/**
	 * Metodo que despliega el titulo de la pantalla
	 */
	public void changeTitulo()
	{
		add(new CustomLabelField(this.titulo, Color.WHITE, 0x400000, FIELD_HCENTER));
		//add(new SeparatorField());
	}
	
	/**
	 * Metodo que asigna el titulo de la pantalla
	 * @param titulo titulo de la pantalla
	 */
	public void setTitulo(String titulo){
		this.titulo = titulo;
	}
	
	/**
	 * Metodo que despliega el titulo de la pantalla
	 */
	public void changeSubTitulo(){
		 
		if (subtitulo == null) add(new SeparatorField());
		else{
		   try 
		   {
			  FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			  Font appFont = familiaFont.getFont(Font.PLAIN, 6, Ui.UNITS_pt);
			  CustomLabelField label = new CustomLabelField(this.subtitulo, Color.WHITE, 0x400000, FIELD_HCENTER);
			  label.setFont(appFont);
			  add(label);
			  add(new SeparatorField());
		   }catch (ClassNotFoundException e){}
		}
	}
	
	/**
	 * Metodo que asigna el titulo de la pantalla
	 * @param titulo titulo de la pantalla
	 */
	public void setSubTitulo(String subtitulo){
		this.subtitulo = subtitulo;
	}
	
	public boolean onClose ()
	{
		setDirty(false);
		close();
		return true;
	}

	public void setcookie(String cookie) {
		this.cookie = cookie;
	}

	public String getcookie() {
		return cookie;
	}
	
}
