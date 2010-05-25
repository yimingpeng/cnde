package br.upe.dsc.de.problem;

public interface IProblem {
	static final double MINIMUM_DIMENSION_VALUE = 0.01;
	
	/**
	 * Returns the name of the problem.
	 * 
	 * @return The name of the problem.
	 */
	String getName();
	
	/**
	 * Returns the number of dimensions of this problem.
	 * @return The number of dimensions of this problem.
	 */
	int getDimensionsNumber();
	
	/**
	 * Returns the lower limit of the search space for this problem in the given dimension.
	 * 
	 * @param dimension The dimension for which we want to know the lower limit.
	 * @return The lower limit of the search space for this problem in the given dimension.
	 */
	double getLowerLimit(int dimension);
	
	/**
	 * Returns the upper limit of the search space for this problem in the given dimension.
	 * 
	 * @param dimension The dimension for which we want to know the upper limit.
	 * @return The upper limit of the search space for this problem in the given dimension.
	 */
	double getUpperLimit(int dimension);
	
	/**
	 * Compares the bestSolutionFitness with the currentSolutionFitness and returns <code>true</code> if
	 * currentSolutionFitness has a better fitness than bestSolutionFitness, otherwise it returns <code>false</code>.
	 * 
	 * @param bestSolutionFitness The fitness of the best solution.
	 * @param currentSolutionFitness The fitness of the current solution.
	 * @return <code>true</code> if currentSolutionFitness has a better fitness than bestSolutionFitness,
	 * otherwise it returns <code>false</code>.
	 */
	boolean compareFitness(double bestSolutionFitness, double currentSolutionFitness);
	
	/**
	 * Calculates the fitness of the given solution.
	 * 
	 * @param solution The values of the solution in each dimension of the problem.
	 * @return The fitness of the given solution.
	 */
	double getFitness(double ... solution);
}
