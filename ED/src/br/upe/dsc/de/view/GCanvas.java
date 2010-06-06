package br.upe.dsc.de.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.LayoutLink;
import br.upe.dsc.de.problem.LayoutMachine;
import br.upe.dsc.de.problem.LayoutProblem;

class GCanvas extends Canvas {
	private static final long serialVersionUID = 1L;
	private double[] solution;
	// private ArrayList<LayoutMachine> machines;
	private IProblem problem;

	public GCanvas(IProblem problem) {
		this.problem = problem;
		// machines = ((LayoutProblem) problem).getMachines();
		solution = new double[problem.getDimensionsNumber()];
		for (int i = 0; i < solution.length; i++) {
			solution[i] = 0.0;
		}
	}

	public void setSolution(double[] solution) {
		this.solution = solution;
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g; // cast to 2D
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		// g2D.setBackground(Color.white);
		// Stroke s = new BasicStroke(1);
		// g2D.setStroke(s);
		// g2D.setColor(Color.white);

		// IProblem problem = new LayoutProblem();
		double x1, x2, y1, y2, w, h, scale;
		int xx, yy, width, height, widthMax, heightMax, margin;
		x1 = problem.getLowerLimit(0);
		x2 = problem.getUpperLimit(0);
		y1 = problem.getLowerLimit(1);
		y2 = problem.getUpperLimit(1);
		w = Math.abs(x2 - x1);
		h = Math.abs(y2 - y1);

		// Calculating the positions and sizes
		widthMax = getWidth();
		heightMax = getHeight();
		margin = 20; // = 20px
		
		// Resizing the image
		width = (widthMax - margin);
		height = (int) Math.round((h * (widthMax - margin)) / w);
		if (height > (heightMax - margin)) {
			height = (heightMax - margin);
			width = (int) Math.round((w * (heightMax - margin)) / h);
		}
		scale = width / w;
		xx = (int) Math.round(margin / 2.0);
		yy = (int) Math.round(margin / 2.0);

		g2D.setColor(Color.WHITE);
		g2D.fillRect(xx, yy, width, height);

		g2D.setColor(Color.LIGHT_GRAY);
		g2D.drawRect(xx, yy, (int) Math.ceil(w * scale), (int) Math.ceil(h * scale));

		double x, y, p;
		int pos;
		LayoutMachine machine1, machine2;

		// Updating the position of all machines
		for (int i = 0; i < 7; i++) {
			x = solution[((i * 3) + 0)];
			y = solution[((i * 3) + 1)];
			p = solution[((i * 3) + 2)];
			pos = convertPosition(p);
			machine1 = ((LayoutProblem) problem).getMachine(i);
			machine1.updatePosition(x, y, pos);
			
			drawRectangle(g2D,
					(float) Math.round(((machine1.getX1() * scale) + xx)),
					(float) Math.round(((machine1.getY1() * scale) + yy)),
					(float) Math.round(((machine1.getX2() - machine1.getX1()) * scale)),
					(float) Math.round(((machine1.getY2() - machine1.getY1()) * scale))
			);
			g2D.drawString(machine1.getName(), Math.round(((((machine1.getX2() - machine1.getX1())/2.0) + machine1.getX1()) * scale) + xx - 5), Math.round(((((machine1.getY2() - machine1.getY1())/2.0) + machine1.getY1()) * scale) + yy + 10));
			if (machine1.getName() == "A1") {
				System.out.println("A1: "+
				Math.round(((machine1.getX1() * scale) + xx)) +" - "+
				Math.round(((machine1.getY1() * scale) + yy)) +" - "+
				Math.round(((machine1.getX2() - machine1.getX1()) * scale)) +" - "+
				Math.round(((machine1.getY2() - machine1.getY1()) * scale)));
			}
		}
		
		// Creating the links
		int posSaida, posEntrada;
		ArrayList<LayoutLink> machineLinks = ((LayoutProblem) problem).getLinks();
		for (LayoutLink machineLink : machineLinks) {
			x1 = 0.0;
			x2 = 0.0;
			y1 = 0.0;
			y2 = 0.0;
			machine1 = ((LayoutProblem) problem).getMachine(machineLink.getSourceIndex());
			machine2 = ((LayoutProblem) problem).getMachine(machineLink.getDestIndex());
			
			// Get the current position of the machines
			posSaida = (machine1.getPosition() + machineLink.getSourceSide()) % 4;
			posEntrada = (machine2.getPosition() + machineLink.getDestSide()) % 4;
			
			System.out.println(machine1.getPosition() +" - "+ machineLink.getSourceSide() +" - "+ posSaida);
			System.out.println("Scale: "+ scale);
			switch (posSaida) {
				case LayoutMachine.TOP :
					x1 = ((machine1.getX2() - machine1.getX1()) / 2.0) + machine1.getX1();
					y1 = machine1.getX1();
					System.out.println("TOP");
					break;
				case LayoutMachine.BOTTOM :
					x1 = ((machine1.getX2() - machine1.getX1()) / 2.0) + machine1.getX1();
					y1 = machine1.getX2();
					System.out.println("BOTTOM");
					break;
				case LayoutMachine.LEFT :
					x1 = machine1.getX2();
					y1 = ((machine1.getY2() - machine1.getY1()) / 2.0) + machine1.getY1();
					System.out.println("LEFT");
					System.out.println(machine1.getX1() +" - "+ machine1.getX2() +" - "+ machine1.getY1() +" - "+ machine1.getY2());
					System.out.println(x1 +" - "+ y1);
					break;
				case LayoutMachine.RIGHT :
					x1 = machine1.getX1();
					y1 = ((machine1.getY2() - machine1.getY1()) / 2.0) + machine1.getY1();
					System.out.println("RIGHT");
					break;
			}
			switch (posEntrada) {
				case LayoutMachine.TOP :
					x2 = ((machine2.getX2() - machine2.getX1()) / 2.0) + machine2.getX1();
					y2 = machine2.getX1();
					break;
				case LayoutMachine.BOTTOM :
					x2 = ((machine2.getX2() - machine2.getX1()) / 2.0) + machine2.getX1();
					y2 = machine2.getX2();
					break;
				case LayoutMachine.LEFT :
					x2 = machine2.getX2();
					y2 = ((machine2.getY2() - machine2.getY1()) / 2.0) + machine2.getY1();
					break;
				case LayoutMachine.RIGHT :
					x2 = machine2.getX1();
					y2 = ((machine2.getY2() - machine2.getY1()) / 2.0) + machine2.getY1();
					break;
			}
			g2D.drawLine(
					(int) Math.round((x1 * scale) + xx),
					(int) Math.round((y1 * scale) + yy),
					/*
					(int) Math.round((x2 * scale) + xx),
					(int) Math.round((y2 * scale) + yy)
					*/
					0,
					0
				);
			break;
		}
	}

	private int convertPosition(double position) {
		if (position <= 0.25)
			return 0;
		if (position <= 0.5)
			return 1;
		if (position <= 0.75)
			return 2;
		return 3;
	}

	public void drawArc(Graphics2D g2D, int x1, int y1, int x2, int y2, int sd, int rd, int cl) {
		Arc2D.Float arc1 = new Arc2D.Float(x1, y1, x2, y2, sd, rd, cl);
		g2D.fill(arc1);
	}

	public void drawEllipse(Graphics2D g2D, int x1, int y1, int x2, int y2) {
		Ellipse2D.Float oval1 = new Ellipse2D.Float(x1, y1, x2, y2);
		g2D.fill(oval1);
	}

	public void drawRectangle(Graphics2D g2D, float x1, float y1, float x2, float y2) {
		Rectangle2D.Float rec1 = new Rectangle2D.Float(x1, y1, x2, y2);
		g2D.draw(rec1);
	}
}
