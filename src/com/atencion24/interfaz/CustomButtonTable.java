/*
 * CustomLabelField.java
 *
 * © <your company here>, 2003-2005
 * Confidential and proprietary.
 */
package com.atencion24.interfaz;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Keypad;

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
    
    public int obtenerNivel(){
        return nivel;
        }
    
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
        this.nivel = nvl;
        this.posicion = pos;
        }
        
    public CustomButtonTable(String label, int foregroundColor, int backgroundColor, long style, int colorB) {
        super(style);
        this.label = label;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.color_border = colorB;
        }
        
    public CustomButtonTable(String label, int foregroundColor, int backgroundColor, long style) {
        super(style);
        this.label = label;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        }
        
    public int [] obtenerPosicion(){
        return posicion;
    }
    
    public String obtenerLabel(){
        return this.label;
    }
    
    protected void layout(int width, int height) {
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
    
    public int getPreferredHeight() {
        
            return getFont().getHeight();
    }
    
    public int getPreferredWidth() {
        int width;
        if (label2 == null){
            width = getFont().getAdvance(label);
            width += getFont().getAdvance(label2);
        }
        else {
            width = getFont().getAdvance(label);
        }
        return width;
    }
    
    /*protected void paint(Graphics graphics) {
        graphics.setBackgroundColor(backgroundColor);
        graphics.clear();
        graphics.setColor(foregroundColor);
        graphics.drawText(label, 0, 0);
    }*/
    
    protected void paint(Graphics graphics) {
        graphics.setBackgroundColor(backgroundColor);
        graphics.clear();
        
        if (color_border != 0){              
            graphics.setColor( color_border );
            graphics.drawLine( 0, 0, 0, getHeight());
            graphics.drawLine( getWidth()-1, 0, getWidth()-1, getHeight());
        }
        if (isFocus()) {
            
            int labelX = (getFont().getAdvance(label))+2;
            graphics.setColor(focusedBackgroundColor);
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.setColor(focusedShadowColor);
            graphics.setGlobalAlpha(100);
            graphics.fillRoundRect(3, 3, getWidth(), getHeight()-6, 12, 12);
            graphics.setGlobalAlpha(255);
            if (label2 != null){
                int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                graphics.setColor(focusedForegroundColor);
                graphics.drawText(label, 0, 0, DrawStyle.ELLIPSIS, text2X - 1);
                graphics.drawText(label2, text2X, 0);
            }else{
                graphics.setColor(focusedForegroundColor);
                graphics.drawText(label, 0, 0);   
            }
            
            
        }else{
            if (label2 != null){
                int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                graphics.setColor(foregroundColor);
                graphics.drawText(label, 0, 0, DrawStyle.ELLIPSIS, text2X - 1);
                graphics.drawText(label2, text2X, 0);
            }else{
                graphics.setColor(foregroundColor);
                graphics.drawText(label, 0, 0);   
            }
            
        }
    }
    
    
    public boolean isFocusable() {
        return true;
    }
    
    protected void drawFocus(Graphics graphics, boolean on) {
        }
        
    protected void onFocus(int direction) {
        super.onFocus(direction);
        invalidate();
    }
        
    protected void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }
        
    protected boolean navigationClick(int status, int time) {
        fieldChangeNotify(0);
        return true;
    }
        
    protected boolean keyChar(char character, int status, int time) {
        if (character == Keypad.KEY_ENTER) {
            fieldChangeNotify(0);
            return true;
        }
            return super.keyChar(character, status, time);
    }

}
