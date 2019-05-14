package game;


import javafx.application.Application;
import javafx.stage.Stage;
import 	javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.*;
import javafx.scene.paint.Color ;
import javafx.geometry.*;
public class Sprite {
	double width;
	double height;
	int x;
	int y;
	Color color;
	Sprite(int x,int y,double width, double height, Color color){
		this.width = width;
		this.height = height;
		this.color = color;
		this.x = x;
		this.y = y;
	}
	
	void render(GraphicsContext gc){
		gc.setFill(this.color);
		gc.fillRect(x, y, height, width);
	}
	
	void update(Color color) {
		this.color = color;
	}
	
}