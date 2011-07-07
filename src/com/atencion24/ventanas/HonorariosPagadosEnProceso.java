package com.atencion24.ventanas;

import java.util.Enumeration;

import com.atencion24.control.Deduccion;
import com.atencion24.control.Pago;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomButtonTableNotFocus;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.ListStyleLabelField;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class HonorariosPagadosEnProceso extends plantilla_screen {

	static Pago pago;
	
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    VerticalFieldManager fieldManager;
	Manager foreground = new ForegroundManager();
	
	public HonorariosPagadosEnProceso(Pago pagoEnProceso) {
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Pago en proceso");
		super.changeTitulo();
		pago = pagoEnProceso;
		
		//Inserto los managers donde ir� el reporte.
		foreground.add(contenido);
        add(foreground);
        
        crearReporte();
	}
	
	public void crearReporte()
    {
        contenido.deleteAll();
        fieldManager = new VerticalFieldManager();
        
        //Agregar la cabecera al reporte
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Pago en proceso", DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    contenido.add(Titulo);
	    
        try {
        	FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
            Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
            
            //Encabezado
            CustomButtonTableNotFocus encabezado = new CustomButtonTableNotFocus(" Al " + pago.getFechaPago()," ", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            //Monto liberado
            CustomButtonTable botonS = new CustomButtonTable(" Monto total liberado:", pago.getMontoLiberado() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            botonS.setFont(appFont);
            fieldManager.add(botonS);
            
            CustomButtonTable boton;
            Enumeration deducciones = pago.getDeducciones().elements();
            while (deducciones.hasMoreElements())
            {
            	Deduccion deduccion = (Deduccion) deducciones.nextElement();
            	boton = new CustomButtonTable(" "+ deduccion.getConcepto()+ ": ", "-" + deduccion.getMonto() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
                boton.setFont(appFont);
                fieldManager.add(boton);
            }
            //Monto neto 
            encabezado = new CustomButtonTableNotFocus(" Monto neto:", pago.getMontoNeto() + " Bs ", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            contenido.add(fieldManager);
            botonS.setFocus();
        }         
        catch (ClassNotFoundException e) {}
    }
}

