package com.atencion24.interfaz;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;


public class ForegroundManager extends NegativeMarginVerticalFieldManager {
    
    public ForegroundManager() {
        super( USE_ALL_HEIGHT | VERTICAL_SCROLL );
        //super( VERTICAL_SCROLL );
    }
    
    protected void paintBackground( Graphics g )
    {
        int oldColor = g.getColor();
        try {
            g.setColor( Color.WHITE );
            g.fillRect( 0, getVerticalScroll(), getWidth(), getHeight());
        } finally {
            g.setColor( oldColor );
        }
    }
}
