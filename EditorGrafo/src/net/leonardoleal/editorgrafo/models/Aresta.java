package net.leonardoleal.editorgrafo.models;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class Aresta {
	static final double ARROW_SIZE = 10;
	static final double ARROW_SIZE2 = 10 * .5;
	
	private Vertice verticeA;
	private Vertice verticeB;
	private Grafo grafo;
	private double weight = Double.NaN;
	private ArestaDirection direction = ArestaDirection.NO_DIRECTION;
	
	private Line line;
	private Polygon arrowA;
	private Polygon arrowB;
	
	private Label weightLabel = null;
	
	public Aresta(Grafo grafo, Vertice verticeA, Vertice verticeB) {
		this.grafo = grafo;
		this.verticeA = verticeA;
		this.verticeB = verticeB;
		
		line = new Line();
		grafo.getPane().getChildren().add(0, line);
		
		updateAresta();
	}

	public Vertice getVerticeA() {
		return verticeA;
	}

	public Vertice getVerticeB() {
		return verticeB;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
		
		if(!Double.isNaN(weight)) {
			if(weightLabel == null) {
				weightLabel = new Label(String.valueOf(weight));
				weightLabel.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
				grafo.getPane().getChildren().add(weightLabel);
			}
			else {
				weightLabel.setText(String.valueOf(weight));
			}
		}
		else {
			if(weightLabel != null) {
				grafo.getPane().getChildren().remove(weightLabel);
				weightLabel = null;
			}
		}
		
		updateAresta();
	}

	public ArestaDirection getDirection() {
		return direction;
	}

	public void setDirection(ArestaDirection direction) {
		this.direction = direction;
		if(direction == ArestaDirection.ONE_DIRECTION || direction == ArestaDirection.BOTH_DIRECTIONS) {
			if(arrowA == null) { 
				arrowA = new Polygon();
				grafo.getPane().getChildren().add(arrowA); 
			}
		}
		else {
			if(arrowA != null) {
				grafo.getPane().getChildren().remove(arrowA);
				arrowA = null;
			}
		}
		
		if(direction == ArestaDirection.BOTH_DIRECTIONS) {
			if(arrowB == null) {
				arrowB = new Polygon();
				grafo.getPane().getChildren().add(arrowB);
			}
		}
		else {
			if(arrowB != null) {
				grafo.getPane().getChildren().remove(arrowB);
				arrowB = null;
			}
		}
		
		updateAresta();
	}

	public Grafo getGrafo() {
		return grafo;
	}
	
	public void updateAresta() {
		
		if(verticeA == verticeB) {
			line.setStartX(verticeA.getLocation().getX());
			line.setStartY(verticeA.getLocation().getY());
			line.setEndX(verticeB.getLocation().getX() + (Vertice.RADIUS * 1.25));
			line.setEndY(verticeB.getLocation().getY() - (Vertice.RADIUS * 1.25));
			return;
		}
		
		double xOffset = 0;
		double yOffset = 0;
		
		if(direction == ArestaDirection.ONE_DIRECTION || direction == ArestaDirection.BOTH_DIRECTIONS) {
			double angle = 
					Math.atan2(
						verticeB.getLocation().getY() - verticeA.getLocation().getY(), 
						verticeB.getLocation().getX() - verticeA.getLocation().getX());
			
			if(direction == ArestaDirection.ONE_DIRECTION) {
				xOffset = -5 * Math.sin(angle);
				yOffset = 5 * Math.cos(angle);
			}
			
			double edgeX = -Vertice.RADIUS * Math.cos(angle) - 0 * Math.sin(angle);
			double edgeY = -Vertice.RADIUS * Math.sin(angle) + 0 * Math.cos(angle);
			double sideAX = -(Vertice.RADIUS + ARROW_SIZE) * Math.cos(angle) - ARROW_SIZE2 * Math.sin(angle);
			double sideAY = -(Vertice.RADIUS + ARROW_SIZE) * Math.sin(angle) + ARROW_SIZE2 * Math.cos(angle);
			double sideBX = -(Vertice.RADIUS + ARROW_SIZE) * Math.cos(angle) + ARROW_SIZE2 * Math.sin(angle);
			double sideBY = -(Vertice.RADIUS + ARROW_SIZE) * Math.sin(angle) - ARROW_SIZE2 * Math.cos(angle);

			arrowA.setLayoutX(verticeB.getLocation().getX() + xOffset);
			arrowA.setLayoutY(verticeB.getLocation().getY() + yOffset);
			arrowA.getPoints().clear();
			arrowA.getPoints().addAll(new Double[] { edgeX, edgeY, sideAX, sideAY, sideBX, sideBY });
			
			if(direction == ArestaDirection.BOTH_DIRECTIONS) {
				arrowB.setLayoutX(verticeA.getLocation().getX() + xOffset);
				arrowB.setLayoutY(verticeA.getLocation().getY() + yOffset);
				arrowB.getPoints().clear();
				arrowB.getPoints().addAll(new Double[] { -edgeX, -edgeY, -sideAX, -sideAY, -sideBX, -sideBY });
			}
		}
		
		
		if(!Double.isNaN(weight)) {
			double halfX = (verticeA.getLocation().getX() + verticeB.getLocation().getX() - weightLabel.getWidth()) * .5; 
			double halfY = (verticeA.getLocation().getY() + verticeB.getLocation().getY() - weightLabel.getHeight() ) * .5; 
			
			weightLabel.setLayoutX(halfX + (xOffset * 3.5));
			weightLabel.setLayoutY(halfY + (yOffset * 3.5));
		}
		
		line.setStartX(verticeA.getLocation().getX() + xOffset);
		line.setStartY(verticeA.getLocation().getY() + yOffset);
		line.setEndX(verticeB.getLocation().getX() + xOffset);
		line.setEndY(verticeB.getLocation().getY() + yOffset);
	}
}
