package br.upe.dsc.de.algorithm;

public class Individual {
	private double[] solution;
	private int dimensions;
	
	public Individual(int dimensions) {
		this.dimensions = dimensions;
		this.solution = new double[dimensions];
	}

	public double[] getSolution() {
		return solution;
	}

	public void setSolution(double[] solution) {
		this.solution = solution;
	}
}