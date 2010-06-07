package br.upe.dsc.de.problem;

public class LayoutLink {
	int sourceIndex;
	int sourceSide;
	int destIndex;
	int destSide;
	
	public LayoutLink(int sourceIndex, int sourceSide, int destIndex, int destSide) {
		this.sourceIndex = sourceIndex;
		this.sourceSide = sourceSide;
		this.destIndex = destIndex;
		this.destSide = destSide;
	}
	
	public int getSourceIndex() {
		return sourceIndex;
	}
	
	public int getDestIndex() {
		return destIndex;
	}
	
	public int getSourceSide() {
		return sourceSide;
	}
	
	public int getDestSide() {
		return destSide;
	}
	
}