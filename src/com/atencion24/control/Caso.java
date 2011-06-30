package com.atencion24.control;

import java.util.Vector;

public class Caso {
	
	private int idCaso;
	private String nombrePaciente;
	private String fechaEmisionFactura;
	private String nroCaso;
	private String unidadNegocio;
	private String ciPaciente;
	private String responsablePago;
	private String montoFacturado;
	private String montoExonerado;
	private String montoAbonado;
	private String totalDeuda;
	private Vector honorarios;
	
	public Caso() {}
	
	public Caso(String nombrePaciente, String fechaEmisionFactura, int idCaso) 
	{  
		this.setIdCaso(idCaso);
		this.setNombrePaciente(nombrePaciente);
	    this.setFechaEmisionFactura(fechaEmisionFactura);
	}
	
	public Caso(String nombrePaciente, String fechaEmisionFactura, String nroCaso, String ciPaciente, 
			String responsablePago, String montoFacturado, String montoExonerado, String montoAbonado,
			String totalDeuda, Vector honorarios) {
		
		this.setNombrePaciente(nombrePaciente);
		this.setFechaEmisionFactura(fechaEmisionFactura);
		this.setNroCaso(nroCaso);
		this.setCiPaciente(ciPaciente);
		this.setResponsablePago(responsablePago);
		this.setMontoFacturado(montoFacturado);
		this.setMontoExonerado(montoExonerado);
		this.setMontoAbonado(montoAbonado);
		this.setTotalDeuda(totalDeuda);
		this.setHonorarios(honorarios);
	}

	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	public String getNombrePaciente() {
		return nombrePaciente;
	}

	public void setFechaEmisionFactura(String fechaEmisionFactura) {
		this.fechaEmisionFactura = fechaEmisionFactura;
	}

	public String getFechaEmisionFactura() {
		return fechaEmisionFactura;
	}

	public void setNroCaso(String nroCaso) {
		this.nroCaso = nroCaso;
	}

	public String getNroCaso() {
		return nroCaso;
	}

	public void setCiPaciente(String ciPaciente) {
		this.ciPaciente = ciPaciente;
	}

	public String getCiPaciente() {
		return ciPaciente;
	}

	public void setResponsablePago(String responsablePago) {
		this.responsablePago = responsablePago;
	}

	public String getResponsablePago() {
		return responsablePago;
	}

	public void setMontoFacturado(String montoFacturado) {
		this.montoFacturado = montoFacturado;
	}

	public String getMontoFacturado() {
		return montoFacturado;
	}

	public void setMontoExonerado(String montoExonerado) {
		this.montoExonerado = montoExonerado;
	}

	public String getMontoExonerado() {
		return montoExonerado;
	}

	public void setMontoAbonado(String montoAbonado) {
		this.montoAbonado = montoAbonado;
	}

	public String getMontoAbonado() {
		return montoAbonado;
	}

	public void setTotalDeuda(String totalDeuda) {
		this.totalDeuda = totalDeuda;
	}

	public String getTotalDeuda() {
		return totalDeuda;
	}

	public void setHonorarios(Vector honorarios) {
		this.honorarios = honorarios;
	}

	public Vector getHonorarios() {
		return honorarios;
	}

	public void setUnidadNegocio(String unidadNegocio) {
		this.unidadNegocio = unidadNegocio;
	}

	public String getUnidadNegocio() {
		return unidadNegocio;
	}

	public void setIdCaso(int idCaso) {
		this.idCaso = idCaso;
	}

	public int getIdCaso() {
		return idCaso;
	}
}
