package br.upe.dsc.de.algorithm;

import java.util.Random;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.algorithm.Statistics;

/**
 * Creates a new instance of this class.
 */
public class DifferentialEvolution {
	private int dimensions;
	private Individual[] population;
	private int maximumIterations;
	private double standardDeviation;
	private double scaleFactor;
	private double[] allFitness;
	private double bestSolutionFitness;
	private double recombinationProbability;
	private IProblem problem;
	
	/**
	 * Creates a new instance of this Differential Evolution implementation.
	 * 
	 * @param dimensions Number of dimensions in the search space
	 * @param problem The problem to be solved.
	 */
	public DifferentialEvolution(int dimensions, int maximumIterations, double standardDeviation, double scaleFactor,
			double recombinationProbability, IProblem problem) {
		this.dimensions = dimensions;
		this.maximumIterations = maximumIterations;
		this.standardDeviation = standardDeviation;
		this.scaleFactor = scaleFactor;
		this.recombinationProbability = recombinationProbability;
		this.problem = problem;
		population = new Individual[dimensions];
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
	
	private void init() {
		double[] initialSolution;
		for (int i = 0; i < dimensions; i++) {
			population[i] = new Individual(dimensions);
			initialSolution = getInitialSolution();
			population[i].updateSolution(initialSolution, problem.getFitness(initialSolution));
		}
		
		bestSolutionFitness = population[0].getSolutionFitness();
		for (Individual individual : population) {
			calculateBestSolution(individual);
		}
	}
	
	private void iterate() {
		Individual experimentalIndividual;
		double[] recombinationIndividualSolution;
		double recombinationIndividualSolutionFitness;
		
		for (int i = 0; i < dimensions; i++) {
			experimentalIndividual = mutation(i);
			recombinationIndividualSolution = crossover(population[i], experimentalIndividual);
			recombinationIndividualSolutionFitness = problem.getFitness(recombinationIndividualSolution);
			
			if (problem.compareFitness(population[i].getSolutionFitness(), recombinationIndividualSolutionFitness)) {
				population[i].updateSolution(recombinationIndividualSolution, recombinationIndividualSolutionFitness);
			}
		}
	}
	
	private Individual mutation(int currentIndividualIndex) {
		int targetIndividualIndex = getRandomIndex(currentIndividualIndex, -1, -1);
		int individualAIndex = getRandomIndex(currentIndividualIndex, targetIndividualIndex, -1);
		int individualBIndex = getRandomIndex(currentIndividualIndex, targetIndividualIndex, individualAIndex);
		
		double[] targetIndividualSolution = population[targetIndividualIndex].getSolution();
		double[] individualASolution = population[individualAIndex].getSolution();
		double[] individualBSolution = population[individualBIndex].getSolution();
		
		for (int i = 0; i < dimensions; i++) {
			targetIndividualSolution[i] = targetIndividualSolution[i] + scaleFactor*(
					individualASolution[i] - individualBSolution[i]);
		}
		
		return population[targetIndividualIndex];
	}
	
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
	
	private int getRandomIndex(int currentIndividualIndex, int individualAIndex, int individualBIndex) {
		Random random = new Random(System.nanoTime());
		int randomIndex = random.nextInt(dimensions);
		
		while (randomIndex == currentIndividualIndex || randomIndex == individualAIndex ||
				randomIndex == individualBIndex) {
			randomIndex = random.nextInt(dimensions);
		}
		
		return randomIndex;
	}
	
	private void calculateBestSolution(Individual individual) {
		if (problem.compareFitness(bestSolutionFitness, individual.getSolutionFitness())) {
			bestSolutionFitness = individual.getSolutionFitness();
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
