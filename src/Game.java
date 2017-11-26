import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent; 
import java.awt.image.BufferedImage; 
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame; 

/** 
 * Main class for the game 
 */ 
public class Game extends JFrame
{        
    /*
     * environment constants, these are immutable
     */
	private static final long serialVersionUID = 6766820551844969787L;

	/*
	 * Variable set by selected level
	 */
	private static int chosenLevel = 1;
	static final int totalFlies = 0;
	private Hud hud = new Hud();
	static int fliesCollected = 0;
	
	protected String title;

	protected Level currentLevel;
	
	private boolean isRunning = false; 
    private int fps = 60; 
    private int frame = 0;
    private GraphicsRenderer graphicsRenderer = new GraphicsRenderer(this);
    private Webly player1;
    
    private Insets insets; 
    private InputHandler input; 

    public static void main(String[] args) 
    { 
    	Game game = new Game(); 
    	game.initialize(); 
        game.run();
        System.exit(0);
        
    } 
       
    public void loadLevel(int level)
    {
    	chosenLevel = level;
    	currentLevel = LevelFactory.INSTANCE.getLevel();
    	setTitle(currentLevel.getTitle()); 
	    player1 = new Webly(new Vector2(320,300), new Vector2(0, 0), 0.1, 25, 25, 0.0, "");//webly-sprites_small.png
    	currentLevel.addCollidableObject(player1);     
	    isRunning = true;
	    graphicsRenderer.setWorld(1).setLevel(level);
    }
    
    
    public void showStartupScreen()
    {
    	loadLevel(1);
    	
    }
    
    /** 
     * This method starts the game and runs it in a loop 
     */ 
    public void run() 
    { 
    	
                
        while(isRunning) 
        { 
        	ArrayList<Contact> contacts;
        	long time = System.currentTimeMillis(); 
            frame++;
            if(frame == 61)
            	frame = 1;
            contacts = update();
            graphicsRenderer.draw(contacts);
            //draw(); 
                        
            //  delay for each frame  -   time it took for one frame 
            time = (1000 / fps) - (System.currentTimeMillis() - time); 
            if (time > 0) 
            { 
            	try 
                { 
            		Thread.sleep(time); 
                } 
                catch(InterruptedException e){} 
            } 
        } 

         setVisible(false); 

     } 
        
     /** 
      * This method will set up everything need for the game to run 
      */ 
    
     void initialize() 
     { 
    	 setTitle(title); 
	     setSize(graphicsRenderer.getWindowWidth(), graphicsRenderer.getWindowHeight()); 
	     setResizable(false); 
	     setDefaultCloseOperation(EXIT_ON_CLOSE); 
	     setVisible(true);      
	     Insets insets = getInsets(); 
	     setSize(insets.left + graphicsRenderer.getWindowWidth() + insets.right, 
	     insets.top + graphicsRenderer.getWindowHeight() + insets.bottom); 
	     //loadLevel();
	     //backBuffer = new BufferedImage(windowWidth, windowHeight, BufferedImage.TYPE_INT_RGB); 
	     input = new InputHandler(this); 
	     showStartupScreen();

     } 
     
     private ArrayList<Contact> update()
     {
    	 ArrayList<Contact> contacts = new ArrayList<Contact>();
    	 
         if (input.isKeyDown(KeyEvent.VK_LEFT)) 
         { 
        	 player1.move(true);
         }
         else
         {
        	 player1.changeVelocity(new Vector2(0,player1.vel.y));
         }

         if (input.isKeyDown(KeyEvent.VK_RIGHT)) 
         { 
        	 player1.move(false);
         }  
    	 
    	 
    	 for(PhysicalObject o : currentLevel.getCollidableObjects())
    	 {
    		 if ( o.invMass > 0 )
    		 {
    				o.vel = Vector2.add(o.vel, Constants.GRAVITY );
    		 }
    		 for(PhysicalObject t : currentLevel.getCollidableObjects())
    		 {
    		   if(o != t)
    		   {
    		     contacts.addAll(o.getClosestPoints(t));
    		   }
    		 }
    	 }

    	 ContactSolver.solve(contacts,1);
    	 for(PhysicalObject o : currentLevel.getCollidableObjects())
    	 {
    		 o.integrate(1);
    	 }   
    	 return contacts;
     }
     
     public static int getChosenLevel()
     {
    	 return chosenLevel;
     }
     
     public int currentFrame()
     {
    	 return frame;
     }
} 
