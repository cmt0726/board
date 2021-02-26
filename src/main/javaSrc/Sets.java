package javaSrc;
import java.util.HashMap;

public class Sets {

    HashMap<String, Integer> sceneShotCounter;
    HashMap<String, String[]> neighbors;
    HashMap<String, String[][]> locationRoleData;


    public Sets(HashMap<String, Integer> scene, 
               HashMap<String, String[]> neighborsInc,
               HashMap<String, String[][]> roleData){
        sceneShotCounter = new HashMap<String, Integer>(scene);
        neighbors = neighborsInc;
        locationRoleData = roleData;
    }

    public void decreaseShotCount(String set){

        sceneShotCounter.put(set, Integer.valueOf(sceneShotCounter.get(set) - 1));
        
    }
}
