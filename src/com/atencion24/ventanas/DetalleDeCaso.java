package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atencion24.control.Caso;
import com.atencion24.control.Honorario;
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

public class DetalleDeCaso extends MainScreen implements FieldChangeListener{

	static Caso caso;
	
	private int nivel = 2;
	int posBotonPresionado = 0;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
	
	public DetalleDeCaso(Caso detalleCaso) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		caso = detalleCaso;
		
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
		add(new CustomLabelField("Detalle de un caso ", Color.WHITE, 0x400000, FIELD_HCENTER));
		add(new SeparatorField());
		
		//Inserto los managers donde irá el reporte.
		foreground.add(contenido);
        add(foreground);
        
        llenarVectorInformacionNivelSup();
	}

	/**
	 * llenarVectorInformacionNivelSup(). Por cada informacion relevante del caso 
	 * se crea una instancia de la clase InformacionNivel la cual contendrá toda la información de los
	 * elementos expandibles del reporte del nivel superior.
	 */
	public void llenarVectorInformacionNivelSup()
	{
    	InformacionNivel info;
    	//Nombre del paciente
		info = new InformacionNivel(" Nombre del Paciente: ", caso.getNombrePaciente(), nivel, new int[] {0});
		informacionNivelSuperior.addElement(info);
		
		//Cédula del paciente
		info = new InformacionNivel(" CI Paciente: ", caso.getCiPaciente(), nivel, new int[] {1});
		informacionNivelSuperior.addElement(info);
		
		//Responsable de pago
		info = new InformacionNivel(" Responsable de Pago: ", caso.getResponsablePago(), nivel, new int[] {2});
		informacionNivelSuperior.addElement(info);		
    	
    	//Honorarios (Unico campo desplegable)
		info = new InformacionNivel(" Honorarios: ", "", nivel, new int[] {3});
		informacionNivelSuperior.addElement(info);		
		
		//Total Facturado
		info = new InformacionNivel(" Total Facturado: ", caso.getMontoFacturado(), nivel, new int[] {4});
		informacionNivelSuperior.addElement(info);		
		
		//Total Exonerado
		info = new InformacionNivel(" Total Exonerado: ", caso.getMontoExonerado(), nivel, new int[] {5});
		informacionNivelSuperior.addElement(info);
		
		//Total Abonado
		info = new InformacionNivel(" Total Abonado: ", caso.getMontoAbonado(), nivel, new int[] {6});
		informacionNivelSuperior.addElement(info);
		
		//Total Deuda
		info = new InformacionNivel(" Total Deuda: ", caso.getTotalDeuda(), nivel, new int[] {7});
		informacionNivelSuperior.addElement(info);
		
    	crearReporte();
	}
	
    /**
     * crearReporte(). Método encargado de mostrar en el dispositivo BB el detalle de un caso
     * sin ningún elemento expandido. Este método debe crear un CustomButtonTable 
     * por cada elemento del reporte del nivel superior y asociarle su información respectiva, la 
     * cual se obtiene del Vector informacionNivelSuperior. Todos los CustomButtonTable creados son almacenados 
     * en el vector botonesF.
     */
    public void crearReporte()
    {
    	contenido.deleteAll();
    	
    	//Agregar la cabecera al reporte
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Detalle del caso " + caso.getNroCaso(), DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    contenido.add(Titulo);
	    
	    CustomButtonTable[] aux;
	    Enumeration listadoInfoNivel = informacionNivelSuperior.elements();
	    botonesF = ((InformacionNivel) listadoInfoNivel.nextElement()).mostrarBotones();
	    
	    //Por cada infoNivel en el nivel superior debo crear un CustomButtonTable  
	    //y agregarlo al Vector botonesF
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
    
    /**
     * crearParteMenu(). Metodo encargado de desplegar el reporte por pantalla, una vez que ha sido presionado 
     * algún botón desplegable (en este caso del nivel 2 o 1). Además esta funcion invoca al metodo mostrarBotones
     * para crear los CustomButtonField de acuerdo a la información del vector informacionNivelSuperior (incluyendo los
     * CustomButtonField de los hijos). A todos lo CustomButtonField creados le setea el ChangeListener y los agrega
     * al contenido (ListStyleButtonSet)
     */
    public void crearParteMenu()
    {
        try{
            // Los botones que vienen despues del boton presionado son eliminados
             for (int i = posBotonPresionado + 2 ; i < botonesF.length + 1; i++){
                 
                     contenido.delete(botonesF[i-1]);
                 }
            // Busco los nuevos botones
            CustomButtonTable[] aux;
     	    Enumeration listadoInfoNivel = informacionNivelSuperior.elements();
     	    botonesF = new CustomButtonTable[1];
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
             for (int i = posBotonPresionado + 1  ; i < botonesF.length; i++){
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
    
    /**
     * desplegarMenu. Método encargado de según el nivel del boton presionado (en este caso si es el 
     * boton de honorarios o sus hijos) crearle sus hijos (InformacionNivel), si no se le han creado, en base a la 
     * información del vector de honorarios del caso y settear el campo mostrar del InformacionNivel 
     * correspondiente al boton presionado en True o False dependendo de si ya estaba desplegado o no.  
     * @param botonPulsado 
     */
    public void desplegarMenu(CustomButtonTable botonPulsado)
    {
    	int nivelPulsado = botonPulsado.obtenerNivel();
    	if (nivelPulsado != 0)
    	{
    		//Si presione algun boton del nivel superior
    		if(nivelPulsado == 2)
    		{
    			int[] pos = botonPulsado.obtenerPosicion();
    			//Si presione el boton de honorarios
    			if(pos[0]==3)
    			{
    				InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(3);
    		        if (info.isMostrar()) info.setMostrar(false);
    		        else 
    		        {
    		        	//Asociarle hijos a info para convertirlo en menu desplegable
    		        	info.setMostrar(true);
    		        	if(info.hijo == null)
    		        	{
    		        		info.hijo = new Hashtable();
    			            InformacionNivel infohijo;
    			            int count = 0 ; 
    		        		Enumeration honorarios = caso.getHonorarios().elements();
    		        		Honorario honorario;
    		        	    while(honorarios.hasMoreElements())
    		        	    {
    		        	    	honorario = (Honorario) honorarios.nextElement();
    			            	infohijo = new InformacionNivel(honorario.getNombre(), "", 1, new int[] {3,count});
    			                info.hijo.put(new Integer(count), infohijo);
    			                count++;
    		        	    }	
    		        	}
    		        }	
    		        informacionNivelSuperior.setElementAt(info,3);
    		        crearParteMenu();  
    			}
    		}
    		else if (nivelPulsado == 1) //Si presionaste algun honorario en particular
    		{
    			int[] pos = botonPulsado.obtenerPosicion();
    			int posicionNivel = pos[1];
    			Integer posNivel  = new Integer(posicionNivel); 
    			
    			InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(3);
    			InformacionNivel hijoPresionado = (InformacionNivel) info.hijo.get(posNivel); 
    			if (hijoPresionado.isMostrar()) hijoPresionado.setMostrar(false);
		        else 
		        {
		        	//Asociarle hijos a info para convertirlo en menu desplegable
		        	hijoPresionado.setMostrar(true);
		        	if(hijoPresionado.hijo == null)
		        	{
		        		hijoPresionado.hijo = new Hashtable();
			            InformacionNivel infohijo;
		        		Honorario honorario = (Honorario) caso.getHonorarios().get(posNivel);
		        	    
		        		//Monto Facturado
		        		infohijo = new InformacionNivel(" Monto Facturado: ", honorario.getMontoFacturado(), 0, new int[] {3,posicionNivel,0});
		        		hijoPresionado.hijo.put(new Integer(0), infohijo);
		        		//Monto Exonerado
		        		infohijo = new InformacionNivel(" Monto Exonerado: ", honorario.getMontoExonerado(), 0, new int[] {3,posicionNivel,1});
		        		hijoPresionado.hijo.put(new Integer(1), infohijo);
		        		//Monto Abonado
		        		infohijo = new InformacionNivel(" Monto Abonado: ", honorario.getMontoAbonado(), 0, new int[] {3,posicionNivel,2});
		        		hijoPresionado.hijo.put(new Integer(2), infohijo);
		        		//Deuda
		        		infohijo = new InformacionNivel(" Deuda: ", honorario.getTotalDeuda(), 0, new int[] {3,posicionNivel,3});
		        		hijoPresionado.hijo.put(new Integer(3), infohijo);
		        	}
		        }
    			info.hijo.remove(posNivel);
    			info.hijo.put(posNivel, hijoPresionado);
		        informacionNivelSuperior.setElementAt(info,3);
		        crearParteMenu();  
    		}
    	}	
    }
    
	public void fieldChanged(Field field, int arg1) 
	{
		 for (int i = 0; i < botonesF.length; i++)
		    {
		        if (field == botonesF[i])
		        {
		            posBotonPresionado = i;
		            desplegarMenu(botonesF[i]);
		            break;
		        }
		    }
	}

}
