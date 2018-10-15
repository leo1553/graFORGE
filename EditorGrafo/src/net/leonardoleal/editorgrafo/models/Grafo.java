package net.leonardoleal.editorgrafo.models;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Grafo {
	private ArrayList<Vertice> vertices = new ArrayList<>();
	private ArrayList<Aresta> arestas = new ArrayList<>();
	
	private Pane pane;
	private Line line;
	
	private Vertice linkingVertice = null;
	private ArrayList<GrafoObserver> observers = new ArrayList<GrafoObserver>();
	
	public Grafo(Pane pane) { 
		this.pane = pane;
		
		line = new Line();
		line.setStroke(Color.LIGHTGRAY);
		line.setVisible(false);
		pane.getChildren().add(line);
	} 
	
	public Pane getPane() {
		return pane;
	}
	
	public Vertice addVertice() {
		Vertice vertice = new Vertice(this);
		vertices.add(vertice);
		
		notifyVerticeAddeded(vertice);
		return vertice;
	}
	
	public Aresta addAresta(Vertice verticeA, Vertice verticeB) {
		if(verticeA == null || verticeB == null) 
			return null;
		
		Aresta aresta = new Aresta(this, verticeA, verticeB);
		verticeA.getArestas().add(aresta);
		if(verticeA != verticeB)
			verticeB.getArestas().add(aresta);
		arestas.add(aresta);
		
		notifyArestaAddeded(aresta);
		return aresta;
	}
	
	public boolean removeVertice(Vertice vertice) {
		if(vertice == null || !vertices.contains(vertice)) 
			return false;
		
		for(Aresta aresta : vertice.getArestas()) 
			removeAresta(aresta);
		vertices.remove(vertice);
		
		notifyVerticeRemoved(vertice);
		return true;
	}
	
	public boolean removeAresta(Aresta aresta) {
		if(aresta == null || !arestas.contains(aresta)) 
			return false;
		
		aresta.getVerticeA().getArestas().remove(aresta);
		if(aresta.getVerticeA() != aresta.getVerticeB())
			aresta.getVerticeB().getArestas().remove(aresta);
		arestas.remove(aresta);
		
		notifyArestaRemoved(aresta);
		return true;
	}
	
	public String findAvaliableName() {
		ArrayList<String> usedNames = new ArrayList<>();
		for(Vertice vertice : vertices)
			usedNames.add(vertice.getName());
		
		for(char c = 'A'; c <= 'Z'; c++) 
			if(!usedNames.contains(String.valueOf(c)))
				return String.valueOf(c);
		for(char c = '0'; c <= '9'; c++) 
			if(!usedNames.contains(String.valueOf(c)))
				return String.valueOf(c);
		for(char c = 'a'; c <= 'z'; c++) 
			if(!usedNames.contains(String.valueOf(c)))
				return String.valueOf(c);
		
		String[] faces = new String[] { " ͡° ͜ʖ ͡°", "ಠ_ಠ", "◕‿◕", "ಠ益ಠ", "•ᴥ•", "⚆ _⚆", "◔ϖ◔", "⌐■_■", "◔̯◔", };
		for(int i = 0; i < faces.length; i++) 
			if(!usedNames.contains(faces[i]))
				return faces[i];
		
		return ";-;";
	}
	
	public boolean areConnected(Vertice verticeA, Vertice verticeB) {
		if(verticeA.getGrafo() != verticeB.getGrafo())
			return false;
		
		for(Aresta aresta : verticeA.getArestas()) {
			if(aresta.getDirection() == ArestaDirection.ONE_DIRECTION) {
				if(aresta.getVerticeA() == verticeA && aresta.getVerticeB() == verticeB)
					return true;
			}
			else {
				if((aresta.getVerticeA() == verticeA && aresta.getVerticeB() == verticeB)
				|| (aresta.getVerticeA() == verticeB && aresta.getVerticeB() == verticeA))
					return true;
			}
		}
		return false;
	}
	
	public Vertice findVerticeAt(Point2D location) {
		if(vertices.size() == 0)
			return null;
		
		int nearestId = 0;
		double distance = Double.MAX_VALUE;
		double aux;
		for(int i = 0; i < vertices.size(); i++) {
			if((aux = vertices.get(i).getLocation().distance(location)) < distance) {
				nearestId = i;
				distance = aux;
			}
		}
		return distance < Vertice.RADIUS ? vertices.get(nearestId) : null;
	}
	
	public boolean isLinkingVertices() {
		return linkingVertice != null;
	}
	
	public void beginLinkVertices(Vertice vertice) {
		linkingVertice = vertice;
		line.setStartX(vertice.getLocation().getX());
		line.setStartY(vertice.getLocation().getY());
		line.setEndX(vertice.getLocation().getX());
		line.setEndY(vertice.getLocation().getY());
		line.setVisible(true);
	}
	
	public void linkingVerticeMouseMoved(Point2D location) {
		line.setEndX(location.getX());
		line.setEndY(location.getY());
	}
	
	public void endLinkVertices(Point2D location) {
		Vertice vertice = findVerticeAt(location);
		if(vertice != null && !areConnected(linkingVertice, vertice)) 
			addAresta(linkingVertice, vertice);
		linkingVertice = null;
		line.setVisible(false);
	}
	
	public void subscriveObserver(GrafoObserver observer) {
		observers.add(observer);
	}
	
	public boolean unsubscribeObserver(GrafoObserver observer) {
		return observers.remove(observer);
	}
	
	private void notifyVerticeAddeded(Vertice vertice) {
		for(GrafoObserver observer : observers)
			observer.verticeAddeded(vertice);
	}
	
	private void notifyVerticeRemoved(Vertice vertice) {
		for(GrafoObserver observer : observers)
			observer.verticeRemoved(vertice);
	}
	
	private void notifyArestaAddeded(Aresta aresta) {
		for(GrafoObserver observer : observers)
			observer.arestaAddeded(aresta);
	}
	
	private void notifyArestaRemoved(Aresta aresta) {
		for(GrafoObserver observer : observers)
			observer.arestaRemoved(aresta);
	}
}
