package it.polito.tdp.crimes.model;

public class Adiacenze {
	private String crime1;
	private String crime2;
	private int peso;
	public Adiacenze(String crime1, String crime2, int peso) {
		super();
		this.crime1 = crime1;
		this.crime2 = crime2;
		this.peso = peso;
	}
	public String getCrime1() {
		return crime1;
	}
	public String getCrime2() {
		return crime2;
	}
	public int getPeso() {
		return peso;
	}
	@Override
	public String toString() {
		return crime1+ " - " + crime2 +" (" + peso+")";
	}
	
	
}
