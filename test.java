package game;

import java.util.ArrayList;

public class test {
	public test() {
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test tata  = new test();
		Board plateau = new Board(3,2,2,8,5);
		System.out.println(plateau.toString());
		Nextsituations nextsituations = plateau.next_positions(plateau.gamers[0]) ;
		System.out.println(nextsituations.toString_boards());
	}

}
