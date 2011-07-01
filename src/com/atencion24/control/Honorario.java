package com.atencion24.control;

public class Honorario {

	private String nombre;
	private String montoFacturado;
	private String montoExonerado;
	private String montoAbonado;
	private String totalDeuda;
	
	public Honorario() {}
	
	public Honorario(String nombre, String montoFacturado, String montoExonerado, String montoAbonado,
			String totalDeuda) 
	{  
		this.setNombre(nombre);
		this.setMontoFacturado(montoFacturado);
		this.setMontoExonerado(montoExonerado);
		this.setMontoAbonado(montoAbonado);
		this.setTotalDeuda(totalDeuda);
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
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
}
