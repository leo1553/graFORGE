package net.leonardoleal.editorgrafo.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import net.leonardoleal.editorgrafo.models.Aresta;
import net.leonardoleal.editorgrafo.models.ArestaDirection;
import net.leonardoleal.editorgrafo.models.Grafo;
import net.leonardoleal.editorgrafo.models.GrafoObserver;
import net.leonardoleal.editorgrafo.models.Vertice;

public class MainSceneController implements Initializable, GrafoObserver {
	@FXML private Pane mainPane;
	@FXML private ComboBox<String> arestaComboBox;
	
	Grafo grafo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		grafo = new Grafo(mainPane);
		grafo.subscriveObserver(this);
		
		arestaComboBox.getItems().addAll(new String[] {
			"A --- B",
			"A --> B",
			"A <-> B"
		});
		arestaComboBox.setValue(arestaComboBox.getItems().get(0));
		
		mainPane.setOnMouseClicked(e -> {
			if(e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2) {
				Vertice vertice = grafo.addVertice();
				vertice.setName(grafo.findAvaliableName());
				
				Point2D mousePosition = mainPane.sceneToLocal(new Point2D(e.getSceneX(), e.getSceneY()));
				double x = mousePosition.getX();
				double y = mousePosition.getY();
				
				if(x < Vertice.RADIUS)
					x = Vertice.RADIUS;
				else if(x > grafo.getPane().getWidth() - Vertice.RADIUS)
					x = grafo.getPane().getWidth() - Vertice.RADIUS;
				
				if(y < Vertice.RADIUS)
					y = Vertice.RADIUS;
				else if(y > grafo.getPane().getHeight() - Vertice.RADIUS)
					y = grafo.getPane().getHeight() - Vertice.RADIUS;
				
				vertice.setLocation(x, y);
			}
		});
		
		
	}

	@Override
	public void verticeAddeded(Vertice vertice) { }

	@Override
	public void verticeRemoved(Vertice vertice) { }

	@Override
	public void arestaAddeded(Aresta aresta) {
		ArestaDirection direction;
		switch(arestaComboBox.getItems().indexOf(arestaComboBox.getValue())) {
			case 1:
				direction = ArestaDirection.ONE_DIRECTION;
				break;
			case 2:
				direction = ArestaDirection.BOTH_DIRECTIONS;
				break;
			default:
				direction = ArestaDirection.NO_DIRECTION;
				break;
		}
		/*switch(arestaComboBox.getValue()) {
			case "A <-> B":
				direction = ArestaDirection.BOTH_DIRECTIONS;
				break;
			case "A --> B":
				direction = ArestaDirection.ONE_DIRECTION;
				break;
			default:
				direction = ArestaDirection.NO_DIRECTION;
				break;
		}*/
		
		aresta.setDirection(direction);
	}

	@Override
	public void arestaRemoved(Aresta aresta) { }
}
