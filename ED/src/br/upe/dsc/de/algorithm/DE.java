package br.upe.dsc.de.algorithm;

import java.util.Random;

import br.upe.dsc.de.problem.IProblem;

public class DE {
	private int dimensions;
	private Individual[] population;
	private IProblem problem;
	
	/**
	 * Creates a new instance of this Differential Evolution implementation.
	 * 
	 * @param dimensions Number of dimensions in the search space
	 * @param problem The problem to be solved.
	 */
	public DE(int dimensions, IProblem problem) {
		this.dimensions = dimensions;
		this.problem = problem;
		population = new Individual[dimensions];
	}
	
	/**
	 * Runs the algorithm until one stop criteria be found.
	 */
	public void run() {
		init();
	}
	
	private void init() {
		for (int i = 0; i < dimensions; i++) {
			population[i] = new Individual(dimensions);
			population[i].setSolution(getInitialSolution());
		}
	}
	
	private double[] getInitialSolution() {
		double[] position = new double[this.dimensions];
		Random random = new Random(System.nanoTime());

		for (int i = 0; i < this.dimensions; i++) {
			double value = random.nextDouble();

			position[i] = (this.problem.getUpperLimit(i) - this.problem
					.getLowerLimit(i))
					* value + this.problem.getLowerLimit(i);

			position[i] = (position[i] <= this.problem.getUpperLimit(i)) ? position[i]
					: this.problem.getUpperLimit(i);
			position[i] = (position[i] >= this.problem.getLowerLimit(i)) ? position[i]
					: this.problem.getLowerLimit(i);
		}

		return position;
	}
}
