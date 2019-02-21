package game;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.*;
import javafx.animation.*;

import java.io.IOException;
import java.util.*;


import javafx.event.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import 	javafx.scene.Scene.*;
import javafx.application.*;
public class Game extends Application {
	public static Board plateau;
	Player[] gamers;
	int nb_round=5;
	
	  public static void main(String[] args) {
		    launch(args);
		  }

	/**public Game(int colonnes, int lignes, int gamers_number, int N, int max_dices, int nb_round) {
		// TODO Auto-generated constructor stub
		System.out.println("test");
		this.plateau = new Board(colonnes,lignes, gamers_number, N, max_dices);
		this.gamers=this.plateau.gamers;
		this.nb_round = nb_round;
		}
*/
	public void runGame() {
		Scanner un = new Scanner(System.in);
		this.plateau = new Board(2,2, 2,8,5);
		this.gamers=this.plateau.gamers;
		this.nb_round = nb_round;
		while (this.nb_round > 0 && !plateau.has_winner() ) {
			for (int j = 0; j < gamers.length; j++) {
				//gestion du tour du joueur j
				if(!plateau.has_winner()) {
					int xd; int yd; int xa; int ya;
					System.out.println( "\n" + "Joueur " + (j+1) + " : phase d'attaque");
					System.out.println(plateau);
					do {
						System.out.println("Joueur " + (j+1));
						System.out.println("(format d'input : x y )");
						System.out.print("Territoire de depart :");
						xd = un.nextInt(); yd = un.nextInt()	;
						System.out.print("Territoire cible : ");
						xa = un.nextInt()	; ya = un.nextInt();
					} while( ! gamers[j].canFight(plateau.territories[yd][xd], plateau.territories[ya][xa]));	
					gamers[j].fightGlobal(plateau.territories[yd][xd], plateau.territories[ya][xa]);
				}
			}
			this.plateau.update();
			System.out.println("Fin du round, gain de des");
			this.nb_round -- ;
		}
		System.out.println("fin du jeu");
		un.close();
	}
		
	public void start(Stage stage) throws IOException {
		runGame();
		double largeur = 697;
		double hauteur = 520;
		Color[] liste_colors = {Color.BLUE,Color.RED,Color.GREEN,Color.PURPLE,Color.YELLOW,Color.ORANGE,Color.PINK,Color.LIGHTGREEN};
		stage.setTitle("DiceWars");
		stage.setAlwaysOnTop(true);
	    Group group = new Group();
	    Scene scene = new Scene(group);
	    stage.setScene(scene);
	    Canvas canvas = new Canvas(largeur,hauteur);
	    group.getChildren().add(canvas);
	    stage.setResizable(false);
	    GraphicsContext gc = canvas.getGraphicsContext2D();
        System.out.println(Game.plateau.toString());
        //Test
        TextField textField = new TextField();
        Button button = new Button("Cliquez!");
        button.setOnAction(action -> {
            System.out.println(textField.getText());
        });
        HBox hbox = new HBox(textField,button);
        group.getChildren().add(hbox);
        Territory[][] displayTerritories = Game.plateau.territories;
        List<Sprite> rectangles = new ArrayList<Sprite>();
        //Fin initialisation
        AnimationTimer animation = new AnimationTimer() {
    		public void handle(long arg0) {
    	        for(int x=0;x<displayTerritories.length;x++) {
    	        	for(int y=0;y<displayTerritories[0].length;y++) {
    	        		Sprite tempsprite = new Sprite(50*x,50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
    	        		rectangles.add(tempsprite);
    	        		tempsprite.render(gc);
    	        	}
    	        }
    		    stage.show();
    		}
        };
	    animation.start();

	}
	
	
}
