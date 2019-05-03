package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Node;
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
	static Player[] gamers;
	Group group = new Group();
	int idjoueur=1;
	static int nb_round=5;
	Color[] liste_colors = {Color.BLUE,Color.RED,Color.GREEN,Color.PURPLE,Color.YELLOW,Color.ORANGE,Color.PINK,Color.LIGHTGREEN};
	String[] liste_colors_string = {"bleu","rouge","vert","violet","jaune","orange","rose","vert clair"};
	static Canvas canvas;
	static IA ia;
	static IA iabis;
	static IA iatris;
	// Displaying UI
	  public static void main(String[] args) {
		  	
		    launch(args);
		  }
	  

	  
	  
	public ReturnClass initialize(Stage stage) {
		// Initiliazing the board, with its players 
		int dices_max = 5;
		double[][] probas_matrix = Probas.ProbaMatrix(dices_max) ;
		Game.plateau = new Board(7,2,2,20,dices_max);
		Game.gamers=Game.plateau.gamers;
		Game.nb_round = 5;
		Game.ia = new IA(0,0);
		Game.iabis = new IA(1,1);  // définition à revoir pour bis,qui peut grâce à la suite jouer en 1 er
		Game.iatris = new IA(1,2);
		// Basic UI parameters : window's height/width, title, and javafx's wrappers for handling scenes
		double largeur = 997;
		double hauteur = 720;
		stage.setTitle("DiceWars");
		stage.setAlwaysOnTop(true);
	    Scene scene = new Scene(group);
	    stage.setScene(scene);
	    Game.canvas = new Canvas(largeur,hauteur);
	    group.getChildren().add(canvas);
	    stage.setResizable(false);
	    GraphicsContext gc = canvas.getGraphicsContext2D();
        // Text to interact with the background tasks
	    final Text text = new Text();    	    
	    text.setY(310); 	
	    text.setX(275);
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
        hbox.setLayoutY(530);  
        hbox.setLayoutX(275);
        group.getChildren().add(hbox);
        // Initializing territories' display
        Territory[][] displayTerritories = Game.plateau.territories;
        List<Sprite> rectangles = new ArrayList<Sprite>();
        List<Text> nbdices = new ArrayList<Text>();
        for(int y=0;y<displayTerritories.length;y++) {
        	for(int x=0;x<displayTerritories[0].length;x++) {
        		// Displaying territories
        		Sprite tempsprite = new Sprite(300+50*x,200+50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
        		rectangles.add(tempsprite);
        		tempsprite.render(gc);
        		// Displaying the number of dices for each territory
        		Text temptext = new Text(325+50*x,230+50*y,Integer.toString(displayTerritories[y][x].dices));
        		// Displaying who's the turn of 
        		Text idjoueurtext = new Text(300,190,"Tour du joueur "+this.idjoueur + "(" + liste_colors_string[this.idjoueur] +")");
        		group.getChildren().add(idjoueurtext);
        		group.getChildren().add(temptext);
        	}
        }
        // Wrapper of all data needed for further procedures
        ReturnClass returnClass = new ReturnClass(probas_matrix, text,button,textField,gc,stage);
		return returnClass;
	}
	

	public class ReturnClass{
		Text text;
		Button button;
		TextField textField;
		GraphicsContext gc;
		Stage stage;
		double[][] probas_matrix;
		// Standard initialization
		ReturnClass(double[][] probas_matrix,Text text, Button button,TextField textField, GraphicsContext gc, Stage stage){
			this.text =text;
			this.button = button;
			this.textField = textField;
			this.gc = gc;
			this.stage =stage;
			this.probas_matrix = probas_matrix;
		}
	}
	
	// Main Thread for updating Board UI
	public class TaskDisplay extends Task{
		Text text;
		int[] xy = {0,0,0,0};
		Button button;
		TextField textField;
		GraphicsContext gc;
		Stage stage;
		int k; //Counting the inputs for attack/defense
		double[][] probas_matrix;
		// Standard initialization with arguments that will be 
		TaskDisplay(double[][] probas_matrix, Text text, Button button,TextField textField, GraphicsContext gc, Stage stage){
			this.text = text;
			this.button = button;
			this.textField = textField;
			this.gc = gc;
			this.stage = stage;
			this.probas_matrix = probas_matrix;
			
		}
		@Override
		// Method called at every UI update
		protected Object call() throws Exception {
			button.setOnAction(action -> {
				// If a player's turn has just been played, we reinitialize the counter of inputs
				if (k==4) {
					k = 0;
				}
				String answer = textField.getText();
				System.out.println(answer);
	            xy[k] = Integer.parseInt(answer);
	            text.setText(answer);
	            textField.setText("");
	            k +=1;
	            // Two first inputs = territory from where one attack
				if (k==2) {
					System.out.print("Territoire cible : ");
				}
			
        });
			while (nb_round  > 0 && !plateau.has_winner()) {
				idjoueur = 0;
				while (idjoueur < gamers.length) {
					//gestion du tour du joueur j
					//System.out.println("idjoueur :" + idjoueur);
					if(!plateau.has_winner()&& plateau.hasAttack(gamers[idjoueur])) {
	
							String toPrint = "";
							toPrint +=  "\n" + "Joueur " + (idjoueur+1) + " : phase d'attaque";
							toPrint += "\n";
							
							//System.out.println(toPrint); // Yet to be added to UI (in a text area)
						// When all inputs have been set by one player
						if(idjoueur==0) { // Here we call our IA; We are supposing for now that IA is player 0 
							System.out.println("TOUR DE IA basique");
							ia.play( probas_matrix, this.textField,this.button,Game.plateau,this.gc,this.stage,this); // Purpose : fill the territory to attack
						}
////						
						//Random IA  : transformer le 0 en 1 et le 1 en 2  pour faire jouer l'ia random en 2 eme
//					    if(idjoueur==0) { // Here we call our random IA; We are supposing for now that IA is player 0 
//							int indice = 0;
//					    	System.out.println("TOUR DE IA random");
//							 if (plateau.hasAttack(plateau.gamers[indice])) {
//								 iabis.playrandom(1 + indice, probas_matrix, this.textField,this.button,Game.plateau,this.gc,this.stage,this); // Purpose : fill the territory to attack
//							 }
//						}
//					    
					    if(idjoueur==1) { //supposition IA evolved is player 0 
							System.out.println("TOUR DE IA evolved");
							 if (plateau.hasAttack(plateau.gamers[1])) {
								 iatris.playevolved(probas_matrix, 3, this.textField,this.button,Game.plateau,this.gc,this.stage,this); // Purpose : fill the territory to attack
							 }
						}
//						
						
//						
						
						if (k==4) {
							if (gamers[idjoueur].canFight(plateau.territories[xy[1]][xy[0]], plateau.territories[xy[3]][xy[2]])) {
								gamers[idjoueur].fightGlobal(plateau.territories[xy[1]][xy[0]], plateau.territories[xy[3]][xy[2]]);
							       idjoueur += 1;
								Platform.runLater(new Runnable() {
									@Override
									// UI update done by run Method
									public void run() {
								        Territory[][] displayTerritories = Game.plateau.territories;
								        List<Sprite> rectangles = new ArrayList<Sprite>();
								        // Deleting previous text boxes (prevent writing over previous dices numbers...)
						        		 for(Iterator<Node> it=group.getChildren().iterator(); it.hasNext();) {
						        			 if(it.next() instanceof Text) {
						        				it.remove();
						        			 }
						        		}
						        		// Displaying who's the turn of 
						        		Text idjoueurtext = new Text(300,190,"Tour du joueur "+(idjoueur+1) + "(" + liste_colors_string[(idjoueur+1)] +")");
						        		group.getChildren().add(idjoueurtext);
						        		 

						        		 for(int x=0;x<displayTerritories[0].length;x++) {
								        	for(int y=0;y<displayTerritories.length;y++) {
								        		Sprite tempsprite = new Sprite(300+50*x,200+50*y,50,50,liste_colors[displayTerritories[y][x].player.id]);
								        		rectangles.add(tempsprite);
								        		tempsprite.render(gc);
								        		// Displaying the number of dices for each territory
								        		Text temptext = new Text(325+50*x,230+50*y,Integer.toString(displayTerritories[y][x].dices));
								        		group.getChildren().add(temptext);
								        	}
								        }
								        stage.show();
								 
									}
									
								});
							}
							k = 0;
						}
					}
					if (plateau.has_winner()) {
						System.out.println("Bravo au joueur "+ (idjoueur-1) + " qui a gagné!");
				        // Deleting previous text boxes (prevent writing over previous dices numbers...)
		        		 for(Iterator<Node> it=group.getChildren().iterator(); it.hasNext();) {
		        			 if(it.next() instanceof Text) {
		        				it.remove();
		        			 }
		        		}
			        	Text idjoueurtext = new Text(300,190,"Le joueur "+(idjoueur-1)+"a gagné!");
			        	group.getChildren().add(idjoueurtext);
				        System.out.println("test");
				        stage.show();
					}
				}
			// Update the number of dices after a turn
			plateau.update();
			};
			System.out.println("PLUS DE TOURS RESTANTS!");
			return null;
		}
		
	}
	@Override
	public void start(Stage stage) throws Exception {
		ReturnClass returnClass = initialize(stage);
		TaskDisplay task = new TaskDisplay(returnClass.probas_matrix, returnClass.text,returnClass.button,returnClass.textField,returnClass.gc,returnClass.stage);
		// Starting the thread that updates the game (UI and raw data)
		new Thread(task).start();
		stage.show();
	}
}