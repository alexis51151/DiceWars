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
		Board plateau = new Board(3,2,2,8,5);
		
	    System.out.println(plateau.toString());
		Nextsituations nextsituations = new Nextsituations(plateau.gamers[0], plateau) ;
		System.out.println(nextsituations.toString_boards());
		Actions_graded actions_notées = new Actions_graded( nextsituations, plateau.gamers[1]);
		System.out.println(actions_notées.toString());
		IA bien_basique  = new IA();
		int[] choix = bien_basique.IA_basique(actions_notées);
		System.out.println(tata.tab_toString(choix));
	}

}
