package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Game extends Application {
	public static Board plateau;
	Player[] gamers;
	int nb_round=5;
	Color[] liste_colors = {Color.BLUE,Color.RED,Color.GREEN,Color.PURPLE,Color.YELLOW,Color.ORANGE,Color.PINK,Color.LIGHTGREEN};
	// Displaying UI
	  public static void main(String[] args) {
		    launch(args);
		  }
	  
	  
	public ReturnClass initialize(Stage stage) {
		// Initiliazing the board, with its players 
		this.plateau = new Board(2,2, 2,8,5);
		this.gamers=this.plateau.gamers;
		this.nb_round = nb_round;
		// Basic UI parameters : window's height/width, title, and javafx's wrappers for handling scenes
		double largeur = 697;
		double hauteur = 520;
		stage.setTitle("DiceWars");
		stage.setAlwaysOnTop(true);
	    Group group = new Group();
	    Scene scene = new Scene(group);
	    stage.setScene(scene);
	    Canvas canvas = new Canvas(largeur,hauteur);
	    group.getChildren().add(canvas);
	    stage.setResizable(false);
	    GraphicsContext gc = canvas.getGraphicsContext2D();
        // Text to interact with the background tasks
	    final Text text = new Text();    	    
	    text.setY(120); 		
	    group.getChildren().add(text);
        TextField textField = new TextField();
        // Button to catch interactions when asked who to attack
        Button button = new Button("Valider");
        button.setOnAction(action -> {
            String answer = textField.getText();
            //text.setText(answer);
            textField.setText("");
        });
        HBox hbox = new HBox(textField,button);

        hbox.setLayoutY(140);         
        group.getChildren().add(hbox);
        // Initializing territories' display
        Territory[][] displayTerritories = Game.plateau.territories;
        List<Sprite> rectangles = new ArrayList<Sprite>();
        for(int x=0;x<displayTerritories.length;x++) {
        	for(int y=0;y<displayTerritories[0].length;y++) {
        		Sprite tempsprite = new Sprite(50*x,50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
        		rectangles.add(tempsprite);
        		tempsprite.render(gc);
        	}
        }
        // Wrapper of all data needed for further procedures
        ReturnClass returnClass = new ReturnClass(text,button,textField,gc,stage);
		return returnClass;
	}
	
	public class ReturnClass{
		Text text;
		Button button;
		TextField textField;
		GraphicsContext gc;
		Stage stage;
		
		ReturnClass(Text text, Button button,TextField textField, GraphicsContext gc, Stage stage){
			this.text =text;
			this.button = button;
			this.textField = textField;
			this.gc = gc;
			this.stage =stage;
		}
	}
	
	// Thread that will look whether conditions are set to play a turn
	public class TaskDisplay extends Task{
		Text text;
		int[] xy = {0,0,0,0};
		Button button;
		TextField textField;
		GraphicsContext gc;
		Stage stage;
		int k; //Counting the inputs for attack/defense
		TaskDisplay(Text text, Button button,TextField textField, GraphicsContext gc, Stage stage){
			this.text = text;
			this.button = button;
			this.textField = textField;
			this.gc = gc;
			this.stage = stage;
		}
		@Override
		protected Object call() throws Exception {
			String str = "";
			Platform.runLater(new Runnable() {
				@Override
				public  void run() {
					
					// TODO Auto-generated method stub
					button.setOnAction(action -> {
						String answer = textField.getText();
						System.out.println(answer);
			            xy[k] = Integer.parseInt(answer);
			            text.setText(answer);
			            textField.setText("");
			            k +=1;
						if (k==2) {
							System.out.print("Territoire cible : ");
						}
						else if (k ==3){
						}
			        });
				}
				
			});
			while (nb_round  > 0 && !plateau.has_winner()) {
				for (int j = 0; j < gamers.length; j++) {
					//gestion du tour du joueur j
					final int z = j;
					if(!plateau.has_winner()) {
					
							String toPrint = "";
							toPrint +=  "\n" + "Joueur " + (z+1) + " : phase d'attaque";
							toPrint += "\n";
							toPrint += plateau;
							toPrint += "\n";
							toPrint += "Joueur " + (z+1);
							toPrint += "\n";
							toPrint += "(format d'input : x y )";
							toPrint += "\n";
							toPrint += "Territoire de depart :";
							//System.out.println(toPrint);		
							
						if (k==3) {
							if (gamers[z].canFight(plateau.territories[xy[1]][xy[0]], plateau.territories[xy[3]][xy[2]])) {
								gamers[z].fightGlobal(plateau.territories[xy[1]][xy[0]], plateau.territories[xy[3]][xy[2]]);
								Platform.runLater(new Runnable() {

									@Override
									public void run() {
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
									}
									
								});
							}
							else {
								k = 0;
							}

						}
				
					}
				}
			};

			return null;
		}
		
	}
	@Override
	public void start(Stage stage) throws Exception {
		ReturnClass returnClass = initialize(stage);
		TaskDisplay task = new TaskDisplay(returnClass.text,returnClass.button,returnClass.textField,returnClass.gc,returnClass.stage);
		new Thread(task).start();
		stage.show();
	}
}