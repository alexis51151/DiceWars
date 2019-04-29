package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Nextsituations {   //permet de lister les d�nouements possibles du tour d'un joueur
	 ArrayList<int[]> actions;
	 ArrayList<Territory[][]> possible_nextboards;
	 ArrayList<Double> transition_chances;
	 Player gamer;
	 
		
	 public Nextsituations(Player gamer, Board plateau) { //Renvoie les actions et plateaux r�sultants possibles avec leurs "probabilit�s" (transition)
		// TODO Auto-generated constructor stub
		 this.gamer = gamer;
			ArrayList<Territory[][]> next_positions = new ArrayList<Territory[][]>();
			ArrayList<Double> transitions = new ArrayList<Double>();
			ArrayList<int[]> actions = new ArrayList<int[]>();
			int k = 0;
			int l = 0;
			Double transi_victoire = 0.0;
			for(Territory myTerritory :gamer.territories) {			
					for (int i = 0; i < plateau.territories.length; i++) {
						for (int j = 0; j < plateau.territories[0].length; j++) {
							if (gamer.canFight(myTerritory, plateau.territories[i][j])) { // Pour chaque territoire qu'on peut attaquer ce tour-ci
								k = myTerritory.ligne;
								l=myTerritory.colonne;
								actions.add(actions_tab(l,k,j,i,1)); //action avec 1 pour victoire
								actions.add(actions_tab(l,k,j,i,0)); //action avec 0 pour d�faite
								next_positions.add(next_victory(plateau.copy(),k,l,i,j));
								next_positions.add(next_defeat(plateau.copy(), k, l));
								transi_victoire = transition(plateau.copy(),k,l,i,j);
								transitions.add(transi_victoire); //transition pour victoire
								transitions.add(1-transi_victoire); //transition pour d�faite : pas obligatoire
							}
					}	
				}
			}
			this.possible_nextboards=next_positions;
			this.transition_chances = transitions;
			this.actions = actions;
		}
		
	
	 
		public Territory[][] next_defeat(Territory[][] copy, int myline, int mycolumn) {
			copy[myline][mycolumn].dices = 1;
			return copy; //modifs pour le cas de d�faite
		}
		
		public Territory[][] next_victory(Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) {
			copy[hisline][hiscolumn].dices = copy[myline][mycolumn].dices -1;
			copy[hisline][hiscolumn].player = copy[myline][mycolumn].player;
			copy[myline][mycolumn].dices = 1;
			
			return copy; //modifs pour le cas de victoire
		}

		
		public Double transition(Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) { // calcule la proba de victoire (donc d'atteindre cet �tat next_win_)
			int mydice = copy[myline][mycolumn].dices-1;
			int hisdice = copy[hisline][hiscolumn].dices;
			//l'esp�rance du lancer d'1 d� isol� est de 7/2
			Double Emydice = Math.pow(7/2, mydice-1);//esp�rance de notre lancer
			Double Ehisdice = Math.pow(7/2, hisdice);//esp�rance de son lancer
			Double transition = 1/(1+Math.exp(Ehisdice-Emydice));  // ligne discutable : fonction sigmoid : renvoie une probabilit� associ�e � Emydice-Ehisdice
			return transition; //estimation discutable des chances d'acc�der au tableau (A modifier : on mettra les vrais probas, car on peut les calculer)
		}
		
		
		public int[] actions_tab(int myabs, int myord, int hisabs, int hisord, int issue) {
			int tab[] = new int[5];
			tab[0]=myabs;
			tab[1]=myord;
			tab[2]=hisabs;
			tab[3]=hisord;
			tab[4]=issue;
			return tab;
		}
		
		//FONCTIONS TOSTRING
		
	    String tab_toString(int[] tab) {
			String chaine = new String();
			for (int i : tab) {
				chaine += " "+ i;
			}
			return chaine;
		}
		
		String int_tab_arrayList_to_string(ArrayList<int[]> list){
			String chaine = new String();
			for (int[] tab : list) {
				chaine += this.tab_toString(tab) + "\n";
			}
			return chaine;
		}
		
		public String terr_toString(Territory[][] territories) {
			 String chaine = "";
			for (Territory[] line : territories) {
				 for(Territory ter : line) {
					 chaine += ter.toString() + "\n" + "\n";
				 }
			 }
			 chaine += "<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "\n" + "\n";
		 return chaine;
	 }
	 
	 public String actions_toString() {
		 ArrayList<int[]> actions = this.actions;
		 ArrayList<Double> transi = this.transition_chances;
		 Iterator<int[]> it_actions = this.actions.iterator();
		 Iterator<Double> it_transi = transi.iterator();
		 String chaine = "";
		 while (it_actions.hasNext()) {
			int[] action = it_actions.next();
			Double note = it_transi.next();
			chaine += " myabs :"+action[0] + " myord :"+action[1] +" hisabs :"+action[2] +" hisord :"+action[3] + "\n"+"note :"+note + "\n";  
		}
		 return chaine;
	 }
	 
	 public String toString_boards() {
		 String chaine = "";
		 for (Territory[][] territories : this.possible_nextboards) {
			 for (Territory[] line : territories) {
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
		 }
		 return chaine;
	 }
		
		
}
		









