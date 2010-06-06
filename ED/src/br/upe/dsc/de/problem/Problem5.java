package br.upe.dsc.de.problem;

public class Problem5 implements IProblem {
    
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "Problem 5";
	}
	
	/**
	 * {@inheritDoc}
	 */
    public int getDimensionsNumber() {
            return 3;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public double getLowerLimit(int dimension) {
            if (dimension == 0) {
                    return -3.0;
            }
            else if (dimension == 2) {
                    return MINIMUM_DIMENSION_VALUE;
            }
            return 4.1;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public double getUpperLimit(int dimension) {
            if (dimension == 0) {
                    return 12.1;
            }
            return 5.8;
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
    public double getFitness(double... dimension) {
            return 21.5 + dimension[0] * Math.sin(4 * Math.PI * dimension[0]) + dimension[1] * Math.sin(20 * Math.PI * dimension[1]);
    }
    
	public boolean verifyConstraints(double... variables) {
		return true;
	}
}