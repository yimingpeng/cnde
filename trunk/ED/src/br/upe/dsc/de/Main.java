package br.upe.dsc.de;

import br.upe.dsc.de.problem.IProblem;
import br.upe.dsc.de.problem.RandomPeaksProblem;
import br.upe.dsc.de.util.FileManager;
import br.upe.dsc.de.view.PopulationObserver;
import br.upe.dsc.de.view.ViewManager;

public class Main {

	public static void main(String[] args) {
		int populationSize = 100;
		int maximumIterations = 100;
		double standardDeviation = 0.01;
		double scaleFactor = 2;
		double recombinationProbability = 0.6;
		IProblem problem = new RandomPeaksProblem();
		PopulationObserver observer = new PopulationObserver(populationSize, new FileManager(problem.getName()));

		ViewManager.runText(populationSize, maximumIterations, standardDeviation, scaleFactor,
			recombinationProbability, problem, observer);

		// ViewManager.runChart(populationSize, maximumIterations,
		// standardDeviation, scaleFactor,
		// recombinationProbability, problem, observer);
	}
}
