package com.atencion24.interfaz;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Keypad;

/**
 * Elemento de interfaz que representa una celda de una tabla
 * label ->  texto del lado izquierdo en la celda
 * label2 -> texto del lado derecho en la celda
 * foregroundColor -> color del texto 
 * backgroundColor -> color del fondo 
 * focusedForegroundColor -> color del texto cuando la celda tiene el foco
 * focusedBackgroundColor -> color del fondo cuando la celda tiene el foco
 * focusedShadowColor -> color para indicar que el foco está sobre la celda
 * color_border -> color de los bordes de la celda
 * nivel -> nivel al que pertenece la celda dentro del posible anidamiento
 * posicion -> arreglo que permite determinar la posicion relativa del celda respecto al resto de las celdas. 
 *  	       pos[nivel] representa la posición de la celda dentro del nivel al que pertenece.
 *  	       pos[nivel-i] representa la posición de su padre del nivel i  
 *  _leftIcon -> Bitmap de la celda (en caso de tener)
 *  _leftOffset -> margen izquierdo que se deberá mantener al dibujar el texto de la celda.  
 */
public class CustomButtonTable extends Field {
         
    private String label;
    private String label2;
    private int foregroundColor;
    private int backgroundColor;
    private int focusedForegroundColor;
    private int focusedBackgroundColor;
    private int focusedShadowColor;
    private int color_border = 0;   
    private int nivel;
    private int[] posicion;
    private Bitmap _leftIcon;
    private int _leftOffset;
    private int tabulado;
    
    private static final int HPADDING = 6; //Display.getWidth() <= 320 ? 6 : 8;
    private static final int VPADDING = 4;
    
    public CustomButtonTable(){}
    /**
     * Constructor de la clase. Con ícono 
     * @param icon Bitmap de la celda 
     * @param label texto del lado izquierdo en la celda
     * @param labelR texto del lado derecho en la celda
     * @param focusedShadowColor color para indicar que el foco está sobre la celda
     * @param foregroundColor color del texto 
     * @param backgroundColor color del fondo 
     * @param focusedForegroundColor color del texto cuando la celda tiene el foco
     * @param focusedBackgroundColor color del fondo cuando la celda tiene el foco
     * @param style estilo del botón (field) 
     * @param colorB color de los bordes de la celda
     * @param nvl nivel al que pertenece la celda dentro del posible anidamiento
     * @param pos arreglo que permite determinar la posicion relativa del celda respecto al resto de las celdas. 
     *  	  pos[nivel] representa la posición de la celda dentro del nivel al que pertenece.
     *  	  pos[nivel-i] representa la posición de su padre del nivel i  
     */
    public CustomButtonTable(Bitmap icon, String label, String labelR, int focusedShadowColor, int foregroundColor, int backgroundColor, int focusedForegroundColor, int focusedBackgroundColor, long style, int colorB, int nvl, int[] pos) {
        super(style);
        this.label = label;
        this.label2 = labelR;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.focusedForegroundColor = focusedForegroundColor;
        this.focusedBackgroundColor = focusedBackgroundColor;
        this.focusedShadowColor = focusedShadowColor;
        this.color_border = colorB;
        this.setNivel(nvl);
        this.posicion = pos;
        set_leftIcon(icon);
        }
    /**
     * Constructor de la clase. Con ícono y tabulado
     * 
     * **/
    public CustomButtonTable(Bitmap icon, String label, String labelR, int focusedShadowColor, int foregroundColor, int backgroundColor, int focusedForegroundColor, int focusedBackgroundColor, long style, int colorB, int nvl, int[] pos, int tabulado) {
        super(style);
        this.label = label;
        this.label2 = labelR;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.focusedForegroundColor = focusedForegroundColor;
        this.focusedBackgroundColor = focusedBackgroundColor;
        this.focusedShadowColor = focusedShadowColor;
        this.color_border = colorB;
        this.setNivel(nvl);
        this.posicion = pos;
        this.tabulado = tabulado;
        set_leftIcon(icon);
        }
    
