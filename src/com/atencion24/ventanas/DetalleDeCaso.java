package com.atencion24.ventanas;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atencion24.control.Caso;
import com.atencion24.control.Honorario;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomButtonTableNotFocus;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.control.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

public class DetalleDeCaso extends plantilla_screen implements FieldChangeListener{

	static Caso caso;
	int posBotonPresionado = 0;
	static final int diferenciaEnDias = 1;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
    
    Bitmap plus = Bitmap.getBitmapResource("com/atencion24/imagenes/plus_vino.png");
    Bitmap minus = Bitmap.getBitmapResource("com/atencion24/imagenes/minus_vino.png");
    Bitmap arrow1 = Bitmap.getBitmapResource("com/atencion24/imagenes/arrow_down_vino.png");
    Bitmap arrow = Bitmap.getBitmapResource("com/atencion24/imagenes/arrow_vino.png");
    Bitmap check = Bitmap.getBitmapResource("com/atencion24/imagenes/check_vinotinto.png");
    
	public DetalleDeCaso(Caso detalleCaso) 
	{
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Detalle de un caso");
		super.changeTitulo();
		
		//El reporte corresponde a los datos cargados hasta ayer 
		Date fechaActual = Calendar.getInstance().getTime();
		long tiempoActual = fechaActual.getTime();
		long unDia = diferenciaEnDias * 24 * 60 * 60 * 1000;
		Date fechaAyer = new Date(tiempoActual - unDia);
		String ayer = new SimpleDateFormat("dd/MM/yyyy").format(fechaAyer);
		super.setSubTitulo("(" + ayer +")");
		super.changeSubTitulo();
		
		caso = detalleCaso;
		
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
		info = new InformacionNivel(check, "Paciente: ", caso.getNombrePaciente(), 0, new int[] {0});
		informacionNivelSuperior.addElement(info);
		
		//Cédula del paciente
		info = new InformacionNivel(check, "CI Paciente: ", caso.getCiPaciente(), 0, new int[] {1});
		informacionNivelSuperior.addElement(info);
		
		//Responsable de pago
		info = new InformacionNivel(check, "Responsable: ", caso.getResponsablePago(), 0, new int[] {2});
		informacionNivelSuperior.addElement(info);		
    	
    	//Honorarios (Unico campo desplegable)
		info = new InformacionNivel(plus,"Honorarios: ", "", 2, new int[] {3});
		informacionNivelSuperior.addElement(info);		
		
		//Total Facturado
		info = new InformacionNivel(check, "Total Facturado: ", caso.getMontoFacturado() + " Bs", 0, new int[] {4});
		informacionNivelSuperior.addElement(info);		
		
		//Total Exonerado
		info = new InformacionNivel(check, "Total Exonerado: ", caso.getMontoExonerado()+ " Bs", 0, new int[] {5});
		informacionNivelSuperior.addElement(info);
		
		//Total Abonado
		info = new InformacionNivel(check, "Total Abonado: ", caso.getMontoAbonado()+ " Bs", 0, new int[] {6});
		informacionNivelSuperior.addElement(info);
		
		//Total Deuda
		info = new InformacionNivel(check, "Total Deuda: ", caso.getTotalDeuda()+ " Bs", 0, new int[] {7});
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
    	try {
	    	FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
	        Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
	    	CustomButtonTableNotFocus encabezado = new CustomButtonTableNotFocus("    Caso " + caso.getNroCaso() ,"", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
		    encabezado.setFont(appFont);
		    contenido.add(encabezado);
    	}
    	catch (ClassNotFoundException e) {}
	         
    	//Agregar la cabecera al reporte
	    //ListStyleLabelField Titulo = new ListStyleLabelField( "Caso " + caso.getNroCaso(), DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    //contenido.add(Titulo);
	    
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
	        if (botonesF[i].getNivel() != 0)  contenido.add(new SeparatorField());
	    	botonesF[i].setChangeListener(this);
	        contenido.add(botonesF[i]);
	    }
        botonesF[0].setFocus();
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
        	int posicionEnManagerDelPresionado = contenido.getFieldWithFocus().getIndex();
            int numeroBotonesABorrar = contenido.getFieldCount() - posicionEnManagerDelPresionado;
            contenido.deleteRange(posicionEnManagerDelPresionado, numeroBotonesABorrar); 
        	
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
            
     	     //Coloco el boton presionado
     	     botonesF[posBotonPresionado].setChangeListener(this);
             contenido.add(botonesF[posBotonPresionado]);
             botonesF[posBotonPresionado].setFocus();
             
             // Coloco en la pantalla los botones nuevos, ignorando los que ya tengo 
             // guardados (el boton presionado y los que vienen antes de el)
             for (int i = posBotonPresionado + 1  ; i < botonesF.length; i++){
                     botonesF[i].setChangeListener(this);
                     if(botonesF[i].getNivel() != 0)  contenido.add(new SeparatorField());
                     contenido.add(botonesF[i]);
             }
            
             //Guardo en mi arreglo los botones que no fueron borrados,
             //para poder hacer bien la comparacion en la función fieldChanged
             int count = 0; 
             for (int i = 0; i < posicionEnManagerDelPresionado; i++){
            	 if(contenido.getField(i).getClass().equals(new CustomButtonTable().getClass()))
                 {
                	 botonesF[count] = (CustomButtonTable)contenido.getField(i);
                	 count++;
                 }
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
    				if (info.isMostrar()) 
    	            {
    	            	info.setMostrar(false);
    	            	info.setIcono(plus);
    	            }
    		        else 
    		        {
    		        	//Asociarle hijos a info para convertirlo en menu desplegable
    		        	info.setMostrar(true);
    		        	info.setIcono(minus);
    		        	if(info.getHijo() == null)
    		        	{
    		        		info.setHijo(new Hashtable());
    			            InformacionNivel infohijo;
    			            int count = 0 ; 
    		        		Enumeration honorarios = caso.getHonorarios().elements();
    		        		Honorario honorario;
    		        	    while(honorarios.hasMoreElements())
    		        	    {
    		        	    	honorario = (Honorario) honorarios.nextElement();
    			            	infohijo = new InformacionNivel(arrow, honorario.getNombre(), "", 1, new int[] {3,count},1);
    			                info.getHijo().put(new Integer(count), infohijo);
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
    			InformacionNivel hijoPresionado = (InformacionNivel) info.getHijo().get(posNivel); 
    			if (hijoPresionado.isMostrar()) 
	            {
    				hijoPresionado.setMostrar(false);
    				hijoPresionado.setIcono(arrow);
	            }
		        else 
		        {
		        	//Asociarle hijos a info para convertirlo en menu desplegable
		        	hijoPresionado.setMostrar(true);
		        	hijoPresionado.setIcono(arrow1);
		        	if(hijoPresionado.getHijo() == null)
		        	{
		        		hijoPresionado.setHijo(new Hashtable());
			            InformacionNivel infohijo;
		        		Honorario honorario = (Honorario) caso.getHonorarios().get(posNivel);
		        	    
		        		//Monto Facturado
		        		infohijo = new InformacionNivel("Facturado: ", honorario.getMontoFacturado() + " Bs", 0, new int[] {3,posicionNivel,0},2);
		        		hijoPresionado.getHijo().put(new Integer(0), infohijo);
		        		//Monto Exonerado
		        		infohijo = new InformacionNivel("Exonerado: ", honorario.getMontoExonerado() + " Bs", 0, new int[] {3,posicionNivel,1},2);
		        		hijoPresionado.getHijo().put(new Integer(1), infohijo);
		        		//Monto Abonado
		        		infohijo = new InformacionNivel("Abonado: ", honorario.getMontoAbonado()+ " Bs", 0, new int[] {3,posicionNivel,2},2);
		        		hijoPresionado.getHijo().put(new Integer(2), infohijo);
		        		//Deuda
		        		infohijo = new InformacionNivel("Deuda: ", honorario.getTotalDeuda()+ " Bs", 0, new int[] {3,posicionNivel,3},2);
		        		hijoPresionado.getHijo().put(new Integer(3), infohijo);
		        	}
		        }
    			info.getHijo().remove(posNivel);
    			info.getHijo().put(posNivel, hijoPresionado);
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

	public void cerrarSesion ()
	{
		int dialog =  Dialog.ask(Dialog.D_YES_NO, "¿Está seguro que desea cerrar sesión y salir?");
		if (dialog == Dialog.YES)
		{
			//Debería hacer cierre de sesion
			Dialog.alert("Hasta luego!");
			System.exit(0);
		}
	}
	
	public void irInicio()
	{
		UiApplication.getUiApplication().popScreen((UiApplication.getUiApplication().getActiveScreen().getScreenBelow()).getScreenBelow()); 
		UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
		UiApplication.getUiApplication().popScreen(this);
	}
	
	public void consultarOtroApellido()
	{
		UiApplication.getUiApplication().popScreen(UiApplication.getUiApplication().getActiveScreen().getScreenBelow()); 
		UiApplication.getUiApplication().popScreen(this);
	}
	
	public void irAtras()
	{
		UiApplication.getUiApplication().popScreen(this);
	}
	
	//Sobreescribes el metodo makeMenu y le agregas sus menuItems
	protected void makeMenu(Menu menu, int instance){
		//super.makeMenu(menu, instance);
		menu.add(new MenuItem("Ir atrás", 20,10) {
			public void run(){
				irAtras();
			}
		});
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Consultar otro apellido", 20,10) {
			public void run(){
				consultarOtroApellido();
			}
		});
		super.makeMenu(menu, instance);
		menu.add(new MenuItem("Ir a inicio", 20,10) {
			public void run(){
				irInicio();
			}
		});
		menu.add(new MenuItem("Cerrar Sesión", 20,10) {
			public void run(){
				cerrarSesion();
			}
		});
	}
}
