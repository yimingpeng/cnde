package br.upe.dsc.de.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.view.ChartView;
import br.upe.dsc.de.view.PopulationObserver;

/**
 * Helper class to create graphics images from the files of the simulation data
 */
public class ChartImageCreator {

	/**
	 * Creates a graphics image based on a previous simulation.
	 * 
	 * @param simulationFilePath
	 *            Path to the file that contains the simulation data
	 * @param simulationFileName
	 *            Name of the file that contains the simulation data
	 * @param chartFileName
	 *            Name of the image that will be created
	 * @param iteration
	 *            The iteration for which we wish to create the image
	 * @param problem
	 *            An instance of problem that was solved in the simulation
	 */
	public static void create(String simulationFilePath, String simulationFileName, String chartFileName,
		int iteration, IProblem problem) {

		BufferedReader chartReader = openReader(simulationFilePath, simulationFileName);
		int populationSize = getPopulationSize(chartReader);

		double[] xAxis = new double[populationSize];
		double[] yAxis = new double[populationSize];
		getPopulationSolutionConfiguration(chartReader, iteration, xAxis, yAxis);

		PopulationObserver populationObserver = new PopulationObserver(populationSize, null);
		populationObserver.setXAxis(xAxis);
		populationObserver.setYAxis(yAxis);

		// Configures the limit of the X and Y axis of the chart
		double[] dataX = new double[31];
		double[] dataY = new double[31];

		for (int i = 0; i < 31; i++) {
			dataX[i] = i;
			dataY[i] = i;
		}

		ChartView chart = new ChartView(null, problem, populationObserver, dataX, dataY);
		Image image = chart.createChartImage();

		// Creates a BufferdImage from the Image
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
			BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, null, null);
		g2.dispose();

		// Prints the image to a file
		try {
			ImageIO.write(bufferedImage, "PNG", new File(simulationFilePath, chartFileName + ".png"));
			chartReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void getPopulationSolutionConfiguration(BufferedReader chartReader, int iteration, double[] xAxis,
		double[] yAxis) {

		boolean foundIterationStart = false;
		int iterationNumber;
		String line;

		try {

			// Searches for the beginning of the iteration
			while (!foundIterationStart) {
				line = chartReader.readLine();
				if (line.toLowerCase().startsWith("iteration")) {
					iterationNumber = Integer.parseInt(line.split(" ")[1].trim());
					foundIterationStart = (iterationNumber == iteration);
				}
			}

			// Goes to the line that contains the position of the first
			// individual.
			line = chartReader.readLine();

			// Once the beginning of the iteration was found, reads the
			// individual's solution
			int i = 0;
			while (!line.startsWith("Iteration") && !line.startsWith("\n") && !line.equals("")) {
				xAxis[i] = Double.parseDouble(line.split(" ")[0].trim());
				yAxis[i] = Double.parseDouble(line.split(" ")[1].trim());
				i++;
				line = chartReader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedReader openReader(String simulationFilePath, String simulationFileName) {
		BufferedReader chartReader = null;

		try {
			chartReader = new BufferedReader(new FileReader(new File(simulationFilePath, simulationFileName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return chartReader;
	}

	private static int getPopulationSize(BufferedReader chartReader) {
		String line = "";

		while (!line.startsWith("Population size:")) {
			try {
				line = chartReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return Integer.parseInt(line.split(":")[1].trim());
	}
}
