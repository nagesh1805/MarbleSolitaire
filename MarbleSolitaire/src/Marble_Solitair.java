import acm.program.GraphicsProgram;
import acm.util.RandomGenerator;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JLabel;

import acm.graphics.GCompound;
import acm.graphics.GLabel;
import acm.graphics.GMath;
import acm.graphics.GObject;
import acm.graphics.GOval;
import acm.graphics.GPoint;


public class Marble_Solitair extends GraphicsProgram{
	
	private SolitairBoard board;

	private final int WINDOW_WIDTH=900;
	private final int WINDOW_HEIGHT=700;
	private JButton solve_for_me, new_puzzle;
	private static  RandomGenerator rgen=RandomGenerator.getInstance();
	private ArrayList<GPoint> boarderStripLocs;
	private ArrayList<Marble> removedList;
	double board_cx,board_cy;
	public static int rm_marble_ix=0,score;
	private GPoint mvng_mrbl_strt_loc,mvng_mrbl_end_loc;
	private double r,m,rr;
	public static Color WHITE_COLOR;
	public int[] marble_colors= {0xFF0000,0xD2691E,0x0000FF,0xFFFF00,0x66CDAA,0xFF8C00,0x800000,0x808000,0x008000,0x99004C,0x008080,
			0xD2B48C,0xCD5C5C,0xFF4500,0xB8860B,0x7B68EE,0x556B2F,0x32CD32,0x20B2AA,0x2F4F4F,0x000000,0x40E0D0,0x4682B4,0x191970,
			0x8A2BE2,0x8B008B,0x696969,0xC71585,0x778899,0xBC8F8F,0x5F9EA0,0x8FBC8F};
	private static int color_ix=0;
	
	public static AudioClip blopClip,errClip;
	private URL blopClipURL,errClipURL;
	private HashMap<Integer,Boolean> board_representation;
	private ArrayList<String> moveHistory;
	private ArrayList<Integer> exploredBoards;
	private GLabel noSolLbl;
	private JLabel time_h_lbl,time_lbl,score_h_lbl,score_lbl;
	private long t_min,t_sec,timeSec;
	private static long startTime;
	private static long currTime;
	
	private Marble rm_marble;
	private static boolean rm_marble_flag=false;
	
	private boolean start_solving;
	private int rm_ix;
	
	

	
	public void init() {
		setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		blopClipURL= getClass().getResource("Blop4.wav");
		blopClip=Applet.newAudioClip(blopClipURL);
		
		errClipURL= getClass().getResource("computerError.wav");
		errClip=Applet.newAudioClip(errClipURL);
		//board_representation = new HashMap<Integer, Boolean>();
		
		time_h_lbl=new JLabel("Time: ");
		//score_h_lbl.setFont("TimesRoman-BOLD-15");
		add(time_h_lbl,NORTH);
		time_lbl= new JLabel("00 : 00");
		add(time_lbl,NORTH);
		
		add(new JLabel("                    "),NORTH);
		
		solve_for_me=new JButton("Solve");
		solve_for_me.setToolTipText("Solve for me");
		//solve_for_me.setHorizontalAlignment(40);
		new_puzzle = new JButton("New");
		new_puzzle.setToolTipText("Get new game");
		add(solve_for_me,NORTH);
		add(new JLabel("                    "),NORTH);
		add(new_puzzle,NORTH);
		add(new JLabel("                    "),NORTH);
		
		
		score_h_lbl = new JLabel("Score:");
		//score_h_lbl.setFont("TimesRoman-BOLD-15");
		add(score_h_lbl,NORTH);
		
		score_lbl = new JLabel(""+score);
		//score_lbl.setColor(Color.BLUE);
	//	score_lbl.setFont("TimesRoman-BOLD-15");
		add(score_lbl,NORTH);
		

		
		

		// Set Up marbles
		setUp();
		

		

	}
	
