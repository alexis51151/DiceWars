package game;

public class Probas { //calcule la proba de gagner si l'attaquant a k dés au total sur son territoire de départ  et l'adversaire n dés (on rapelle qu'en cas d'égalité lors du tirage, le defenseur gagne)
	int k ;//dés de l'attaquant
	int n; //du défenseur
	double proba;
	Probas(int k, int n){
		double proba = 1/4;
		//int iterations = 1000000; 
		int iterations = 100;
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
	
	public static double[][] ProbaMatrix(int dices_max){ 
		double[][] probas_matrix = new double[dices_max][dices_max];
		for (int i = 0; i < probas_matrix.length; i++) {
			for (int j = 0; j < probas_matrix[0].length; j++) {
				probas_matrix[i][j] = new Probas(i+1,j+1).proba;
			}
		}
		return probas_matrix;
	}
	
	public static String mattoString(double[][] probas_matrix) {
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
		double[][] probas = ProbaMatrix( dices_max);
		System.out.println(mattoString(probas ));
	}
}
