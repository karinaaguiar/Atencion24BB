/*
 * CustomLabelField.java 
 */ 
package com.atencion24.interfaz;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;

public class CustomLabelField extends Field {
        
    private Bitmap image;
    private String label;
    private String label2;
    private int foregroundColor;
    private int backgroundColor;
    private int color_border = 0;   
    
	public CustomLabelField(String label, int foregroundColor, 
			int backgroundColor, long style){
		super(style);
		this.setLabel(label);
		this.setForegroundColor(foregroundColor);
		this.setBackgroundColor(backgroundColor);
	}
	
    public CustomLabelField(String label, String labelR, int foregroundColor, int backgroundColor, Bitmap image, long style, int colorB) {
        super(style);
        this.setLabel(label);
        this.label2 = labelR;
        this.setForegroundColor(foregroundColor);
        this.setBackgroundColor(backgroundColor);
        this.image = image;
        this.color_border = colorB;
        }
        
    public CustomLabelField(String label, int foregroundColor, int backgroundColor, Bitmap image, long style, int colorB) {
        super(style);
        this.setLabel(label);
        this.setForegroundColor(foregroundColor);
        this.setBackgroundColor(backgroundColor);
        this.image = image;
        this.color_border = colorB;
        }
        
    public CustomLabelField(String label, int foregroundColor, int backgroundColor, Bitmap image, long style) {
        super(style);
        this.setLabel(label);
        this.setForegroundColor(foregroundColor);
        this.setBackgroundColor(backgroundColor);
        this.image = image;
        }
    
    protected void layout(int width, int height) {
        if (((getStyle() & Field.USE_ALL_WIDTH) == Field.USE_ALL_WIDTH ) || getLabel() == null) {
            setExtent(width, Math.min(height, getPreferredHeight()));
        }
        else {
            if (getLabel() != null){
                setExtent(width, Math.min(height, getPreferredHeight()));
            }else{
                setExtent(getPreferredWidth(), getPreferredHeight());
            }
        }
    }
    
    public int getPreferredHeight() {
        if (image != null) {
            return Math.max(getFont().getHeight(), image.getHeight());
        }
        else {
            return getFont().getHeight();
        }
    }
    
    public int getPreferredWidth() {
        int width;
        if (label2 == null){
            width = getFont().getAdvance(getLabel());
            width += getFont().getAdvance(label2);
            if (image != null) {
                width += image.getWidth();
            }
        }
        else {
            width = getFont().getAdvance(getLabel());
            if (image != null) {
                width += image.getWidth();
            }
        }
        return width;
    }
    
    /*protected void paint(Graphics graphics) {
        graphics.setBackgroundColor(backgroundColor);
        graphics.clear();
        graphics.setColor(foregroundColor);
        graphics.drawText(label, 0, 0);
    }*/
    
    public void paint(Graphics graphics) {
        graphics.setBackgroundColor(getBackgroundColor());
        graphics.clear();
        
        if (color_border != 0){              
            graphics.setColor( color_border );
            graphics.drawLine( 0, 0, 0, getHeight());
            graphics.drawLine( getWidth()-1, 0, getWidth()-1, getHeight());
        }
        graphics.setColor(getForegroundColor());
        if (image != null) {
            int textY = (getHeight() - getFont().getHeight()) / 2;
            int imageY = (getHeight() - image.getHeight()) / 2;
            
            if ((getStyle() & FIELD_HCENTER) == FIELD_HCENTER) {
                int imageX = (getWidth() - image.getWidth());
                graphics.drawBitmap(imageX, imageY, image.getWidth(), image.getHeight(), image, 0, 0);
            }
            else {
                graphics.drawBitmap(0, imageY, image.getWidth(), image.getHeight(), image, 0, 0);
                graphics.drawText(getLabel(), image.getWidth(), textY, DrawStyle.ELLIPSIS, getWidth()-image.getWidth());
            }
            
        }
        else {
            if ((getStyle() & FIELD_HCENTER) == FIELD_HCENTER) {
                graphics.drawText(getLabel(), 0, 0, DrawStyle.HCENTER, getWidth());
                if (label2 != null){
                    int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                    graphics.drawText(label2, text2X, 0, DrawStyle.HCENTER, getWidth());
                }
            }else{                
                graphics.drawText(getLabel(), 0, 0, DrawStyle.ELLIPSIS, getWidth());
                if (label2 != null){
                    int text2X = (getWidth() - getFont().getAdvance(label2))-2;
                    graphics.drawText(label2, text2X, 0, DrawStyle.ELLIPSIS, getWidth());
                }
            }
        }
    }

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setForegroundColor(int foregroundColor) {
		this.foregroundColor = foregroundColor;
	}

	public int getForegroundColor() {
		return foregroundColor;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

}
