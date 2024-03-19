package com.client.definitions.custom;


import com.client.definitions.AnimationDefinition;
import com.client.definitions.GraphicsDefinition;

public class GraphicsDefinitionCustom {
    public static void custom1(int j, GraphicsDefinition[] cache) {
        switch (j) {
            case 1729: // korasi
                cache[1729].modelId = 60776; //60776
                cache[1729].animationId = 9492;
                cache[1729].seqtype = AnimationDefinition.anims[9492]; //9492
                cache[1729].resizeXY = 60;
                cache[1729].resizeZ = 60;
                break;
            case 1730: // korasi
                cache[1730].modelId = 60776; //60776
                cache[1730].animationId = 9499;
                cache[1730].seqtype = AnimationDefinition.anims[9499]; //9492
                cache[1730].resizeXY = 60;
                cache[1730].resizeZ = 60;
                break;
            case 1731: // Battle korasi
                cache[1731].modelId = 60778; //60776
                cache[1731].animationId = 9492;
                cache[1731].seqtype = AnimationDefinition.anims[9492]; //9492
                cache[1731].resizeXY = 25;
                cache[1731].resizeZ = 25;
                break;
            case 1732: // Battle korasi
                cache[1732].modelId = 60778; //60776
                cache[1732].animationId = 9499;
                cache[1732].seqtype = AnimationDefinition.anims[9499]; //9492
                cache[1732].resizeXY = 40;
                cache[1732].resizeZ = 40;
                break;
            case 2240: // weird thing
                cache[2240].modelId = 55470;
                cache[2240].animationId = 9803;
                cache[2240].seqtype = AnimationDefinition.anims[9803]; //9492
//                cache[2451].resizeXY = 60;
//                cache[2451].resizeZ = 60;
                //cache[j].anInt413 = 40;
                break;
        }
    }
}
