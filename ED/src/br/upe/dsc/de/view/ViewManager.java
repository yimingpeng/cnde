package br.upe.dsc.de.view;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ChartDirector.ChartViewer;
import br.upe.dsc.de.algorithm.DifferentialEvolution;
import br.upe.dsc.de.problem.IProblem;

public class ViewManager {

	/**
	 * Runs in text mode.
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
	 */
	public static void runText(int populationSize, int maximumIterations, double standardDeviation, double scaleFactor,
		double recombinationProbability, IProblem problem, PopulationObserver populationObserver) {

		DifferentialEvolution de;
		for (int i = 0; i < 30; i++) {
			populationObserver.resetIteration();
			de = new DifferentialEvolution(populationSize, maximumIterations, standardDeviation, scaleFactor,
				recombinationProbability, problem, populationObserver, false);
			de.run();
		}
	}

	/**
	 * Runs in the graphics mode. It displays an window with a chart of the
	 * problem being solved and shows the individuals moving on the screen.
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
	 */
	public static void runChart(int populationSize, int maximumIterations, double standardDeviation,
		double scaleFactor, double recombinationProbability, IProblem problem, PopulationObserver populationObserver) {

		DifferentialEvolution de = new DifferentialEvolution(populationSize, maximumIterations, standardDeviation,
			scaleFactor, recombinationProbability, problem, populationObserver, true);
		ChartView chart;

		// Create and set up the main window
		JFrame frame = new JFrame("PSO");
		frame.getContentPane().setBackground(Color.white);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// The x and y coordinates of the chart grid
		double[] dataX = new double[31];
		double[] dataY = new double[31];

		for (int i = 0; i < 31; i++) {
			dataX[i] = i;
			dataY[i] = i;
		}

		// Instantiate an instance of this chart
		chart = new ChartView(frame, de.getProblem(), de.getPopulationObserver(), dataX, dataY);

		// Create the chart and put them in the content pane
		chart.setViewer(new ChartViewer());
		chart.createChart();
		frame.getContentPane().add(chart.getViewer());

		// Display the window
		frame.pack();
		frame.setVisible(true);

		// Runs the algorithm
		chart.setRunning(true);
		new Thread(chart).start();
		de.run();

		chart.setRunning(false);
	}
}
