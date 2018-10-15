package net.leonardoleal.editorgrafo.models;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Vertice {
	public final static double RADIUS = 12.5;
	public final static double DIAMETER = RADIUS * 2;
	final static double INNER_CIRCLE_OFFSET = 1; 
	final static double INNER_RADIUS = RADIUS - INNER_CIRCLE_OFFSET;
	
	private ArrayList<Aresta> arestas = new ArrayList<>();
	private String name = "~";
	private Point2D location = new Point2D(0, 0);
	private Grafo grafo;
	private Color color = Color.WHITE;

	private Circle circle;
	private Circle innerCircle;
	private Label label;
	
	private boolean dragging = false;
	private Point2D offset;
	
	public Vertice(Grafo grafo) {
		this.grafo = grafo;
		
		circle = new Circle(RADIUS, RADIUS, RADIUS);
		innerCircle = new Circle(INNER_RADIUS, INNER_RADIUS, INNER_RADIUS);
		innerCircle.setMouseTransparent(true);
		label = new Label(name);
		label.setPrefSize(DIAMETER * 2, DIAMETER);
		label.setAlignment(Pos.CENTER);
		label.setWrapText(false);
		label.setMouseTransparent(true);
		
		circle.setOnMousePressed(e -> {
			if(e.getButton() == MouseButton.PRIMARY) {
				offset = circle.sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY())).subtract(RADIUS, RADIUS);
				dragging = true;
			}
		});
		
		circle.setOnMouseDragged(e -> {
			if(e.isPrimaryButtonDown() && dragging) {
				Point2D mousePosition = grafo.getPane().sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY()));
				double x = mousePosition.getX() - offset.getX();
				double y = mousePosition.getY() - offset.getY();
				
				if(x < RADIUS)
					x = RADIUS;
				else if(x > grafo.getPane().getWidth() - RADIUS)
					x = grafo.getPane().getWidth() - RADIUS;
				
				if(y < RADIUS)
					y = RADIUS;
				else if(y > grafo.getPane().getHeight() - RADIUS)
					y = grafo.getPane().getHeight() - RADIUS;
				
				setLocation(x, y);
			}
			if(e.isSecondaryButtonDown()) {
				Point2D mousePosition = grafo.getPane().sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY()));
				grafo.linkingVerticeMouseMoved(new Point2D(mousePosition.getX(), mousePosition.getY()));
			}
		});
		
		circle.setOnMouseReleased(e -> {
			if(e.getButton() == MouseButton.PRIMARY && dragging) 
				dragging = false;
			else if(e.getButton() == MouseButton.SECONDARY && grafo.isLinkingVertices()) {
				Point2D mousePosition = grafo.getPane().sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY()));
				grafo.endLinkVertices(new Point2D(mousePosition.getX(), mousePosition.getY()));
			}
		});
		
		circle.setOnDragDetected(e -> {
			if(e.isSecondaryButtonDown()) 
				grafo.beginLinkVertices(this);
		});
		
		setLocation(0, 0);
		setColor(color);
		
		grafo.getPane().getChildren().add(circle);
		grafo.getPane().getChildren().add(innerCircle);
		grafo.getPane().getChildren().add(label);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		
		label.setText(name);
	}

	public Point2D getLocation() {
		return location;
	}

	public void setLocation(double x, double y) {
		setLocation(new Point2D(x, y));
	}
	public void setLocation(Point2D location) {
		this.location = location;
		
		circle.setLayoutX(location.getX() - RADIUS);
		circle.setLayoutY(location.getY() - RADIUS);
		innerCircle.setLayoutX(location.getX() - RADIUS + INNER_CIRCLE_OFFSET);
		innerCircle.setLayoutY(location.getY() - RADIUS + INNER_CIRCLE_OFFSET);
		label.setLayoutX(location.getX() - DIAMETER);
		label.setLayoutY(location.getY() - RADIUS);
		
		for(Aresta aresta : arestas) 
			aresta.updateAresta();
	}

	public ArrayList<Aresta> getArestas() {
		return arestas;
	}

	public Grafo getGrafo() {
		return grafo;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
		
		innerCircle.setFill(color);
		if(getLuminance(color) < 0.5) {
			label.setTextFill(Color.WHITE);
			circle.setFill(Color.GRAY);
		}
		else {
			label.setTextFill(Color.BLACK);
			circle.setFill(Color.BLACK);
		}
	}
	
    private static float getLuminance(Color color) {
        return (0.299F * (int)(color.getRed() * 255) + 0.587F * (int)(color.getGreen() * 255) + 0.114F * (int)(color.getBlue() * 255)) / 255;
    }
}
