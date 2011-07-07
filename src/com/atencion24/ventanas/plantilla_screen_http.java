package com.atencion24.ventanas;

/**
 * Clase abstracta para aquellas pantallas (screen) que se comuniquen via http 
 * con el servidor. Esta clase abstracta invita a implementar los métodos llamadaExitosa 
 * y llamadaFallada 
 */
public abstract class plantilla_screen_http extends plantilla_screen {

	/**
	 * Constructor de la clase
	 * @param style
	 */
	plantilla_screen_http (long style) { super(style); }
	
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
}
