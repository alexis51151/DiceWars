package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Nextsituations {   //permet de lister les dénouements possibles du tour d'un joueur
	 ArrayList<int[]> actions;  //liste des actions possibles
	 ArrayList<Territory[][]> possible_nextboards; //des plateaux possibles (victoire ou défaite) , dans le même ordre que les actions
	 ArrayList<Float> transition_chances; // liste des probabilités de transition associées
	 Player gamer; //pour un joueur donné

		
	 public Nextsituations(Player gamer, Board plateau, float[][] probaMatrix) { //Créer une instance de Nextsituations, à partir d'un plateau et d'un joueur
		// TODO Auto-generated constructor stub
		 this.gamer = gamer; 
		// création des listes
			ArrayList<Territory[][]> next_positions = new ArrayList<Territory[][]>();  
			ArrayList<int[]> actions = new ArrayList<int[]>();
			ArrayList<Float> transitions = new ArrayList<Float>();
			float transi_victoire = 0f;
// on vérifie si le combat est possible, pour toutes les cases du plateau (produit cartésien des cases par elles même), pour le joueur gamer
			for (int k = 0; k < plateau.territories.length; k++) {
				for (int l = 0; l < plateau.territories[0].length; l++) {
					for (int i = 0; i < plateau.territories.length; i++) {
						for (int j = 0; j < plateau.territories[0].length; j++) {
							Territory myTerritory = plateau.territories[k][l];
							// Vérification si le combat peut bien se dérouler entre les deux territoires ci dessous :
							if (gamer.canFight(myTerritory, plateau.territories[i][j]) ) { 
								//-------------ajout des actions : versions victorieuse et perdante de la même action--------------- 
								actions.add(actions_tab(l,k,j,i,1)); //action (avec 1 pour indiquer qu'elle est victorieuse)
								//actions.add(actions_tab(l,k,j,i,0)); //action avec 0 (pour indiquer  qu'elle est perdante)
								//------------ajout des plateaux correspondants (victoire / défaite)  
								next_positions.add(next_victory(plateau,k,l,i,j, gamer));
								//next_positions.add(next_defeat(plateau, k, l, i, j, gamer));
								//------ajout des transitions --------------------------
								transi_victoire = probaMatrix[myTerritory.dices -1][plateau.territories[i][j].dices-1];
								transitions.add(transi_victoire); //transition pour victoire
								//transitions.add(1-transi_victoire); 
							}
						}
					}	
				}
			}
			this.possible_nextboards=next_positions;
			this.transition_chances = transitions;
			this.actions = actions;
	 }

	 
	 //------------------------------------------------
	
//	 ArrayList<LinkedList<Territory>> gamer_territories; //les territoires du joueur
//	 ArrayList<LinkedList<Territory>> ennemi_territories; //les territoires de l'ennemi
//		ArrayList<LinkedList<Territory>> gamer_territories = new ArrayList<LinkedList<Territory>>();
//		this.gamer_territories = gamer_territories;
		//ArrayList<Double> transitions = new ArrayList<Double>();
//		int k = 0;
//		int l = 0;
//		int save = 0;
		//Double transi_victoire = 0.0;
		//for(Territory myTerritory :gamer.territories) {
		//							k = myTerritory.ligne;
		//							l=myTerritory.colonne;	
		//							if (myTerritory.dices == 1) {
		//								System.out.println("-------------------PROBLEM-----------------");
		//							}
		//save = myTerritory.dices;
		//System.out.println("before : " +save);
		//transi_victoire = transition(plateau.copy(),k,l,i,j);
		//transitions.add(transi_victoire); //transition pour victoire
		//transitions.add(1-transi_victoire); //transition pour défaite : pas obligatoire
	 //---------------------------------------------------
	 
		public Territory[][] next_defeat(Board plateau, int myline, int mycolumn,int hisline, int hiscolumn, Player gamer) {
			Territory[][] copy = plateau.copy();
			copy[myline][mycolumn].dices = 1;
//			LinkedList<Territory> gamer_ter = gamer.copy();// copy gamer_territories
//			this.gamer_territories.add(gamer_ter);
//			LinkedList<Territory> ennemi_ter = plateau.territories[hisline][hiscolumn].player.copy();// copy ennemi_territories
//			this.ennemi_territories.add(ennemi_ter);
			return copy; //modifs pour le cas de défaite
		}
		
		public Territory[][] next_victory( Board plateau,int myline, int mycolumn, int hisline, int hiscolumn,Player gamer) {
			//plateau.territories[myline][mycolumn].dices = save;
			//int lala = plateau.territories[myline][mycolumn].dices;
			Territory[][] copy = plateau.copy();
			//int lili = plateau.territories[myline][mycolumn].dices;
//			for (int i = 0; i < copy.length; i++) {
//				for (int j = 0; j < copy[0].length; j++) {
//					copy[i][j].dices = plateau.territories[i][j].dices;					
//				}
//			}
			copy[hisline][hiscolumn].dices = copy[myline][mycolumn].dices -1;
			copy[hisline][hiscolumn].player = gamer;
//			if (copy[hisline][hiscolumn].dices == 0) {
//				System.out.println("lala :"+lala);
//				System.out.println("lili :"+lili);
//				System.out.println("myline :"+ myline +" mycolumn :" + mycolumn + " hisline :" + hisline +  " hiscolumn :" + hiscolumn);
//				System.out.println(plateau.territories[myline][mycolumn].dices + "---> copy ----> "+ copy[myline][mycolumn].dices);
//			}
			copy[hisline][hiscolumn].player = copy[myline][mycolumn].player;
			copy[myline][mycolumn].dices = 1;
//			LinkedList<Territory> gamer_ter = gamer.copy();// copy gamer_territories
//			gamer_ter.add(copy[hisline][hiscolumn]);
//			this.gamer_territories.add(gamer_ter);
//			LinkedList<Territory> ennemi_ter = plateau.territories[hisline][hiscolumn].player.copy();// copy ennemi_territories
//			ennemi_ter.remove(copy[hisline][hiscolumn]);
//			this.ennemi_territories.add(ennemi_ter);
			
			return copy; //modifs pour le cas de victoire
		}

		// ----------------Utilisé au début pour IA basique : Espérance de notre lancer avec k lancers sur la case d'attaque, contre n lancers sur la case de defense--------------------------- 
		public Double transitionold(Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) { // calcule la proba de victoire (donc d'atteindre cet �tat next_win_)
			int mydice = copy[myline][mycolumn].dices-1;
			int hisdice = copy[hisline][hiscolumn].dices;
			//l'espérance du lancer d'1 dé isolé est de 7/2
			Double Emydice = Math.pow(7/2, mydice-1);//espérance de notre lancer
			Double Ehisdice = Math.pow(7/2, hisdice);//espérance de son lancer    
			Double transition = 1/(1+Math.exp(Ehisdice-Emydice));  // ligne discutable : fonction sigmoid : renvoie une probabilité associée à Emydice-Ehisdice
			return transition; //estimation discutable des chances d'accéder au tableau (A modifier : on mettra les vrais probas, car on peut les calculer)
		}
		
//		public Double transition(int[][] probas, Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) { // calcule la proba de victoire (donc d'atteindre cet �tat next_win_)
//			int mydice = copy[myline][mycolumn].dices-1;
//			int hisdice = copy[hisline][hiscolumn].dices;
//			//l'espérance du lancer d'1 dé isolé est de 7/2
//			Double Emydice = Math.pow(7/2, mydice-1);//espérance de notre lancer
//			Double Ehisdice = Math.pow(7/2, hisdice);//espérance de son lancer    
//			Double transition = 1/(1+Math.exp(Ehisdice-Emydice));  // ligne discutable : fonction sigmoid : renvoie une probabilité associée à Emydice-Ehisdice
//			return transition; //estimation discutable des chances d'accéder au tableau (A modifier : on mettra les vrais probas, car on peut les calculer)
//		}
		
		
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
		 ArrayList<Float> transi = this.transition_chances;
		 Iterator<int[]> it_actions = this.actions.iterator();
		 Iterator<Float> it_transi = transi.iterator();
		 String chaine = "";
		 while (it_actions.hasNext()) {
			int[] action = it_actions.next();
			Float note = it_transi.next();
			chaine += " myabs :"+action[0] + " myord :"+action[1] +" hisabs :"+action[2] +" hisord :"+action[3] + " defeat?" + action[4]+"\n"+"note :"+note + "\n";  
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
	 
	 
//	 public boolean appartientPas(float x, float[] tab) {
//		 boolean result = true;
//		 for (float f : tab) {
//			if (x == f) {
//				result = false;
//			}
//		}
//		 System.out.println("result");
//		 return result;
//	 }
	 

	 
//	 public float[] best_proba(int rest) {
//		 Iterator<Float> it_transitions = this.transition_chances.iterator();
//		 float[] probas = new float[this.transition_chances.size()/2];
//		 int i = 0;
//		// System.out.println("coucou");
//		 while (it_transitions.hasNext()) {
//			 Float proba_transi = it_transitions.next();
//			 probas[i] = proba_transi;
//			 proba_transi = it_transitions.next();
//			 i+=1;
//		 }
//		 java.util.Arrays.sort(probas);
//		 float[] bestprobas = new float[rest];
//		 for (int j = 0; j < bestprobas.length; j++) {
//			bestprobas[j] = probas[probas.length -j-1];
//		}
//		 for (int j = 0; j < bestprobas.length; j++) {
//			//System.out.println("ah :"+ bestprobas[j]);
//		}
//		return bestprobas;	 
//	
//	 }
	 
//	 public void eliminate(int rest) {  // permet de ne garder que les rest meilleures probas
//		 float[] best_tab = best_proba(rest);
//		 System.out.println("best probas done!");
//		 Iterator<Float> it_transitions = this.transition_chances.iterator();
//		 Iterator<int[]> it_actions = this.actions.iterator();
//		 Iterator<Territory[][]> it_nextboards = possible_nextboards.iterator();
//		 System.out.println(" iterator done");
//		 while (it_actions.hasNext()) {
//			 System.out.println("yo");
//			 int[] action = it_actions.next();
//			 Float proba_transi = it_transitions.next();
//			 Territory[][] plateau = it_nextboards.next();
//			 System.out.println("koko");
//			 if (action[4] == 1 ) {
//				 System.out.println("yeahh");
//					 if (appartientPas( proba_transi, best_tab)) {
//						 System.out.println("appartient pas");
//						 this.possible_nextboards.remove(plateau);
//						 this.actions.remove(action);
//						 this.transition_chances.remove(proba_transi);						 
//				 }						 
//			 }
//		 }  
//		 
//	 }
		
}