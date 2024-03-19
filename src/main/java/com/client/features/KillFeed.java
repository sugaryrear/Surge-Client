package com.client.features;

import com.client.Client;

public class KillFeed {

    /**
     * The amount of experience gained in the drop
     */
    private String killer;
    private String victim;
    private String style;

    private int yPosition = START_Y;


    public static final int END_Y = 41;

    public static final int START_Y = 150;


    public static final int EXCESS_DISTANCE = 314 - START_Y;

    private static final int INTERVAL = 1;


    public KillFeed(String killer, String victim, String style) {
        this.killer = killer;
        this.victim = victim;
        this.style = style;
    }


    public void pulse() {
        yPosition -= (int) Math.ceil((double) INTERVAL * 50d / (double) Math.max(Client.instance.getFPS(), 1));
        if (yPosition < END_Y) {
            yPosition = -1;
        }
    }

    public void increasePosition(int offset) {
        yPosition += offset;
    }

    public byte getTransparency() {
        int halfway = START_Y / 2;
        if (yPosition > halfway) {
            return 100;
        }
        byte percentile = (byte) ((1D / 55D) * 100);
        if (percentile < 0) {
            return 0;
        }
        return (byte) Math.abs((percentile * yPosition));
    }

    public final String getKiller() {
        return killer;
    }
    public final String getVictim() {
        return victim;
    }
    public final String getStyle() {
        return style;
    }


    public final int getYPosition() {
        return yPosition;
    }


}
