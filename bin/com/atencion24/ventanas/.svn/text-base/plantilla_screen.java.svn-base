/*
 * plantilla_screen.java
 */ 
package com.atencion24.ventanas;

import net.rim.device.api.ui.container.MainScreen;

/**
 * Esta clase abstracta define las pantallas (screen)
 * de la aplicación.  
 */
public abstract class plantilla_screen extends MainScreen {
	
	/** 
	 * @param style define el estilo de la nueva pantalla 
	 */
	plantilla_screen (long style) {
        super(style); 
    }
	
	/** 
	 * @param respuesta si la comunicación con el servidor fue exitosa 
	 * entonces este parámetro contiene la respuesta del servidor 
	 */
    public abstract void llamadaExitosa( String respuesta );
    
    /** 
	 * @param respuesta si la comunicación con el servidor fue fallida
	 * entonces este parámetro contiene el código de error 
	 */
    public abstract void llamadaFallada( String respuesta );
    
    /** 
	 * @param 
	 */
    /*protected boolean onSavePrompt(){
        return true;   
    }*/

}
