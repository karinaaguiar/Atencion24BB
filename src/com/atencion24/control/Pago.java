package com.atencion24.control;

import java.util.Vector;

public class Pago {

	private String montoLiberado;
	private Vector deducciones;
	private String montoNeto;
	private String fechaPago;
	
	public Pago() {}
	
	public Pago(String montoLiberado, Vector deducciones, String montoNeto, String fechaPago) 
	{  
	    this.montoLiberado = montoLiberado;
	    this.deducciones = deducciones;
	    this.montoNeto = montoNeto;
	    this.fechaPago = fechaPago;
	}
	
	public void setFechaPago(String fechaPago) {
		this.fechaPago = fechaPago;
	}
	public String getFechaPago() {
		return fechaPago;
	}
	public void setMontoNeto(String montoNeto) {
		this.montoNeto = montoNeto;
	}
	public String getMontoNeto() {
		return montoNeto;
	}
	public void setDeducciones(Vector deducciones) {
		this.deducciones = deducciones;
	}
	public Vector getDeducciones() {
		return deducciones;
	}
	public void setMontoLiberado(String montoLiberado) {
		this.montoLiberado = montoLiberado;
	}
	public String getMontoLiberado() {
		return montoLiberado;
	}
	
	
}
