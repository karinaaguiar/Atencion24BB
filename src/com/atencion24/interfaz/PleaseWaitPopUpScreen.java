package com.atencion24.interfaz;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class PleaseWaitPopUpScreen extends PopupScreen {

	public PleaseWaitPopUpScreen()
	{
		super(new VerticalFieldManager());
		this.add(new LabelField(" Por favor espere ", LabelField.FIELD_HCENTER));
        ProgressAnimationField example = new ProgressAnimationField( Bitmap.getBitmapResource( "com/atencion24/imagenes/spinner.png" ), 5, Field.FIELD_HCENTER );
        example.setMargin(15, 15, 15, 15);
        this.add(example);
	}
}
