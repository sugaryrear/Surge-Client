package com.client.graphics.textures;

public interface TextureLoader {

    int[] getTexturePixels(int var1);

    int getAverageTextureHSL(int var1);

    boolean isTransparent(int var1);

    boolean isLowDetail(int var1);

}
