package com.atencion24.ventanas;

import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.HeaderBar;
import com.atencion24.interfaz.SpacerField;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
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
	HorizontalFieldManager screen; 
	
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
	
		
		//add(new SpacerField());
		
		//Add Señal del teléfono
		HorizontalFieldManager hfmSignal = new HorizontalFieldManager(FIELD_RIGHT);
		HeaderBar bar = new HeaderBar();
		hfmSignal.add(bar);
		add(hfmSignal);
		add(new SeparatorField());
		
		//Add logo
		EncodedImage image = EncodedImage.getEncodedImageResource("com/atencion24/imagenes/logo.png");
		EncodedImage m = sizeImage(image, (Display.getWidth()*6)/10 ,((Display.getWidth()*6)/10)/4);
		BitmapField logoField = new BitmapField();
		logoField.setImage(m);	
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
	    hfmLabel.add(logoField);
	    add(hfmLabel);
	    
		add(new SpacerField());
		add(new SeparatorField());
	}	
	

	public EncodedImage sizeImage(EncodedImage image, int width, int height) 
	{
		 EncodedImage result = null;
		 int currentWidthFixed32 = Fixed32.toFP(image.getWidth());
		 int currentHeightFixed32 = Fixed32.toFP(image.getHeight());
		 int requiredWidthFixed32 = Fixed32.toFP(width);
		 int requiredHeightFixed32 = Fixed32.toFP(height);
		 int scaleXFixed32 = Fixed32.div(currentWidthFixed32, requiredWidthFixed32);
		 int scaleYFixed32 = Fixed32.div(currentHeightFixed32, requiredHeightFixed32);
		 result = image.scaleImage32(scaleXFixed32, scaleYFixed32);
		 return result;
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
