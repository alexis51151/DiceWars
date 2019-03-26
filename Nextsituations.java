package game;

import java.util.ArrayList;

public class Nextsituations {
	 ArrayList<Territory[][]> possible_nextboards;
	 ArrayList<Double> transition_chances;
	 
	 public Nextsituations() {
		// TODO Auto-generated constructor stub
		 possible_nextboards = null;
		 transition_chances = null;
	 }
	 
	 public  ArrayList<Territory[][]> possible_nextboards(){
		 return this.possible_nextboards;
	 }
	 
	 public  ArrayList<Double> transition_chances(){
		 return this.transition_chances;
	 }
	 
	 public String toString_boards() {
		 String chaine = "";
		 for (Territory[][] territories : this.possible_nextboards) {
			 for (Territory[] line : territories) {
				 for(Territory ter : line) {
					 if (ter != null) {
					 chaine += ter.toString() + "\n" + "\n";
					 } 
					 else {
						 System.out.println("bouh");
					 }
				 }
			 }
			 chaine += "<<<<<<<<<<<<<<<<<<<<<<<<<<<" + "\n" + "\n";
		 }
		 return chaine;
	 }

}
