package com.atencion24.control;

import java.util.Enumeration;
import java.util.Hashtable;

import com.atencion24.interfaz.CustomButtonTable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Ui;

/**
 *  La clase InformacionNivel representa una estructura de datos lógica que contiene los datos y 
 *  comportamiento de los CustomButtonTables que representen widgets despleglables.
 *  nombre -> valor de la etiqueta del lado izquierdo del widget despleglable
 *  valor ->  valor de la etiqueta del lado derecho del widget despleglable
 *  icono -> Bitmap del icono del widget desplegable
 *  nivel -> entero que representa el nivel de anidamiento donde se encuentra el widget desplegable.  
 *  pos -> arreglo que permite determinar la posicion relativa del botón respecto al resto de los botones. 
 *  	   pos[nivel] representa la posición del botón dentro del nivel al que pertenece.
 *  	   pos[nivel-i] representa la posición de su padre del nivel i  
 *  mostrar -> bandera que nos indica si el botón está desplegado o no.
 *  hijo -> estructura que contiene los hijos del botón desplegable (los que se expanden o comprimen al 
 *  		presionar el botón desplegable)  
 *  bold -> booleano que indica si el texto va en negritas o no
 */
public class InformacionNivel {
        
    private String nombre;
    private String valor;
    private Bitmap icono; 
    private int nivel;
    private int[] pos;
    private boolean mostrar = false;
    private Hashtable hijo;
    private boolean bold = false; 
    
    
    /**
     * Constructor de la clase. Con icono
     * @param icon Bitmap del icono del widget desplegable
     * @param nombre valor de la etiqueta del lado izquierdo del widget despleglable
     * @param valor valor de la etiqueta del lado derecho del widget despleglable
     * @param nivel entero que representa el nivel de anidamiento donde se encuentra el widget desplegable  
     * @param pos arreglo que permite determinar la posicion relativa del botón respecto al resto de los botones
     */
    public InformacionNivel(Bitmap icon,String nombre, String valor, int nivel, int[] pos) {  
        
        this.setNombre(nombre);
        this.valor = valor;
        this.nivel = nivel;
        this.pos = pos;
        this.setHijo(null); 
        this.setIcono(icon);
    }
    
    /**
     * Constructor de la clase. Con icono y bold 
     * @param icon Bitmap del icono del widget desplegable
     * @param nombre valor de la etiqueta del lado izquierdo del widget despleglable
     * @param valor valor de la etiqueta del lado derecho del widget despleglable
     * @param nivel entero que representa el nivel de anidamiento donde se encuentra el widget desplegable  
     * @param pos arreglo que permite determinar la posicion relativa del botón respecto al resto de los botones
     * @param bold booleano que indica si el texto va en negritas o no
     */
    public InformacionNivel(Bitmap icon,String nombre, String valor, int nivel, int[] pos, boolean bold) {  
        
        this.setNombre(nombre);
        this.valor = valor;
        this.nivel = nivel;
        this.pos = pos;
        this.setHijo(null); 
        this.setIcono(icon);
        this.bold = bold;
    }
    
    /**
     * Constructor de la clase. Sin icono
     * @param nombre valor de la etiqueta del lado izquierdo del widget despleglable
     * @param valor valor de la etiqueta del lado derecho del widget despleglable
     * @param nivel entero que representa el nivel de anidamiento donde se encuentra el widget desplegable  
     * @param pos arreglo que permite determinar la posicion relativa del botón respecto al resto de los botones
     */
    public InformacionNivel(String nombre, String valor, int nivel, int[] pos) {  
        
        this.setNombre(nombre);
        this.valor = valor;
        this.nivel = nivel;
        this.pos = pos;
        this.setHijo(null); 
        this.setIcono(null);
    }
    
    /**
     * Constructor de la clase. Sin icono y bold
     * @param nombre valor de la etiqueta del lado izquierdo del widget despleglable
     * @param valor valor de la etiqueta del lado derecho del widget despleglable
     * @param nivel entero que representa el nivel de anidamiento donde se encuentra el widget desplegable  
     * @param pos arreglo que permite determinar la posicion relativa del botón respecto al resto de los botones
     * @param bold booleano que indica si el texto va en negritas o no
     */
    public InformacionNivel(String nombre, String valor, int nivel, int[] pos, boolean bold) {  
        
        this.setNombre(nombre);
        this.valor = valor;
        this.nivel = nivel;
        this.pos = pos;
        this.setHijo(null); 
        this.setIcono(null);
        this.bold = bold;
    }
    
