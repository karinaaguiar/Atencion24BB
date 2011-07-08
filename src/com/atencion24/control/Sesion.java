package com.atencion24.control;

import java.util.Vector;

public class Sesion {

	private String nombre;
	private String apellido;
	private Vector codigoMedico;
	private String nombreUsuario;

	public Sesion(){}
	
	public Sesion(String nombre, String apellido, Vector codigoMedico, String nombreUsuario) 
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

	public Vector getCodigoMedico() {
		return codigoMedico;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}
}

