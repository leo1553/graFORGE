package net.leonardoleal.editorgrafo.models;

public interface GrafoObserver {
	public void verticeAddeded(Vertice vertice);
	public void verticeRemoved(Vertice vertice);
	public void arestaAddeded(Aresta aresta);
	public void arestaRemoved(Aresta aresta);
}
