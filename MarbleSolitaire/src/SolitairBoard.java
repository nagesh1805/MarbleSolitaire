import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import acm.graphics.GCompound;
import acm.graphics.GMath;
import acm.graphics.GOval;
import acm.graphics.GPoint;
import acm.util.RandomGenerator;
import acmx.export.java.util.Map;
public class SolitairBoard  extends GCompound{
	MCircle background;
	MCircle strip;
	private Marble movingMarble ;
	private Marble recently_deleted_marble;
	private GPoint recently_deleted_marble_loc;
	private int recently_deleted_ix;
	private boolean marblePicked;
	HashMap<Integer,GPoint> index_loc;
	HashMap<Integer,Marble> index_marble;
	
	private GPoint[] rm_marble_locs;
	private double board_radius;
	
	private static  RandomGenerator rgen=RandomGenerator.getInstance();
	
	public SolitairBoard(double r) {
		board_radius=r;
		//rm_marble_locs=restLocs();
		background= new MCircle(r,new Color(0x8B4513));
		//background= new GOval(2*r,2*r);
		strip= new MCircle(r-r/5,new Color(0xFFEFD5));
		//background.setFilled(true);
		//background.setFillColor(Color.CYAN);
		add(background);
		add(strip);
		index_loc = new HashMap<Integer,GPoint>();
		index_marble= new HashMap<Integer,Marble>();
	/*	double x1=this.getX()+3.1*r/4;
		double y1=this.getY()+r/3;
		int m=65;
		int rr=55;
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				add(new MCircle(rr/2),x1+j*m,y1+i*m);
				index_loc.put((i+1)*10+(j+1),new GPoint(x1+j*m,y1+i*m));
			}
		}
		double x2=this.getX()+3.1*r/4-2*m;
		double y2=this.getY()+r/3+2*m;
		for(int i=0;i<3;i++) {
			for(int j=0;j<7;j++) {
				add(new MCircle(rr/2),x2+j*m,y2+i*m);
				index_loc.put(((i+3)*10+j+1),new GPoint(x2+j*m ,y2+i*m ));
			}
		}
		double x3=this.getX()+3.1*r/4;
		double y3=this.getY()+r/3+5*m;
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				add(new MCircle(rr/2),x3+j*m,y3+i*m);
				index_loc.put(((i+6)*10+j+1),new GPoint(x3+j*m,y3+i*m));
			}
		}
		
		for(Entry<Integer, GPoint> entry: getIndex_loc_map().entrySet()) {
			if(!(entry.getKey()==44)) {
				Color clr = new Color(rgen.nextInt(100)+100,rgen.nextInt(100),rgen.nextInt(100));
				Marble marble=new Marble(55/2.0,clr );
				marble.setIndex(entry.getKey());
				getIndex_marble_map().put(entry.getKey(), marble);
				//Marble marble=new Marble(55/2.0);
				marble.setLocation(entry.getValue().getX(),entry.getValue().getY());
				add(marble);
			}

		}*/
		
	}
	
	public HashMap<Integer,GPoint> getIndex_loc_map() {
		return index_loc;
	}
	
	public HashMap<Integer, Marble> getIndex_marble_map() {
		return index_marble;
	}
	
	public Marble getMovingMarble() {
		return movingMarble;
	}
	
	public void setMovingMarble(Marble mrbl) {
		movingMarble=mrbl;
	}
	public boolean getMarblepicked() {
		return marblePicked;
	}
	
	public void setmarblePicked(boolean flag) {
		marblePicked=flag;
	}
	