    /**
     * Constructor de la clase. Sin ícono 
     * @param label texto del lado izquierdo en la celda
     * @param labelR texto del lado derecho en la celda
     * @param focusedShadowColor color para indicar que el foco está sobre la celda
     * @param foregroundColor color del texto 
     * @param backgroundColor color del fondo 
     * @param focusedForegroundColor color del texto cuando la celda tiene el foco
     * @param focusedBackgroundColor color del fondo cuando la celda tiene el foco
     * @param style estilo del botón (field) 
     * @param colorB color de los bordes de la celda
     * @param nvl nivel al que pertenece la celda dentro del posible anidamiento
     * @param pos arreglo que permite determinar la posicion relativa del celda respecto al resto de las celdas. 
 *  	          pos[nivel] representa la posición de la celda dentro del nivel al que pertenece.
 *  	          pos[nivel-i] representa la posición de su padre del nivel i  
     */
    public CustomButtonTable(String label, String labelR, int focusedShadowColor, int foregroundColor, int backgroundColor, int focusedForegroundColor, int focusedBackgroundColor, long style, int colorB, int nvl, int[] pos) {
        super(style);
        this.label = label;
        this.label2 = labelR;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.focusedForegroundColor = focusedForegroundColor;
        this.focusedBackgroundColor = focusedBackgroundColor;
        this.focusedShadowColor = focusedShadowColor;
        this.color_border = colorB;
        this.setNivel(nvl);
        this.posicion = pos;
        }
    /**
     * Constructor de la clase. Sin ícono y tabulado
     * 
     **/
    public CustomButtonTable(String label, String labelR, int focusedShadowColor, int foregroundColor, int backgroundColor, int focusedForegroundColor, int focusedBackgroundColor, long style, int colorB, int nvl, int[] pos, int tabulado) {
        super(style);
        this.label = label;
        this.label2 = labelR;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.focusedForegroundColor = focusedForegroundColor;
        this.focusedBackgroundColor = focusedBackgroundColor;
        this.focusedShadowColor = focusedShadowColor;
        this.color_border = colorB;
        this.setNivel(nvl);
        this.tabulado = tabulado;
        this.posicion = pos;
    }
       
    public int obtenerNivel(){
        return getNivel();
        }
        
