/*
 * CustomButtonField.java
 */ 
package com.atencion24.interfaz;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;


/**
 * @author BeginningBlackBerryBDevelopment
 * Esta clase define botones configurables
 * a los cuales se les puede cambiar el color de fondo
 * el color del texto y el color de fondo cuando el foco 
 * esta sobre el botón 
 */
public class CustomButtonField extends Field {

    private String label;
    private int backgroundColor;
    private int foregroundColor;
    private int focusedForegroundColor;
    private int focusedBackgroundColor;
    
    /**
     * Constructor de la clase
     * @param label texto del botón
     * @param foregroundColor color del texto
     * @param backgroundColor color del fondo
     * @param focusedForegroundColor color del texto cuando el foco está sobre el botón
     * @param focusedBackgroundColor color del fondo cuando el foco está sobre el botón
     * @param style estilo del botón
     */
    public CustomButtonField(String label, int foregroundColor,
        int backgroundColor, int focusedForegroundColor,
        int focusedBackgroundColor,long style) {
                
            super(style);
            this.label = label;
            this.foregroundColor = foregroundColor;
            this.backgroundColor = backgroundColor;
            this.focusedForegroundColor = focusedForegroundColor;
            this.focusedBackgroundColor = focusedBackgroundColor;
    }

    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#getPreferredHeight()
     */
    public int getPreferredHeight() {
        return getFont().getHeight() + 8;
    }

    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#getPreferredWidth()
     */
    public int getPreferredWidth() {
        return getFont().getAdvance(label) + 8;
    }

    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#layout(int, int)
     */
    protected void layout(int width, int height) {
        setExtent
        (Math.min(width, getPreferredWidth()), Math.min(height,
        getPreferredHeight()));
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
        
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#paint(net.rim.device.api.ui.Graphics)
     */
    protected void paint(Graphics graphics) {
        if (isFocus()) {
            graphics.setColor(focusedBackgroundColor);
            graphics.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
            graphics.setColor(Color.WHITE);
            graphics.setGlobalAlpha(100);
            graphics.fillRoundRect(3, 3, getWidth()-6, getHeight()/2, 12, 12);
            graphics.setGlobalAlpha(255);
            graphics.setColor(focusedForegroundColor);
            graphics.drawText(label, 4, 4);
        }
        else {
            graphics.setColor(backgroundColor);
            graphics.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 12, 12);
            graphics.setColor(foregroundColor);
            graphics.drawText(label, 4, 4);
        }
    }
}
