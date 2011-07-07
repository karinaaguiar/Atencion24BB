package com.atencion24.ventanas;

import com.atencion24.control.EstadoCuentaAS;
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

public class EstadoDeCuentaAntiguedadSaldo extends plantilla_screen
{
	static EstadoCuentaAS edoCuenta;
	
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    VerticalFieldManager fieldManager;
	Manager foreground = new ForegroundManager();
	
	EstadoDeCuentaAntiguedadSaldo(EstadoCuentaAS edoCta) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Estado de Cuenta");
		super.changeTitulo();
		edoCuenta = edoCta;
		
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
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Estado de Cuenta", DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    contenido.add(Titulo);
	    
        try {
            FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
            Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
            
            CustomButtonTable boton;
            
            //Deuda A 30 dias
            CustomButtonTable botonS = new CustomButtonTable(" Deuda a 30 d�as:", edoCuenta.getA30dias() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            botonS.setFont(appFont);
            fieldManager.add(botonS);
            
            //Deuda A 60 dias
            boton = new CustomButtonTable(" Deuda a 60 d�as:", edoCuenta.getA60dias() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            //Deuda A 90 dias
            boton = new CustomButtonTable(" Deuda a 90 d�as:", edoCuenta.getA90dias() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            //Deuda A 180 dias
            boton = new CustomButtonTable(" Deuda a 180 d�as:", edoCuenta.getA180dias() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            //Deuda A mas 180 dias
            boton = new CustomButtonTable(" Deuda a m�s de 180 d�as:", edoCuenta.getMas180() + " Bs ", 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, 0, new int[] {0});
            boton.setFont(appFont);
            fieldManager.add(boton);
            
            //Total Deuda
            CustomButtonTableNotFocus encabezado = new CustomButtonTableNotFocus(" Total deuda:", edoCuenta.getTotalDeuda() + " Bs ", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(appFont);
            fieldManager.add(encabezado);
            
            contenido.add(fieldManager);
            botonS.setFocus();
        }         
        catch (ClassNotFoundException e) {}
    }

}
