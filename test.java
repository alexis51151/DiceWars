package game;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class test extends Application{
	static ChoicesTree choicestree;
	Color[] liste_colors = {Color.BLUE,Color.RED,Color.GREEN,Color.PURPLE,Color.YELLOW,Color.ORANGE,Color.PINK,Color.LIGHTGREEN};
	public test() {
		
		
	}
	String tab_toString(int[] tab) {
				String chaine = new String();
				for (int i : tab) {
					chaine += " "+ i;
				}
				return chaine;
	}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test monTest  = new test();
		Game maGame = new Game();
		float[][] probaMatrix = Probas.ProbaMatrix(8);
		Board plateau = new Board(3,2,2,8,5);
		System.out.println("PLATEAU ACTUEL" + "\n");
	    System.out.println(plateau.toString());
		Nextsituations nextsituations = new Nextsituations(plateau.gamers[0], plateau, probaMatrix) ;
		System.out.println("all :");
		int i = 0;
		for (float f : nextsituations.transition_chances) {
			System.out.println(i +" "+f);
			i +=1;
		}
//		System.out.println("\n PROCHAINS PLATEAUX POSSIBLES A L'ISSUE DU TOUR DU JOUEUR"+ "\n");
//	    System.out.println(nextsituations.toString_boards());
//		Actions_graded actions_notees = new Actions_graded( nextsituations, plateau.gamers[1]);
//		System.out.println("PROCHAINES ACTIONS POSSIBLES DU JOUEUR "+ "\n");
//		System.out.println(actions_notees.toString());
//		IA bien_basique  = new IA(0,0);
//		int[] choix = bien_basique.IA_basique(actions_notees);
//		System.out.println("ACTION RETENUE"+ "\n");
//		System.out.println(monTest.tab_toString(choix));
//	    choicestree = new ChoicesTree(plateau,1);
//		System.out.println(choicestree.racine.toString_boards()); // Affiche le plateau de d�part
//		choicestree.AddLeaves(choicestree.racine,4, 1);
//		System.out.println(choicestree.toString_actions());
//		System.out.println(choicestree.nb_leaves); // Nb de plateaux intermédiaires
//		Application.launch(args);
		
		
		
	}
	
	
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		// Basic UI parameters : window's height/width, title, and javafx's wrappers for handling scenes
	    int l = 0,c=0;
		for(ArrayList<Territory[][]> elt : choicestree.Layers) {
			System.out.println("Taille :" + elt.size());
			for(Territory[][] ligne : elt) {
				c +=1;
			}
			l +=1;
		}
		int largeur = 25*(c);
		int hauteur = 25*(l+1);
		stage.setTitle("DiceWars");
		stage.setAlwaysOnTop(true);
		Group group = new Group();
	    Scene scene = new Scene(group);
	    stage.setScene(scene);
	    Canvas canvas = new Canvas(Math.min(1500,largeur),hauteur+30);
	    group.getChildren().add(canvas);
	    stage.setResizable(false);
	    GraphicsContext gc = canvas.getGraphicsContext2D();
	    l = 0;
	    c=0;
	    int k = 0;
	    int t = 0;
		for(ArrayList<Territory[][]> elt : choicestree.Layers) {
			for(Territory[][] ligne : elt) {
				if (c*25+20+5>1500) {
					c = 0; 
					t +=1;
					l +=1;
					Sprite newsprite = new Sprite(c*25,l*25,20,20,liste_colors[(l-t)%7]);
	        		Text temptext = new Text(c*25+4,l*25+13,Integer.toString(k+l-t));
	        		group.getChildren().add(temptext);
					newsprite.rendercircle(gc);
					System.out.println("On dépasse la largeur max de l'écran");
				}
				else {
					Sprite newsprite = new Sprite(c*25,l*25,20,20,liste_colors[(l-t)%7]);
	        		Text temptext = new Text(c*25+4,l*25+13,Integer.toString(k+l-t));
	        		group.getChildren().add(temptext);
					newsprite.rendercircle(gc);
				}
				c +=1;
				k += 1;
			}
			c = 0;
			l +=1;
			k -=1;
		}
		//System.out.println("Hauteur : " + hauteur);
		//System.out.println(l);
		System.out.println("Couches : " + choicestree.Layers.size());


	    stage.show();
	}

	
//USELESS
	
//	public Board  copyBoard(int N) { // permet de créer un Board identique à un Board d'origine (this)
//	//N est le nombre de dés par joueur initial du plateau
//	 Board copy = new Board(this.colonnes, this.lignes, this.gamers_number,N, this.max_dices);
////	 copy.colonnes;
////	 copy.lignes;
////	 copy.max_dices;
////	 copy.number_territory;   
////	 gamers_number; tous ces éléments sont déjà bons		 
//	 
//	 copy.territories = this.copy();// il faut réattribuer les bons gamers
//	 for (int i = 0; i < copy.gamers.length; i++) {
//		 copy.gamers[i].ajust(this.territories);
//	 }
//	 
//	 players_territories = this.copyPlayerTerritories(); // nb territoires par players : Scores
//	return copy;
//}
//
//public int[] copyPlayerTerritories() {
//	int[] copy = new int[this.players_territories.length];
//	for (int i = 0; i < copy.length; i++) {
//		copy[i]=this.players_territories[i];
//	}
//	return copy;
//}
	
	// USELESS	
	
}