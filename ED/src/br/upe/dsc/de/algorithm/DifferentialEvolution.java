package br.upe.dsc.de.algorithm;

import java.util.Random;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.view.PopulationObserver;

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
	private boolean delayExecution;

	/**
	 * Creates a new instance of this Differential Evolution implementation.
	 * 
	 * @param populationSize
	 *            Size of the population of individuals that will be created
	 * @param maximumIterations
	 *            Maximum number of iterations that will be performed
	 * @param standardDeviation
	 *            Standard deviation
	 * @param scaleFactor
	 *            Scale factor
	 * @param recombinationProbability
	 *            Controls the probability with which an individual will be
	 *            recombined with the experimental vector created during the
	 *            mutation phase.
	 * @param problem
	 *            An instance of the problem to be solved
	 * @param populationObserver
	 *            An instance of a population observer
	 * @param problem
	 *            The problem to be solved.
	 * @param delayExecution
	 *            Determines if the algorithm will have a delay after each
	 *            iteration. It should only be used when running in the graphics
	 *            mode.
	 */
	public DifferentialEvolution(int populationSize, int maximumIterations, double standardDeviation,
		double scaleFactor, double recombinationProbability, IProblem problem, PopulationObserver populationObserver,
		boolean delayExecution) {

		this.dimensions = problem.getDimensionsNumber();
		this.populationSize = populationSize;
		this.maximumIterations = maximumIterations;
		this.standardDeviation = standardDeviation;
		this.scaleFactor = scaleFactor;
		this.recombinationProbability = recombinationProbability;
		this.problem = problem;
		this.populationObserver = populationObserver;
		this.delayExecution = delayExecution;
		population = new Individual[populationSize];
		allFitness = new double[populationSize];
	}

	/**
	 * Runs the algorithm until one stop criteria be found.
	 */
	public void run() {
		init();
		System.exit(0);
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
	 * Returns the instance of PopulationObserver currently associated with this
	 * instance.
	 * 
	 * @return The instance of PopulationObserver currently associated with this
	 *         instance.
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
		populationObserver.getFileManager().printFileHeader(populationSize, maximumIterations, standardDeviation,
			scaleFactor, recombinationProbability);

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
		try {
			if (delayExecution) {
				Thread.sleep(250);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			position = targetIndividualSolution[i] + scaleFactor * (individualASolution[i] - individualBSolution[i]);
			experimentalSolution[i] = trimPositionToProblemLimits(position, i);
		}
		experimentalIndividual.updateSolution(experimentalSolution, problem.getFitness(experimentalSolution));

		return experimentalIndividual;
	}

	// Performs the crossover phase of the algorithm, in which occurs the
	// recombination of
	// the current individual with the experimental vector created during the
	// mutation phase.
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

	// Returns an random index between 0 and the number of dimensions of the
	// current problem and that
	// is different from the indexes received as parameters.
	private int getRandomIndex(int currentIndividualIndex, int individualAIndex, int individualBIndex) {
		Random random = new Random(System.nanoTime());
		int randomIndex = random.nextInt(populationSize);

		while (randomIndex == currentIndividualIndex || randomIndex == individualAIndex
			|| randomIndex == individualBIndex) {
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

		do {
			System.out.println("Inicial...");
			for (int i = 0; i < dimensions; i++) {
				double value = random.nextDouble();
	
				position[i] = (this.problem.getUpperLimit(i) - this.problem.getLowerLimit(i)) * value
					+ this.problem.getLowerLimit(i);
	
				position[i] = (position[i] <= this.problem.getUpperLimit(i)) ? position[i] : this.problem.getUpperLimit(i);
				position[i] = (position[i] >= this.problem.getLowerLimit(i)) ? position[i] : this.problem.getLowerLimit(i);
			}
		} while (!this.problem.verifyConstraints(position));

		return position;
	}
}
