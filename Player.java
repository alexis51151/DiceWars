package game;
import java.util.LinkedList;



public class Player {
	int id;
	LinkedList<Territory> territories;
	boolean hasAttacked;
	Board plateau;
	
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
			System.out.println("pas assez de des");
			return false;
		}
	    else if (myTerritory.player.id != this.id) {
	    	System.out.println("territoire de depart n'appartient pas au joueur");
			return false;
		}
		else if (this.id == hisTerritory.player.id ) {
			System.out.println("territoire cible apparteant au joueur");
			return false;
		}
		else {
		if(myTerritory.ligne-hisTerritory.ligne ==0) {
			if (Math.abs(myTerritory.colonne-hisTerritory.colonne)<=1) {
				return(true);
			}
			else {
				System.out.println("territoire non-atteignable");
				return(false);
			}
		}
		else if(myTerritory.colonne-hisTerritory.colonne ==0) {
			if (Math.abs(myTerritory.ligne-hisTerritory.ligne)<=1) {
				return(true);
			}
			else {
				System.out.println("territoire non-atteignable");
				return(false);
			}
		}
		else {
			System.out.println("probleme autre");
			return(false);
		}

	}
	}
	
	void fightGlobal(Territory myTerritory,Territory hisTerritory) {
		if(this.canFight(myTerritory, hisTerritory)) {	//Si je peux combattre
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
		}
	}

}
