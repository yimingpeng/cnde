package br.upe.dsc.de.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

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
		for (int i = 0; i < 1; i++) {
			populationObserver.resetIteration();
			de = new DifferentialEvolution(populationSize, maximumIterations, standardDeviation, scaleFactor,
				recombinationProbability, problem, populationObserver, false);
			de.run();
		}
	}

	/**
	 * Runs in the graphics mode. It displays an window with a chart of the
	 * problem being solved and shows the individuals moving on the screen. It
	 * also saves an image of the chart immediately after it stops running.
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
		ChartLayout chart;

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
		// chart = new ChartView(frame, de.getProblem(),
		// de.getPopulationObserver(), dataX, dataY);
		chart = new ChartLayout(de.getProblem());

		// Create the chart and put them in the content pane
		chart.setViewer(new ChartViewer());
		// chart.createChart();
		frame.getContentPane().add(chart.getViewer());

		// Display the window
		frame.pack();
		frame.setVisible(true);

		// Runs the algorithm
		chart.setRunning(true);
		// new Thread(chart).start();
		de.run();

		chart.setRunning(false);

		// Gets the last image of the chart
		Image image = chart.getViewer().getImage();

		// Creates a BufferdImage from the Image
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
			BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, null, null);
		g2.dispose();

		// Prints the image to a file
		populationObserver.getFileManager().printImage(bufferedImage);
	}

	public static void runChartLayout(int populationSize, int maximumIterations, double standardDeviation,
		double scaleFactor, double recombinationProbability, IProblem problem, PopulationObserver populationObserver) {

		ChartLayout chart = new ChartLayout(problem);
		DifferentialEvolution de = new DifferentialEvolution(populationSize, maximumIterations, standardDeviation,
			scaleFactor, recombinationProbability, problem, populationObserver, true);

		de.setChartLayout(chart);

		// Runs the algorithm
		chart.setRunning(true);
		new Thread(chart).start();
		de.run();

		chart.setRunning(false);
	}
}
