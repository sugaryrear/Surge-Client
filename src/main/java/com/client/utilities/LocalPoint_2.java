package com.client.utilities;

public class LocalPoint_2 {

    private final int x;
    private final int y;
    public static final int SCENE_SIZE = 104;
    public static final int LOCAL_COORD_BITS = 7;
    public LocalPoint_2(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isInScene()
    {
        return x >= 0 && x < SCENE_SIZE << LOCAL_COORD_BITS
                && y >= 0 && y < SCENE_SIZE << LOCAL_COORD_BITS;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSceneX() {
        return x >>> 7;
    }

    public int getSceneY() {
        return y >>> 7;
    }
}
