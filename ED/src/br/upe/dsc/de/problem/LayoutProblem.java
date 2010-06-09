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
	ArrayList<LayoutMapRestriction> mapRestrictions;
	
	public LayoutProblem() {
		super();
		qtyMachines = 6;
		
		// Creating machines
		machines = new ArrayList<LayoutMachine>();
		machines.add(new LayoutMachine("A1", 10, 20)); // A1
		machines.add(new LayoutMachine("B1", 10, 10)); // B1
		machines.add(new LayoutMachine("B2", 10, 10)); // B2
		machines.add(new LayoutMachine("C1", 10, 10)); // C1
		machines.add(new LayoutMachine("C2", 10, 10)); // C2
		machines.add(new LayoutMachine("D1", 10, 20)); // D1
		
		// Creating links
		machinesLinks = new ArrayList<LayoutLink>();
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 1, LayoutMachine.LEFT)); // A1->B1
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 2, LayoutMachine.LEFT)); // A1->B2
		machinesLinks.add(new LayoutLink(1, LayoutMachine.RIGHT, 3, LayoutMachine.LEFT)); // B1->C1
		machinesLinks.add(new LayoutLink(2, LayoutMachine.RIGHT, 4, LayoutMachine.LEFT)); // B2->C2
		machinesLinks.add(new LayoutLink(3, LayoutMachine.RIGHT, 5, LayoutMachine.LEFT)); // C1->D1
		machinesLinks.add(new LayoutLink(4, LayoutMachine.RIGHT, 5, LayoutMachine.LEFT)); // C2->D1
		
		// Creating restrictions
		mapRestrictions = new ArrayList<LayoutMapRestriction>();
		mapRestrictions.add(new LayoutMapRestriction(0, 0, 20, 10));
		mapRestrictions.add(new LayoutMapRestriction(0, 0, 10, 20));
		mapRestrictions.add(new LayoutMapRestriction(50, 0, 20, 10));
		mapRestrictions.add(new LayoutMapRestriction(60, 0, 10, 20));
		mapRestrictions.add(new LayoutMapRestriction(0, 60, 20, 10));
		mapRestrictions.add(new LayoutMapRestriction(0, 50, 10, 20));
		mapRestrictions.add(new LayoutMapRestriction(50, 60, 20, 10));
		mapRestrictions.add(new LayoutMapRestriction(60, 50, 10, 20));
		//mapRestrictions.add(new LayoutMapRestriction(30, 30, 10, 10));
		
		/*
		qtyMachines = 6;
		
		// Creating machines
		machines = new ArrayList<LayoutMachine>();
		machines.add(new LayoutMachine("A1", (int) (17 * 0.8), (int) (27 * 0.8))); // A1
		machines.add(new LayoutMachine("B1", (int) (17 * 0.8), (int) (34 * 0.8))); // B1
		machines.add(new LayoutMachine("B2", (int) (17 * 0.8), (int) (30 * 0.8))); // B2
		machines.add(new LayoutMachine("C1", (int) (39 * 0.8), (int) (17 * 0.8))); // C1
		machines.add(new LayoutMachine("C2", (int) (39 * 0.8), (int) (17 * 0.8))); // C2
		machines.add(new LayoutMachine("D1", (int) (16 * 0.8), (int) (23 * 0.8))); // D1
		
		// Creating links
		machinesLinks = new ArrayList<LayoutLink>();
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 1, LayoutMachine.LEFT)); // A1->B1
		machinesLinks.add(new LayoutLink(0, LayoutMachine.RIGHT, 2, LayoutMachine.LEFT)); // A1->B2
		machinesLinks.add(new LayoutLink(1, LayoutMachine.RIGHT, 3, LayoutMachine.LEFT)); // B1->C1
		machinesLinks.add(new LayoutLink(2, LayoutMachine.RIGHT, 4, LayoutMachine.LEFT)); // B2->C2
		machinesLinks.add(new LayoutLink(3, LayoutMachine.RIGHT, 5, LayoutMachine.LEFT)); // C1->D1
		machinesLinks.add(new LayoutLink(4, LayoutMachine.RIGHT, 5, LayoutMachine.LEFT)); // C2->D1
		
		// Creating restrictions
		mapRestrictions = new ArrayList<LayoutMapRestriction>();
		mapRestrictions.add(new LayoutMapRestriction(0, 0, 54, 16));
		mapRestrictions.add(new LayoutMapRestriction(0, 0, 12, 24));
		mapRestrictions.add(new LayoutMapRestriction(0, 78, 12, 22));
		mapRestrictions.add(new LayoutMapRestriction(55, 89, 45, 11));
		*/
		
		dimensions = (3 * qtyMachines);
		leftBounds = new double[dimensions];
		rightBounds = new double[dimensions];
		for (int i = 0; i < dimensions; i++) {
			double[] dimMax = new double[]{70,70,1};
			//double[] dimMax = new double[]{100,100,1};
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
		return currentPositionFitness < pBestFitness;
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
			p = variables[ ((i*3)+2) ];
			pos = convertPosition(p);
			machine1 = machines.get(i);
			machine1.updatePosition(x, y, pos);
		}
		
		double x1, x2, y1, y2, w, h;
		int posSaida, posEntrada;
		for (LayoutLink machineLink : machinesLinks) {
			x1 = 0.0;
			x2 = 0.0;
			y1 = 0.0;
			y2 = 0.0;
			machine1 = machines.get(machineLink.getSourceIndex());
			machine2 = machines.get(machineLink.getDestIndex());
			
			// Get the current position of the machines
			posSaida = (machine1.getPosition() + machineLink.getSourceSide()) % 4;
			posEntrada = (machine2.getPosition() + machineLink.getDestSide()) % 4;
			double value;
			
			switch (posSaida) {
				case LayoutMachine.TOP :
					x1 = ((machine1.getX2() - machine1.getX1()) / 2.0) + machine1.getX1();
					y1 = machine1.getY1();
					break;
				case LayoutMachine.BOTTOM :
					x1 = ((machine1.getX2() - machine1.getX1()) / 2.0) + machine1.getX1();
					y1 = machine1.getY2();
					break;
				case LayoutMachine.LEFT :
					x1 = machine1.getX1();
					y1 = ((machine1.getY2() - machine1.getY1()) / 2.0) + machine1.getY1();
					break;
				case LayoutMachine.RIGHT :
					x1 = machine1.getX2();
					y1 = ((machine1.getY2() - machine1.getY1()) / 2.0) + machine1.getY1();
					break;
			}
			switch (posEntrada) {
				case LayoutMachine.TOP :
					x2 = ((machine2.getX2() - machine2.getX1()) / 2.0) + machine2.getX1();
					y2 = machine2.getY1();
					break;
				case LayoutMachine.BOTTOM :
					x2 = ((machine2.getX2() - machine2.getX1()) / 2.0) + machine2.getX1();
					y2 = machine2.getY2();
					break;
				case LayoutMachine.LEFT :
					x2 = machine2.getX1();
					y2 = ((machine2.getY2() - machine2.getY1()) / 2.0) + machine2.getY1();
					break;
				case LayoutMachine.RIGHT :
					x2 = machine2.getX2();
					y2 = ((machine2.getY2() - machine2.getY1()) / 2.0) + machine2.getY1();
					break;
			}
			w = Math.abs(x2 - x1);
			h = Math.abs(y2 - y1);
			value = (w*w) + (h*h);
			result += (value * value);
		}
		
		
		for (int i = 0; i < qtyMachines; i++) {
			machine1 = machines.get(i);
			
			if (machine1.getX1() < leftBounds[ ((i*3)+0) ] ||
			machine1.getY1() < leftBounds[ ((i*3)+1) ]||
			machine1.getX2() > rightBounds[ ((i*3)+0) ]||
			machine1.getY2() > rightBounds[ ((i*3)+1) ]){ 
				
				result +=1000000;
				
			}
			
			for (int j = (i + 1); j < qtyMachines; j++) {
				machine2 = machines.get(j);
				if (!(machine1.getX1() > machine2.getX2() || machine1.getX2() < machine2.getX1() ||
					machine1.getY1() > machine2.getY2() || machine1.getY2() < machine2.getY1())) {
					
					double x_ =  Math.abs(machine2.getX2() - machine1.getX1());
					double y_ = Math.abs(machine2.getY2() - machine1.getY1());
					
					double area = x_ * y_;
					
					result += Math.pow(area, 7);
				}
			}
			
			for (LayoutMapRestriction mapRestriction : mapRestrictions) {
				if (!(machine1.getX1() > mapRestriction.getX2() || machine1.getX2() < mapRestriction.getX1() ||
						machine1.getY1() > mapRestriction.getY2() || machine1.getY2() < mapRestriction.getY1())) {
					
					double x_ =  Math.abs(mapRestriction.getX2() - machine1.getX1());
					double y_ = Math.abs(mapRestriction.getY2() - machine1.getY1());
					
					double area = x_ * y_;
					
					result += Math.pow(area, 7);
				}
			}
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
		
		//x1 e y1 representam o canto inferior esquerdo do retângulo
		//x2 e y2 representam o canto superior direito do retângulo
		// Verify if exists a machine under other machine
		for (int i = 0; i < qtyMachines; i++) {
			machine1 = machines.get(i);
			for (int j = (i + 1); j < qtyMachines; j++) {
				machine2 = machines.get(j);
				if (!(machine1.getX1() > machine2.getX2() || machine1.getX2() < machine2.getX1() ||
					machine1.getY1() > machine2.getY2() || machine1.getY2() < machine2.getY1())) {
					
					double x_ =  Math.abs(machine2.getX2() - machine1.getX1());
					double y_ = Math.abs(machine2.getY2() - machine1.getY1());
					
					double area = x_ * y_;
					
					area = Math.pow(area, 5);
					
					return false;
				}
			}
			for (LayoutMapRestriction mapRestriction : mapRestrictions) {
				if (!(machine1.getX1() > mapRestriction.getX2() || machine1.getX2() < mapRestriction.getX1() ||
						machine1.getY1() > mapRestriction.getY2() || machine1.getY2() < mapRestriction.getY1())) {
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
	
	public ArrayList<LayoutLink> getLinks() {
		return machinesLinks;
	}
}