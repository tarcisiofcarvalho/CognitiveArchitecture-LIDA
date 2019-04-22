package modules;

import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import support.AStarSupport;
import support.GridMap;
import support.Location;
import ws3dproxy.CommandUtility;
import ws3dproxy.WS3DProxy;
import ws3dproxy.model.Creature;
import ws3dproxy.model.Leaflet;
import ws3dproxy.model.Thing;
import ws3dproxy.model.World;
import ws3dproxy.util.Constants;

public class Environment extends EnvironmentImpl {

    private static final int DEFAULT_TICKS_PER_RUN = 100;
    private int ticksPerRun;
    private WS3DProxy proxy;
    private Creature creature;
    private Thing food;
//    private Thing jewel;
    private List<Thing> thingAhead;
//    private Thing leafletJewel;
    private String currentAction;
    private Boolean nextStep;
    private double nextCellX ;
    private double nextCellY;
    
    public Environment() {
        this.ticksPerRun = DEFAULT_TICKS_PER_RUN;
        this.proxy = new WS3DProxy();
        this.creature = null;
//        this.food = null;
//        this.jewel = null;
        this.thingAhead = new ArrayList<>();
//        this.leafletJewel = null;
        this.nextStep=true;
        this.currentAction = "gotoNextStep";
        
        // Temporary action
        nextCellX = 734.0;
        nextCellY = 231.0;
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
            
            // Set a jewel as target
            World.createJewel(0,500,50);
            
            //World.grow(1);
            /*
            Scenario definition
            */
            
            //Grid map test part: Start
            World ws = proxy.getWorld();
            List<Location> locList = new ArrayList<>();
            for(int x=1;x<=16;x++){ // x loop
                for(int y=1;y<=16;y++){ // y loop
                    locList.add(new Location(x, y, x*50, y*50)); // creating grid
                }
            }
            GridMap grid = new GridMap(16, 16, locList, null);
            System.out.println("Grid map created...");

            // AStart search
            AStarSupport astar = new AStarSupport();
            List<Location> path = astar.AStarSearch(grid, 
                              grid.getGridCreaturePosition(creature.getPosition().getX(), creature.getPosition().getY()), 
                              grid.getGridCreaturePosition(700, 500));
            System.out.println("Path planning created...");
            for(Location loc: path)
                System.out.println("step:" + loc.getX() + " / " + loc.getY());
                        
            //Location loc = grid.getGridCreaturePosition(creature.getPosition().getX(), creature.getPosition().getY());
            //System.out.println("creature grid pos: " + loc.getX() + "," + loc.getY());
            
            
            //Grid map test part: End
            //System.out.println("ws.getDeliverySpot()" + ws.getDeliverySpot().getX() + "," + ws.getDeliverySpot().getY());
            //System.out.println("creature" + creature.getPosition().getX() + "," + creature.getPosition().getX());
            //ws.getDeliverySpot()
            //System.out.println("height" + ws.getEnvironmentHeight());
            //System.out.println("width" + ws.getEnvironmentWidth());
         
            
            
            
            
            Thread.sleep(4000);
            creature.updateState();
            System.out.println("DemoLIDA has started...");
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        currentAction = "gotoNextStep"; // go to target
    }

//    public void finishState() {
//       currentAction = "completed";
//       System.out.println("The missing was accomplished.");
//       try 
//       {
//          Thread.sleep(4000); //ck
//       } catch (Exception e){
//          e.printStackTrace();
//       }
//       System.exit(1);
//    }
    
