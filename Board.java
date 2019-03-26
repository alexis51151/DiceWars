package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board { // pour le moment, que des territoires carres
	public Territory[][] territories; // peut etre change pour une collection
	public int gamers_number;
	public int colonnes;
	public int lignes;
	public int max_dices;
	public int number_territory;
	public int[] players_territories; // nb territoires par players : Scores
	public Player[] gamers; // liste des joueurs

	public Board(int colonnes, int lignes, int gamers_number, int N, int max_dices) { //N = nbr de des par joueur au debut
		this.max_dices = max_dices;
		this.colonnes = colonnes;
		this.lignes = lignes;
		this.number_territory = lignes * colonnes;
		this.gamers_number = gamers_number;
		int init_territory_per_player = this.number_territory / this.gamers_number;
		this.territories = new Territory[lignes][colonnes];
		this.players_territories = new int[this.gamers_number];
		int player_num = (int) ((int) gamers_number * Math.abs(Math.random() - 0.01)); // (id de 1 gamer number)-1
		for (int i = 0; i < players_territories.length; i++) {
			this.players_territories[i] = init_territory_per_player;
		}
		this.gamers = new Player[gamers_number];
		for (int i = 0; i < gamers_number; i++) {
			this.gamers[i] = new Player(i + 1, this);
		}
		for (int i = 0; i < this.lignes; i++) {
			for (int j = 0; j < this.colonnes; j++) {
				this.territories[i][j] = new Territory(j, i);
				player_num = (int) ((int) gamers_number * Math.abs(Math.random() - 0.01));
				while (this.players_territories[player_num] == 0) {
					player_num = (int) ((int) gamers_number * Math.abs(Math.random() - 0.01));
				}
				this.gamers[player_num].territories.add(this.territories[i][j]);
				this.territories[i][j].player = this.gamers[player_num];
				this.players_territories[player_num] -= 1;
			}
		}
		for (int i = 0; i < players_territories.length; i++) {
			this.players_territories[i] = init_territory_per_player;
			
		}
		int i = 0;
		int[] init_dice = new int[init_territory_per_player];// tableau des des par territoire
		for (Player gamer : gamers) {
			i = 0;
			init_dice = this.dice_choice(N, init_territory_per_player, max_dices);
			for (Territory territory : gamer.territories) {
				territory.dices = init_dice[i];
				i += 1;
			}
		}
	}

	public int[] dice_choice(int N, int k, int nb_max_case) {
		int[] dices_numbers = new int[k];
		int Val_som = k;
		for (int i = 0; i < dices_numbers.length; i++) {
			dices_numbers[i] = 1;
		}
		int numero = 0;
		while (Val_som < N) {
			numero = (int) (k * Math.abs(Math.random() - 0.01));
			if (dices_numbers[numero] < nb_max_case) {
				dices_numbers[numero] += 1;
				Val_som += 1;
			}
		}
		return dices_numbers;
	}

	public String toString() {
		String chaine = "";
		for (Territory[] liste : this.territories) {
			for (Territory ter : liste) {
				chaine += ter.toString() + "\n" + "\n";
			}
		}
		return chaine;
	}

	public void update() { // ajout 1 de partout sauf si > max_dices
		for (Territory[] liste : this.territories) {
			for (Territory ter : liste) {
				if (ter.dices < this.max_dices) {
					ter.dices++;
				}
			}
		}
	}

	public boolean has_winner() { // qqn a gagne?
		int check = 0;
		for (int ter : players_territories) {
			if (ter > 0) {
				check++;
			}
		}
		//System.out.println("\n" + "joueurs encore en jeux : " + check);
		return check == 1;
	}
	public boolean hasAttack(Player player){
		for (Territory[] liste : this.territories) {
			for(Territory cur : liste) {
				if (cur.player.id == player.id) {
					if (cur.dices > 1) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	
	
	//FIN DU JEU AU SENS STRICT : CODE POUR L'IA
	
	
	public double value(Player gamer_1, Player gamer_2, double alpha) { //>0 : avantagé, <0 : désavantagé, la valeur absolue donne l'importance de la situation
		double inter_val_1 = gamer_1.intermediate_value(alpha);
		double inter_val_2 = gamer_2.intermediate_value(alpha);
		return (inter_val_1 - inter_val_2) ;   // retourne la valeur associée à la position du gamer_1
	}// on pourrait, dans un cas avec 3+ joueurs, choisir la valeur de la position de gamer_1 comme la valeur min(k dans l'ensemble des joueurs ennemis){value(gamer_1, gamer_k)}

    
	public Territory[][] copy() {   //il ya avait aussi la fonction clone pour un objet, mais elle ne dupliquait pas les territoires
		Territory[][] copy = new Territory[this.lignes][this.colonnes];
		for (int i = 0; i < this.lignes; i++) {
			for (int j = 0; j < this.colonnes; j++) {
				copy[i][j] = (this.territories[i][j]).copy();
			}
		}
		return copy;//copie des territoires (n'altère pas les territoires d'origine)
	}
	
	public Territory[][] next_defeat(Territory[][] copy, int myline, int mycolumn) {
		copy[myline][mycolumn].dices = 1;
		return copy;//modifs pour le cas de défaite
	}
	
	public Territory[][] next_win(Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) {
		copy[hisline][hiscolumn].dices = copy[myline][mycolumn].dices -1;
		copy[hisline][hiscolumn].player = copy[myline][mycolumn].player;
		copy[myline][mycolumn].dices = 1;
		
		return copy;//modifs pour le cas de victoire
	}
	
	public Double transition(Territory[][] copy,int myline, int mycolumn, int hisline, int hiscolumn) { // calcule la proba de victoire (donc d'atteindre cet état next_win_)
		int mydice = copy[myline][mycolumn].dices-1;
		int hisdice = copy[hisline][hiscolumn].dices;
		//l'espérance du lancer d'1 dé isolé est de 7/2
		Double Emydice = Math.pow(7/2, mydice);//espérance de notre lancer
		Double Ehisdice = Math.pow(7/2, hisdice);//espérance de son lancer
		Double transition = 1/(1+Math.exp(Ehisdice-Emydice));  // fonction sigmoid : renvoie une probabilité associée à Emydice-Ehisdice
		return transition;//estimation discutable des chances d'accèder au tableau
	}
	
	
	public  Nextsituations next_positions(Player gamer){
		ArrayList<Territory[][]> next_positions = new ArrayList<Territory[][]>();
		ArrayList<Double> transitions = new ArrayList<Double>();
		int k = 0;
		int l = 0;
		Double transi_victoire = 0.0;
		for(Territory myTerritory :gamer.territories) {			
				for (int i = 0; i < territories.length; i++) {
					for (int j = 0; j < territories[0].length; j++) {
						if (gamer.canFight(myTerritory, territories[i][j])) {
							k = myTerritory.ligne;
							l=myTerritory.colonne;
							next_positions.add(next_win(this.copy(),k,l,i,j));
							next_positions.add(next_defeat(this.copy(), k, l));
							transi_victoire = transition(this.copy(),k,l,i,j);
							transitions.add(transi_victoire); //transition pour win
							transitions.add(1-transi_victoire); //transition pour defeat
						}
				}	
			}
		}
		Nextsituations next_situations = new Nextsituations();
		next_situations.possible_nextboards=next_positions;
		next_situations.transition_chances = transitions;
		return next_situations;
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
}





