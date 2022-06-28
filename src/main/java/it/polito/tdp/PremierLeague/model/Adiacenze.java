package it.polito.tdp.PremierLeague.model;

public class Adiacenze {
	private Match m1;
	private Match m2;
	private Double peso;
	public Match getM1() {
		return m1;
	}
	public void setM1(Match m1) {
		this.m1 = m1;
	}
	public Match getM2() {
		return m2;
	}
	public void setM2(Match m2) {
		this.m2 = m2;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Adiacenze(Match m1, Match m2, Double peso) {
		super();
		this.m1 = m1;
		this.m2 = m2;
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "" + m1.getTeamHomeNAME() + " vs " + m1.getTeamAwayNAME() + 
				"------"+m2.getTeamHomeNAME()+" vs "+m2.getTeamAwayNAME()+
				", peso=" + peso + "";
	}
	
}
