package com.atencion24.control;

public class Sesion {

	private String nombre;
	private String apellido;
	private String codigoMedico;
	private String nombreUsuario;

	public Sesion(){}
	
	public Sesion(String nombre, String apellido, String codigoMedico, String nombreUsuario) 
	{  
	    this.nombre = nombre;
	    this.apellido = apellido;
	    this.codigoMedico = codigoMedico;
	    this.nombreUsuario = nombreUsuario;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getCodigoMedico() {
		return codigoMedico;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}
}

