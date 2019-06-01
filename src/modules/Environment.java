package modules;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import support.AStarSupport;
import support.GridMap;
import support.Location;
import ws3dproxy.CommandUtility;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Thing;
import ws3dproxy.util.Constants;

public class Environment extends EnvironmentImpl {

    private static final int DEFAULT_TICKS_PER_RUN = 100;
    private int ticksPerRun;
    private WS3DProxy proxy;
    private Creature creature;
    private boolean thingAhead;
    private boolean goalCompleted;
    private String currentAction;
    private Boolean nextStep;
    private double nextCellX ;
    private double nextCellY;
    private AStarSupport astar;
    private GridMap grid;
    List<Location> path;
    List<Location> locList;
    
    public Environment() {
        this.ticksPerRun = DEFAULT_TICKS_PER_RUN;
        this.proxy = new WS3DProxy();
        this.creature = null;
        this.nextStep=true;
        this.currentAction = "gotoNextStep";
        
        // Temporary action
        nextCellX = 100.0;
        nextCellY = 100.0;
    }

    @Override
    public void init() {
        super.init();
        ticksPerRun = (Integer) getParam("environment.ticksPerRun", DEFAULT_TICKS_PER_RUN);
        taskSpawner.addTask(new BackgroundTask(ticksPerRun));
        
        try {
            System.out.println("Reseting the WS3D World ...");
            proxy.getWorld().reset();
            creature = proxy.createCreature(100,100, 0);
            creature.start();
            System.out.println("Starting the WS3D Resource Generator ... ");
            
            // --- Defining the target objective --- //
            CommandUtility.sendNewWaypoint(700.00, 300.00);
              
            // --- Defining Bricks - Case 1--- //
//            System.out.println(CommandUtility.sendNewBrick(0, 400, 50, 450, 250));
//            System.out.println(CommandUtility.sendNewBrick(0, 600, 200, 650, 600));
//            System.out.println(CommandUtility.sendNewBrick(0, 400, 400, 450, 600));

            // --- Defining Bricks - Case 2--- //
//            System.out.println(CommandUtility.sendNewBrick(0, 200, 50, 250, 250));
//            System.out.println(CommandUtility.sendNewBrick(0, 600, 200, 650, 600));
//            System.out.println(CommandUtility.sendNewBrick(0, 400, 150, 450, 600));
            
            // --- Defining Bricks - Case 3--- //
            System.out.println(CommandUtility.sendNewBrick(0, 200, 50, 250, 250));
            System.out.println(CommandUtility.sendNewBrick(0, 600, 200, 650, 600));
            System.out.println(CommandUtility.sendNewBrick(0, 400, 50, 450, 400));            
            // -- Generating the grid map location -- //
            locList = new ArrayList<>();
            for(int x=1;x<=16;x++){ // x loop
                for(int y=1;y<=16;y++){ // y loop
                    locList.add(new Location(x, y, x*50, y*50)); // creating grid
                }
            }
            
            // -- Generate the creature path to the target -- //
            this.createCreaturePath();
            
            System.out.println("Path planning created...");

            Thread.sleep(4000);
            creature.updateState();
            System.out.println("DemoLIDA has started...");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createCreaturePath() {
    	
        // --- Creating the Grid map cells --- //
        grid = new GridMap(16, 16, locList, this.getWalls(locList));
        System.out.println("Grid map created...");

        // --- Defining the best Creature path, to avoid bricks and reach the target objective --- //
        astar = new AStarSupport();
        Location target = grid.getGridCreaturePosition(700, 300);
        path = astar.AStarSearch(grid, 
                          grid.getGridCreaturePosition(creature.getPosition().getX(), creature.getPosition().getY()), 
                          target);
        System.out.println("Creature path created.....");
        
    }

    private List<Location> getWalls(List<Location> cells){
    	
		List<Location> walls = new ArrayList<Location>();
		
		try {
	        StringTokenizer stW = CommandUtility.sendGetWorldEntities();
			
			// -- Iterate over tokens looking for Brick things -- //
	        while(stW.hasMoreTokens()) {
	        	
	        	String temp_token = stW.nextToken();
	        	
	        	// -- Check if the next token is related to Brick thing -- /
	        	if(temp_token.length()>6) {
	        		if(temp_token.substring(0, 6).equals("Brick_")) {
	        			
	        			// -- skip first two tokens that are not related to X and Y position -- //
	    	        	stW.nextToken();	
	    	        	stW.nextToken();
	        			
	        			// -- Identify brick edges and parse it to grid cells--- //
	    	        	double x_A = Double.parseDouble(stW.nextToken());
	    	        	double x_B = Double.parseDouble(stW.nextToken());
	    	        	double y_A = Double.parseDouble(stW.nextToken());
	    	        	double y_B = Double.parseDouble(stW.nextToken());
	    	        	
	        			Location edgeA = this.getGridThingPosition(x_A, y_A, cells);
	        			Location edgeB = this.getGridThingPosition(x_B, y_B, cells);
	        			
	        			// -- Identify the number of x and y positions the brick covers -- //
	        			int xStart = 0;
	        			int xEnd = 0;
	        			int yStart = 0;
	        			int yEnd = 0;
	        			
	        			if(edgeB.getX()>=edgeA.getX()) {
	        				xStart=edgeA.getX();
	        				xEnd=edgeB.getX();
	        			}else {
	        				xStart=edgeB.getX();
	        				xEnd=edgeA.getX();            				
	        			}
	        			
	        			if(edgeB.getY()>=edgeA.getY()) {
	        				yStart=edgeA.getY();
	        				yEnd=edgeB.getY();
	        			}else {
	        				yStart=edgeB.getY();
	        				yEnd=edgeA.getY();            				
	        			}            			
	        			
	        			// -- Generating grid occupied cells based on bricks body -- //
	        			for(int x=xStart;x<=xEnd;x++) {
	        				for(int y=yStart;y<=yEnd;y++) {
	        					walls.add(new Location(x, y, x*50, y*50));
	        				}
	        			}
	        		}
	        	}
	        } 

		}catch (Exception e) {
			walls = null;
			e.printStackTrace();
		}
		
		return walls;
    }
    
    public Location getGridThingPosition(double x, double y, List<Location> cells){
        for (Location cell : cells) {
            if( (x <= cell.getxRange() && x>= (cell.getxRange()-50))
               && (y <= cell.getyRange() && y>= (cell.getyRange()-50))){
                return cell;
            }
        }
        return null;
    }    
    
    private Location getNextStep(double creature_x, double creature_y) {
    	
    	for(int i=0;i < path.size();i++) {
    		
    		double x = creature_x / 50;
    		double y = creature_y / 50;
    		int x_min = path.get(i).getX()-1;
    		int x_max = path.get(i).getX()+1;
    		int y_min = path.get(i).getY()-1;
    		int y_max = path.get(i).getY()+1;
    		
    		if((x >= x_min && x <= x_max) &&
    		   (y >= y_min && y <= y_max)) {
    		   if(i==path.size()-1) {
    			   return path.get(i);
    		   }else {
    			   return path.get(i+1);
    		   }
    		}
    	}
    	return null;
    }

    private Double calculateDistancedouble(double creatureX, double creatureY, double thingX, double thingY) {
		
    	Double distance = 0.0;

		if (creatureX > thingX) {
			distance += (creatureX - thingX) * (creatureX - thingX);
		} else {
			distance += (thingX - creatureX) * (thingX - creatureX);
		}

		if (creatureY > thingY) {
			distance += (creatureY - thingY) * (creatureY - thingY);
		} else {
			distance += (thingY - creatureY) * (thingY - creatureY);
		}

		return Math.sqrt(distance);
    }

    private class BackgroundTask extends FrameworkTaskImpl {

        public BackgroundTask(int ticksPerRun) {
            super(ticksPerRun);
        }

        @Override
        protected void runThisFrameworkTask() {
            updateEnvironment();
            performAction(currentAction);
        }
    }

    @Override
    public void resetState() {
        currentAction = "nextStep"; // go to target
    }

    @Override
    public Object getState(Map<String, ?> params) {
        Object requestedObject = null;
        String mode = (String) params.get("mode");
        switch (mode) {

            case "goalCompleted":
                requestedObject = goalCompleted;
                break;

            case "thingAhead":
        		requestedObject = thingAhead;
        		break;        
            
            case "nextStep":
                requestedObject = nextStep;
                break;                
            
            default:
                break;      
        }
        return requestedObject;
    }

    public void updateEnvironment() {
        creature.updateState();
        nextStep = false;
        thingAhead = false;
        goalCompleted = false;
        
        double distance = this.calculateDistancedouble(creature.getPosition().getX(), creature.getPosition().getY(), 700.0, 300.0);

        if(distance<=20) {
        	
        	goalCompleted = true;
        	
        }else {
        	
	        for (Thing thing : creature.getThingsInVision()) {
	            
	            if(thing.getCategory()==Constants.categoryBRICK && creature.calculateDistanceTo(thing)<=30){
	                
	                thingAhead = true;
	                this.createCreaturePath();
	                
	            }else{
	            	
	                nextStep = true;
	                
	            }
	           
	        }

        }
    }
        
    @Override
    public void processAction(Object action) {
        String actionName = (String) action;
        currentAction = actionName.substring(actionName.indexOf(".") + 1);
    }

    private void performAction(String currentAction) {
        try {
            switch (currentAction) {
             
            	case "goalCompleted":
            		
            		// -- Goal completed - Stop the creature -- //
                    creature.stop();
                    System.out.println("Goal completed.....");
	                try 
	                {
	                     Thread.sleep(4000);
	                } catch (Exception e){
	                     e.printStackTrace();
	                }
	                System.exit(1);
                    
	                break;
                
            	case "thingAhead":

            		// -- Move Back to avoid collision -- //
                	//creature.move(-7.0, 0.0, 0.0);
                	creature.rotate(5.0);
            		System.out.println("Re-calculating new path...");
                    break;     

            	case "nextStep":
            		
            		// -- Move next step -- //
            		Location loc = getNextStep(creature.getPosition().getX(), creature.getPosition().getY());

            		if(loc!=null) {
            			nextCellX = loc.getX()*50;
            			nextCellY = loc.getY()*50;
            		}else {
            			System.out.println("Loc null");
            		}
                    creature.moveto(1.0, nextCellX, nextCellY);

                    System.out.println("Go to next step: " + nextCellX + " x " + nextCellY);
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//  public void finishState() {
//  currentAction = "completed";
//  System.out.println("The missing was accomplished.");
//  try 
//  {
//     Thread.sleep(4000); //ck
//  } catch (Exception e){
//     e.printStackTrace();
//  }
//  System.exit(1);
//}

}
