package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Vector;

import com.atencion24.control.Deduccion;
import com.atencion24.control.Pago;
import com.atencion24.control.ReporteForecast;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomLabelField;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.interfaz.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;
import com.atencion24.interfaz.ListStyleLabelField;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class HonorariosPagadosHistorico extends MainScreen implements FieldChangeListener {

	static Vector pagos;
	
	Manager foreground;
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    VerticalFieldManager mainVerticalManager = new VerticalFieldManager(Field.FIELD_HCENTER);
    
    private int nivel = 1;
    
    Vector informacionNivel = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];

    int[] pos;
    ReporteForecast reporteF = new ReporteForecast();
    int posBotonPresionado = 0;
	
	public HonorariosPagadosHistorico(Vector historicoPagos) {
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		pagos = historicoPagos;
		
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
		add(new CustomLabelField("Honorarios Pagados", Color.WHITE, 0x400000, FIELD_HCENTER));
		add(new SeparatorField());
		
		//Inserto los managers donde irá el reporte.
		foreground = new ForegroundManager();
		foreground.add(contenido);
        foreground.add(mainVerticalManager);
        add(foreground);
        
        mostrarReporte();
        
	}
	
	public void mostrarReporte()
	{
		//Si boton es 0 Estoy mostrando la lista de pagos SIN desplegar el detalle 
    	int size = pagos.size();
    	System.out.println("Numero de pagos " + size);
    	int count = size;
    	Enumeration listadoPagos = pagos.elements();
    	Pago pago;
    	InformacionNivel info;
    	//Por cada pago en listadoPagos, debo crear un InformacionNivel y agregarlo al Vector informacionNivel
    	while (listadoPagos.hasMoreElements()) 
    	{
    		pago = (Pago) listadoPagos.nextElement();
    		info = new InformacionNivel(pago.getFechaPago(), pago.getMontoNeto()+ " Bs", nivel, new int[] {size-count});
    		informacionNivel.addElement(info);
    		count --;
    	}	
    	crearMenu();
	}
    
    public void crearMenu()
    {
	    contenido.deleteAll();
	    mainVerticalManager.deleteAll();
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Honorarios Pagados", DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    contenido.add(Titulo);
	    
	    CustomButtonTable[] aux;
	    Enumeration listadoInfoNivel = informacionNivel.elements();
	    botonesF = ((InformacionNivel) listadoInfoNivel.nextElement()).mostrarBotones();
	    
	    //Por cada infoNivel en el nivel 1 debo crear un CustomButtonTable  
	    while (listadoInfoNivel.hasMoreElements()) 
    	{
	    	InformacionNivel info = (InformacionNivel) listadoInfoNivel.nextElement();
	    	aux = info.mostrarBotones();
	    	botonesF = info.mezclarArray(botonesF,aux);
    	}
	    
	    //Set ChangeListener y agregarlos a la interfaz
	    for (int i = 0; i < botonesF.length; i++)
	    {
	         botonesF[i].setChangeListener(this);
	         contenido.add(botonesF[i]);
	    }
    }
    
    public void crearParteMenu()
    {
        try{
            // Los botones que vienen despues del boton presionado son eliminados
             for (int i = posBotonPresionado + 2; i < botonesF.length + 1; i++){
                 
                     contenido.delete(botonesF[i-1]);
                 }
             // Busco los nuevos botones
             CustomButtonTable[] aux;
     	    Enumeration listadoInfoNivel = informacionNivel.elements();
     	    botonesF = ((InformacionNivel) listadoInfoNivel.nextElement()).mostrarBotones();
     	    
     	    //Por cada infoNivel en el nivel 1 debo crear un CustomButtonTable  
     	    while (listadoInfoNivel.hasMoreElements()) 
         	{
     	    	InformacionNivel info = (InformacionNivel) listadoInfoNivel.nextElement();
     	    	aux = info.mostrarBotones();
     	    	botonesF = info.mezclarArray(botonesF,aux);
         	}
             
             // Coloco en la pantalla los botones nuevos, ignorando los que ya tengo 
             // guardados (el boton presionado y los que vienen antes de el)
             for (int i = posBotonPresionado + 1; i < botonesF.length; i++){
                     botonesF[i].setChangeListener(this);
                     contenido.add(botonesF[i]);
             }
             //Guardo en mi arreglo los botones que no fueron borrados,
             //para poder hacer bien la comparacion en la función fieldChanged
             for (int i = 0; i < posBotonPresionado + 1; i++){
                 botonesF[i] = (CustomButtonTable)contenido.getField(i + 1);
                 }
         }catch(IndexOutOfBoundsException e){
             System.out.println("Error: " + e);
         }
        
    }
    
    public void desplegarMenu(CustomButtonTable botonPulsado){
        
    	if (botonPulsado.obtenerNivel() != 0)
    	{
            pos = botonPulsado.obtenerPosicion();
            System.out.println(pos[0]);
            
            InformacionNivel info = (InformacionNivel) informacionNivel.elementAt(pos[0]);
            Pago pago = (Pago) pagos.elementAt(pos[0]);
            
            //Asociarle hijos a info para convertirlo en menu desplegable
            
            //Primero el monto liberado
            InformacionNivel infohijo;
            int posicion = 0 ; 
            infohijo = new InformacionNivel("Monto Liberado" , pago.getMontoLiberado()+ " Bs", botonPulsado.obtenerNivel()-1, new int[] {posicion});
            info.hijo.addElement(infohijo);
            
            Enumeration deducciones = pago.getDeducciones().elements();
            Deduccion deduccion;
            while(deducciones.hasMoreElements())
            {
            	posicion ++;
            	deduccion = (Deduccion) deducciones.nextElement();
            	infohijo = new InformacionNivel(deduccion.getConcepto() , deduccion.getMonto()+ " Bs", botonPulsado.obtenerNivel()-1, new int[] {posicion});
                info.hijo.addElement(infohijo);
            }	
            info.setMostrar(true); 
            crearParteMenu();  
        }
    }
    
    public void fieldChanged(Field field, int context) 
    {
	    for (int i = 0; i < botonesF.length; i++)
	    {
	        if (field == botonesF[i]){
	            posBotonPresionado = i;
	            desplegarMenu(botonesF[i]);
	            break;
	        }
	    }

    }
}
	
	
	
	
	
