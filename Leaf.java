package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

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
//		if(this.action != null) {
//				if(this.mark != null) {
//					System.out.println("action :" + this.action_toString() + " proba :"+this.probability + " mark :"+this.mark);
//			}
//		}
		
	}
	
	public LinkedList<Territory> ajustGamerTerr(Player gamer) {
		LinkedList<Territory> GamerTerr = new LinkedList<Territory>();
		for (Territory[] ligne : this.board) {
			for (Territory territory : ligne) {
				if (territory.player == gamer ) {
					GamerTerr.add(territory);
				}
			}
		}
	return GamerTerr;
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
		public Territory[][] copy() {   //il ya avait aussi la fonction clone pour un objet, mais elle ne dupliquait pas les territoires
			Territory[][] copy = new Territory[this.board.length][this.board[0].length];
			for (int i = 0; i < this.board.length; i++) {
				for (int j = 0; j < this.board[0].length; j++) {
					copy[i][j] = (this.board[i][j]).copy();
//					if (copy[i][j].dices != this.territories[i][j].dices ){
//					System.out.println("--------- "+this.territories[i][j].dices + " copy --> " +copy[i][j].dices + " --------------" );}
			}
			}
			
			return copy;//copie des territoires (n'alt�re pas les territoires d'origine)
		}




}