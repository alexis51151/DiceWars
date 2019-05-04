package game;
import java.util.Iterator;
import java.util.LinkedList;



public class Player {
	int id;
	LinkedList<Territory> territories;
	boolean hasAttacked;
	Board plateau;
	boolean isIA; // to perform actions specific to IA
	Player(int id, Board plateau){
		this.territories = new LinkedList<Territory>();
		this.id = id;
		this.plateau = plateau;
		this.hasAttacked = false;
	}
	
	double diceThrow() {
		return(Math.random()*6+1);
	}
	
	boolean fightWon(Territory myTerritory,Territory hisTerritory) {
		int myScore = 0;
		int hisScore = 0;
		for(int i = 0; i < myTerritory.dices-1;i++) {		//Lancers de des de l'attaquant
			myScore += (int) diceThrow();
		}
		for(int i = 0; i < hisTerritory.dices;i++) {	//Lancers de des du defenseur
			hisScore += (int) diceThrow();
		}
		return(myScore>hisScore);
	}
	
	
	boolean canFight(Territory myTerritory, Territory hisTerritory) { //pas les diagonales 
		if(myTerritory.dices <2) {
			//System.out.println("pas assez de des");
			return false;
		}
	    else if (myTerritory.player.id != this.id) {
	    	//System.out.println("territoire de depart n'appartient pas au joueur");
			return false;
		}
		else if (this.id == hisTerritory.player.id ) {
			//System.out.println("territoire cible appartenant au joueur");
			return false;
		}
		else {
		if(myTerritory.ligne-hisTerritory.ligne ==0) {
			if (Math.abs(myTerritory.colonne-hisTerritory.colonne) == 1) {
				return(true);
			}
			else {
				//System.out.println("territoire non-atteignable");
				return(false);
			}
		}
		else if(myTerritory.colonne-hisTerritory.colonne ==0) {
			if (Math.abs(myTerritory.ligne-hisTerritory.ligne) ==1) {
				return(true);
			}
			else {
				//System.out.println("territoire non-atteignable");
				return(false);
			}
		}
		else {
			//System.out.println("probleme autre");
			return(false);
		}

	}
	}
	
	void fightGlobal(Territory myTerritory,Territory hisTerritory) {
		//System.out.println("FIGHT GLOBAL :"+myTerritory.player.id);
		//if(this.canFight(myTerritory, hisTerritory)) {	//Si je peux combattre
			if(fightWon(myTerritory,hisTerritory)) {	//Si je gagne, je prends le controle du territoire adverse
				System.out.println("Victoire de l'attaquant");
				hisTerritory.player.territories.remove(hisTerritory);
				this.plateau.players_territories[hisTerritory.player.id - 1] --;
				hisTerritory.player = myTerritory.player;
				this.territories.add(hisTerritory);
				this.hasAttacked = true;
				hisTerritory.dices = myTerritory.dices -1;
				myTerritory.dices =1;
				this.plateau.players_territories[this.id - 1] ++;
				
				
			}
			else {
				System.out.println("Victoire du defenseur");
				myTerritory.dices = 1;
			}
		//}
	}
	
	
	
	
	//FONCTIONS UTILES A L'IA
	
//     public double sigmoid(double x) {
//    	 return 1/(1+Math.exp(x));
//     }
     
	
	public LinkedList<Territory> copy() {
		LinkedList<Territory> copy = new LinkedList<Territory>();
		 Iterator<Territory> it_ter = this.territories.iterator();
		 while (it_ter.hasNext()) {
			 Territory next = it_ter.next();
			 copy.add(next.copy());
		 }

//				if (copy[i][j].dices != this.territories[i][j].dices ){
//				System.out.println("--------- "+this.territories[i][j].dices + " copy --> " +copy[i][j].dices + " --------------" );}
		
		
		
		return copy;//copie des territoires (n'alt�re pas les territoires d'origine)
	}
	
	 public double value( Player gamer_2, double alpha,LinkedList<Territory> territories1,LinkedList<Territory> territories2) { //plus c'est proche de 0 : désavantagé, plus c'est proche de 1 : avantagé
			double inter_val_1 = this.intermediate_value(alpha,territories1);
			double inter_val_2 = gamer_2.intermediate_value(alpha,territories2);
			return (inter_val_1 - inter_val_2)/(inter_val_1 + inter_val_2) ;   // retourne la valeur associ�e � la position du gamer_1
		}// on pourrait, dans un cas avec 3+ joueurs, choisir la valeur de la position de gamer_1 comme la valeur min(k dans l'ensemble des joueurs ennemis){value(gamer_1, gamer_k)}
	 
 
	
	double intermediate_value(double alpha, LinkedList<Territory> territories) { //valeur intermédiaire servant à calculer la valeur d'une situation pour un joueur. Alpha permet de choisir quel nombre de dés est l'optimum à réaliser (avant le gain de tour... donc pas maximum)
		double inter_value = 0;
		for (Territory territory : territories) {
			inter_value += -Math.pow(territory.dices-alpha,territory.dices-alpha)+10 ;
		}
		return inter_value;
	}// je précise quels territories, car ça pourrait être d'autre plateaux que celui immédiat du joueur (coups à l'avance de l'ia)

}