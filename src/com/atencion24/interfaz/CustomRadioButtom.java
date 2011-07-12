package com.atencion24.interfaz;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;

public class CustomRadioButtom extends RadioButtonField {

	String label; 
    private static Bitmap _NotfocusedNotSelected = Bitmap.getBitmapResource("com/atencion24/imagenes/notFocusedNotSelected.png");        
    private static Bitmap _NotfocusedSelected = Bitmap.getBitmapResource("com/atencion24/imagenes/NotFocusedSelected.png");        

	public CustomRadioButtom (String nombre, RadioButtonGroup group, boolean selected)
	{
		super(nombre, group, selected);
		this.label = nombre;
	}
	
	protected void paint(Graphics graphics) 
	{
        if (isSelected()) 
        {
            int imageY = (getHeight() - _NotfocusedSelected.getHeight()) / 2;
            int textY = (getHeight() - getFont().getHeight()) / 2;
            graphics.drawBitmap(0, imageY , _NotfocusedSelected.getWidth(), _NotfocusedSelected.getHeight(), _NotfocusedSelected, 0, 0);
            graphics.drawText(label, _NotfocusedSelected.getWidth(), textY);
        } 
        else 
        { 
            int imageY = (getHeight() - _NotfocusedNotSelected.getHeight()) / 2;
            int textY = (getHeight() - getFont().getHeight()) / 2;
            graphics.drawBitmap(0, imageY , _NotfocusedNotSelected.getWidth(), _NotfocusedNotSelected.getHeight(), _NotfocusedNotSelected, 0, 0);
            graphics.drawText(label,_NotfocusedNotSelected.getWidth(), textY);
        
        }
    }
}
