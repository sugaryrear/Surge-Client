package com.client.draw.expcounter;



import com.client.*;

import java.awt.*;
import java.text.NumberFormat;
import java.util.*;


public class ExpCounter extends Client {

    private static final int START_SPRITE = 82;
    private static final int START = 130;
    private static final int STOP = 15;
    private static final int MIDLINE = (START + STOP) / 2;
    private static long xpCounter;
    private static final ArrayList<ExpGain> GAINS = new ArrayList<>();
    private static ExpGain currentGain = null;

    public static void addXP(int skill, long xp, boolean increment) {
        if (skill == 23) {//total level updating - dont do any sort of xp drops
            xpCounter = xp;
        } else {
            if (increment) {
                xpCounter += xp;
            }
            if (xp != 0) {
                if (currentGain != null && Math.abs(currentGain.getY() - START) <= getSize(1).baseCharacterHeight) {
                    currentGain.xp += xp;
                    currentGain.addSprite(skill);
                } else {
                    ExpGain gain = new ExpGain(skill, xp);
                    GAINS.add(gain);
                    currentGain = gain;

                }
            }
        }
    }

    public static int getSpriteBasedonSkill(int skill){
        switch(skill){
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;


            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;

            case 19:
                return 19;
            case 20:
                return 20;
            case 21:
                return 21;
            case 22:
                return 22;


            case 23:
                return 23;
        }
        return 23;
    }

    public static long getCurrentXPBasedonSkill(int skill){
        return skill == 23 ? xpCounter : Client.instance.currentExp[skill];

    }

    public static double getPercentagetillLevel(int skill){
        //int remainder = getRemaindertoLevel(skill);

        int numerator = skill == 23 ? Client.instance.sumofTotalLevel() : Client.instance.currentExp[skill] - Client.instance.getXPForLevel(Client.instance.maxStats[skill]);

        int denom = skill == 23 ? 2277: (Client.instance.getXPForLevel(Client.instance.maxStats[skill]+1) - Client.instance.getXPForLevel(Client.instance.maxStats[skill]));
        if(skill != 23 && Client.instance.maxStats[skill] >= 99){
            denom =1;
            numerator = 1;
        }
        double percentage =  (double) numerator / denom *  123.0D;
      //  System.out.println("P: "+percentage);
//System.out.println("xptolevel: "+(Client.instance.getXPForLevel(Client.instance.maxStats[skill]+1)+" remainder to level: "+getRemaindertoLevel(skill)+", subtraction: "+(getXPtoLevel(skill) - getRemaindertoLevel(skill))+" ");
        //System.out.println("percent: "+percentage);
        return percentage;
    }

