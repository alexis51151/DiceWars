package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Actions_graded {  //renvoie une liste des actions possibles, avec les notes (gr�ce � nextsituations)
	 ArrayList<int[]> actions;
	 ArrayList<Double> grades;
	 ArrayList<Double> gradesBasic; //include probability
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
		
	 

	 Actions_graded(Nextsituations nextsituations, Player gamer2){ //note les  actions répertoriées par nextsituations, face à un l'adversaire
		 ArrayList<int[]> actions = new ArrayList<int[]>();    //liste des actions
		 ArrayList<Double> grades = new ArrayList<Double>();   // liste des notes
		 ArrayList<Double> grades_for_basic = new ArrayList<Double>();
		 Player gamer = nextsituations.gamer;
		 Double note = 0.0;
		 ArrayList<int[]> actions_of_nextsituations = nextsituations.actions;
		 ArrayList<Territory[][]> possible_nextboards = nextsituations.possible_nextboards;
		 ArrayList<Float> transition_chances = nextsituations.transition_chances;
		 Iterator<Territory[][]> it_nextboards = possible_nextboards.iterator();
		 Iterator<Float> it_transitions = transition_chances.iterator();
		 Iterator<int[]> it_actions = actions_of_nextsituations.iterator();
		 while (it_actions.hasNext()) {
			 int[] action = it_actions.next();
			 Territory[][] plateau = it_nextboards.next();
			 Float proba_transi = it_transitions.next();
			 if (action[4] == 1) {
				LinkedList<Territory> territories1  = linked_list_territories(plateau , gamer );
				LinkedList<Territory> territories2  = linked_list_territories(plateau , gamer2 );
				note = nextsituations.gamer.value(gamer2, gamer2.plateau.max_dices-1, territories1, territories2); //*proba_transi si on la prend  en compte
				actions.add(action);
				grades.add(note);
				grades_for_basic.add(note*proba_transi);
			}
		}
		 this.gradesBasic = grades_for_basic;
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