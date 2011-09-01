package com.atencion24.interfaz;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.ui.DrawStyle;

public class HeaderBar extends Field implements DrawStyle{
    
    private boolean showSignal;
    private int fieldWidth;
    private int fieldHeight;
    private int backgroundColour;
    
    public HeaderBar() {
        super(Field.NON_FOCUSABLE);
        showSignal = true;
        fieldHeight = 14;
        fieldWidth = Display.getWidth();
        backgroundColour = 0x000000;
        
    }
	
	protected void layout(int arg0, int arg1) {
		setExtent(getPreferredWidth(), getPreferredHeight());

	}

	public int getPreferredWidth() {
	       return fieldWidth;
	}
	    
    public int getPreferredHeight() {
        return fieldHeight;
    }
    
	protected void paint(Graphics graphics) 
	{
        
		graphics.setColor(backgroundColour);
        graphics.fillRect(0, 0, this.getPreferredWidth(), this.getPreferredHeight());
        
		
		if(showSignal){
            graphics.setColor(0xEFEFEF);
            //draw antena
            graphics.fillRect(this.getPreferredWidth() - 33, 2, 7, 1);
            graphics.fillRect(this.getPreferredWidth() - 32, 3, 5, 1);
            graphics.fillRect(this.getPreferredWidth() - 31, 4, 3, 1);
            graphics.fillRect(this.getPreferredWidth() - 30, 5, 1, 9);
            
            //draw blank background
            /*graphics.fillRect(this.getPreferredWidth() - 28, 10, 6, 4);
            graphics.fillRect(this.getPreferredWidth() - 23, 8, 6, 6);
            graphics.fillRect(this.getPreferredWidth() - 18, 6, 6, 8);
            graphics.fillRect(this.getPreferredWidth() - 13, 4, 6, 10);
            graphics.fillRect(this.getPreferredWidth() -  8, 2, 6, 12);*/
            int signalLevel = RadioInfo.getSignalLevel();
            if(signalLevel > -77){
                //5 bands
                graphics.setColor(0xEFEFEF);
                graphics.fillRect(this.getPreferredWidth() - 27,11,4,2);
                graphics.fillRect(this.getPreferredWidth() - 22,9,4,4);
                graphics.fillRect(this.getPreferredWidth() - 17,7,4,6);
                graphics.fillRect(this.getPreferredWidth() - 12,5,4,8);
                graphics.fillRect(this.getPreferredWidth() - 7,3,4,10);
            }else if(signalLevel > -86){
                //4 bands
                graphics.setColor(0xEFEFEF);
                graphics.fillRect(this.getPreferredWidth() - 27,11,4,2);
                graphics.fillRect(this.getPreferredWidth() - 22,9,4,4);
                graphics.fillRect(this.getPreferredWidth() - 17,7,4,6);
                graphics.fillRect(this.getPreferredWidth() - 12,5,4,8);
            }else if(signalLevel > -92){
                //3 bands
                graphics.setColor(0xEFEFEF);
                graphics.fillRect(this.getPreferredWidth() - 27,11,4,2);
                graphics.fillRect(this.getPreferredWidth() - 22,9,4,4);
                graphics.fillRect(this.getPreferredWidth() - 17,7,4,6);
            }else if(signalLevel > -101){
                //2 bands
                graphics.setColor(0xEFEFEF);
                graphics.fillRect(this.getPreferredWidth() - 27,11,4,2);
                graphics.fillRect(this.getPreferredWidth() - 22,9,4,4);
            }else if(signalLevel > -120){
                //1 band
                graphics.setColor(0xEFEFEF);
                graphics.fillRect(this.getPreferredWidth() - 27,11,4,2);
            }
        }
	}

}
