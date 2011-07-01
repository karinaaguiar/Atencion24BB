package com.atencion24.control;

import java.util.Vector;

public class Fianza {

	private String nroCaso;
	private String fechaEmisionFactura;
	private String paciente;
	private String montoACobrar;
	private String montoAbonado;
	private String montoNeto;
	private Vector descuentos;
	
	public Fianza() {}
	
	public Fianza(String nroCaso, String fechaEmisionFactura, String paciente , 
			String montoACobrar, String montoAbonado , String montoNeto, Vector descuentos) 
	{  
		this.setNroCaso(nroCaso);
	    this.setFechaEmisionFactura(fechaEmisionFactura);
	    this.setPaciente(paciente);
	    this.setMontoACobrar(montoACobrar);
	    this.setMontoAbonado(montoAbonado);
	    this.setMontoNeto(montoNeto);
	    this.setDescuentos(descuentos);
	}

	public void setNroCaso(String nroCaso) {
		this.nroCaso = nroCaso;
	}

	public String getNroCaso() {
		return nroCaso;
	}

	public void setFechaEmisionFactura(String fechaEmisionFactura) {
		this.fechaEmisionFactura = fechaEmisionFactura;
	}

	public String getFechaEmisionFactura() {
		return fechaEmisionFactura;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setMontoACobrar(String montoACobrar) {
		this.montoACobrar = montoACobrar;
	}

	public String getMontoACobrar() {
		return montoACobrar;
	}

	public void setMontoAbonado(String montoAbonado) {
		this.montoAbonado = montoAbonado;
	}

	public String getMontoAbonado() {
		return montoAbonado;
	}

	public void setMontoNeto(String montoNeto) {
		this.montoNeto = montoNeto;
	}

	public String getMontoNeto() {
		return montoNeto;
	}

	public void setDescuentos(Vector descuentos) {
		this.descuentos = descuentos;
	}

	public Vector getDescuentos() {
		return descuentos;
	}
}
