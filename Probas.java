package game;

public class Probas { /*calcule la proba de gagner si l'attaquant a k dés au total sur son territoire 
de départ  et l'adversaire n dés (on rappelle qu'en cas d'égalité lors du tirage, le defenseur gagne) */
	int k ;//dés de l'attaquant
	int n; //du défenseur
	float proba;
	Probas(int k, int n){   //ici, on estime la proba de gagner avec k dés contre n
		float proba = 1/4;
		int iterations = 100000; //nombre de simulation de combats
		for (int i = 0; i < iterations; i++) {
			if (fightWon(k, n)== true) {
				proba += 1 ;
			}
		}
		proba = proba / iterations;
		this.proba = proba;
	}
	
	double diceThrow() {   //simulation de lancer de dé
		return(Math.random()*6+1);
	}
	
	boolean fightWon(int k,int n) {   //renvoie un boolean True ou False si le combat a été gagné / perdu    
		int myScore = 0;
		int hisScore = 0;
		for(int i = 0; i < k-1;i++) {		//Lancers de des de l'attaquant : k-1 dés (on en laisse un derrière)
			myScore += (int) diceThrow();
		}
		for(int i = 0; i <n;i++) {	//Lancers de tous les dés du defenseur
			hisScore += (int) diceThrow();   
		}
		return(myScore>hisScore);
	}
	
	public static float[][] ProbaMatrix(int dices_max){ /* on utilise les fonctions précédentes
(diceThrow et fightwon) pour estimer statistiquement la proba de victoire pour toutes les situations,
c'est à dire pour k allant de 1 au nombre maximal de dés */
		float[][] probas_matrix = new float[dices_max][dices_max];
		for (int i = 0; i < probas_matrix.length; i++) {
			for (int j = 0; j < probas_matrix[0].length; j++) {
				probas_matrix[i][j] = new Probas(i+1,j+1).proba;
			}
		}
		return probas_matrix;
	}
	
	public static String matToString(float[][] probas_matrix) {  // permet d'afficher dans la console les probas
		String chaine = "";
		for (int i = 0; i < probas_matrix.length; i++) {
			for (int j = 0; j < probas_matrix[0].length; j++) {
				chaine += "attack :" + (i+1) + " defense : " + (j+1)  + " proba :" + probas_matrix[i][j] + "\n" + "\n";
			}
		}
		return chaine;
	}
	
	public static void main(String[] args) {
		int dices_max = 5;
		float[][] probas = ProbaMatrix(dices_max);
		System.out.println(matToString(probas ));
	}
}