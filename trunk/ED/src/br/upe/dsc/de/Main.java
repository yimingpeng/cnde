package br.upe.dsc.de;

import br.upe.dsc.de.algorithm.DifferentialEvolution;
import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.RandomPeaksProblem;

public class Main {

	public static void main(String[] args) {
		IProblem problem = new RandomPeaksProblem();
		
		for (int i = 0; i < 30; i++) {
			DifferentialEvolution de = new DifferentialEvolution(100, 100, 0.01, 2, 0.6, problem);
			runSimple(de);
		}
	}

	private static void runSimple(DifferentialEvolution de) {
		de.run();
	}
}
