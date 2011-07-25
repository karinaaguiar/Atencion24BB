package com.atencion24.control;

public class Fianza {

	private String nroCaso;
	private String fechaEmisionFactura;
	private String paciente;
	private String montoACobrar;
	private String montoAbonado;
	private String montoReintegro;
	private String montoNotasCred;
	private String montoNotasDeb;
	private String montoNeto;
	
	public Fianza() {}
	
	public Fianza(String nroCaso, String fechaEmisionFactura, String paciente , 
			String montoACobrar, String montoAbonado , String montoReintegro, String montoNotasCred, 
			String montoNotasDeb, String montoNeto)
	{  
		this.setNroCaso(nroCaso);
	    this.setFechaEmisionFactura(fechaEmisionFactura);
	    this.setPaciente(paciente);
	    this.setMontoACobrar(montoACobrar);
	    this.setMontoAbonado(montoAbonado);
	    this.setMontoReintegro(montoReintegro);
	    this.setMontoNotasCred(montoNotasCred);
	    this.setMontoNotasDeb(montoNotasDeb);
	    this.setMontoNeto(montoNeto);
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

	public void setMontoReintegro(String montoReintegro) {
		this.montoReintegro = montoReintegro;
	}

	public String getMontoReintegro() {
		return montoReintegro;
	}

	public void setMontoNotasCred(String montoNotasCred) {
		this.montoNotasCred = montoNotasCred;
	}

	public String getMontoNotasCred() {
		return montoNotasCred;
	}
	
	public void setMontoNotasDeb(String montoNotasDeb) {
		this.montoNotasDeb = montoNotasDeb;
	}

	public String getMontoNotasDeb() {
		return montoNotasDeb;
	}
	
	public void setMontoNeto(String montoNeto) {
		this.montoNeto = montoNeto;
	}

	public String getMontoNeto() {
		return montoNeto;
	}
}
