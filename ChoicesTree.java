package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class ChoicesTree {
	Leaf racine; // Arbre compos� de feuilles qui sont les plateaux associés à des actions du joueur ou de son adversaire
	int numjoueur;
	int[] action_retenue;
	public Double best_grade;
	Board auxboard;
	Float proba;
	float[][] probaMatrix ;
	Float seuil_proba = 0.999999f; 
	// Caractéristiques du plateau initial (on ne s'int�resse qu'au tableau territories par la suite)
	public int gamers_number;
	public int colonnes;
	public int lignes;
	public int max_dices;
	public int number_territory;
	public int[] players_territories; // nb territoires par players : Scores
	public int N;
	public int nb_leaves;
	public Player[] gamers; // liste des joueurs
	ArrayList<ArrayList<Territory[][]>> Layers = new ArrayList<ArrayList<Territory[][]>>(); // Les couches de l'arbre (pour représentation)
	
	
	public ChoicesTree(Board plateau, int numjoueur, float[][] probaMatrix ){ 
		// Création de l'arbre des possibles plateaux pour le joueur n�numjoueur (algorithme r�cursif)
		// Profondeur est la profondeur max (<-> nb de coups d'avance) que l'on pr�voit
		this.numjoueur = numjoueur;
		this.racine = new Leaf(null,null,plateau.territories,null,1);
		this.gamers_number = plateau.gamers_number;
		this.colonnes = plateau.colonnes;
		this.lignes = plateau.lignes;
		this.max_dices = plateau.max_dices;
		this.probaMatrix = probaMatrix;
		this.number_territory = plateau.number_territory;
		this.players_territories = plateau.players_territories;
		this.N = 8;
		this.nb_leaves = 0;
		this.gamers = plateau.gamers;
		this.best_grade = -10E20;
		this.auxboard = new Board(this.colonnes,this.lignes,this.gamers_number,this.N,this.max_dices);

		
	}
	
	public void AddLeaves(Leaf leaf,int profondeur,int parite) { // J'ai fait que le cas pour 2 joueurs pour l'instant (� faire : utiliser la parit� (initialis�e � 1) pour 2 fonctions de co�t) 
		
		float currentseuil = this.seuil_proba;
		//System.out.println(">>>>>>>>>>>>>PROFONDEUR : " + profondeur);
		auxboard.territories = leaf.copy(); //Seul élément du board auquel on s'int�resse ici
//		for (Player gamer : auxboard.gamers) {
//			gamer.territories = leaf.ajustGamerTerr(gamer);
//		}
		
		Nextsituations nextsituations = new Nextsituations(gamers[(numjoueur+parite)%2], auxboard,probaMatrix) ; // Pour k joueurs : changer %2 par %k
	    float best_probas = Collections.max(nextsituations.transition_chances); //pour le nextsituation actuel
	    //System.out.println(" BESST :" + best_probas);
	    if (currentseuil >= best_probas) {
	    	currentseuil = best_probas;
	    }
		//System.out.println(nextsituations.actions_toString());
		//System.out.println(nextsituations.toString_dices());
		Actions_graded actions_notees = new Actions_graded(nextsituations, gamers[(numjoueur+parite+1)%2]); 
		ArrayList<int[]> actions_possibilities = actions_notees.actions;
		ArrayList<Double> grades = actions_notees.grades;
		ArrayList<Territory[][]> boards = nextsituations.possible_nextboards;
		Iterator<Double> it_grades = grades.iterator();
		Iterator<int[]> it_actions = actions_possibilities.iterator();
		Iterator<Territory[][]> it_boards = boards.iterator();
		// On considère au max 8 dés sur un territoire
		if (profondeur != 0) {
			while (it_actions.hasNext()) { // Création des feuilles associées au plateau de la feuille en paramètre de notre fonction
				int[] action_possibility = it_actions.next();
				String coucou ="";
				for (int i = 0; i < action_possibility.length; i++) {
					coucou += " "+action_possibility[i];
				}

				double grade  =it_grades.next();
				Territory[][] board = it_boards.next();
				// On cherche le nb de dés sur territoires de d�part et d'arriv�e pour calculer la probabilité du nouveau plateau
				int k = board[action_possibility[1]][action_possibility[0]].dices;
				int n = board[action_possibility[3]][action_possibility[2]].dices;
				// On calcule la probabilité d'avoir ce plateau (utilisation d'une méthode d'estimateur statistique)
				float proba = probaMatrix[n][k-1]; // Pas d'erreur car on a au moins un dé sur un territoire
//				 System.out.println("best proba again :"+best_probas[0]);
       			//System.out.println("seuil :" + seuil_proba+" previous :"+leaf.probability +" k " + k + " n "+ n + " proba :"+proba +" action :"+ coucou +" pro :"+ profondeur);
				if (proba >= currentseuil) {			
					//nb_dices(board);
					float proba_totale = leaf.probability*proba*100;
					//System.out.println("proba_totale :"+proba_totale + " proba:"+proba + " prev :"+leaf.probability);
					Leaf new_leaf = new Leaf(leaf,action_possibility,board,grade*proba_totale,proba_totale);
					if ((numjoueur+parite)%2 == 1) {
						new_leaf.update();
					}
					 // Rajouter les dés pour le tour suivant
					leaf.next.add(new_leaf);
					nb_leaves +=1;
					//System.out.println(profondeur);
					//System.out.println(new_leaf.toString_boards());
					//System.out.println(new_leaf.action_toString());
					try { // On regarde si on a besoin de la créer
						Layers.get(profondeur);
						Layers.get(profondeur).add(board);
					}
					catch (Exception e){ // Sinon, on créer la couche
						ArrayList<Territory[][]> aux = new ArrayList<Territory[][]>();
						aux.add(board);
						Layers.add(aux);
					}
					AddLeaves(new_leaf,profondeur-1,(parite+1)%2);
				}				
			}
			
		}
		else {
			//System.out.println("bonjour");
			//System.out.println(leaf.mark);
			//System.out.println(" leaf : "+leaf.mark+" best :" + best_grade);
			if (leaf.mark > best_grade) {
				//System.out.println("oh!");
					best_grade = leaf.mark;
				    this.findBestAction(leaf);
			}
		}
		//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
	}
	

	
	 public boolean appartient(float x, float[] tab) {
		 boolean result = false;
		 for (float f : tab) {
			if (x == f) {
				result = true;
			}
		}
		 System.out.println("result");
		 return result;
	 }
	 
	public void findBestAction(Leaf leaf) {
		//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<  arborescence :");
     //System.out.println(" proba : "+leaf.probability + "mark : "+leaf.mark);
		while (leaf.previous.mark != null) {
			leaf = leaf.previous;
			  System.out.println(" proba : "+leaf.probability + "mark : "+leaf.mark);
		}
		this.action_retenue = leaf.action;
		//System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<best ");
	}
	
	public String toString_actions() {
		String chaine = "";
		if (this.action_retenue != null ) {
			chaine += " myabs :"+this.action_retenue[0] + " myord :"+this.action_retenue[1] +" hisabs :"+this.action_retenue[2] +" hisord :"+this.action_retenue[3] + "\n"+"note :"+this.best_grade + "\n"+"\n";  
		}
		else {
			chaine += "Pas d'action retenue : veuillez modifier le seuil de proba";
		}
		return(chaine);
	}
	
	public static void nb_dices(Territory[][] board) {
		for(int i =0; i <board.length;i++ ) {
			for(int j =0; j < board[0].length;j++) {
				System.out.println("En i="+i + " et j="+j + ", on a : " + board[i][j].dices+ "d�s");
			}
		}
	}
		
}