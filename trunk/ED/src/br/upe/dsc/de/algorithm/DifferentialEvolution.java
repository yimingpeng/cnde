package br.upe.dsc.de.algorithm;

import java.util.Random;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.view.PopulationObserver;
import br.upe.dsc.de.algorithm.Statistics;

/**
 * Creates a new instance of this class.
 */
public class DifferentialEvolution {
	private int dimensions;
	private Individual[] population;
	private int populationSize;
	private int maximumIterations;
	private double standardDeviation;
	private double scaleFactor;
	private double[] allFitness;
	private double bestSolutionFitness;
	private double recombinationProbability;
	private IProblem problem;
	private PopulationObserver populationObserver;
	
	/**
	 * Creates a new instance of this Differential Evolution implementation.
	 * 
	 * @param dimensions Number of dimensions in the search space
	 * @param problem The problem to be solved.
	 */
	public DifferentialEvolution(int populationSize, int maximumIterations, double standardDeviation, double scaleFactor,
			double recombinationProbability, IProblem problem, PopulationObserver populationObserver) {
		this.dimensions = problem.getDimensionsNumber();
		this.populationSize = populationSize;
		this.maximumIterations = maximumIterations;
		this.standardDeviation = standardDeviation;
		this.scaleFactor = scaleFactor;
		this.recombinationProbability = recombinationProbability;
		this.problem = problem;
		this.populationObserver = populationObserver;
		population = new Individual[populationSize];
		allFitness = new double[populationSize];
	}
	
	/**
	 * Runs the algorithm until one stop criteria be found.
	 */
	public void run() {
		init();
		double currentStandardDeviation;
		for (int i = 0; i < maximumIterations; i++) {
			iterate();

			currentStandardDeviation = Statistics.getStandardDeviation(allFitness);
			if (currentStandardDeviation < standardDeviation) {
				break;
			}
		}
		
		System.out.println("Best solution: " + bestSolutionFitness);
	}

	/**
	 * Returns the instance of PopulationObserver currently associated with this instance.
	 * 
	 * @return The instance of PopulationObserver currently associated with this instance.
	 */
	public PopulationObserver getPopulationObserver() {
		return populationObserver;
	}
	
	
	/**
	 * Returns the instance of Problem currently associated with this instance.
	 * 
	 * @return The instance of Problem currently associated with this instance.
	 */
	public IProblem getProblem() {
		return problem;
	}

	// Initializes the algorithm
	private void init() {
		double[] initialSolution;
		for (int i = 0; i < populationSize; i++) {
			population[i] = new Individual(dimensions);
			initialSolution = getInitialSolution();
			population[i].updateSolution(initialSolution, problem.getFitness(initialSolution));
			allFitness[i] = population[i].getSolutionFitness();
		}
		
		bestSolutionFitness = population[0].getSolutionFitness();
		for (Individual individual : population) {
			calculateBestSolution(individual);
		}
	}
	
	// Performs the iterations of the algorithm
	private void iterate() {
		Individual experimentalIndividual;
		double[] recombinationIndividualSolution;
		double recombinationIndividualSolutionFitness;
		
		for (int i = 0; i < populationSize; i++) {
			experimentalIndividual = mutation(i);
			recombinationIndividualSolution = crossover(population[i], experimentalIndividual);
			recombinationIndividualSolutionFitness = problem.getFitness(recombinationIndividualSolution);
			
			if (problem.compareFitness(population[i].getSolutionFitness(), recombinationIndividualSolutionFitness)) {
				population[i].updateSolution(recombinationIndividualSolution.clone(),
						recombinationIndividualSolutionFitness);
				allFitness[i] = recombinationIndividualSolutionFitness;
				calculateBestSolution(population[i]);
			}
		}
		
		populationObserver.update(population);
		
		// Controls the velocity which the particles moves on the screen
//		try {
//			Thread.sleep(250);
//		} catch (InterruptedException e) { }
	}
	
	// Performs the mutation phase of the algorithm creating the
	// experimental vector.
	private Individual mutation(int currentIndividualIndex) {
		int targetIndividualIndex = getRandomIndex(currentIndividualIndex, -1, -1);
		int individualAIndex = getRandomIndex(currentIndividualIndex, targetIndividualIndex, -1);
		int individualBIndex = getRandomIndex(currentIndividualIndex, targetIndividualIndex, individualAIndex);
		
		double[] experimentalSolution = new double[dimensions];
		double[] targetIndividualSolution = population[targetIndividualIndex].getSolution();
		double[] individualASolution = population[individualAIndex].getSolution();
		double[] individualBSolution = population[individualBIndex].getSolution();
		
		Individual experimentalIndividual = new Individual(dimensions);
		double position;
		
		for (int i = 0; i < dimensions; i++) {
			position = targetIndividualSolution[i] + scaleFactor*(
					individualASolution[i] - individualBSolution[i]);
			experimentalSolution[i] = trimPositionToProblemLimits(position, i);
		}
		experimentalIndividual.updateSolution(experimentalSolution, problem.getFitness(experimentalSolution));
		
		return experimentalIndividual;
	}

	// Performs the crossover phase of the algorithm, in which occurs the recombination of
	// the current individual with the experimental vector created during the mutation phase.
	private double[] crossover(Individual currentIndividual, Individual experimentalIndividual) {
		Random random = new Random(System.nanoTime());
		double[] recombinationIndividualSolution = currentIndividual.getSolution().clone();
		double[] experimentalIndividualSolution = experimentalIndividual.getSolution();
		double randomValue;
		
		for (int i = 0; i < dimensions; i++) {
			randomValue = random.nextDouble();
			if (randomValue <= recombinationProbability) {
				recombinationIndividualSolution[i] = experimentalIndividualSolution[i];
			}
		}
		
		return recombinationIndividualSolution;
	}
	
	// Returns an random index between 0 and the number of dimensions of the current problem and that
	// is different from the indexes received as parameters.
	private int getRandomIndex(int currentIndividualIndex, int individualAIndex, int individualBIndex) {
		Random random = new Random(System.nanoTime());
		int randomIndex = random.nextInt(populationSize);
		
		while (randomIndex == currentIndividualIndex || randomIndex == individualAIndex ||
				randomIndex == individualBIndex) {
			randomIndex = random.nextInt(populationSize);
		}
		
		return randomIndex;
	}
	
	private void calculateBestSolution(Individual individual) {
		if (problem.compareFitness(bestSolutionFitness, individual.getSolutionFitness())) {
			bestSolutionFitness = individual.getSolutionFitness();
		}
	}
	
	private double trimPositionToProblemLimits(double position, int dimension) {
		double retorno = position;
		
		if (retorno > problem.getUpperLimit(dimension)) {
			retorno = problem.getUpperLimit(dimension);
		} else if (retorno < problem.getLowerLimit(dimension)) {
			retorno = problem.getLowerLimit(dimension);
		}
		
		return retorno;
	}
	
	private double[] getInitialSolution() {
		double[] position = new double[dimensions];
		Random random = new Random(System.nanoTime());

		for (int i = 0; i < dimensions; i++) {
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