    public int [] obtenerPosicion(){
        return posicion;
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#layout(int, int)
     */
    protected void layout(int width, int height) {
        
    	_leftOffset = HPADDING;
        if( _leftIcon != null ) {
            _leftOffset += _leftIcon.getWidth() + HPADDING;
        }
        
    	if (((getStyle() & Field.USE_ALL_WIDTH) == Field.USE_ALL_WIDTH ) || label == null) {
            setExtent(width, Math.min(height, getPreferredHeight()));
        }
        else {
            if (label != null){
                setExtent(width, Math.min(height, getPreferredHeight()));
            }else{
                setExtent(getPreferredWidth(), getPreferredHeight());
            }
        }
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#getPreferredHeight()
     */
    public int getPreferredHeight() {
    	if (get_leftIcon() != null) {
            return Math.max(getFont().getHeight(), get_leftIcon().getHeight());
        }
    	else {
            return getFont().getHeight();
        }
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#getPreferredWidth()
     */
    public int getPreferredWidth() {
        int width;
        if (label2 == null){
            width = getFont().getAdvance(label);
            width += getFont().getAdvance(label2);
            if (get_leftIcon() != null) {
                width += get_leftIcon().getWidth();
            }
        }
        else {
            width = getFont().getAdvance(label);
            if (get_leftIcon() != null) {
                width += get_leftIcon().getWidth();
            }
        }
        return width;
    }
    
    public void auxiliarPaint(Graphics graphics, int color)
    {
    	if (label2 != null)
        {
            int text2X = (getWidth() - getFont().getAdvance(label2))-2;
            graphics.setColor(color);
            // Left Bitmap
            if( get_leftIcon() != null ) {
            	if(tabulado != 0)
            	{	
            		graphics.drawBitmap( _leftOffset*tabulado, VPADDING, get_leftIcon().getWidth(), get_leftIcon().getHeight(), get_leftIcon(), 0, 0 );
            		graphics.drawText(label, _leftOffset*tabulado + _leftOffset -HPADDING, 0, DrawStyle.ELLIPSIS, text2X - 1);
            	}
            	else
            	{	
            		graphics.drawBitmap( HPADDING, VPADDING, get_leftIcon().getWidth(), get_leftIcon().getHeight(), get_leftIcon(), 0, 0 );
            		graphics.drawText(label, _leftOffset, 0, DrawStyle.ELLIPSIS, text2X - 1);
            	}
            }
            else
            {	
            	if(tabulado != 0)
            	{	
            		graphics.drawText(label, 4*_leftOffset*tabulado , 0, DrawStyle.ELLIPSIS, text2X - 1);
            	}
            	else
            	{	
            		graphics.drawText(label, _leftOffset, 0, DrawStyle.ELLIPSIS, text2X - 1);
            	}
            }
            graphics.drawText(label2, text2X, 0);
            
        }
        else
        {
        	graphics.setColor(color);
            // Left Bitmap
            if( get_leftIcon() != null ) 
            {
            	if(tabulado != 0)
            	{	
            		graphics.drawBitmap( _leftOffset*tabulado, VPADDING, get_leftIcon().getWidth(), get_leftIcon().getHeight(), get_leftIcon(), 0, 0 );
            		graphics.drawText(label, _leftOffset*tabulado + _leftOffset -HPADDING, 0, DrawStyle.ELLIPSIS);
            	}	
            	else
            	{	
            		graphics.drawBitmap( HPADDING, VPADDING, get_leftIcon().getWidth(), get_leftIcon().getHeight(), get_leftIcon(), 0, 0 );
            		graphics.drawText(label, _leftOffset, 0, DrawStyle.ELLIPSIS);
            	}	
            }
            else
            {
            	if(tabulado != 0)
            	{	
            		graphics.drawText(label, 4*_leftOffset*tabulado , 0, DrawStyle.ELLIPSIS);
            	}
            	else
            	{	
            		graphics.drawText(label, _leftOffset, 0, DrawStyle.ELLIPSIS);
            	}
            }
        }
    	
    }
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#paint(net.rim.device.api.ui.Graphics)
     */
    protected void paint(Graphics graphics) {
        graphics.setBackgroundColor(backgroundColor);
        graphics.clear();
        
        if (color_border != 0)
        {              
            graphics.setColor( color_border );
            graphics.drawLine( 0, 0, 0, getHeight());
            graphics.drawLine( getWidth()-1, 0, getWidth()-1, getHeight());
        }
        if (isFocus()) 
        {
            graphics.setColor(focusedBackgroundColor);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.setColor(focusedShadowColor);
            graphics.setGlobalAlpha(100);
            graphics.fillRoundRect(3, 3, getWidth(), getHeight()-6, 12, 12);
            graphics.setGlobalAlpha(255);
            auxiliarPaint(graphics, focusedForegroundColor);
        }else
        {
        	auxiliarPaint(graphics, foregroundColor);
        }
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#isFocusable()
     */
    public boolean isFocusable() {
        return true;
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#drawFocus(net.rim.device.api.ui.Graphics, boolean)
     */
    protected void drawFocus(Graphics graphics, boolean on) {
        }
        
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#onFocus(int)
     */
    protected void onFocus(int direction) {
        super.onFocus(direction);
        invalidate();
    }
        
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#onUnfocus()
     */
    protected void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }
        
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#navigationClick(int, int)
     */
    protected boolean navigationClick(int status, int time) {
        fieldChangeNotify(0);
        return true;
    }
        
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#keyChar(char, int, int)
     */
    protected boolean keyChar(char character, int status, int time) {
        if (character == Keypad.KEY_ENTER) {
            fieldChangeNotify(0);
            return true;
        }
            return super.keyChar(character, status, time);
    }

	public void set_leftIcon(Bitmap _leftIcon) {
		this._leftIcon = _leftIcon;
	}

	public Bitmap get_leftIcon() {
		return _leftIcon;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getNivel() {
		return nivel;
	}

}
