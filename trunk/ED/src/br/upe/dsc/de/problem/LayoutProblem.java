package br.upe.dsc.de.problem;

import java.util.ArrayList;

/**
 * The problem proposed:
 * 
 * +------+    +---+
 * |      | -> | B |
 * |      |    +---+    +---+
 * |      |          -> | C |
 * |      |    +---+    +---+    +------+
 * |  A   | -> | B |          -> |  D   |
 * |      |    +---+    +---+    +------+
 * |      |          -> | C |
 * |      |    +---+    +---+
 * |      | -> | B |
 * +------+    +---+
 * 
 */
public class LayoutProblem implements IProblem {
	int dimensions;
	double[] leftBounds;
	double[] rightBounds;
	
	int qtyMachines;
	ArrayList<LayoutMachine> machines;
	ArrayList<LayoutLink> machinesLinks;
	
	public LayoutProblem() {
		super();
		qtyMachines = 7;
		
		// Creating machines
		machines = new ArrayList<LayoutMachine>();
		machines.add(new LayoutMachine("A1", 8, 24)); // A1
		machines.add(new LayoutMachine("B1", 8, 8));   // B1
		machines.add(new LayoutMachine("B2", 8, 8));   // B2
		machines.add(new LayoutMachine("B3", 8, 8));   // B3
		machines.add(new LayoutMachine("C1", 8, 8));   // C1
		machines.add(new LayoutMachine("C2", 8, 8));   // C2
		machines.add(new LayoutMachine("C2", 16, 8));  // D1
		
		// Creating links
		machinesLinks = new ArrayList<LayoutLink>();
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 1, LayoutMachine.LEFT)); // A1->B1
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 2, LayoutMachine.LEFT)); // A1->B2
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 3, LayoutMachine.LEFT)); // A1->B3
		machinesLinks.add(new LayoutLink(1, LayoutMachine.RIGHT, 4, LayoutMachine.LEFT)); // B1->C1
		machinesLinks.add(new LayoutLink(2, LayoutMachine.RIGHT, 4, LayoutMachine.LEFT)); // B2->C1
		machinesLinks.add(new LayoutLink(2, LayoutMachine.RIGHT, 5, LayoutMachine.LEFT)); // B2->C2
		machinesLinks.add(new LayoutLink(3, LayoutMachine.RIGHT, 5, LayoutMachine.LEFT)); // B3->C2
		machinesLinks.add(new LayoutLink(4, LayoutMachine.RIGHT, 6, LayoutMachine.LEFT)); // C1->D1
		machinesLinks.add(new LayoutLink(5, LayoutMachine.RIGHT, 6, LayoutMachine.LEFT)); // C2->D1
		
		dimensions = (3 * qtyMachines);
		leftBounds = new double[dimensions];
		rightBounds = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			double[] dimMax = new double[]{200,200,1};
			leftBounds[i] = 0;
			rightBounds[i] = dimMax[ (i % 3) ];
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "Layout Problem";
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int getDimensionsNumber() {
		return dimensions;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double getLowerLimit(int dimension) {
		return leftBounds[dimension];
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double getUpperLimit(int dimension) {
		return rightBounds[dimension];
	}
	
	/**
	 * {@inheritDoc}
	 */
	public boolean compareFitness(double pBestFitness, double currentPositionFitness) {
		return currentPositionFitness > pBestFitness;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public double getFitness(double... variables) {
		double result = 0;
		double x, y, p;
		int pos;
		LayoutMachine machine1, machine2;
		
		// Updating the position of all machines
		for (int i = 0; i < qtyMachines; i++) {
			x = variables[ ((i*3)+0) ];
			y = variables[ ((i*3)+1) ];
			System.out.println(((i*3)+2) +" - "+ variables.length);
			p = variables[ ((i*3)+2) ];
			pos = this.convertPosition(p);
			machine1 = machines.get(i);
			machine1.updatePosition(x, y, pos);
		}
		
		double x1, x2, y1, y2, w, h;
		int s, e;
		for (LayoutLink machineLink : machinesLinks) {
			x1 = 0.0;
			x2 = 0.0;
			y1 = 0.0;
			y2 = 0.0;
			machine1 = machines.get(machineLink.getSourceIndex());
			machine2 = machines.get(machineLink.getDestIndex());
			
			// Get the current position of the machines
			s = (machine1.getPosition() + machineLink.getSourceSide()) % 4;
			e = (machine2.getPosition() + machineLink.getDestSide()) % 4;
			
			switch (s) {
				case LayoutMachine.TOP :
					x1 = (machine1.getX2() - machine1.getX1()) / 2.0;
					y1 = machine1.getX1();
					break;
				case LayoutMachine.BOTTOM :
					x1 = (machine1.getX2() - machine1.getX1()) / 2.0;
					y1 = machine1.getX2();
					break;
				case LayoutMachine.LEFT :
					x1 = machine1.getX2();
					y1 = (machine1.getY2() - machine1.getY1()) / 2.0;
					break;
				case LayoutMachine.RIGHT :
					x1 = machine1.getX1();
					y1 = (machine1.getY2() - machine1.getY1()) / 2.0;
					break;
			}
			switch (e) {
				case LayoutMachine.TOP :
					x2 = (machine2.getX2() - machine2.getX1()) / 2.0;
					y2 = machine2.getX1();
					break;
				case LayoutMachine.BOTTOM :
					x2 = (machine2.getX2() - machine2.getX1()) / 2.0;
					y2 = machine2.getX2();
					break;
				case LayoutMachine.LEFT :
					x2 = machine2.getX2();
					y2 = (machine2.getY2() - machine2.getY1()) / 2.0;
					break;
				case LayoutMachine.RIGHT :
					x2 = machine2.getX1();
					y2 = (machine2.getY2() - machine2.getY1()) / 2.0;
					break;
			}
			w = Math.abs(x2 - x1);
			h = Math.abs(y2 - y1);
			result = Math.sqrt((w*w) + (h*h));
		}
		return result;
	}
	
	public boolean verifyConstraints(double... variables) {
		// Updating the position of all machines
		double x, y, p;
		int pos;
		LayoutMachine machine1, machine2;
		for (int i = 0; i < qtyMachines; i++) {
			x = variables[ ((i*3)+0) ];
			y = variables[ ((i*3)+1) ];
			p = variables[ ((i*3)+2) ];
			pos = this.convertPosition(p);
			machine1 = machines.get(i);
			machine1.updatePosition(x, y, pos);
		}
		
		// Verify if the machines can be into the area
		for (int i = 0; i < qtyMachines; i++) {
			machine1 = machines.get(i);
			if (machine1.getX1() < leftBounds[ ((i*3)+0) ]) return false;
			if (machine1.getY1() < leftBounds[ ((i*3)+1) ]) return false;
			if (machine1.getX2() > rightBounds[ ((i*3)+0) ]) return false;
			if (machine1.getY2() > rightBounds[ ((i*3)+1) ]) return false;
		}
		
		// Verify if exists a machine under other machine
		for (int i = 0; i < qtyMachines; i++) {
			machine1 = machines.get(i);
			for (int j = (i + 1); j < qtyMachines; j++) {
				machine2 = machines.get(j);
				if (!(machine1.getX1() > machine2.getX2() || machine1.getX2() < machine2.getX1() ||
					machine1.getY1() > machine2.getY2() || machine1.getY2() < machine2.getY1())) {
					return false;
				}
			}
		}
		return true;
	}
	
	private int convertPosition(double position) {
		if (position <= 0.25) return 0;
		if (position <= 0.5) return 1;
		if (position <= 0.75) return 2;
		return 3;
	}
	
	public ArrayList<LayoutMachine> getMachines() {
		return machines;
	}
	
	public LayoutMachine getMachine(int index) {
		return machines.get(index);
	}
}