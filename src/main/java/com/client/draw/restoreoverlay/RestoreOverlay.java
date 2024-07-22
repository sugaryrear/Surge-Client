package com.client.draw.restoreoverlay;


import com.client.Client;
import com.client.Rasterizer2D;
import com.client.model.Skill;

import java.util.HashMap;
import java.util.Map;

public class RestoreOverlay {

    private Client client;

    public RestoreOverlay(Client client) {
        this.client = client;
    }
    public static class Listof {
        private final String name;
        private final int skillid;
        private final int currentlevel;
        private final int maxlevel;

        public Listof(String name, int skillid, int currentlevel, int maxlevel){
            this.name = name;
            this.skillid = skillid;
            this.currentlevel = currentlevel;
            this.maxlevel = maxlevel;
        }
        public String getName(){
            return name;
        }
        public int getSkillId(){
            return skillid;
        }
        public int getCurrentLevel(){
            return currentlevel;
        }
        public int getMaxLevel(){
            return maxlevel;
        }
    }
    public Map<Integer,Listof> boostedskills = new HashMap<>();

    public void update() {


        for(int i = 0; i < client.maxStats.length; i++){
            if(client.currentStats[i] > client.maxStats[i]){
                if (!boostedskills.containsKey(i)) {
boostedskills.put(i, new Listof(Skill.SKILL_NAMES_ORDER[i],i,client.currentStats[i],client.maxStats[i]));

                } else {
boostedskills.replace(i, boostedskills.get(i), new Listof(Skill.SKILL_NAMES_ORDER[i],i,client.currentStats[i],client.maxStats[i]));

                }
                //boostedskills.put()
            } else {
                if (boostedskills.containsKey(i)) {
                    boostedskills.remove(i);
                }
                }
        }

    }

    public  void drawrestoreOverlay() {
        if(!client.isstatoverlayEnabled()){
            return;
        }
        if(client.openInterfaceID != -1) {
            return;
        }
if(boostedskills.isEmpty()){
    return;
}



        int x = 6;

        int y = 87;

        int drawingHeight = 9;
        int yposition = 14;


        if(boostedskills.size() < 1)
            return;


        String thetimer = (client.calculateRestore(client.getSecondsTimer().secondsRemaining()));
        client.newRegularFont.drawBasicString("Next + restore in     "+thetimer, x+1, y+yposition, 0xFFFFFF, 0x000000);
        for (Map.Entry<Integer, Listof> entry : boostedskills.entrySet()) {

            yposition+=15;
            client.newRegularFont.drawBasicString(entry.getValue().getName(), x+1, y+(yposition), 0xFFFFFF, 0x000000);
            client.newRegularFont.drawBasicString("@gre@"+entry.getValue().getCurrentLevel()+"@whi@/"+entry.getValue().getMaxLevel(), x+83, y+(yposition), 0xFFFFFF, 0x000000);

        }

        Rasterizer2D.draw_filled_rect(x, y, 129, drawingHeight +(yposition), 0x000000, 50);
        Rasterizer2D.drawRectangle((x)-1, y-1, 129, drawingHeight+(yposition), 0x696969);

    }

}
