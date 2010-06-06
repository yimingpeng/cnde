package br.upe.dsc.de.problem;

public class Problem3 implements IProblem {

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "Problem 3";
	}
	
	/**
	 * {@inheritDoc}
	 */
    public int getDimensionsNumber() {
            return 6;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public double getLowerLimit(int dimension) {
            if (dimension == 5) {
                    return MINIMUM_DIMENSION_VALUE;
            }
            return -5.12;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public double getUpperLimit(int dimension) {
            return 5.12;
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
            double result = 0;
            for (int i = 0; i < dimension.length - 1; i++){
                    result += Math.floor(dimension[i]);
            }
            return result;
    }
    
	public boolean verifyConstraints(double... variables) {
		return true;
	}
}