package com.atencion24.interfaz;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

public class SpacerField extends Field {

    public SpacerField() 
    {
        super(Field.NON_FOCUSABLE);
    }
   
    protected void layout(int width, int height) 
    {
        setExtent(6, 6);
    }
   
    protected void paint(Graphics graphics) {}

}