    /**
     * Método que permite convertir dos arreglos de CustomButtonTable en uno solo
     * @param a primer arreglo de CustomButtonTable
     * @param b segundo arreglo de CustomButtonTable
     * @return union de los arreglos a y b en uno solo
     */
    public CustomButtonTable[] mezclarArray(CustomButtonTable[] a, CustomButtonTable[] b){
        
        CustomButtonTable[] c = new CustomButtonTable[ a.length + b.length ];
        int tam = 0;
        for(int i = 0; i < a.length; i++){
            c[tam] = a[i];
            tam++;  
        }
        for(int j = 0; j < b.length; j++){
            c[tam] = b[j];
            tam++; 
        }
        return c;
    }
    
    /**
     * Método que retorna un arreglo de CustomButtonTable, donde cada CustomButtonTable del arreglo 
     * representa el botón desplegable y sus hijos (en caso de tenerlos) a nivel físico (ya no lógico)
     * @return arreglo de CustomButtonTable
     */
    public CustomButtonTable[]  mostrarBotones(){
        
    	CustomButtonTable[] botones;
    	
    	try {
            FontFamily alphaSansFamily = FontFamily.forName("BBClarity");
            Font appFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt);
            Font boldFont = alphaSansFamily.getFont(Font.PLAIN, 7, Ui.UNITS_pt).derive(Font.BOLD);
            if (isMostrar() && nivel != 0 && this.getHijo() != null)
            {
                CustomButtonTable[] auxBotones = new CustomButtonTable[0];
                Enumeration hijos = this.getHijo().elements();
                while (hijos.hasMoreElements())
                {
                    InformacionNivel info = (InformacionNivel) hijos.nextElement();
                    CustomButtonTable[] aux2Botones = info.mostrarBotones();
                    auxBotones  = mezclarArray(auxBotones, aux2Botones);
                }
                botones = crearCustomButton (this.nivel);
                if (!this.bold) botones[0].setFont(appFont);
                else botones[0].setFont(boldFont);
                return mezclarArray(botones, auxBotones);
            }
            else
            {
            	botones = crearCustomButton (this.nivel);
            	if (!this.bold) botones[0].setFont(appFont);
                else botones[0].setFont(boldFont);
                return botones;
            }
        }catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    /**
     * Función auxiliar que complementa al método mostrarBotones
     * @param nivel entero que representa el nivel de anidamiento donde se encuentra el widget desplegable  
     * @return arreglo de CustomButtonTable
     */
    public CustomButtonTable[] crearCustomButton (int nivel)
    {
    	CustomButtonTable[] botones = new CustomButtonTable[1];
    	switch (nivel)
        {
            case (3): if(this.getIcono()!=null)
  		  			  {
            		  	botones[0] = new CustomButtonTable(this.getIcono(), this.getNombre(), this.valor, 0x400202, Color.LIGHTYELLOW, 0x900A0A , Color.LIGHTYELLOW, 0x600808, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
            		  	break;
  		  			  }
            		  else {
            			  botones[0] = new CustomButtonTable(this.getNombre(), this.valor, 0xC8A8A8, Color.LIGHTYELLOW, 0x600808, Color.LIGHTYELLOW, 0xA06B6B, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
              		  	break;
            		  } 
            case (2): if(this.getIcono()!=null)
            		  {
            		  	botones[0] = new CustomButtonTable(this.getIcono(), this.getNombre(), this.valor, 0x704B4B, Color.BLACK, 0xFFC0CB, Color.BLACK, 0xFFC0CB, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
            		  	break;
            		  }
            		  else {
            		    botones[0] = new CustomButtonTable(this.getNombre(), this.valor, 0x704B4B, Color.BLACK, 0xFFC0CB, Color.BLACK, 0xFFC0CB, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
              		  	break;
            		  }
            case (1): if(this.getIcono()!=null)
  		  			  { 
            			botones[0] = new CustomButtonTable(this.getIcono(), this.getNombre(), this.valor, 0x704B4B, Color.BLACK, 0xF8DCDC, Color.BLACK, 0xF8DCDC, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
            			break;
  		  			  }
            		  else 
            		  {
            			botones[0] = new CustomButtonTable(this.getNombre(), this.valor, 0x704B4B, Color.BLACK, 0xF8DCDC, Color.BLACK, 0xF8DCDC, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
              			break;
            		  } 
            case (0): botones[0] = new CustomButtonTable(this.getNombre(), this.valor, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                      break;
            default:  botones[0] = new CustomButtonTable(this.getNombre(), this.valor, 0x704B4B, Color.BLACK, Color.WHITE, Color.BLACK, Color.WHITE, Field.USE_ALL_WIDTH, 0xBBBBBB, nivel, pos);
                      break;
        }
    	return botones;
    }
    
	public void setMostrar(boolean mostrar) {
		this.mostrar = mostrar;
	}

	public boolean isMostrar() {
		return mostrar;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setIcono(Bitmap icono) {
		this.icono = icono;
	}

	public Bitmap getIcono() {
		return icono;
	}

	public void setHijo(Hashtable hijo) {
		this.hijo = hijo;
	}

	public Hashtable getHijo() {
		return hijo;
	}
}          