	public int marblesLeft() {
		
		return 0;
	}
	

	
/*	public boolean inBounds(int r, int c) {
		if((r==1 || r==2|| r==6 || r==7) && (c>=1 && c<=3)) {
			return true;
		}
		else if((r==3 || r==4 || r==5)&&(c>=1 && c<=7) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isMarbleAt(int r,int c) {
		 if (getElementAt(index_loc.get(r*10+c).getX(),index_loc.get(r*10+c).getY()) instanceof Marble ) {
			 return true;
		 }
		 else {
			 return false;
		 }
	}
	
	public boolean ismoveLeftLegal(Marble mrbl) {
		int ix=mrbl.getIndex();
		if (inBounds(ix/10 -2,ix%10) && ( isMarbleAt(ix/10 -1,ix%10)) && ( isMarbleAt(ix/10 -2,ix%10))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean ismoveRightLegal(Marble mrbl) {
		int ix=mrbl.getIndex();
		if (inBounds(ix/10 +2,ix%10) && ( isMarbleAt(ix/10 +1,ix%10)) && ( isMarbleAt(ix/10 +2,ix%10))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean ismoveUpLegal(Marble mrbl) {
		int ix=mrbl.getIndex();
		if (inBounds(ix/10,ix%10 -2) && ( isMarbleAt(ix/10,ix%10 -1)) && ( isMarbleAt(ix/10,ix%10 -2))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean ismoveDownLegal(Marble mrbl) {
		int ix=mrbl.getIndex();
		if (inBounds(ix/10,ix%10 +2) && ( isMarbleAt(ix/10,ix%10 +1)) && ( isMarbleAt(ix/10,ix%10 +2))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void moveLeft(Marble mrbl) {
		int ix =mrbl.getIndex();
		Marble mb = index_marble.get((ix/10-1)*10+ix%10);
		//Marble mb=(Marble) getElementAt(index_loc.get((ix/10-1)*10+ix%10));
		recently_deleted_marble_loc=index_loc.get((ix/10-1)*10+ix%10);
		recently_deleted_marble=mb;
		recently_deleted_ix=(ix/10-1)*10+ix%10;
		index_marble.remove((ix/10-1)*10+ix%10);
		mb.setLocation(rm_marble_locs[rm_marble_ix++]);
		mrbl.setLocation(index_loc.get(index_loc.get((ix/10-2)*10+ix%10)));
		mrbl.setIndex((ix/10-2)*10+ix%10);
		
	}
	
	public void moveRight(Marble mrbl) {
		int ix =mrbl.getIndex();
		Marble mb = index_marble.get((ix/10+1)*10+ix%10);
	//	Marble mb=(Marble) getElementAt(index_loc.get((ix/10+1)*10+ix%10));
		recently_deleted_marble_loc=index_loc.get((ix/10+1)*10+ix%10);
		recently_deleted_marble=mb;
		recently_deleted_ix=(ix/10+1)*10+ix%10;
		index_marble.remove((ix/10+1)*10+ix%10);
		mb.setLocation(rm_marble_locs[rm_marble_ix++]);
		mrbl.setLocation(index_loc.get(index_loc.get((ix/10+2)*10+ix%10)));
		mrbl.setIndex((ix/10+2)*10+ix%10);
	}
	
	public void moveUp(Marble mrbl) {
		int ix =mrbl.getIndex();
		Marble mb = index_marble.get((ix/10)*10+ix%10-1);
		//Marble mb=(Marble) getElementAt(index_loc.get((ix/10)*10+ix%10-1));
		
		recently_deleted_marble_loc=index_loc.get((ix/10)*10+ix%10-1);
		recently_deleted_marble=mb;
		recently_deleted_ix=(ix/10)*10+ix%10-1;
		index_marble.remove((ix/10)*10+ix%10-1);
		mb.setLocation(rm_marble_locs[rm_marble_ix++]);
		mrbl.setLocation(index_loc.get(index_loc.get((ix/10)*10+ix%10-2)));
		mrbl.setIndex((ix/10)*10+ix%10-2);
	}
	
	public void moveDown(Marble mrbl) {
		int ix =mrbl.getIndex();
		Marble mb = index_marble.get(index_loc.get((ix/10)*10+ix%10+1));
		//Marble mb=(Marble) getElementAt(index_loc.get((ix/10)*10+ix%10+1));
		
		recently_deleted_marble_loc=index_loc.get((ix/10)*10+ix%10+1);
		recently_deleted_marble=mb;
		recently_deleted_ix=(ix/10)*10+ix%10+1;
		index_marble.remove((ix/10)*10+ix%10+1);
		mb.setLocation(rm_marble_locs[rm_marble_ix++]);
		mrbl.setLocation(index_loc.get(index_loc.get((ix/10)*10+ix%10+2)));
		mrbl.setIndex((ix/10)*10+ix%10+2);
	}*/
	

	
/*	public String compressBoard() {
		String s="";
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				if(index_marble.containsKey((i+1)*10+(j+1))) {
					s=s+"1";
				}
				else {
					s=s+"0";
				}
			}
		}
		for(int i=0;i<3;i++) {
			for(int j=0;j<7;j++) {
				if(index_marble.containsKey((i+3)*10+j+1)) {
					s=s+"1";
				}
				else {
					s=s+"0";
				}

			}
		}
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				if(index_marble.containsKey((i+6)*10+j+1)) {
					s=s+"1";
				}
				else {
					s=s+"0";
	
			   }
	
			}
		}
		
		return s;
	}
	
	ArrayList<String> findAllPossibleMoves(){
		ArrayList<String> all_pos_mvs= new ArrayList<String>();
		for(Entry<Integer, Marble> entry: getIndex_marble_map().entrySet()) {
			
			if(ismoveLeftLegal(entry.getValue())) {
				all_pos_mvs.add(""+entry.getKey()+"-"+(entry.getKey()-2));
			}
			if(ismoveRightLegal(entry.getValue())) {
				all_pos_mvs.add(""+entry.getKey()+"-"+(entry.getKey()+2));
			}
			if(ismoveUpLegal(entry.getValue())) {
				all_pos_mvs.add(""+entry.getKey()+"-"+((entry.getKey()/10-2)*10+entry.getKey()));
			}
			
			if(ismoveDownLegal(entry.getValue())) {
				all_pos_mvs.add(""+entry.getKey()+"-"+((entry.getKey()/10+2)*10+entry.getKey()));
			}

		}
		
		return all_pos_mvs;
		
	}*/
	
