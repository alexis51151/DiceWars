package game;

public class Territory {
	int colonne;
	int ligne;
	int dices;
	Player player;	
	Territory(int colonne, int ligne){
		this.colonne = colonne;
		this.ligne = ligne;
		this.dices = 0;
	}
	public String toString() {
		String chaine = "Coordonnees : ( "+this.colonne +" ; " + this.ligne+" )"+ "\n"+"Dices : "+this.dices +"\n" +"Gamer Id : "+this.player.id;
		return chaine;
	}
}
