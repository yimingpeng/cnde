package br.upe.dsc.de.algorithm;

public class Individual {
	private double[] solution;
	private int dimensions;
	private double solutionFitness;
	
	public Individual(int dimensions) {
		this.dimensions = dimensions;
		this.solution = new double[dimensions];
	}

	public double[] getSolution() {
		return solution;
	}

	public void updateSolution(double[] solution, double solutionFitness) {
		this.solution = solution;
		this.solutionFitness = solutionFitness;
	}
	
	public double getSolutionFitness() {
		return this.solutionFitness;
	}
}