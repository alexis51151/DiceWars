package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

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
		//System.out.println("\n" + "joueurs encore en jeu : " + check);
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
	
	   
	public Territory[][] copy() {   //il ya avait aussi la fonction clone pour un objet, mais elle ne dupliquait pas les territoires
		Territory[][] copy = new Territory[this.lignes][this.colonnes];
		for (int i = 0; i < this.lignes; i++) {
			for (int j = 0; j < this.colonnes; j++) {
				copy[i][j] = (this.territories[i][j]).copy();
			}
		}
		return copy;//copie des territoires (n'alt�re pas les territoires d'origine)
	}
	
}





