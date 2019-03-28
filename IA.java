package game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class IA {
	int numjoueur; //IA id in the game
	int id; // Which IA is it ?
	
	IA(){
		
	}
	public int[] IA_basique(Actions_graded actions) {  //à partir d'une actions_graded, choisi l'action qui a la meilleure note
		int[] action_choice = new int[4];
		double note = -1; //on est sûr qu'elle prendra l'une des valeur des notes des actions
		ArrayList<int[]> actions_possibilities = actions.actions;
		ArrayList<Double> grades = actions.grades;
		Iterator<Double> it_grades = grades.iterator();
		Iterator<int[]> it_actions = actions_possibilities.iterator();
		while (it_actions.hasNext()) {
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
	
	public void play(TextField textField,Button button,HBox hbox, Board plateau, GraphicsContext gc, Stage stage) {
		// Main interaction method to communicate with UI and display
	}

	// We will probably add new methods to simplify play's code but the final structure will be next to this one
	
}














