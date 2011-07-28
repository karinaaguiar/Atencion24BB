package com.atencion24.control;

import java.util.Vector;

public class Sesion {

	private String nombre;
	private Vector codigoMedico;

	public Sesion(){}
	
	public Sesion(String nombre, Vector codigoMedico) 
	{  
	    this.nombre = nombre;
	    this.codigoMedico = codigoMedico;
	}

	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Vector getCodigoMedico() {
		return codigoMedico;
	}
	
	public void setCodigoMedico(Vector codigosPago) {
		this.codigoMedico = codigosPago;
	}

}

