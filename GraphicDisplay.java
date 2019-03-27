package game;

import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.paint.Color ;
import javafx.geometry.*;

import java.util.ArrayList;
import java.util.List;

public class GraphicDisplay extends Application {
		  

		public void start(Stage stage) {
			double largeur = 697;
			double hauteur = 520;
			Color[] liste_colors = {Color.BLUE,Color.RED,Color.GREEN,Color.PURPLE,Color.YELLOW,Color.ORANGE,Color.PINK,Color.LIGHTGREEN};
			stage.setTitle("DiceWars");
			stage.setAlwaysOnTop(true);
			javafx.application.Platform.setImplicitExit(false);
		    Group group = new Group();
		    Scene scene = new Scene(group);
		    stage.setScene(scene);
		    Canvas canvas = new Canvas(largeur,hauteur);
		    group.getChildren().add(canvas);
		    stage.setResizable(false);
		    GraphicsContext gc = canvas.getGraphicsContext2D();
	        Territory[][] displayTerritories = Game.plateau.territories;
	        List<Sprite> rectangles = new ArrayList<Sprite>();
	        //Fin initialisation
	        for(int x=0;x<displayTerritories.length;x++) {
	        	for(int y=0;y<displayTerritories[0].length;y++) {
	        		Sprite tempsprite = new Sprite(50*x,50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
	        		rectangles.add(tempsprite);
	        		tempsprite.render(gc);
	        	}
	        }
	        //Affichage standard
		    stage.show();
		}
		
		


}


