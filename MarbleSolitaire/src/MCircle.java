import java.awt.Color;

import acm.graphics.GCompound;
import acm.graphics.GOval;

public class MCircle extends GCompound{
	
	public MCircle(double radius) {
	      GOval ball = new GOval(2 * radius, 2 * radius);
	      add(ball, -radius, -radius);
	      markAsComplete();
	}
	
	public MCircle(double radius, boolean set_filled) {
	      GOval ball = new GOval(2 * radius, 2 * radius);
	      ball.setFilled(set_filled);
	      add(ball, -radius, -radius);
	      markAsComplete();
	}
	
	public MCircle(double radius, Color clr) {
	      GOval ball = new GOval(2 * radius, 2 * radius);
	      ball.setFilled(true);
	      ball.setFillColor(clr);
	      
	      add(ball, -radius, -radius);
	      markAsComplete();
	}
	

}
