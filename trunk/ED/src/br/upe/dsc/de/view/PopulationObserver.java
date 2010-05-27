package br.upe.dsc.de.view;

import br.upe.dsc.de.algorithm.Individual;
import br.upe.dsc.de.util.FileManager;

/**
 * This class stores the position of each individual of the population of the differential
 * evolution algorithm at a given moment of time.
 */
public class PopulationObserver {
	private double[] xAxis;
	private double[] yAxis;
	private double[] zAxis;
	private int populationSize;
	private FileManager fileManager;
	private int iteration;
	
	/**
	 * Creates a new instance of this class.
	 * 
	 * @param populationSize The size of the population.
	 * @param fileManager An instance of the FileManager class.
	 */
	public PopulationObserver(int populationSize, FileManager fileManager) {
		this.populationSize = populationSize;
		this.fileManager = fileManager;
		
		xAxis = new double[populationSize];
		yAxis = new double[populationSize];
		zAxis = new double[populationSize];
		iteration = 1;
	}

	/**
	 * Updates this observer to reflect the current configuration of the population.
	 * 
	 * @param population The population that will have the position of each individual stored
	 * 					 by this observer.
	 */
	public void update(Individual[] population) {
		double[] currentSolution;
		
		fileManager.printIterationHeader(iteration++);
		
		for (int i = 0; i < populationSize; i++) {
			currentSolution = population[i].getSolution();
			xAxis[i] = currentSolution[0];
			yAxis[i] = currentSolution[1];
			zAxis[i] = population[i].getSolutionFitness();
			
			fileManager.printPosition(xAxis[i], yAxis[i], zAxis[i]);
		}
	}
	
	/**
	 * Restarts the iteration counter to its initial value.
	 */
	public void resetIteration() {
		iteration = 1;
	}
	
	/**
	 * Returns the X axis values.
	 * 
	 * @return The X axis values.
	 */
	public double[] getXAxis() {
		return xAxis;
	}

	/**
	 * Returns the Y axis values.
	 * 
	 * @return The Y axis values.
	 */
	public double[] getYAxis() {
		return yAxis;
	}

	/**
	 * Returns the Z axis values.
	 * 
	 * @return The Z axis values.
	 */
	public double[] getZAxis() {
		return zAxis;
	}

	/**
	 * Sets the X axis values.
	 * 
	 * @param xAxis The X axis values.
	 */
	public void setXAxis(double[] xAxis) {
		this.xAxis = xAxis;
	}

	/**
	 * Sets the Y axis values.
	 * 
	 * @param xAxis The Y axis values.
	 */
	public void setYAxis(double[] yAxis) {
		this.yAxis = yAxis;
	}

	/**
	 * Returns the instance of the FileManger currently used by this class.
	 * 
	 * @return The instance of the FileManger currently used by this class.
	 */
	public FileManager getFileManager() {
		return fileManager;
	}
}