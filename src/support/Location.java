/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.Collection;
import java.util.List;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author tarci
 */
public class Location{
   
    public int x;
    public int y;
    public int xRange;
    public int yRange;
    private double cost;
    private double priority;
    
    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public double getCost() {
        return cost;
    }

    public double getPriority() {
        return priority;
    }

     public int getxRange() {
        return xRange;
    }

    public void setxRange(int xRange) {
        this.xRange = xRange;
    }

    public int getyRange() {
        return yRange;
    }

    public void setyRange(int yRange) {
        this.yRange = yRange;
    }
    
//    @Override
//    protected Location clone() {
//        Location result = null;
//        try {
//            result = (Location) super.clone();
//        } catch (CloneNotSupportedException ex) {
//            Logger.getLogger(Location.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return result;
//    }    
    
    public Location(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Location(int x, int y, int xRange, int yRange)
    {
        this.x = x;
        this.y = y;
        this.xRange = xRange;
        this.yRange = yRange;
    }    

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public boolean Equals(Location loc){
        if(loc.getX()==this.getX() && loc.getY()==this.getY())
            return true;
        
        return false;
    }
 
    public boolean isOnList(List<Location> list) {
        for(Location item : list) {
            if(item != null && item.getX()==this.getX() && item.getY()==this.getY()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isOnList(Collection<Location> list) {
        for(Location item : list) {
            if(item != null && item.getX()==this.getX() && item.getY()==this.getY()) {
                return true;
            }
        }
        return false;
    } 
    
    public Location loadFromList(List<Location> list) {
        for(Location item : list) {
            if(item != null && item.getX()==this.getX() && item.getY()==this.getY()) {
                return item;
            }
        }
        return this;
    } 
    
    public int getIndexFromList(List<Location> list) {
        int result = -1;
        for(int i=0;i<list.size();i++) {
            if(list.get(i).getX()==this.getX() && list.get(i).getY()==this.getY()){
                result = i;
            }
        }
        return result;
    }     

    public boolean isOnMap(Map<Location, Double> map) {
        for(Map.Entry<Location, Double> item : map.entrySet()) {
            if(item != null && item.getKey().getX()==this.getX() && item.getKey().getY()==this.getY()) {
                return true;
            }
        }
        return false;
    } 
    
    public Location loadFromMap(Map<Location, Location> map) {
        for(Map.Entry<Location, Location> item : map.entrySet()) {
            if(item != null && item.getKey().getX()==this.getX() && item.getKey().getY()==this.getY()) {
                return item.getKey();
            }
        }
        return this;
    }     
    public Double getValueFromMap(Map<Location, Double> map) {
        for(Map.Entry<Location, Double> item : map.entrySet()) {
            if(item != null && item.getKey().getX()==this.getX() && item.getKey().getY()==this.getY()) {
                return item.getValue();
            }
        }
        return null;
    }      
}
