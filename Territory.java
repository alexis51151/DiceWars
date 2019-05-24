package game;

//import org.omg.CORBA.Object;

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

	public Territory copy() {
		Territory copy = new Territory(0, 0);
		copy.ligne = this.ligne;
		copy.colonne = this.colonne;
		copy.player = this.player;
		copy.dices = this.dices;
		return copy;
	}
	
	public void change(int dices, Player player) {
		this.dices = dices;
		this.player=player;
	}
}