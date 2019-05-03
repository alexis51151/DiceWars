package game;

import java.util.ArrayList;
import java.util.Iterator;

public class Leaf {
	int[] action; // Action à effectuer
	Territory[][] board; // Etat du tableau après l'action
	Double mark; // Note de l'action
	ArrayList<Leaf> next; // Plateaux possibles pour la suite
	Leaf previous; // Plateau dont est issue cette possibilité
	float probability;
	int max_dices = 8;
	public Leaf(Leaf previous,int[] action, Territory[][] board, Double mark, float probability){
		this.previous = previous;
		this.action = action;
		this.board = board;
		this.mark = mark;
		this.next = new ArrayList<Leaf>();
		this.probability = probability;
	}
	
	 public String toString_boards() {
		 String chaine = "";
			 for (Territory[] line : this.board) {
				 for(Territory ter : line) {
					 if (ter != null) {
					 chaine += ter.toString() + "\n" + "\n";
					 } 
					 else {
						 System.out.println("bouh");
					 }
				 }
			 }
			 chaine += "<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "\n" + "\n";
		 
		 return chaine;
	 }
	 
	 public String action_toString() {
		 String chaine = "";
		chaine += " myabs :"+action[0] + " myord :"+action[1] +" hisabs :"+action[2] +" hisord :"+action[3] + "\n"+"\n";  
		
		 return chaine;
	 }
	 
		public void update() { // ajout 1 de partout sauf si > max_dices
			for (Territory[] liste : this.board) {
				for (Territory ter : liste) {
					if (ter.dices < this.max_dices) {
						ter.dices++;
					}
				}
			}
		}





}