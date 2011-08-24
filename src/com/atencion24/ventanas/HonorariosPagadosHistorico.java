package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.atencion24.control.Deduccion;
import com.atencion24.control.HttpConexion;
import com.atencion24.control.Pago;
import com.atencion24.interfaz.CustomButtonTable;
import com.atencion24.interfaz.CustomButtonTableNotFocus;
import com.atencion24.interfaz.ForegroundManager;
import com.atencion24.control.InformacionNivel;
import com.atencion24.interfaz.ListStyleButtonSet;

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

/**
 * HonorariosPagadosHistorico esta clase es la encargada de desplegar en el dispositivo
 * BlackBerry el reporte historico de pagos de acuerdo al rango de fechas de búsqueda ingresado 
 * por el usuario 
 * 
 * pagos -> Vector de Pagos. (Todos los pagos generados en el rango de fechas de la búsqueda)
 * nivel -> número máximo de niveles de expansión que puede tener algún elemento expansible del reporte.
 * posBotonPresionado -> posicion del elemento expansible que ha sido presionado.
 * foreground -> Manager de los elementos de interfaz del reporte.
 * contenido -> ListStyleButtonSet Elemento de interfaz donde se aloja el reporte.
 * informacionNivel -> Vector de InformacionNivel. (Contiene la información de todos los elementos expandibles del reporte del nivel superior)
 * botonesF -> Arreglo de CustomButtonTable (Contiene todos los elementos expandibles del reporte del nivel superior) 
 * 
 */
public class HonorariosPagadosHistorico extends plantilla_screen_http implements FieldChangeListener {

	static Vector pagos;
	
	String fechaI; 
	String fechaF;
	
