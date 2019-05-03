package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Actions_graded {  //renvoie une liste des actions possibles, avec les notes (grâce à nextsituations)
	 ArrayList<int[]> actions;
	 ArrayList<Double> grades;
	 Player gamer;
	 Player ennemy;
	 
	 
		public LinkedList<Territory> linked_list_territories(Territory[][] territories , Player gamer){ 
			LinkedList<Territory> linked_territories = new LinkedList<Territory>() ;
			for (Territory[] line : territories) {
				for(Territory territory : line) {
					if (territory.player.id == gamer.id) {
						linked_territories.add(territory);
					}
				}
			}
			return linked_territories;
		}//donne une linked list de territoires du joueurs parmi tous les territoires
		
	 

	 Actions_graded(Nextsituations nextsituations, Player gamer2){ //note la meilleure action face à l'adversaire
		 ArrayList<int[]> actions = new ArrayList<int[]>();
		 ArrayList<Double> grades = new ArrayList<Double>();
		 Player gamer = nextsituations.gamer;
		 Double note = 0.0;
		 ArrayList<int[]> actions_of_nextsituations = nextsituations.actions;
		 ArrayList<Territory[][]> possible_nextboards = nextsituations.possible_nextboards;
		 ArrayList<Double> transition_chances = nextsituations.transition_chances;
		 Iterator<Territory[][]> it_nextboards = possible_nextboards.iterator();
		 //Iterator<Double> it_transitions = transition_chances.iterator();
		 Iterator<int[]> it_actions = actions_of_nextsituations.iterator();
		 while (it_actions.hasNext()) {
			 int[] action = it_actions.next();
			 Territory[][] plateau = it_nextboards.next();
			 //Double proba_transi = it_transitions.next();
			 if (action[4] == 1) {
				LinkedList<Territory> territories1  = linked_list_territories(plateau , gamer );
				LinkedList<Territory> territories2  = linked_list_territories(plateau , gamer2 );
				note = nextsituations.gamer.value(gamer2, 4, territories1, territories2); //*proba_transi si on la prend pas en compte
				actions.add(action);
				grades.add(note);
			}
		}
		 this.actions = actions;
		 this.grades = grades;
		 this.gamer = gamer;
		 this.ennemy = gamer2;
	 }
	 
	 public String toString() {
		 Iterator<int[]> it_actions = this.actions.iterator();
		 Iterator<Double> it_grades = grades.iterator();
		 String chaine = "";
		 while (it_actions.hasNext()) {
			int[] action = it_actions.next();
			Double note = it_grades.next();
			chaine += " myabs :"+action[0] + " myord :"+action[1] +" hisabs :"+action[2] +" hisord :"+action[3] + "\n"+"note :"+note + "\n"+"\n";  
		}
		 return chaine;
	 }
	 
	 public String linkedListToString(LinkedList<Territory> territories ) {
		 Iterator<Territory> it_link = territories.iterator();
		 String chaine = "";
		 while (it_link.hasNext()) {
			 Territory terr = it_link.next();
			 chaine += terr.toString();
		}
		 return chaine;
	 }

}