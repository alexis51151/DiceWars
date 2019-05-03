package game;

import java.util.ArrayList;
import java.util.Iterator;

public class ChoicesTree {
	Leaf racine; // Arbre compos� de feuilles qui sont les plateaux associ�s � des actions du joueur ou de son adversaire
	int numjoueur;
	int[] action_retenue;
	Leaf best_leaf;
	public Double best_grade;
	// Caract�ristiques du plateau initial (on ne s'int�resse qu'au tableau territories par la suite)
	public int gamers_number;
	public int colonnes;
	public int lignes;
	public int max_dices;
	public int number_territory;
	public int[] players_territories; // nb territoires par players : Scores
	public int N;   //nombre de dés au début
	public int nb_leaves;
	public Player[] gamers; // liste des joueurs
	
	
	public ChoicesTree(Board plateau, int numjoueur){ 
		// Cr�ation de l'arbre des possibles plateaux pour le joueur n�numjoueur (algorithme r�cursif)
		// Profondeur est la profondeur max (<-> nb de coups d'avance) que l'on pr�voit
		this.numjoueur = numjoueur;
		this.racine = new Leaf(null,null,plateau.territories,null,null);
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
		
	}
	
	
	public void AddLeaves(int max_dices, double[][] probas_matrix, Leaf leaf,int profondeur,int parite) { // J'ai fait que le cas pour 2 joueurs pour l'instant (� faire : utiliser la parit� (initialis�e � 1) pour 2 fonctions de co�t) 
		Board auxboard = new Board(colonnes,lignes,gamers_number,N,max_dices);
		System.out.println("profondeur" + profondeur);
		auxboard.territories = leaf.board; //Seul �l�ment du board auquel on s'int�resse ici
		//System.out.println(auxboard.toString());
		Nextsituationsevolved nextsituations = new Nextsituationsevolved(probas_matrix,gamers[(numjoueur+parite)%2], auxboard) ; // Pour k joueurs : changer %2 par %k
		//System.out.println("next");
		Actions_graded_evolved actions_notees = new Actions_graded_evolved(nextsituations, gamers[(numjoueur +parite + 1)%2]); 
		System.out.println("friend :" + nextsituations.gamer.id + "  foe : " + gamers[(numjoueur)%2].id);
		System.out.println("graded");
		ArrayList<int[]> actions_possibilities = actions_notees.actions;
		ArrayList<Double> grades = actions_notees.grades;
		ArrayList<Territory[][]> boards = nextsituations.possible_nextboards;
		ArrayList<Double> transitions = actions_notees.transitions;
		Iterator<Double> it_grades = grades.iterator();
		Iterator<Double> it_transi = transitions.iterator();
		Iterator<int[]> it_actions = actions_possibilities.iterator();
		Iterator<Territory[][]> it_boards = boards.iterator();
		if (profondeur != 0) {
			while (it_actions.hasNext()) { // Cr�ation des feuilles associ�es au plateau de la feuille en paramtre de notre fonction
				int[] action_possibility = it_actions.next();
				double grade  =it_grades.next();
				double transi = it_transi.next();
				Territory[][] board = it_boards.next();
				Leaf new_leaf = new Leaf(leaf,action_possibility,board,grade, transi);
				if (parite ==1) {
					update(new_leaf.board, max_dices);
				}
				leaf.next.add(new_leaf);
				nb_leaves +=1;
				//System.out.println(new_leaf.toString_boards());
				System.out.println(new_leaf.action_toString());
				System.out.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				AddLeaves(max_dices, probas_matrix,new_leaf,profondeur-1,(parite+1)%2);
			}
		}
		else {
			System.out.println("else");
			System.out.println(leaf.mark);
			System.out.println(best_grade);
			if (leaf.mark > best_grade) {   //niveau  : action concurrente imédiate
				best_grade = leaf.mark;
				this.best_leaf = leaf;
				//System.out.println("test");
			}
		}
	}
	
	public void update(Territory[][] territories, int max_dices) { // ajout 1 de partout sauf si > max_dices
		for (Territory[] liste : territories) {
			for (Territory ter : liste) {
				if (ter.dices < max_dices) {
					ter.dices++;
				}
			}
		}
	}
 
	
public void findBestAction() {
	Leaf leaf = this.best_leaf;
	while (leaf.previous.mark != null) {
		leaf = leaf.previous;
	}
	this.action_retenue = leaf.action;
}
	
	public String toString_actions() {
		String chaine = "";
		chaine += " myabs :"+this.action_retenue[0] + " myord :"+this.action_retenue[1] +" hisabs :"+this.action_retenue[2] +" hisord :"+this.action_retenue[3] + "\n"+"note :"+this.best_grade + "\n"+"\n";  
		return(chaine);
	}
		
}