	int posBotonPresionado = 0;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];
    
    Bitmap plus = Bitmap.getBitmapResource("com/atencion24/imagenes/plus_blanco.png");
    Bitmap minus = Bitmap.getBitmapResource("com/atencion24/imagenes/minus_blanco.png");

	/**
	 * HonorariosPagadosHistorico. Constructor de la clase
	 * @param historicoPagos (Pago)Vector que contiene todos los pagos generados en el 
	 * rango de fechas de la búsqueda.
	 * @param fechaF 
	 * @param fechaI 
	 */
	public HonorariosPagadosHistorico(Vector historicoPagos, String fechaI, String fechaF) 
	{
		
		super( NO_VERTICAL_SCROLL | USE_ALL_HEIGHT | USE_ALL_WIDTH );
		super.setTitulo("Histórico de Pagos");
		super.changeTitulo();
		super.setSubTitulo("(" + fechaI + " - " + fechaF + ")");
		super.changeSubTitulo();
		pagos = historicoPagos;
		this.fechaI = fechaI;
		this.fechaF = fechaF;
		
		//Inserto los managers donde irá el reporte.
		foreground.add(contenido);
        add(foreground);
        
        llenarVectorInformacionNivelSup();
	}
	
	/**
	 * llenarVectorInformacionNivelSup(). Por cada pago en el rango de fechas consultadas (en el vector pagos)
	 * se crea una instancia de la clase InformacionNivel el cual contendrá toda la información de los
	 * elementos expandibles del reporte del nivel superior.
	 */
	public void llenarVectorInformacionNivelSup()
	{
		int size = pagos.size();
    	int count = size;
    	Enumeration listadoPagos = pagos.elements();
    	Pago pago;
    	InformacionNivel info;
    	//Por cada pago en listadoPagos, debo crear un InformacionNivel y agregarlo al Vector informacionNivel
    	while (listadoPagos.hasMoreElements()) 
    	{
    		pago = (Pago) listadoPagos.nextElement();
    		info = new InformacionNivel(plus, pago.getFechaPago(), pago.getMontoNeto(), 3, new int[] {size-count});
    		informacionNivelSuperior.addElement(info);
    		count --;
    	}	
    	crearReporte();
	}
    
    /**
     * crearReporte(). Método encargado de mostrar en el dispositivo BB el reporte de historico de pagos
     * sin ningún elemento expandido. Este método debe crear un CustomButtonTable 
     * por cada elemento expandible del reporte del nivel superior y asociarle su información respectiva, la 
     * cual se obtiene del Vector informacionNivelSuperior. Todos los CustomButtonTable creados son almacenados 
     * en el vector botonesF.
     */
    public void crearReporte()
    {
    	contenido.deleteAll();
    	
    	//Agregar la cabecera al reporte
	    //ListStyleLabelField Titulo = new ListStyleLabelField( "", DrawStyle.HCENTER , 0x400000, Color.WHITE);
	    //contenido.add(Titulo);
	    
	    try {
            FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
            Font boldFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt).derive(Font.BOLD);
            CustomButtonTableNotFocus encabezado = new CustomButtonTableNotFocus("   Fecha", " Monto Bs", Color.LIGHTYELLOW, 0x400000, Field.USE_ALL_WIDTH, 0xBBBBBB);
            encabezado.setFont(boldFont);
            contenido.add(encabezado);
	    }
	    catch (ClassNotFoundException e) {}
	    
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
	    	contenido.add(new SeparatorField()); 
	    	botonesF[i].setChangeListener(this);
	        contenido.add(botonesF[i]);
	    }
    }
    
    /**
     * crearParteMenu(). Metodo encargado de desplegar el reporte por pantalla, una vez que ha sido presionado 
     * algún botón desplegable (en este caso del nivel superior 1). Además esta funcion invoca al metodo mostrarBotones
     * para crear los CustomButtonField de acuerdo a la información del vector informacionNivelSuperior (incluyendo los
     * CustomButtonField) de los hijos. A todos lo CustomButtonField creados le setea el ChangeListener y los agrega
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
             for (int i = posBotonPresionado + 1  ; i < botonesF.length; i++)
             {
            	 botonesF[i].setChangeListener(this);
                 if(botonesF[i].getNivel() == 3)  contenido.add(new SeparatorField());
            	 contenido.add(botonesF[i]);
             }
            
             //Guardo en mi arreglo los botones que no fueron borrados,
             //para poder hacer bien la comparacion en la función fieldChanged
             int count = 0; 
             for (int i = 0; i < posicionEnManagerDelPresionado ; i++){
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
     * desplegarMenu. Método encargado de según el nivel del boton presionado (en este caso si es 1, es 
     * decir, del nivel superior) crearle sus hijos (InformacionNivel), si no se le han creado, en base a la 
     * información del vector de pagos y settear el campo mostrar del InformacionNivel correspondiente al 
     * boton presionado en True o False dependendo de si ya estaba desplegado o no.   
     * @param botonPulsado
     */
    public void desplegarMenu(CustomButtonTable botonPulsado){
        
    	if (botonPulsado.obtenerNivel() != 0)
    	{
    		int[] pos = botonPulsado.obtenerPosicion();
    		
            InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(pos[0]);
            Pago pago = (Pago) pagos.elementAt(pos[0]);
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
            		//Primero el monto liberado
                	info.setHijo(new Hashtable());
    	            InformacionNivel infohijo;
    	            int posicion = 0 ; 
    	            infohijo = new InformacionNivel("Monto Liberado:" , pago.getMontoLiberado(), 0, new int[] {posicion});
    	            info.getHijo().put(new Integer(posicion), infohijo);
    	            
    	            if(pago.getDeducciones()!=null)
    	            {	
	    	            Enumeration deducciones = pago.getDeducciones().elements();
	    	            Deduccion deduccion;
	    	            while(deducciones.hasMoreElements())
	    	            {
	    	            	posicion ++;
	    	            	deduccion = (Deduccion) deducciones.nextElement();
	    	            	infohijo = new InformacionNivel(deduccion.getConcepto() + ":" , "-" + deduccion.getMonto(), 0, new int[] {posicion});
	    	                info.getHijo().put(new Integer(posicion),infohijo);
	    	            }	
    	            }
            	}
            }    
            informacionNivelSuperior.setElementAt(info,pos[0]);
	        crearParteMenu();  
    		
        }
    }
    
    /* (non-Javadoc)
     * @see net.rim.device.api.ui.FieldChangeListener#fieldChanged(net.rim.device.api.ui.Field, int)
     */
    public void fieldChanged(Field field, int context) 
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
			HttpConexion thread = new HttpConexion("/cerrarSesion", "GET", this, false);
			thread.start();
			Dialog.alert("Hasta luego!");
			System.exit(0);
		}
	}
	
	public void irInicio()
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

	public void llamadaExitosa(String respuesta) {
		// TODO Auto-generated method stub
		
	}

	public void llamadaFallada(final String respuesta) {
		// TODO Auto-generated method stub
	}
}
	
	
	
	
	
