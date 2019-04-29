package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import game.Game.TaskDisplay;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class IA {
	int numjoueur; //IA id in the game
	int id; // Which IA is it ?
	
	public IA(int numjoueur, int id){ 
		this.numjoueur = numjoueur;
		this.id = id;
	}
	
	public int[] IA_basique(Actions_graded actions) {  //A� partir d'une actions_graded, choisit l'action qui a la meilleure note
		int[] action_choice = new int[4];
		double note = -1; //on est s�r qu'elle prendra l'une des valeur des notes des actions
		ArrayList<int[]> actions_possibilities = actions.actions;
		ArrayList<Double> grades = actions.grades;
		Iterator<Double> it_grades = grades.iterator();
		Iterator<int[]> it_actions = actions_possibilities.iterator();
		while (it_actions.hasNext()) { // Recherche de la meilleure action <-> recherche de grade maximal (note max en fait)
			int[] action_possibility = it_actions.next();
			double grade  =it_grades.next();
			if (note < grade) {
				note = grade;
				for (int i = 0; i < action_possibility.length -1; i++) {
					action_choice[i]=action_possibility[i];
				}
			}
			
		}
		return action_choice;
	}
	
	public int[] IA_random(Actions_graded actions) {  //choisi au hasard une action parmi celles possibles
		int[] action_choice = new int[4];
		double note = -1; //on est s�r qu'elle prendra l'une des valeur des notes des actions
		ArrayList<int[]> actions_possibilities = actions.actions;
		ArrayList<Double> grades = actions.grades;
		Iterator<Double> it_grades = grades.iterator();
		Iterator<int[]> it_actions = actions_possibilities.iterator();
		int Size = actions_possibilities.size();
		int random_int = (int) Math.floor(9*Size*Math.random()/10);
		int[] action_possibility = it_actions.next();
		double grade  =it_grades.next();
		for (int i = 0; i < random_int; i++) {
			action_possibility = it_actions.next();
			grade  =it_grades.next();
			}
		action_choice = action_possibility;
		return action_choice;
	}

	public void play(TextField textField,Button button, Board plateau, GraphicsContext gc, Stage stage,TaskDisplay tsk) {
		// Main interaction method to fill the forms
		Nextsituations nextsituations = new Nextsituations(plateau.gamers[0], plateau);
		Actions_graded actions_notees = new Actions_graded( nextsituations, plateau.gamers[1]);
		int[] choix = this.IA_basique(actions_notees);
		textField.setText(Integer.toString(choix[0]));
		button.fire();
		textField.setText(Integer.toString(choix[1]));
		button.fire();
		textField.setText(Integer.toString(choix[2]));
		button.fire();
		textField.setText(Integer.toString(choix[3]));
		button.fire();
		System.out.println("ACTION RETENUE"+ "\n");
		System.out.println(this.tab_toString(choix));

	}
	
	public void play2(TextField textField,Button button, Board plateau, GraphicsContext gc, Stage stage,TaskDisplay tsk) {
		// Main interaction method to fill the forms
		ChoicesTree choicestree = new ChoicesTree(plateau,1);
		System.out.println(choicestree.racine.toString_boards()); // Affiche le plateau de d�part
		choicestree.AddLeaves(choicestree.racine, 3, 1);
		int[] choix = choicestree.action_retenue;
		textField.setText(Integer.toString(choix[0]));
		button.fire();
		textField.setText(Integer.toString(choix[1]));
		button.fire();
		textField.setText(Integer.toString(choix[2]));
		button.fire();
		textField.setText(Integer.toString(choix[3]));
		button.fire();
		System.out.println("ACTION RETENUE"+ "\n");
		System.out.println(this.tab_toString(choix));

	}
	
	//pour l'instant l'ia random est l'ia 1
	public void playrandom(TextField textField,Button button, Board plateau, GraphicsContext gc, Stage stage,TaskDisplay tsk) {
		// Main interaction method to fill the forms
		Nextsituations nextsituations = new Nextsituations(plateau.gamers[1], plateau);
		Actions_graded actions_notees = new Actions_graded( nextsituations, plateau.gamers[0]);
		int[] choix = this.IA_random(actions_notees);
		textField.setText(Integer.toString(choix[0]));
		button.fire();
		textField.setText(Integer.toString(choix[1]));
		button.fire();
		textField.setText(Integer.toString(choix[2]));
		button.fire();
		textField.setText(Integer.toString(choix[3]));
		button.fire();
		System.out.println("ACTION RETENUE"+ "\n");
		System.out.println(this.tab_toString(choix));

	}

	
	
	   String tab_toString(int[] tab) {
				String chaine = new String();
				for (int i : tab) {
					chaine += " "+ i;
				}
				return chaine;
			}

	
}














