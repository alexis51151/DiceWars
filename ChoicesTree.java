package game;

import java.util.ArrayList;
import java.util.Iterator;

public class ChoicesTree {
	Leaf racine; // Arbre composé de feuilles qui sont les plateaux associés à des actions du joueur ou de son adversaire
	int numjoueur;
	int[] action_retenue;
	public Double best_grade;
	Board auxboard;
	Float proba;
	Float seuil_proba = 0f;
	float[][] probaMatrix = Probas.ProbaMatrix(8);
	// Caractéristiques du plateau initial (on ne s'intéresse qu'au tableau territories par la suite)
	public int gamers_number;
	public int colonnes;
	public int lignes;
	public int max_dices;
	public int number_territory;
	public int[] players_territories; // nb territoires par players : Scores
	public int N;
	public int nb_leaves;
	public Player[] gamers; // liste des joueurs
	
	
	public ChoicesTree(Board plateau, int numjoueur){ 
		// Création de l'arbre des possibles plateaux pour le joueur n°numjoueur (algorithme récursif)
		// Profondeur est la profondeur max (<-> nb de coups d'avance) que l'on prévoit
		this.numjoueur = numjoueur;
		this.racine = new Leaf(null,null,plateau.territories,null,1);
		this.gamers_number = plateau.gamers_number;
		this.colonnes = plateau.colonnes;
		this.lignes = plateau.lignes;
		this.max_dices = plateau.max_dices;
		this.number_territory = plateau.number_territory;
		this.players_territories = plateau.players_territories;
		this.N = 8;
		this.nb_leaves = 0;
		this.gamers = plateau.gamers;
		this.best_grade = 0.0;
		this.auxboard = new Board(this.colonnes,this.lignes,this.gamers_number,this.N,this.max_dices);

		
	}
	
	public void AddLeaves(Leaf leaf,int profondeur,int parite) { // J'ai fait que le cas pour 2 joueurs pour l'instant (à faire : utiliser la parité (initialisée à 1) pour 2 fonctions de coût) 
		auxboard.territories = leaf.board; //Seul élément du board auquel on s'intéresse ici
		Nextsituations nextsituations = new Nextsituations(gamers[(numjoueur+parite)%2], auxboard) ; // Pour k joueurs : changer %2 par %k
		Actions_graded actions_notees = new Actions_graded(nextsituations, gamers[(numjoueur+parite+1)%2]); 
		ArrayList<int[]> actions_possibilities = actions_notees.actions;
		ArrayList<Double> grades = actions_notees.grades;
		ArrayList<Territory[][]> boards = nextsituations.possible_nextboards;
		Iterator<Double> it_grades = grades.iterator();
		Iterator<int[]> it_actions = actions_possibilities.iterator();
		Iterator<Territory[][]> it_boards = boards.iterator();
		// On considère au max 8 dés sur un territoire
		if (profondeur != 0) {
			while (it_actions.hasNext()) { // Création des feuilles associées au plateau de la feuille en paramtre de notre fonction
				int[] action_possibility = it_actions.next();
				double grade  =it_grades.next();
				Territory[][] board = it_boards.next();
				// On cherche le nb de dés sur territoires de départ et d'arrivée pour calculer la probabilité du nouveau plateau
				int k = board[action_possibility[1]][action_possibility[0]].dices;
				int n = board[action_possibility[3]][action_possibility[2]].dices;
				if (k==0 || n ==0) {
					System.out.println("BIZARRE");
					System.out.println("k: " + k);
					System.out.println("n: " + n);
				}
				// On calcule la probabilité d'avoir ce plateau (utilisation d'une méthode d'estimateur statistique)
				float proba = probaMatrix[k-1][n-1]; // Pas d'erreur car on a au moins un dé sur un territoire
				if (leaf.probability*proba > seuil_proba) {
					nb_dices(board);
					Leaf new_leaf = new Leaf(leaf,action_possibility,board,grade,leaf.probability*proba);
					leaf.next.add(new_leaf);
					nb_leaves +=1;
					//System.out.println(new_leaf.toString_boards());
					System.out.println(new_leaf.action_toString());
					AddLeaves(new_leaf,profondeur-1,(parite+1)%2);
				}
			}
		}
		else {
			if (leaf.mark > best_grade) {
				best_grade = leaf.mark;
				action_retenue = leaf.action;
			}
		}
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
				System.out.println("En i="+i + " et j="+j + ", on a : " + board[i][j].dices+ "dés");
			}
		}
	}
		
}