package game;

import java.util.ArrayList;
import java.util.Iterator;

public class Leaf {
	int[] action; // Action � effectuer
	Territory[][] board; // Etat du tableau apr�s l'action
	Double mark; // Note de l'action (dépends aussi des probas des leaf précédentes
	ArrayList<Leaf> next; // Plateaux possibles pour la suite
	Leaf previous; // Plateau dont est issue cette possibilit�
	Double proba; //proba d'arriver à cette leaf (utile pour les mark des leaf suivantes
	
	public Leaf(Leaf previous,int[] action, Territory[][] board, Double grade, Double proba){//ici grade ne dépends pas des probas
		
		this.previous = previous;
		this.action = action;
		this.board = board;
		if (previous != null  ) {
			if (previous.proba !=null) {
				this.proba = proba*previous.proba;  // on a ainsi la proba de la branche, avec la proba imédiate proba et la proba du début de la branche previous.proba
				this.mark = this.proba*grade;
			}
			else {
				this.proba = proba;
				this.mark = grade;
			}
		}
		
		else {
			this.proba = proba;
			this.mark = grade;
		}
	
		this.next = new ArrayList<Leaf>();		 
	}
	
	 public String toString_boards() {
		 String chaine = "";
			 for (Territory[] line : this.board) {
				 for(Territory ter : line) {
					 if (ter != null) {
					 chaine += ter.toString() + "\n" + "\n";
					 } 
					 else {
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




}
