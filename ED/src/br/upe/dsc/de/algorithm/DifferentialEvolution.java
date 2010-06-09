package br.upe.dsc.de.algorithm;

import java.util.Random;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.LayoutMachine;
import br.upe.dsc.de.problem.LayoutProblem;
import br.upe.dsc.de.view.ChartLayout;
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
	private double[] bestSolution;
	private double recombinationProbability;
	private IProblem problem;
	private PopulationObserver populationObserver;
	private boolean delayExecution;
	private ChartLayout chartLayout;

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
		double currentStandardDeviation;
		for (int i = 0; i < maximumIterations; i++) {
			iterate();

			currentStandardDeviation = Statistics.getStandardDeviation(allFitness);
			if (currentStandardDeviation < standardDeviation) {
				break;
			}

			chartLayout.createChart(bestSolution);

			if ((i % 50) == 0) {
				System.out.println("Iteração " + i + " - BestFitness: " + bestSolutionFitness +" - Desv. Padrão: "+ currentStandardDeviation);
				/*
				 * for (int j = 0; j < dimensions; j++) {
				 * System.out.println("Solucao["+ j +"] = "+ bestSolution[j]); }
				 */
			}
			// break;
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

		int[] availablePositions = divideAreaIntoPositions();
		
		for (int i = 0; i < populationSize; i++) {
			createIndividual(i, availablePositions.clone());
			// getRandomSolution();
		}

		bestSolutionFitness = population[0].getSolutionFitness();
		for (Individual individual : population) {
			calculateBestSolution(individual);
		}
	}

	private void createIndividual(int i, int[] availablePositions) {
		double[] initialSolution;
		population[i] = new Individual(dimensions);
//		initialSolution = getSolution(availablePositions);
		initialSolution = getRandomSolution();
		population[i].updateSolution(initialSolution, problem.getFitness(initialSolution));
		allFitness[i] = population[i].getSolutionFitness();
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
			bestSolution = individual.getSolution().clone();
		}
	}

	private double trimPositionToProblemLimits(double currentPosition, int dimension) {
		double position = currentPosition;

		if (position > problem.getUpperLimit(dimension)) {
			position = problem.getUpperLimit(dimension);
		} else if (position < problem.getLowerLimit(dimension)) {
			position = problem.getLowerLimit(dimension);
		}

		return position;
	}

	private double[] getSolution(int[] availablePositions) {
		double[] solution = new double[dimensions];
		Random random = new Random(System.nanoTime());
		double rightBound, leftBound;
		
		int availablePositionIndex = getNextAvailablePosition(availablePositions);
		
		for (int i = 0; i < dimensions; i += 3) {

			// Coordinates of X and Y axis
			solution[i] = availablePositions[availablePositionIndex];
			solution[i + 1] = availablePositions[availablePositionIndex+1];
			
			// Position of the machine
			leftBound = problem.getLowerLimit(i + 2);
			rightBound = problem.getUpperLimit(i + 2);
			
			solution[i + 2] = leftBound + (rightBound - leftBound) * random.nextDouble();
			if (solution[i + 2] > rightBound) {
				solution[i + 2] = rightBound;
			} else if (solution[i + 2] < leftBound) {
				solution[i + 2] = leftBound;
			}

			// Brand values as used
			availablePositions[availablePositionIndex] = -1;
			availablePositions[availablePositionIndex + 1] = -1;

			availablePositionIndex = getNextAvailablePosition(availablePositions);
		}
		
		return solution;
	}

	private int getNextAvailablePosition(int[] availablePositions) {
		Random random = new Random(System.nanoTime());
		int i;
		
		int[] possibleIndexes = new int[(availablePositions.length+1)/2];
		for (int j = 0, k = 0; j < possibleIndexes.length; j++, k += 2) {
			possibleIndexes[j] = k;
		}
		
		do {
			i = random.nextInt(possibleIndexes.length);
		} while (availablePositions[possibleIndexes[i]] == -1);
		
		return possibleIndexes[i];
	}

	private int[] divideAreaIntoPositions() {
		int maximumSide = 0;
		int currentSide = 0;
		
		for (LayoutMachine lm : ((LayoutProblem) problem).getMachines()) {
			currentSide = lm.getHeight() > lm.getWidth() ? (int) lm.getHeight() : (int) lm.getWidth();
			if (currentSide > maximumSide) {
				maximumSide = currentSide;
			}
		}
		
		int machineArea = maximumSide * maximumSide;
		int problemArea = (int) (problem.getUpperLimit(0) * problem.getUpperLimit(1));
		int numberAvailablePositions = problemArea / machineArea;

		int[] availablePositions = new int[numberAvailablePositions * 2];

		int machinesOnSameLine = (int) (problem.getUpperLimit(0) / maximumSide);

		int width = 0;
		int heigth = 0;
		int machineCount = 0;

		for (int i = 0; i < availablePositions.length; i += 2) {
			availablePositions[i] = width;
			availablePositions[i + 1] = heigth;

			width += maximumSide;
			machineCount++;

			// If the end of the line was hit, goes to the next line
			if (machineCount >= machinesOnSameLine) {
				heigth += maximumSide;
				width = 0;
				machineCount = 0;
			}
		}
		
		return availablePositions;
	}
	
	private double[] getRandomSolution() {
        double[] position = new double[dimensions];
        Random random = new Random(System.nanoTime());
        
       // do {
                for (int i = 0; i < dimensions; i++) {
                        double rightBound, leftBound, value = random.nextDouble();
                        rightBound = problem.getUpperLimit(i);
                        leftBound = problem.getLowerLimit(i);
                        
                        value = leftBound + (rightBound - leftBound) * value;
                        position[i] = value;
                        if (position[i] > rightBound) {
                                position[i] = rightBound;
                        }
                        if (position[i] < leftBound) {
                                position[i] = leftBound;
                        }
                }
       // } while (!problem.verifyConstraints(position));
        return position;
	}

	
	public void setChartLayout(ChartLayout chartLayout) {
		this.chartLayout = chartLayout;
	}
}