    public static void drawExperienceCounter() {

        		if (!Client.instance.drawExperienceCounter || Client.instance.openInterfaceID != -1) {
			return;
		}

        boolean isFixed = !Client.instance.isResized();
        boolean inWild = Client.instance.inWild() || Client.instance.inRevs() ;
        int x_xpdrops = 0;
        int y = 12;
        int wildyoffset = 0;
        if(inWild){
            wildyoffset =200;
        }

        Rasterizer2D.drawTransparentBoxOutline( isFixed ? 382 - wildyoffset : (Client.instance.getCanvasWidth() - 366- wildyoffset ), 6, 127, 36, 0x41392c,250);

        Rasterizer2D.drawTransparentBox( isFixed ? 383 - wildyoffset  : (Client.instance.getCanvasWidth()- 365 - wildyoffset  ), 7, 125, 34, 0x383023,150);


        Client.instance.xpbar.drawSprite(isFixed? 396-13-wildyoffset : Client.instance.getCanvasWidth() - 365-wildyoffset, 33);


        int orange = new Color(255, 0, 0).getRGB();
        int yellow = new Color(255, 255, 0).getRGB();
        int green = new Color(55, 181, 20).getRGB();


        if (!GAINS.isEmpty()) {
            Iterator<ExpGain> gained = GAINS.iterator();

            while (gained.hasNext()) {


                ExpGain gain = gained.next();
                int theskill = gain.getSkill();
                String thexp = StringUtils.insertCommasToNumber(String.valueOf(getCurrentXPBasedonSkill(theskill)));

                Client.instance.xpbarsprites[getSpriteBasedonSkill(theskill)].drawSprite(isFixed ? 387-wildyoffset : Client.instance.getCanvasWidth() - 363-wildyoffset, 8);

                int amtToFill = (int)  getPercentagetillLevel(theskill);

                if(amtToFill >= 123)
                    amtToFill = 123;

                Client.instance.newRegularFont.drawBasicString(thexp, (isFixed ?  495-wildyoffset : Client.instance.getCanvasWidth() - 252-wildyoffset) - Client.instance.newRegularFont.getTextWidth(thexp), 24, 0xFFFFFF, 0x000000);

                if (amtToFill > 0) {
                    if (amtToFill <= 123 / 4) {//from 0 to 25% its orange
                        Client.instance.xpbartick.drawPixelsWithOpacity(orange, 34,  amtToFill, 6, 150, isFixed ? 384-wildyoffset : Client.instance.getCanvasWidth() - 364-wildyoffset);

                    } else if (amtToFill > 123 / 4 && amtToFill < 123) {
                        Client.instance.xpbartick.drawPixelsWithOpacity(yellow, 34,  amtToFill, 6, 180, isFixed ? 384-wildyoffset : Client.instance.getCanvasWidth()- 364-wildyoffset);

                    } else if (amtToFill >= 123) {
                        Client.instance.xpbartick.drawPixelsWithOpacity(green, 34,  amtToFill, 6, 200, isFixed ? 384-wildyoffset : Client.instance.getCanvasWidth() - 364-wildyoffset);



                    }
                }


                if (gain.getY() > -30) {

                    if (gain.getY() >= MIDLINE) {
                        gain.increaseAlpha();
                    } else {
                        gain.decreaseAlpha();
                    }

                    gain.changeY();

                } else if (gain.getY() <= -30) {
                    gained.remove();
                }

                if (gain.getY() > -30) {
                    Queue<ExpSprite> temp = new PriorityQueue<>(gain.sprites);
                    int dx = 0;
//                    String drop = String.format("<trans=%s>%,d", gain.getAlpha(), gain.getXP());
                    String drop = StringUtils.insertCommasToNumber(String.valueOf(gain.getXP()));
                    //   System.out.println("width of "+drop+" is "+Client.adv_font_small.get_width(drop));
                    x_xpdrops = (isFixed ? 507-wildyoffset : Client.instance.getCanvasWidth() - 246-wildyoffset) - Client.instance.newSmallFont.getTextWidth(drop);

                    while (!temp.isEmpty()) {
                        ExpSprite expSprite = temp.poll();
                       // dx -= 4;
                           dx -= expSprite.skill == 17 ? 26 : 17;// how far away from the xp drops do we drop the sprite?// it?
                   //     System.out.println("here: "+expSprite.skill);
                        expSprite.sprite.drawSprite(x_xpdrops + dx, (int) (y + gain.getY()));
                    }
                    Client.instance.newSmallFont.drawBasicString(drop, x_xpdrops, (int) (gain.getY() + y) + 14, 0xffffff, 0);

                }
            }
        }else {

            String thexp = StringUtils.insertCommasToNumber(String.valueOf(getCurrentXPBasedonSkill(23)));

            Client.instance.xpbarsprites[23].drawSprite(isFixed ? 387-wildyoffset : Client.instance.getCanvasWidth() - 363-wildyoffset, 8);

            int amtToFill = (int)  getPercentagetillLevel(23);



            if(amtToFill >= 123)
                amtToFill = 123;

            Client.instance.newRegularFont.drawBasicString(thexp, (isFixed ?  500-wildyoffset :Client.instance.getCanvasWidth() - 252-wildyoffset) - Client.instance.newRegularFont.getTextWidth(thexp), 24, 0xFFFFFF, 0x000000);

            if (amtToFill > 0) {
                if (amtToFill <= 123 / 4) {//from 0 to 25% its orange / red
                    Client.instance.xpbartick.drawPixelsWithOpacity(orange, 34,  amtToFill, 6, 150, isFixed ? 384-wildyoffset : Client.instance.getCanvasWidth() - 364-wildyoffset);

                } else if (amtToFill > 123 / 4 && amtToFill < 123) {
                    Client.instance.xpbartick.drawPixelsWithOpacity(yellow, 34,  amtToFill, 6, 180, isFixed ? 384-wildyoffset : Client.instance.getCanvasWidth()- 364-wildyoffset);

                } else if (amtToFill >= 123) {
                    Client.instance.xpbartick.drawPixelsWithOpacity(green, 34,  amtToFill, 6, 200, isFixed ? 384-wildyoffset : Client.instance.getCanvasWidth() - 364-wildyoffset);



                }
            }
        }
    }


    private static RSFont getSize(int size) {
        return Client.instance.newSmallFont;

    }

    static class ExpSprite implements Comparable<ExpSprite> {
        private int skill;
        private Sprite sprite;

        ExpSprite(int skill, Sprite sprite) {
            this.skill = skill;
            this.sprite = sprite;
        }

        @Override
        public int compareTo(ExpSprite other) {
            return Integer.signum(other.skill - skill);
        }
    }

    static class ExpGain {
        private int skill;
        private long xp;
        private float y;
        private double alpha = 0;
        private Set<ExpSprite> sprites = new TreeSet<>();

        ExpGain(int skill, long xp) {
            this.skill = skill;
            this.xp = xp;
            this.y = START;
            addSprite(skill);
        }
        int getSkill(){
            return skill;
        }
        void addSprite(int skill) {
            for (ExpSprite sprite : sprites) {
                if (sprite.skill == skill) {
                    return;
                }
            }
            sprites.add(new ExpSprite(skill, Client.instance.smallXpSprites[skill]));
        }

        void changeY() {
            y -=  1.0f;
        }

        long getXP() {
            return xp;
        }

        public float getY() {
            return y;
        }

        public int getAlpha() {
            return (int) alpha;
        }

        void increaseAlpha() {
            alpha += alpha < 256 ? 30 : 0;
            alpha = alpha > 256 ? 256 : alpha;
        }

        void decreaseAlpha() {
            alpha -= (alpha > 0 ? 30 : 0) * 0.10;
            alpha = alpha > 256 ? 256 : alpha;
        }
    }
}

