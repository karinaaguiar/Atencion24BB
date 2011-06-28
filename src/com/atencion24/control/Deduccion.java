package com.atencion24.control;

public class Deduccion {

	private String concepto;
	private String monto;
	
	public Deduccion() {}
	
	public Deduccion(String concepto, String monto) 
	{  
	    this.monto = monto;
	    this.concepto = concepto;
	}
	
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setMonto(String monto) {
		this.monto = monto;
	}
	public String getMonto() {
		return monto;
	}
}
