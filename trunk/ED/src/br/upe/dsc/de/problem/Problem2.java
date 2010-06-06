package br.upe.dsc.de.problem;

public class Problem2 implements IProblem {

	/**
	 * {@inheritDoc}
	 */
	public String getName() {
		return "Problem 2";
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
            return -2.048;
    }
    
    /**
	 * {@inheritDoc}
	 */
    public double getUpperLimit(int dimension) {
            return 2.048;
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
            double fator1 = (dimension[1] - dimension[0] * dimension[0]);
            return 100 * fator1 * fator1 + (1 - dimension[0]) * (1 - dimension[0]);
    }
    
	public boolean verifyConstraints(double... variables) {
		return true;
	}
}