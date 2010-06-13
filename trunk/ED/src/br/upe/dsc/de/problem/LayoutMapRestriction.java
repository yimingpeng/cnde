package br.upe.dsc.de.problem;

public class LayoutMapRestriction {
	double x1;
	double y1;
	double x2;
	double y2;
	double cx;
	double cy;
	double w;
	double h;
	
	public LayoutMapRestriction(double x1, double y1, double w, double h) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1 + w;
		this.y2 = y1 + h;
		this.w = w;
		this.h = h;
		this.cx = ((x2 - x1) / 2.0);
		this.cy = ((y2 - y1) / 2.0);
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
	
	public double getW() {
		return w;
	}
	
	public double getH() {
		return h;
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
}