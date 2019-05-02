package game;

public class Probas { //calcule la proba de gagner si l'attaquant a k dés au total sur son territoire de départ  et l'adversaire n dés (on rappelle qu'en cas d'égalité lors du tirage, le defenseur gagne)
	int k ;//dés de l'attaquant
	int n; //du défenseur
	float proba;
	Probas(int k, int n){
		float proba = 1/4;
		int iterations = 1000000; 
		for (int i = 0; i < iterations; i++) {
			if (fightWon(k, n)== true) {
				proba += 1 ;
			}
		}
		proba = proba / iterations;
		this.proba = proba;
	}
	
	double diceThrow() {
		return(Math.random()*6+1);
	}
	
	boolean fightWon(int k,int n) {
		int myScore = 0;
		int hisScore = 0;
		for(int i = 0; i < k-1;i++) {		//Lancers de des de l'attaquant
			myScore += (int) diceThrow();
		}
		for(int i = 0; i <n;i++) {	//Lancers de des du defenseur
			hisScore += (int) diceThrow();
		}
		return(myScore>hisScore);
	}
	
	public static float[][] ProbaMatrix(int dices_max){ 
		float[][] probas_matrix = new float[dices_max][dices_max];
		for (int i = 0; i < probas_matrix.length; i++) {
			for (int j = 0; j < probas_matrix[0].length; j++) {
				probas_matrix[i][j] = new Probas(i+1,j+1).proba;
			}
		}
		return probas_matrix;
	}
	
	public static String matToString(float[][] probas_matrix) {
		String chaine = "";
		for (int i = 0; i < probas_matrix.length; i++) {
			for (int j = 0; j < probas_matrix[0].length; j++) {
				chaine += "attack :" + (i+1) + " defense : " + (j+1)  + " proba :" + probas_matrix[i][j] + "\n" + "\n";
			}
		}
		return chaine;
	}
	
	public static void main(String[] args) {
		int dices_max = 8;
		float[][] probas = ProbaMatrix(dices_max);
		System.out.println(matToString(probas ));
	}
}