	public void setUp() {
		noSolLbl = new GLabel("");
		
		noSolLbl.setColor(Color.RED);
		noSolLbl.setFont("TimesRoman-BOLD-30");
		add(noSolLbl,280,650);

		
		
        r=300;
		m=65;
		rr=55;
		start_solving=false;
		WHITE_COLOR= new Color(255,215,0);
		

		board = new SolitairBoard(r);
		
		board_cx=100+r;
		board_cy=10+r;
		add(board,board_cx,board_cy);

		
		exploredBoards  = new ArrayList<Integer>();
		moveHistory = new ArrayList<String>();
		removedList = new ArrayList<Marble>();
		boarderStripLocs=ringLocations(r, rr/2);
		double x1=this.getX()+335;
		double y1=this.getY()+115;

		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				add(new MCircle(rr/2),x1+j*m,y1+i*m);
				board.index_loc.put((i+1)*10+(j+1),new GPoint(x1+j*m,y1+i*m));
			}
		}
		double x2=this.getX()-2*m +335;
		double y2=this.getY()+2*m+115;
		for(int i=0;i<3;i++) {
			for(int j=0;j<7;j++) {
				add(new MCircle(rr/2),x2+j*m,y2+i*m);
				board.index_loc.put(((i+3)*10+j+1),new GPoint(x2+j*m ,y2+i*m ));
			}
		}
		double x3=this.getX()+335;
		double y3=this.getY()+5*m+115;
		for(int i=0;i<2;i++) {
			for(int j=0;j<3;j++) {
				add(new MCircle(rr/2),x3+j*m,y3+i*m);
				board.index_loc.put(((i+6)*10+j+1),new GPoint(x3+j*m,y3+i*m));
			}
		}
		
		for(Entry<Integer, GPoint> entry: board.getIndex_loc_map().entrySet()) {
			if(!(entry.getKey()==44)) {
				Color clr = new Color(marble_colors[color_ix++]);
				Marble marble=new Marble(55/2.0,clr );
				marble.setIndex(entry.getKey());
				board.getIndex_marble_map().put(entry.getKey(), marble);
				//Marble marble=new Marble(55/2.0);
				marble.setLocation(entry.getValue().getX(),entry.getValue().getY());
				add(marble);
			}

		}
		
		for(GPoint pt : boarderStripLocs) {
			add(new MCircle(55/2),pt);
		}
		
		
	}
	
	public void excuteMoves(ArrayList<String> mvs) {
		
			for(String mv : mvs) {
				
	    		String[] ixs =mv.split("-");
	    		int ix1 =Integer.parseInt(ixs[0]);
	    		int ix2 = Integer.parseInt(ixs[1]);
	    		GPoint p1= board.index_loc.get(ix1);
	    		GPoint p2= board.index_loc.get(ix2);
	    		GPoint mid_pt = midPoint(p1,p2);
	    		int ix1_5 = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
	    		
	    		Marble mrbl_strt =board.index_marble.get(ix1);
	    		mrbl_strt.setColor(WHITE_COLOR);
	    		mrbl_strt.sendToFront();
	    		double l1x= p1.getX();
	    		double l1y = p1.getY();
	    		double l2x=p2.getX();
	    		double l2y =p2.getY();
	    		if(l1x == l2x) {
		    		for(int i=1;i<=30;i++) {
		    			mrbl_strt.setLocation(l1x, l1y+(i/30.0)*(l2y-l1y));
		    			pause(10);
		    		}
	    		}
	    		else {
		    		for(int i=1;i<=30;i++) {
		    			mrbl_strt.setLocation(l1x+(i/30.0*(l2x-l1x)), l1y);
		    			pause(10);
		    		}
	    		}

	    		Marble rm_mrbl = board.index_marble.get(ix1_5);
	    		rm_mrbl.sendToFront();
	    		GPoint loc =boarderStripLocs.get(rm_marble_ix++);
	    		double loc1x = mid_pt.getX();
	    		double loc1y = mid_pt.getY();
	    		double loc2x = loc.getX();
	    		double loc2y = loc.getY();
	    				
	    		for(int j=1;j<=50;j++) {
	    			rm_mrbl.setLocation(loc1x+(j/50.0)*(loc2x -loc1x), loc1y+(j/50.0)*(loc2y-loc1y));
	    			pause(10);
	    		}
	    	
	            board.index_marble.get(ix1_5).setLocation(loc);
		    	board.index_marble.get(ix1_5).setMarbleRemoved(true);
		    	removedList.add(board.index_marble.get(ix1_5));
		    	board.index_marble.remove(ix1_5);
		    	mrbl_strt.setLocation(board.index_loc.get(ix2));
		    	mrbl_strt.setIndex(ix2);
		    	board.index_marble.put(ix2,mrbl_strt );
		    	mrbl_strt.setColor(Color.BLACK);
		    		
		    	pause(100);
	    
	
			}
	
		

	}
		

	
	public void run() {
		addMouseListeners();
		addActionListeners();
		startTime=System.currentTimeMillis();
	    
		while(true) {
			currTime=System.currentTimeMillis();
			timeSec=(currTime-startTime)/1000;
			t_min=(timeSec/60) ;
			t_sec=timeSec%60;
    		if((t_min/10 == 0)&&(t_sec/10 ==0)) {
	    	    time_lbl.setText("0"+t_min+" : "+"0"+t_sec);
    		}
    		else if((t_min/10 >0)&&(t_sec/10 ==0)) {
    			 time_lbl.setText(t_min+" : "+"0"+t_sec);
    		}
    		else if((t_min/10 == 0)&&(t_sec/10 >0)) {
   			 time_lbl.setText("0"+t_min+" : "+t_sec);
    		}
    		else {
    			time_lbl.setText(t_min+" : "+t_sec);
    		}
    		
    		if(rm_marble_flag) {
	    		//Marble rm_mrbl = board.index_marble.get(rm_ix);
	    		//int rm_ix = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
	    		rm_marble.sendToFront();
	    		GPoint loc1=board.index_loc.get(rm_ix);
	    		GPoint loc2 =boarderStripLocs.get(rm_marble_ix);
	    		double loc1x = loc1.getX();
	    		double loc1y = loc1.getY();
	    		double loc2x = loc2.getX();
	    		double loc2y = loc2.getY();
	    				
	    		for(int j=1;j<=50;j++) {
	    			rm_marble.setLocation(loc1x+(j/50.0)*(loc2x -loc1x), loc1y+(j/50.0)*(loc2y-loc1y));
	    			pause(5);
	    		}
	    		rm_marble_flag=false;
	    		rm_marble_ix++;
    		}
    		
			if(start_solving) {
		    	
				noSolLbl.setColor(Color.BLUE);
				noSolLbl.setLabel("Please wait ...");
				if(solvePuzzle()) {
					//System.out.println(moveHistory);
					noSolLbl.setLabel("");
					excuteMoves(moveHistory);
				}
				else {
					noSolLbl.setColor(Color.RED);
					//System.out.println("No solution");
					noSolLbl.setLabel("Sorry! No Solution...");
				}
				start_solving=false;
				new_puzzle.setEnabled(true);
			}
			pause(10);
		}

		
		
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("Solve")) {
			new_puzzle.setEnabled(false);
			solve_for_me.setEnabled(false);
			score_h_lbl.setVisible(false);
			score_lbl.setVisible(false);
			time_h_lbl.setVisible(false);
			time_lbl.setVisible(false);
			//System.out.println("Solve Clicked");
			board_representation = new HashMap<Integer, Boolean>();
			for( Entry<Integer, GPoint> entry: board.getIndex_loc_map().entrySet()) {
				   if(board.getIndex_marble_map().containsKey(entry.getKey())) {
					  board_representation.put(entry.getKey(),true);
				   }
				   else {
					   board_representation.put(entry.getKey(),false);
				   }
		    }
			start_solving=true;
		}
		else if(e.getActionCommand().equals("New")) {
			startTime=System.currentTimeMillis();
			score_h_lbl.setVisible(true);
			score_lbl.setVisible(true);
			time_h_lbl.setVisible(true);
			time_lbl.setVisible(true);
			removeAll();
			color_ix=0;
			rm_marble_ix=0;
			score=0;
			score_lbl.setText(""+score);
			setUp();
			solve_for_me.setEnabled(true);
			
			
		}

	}
	

