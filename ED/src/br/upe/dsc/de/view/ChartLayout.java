package br.upe.dsc.de.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import ChartDirector.ChartViewer;
import br.upe.dsc.de.problem.IProblem;

public class ChartLayout extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	
	private PopulationObserver populationObserver;
	private ChartViewer viewer;
	private boolean running;
	private JFrame frame;
	private double[] dataX;
	private double[] dataY;
	private IProblem problem;
	
	public ChartLayout(JFrame frame, IProblem problem, PopulationObserver populationObserver, double[] dataX, double[] dataY) {
		this.frame = frame;
		this.populationObserver = populationObserver;
		this.dataX = dataX;
		this.dataY = dataY;
		this.problem = problem;
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
		double x1, x2, y1, y2, w, h, scale;
		int x, y, width, height, widthMax, heightMax, margin;
		Image mImage;
		x1 = problem.getLowerLimit(0);
		x2 = problem.getUpperLimit(0);
		y1 = problem.getLowerLimit(1);
		y2 = problem.getUpperLimit(1);
		w = Math.abs(x2 - x1);
		h = Math.abs(y2 - y1);
		
		
		// Calculating the positions and sizes
		widthMax = 800;
		heightMax = 600;
		margin = 20; // = 20px
		
		System.out.println(w +" - "+ h);
		// Resizing the image
		width = (widthMax - margin);
		height = (int) Math.round((width * (heightMax - margin)) / width);
		if (height > (heightMax - margin)) {
			height = (heightMax - margin);
			width = (int) Math.round((height * (widthMax - margin)) / height);
		}
		scale = width / w;
		x = (int) Math.round(margin / 2.0);
		y = (int) Math.round(margin / 2.0);
		
		mImage = createImage(width + margin, height + margin);
		//BufferedImage bufferedImage = new BufferedImage(mImage.getWidth(null), mImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
		//Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();
		Graphics2D g2 = (Graphics2D) mImage.getGraphics();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, width, height);
		
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(x, y, (int) Math.ceil(w * scale), (int) Math.ceil(h * scale));
		
		return mImage;
	}
	
	// Main code for creating charts
	public void createChart() {

		// Output the chart
		Image image = createChartImage();
		viewer.setImage(image);
	}
	
	@Override
	public void run() {
		while (running) {
			createChart();
			if (frame != null) {
				frame.repaint();
			}
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

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}