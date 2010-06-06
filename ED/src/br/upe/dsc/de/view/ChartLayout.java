package br.upe.dsc.de.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Image;

import javax.swing.JFrame;

import ChartDirector.ChartViewer;
import br.upe.dsc.de.problem.IProblem;

public class ChartLayout extends JFrame implements Runnable {
	private static final long serialVersionUID = 1L;

	private PopulationObserver populationObserver;
	private ChartViewer viewer;
	private boolean running;
	private GCanvas canvas;

	public ChartLayout(IProblem problem) {
		super(problem.getName());

		setBounds(50, 50, 800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container con = this.getContentPane();
		con.setBackground(Color.white);
		canvas = new GCanvas(problem);
		con.add(canvas);
		setVisible(true);

	}

	/**
	 * Name of the chart.
	 */
	public String toString() {
		return "Differential Evolution";
	}

	/**
	 * Number of charts produced
	 */
	public int getNoOfCharts() {
		return 1;
	}

	public Image createChartImage() {
		return null;
	}

	// Main code for creating charts
	public void createChart(double[] solution) {
		canvas.setSolution(solution.clone());
		// this.paint(getGraphics());
		//this.repaint();
		// this.repaint();
		// Output the chart
		// Image image = createChartImage();
		// viewer.setImage(image);
	}

	@Override
	public void run() {
		while (running) {
			// createChart();
			// this.paint(getGraphics());
			canvas.repaint();
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public PopulationObserver getSwarmObserver() {
		return populationObserver;
	}

	public void setPopulationObserver(PopulationObserver populationObserver) {
		this.populationObserver = populationObserver;
	}

	public ChartViewer getViewer() {
		return viewer;
	}

	public void setViewer(ChartViewer viewer) {
		this.viewer = viewer;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}