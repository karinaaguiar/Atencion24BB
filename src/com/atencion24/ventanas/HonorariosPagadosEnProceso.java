package com.atencion24.ventanas;

import java.util.Enumeration;

import com.atencion24.control.Deduccion;
import com.atencion24.control.Pago;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.ListStyleLabelField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class HonorariosPagadosEnProceso extends MainScreen {

	static Pago pago;
	
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    VerticalFieldManager fieldManager;
	Manager foreground = new ForegroundManager();
	
	public HonorariosPagadosEnProceso(Pago pagoEnProceso) {
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		pago = pagoEnProceso;
		
		//Cambiar el font de la aplicación
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
		add(new CustomLabelField("Pago en proceso", Color.WHITE, 0x400000, FIELD_HCENTER));
		add(new SeparatorField());
		
		//Inserto los managers donde irá el reporte.
		foreground.add(contenido);
        add(foreground);
		
        crearReporte();
	}
	
	public void crearReporte()
    {
        contenido.deleteAll();
        fieldManager = new VerticalFieldManager();
        
        //Agregar la cabecera al reporte
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Pago en proceso \n "+ pago.getFechaPago(), DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    contenido.add(Titulo);
	    
        try {
            FontFamily alphaSansFamily = FontFamily.forName(FontFamily.FAMILY_SYSTEM);
            Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
            
            CustomButtonTable boton;
            boton = new CustomButtonTable(" Monto total liberado:", pago.getMontoLiberado() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            Enumeration deducciones = pago.getDeducciones().elements();
            while (deducciones.hasMoreElements())
            {
            	Deduccion deduccion = (Deduccion) deducciones.nextElement();
            	boton = new CustomButtonTable(" "+ deduccion.getConcepto()+ ": ", "-" + deduccion.getMonto() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                boton.setFont(appFont);
                fieldManager.add(boton);
            }
            boton = new CustomButtonTable(" Monto neto:", pago.getMontoNeto() + " Bs ", 0x704B4B, Color.WHITE, 0x400000, Color.WHITE, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            contenido.add(fieldManager);
        }         
        catch (ClassNotFoundException e) {}
    }
}

