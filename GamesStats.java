package game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class GamesStats {
	int[] res;
	int nb = 3;
	static int idgagnant;
	public static void main(String[] args) throws Exception {
		GamesStats gamestats = new GamesStats();
		for(int i=0; i <gamestats.nb;i++) {
			Game game = new Game();
			game.gamestats = gamestats;
			Application.launch(Game.class);
			gamestats.res[idgagnant]+=1;
			System.out.println("testttttttttt");
		}
		System.out.println(gamestats.res[0]);
		System.out.println(gamestats.res[1]);
		
	}

}
