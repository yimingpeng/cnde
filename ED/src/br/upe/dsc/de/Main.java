package br.upe.dsc.de;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import ChartDirector.ChartViewer;
import br.upe.dsc.de.algorithm.DifferentialEvolution;
import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.RandomPeaksProblem;
import br.upe.dsc.de.util.FileManager;
import br.upe.dsc.de.view.ChartView;
import br.upe.dsc.de.view.PopulationObserver;

public class Main {

	public static void main(String[] args) {
		int populationSize = 100;
		IProblem problem = new RandomPeaksProblem();
		FileManager fileManager = new FileManager(problem.getName());
		PopulationObserver observer = new PopulationObserver(populationSize, fileManager);
		DifferentialEvolution de;
		
//		for (int i = 0; i < 30; i++) {
//			observer.resetIteration();
			de = new DifferentialEvolution(populationSize, 100, 0.01, 2, 0.6, problem, observer);
//			runSimple(de);
//		}
		
		runChart(de);
	}

	private static void runSimple(DifferentialEvolution de) {
		de.run();
	}
	
	public static void runChart(DifferentialEvolution de) {
		ChartView chart;
		
		// Create and set up the main window
		JFrame frame = new JFrame("PSO");
		frame.getContentPane().setBackground(Color.white);
//		frame.addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				System.exit(0);
//			}
//		});
		
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
		
		// Gets the last image of the chart
		Image image = chart.getViewer().getImage();
		
		// Creates a BufferdImage from the Image
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);  
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, null, null);  
		g2.dispose();
		
		// Prints the image to a file
		de.getPopulationObserver().getFileManager().printImage(bufferedImage);
		
		chart.setRunning(false);
		
//		frame.dispose();
	}
}
