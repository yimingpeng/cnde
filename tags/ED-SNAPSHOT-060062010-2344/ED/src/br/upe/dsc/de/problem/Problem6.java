package br.upe.dsc.de.problem;

public class Problem6 implements IProblem {
    
	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "Problem 6";
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
            if (dimension == 2) {
                    return MINIMUM_DIMENSION_VALUE;
            }
            return -50.0;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public double getUpperLimit(int dimension) {
            return 50.0;
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
            return 0.7 + dimension[0] * dimension[0] + 2 * dimension[1] * dimension[1] - 0.3 * Math.cos(3 * Math.PI * dimension[0]) - 0.4 * Math.cos(4 * Math.PI * dimension[1]);
    }
    
	public boolean verifyConstraints(double... variables) {
		return true;
	}
}