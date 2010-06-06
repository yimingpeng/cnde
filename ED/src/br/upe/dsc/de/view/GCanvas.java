package br.upe.dsc.de.view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.LayoutProblem;

class GCanvas extends Canvas // create a canvas for your graphics
{
	public void paint(Graphics g) // display shapes on canvas
	{
		IProblem problem = new LayoutProblem();
		Graphics2D g2D = (Graphics2D) g; // cast to 2D
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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

		System.out.println(w + " - " + h);
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

		g2D.setColor(Color.WHITE);
		g2D.fillRect(0, 0, width, height);

		g2D.setColor(Color.LIGHT_GRAY);
		g2D.fillRect(x, y, (int) Math.ceil(w * scale), (int) Math.ceil(h * scale));
	}

	public void drawArc(Graphics2D g2D, int x1, int y1, int x2, int y2, int sd, int rd, int cl) {
		Arc2D.Float arc1 = new Arc2D.Float(x1, y1, x2, y2, sd, rd, cl);
		g2D.fill(arc1);
	}

	public void drawEllipse(Graphics2D g2D, int x1, int y1, int x2, int y2) {
		Ellipse2D.Float oval1 = new Ellipse2D.Float(x1, y1, x2, y2);
		g2D.fill(oval1);
	}
}
