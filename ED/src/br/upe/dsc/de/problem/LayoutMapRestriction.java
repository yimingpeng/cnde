package br.upe.dsc.de.problem;

public class LayoutMapRestriction {
	double x1;
	double y1;
	double w;
	double h;
	
	public LayoutMapRestriction(double x1, double y1, double w, double h) {
		this.x1 = x1;
		this.y1 = y1;
		this.w = w;
		this.h = h;
	}
	
	public double getX1() {
		return x1;
	}
	
	public double getY1() {
		return y1;
	}
	
	public double getX2() {
		return x1 + w;
	}
	
	public double getY2() {
		return y1 + h;
	}
	
	public double getW() {
		return w;
	}
	
	public double getH() {
		return h;
	}
}