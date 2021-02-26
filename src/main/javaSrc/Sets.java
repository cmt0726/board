package javaSrc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Random;
import java.util.Set;



public class Sets {

    HashMap<String, Integer> sceneShotCounter;
    HashMap<String, String[]> neighbors;
    HashMap<String, String[][]> locationRoleData;
    HashMap<String, String[][]> locationCardRoleData = new HashMap<String, String[][]>();
    Cards card;
    int cardIdx = 0;


    public Sets(HashMap<String, Integer> scene, 
               HashMap<String, String[]> neighborsInc,
               HashMap<String, String[][]> roleData,
               Cards cData){
        sceneShotCounter = new HashMap<String, Integer>(scene);
        neighbors = neighborsInc;
        locationRoleData = roleData;
        card = cData;

    }

    public void decreaseShotCount(String set){

        sceneShotCounter.put(set, Integer.valueOf(sceneShotCounter.get(set) - 1));
        
    }

    public void resetShotCount(HashMap<String, Integer> scene){
        sceneShotCounter = new HashMap<String, Integer>(scene);
    }

    public void generateSceneCards(){

        //code copied from https://www.journaldev.com/32661/shuffle-array-java
        Random rand = new Random();
		
        int[] dataIdxs = new int[40];
        for(int i = 0; i < 40; i++)
            dataIdxs[i] = i;

		for (int i = 0; i < dataIdxs.length; i++) {
			int randomIndexToSwap = rand.nextInt(dataIdxs.length);
			int temp = dataIdxs[randomIndexToSwap];
			dataIdxs[randomIndexToSwap] = dataIdxs[i];
			dataIdxs[i] = temp;
		}

        //end copy
        Set<String> temp = locationRoleData.keySet();
        Iterator<String> temp1 = temp.iterator();
        int i = cardIdx;
        while(temp1.hasNext()){
            try{
                locationCardRoleData.put(temp1.next(), card.cardsData.get(i));
                i++;
                cardIdx++;
            }catch(Exception e){
                System.out.println("no more scenes");
            }
        }
        

    }
}
