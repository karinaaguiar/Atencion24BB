package com.atencion24.control;

public class CodigoPago {

	private String codigo;
	private String nombre;
	
	public CodigoPago() {};
	
	public CodigoPago(String codigo, String nombre)
	{
		this.setCodigo(codigo);
		this.setNombre(nombre);
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}
	
}

