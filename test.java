package game;

import java.util.ArrayList;
import java.util.Iterator;

public class test {
	public test() {
		
		
	}
	   String tab_toString(int[] tab) {
				String chaine = new String();
				for (int i : tab) {
					chaine += " "+ i;
				}
				return chaine;
			}
		
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test tata  = new test();
		Board plateau = new Board(8,4,2,30,5);
		/*///System.out.println("PLATEAU ACTUEL" + "\n");
	    //System.out.println(plateau.toString());
		Nextsituations nextsituations = new Nextsituations(plateau.gamers[0], plateau) ;
		//System.out.println("\n PROCHAINS PLATEAUX POSSIBLES A L'ISSUE DU TOUR DU JOUEUR"+ "\n");
		//System.out.println(nextsituations.toString_boards());
		Actions_graded actions_notees = new Actions_graded( nextsituations, plateau.gamers[1]);
		System.out.println("PROCHAINES ACTIONS POSSIBLES DU JOUEUR "+ "\n");
		//System.out.println(actions_notees.toString());
		IA bien_basique  = new IA(0,0);
		int[] choix = bien_basique.IA_basique(actions_notees);
		System.out.println("ACTION RETENUE"+ "\n");
		System.out.println(tata.tab_toString(choix));*/
		ChoicesTree choicestree = new ChoicesTree(plateau,1);
		System.out.println(choicestree.racine.toString_boards()); // Affiche le plateau de d�part
		choicestree.AddLeaves(choicestree.racine, 3, 1);
		System.out.println(choicestree.toString_actions());
		System.out.println(choicestree.nb_leaves); // Nb de plateaux intermédiaires

	}

}