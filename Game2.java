package game;

import javafx.application.Application;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
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
public class Game2 extends Application {
	public static Board plateau;
	Player[] gamers;
	int nb_round=5;
	int xd,yd,ya,xa;
	
	  public static void main(String[] args) {
		    launch(args);
		  }

		
	public void start(Stage stage) throws IOException {
		this.plateau = new Board(2,2, 2,8,5);
		this.gamers=this.plateau.gamers;
		this.nb_round = nb_round;
		plateau = this.plateau;
		gamers = this.gamers;
		nb_round = this.nb_round;
		
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
		Scanner un = new Scanner(System.in);
		this.plateau = new Board(2,2, 2,8,5);
		this.gamers=this.plateau.gamers;
		this.nb_round = nb_round;
        TextField textField = new TextField();
        Button button = new Button("Cliquez!");
        button.setOnAction(action -> {
            //System.out.println(textField.getText());
        });
        HBox hbox = new HBox(textField,button);
        group.getChildren().add(hbox);
        //Fin ajout
        Territory[][] displayTerritories = Game.plateau.territories;
        List<Sprite> rectangles = new ArrayList<Sprite>();
        for(int x=0;x<displayTerritories.length;x++) {
        	for(int y=0;y<displayTerritories[0].length;y++) {
        		Sprite tempsprite = new Sprite(50*x,50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
        		rectangles.add(tempsprite);
        		tempsprite.render(gc);
        	}
        }
        stage.show();

        Service<Void> AffichageEtEntrees = new Service<Void>() {

			@Override
			
			public Task<Void> createTask() {
				// TODO Auto-generated method stub
				return new Task<Void>() {

					
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						for (int j = 0; j < gamers.length; j++) {
							//gestion du tour du joueur j
							final int z = j;
							if(!plateau.has_winner()) {
								do {
									Platform.runLater(new Runnable() {
										@Override
										public  void run() {
											// TODO Auto-generated method stub
											System.out.println( "\n" + "Joueur " + (z+1) + " : phase d'attaque");
											System.out.println(plateau);
											System.out.println("Joueur " + (z+1));
											System.out.println("(format d'input : x y )");
											System.out.print("Territoire de depart :");
											xd = un.nextInt(); yd = un.nextInt()	;
											System.out.print("Territoire cible : ");
											xa = un.nextInt()	; ya = un.nextInt();
									        List<Sprite> rectangles = new ArrayList<Sprite>();
									        for(int x=0;x<displayTerritories.length;x++) {
									        	for(int y=0;y<displayTerritories[0].length;y++) {
									        		Sprite tempsprite = new Sprite(50*x,50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
									        		rectangles.add(tempsprite);
									        		tempsprite.render(gc);
									        	}
									        }
											stage.show();
										}
										
									});
								} while( ! gamers[j].canFight(plateau.territories[yd][xd], plateau.territories[ya][xa]));	
								gamers[j].fightGlobal(plateau.territories[yd][xd], plateau.territories[ya][xa]);
							}
						}
						return null;
					}
					
				};
			}
        	
        };

        final AnimationTimer animation = new AnimationTimer() {

			@Override
			public void handle(long arg0) {
				// TODO Auto-generated method stub
				AffichageEtEntrees.start();
			}
        	
        };

    
	}
	
	
}

