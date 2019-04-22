/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author tarci
 */
public class GridMap {
    
    private Location[] directions = null; 
    private List<Location> walls = null;
    private List<Location> cells = null;    
    private int width, height;
    
    public GridMap(int width, int height, List<Location> cells, List<Location> wall){
        
        this.directions = new Location[4];
        this.directions[0] = new Location(1, 0);
        this.directions[1] = new Location(0, -1);
        this.directions[2] = new Location(-1, 0);
        this.directions[3] = new Location(0, 1); 
        this.width = width;
        this.height = height;
        this.cells = cells;
        this.walls = wall;

    }
    
    public boolean inBound(Location location){
        return 0 < location.getX() && location.getX() <= width
            && 0 < location.getY() && location.getY() <= height;     
    }
    
    public double Cost(Location a, Location b)
    {
        return b.isOnList(walls) ? 5 : 1;
        //return Math.sqrt( Math.pow((b.getX()-a.getX()),2) + Math.pow((b.getY()-a.getY()),2) );
        
    }
    
    public boolean hasWall(Location location){
        return !location.isOnList(walls);        
    }    
    
    public List<Location> neighbors(Location location){
        List<Location> neighbors = new ArrayList<>();
        for(Location dir : this.directions){
            Location next = new Location(location.getX()+dir.getX(), location.getY()+dir.getY());
            if(inBound(next)&&hasWall(next)){
                neighbors.add(next);
            }
        }
        return neighbors;
    }
    
    /**
     * Method to get creature grid position    
     * @param x
     * @param y
     * @return cell id
    */
    public Location getGridCreaturePosition(double x, double y){
        for (Location cell : cells) {
            if( (x <= cell.getxRange() && x>= (cell.getxRange()-50))
               && (y <= cell.getyRange() && y>= (cell.getyRange()-50))){
                return cell;
            }
        }
        return null;
    }
}
