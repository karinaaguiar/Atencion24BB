package com.atencion24.ventanas;

import java.util.Enumeration;
import java.util.Vector;

import com.atencion24.control.Deduccion;
import com.atencion24.control.Pago;
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
public class HonorariosPagadosHistorico extends MainScreen implements FieldChangeListener {

	static Vector pagos;
	
	private int nivel = 1;
	int posBotonPresionado = 0;
	
	Manager foreground = new ForegroundManager();
	ListStyleButtonSet contenido   = new ListStyleButtonSet();
    Vector informacionNivelSuperior = new Vector();
    CustomButtonTable[] botonesF = new CustomButtonTable[0];

	/**
	 * HonorariosPagadosHistorico. Constructor de la clase
	 * @param historicoPagos (Pago)Vector que contiene todos los pagos generados en el 
	 * rango de fechas de la búsqueda.
	 */
	public HonorariosPagadosHistorico(Vector historicoPagos) 
	{
		
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
		add(new CustomLabelField("Histórico de Pagos", Color.WHITE, 0x400000, FIELD_HCENTER));
		add(new SeparatorField());
		
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
    		info = new InformacionNivel(pago.getFechaPago(), pago.getMontoNeto()+ " Bs", nivel, new int[] {size-count});
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
	    ListStyleLabelField Titulo = new ListStyleLabelField( "Histórico de Pagos", DrawStyle.HCENTER , 0x400000, Color.WHITE);
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
     * algún botón desplegable (en este caso del nivel superior 1). Además esta funcion invoca al metodo mostrarBotones
     * para crear los CustomButtonField de acuerdo a la información del vector informacionNivelSuperior (incluyendo los
     * CustomButtonField) de los hijos. A todos lo CustomButtonField creados le setea el ChangeListener y los agrega
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
            System.out.println("Posicion del boton presionado " +pos[0]);
            
            InformacionNivel info = (InformacionNivel) informacionNivelSuperior.elementAt(pos[0]);
            Pago pago = (Pago) pagos.elementAt(pos[0]);
            
            if (info.isMostrar()) info.setMostrar(false);
            else 
            {
            	//Asociarle hijos a info para convertirlo en menu desplegable
            	info.setMostrar(true);
            	if(info.hijo == null)
            	{
            		//Primero el monto liberado
                	info.hijo = new Vector();
                	System.out.println("No tenia hijos! asi que se los agrego");
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
    	            	infohijo = new InformacionNivel(deduccion.getConcepto() , "-" + deduccion.getMonto()+ " Bs", botonPulsado.obtenerNivel()-1, new int[] {posicion});
    	                info.hijo.addElement(infohijo);
    	            }	
    	            System.out.println("Numero hijos " +( posicion +1));
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
}
	
	
	
	
	