	/*public boolean solvePuzzle(ArrayList<String> exploredBoards,ArrayList<String> moveHistory) {
		
		if(index_marble.size()==1) {
			exploredBoards.add(compressBoard());
			return true;
		}
		
		else {
			if (!exploredBoards.contains(compressBoard())) {
				exploredBoards.add(compressBoard());
				ArrayList<String> all_mvs=findAllPossibleMoves();
				for(String mv : all_mvs) {
					 String[] from_to= mv.split("-");
					 int from = Integer.parseInt(from_to[0]);
					 int to = Integer.parseInt(from_to[1]);
					 if(from/10 > to/10) {
						 moveLeft(index_marble.get(from));
					 }
					 else if(from/10 < to/10) {
						 moveRight(index_marble.get(from));
					 }
					 else if(from%10 < to%10) {
						 moveDown(index_marble.get(from));
					 }
					 else {
						 moveUp(index_marble.get(from));
					 }
					 
					 moveHistory.add(mv);
					 
					 if(solvePuzzle(exploredBoards,moveHistory)) {
						 return true;
					 }
					 else {
						 recently_deleted_marble.setLocation(recently_deleted_marble_loc);
						 recently_deleted_marble.setIndex(recently_deleted_ix);
						 index_marble.put(recently_deleted_ix, recently_deleted_marble);
						 if(from/10 > to/10) {
							 moveRight(index_marble.get(to));
						 }
						 else if(from/10 < to/10) {
							 moveLeft(index_marble.get(to));
						 }
						 else if(from%10 < to%10) {
							 moveUp(index_marble.get(to));
						 }
						 else {
							 moveDown(index_marble.get(to));
						 }
						 
						 recently_deleted_marble.setLocation(recently_deleted_marble_loc);
						 recently_deleted_marble.setIndex(recently_deleted_ix);
						 index_marble.put(recently_deleted_ix, recently_deleted_marble);
						 moveHistory.remove(moveHistory.size()-1);
						 
					 }
				}
				
			}
			
		}
		return false;
	
	}
	
	public GPoint[] restLocs() {
		int  total=32;
		GPoint[] points=new GPoint[total];
		for(int k=0;k<total;k++) {
			double x=getWidth()/2+board_radius*GMath.cosDegrees((GMath.toDegrees((2*Math.PI*k)/total)));
			double y=getHeight()/2+board_radius*GMath.sinDegrees((GMath.toDegrees((2*Math.PI*k)/total)));
			GPoint point=new GPoint(x,y);
			points[k]=point;	
		}
		
		return points;
	}*/

}
