package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Nextsituations {   //permet de lister les dénouements possibles du tour d'un joueur
	 ArrayList<int[]> actions;
	 ArrayList<Territory[][]> possible_nextboards;
	 ArrayList<Double> transition_chances;
	 Player gamer;
	 
		
	 public Nextsituations(Player gamer, Board plateau) { //Renvoie les actions et plateaux résultants possibles avec leurs "probabilités" (transition)
		// TODO Auto-generated constructor stub
		 this.gamer = gamer;
			ArrayList<Territory[][]> next_positions = new ArrayList<Territory[][]>();
			//ArrayList<Double> transitions = new ArrayList<Double>();
			ArrayList<int[]> actions = new ArrayList<int[]>();
			int k = 0;
			int l = 0;
			int save = 0;
			//Double transi_victoire = 0.0;
			for(Territory myTerritory :gamer.territories) {			
				for (int i = 0; i < plateau.territories.length; i++) {
					for (int j = 0; j < plateau.territories[0].length; j++) {
						if (gamer.canFight(myTerritory, plateau.territories[i][j]) ) { // Pour chaque territoire qu'on peut attaquer ce tour-ci
							k = myTerritory.ligne;
							l=myTerritory.colonne;								

							actions.add(actions_tab(l,k,j,i,1)); //action avec 1 pour victoire
							actions.add(actions_tab(l,k,j,i,0)); //action avec 0 pour défaite
//							if (myTerritory.dices == 1) {
//								System.out.println("-------------------PROBLEM-----------------");
//							}
							save = myTerritory.dices;
							//System.out.println("before : " +save);
							next_positions.add(next_victory(save,plateau,k,l,i,j));
							next_positions.add(next_defeat(plateau, k, l));
							//transi_victoire = transition(plateau.copy(),k,l,i,j);
							//transitions.add(transi_victoire); //transition pour victoire
							//transitions.add(1-transi_victoire); //transition pour défaite : pas obligatoire


						}
					}	
				}
			}
			this.possible_nextboards=next_positions;
			//this.transition_chances = transitions;
			this.actions = actions;
		}
		
	
	 
		public Territory[][] next_defeat(Board plateau, int myline, int mycolumn) {
			Territory[][] copy = plateau.copy();
			copy[myline][mycolumn].dices = 1;
			return copy; //modifs pour le cas de défaite
		}
		
		public Territory[][] next_victory(int save, Board plateau,int myline, int mycolumn, int hisline, int hiscolumn) {
			plateau.territories[myline][mycolumn].dices = save;
			//int lala = plateau.territories[myline][mycolumn].dices;
			Territory[][] copy = plateau.copy();
			//int lili = plateau.territories[myline][mycolumn].dices;
//			for (int i = 0; i < copy.length; i++) {
//				for (int j = 0; j < copy[0].length; j++) {
//					copy[i][j].dices = plateau.territories[i][j].dices;					
//				}
//			}
			copy[hisline][hiscolumn].dices = copy[myline][mycolumn].dices -1;
//			if (copy[hisline][hiscolumn].dices == 0) {
//				System.out.println("lala :"+lala);
//				System.out.println("lili :"+lili);
//				System.out.println("myline :"+ myline +" mycolumn :" + mycolumn + " hisline :" + hisline +  " hiscolumn :" + hiscolumn);
//				System.out.println(plateau.territories[myline][mycolumn].dices + "---> copy ----> "+ copy[myline][mycolumn].dices);
//			}
			copy[hisline][hiscolumn].player = copy[myline][mycolumn].player;
			copy[myline][mycolumn].dices = 1;
			
			return copy; //modifs pour le cas de victoire
		}

		
		public Double transition(Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) { // calcule la proba de victoire (donc d'atteindre cet �tat next_win_)
			int mydice = copy[myline][mycolumn].dices-1;
			int hisdice = copy[hisline][hiscolumn].dices;
			//l'esp�rance du lancer d'1 dé isolé est de 7/2
			Double Emydice = Math.pow(7/2, mydice-1);//espérance de notre lancer
			Double Ehisdice = Math.pow(7/2, hisdice);//espérance de son lancer
			Double transition = 1/(1+Math.exp(Ehisdice-Emydice));  // ligne discutable : fonction sigmoid : renvoie une probabilité associée à Emydice-Ehisdice
			return transition; //estimation discutable des chances d'accéder au tableau (A modifier : on mettra les vrais probas, car on peut les calculer)
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
		
	 
	 public String toString_dices() {
		 String chaine = "";
		 for (Territory[][] territories : this.possible_nextboards) {
			 for (Territory[] line : territories) {
				 for(Territory ter : line) {
					 if (ter != null) {
					 chaine += ter.dices + "  ";
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