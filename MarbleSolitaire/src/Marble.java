import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import acm.graphics.GCompound;
import acm.graphics.GObject;
import acm.graphics.GOval;

public class Marble extends GCompound {
	
	private double radius;
	private int index;
	private double x,y;
	private boolean removed;
	private GOval ball;
	//private SolitairBoard board;

	
	public Marble(double radius) {
	      ball = new GOval(2 * radius, 2 * radius);
	      ball.setFilled(true);
	      add(ball, -radius, -radius);
	      removed=false;
	      markAsComplete();
	}
	
	public Marble(double radius,Color clr) {
	      GOval ball = new GOval(2 * radius, 2 * radius);
	      ball.setFilled(true);
	      ball.setFillColor(clr);
	      add(ball, -radius, -radius);
	      removed=false;
	      markAsComplete();
	
	}
	
	public boolean marbleRemoved() {
		return removed;
	}
	
	public void setMarbleRemoved(boolean flag) {
		removed = flag;
	}
	
	public void set_color(Color clr) {
		ball.setColor(clr);
	}
	
	
	/*public double getX() {
		return super.getX()+this.radius;
	}
	
	public double getY() {
		return super.getY()+this.radius;
	}
	
	public void setLocation(double x, double y) {
		super.setLocation(x-this.radius, y-this.radius);
	}*/
	
	public double getRadius() {
		return radius;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int ix) {
		index = ix;
	}

	

	
	

}
