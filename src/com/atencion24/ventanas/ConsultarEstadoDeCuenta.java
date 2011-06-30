package com.atencion24.ventanas;

import com.atencion24.interfaz.CustomLabelField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public class ConsultarEstadoDeCuenta extends MainScreen 
{
	ConsultarEstadoDeCuenta() 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		try {
			FontFamily familiaFont = FontFamily.forName("BBAlpha Serif");
			Font appFont = familiaFont.getFont(Font.PLAIN, 8, Ui.UNITS_pt);
			setFont(appFont);
		}catch (ClassNotFoundException e){}
	
		//Logo CSS alineado al centro
		Bitmap logoBitmap = Bitmap.getBitmapResource("com/atencion24/imagenes/logo.png");
		BitmapField bitmapField = new BitmapField(logoBitmap);
		HorizontalFieldManager hfmLabel = new HorizontalFieldManager(FIELD_HCENTER);
	    hfmLabel.add(bitmapField);
	    add(hfmLabel);
	   
	    //**Label field simple**
		add(new CustomLabelField("Estado de Cuenta", Color.WHITE, 0x400000, FIELD_HCENTER));
		add(new SeparatorField());
	}

}
