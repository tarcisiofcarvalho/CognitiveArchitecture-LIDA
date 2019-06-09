/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
//import java.util.PriorityQueue;

/**
 *
 * @author tarci
 */
public class AStarSupport {
    
    public List<Location> listTo = new ArrayList<>();
    public List<Location> listFrom = new ArrayList<>();
    public List<Double> listG = new ArrayList<>();
    public List<Double> listF = new ArrayList<>();

    private double heuristicDistance(Location a, Location b)
    {
        return Math.sqrt( Math.pow((b.getX()-a.getX()),2) + Math.pow((b.getY()-a.getY()),2) );
    }
    
    public List<Location> AStarSearch(GridMap graph, Location start, Location goal)
    {
        
        PriorityQueue<Location> frontier = new PriorityQueue<Location>(new Comparator<Location>() {
            @Override
            public int compare(Location locA, Location locB) {
                if(locA.getPriority() < locB.getPriority()){
                    return -1;
                }else if(locA.getPriority() > locB.getPriority()){
                    return 1;
                }else{
                    return 0;
                }               
            }
        });

        frontier.add(start);
        
        start.setCost(0);

        while (frontier.size() > 0)
        {
            Location current = frontier.poll();

            if (current.Equals(goal))
            {
                break;
            }

            for(Location next : graph.neighbors(current)){
                //next = next.loadFromList(this.listTo);
                int iCurrent = current.getIndexFromList(listTo);
                int iNext = next.getIndexFromList(listTo);
                double newCost;
                if(iCurrent > -1){
                    newCost = listG.get(iCurrent) + graph.Cost(current, next); // Calc G
                }else{
                    newCost = graph.Cost(current, next); // Calc G
                }
                if(iNext == -1){ // Node not visited
                    next.setPriority(newCost + heuristicDistance(next, goal)); // Queue priority
                    frontier.add(next); // Closed list
                    this.listFrom.add(current); // From Location
                    this.listTo.add(next); // To Location
                    this.listG.add(newCost); // G - Cost so far
                    this.listF.add(newCost + heuristicDistance(next, goal)); // F (Cost so far plus heristic distance)                
                }else if(newCost < listG.get(iNext)){
                    next.setPriority(newCost + heuristicDistance(next, goal)); // Update F 
                    frontier.add(next); // Add to closed list again
                    this.listG.set(iNext, newCost); // G - Cost so far
                    this.listF.set(iNext, (newCost + heuristicDistance(next, goal))); // F (Cost so far plus heristic distance)
                }
            }
        }

	    int iPath = -1;
//	    System.out.println(">>>> Print list >>>>");
	    for(int i=0;i<this.listTo.size();i++) {
	    	if(this.listTo.get(i).Equals(goal))
	    		iPath = i;
	        	System.out.println("To: " + this.listTo.get(i).getX() + "," + this.listTo.get(i).getY() +
	                           " From: " + this.listFrom.get(i).getX() + "," + this.listFrom.get(i).getY() +
	                           " G: " + this.listG.get(i) + 
	                           " F: " + this.listF.get(i));
	
	    }
        // Prepare path
        List<Location> path = new ArrayList<>();
        //int iPath = this.listTo.size()-1;
        path.add(this.listTo.get(iPath));
        path.add(this.listFrom.get(iPath));
        while(true){
            iPath = this.listFrom.get(iPath).getIndexFromList(listTo);
            path.add(this.listFrom.get(iPath));
            if(this.listFrom.get(iPath).Equals(start)){
                break;
            }
        }
        List<Location> pathReverse = path.subList(0, path.size());
        Collections.reverse(pathReverse);        
        
//        for(Location steps : pathReverse){
//            System.out.println("from: " + steps.getX() + "," + steps.getY());
//        }        
        return pathReverse;
    }
}
