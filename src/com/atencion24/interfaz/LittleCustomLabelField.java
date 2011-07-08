package com.atencion24.interfaz;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.DrawStyle;

public class LittleCustomLabelField extends Field {
    
    private String label;
    private String label2;
    private int foregroundColor;
    private int backgroundColor;
    private int color_border = 0;   
    
    public LittleCustomLabelField(String label, String labelR, int foregroundColor, int backgroundColor, long style, int colorB) {
        super(style);
        this.label = label;
        this.label2 = labelR;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.color_border = colorB;
        }
        
    public LittleCustomLabelField(String label, int foregroundColor, int backgroundColor, long style, int colorB) {
        super(style);
        this.label = label;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        this.color_border = colorB;
        }
        
    public LittleCustomLabelField(String label, int foregroundColor, int backgroundColor, long style) {
        super(style);
        this.label = label;
        this.foregroundColor = foregroundColor;
        this.backgroundColor = backgroundColor;
        }
    
    protected void layout(int width, int height) {
    	if (((getStyle() & Field.USE_ALL_WIDTH) == Field.USE_ALL_WIDTH ) || label == null) {
            setExtent(Math.min(width,getPreferredHeight() ) , Math.min(height, getPreferredHeight()));
        }
        else {
            if (label != null){
                setExtent(Math.min(getPreferredWidth(), width), Math.min(height, getPreferredHeight()));
            }else{
                setExtent(Math.min(getPreferredWidth(), width), getPreferredHeight());
            }
        }
    }
    
    /*protected void layout(int width, int height) {
        if (image != null) {
            setExtent(width, Math.max(image.getHeight(), getFont().getHeight()));
        }else {
            setExtent(width, getFont().getHeight());
        }
    } */ 
    public int getPreferredHeight() 
    {
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
    
    protected void paint(Graphics graphics) {
        graphics.setBackgroundColor(backgroundColor);
        graphics.clear();
        
        if (color_border != 0){              
            graphics.setColor( color_border );
            graphics.drawLine( 0, 0, 0, getHeight());
            graphics.drawLine( getWidth()-1, 0, getWidth()-1, getHeight());
        }
        graphics.setColor(foregroundColor);
        /*if ((getStyle() & FIELD_HCENTER) == FIELD_HCENTER) {
            graphics.drawText(label, 0, 0, DrawStyle.HCENTER, getWidth());
            if (label2 != null){
                int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                graphics.drawText(label2, text2X, 0, DrawStyle.HCENTER, getWidth());
            }
        }else{*/                
            graphics.drawText(label, 0, 0, DrawStyle.ELLIPSIS, getWidth());
            if (label2 != null){
                int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                graphics.drawText(label2, text2X, 0, DrawStyle.ELLIPSIS, getWidth());
            }
        //}
    }
}
