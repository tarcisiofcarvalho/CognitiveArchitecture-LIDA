/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tarci
 */
public class PriorityQueue {
    
    private Map<Location, Double> elements = new HashMap<Location, Double>();
    
    public int count(){
    
        return elements.size();
        
    }

    public void Enqueue(Location item, double priority)
    {
        elements.put(item, priority);
    }

    public Location Dequeue()
    {
       
       Map.Entry<Location,Double> bestItem = elements.entrySet().iterator().next();
       //System.out.println("Print queue in dequeue: " + elements.size());
       for(Map.Entry<Location,Double> item : elements.entrySet()){
           //System.out.println(item.getKey().getX() + "," + item.getKey().getY());
            if(bestItem.getValue()<item.getValue()){
                    bestItem = item;
            }
        }
        elements.remove(bestItem.getKey());
        return bestItem.getKey();
    }
}
