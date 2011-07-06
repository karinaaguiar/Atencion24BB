package com.atencion24.interfaz;

import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class CustomButtonTableNotFocus extends Field {
	private String label;
    private String label2;
    private int foregroundColor;
    private int backgroundColor;
    private int color_border = 0;   
    
    private static final int HPADDING = 6; //Display.getWidth() <= 320 ? 6 : 8;
    
    /**
     * Constructor de la clase. Sin ícono 
     * @param label texto del lado izquierdo en la celda
     * @param labelR texto del lado derecho en la celda
     * @param foregroundColor color del texto 
     * @param backgroundColor color del fondo 
     * @param style estilo del botón (field) 
     * @param colorB color de los bordes de la celda
     */
    public CustomButtonTableNotFocus(String label, String labelR, int foregroundColor, int backgroundColor, long style, int colorB) {
        super(style);
        this.label = label;
        this.label2 = labelR;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.color_border = colorB;
        }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#layout(int, int)
     */
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
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#getPreferredHeight()
     */
    public int getPreferredHeight() {
    	return getFont().getHeight();
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.Field#getPreferredWidth()
     */
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
            graphics.fillRect(0, 0, getWidth(), getHeight());
            graphics.setGlobalAlpha(100);
            graphics.fillRoundRect(3, 3, getWidth(), getHeight()-6, 12, 12);
            graphics.setGlobalAlpha(255);
            if (label2 != null)
            {
                int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                // Left Bitmap
                graphics.drawText(label, HPADDING, 0, DrawStyle.ELLIPSIS, text2X - 1);
                graphics.drawText(label2, text2X, 0);
            }else{
                graphics.drawText(label, 0, 0);
            }
        }else{
            if (label2 != null){
                int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                graphics.setColor(foregroundColor);
                graphics.drawText(label, HPADDING , 0, DrawStyle.ELLIPSIS, text2X - 1);
                graphics.drawText(label2, text2X, 0);
            }
            else{
                graphics.setColor(foregroundColor);
                graphics.drawText(label, 0, 0);   
            }
        }
    }
    
}
