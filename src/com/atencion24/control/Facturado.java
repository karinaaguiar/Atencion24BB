package com.atencion24.control;

public class Facturado {

	private String monto;
	private String udn;
	
	public Facturado() {};
	
	public Facturado(String monto, String udn)
	{
		this.setMonto(monto);
		this.setUdn(udn);
	}

	public void setUdn(String udn) {
		this.udn = udn;
	}

	public String getUdn() {
		return udn;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getMonto() {
		return monto;
	}
}