public boolean validMove(int ix1, int ix2) {
		GPoint p1= board.index_loc.get(ix1);
		GPoint p2= board.index_loc.get(ix2);
		GPoint mid_pt = midPoint(p1,p2);
		int ix1_5 = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
		
		
		if( ((ix1_5 != -1)&& board_representation.get(ix1) && board_representation.get(ix1_5) && (!board_representation.get(ix2)) && GMath.distance(p1.getX(), p1.getY(), p2.getX(), p2.getY())<=130) 
			&&  board.index_loc.containsKey(ix1) && board.index_loc.containsKey(ix2) && ((p1.getX()==p2.getX())||(p1.getY()==p2.getY()))) {
		//	System.out.println("hool");
			
			if(!board_representation.get(ix2) && (ix1_5 !=-1)&&(board_representation.get(ix1_5))) {
				//System.out.println("hbol");
				return true;
			}
			

	
			
		}
		return false;

		 
		
	}

   public void makeMove(int ix1, int ix2) {
		GPoint p1= board.index_loc.get(ix1);
		GPoint p2= board.index_loc.get(ix2);
		GPoint mid_pt = midPoint(p1,p2);
		int ix1_5 = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
		
	   board_representation.put(ix2, true);
	   board_representation.put(ix1, false);
	   board_representation.put(ix1_5, false);
	   
   }
   
   public void undoMove(int ix1, int ix2) {
	   
		GPoint p1= board.index_loc.get(ix1);
		GPoint p2= board.index_loc.get(ix2);
		GPoint mid_pt = midPoint(p1,p2);
		int ix1_5 = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
	   
	   board_representation.put(ix2, false);
	   board_representation.put(ix1, true);
	   board_representation.put(ix1_5, true);
   }
   
   public int compressBoard() {
	   int encoding = 0;
	   int place=0;
	   
	   for( Entry<Integer, GPoint> entry: board.getIndex_loc_map().entrySet()) {
		   if(board_representation.get(entry.getKey())) {
			   encoding |= (1<<place);
			   place++;
		   }
		   else {
			   place++;
		   }
	   }
	   return encoding;
   }
   
   int marblesLeft() {
	   int count=0;
	   for( Entry<Integer, Boolean> entry: board_representation.entrySet()) {
		   if(entry.getValue()) {
			   count++;
		   }
	    }
	   return count;
   }
   
   public ArrayList<String> findAllPossibleMoves(){
	   ArrayList<String> validMoves = new ArrayList<String>();
	   for( Entry<Integer, Boolean> entry_i: board_representation.entrySet()) {
		   for( Entry<Integer, Boolean> entry_j: board_representation.entrySet()) {
			   
			
			   
			   if((entry_i.getKey() != entry_j.getKey()) && validMove(entry_i.getKey(),entry_j.getKey()) ) {
					   validMoves.add(entry_i.getKey()+"-"+entry_j.getKey());
					 //  System.out.println("ix1="+entry_i.getKey()+" , ix2="+entry_j.getKey());
			
				  

			   }
		    }
	    }
	   return validMoves;
   } 
   public boolean solvePuzzle() {

	   if(marblesLeft()==1) {
		   exploredBoards.add(compressBoard());
		   if(board_representation.get(44)) {
			   return true;
		   }
		   else{
			   return false;
		   }
		   
	   }
	   else {
		    if(! exploredBoards.contains(compressBoard())) {
		    	exploredBoards.add(compressBoard());
		    	ArrayList<String> fpmvs = findAllPossibleMoves();
		    	for( String mv : fpmvs) {
		    		String[] ixs =mv.split("-");
		    		int ix1 =Integer.parseInt(ixs[0]);
		    		int ix2 = Integer.parseInt(ixs[1]);
		    		makeMove(ix1,ix2);
		    		moveHistory.add(mv);
		    		if(solvePuzzle()) {
		    			return true;
		    		}
		    		else {
		    			undoMove(ix1,ix2);
		    			moveHistory.remove(moveHistory.size()-1);
		    			
		    		}
		    		//System.out.println(mv);
		    	}
		    	
		    	
		    }
		    return false;
	   }
   }
	
	
	
	
	public Marble getMarbleAtPress(SolitairBoard board, double ex, double ey) {

		for(Entry<Integer, Marble> entry: board.getIndex_marble_map().entrySet()) {
			if(entry.getValue().contains(ex, ey)){
				return entry.getValue();
			}

		}
		
		return null;
		
	}
	
	public int getIndexatLoc(double x, double y) {
		for(Entry<Integer, GPoint> entry: board.getIndex_loc_map().entrySet()) {
			if(GMath.distance(x, y, entry.getValue().getX(), entry.getValue().getY())< rr/2){
				return entry.getKey();
			}
		}
		
		return -1;
		
	}

	
	public boolean validMoveIndexPair(GPoint p1, GPoint p2) {
		
		GPoint mid_pt = midPoint(p1,p2);
		int ix0 = getIndexatLoc(p1.getX(), p1.getY());
		int ix1 = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
		int ix2 = getIndexatLoc(p2.getX(), p2.getY());
		
		//if()
		
		if((GMath.distance(p1.getX(), p1.getY(), p2.getX(), p2.getY())<=130) &&  board.index_loc.containsKey(ix0) && board.index_loc.containsKey(ix2) && ((p1.getX()==p2.getX()) || (p1.getY()==p2.getY()) ) ) {
			//System.out.println("hool"); (Math.abs(p1.getY()-p2.getY()) <= 75)   &&(Math.abs(p1.getX()-p2.getX()) <= 75) )
			if(!board.index_marble.containsKey(ix2) && (board.index_marble.containsKey(ix1))) {
				//System.out.println("hbol"); 
				return true;
			}
			
		}
		return false;

		 
		
	}
	
	public GPoint midPoint(GPoint p1, GPoint p2) {
		return new GPoint((p1.getX()+p2.getX())/2 ,(p1.getY()+p2.getY())/2);
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {

		// TODO Auto-generated method stub
		if(! start_solving) {
			Marble mrbl =getMarbleAtPress(board, e.getX(), e.getY()); 
			if((mrbl != null)&&(!mrbl.marbleRemoved()) ) {
				mvng_mrbl_strt_loc=mrbl.getLocation();
				board.setMovingMarble(mrbl);
				board.setmarblePicked(true);
				board.getMovingMarble().setColor(WHITE_COLOR);
				board.getMovingMarble().sendToFront();
			}
		}



	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
		if(board.getMarblepicked()&& (!start_solving)) {
			mvng_mrbl_end_loc=board.getMovingMarble().getLocation();

			int ix = getIndexatLoc(mvng_mrbl_end_loc.getX(), mvng_mrbl_end_loc.getY());
			
			if((ix != -1)&&validMoveIndexPair(mvng_mrbl_strt_loc,board.index_loc.get(ix))) {
				//System.out.println("Boo..");
				
				GPoint mid_pt = midPoint(mvng_mrbl_strt_loc, board.index_loc.get(ix));
				rm_ix = getIndexatLoc(mid_pt.getX(), mid_pt.getY());
				if(board.index_marble.containsKey(rm_ix)) {
				//if(!board.index_marble.get(rm_ix).marbleRemoved()) {
				//	board.index_marble.get(rm_ix).setMarbleRemoved(true);
					//board.index_marble.get(rm_ix).setMarbleRemoved(true);
				//	board.index_marble.get(rm_ix).setLocation(boarderStripLocs.get(rm_marble_ix));
					board.getMovingMarble().setLocation(board.index_loc.get(ix));
					rm_marble = board.index_marble.get(rm_ix);
					rm_marble_flag=true;
					
					board.getMovingMarble().setIndex(ix);
					removedList.add(board.index_marble.get(rm_ix));
					board.index_marble.remove(rm_ix);
					board.index_marble.remove(getIndexatLoc(mvng_mrbl_strt_loc.getX(),mvng_mrbl_strt_loc.getY()));
					board.index_marble.put(ix, board.getMovingMarble());
					
					
					score=score+10;
					score_lbl.setText(""+score);
					blopClip.play();
					
				}
				else {
					board.getMovingMarble().setLocation(mvng_mrbl_strt_loc);
					errClip.play();
				}
				
			}
			else {
				board.getMovingMarble().setLocation(mvng_mrbl_strt_loc);
				errClip.play();
			}
			
			board.setmarblePicked(false);
			board.getMovingMarble().setColor(Color.BLACK);
		}
		
		//System.out.println(compressBoard());


	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println("Hi1");
		if(board.getMarblepicked() &&(!start_solving)) {
		//	System.out.println("Hi2");
			board.getMovingMarble().setLocation(e.getX(), e.getY());
	
		}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	

	
	public ArrayList<GPoint> ringLocations(double r,double mr){
		
		ArrayList<GPoint> ringLocs= new ArrayList<GPoint>();
		int size=31;
		for(int i=0;i<size;i++) {
			double x=board_cx+(r-mr)*GMath.cosDegrees((GMath.toDegrees((2*Math.PI*i)/size)));
			double y=board_cy+(r-mr)*GMath.sinDegrees((GMath.toDegrees((2*Math.PI*i)/size)));
			ringLocs.add(new GPoint(x,y));
			
			
		}
		return ringLocs;
	}
	


}