    @Override
    public Object getState(Map<String, ?> params) {
        Object requestedObject = null;
        String mode = (String) params.get("mode");
        switch (mode) {
//            case "food":
//                requestedObject = food;
//                break;
//            case "jewel":
//                requestedObject = jewel;
//                break;
//            case "thingAhead":
//                requestedObject = thingAhead;
//                break;
//            case "leafletJewel":
//                requestedObject = leafletJewel;
//                break;
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
        food = null;
//        jewel = null;
//        leafletJewel = null;
        thingAhead.clear();
        nextStep = true;
        
        for (Thing thing : creature.getThingsInVision()) {
            
            if(thing.getCategory()==Constants.categoryBRICK
                    && creature.calculateDistanceTo(thing)<=Constants.OFFSET){
                
                System.out.println("Brick ahead");
                
            }else{
                nextStep = true;
            }
//            if (creature.calculateDistanceTo(thing) <= Constants.OFFSET) {
//                // Identifica o objeto proximo
//                thingAhead.add(thing);
//                break;
//            } else if (thing.getCategory() == Constants.categoryJEWEL) {
//                if (leafletJewel == null) {
//                    // Identifica se a joia esta no leaflet
//                    for(Leaflet leaflet: creature.getLeaflets()){
//                        if (leaflet.ifInLeaflet(thing.getMaterial().getColorName()) &&
//                                leaflet.getTotalNumberOfType(thing.getMaterial().getColorName()) > leaflet.getCollectedNumberOfType(thing.getMaterial().getColorName())){
//                            leafletJewel = thing;
//                            break;
//                        }
//                    }
//                } else {
//                    // Identifica a joia que nao esta no leaflet
//                    jewel = thing;
//                }
//            } else if (food == null && creature.getFuel() <= 300.0
//                        && (thing.getCategory() == Constants.categoryFOOD
//                        || thing.getCategory() == Constants.categoryPFOOD
//                        || thing.getCategory() == Constants.categoryNPFOOD)) {
//                
//                    // Identifica qualquer tipo de comida
//                    food = thing;
//            }
            // Add here the nextStep logic
           
        }
    }
    
    @Override
    public void processAction(Object action) {
        String actionName = (String) action;
        currentAction = actionName.substring(actionName.indexOf(".") + 1);
    }

    private void performAction(String currentAction) {
        try {
            //System.out.println("Action: "+currentAction);
            switch (currentAction) {
//                case "rotate":
//                    creature.rotate(1.0);
//                    //this.resetState();
//                    //CommandUtility.sendSetTurn(creature.getIndex(), -1.0, -1.0, 3.0);
//                    break;
//                case "gotoFood":
//                    if (food != null) 
//                        creature.moveto(3.0, food.getX1(), food.getY1());
//                        //CommandUtility.sendGoTo(creature.getIndex(), 3.0, 3.0, food.getX1(), food.getY1());
//                    break;
//                case "gotoJewel":
//                    if (leafletJewel != null)
//                        creature.moveto(3.0, leafletJewel.getX1(), leafletJewel.getY1());
//                        //CommandUtility.sendGoTo(creature.getIndex(), 3.0, 3.0, leafletJewel.getX1(), leafletJewel.getY1());
//                    break;     
                case "gotoNextStep":
                    //if (leafletJewel != null)
                    //creature.rotate(1.0);
                        creature.moveto(3.0, nextCellX, nextCellY);
                        //CommandUtility.sendGoTo(creature.getIndex(), 3.0, 3.0, nextCellX, nextCellY);
                    //break;                    
//                case "get":
//                    creature.move(0.0, 0.0, 0.0);
//                    //CommandUtility.sendSetTurn(creature.getIndex(), 0.0, 0.0, 0.0);
//                    if (thingAhead != null) {
//                        for (Thing thing : thingAhead) {
//                            if (thing.getCategory() == Constants.categoryJEWEL) {
//                                creature.putInSack(thing.getName());
//                            } else if (thing.getCategory() == Constants.categoryFOOD || thing.getCategory() == Constants.categoryNPFOOD || thing.getCategory() == Constants.categoryPFOOD) {
//                                creature.eatIt(thing.getName());
//                            }
//                        }
//                    }
//                    
//                    //ck add finishState method
//                    this.resetState();
//                    break;                   
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
