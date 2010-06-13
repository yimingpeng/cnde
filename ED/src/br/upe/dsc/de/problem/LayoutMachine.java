package br.upe.dsc.de.problem;

public class LayoutMachine {
	public static final int TOP = 0;
	public static final int RIGHT = 1;
	public static final int BOTTOM = 2;
	public static final int LEFT = 3;
	
	String name;
	double width, height;
	double x1, y1, x2, y2, cx, cy;
	int position;
	
	public LayoutMachine(String name, double width, double height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
	public String getName() {
		return name;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public double getX1() {
		return x1;
	}
	
	public double getY1() {
		return y1;
	}
	
	public double getX2() {
		return x2;
	}
	
	public double getY2() {
		return y2;
	}
	
	public double getHalfWidth() {
		return cx;
	}
	
	public double getHalfHeight() {
		return cy;
	}
	
	public double getCenterX() {
		return cx + x1;
	}
	
	public double getCenterY() {
		return cy + y1;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void updatePosition(double x, double y, int pos) {
		x1 = x;
		y1 = y;
		position = pos;
		x2 = x1;
		y2 = y1;
		if (pos == TOP || pos == BOTTOM) {
			x2 += width;
			y2 += height;
		}
		else {
			x2 += height;
			y2 += width;
		}
		cx = ((x2 - x1) / 2.0);
		cy = ((y2 - y1) / 2.0);
	}
	
	public void updatePosition(double x, double y) {
		x1 = x;
		y1 = y;
		x2 = x1 + width;
		y2 = y1 + height;
		cx = ((x2 - x1) / 2.0);
		cy = ((y2 - y1) / 2.0);
	}
}