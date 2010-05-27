package br.upe.dsc.de;

import br.upe.dsc.de.algorithm.DifferentialEvolution;
import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.Problem4;

public class Main {

	public static void main(String[] args) {
		IProblem problem = new Problem4();
		
		for (int i = 0; i < 30; i++) {
			DifferentialEvolution de = new DifferentialEvolution(100, 500, 0.01, 0.2, 0.2, problem);
			runSimple(de);
		}
	}

	private static void runSimple(DifferentialEvolution de) {
		de.run();
	}
}
