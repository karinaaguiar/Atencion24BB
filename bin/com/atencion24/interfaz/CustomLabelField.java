/*
 * CustomLabelField.java 
 */ 
package com.atencion24.interfaz;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

/**
 * @author BeginningBlackBerryBDevelopment
 * Esta clase define unas etiquetas configurables
 * a las cuales se les puede cambiar el color de fondo
 * y el color del texto 
 *
 */
public class CustomLabelField extends Field {

	private String label;
	private int foregroundColor;
	private int backgroundColor;
	
	/**
	 * Constructor de la clase
	 * @param label texto de la etiqueta
	 * @param foregroundColor color de la letra
	 * @param backgroundColor color del fondo de la etiqueta
	 * @param style estilo del campo
	 */
	public CustomLabelField(String label, int foregroundColor, 
			int backgroundColor, long style){
		super(style);
		this.label = label;
		this.foregroundColor = foregroundColor;
		this.backgroundColor = backgroundColor;
	}
	
	
	//Definir el tamaño del LabelField
	/* (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#layout(int, int)
	 */
	protected void layout(int width, int height) {
		if((getStyle() & Field.USE_ALL_WIDTH)== Field.USE_ALL_WIDTH){
			setExtent(width, Math.min(height, getPreferredHeight()));
		}
		else{
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}
	
	//Para dibujar el label field
	/* (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#paint(net.rim.device.api.ui.Graphics)
	 */
	protected void paint(Graphics graphics) {
		graphics.setBackgroundColor(backgroundColor);
		graphics.clear();
		graphics.setColor(foregroundColor);
		graphics.drawText(label, 0, 0, DrawStyle.ELLIPSIS, getWidth());
	}
	
	//Altura ideal 
	/* (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#getPreferredHeight()
	 */
	public int getPreferredHeight(){
		return getFont().getHeight();
	}
	
	//Ancho ideal
	/* (non-Javadoc)
	 * @see net.rim.device.api.ui.Field#getPreferredWidth()
	 */
	public int getPreferredWidth(){
		return getFont().getAdvance(label);
	}

}
