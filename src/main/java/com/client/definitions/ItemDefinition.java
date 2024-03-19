package com.client.definitions;

import com.client.*;
import com.client.utilities.FileOperations;
import com.google.common.base.Preconditions;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSItemComposition;
import net.runelite.rs.api.RSIterableNodeHashTable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;

public final class ItemDefinition implements RSItemComposition {

    public static ReferenceCache sprites = new ReferenceCache(100);
    public static ReferenceCache models = new ReferenceCache(50);
    public static boolean isMembers = true;
    public static int totalItems;
    public static ItemDefinition[] cache;
   // public static int customspriteitems;
    public static int[] customspriteitems = { 31015,31017,31025,31026,31027,31028};
    private static int cacheIndex;
    private static Buffer1 item_data;
    private static int[] streamIndices;
    public int cost;
    public int[] colorReplace;
    public int id;
    public int[] colorFind;
    public boolean members;
    public int noted_item_id;
    public int wearpos = -1;
    public int wearpos2 = -1;
    public int wearpos3 = -1;
    public int womanwear2;
    public int manwear;
    public String[] options;
    public int xOffset2d;
    public String name;
    public int inventory_model;
    public int manhead;
    public boolean stackable;
    public int unnoted_item_id;
    public int zoom2d;
    public boolean customSprite = false;
    public int manwear2;
    public String[] interfaceOptions;
    public int xan2d;
    public int[] countObj;
    public int yOffset2d;//
    public int womanhead;
    public int yan2d;
    public int womanwear;
    public int[] countCo;
    public int team;
    public int zan2d;
    public String[] equipActions;
    public boolean tradeable;
    public int weight = 0;
    public HashMap<Integer, Object> params;
    public int glowColor = -1;
    private short[] textureReplace;
    private short[] textureFind;
    private int womanwear_offset = 0;
    private int womanwear3;
    private int manhead2;
    private int resizeX;
    private int womanhead2;
    private int contrast;
    private int manwear3;
    private int resizeZ;
    private int resizeY;
    private int ambient;
    private int manwear_offset = 0;
    private int shiftClickIndex = -2;
    private int category;
    private int bought_id;
    private int bought_template_id;
    private int placeholder_id;
    private int placeholder_template_id;
    private byte field2142;
    private byte field2157;
    private byte field2158;
    private int field2182;

    private ItemDefinition() {
        id = -1;
    }

    public static void clear() {
        models = null;
        sprites = null;
        streamIndices = null;
        cache = null;
        item_data = null;
    }

    public static void init(FileArchive archive) {
        item_data = new Buffer1(archive.readFile("obj.dat"));
        Buffer1 stream = new Buffer1(archive.readFile("obj.idx"));

        totalItems = stream.readUShort();
        streamIndices = new int[totalItems + 20_000];
        int offset = 2;

        for (int _ctr = 0; _ctr < totalItems; _ctr++) {
            streamIndices[_ctr] = offset;
            offset += stream.readUShort();
        }

        cache = new ItemDefinition[10];

        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new ItemDefinition();
        }

        System.out.println("Loaded: " + totalItems + " items");
    }
    private static ItemDefinition newCustomItems(int itemId) {
        ItemDefinition itemDef = new ItemDefinition();
        itemDef.setDefaults();
        switch (itemId) {
            case 30000:
                return copy(itemDef, 30_000, 11738, "Resource box(small)", "Open");
            case 30001:
                return copy(itemDef, 30_001, 11738, "Resource box(medium)", "Open");
            case 30002:
                return copy(itemDef, 30_002, 11738, "Resource box(large)", "Open");
            case 22375:
                return copy(itemDef, 22375, 22374, "Mossy key");
        }

        return null;
    }
    //                case 27660:
//                        itemDef.setDefaults();
//    itemDef.id = 27660;
//    itemDef.inventory_model = 47423;
//    itemDef.name = "@blu@Urasine Chainmace";
//    itemDef.resizeX = 128;
//    itemDef.resizeY = 128;
//    itemDef.resizeZ = 128;
//    itemDef.xan2d = 1495;
//    itemDef.yan2d = 256;
//    itemDef.zan2d = 0;
//    itemDef.wearpos = 3;
//    itemDef.wearpos2 = 0;
//    itemDef.wearpos3 = 0;
//    itemDef.cost = 175000;
//    itemDef.zoom2d = 1351;
//    itemDef.xOffset2d = 8;
//    itemDef.yOffset2d = -14;
//    itemDef.manwear = 47209;
//    itemDef.womanwear = 47209;
//    itemDef.interfaceOptions = new String[5];  /// 1 sec
//    itemDef.interfaceOptions[1] = "Wear";
//    itemDef.interfaceOptions[2] = "Teleports";
//    itemDef.interfaceOptions[3] = "Features";
//    itemDef.interfaceOptions[4] = "Drop";
//                break;
    private static void customItems(int itemId) {
        ItemDefinition itemDef = lookup(itemId);
     //   itemDef.countObj = null; // this is why u get 1gp stack size

        if(itemDef.name != null){
            if(itemDef.name.contains("armour set") || itemDef.name.contains("page set"))
                itemDef.interfaceOptions = new String[] {"Open",null, null, null, "Drop" };

        }

        switch (itemId) {

            case 31015:
                itemDef.name = "Inf run energy";
                itemDef.interfaceOptions = new String[] {"Redeem",null, null, null, "Drop" };
                itemDef.zoom2d = 1000;
                itemDef.customSprite = true;
                itemDef.createCustomSprite("runenergyitem.png");
                itemDef.stackable = false;
                break;
            case 31017:
             copy(itemDef, 31017, 4740, "7.62x39mm Bullets");
                itemDef.countCo = new int[] { 2, 3, 4, 5, 0, 0, 0, 0, 0, 0 };//the amounts at which it will change to the corresponding countObj
                itemDef.countObj = new int[] { 31025, 31026, 31027, 31028, 0, 0, 0, 0, 0, 0 };
                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
                itemDef.createCustomSprite("ak47bullet_1.png");
            break;
case 787:
                copy(itemDef,787,6199, "Resource Box");
                itemDef.interfaceOptions = new String[] {"Open",null, null, null, "Drop" };
break;
            case 31025:
                copy(itemDef, 31025, 4741, "7.62x39mm Bullets");
                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };

                itemDef.createCustomSprite("ak47bullet_2.png");
                break;
            case 31026:
                copy(itemDef, 31026, 4742, "7.62x39mm Bullets");
                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
                itemDef.createCustomSprite("ak47bullet_3.png");
                break;
            case 31027:
                copy(itemDef, 31027, 4743, "7.62x39mm Bullets");
                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
                itemDef.createCustomSprite("ak47bullet_4.png");
                break;
            case 31028:
                copy(itemDef, 31028, 4744, "7.62x39mm Bullets");
                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
                itemDef.createCustomSprite("ak47bullet_5.png");
                break;
            case 7292:
//             copy(itemDef, 31035, 7292, "Teleport Scroll");
                itemDef.name = "Teleport Scroll";
                itemDef.interfaceOptions = new String[] {"Redeem", null, null, null, "Drop" };
            break;
            case 27:
                itemDef.name = "Battlepass";
                itemDef.interfaceOptions = new String[] { "Unlock", null, null, "Check", "Drop" };
           break;
            case 31014:
                itemDef.setDefaults();
                itemDef.id = 31014;
                itemDef.inventory_model = 22165;
                itemDef.name = "Pack Yak";
                itemDef.zoom2d = 3200;
                itemDef.xan2d = 0;//this value  rotates it up/down
                itemDef.zan2d = 0;//this tilts it sideways (if negative)
              itemDef.yan2d =  300;
               itemDef.xOffset2d = 5;
                itemDef.yOffset2d = 5;
                itemDef.manwear = 23892;
                itemDef.womanwear = 23892;
                itemDef.countObj = null;
                itemDef.interfaceOptions = new String[] {null,null, null, null, "Drop" };
                break;
            case 31016:
                itemDef.setDefaults();

               itemDef.inventory_model = 64448;
                //           itemDef.maleModel = 51002;
                itemDef.manwear = 64449;
                itemDef.womanwear = 64449;
              //  itemDef.femaleModel = 51002;
                itemDef.zoom2d = 1700;//increase to zoom OUT
//                itemDef.xan2d = 500;//was 500
//                itemDef.zan2d = 300;//was 300
//                itemDef.yan2d = 300;//was 300
                itemDef.xan2d = 300;//was 500
                itemDef.zan2d = 0;//was 300
                itemDef.yan2d = 0;//was 300
                itemDef.yOffset2d = 0; //move model up and down
                itemDef.xOffset2d = 0; //move model r and l


                itemDef.stackable = false;
                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
            //  itemDef.createCustomSprite("ak47.png");
                itemDef.name = "AK-47";
                itemDef.ambient = 15;
                itemDef.contrast = 5;
                break;
//            case 31020:
//                itemDef.setDefaults();
//
//                itemDef.inventory_model = 64444;
//                //           itemDef.maleModel = 51002;
//                itemDef.manwear = 64445;
//                itemDef.womanwear = 64445;
//                //  itemDef.femaleModel = 51002;
//                itemDef.zoom2d = 1513;//increase to zoom OUT
////                itemDef.xan2d = 500;//was 500
////                itemDef.zan2d = 300;//was 300
////                itemDef.yan2d = 300;//was 300
//                itemDef.xan2d = 384;//was 500
////                itemDef.zan2d = 0;//was 300
////                itemDef.yan2d = 0;//was 300
//                itemDef.yOffset2d = 0; //move model up and down
//                itemDef.xOffset2d = 0; //move model r and l
//
//
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[] {null,"Wear", null, null, "Drop" };
//                //  itemDef.createCustomSprite("ak47.png");
//                itemDef.name = "Ganondermic platebody";
//             //   itemDef.ambient = 15;
//              //  itemDef.contrast = 5;
//                break;

//            case 31019:
//                itemDef.setDefaults();
//              //  itemDef.inventory_model = 13426;
//                itemDef.inventory_model = 64800;
//                itemDef.name = "Polypore Staff";
//               // itemDef. = "It's a Polypore Staff.";
//                itemDef.stackable = false;
//                itemDef.zoom2d = 0;
//                itemDef.xan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.manwear  = 64801;
//                itemDef.womanwear  = 64801;
//                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
//                break;
//            case 31019:
//                itemDef.setDefaults();
//                //itemDef.description = "Fo";
//                itemDef.inventory_model = 64445;
//                //           itemDef.maleModel = 51002;
//                itemDef.manwear = 64444;
//                itemDef.womanwear = 64444;
//                //  itemDef.femaleModel = 51002;
//                itemDef.zoom2d = 1500;//increase to zoom OUT
//                itemDef.xan2d = 0;//was 500
//                itemDef.zan2d = 300;//was 300
//                itemDef.yan2d = 0;//was 300
//                itemDef.yOffset2d = 0; //move model up and down
//                itemDef.xOffset2d = 0; //move model r and l
//
//
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
//                //  itemDef.createCustomSprite("ak47.png");
//                itemDef.name = "AK-47";
//                itemDef.ambient = 15;
//                itemDef.contrast = 5;
//                break;
//            case 31020:
//                itemDef.setDefaults();
//                //itemDef.description = "Fo";
//                itemDef.inventory_model = 64445;
//                //           itemDef.maleModel = 51002;
//                itemDef.manwear = 64444;
//                itemDef.womanwear = 64444;
//                //  itemDef.femaleModel = 51002;
//                itemDef.zoom2d = 1300;//increase to zoom OUT
//                itemDef.xan2d = 0;//was 500
//                itemDef.zan2d = 0;//was 300
//                itemDef.yan2d = 300;//was 300
//                itemDef.yOffset2d = 0; //move model up and down
//                itemDef.xOffset2d = 0; //move model r and l
//
//
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[] {null,"Equip", null, null, "Drop" };
//                //  itemDef.createCustomSprite("ak47.png");
//                itemDef.name = "AK-47";
//                itemDef.ambient = 15;
//                itemDef.contrast = 5;
//                break;

//            case 31015:
//                itemDef.setDefaults();
//                itemDef.id = 31015;
//                itemDef.inventory_model = 22165;
//                itemDef.name = "Pack Yak";
//                itemDef.zoom2d = 2500;
//                itemDef.xan2d = 0;
//                itemDef.zan2d = -100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 23892;
//                itemDef.womanwear = 23892;
//                itemDef.countObj = null;
//                itemDef.interfaceOptions = new String[] {null,null, null, null, "Drop" };
//                break;
//            case 31016:
//                itemDef.setDefaults();
//                itemDef.id = 31016;
//                itemDef.inventory_model = 22165;
//                itemDef.name = "Pack Yak";
//                itemDef.zoom2d = 2500;
//                itemDef.xan2d = 0;
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 23892;
//                itemDef.womanwear = 23892;
//                itemDef.countObj = null;
//                itemDef.interfaceOptions = new String[] {null,null, null, null, "Drop" };
//                break;
//            case 31017:
//                itemDef.setDefaults();
//                itemDef.id = 31017;
//                itemDef.inventory_model = 22165;
//                itemDef.name = "Pack Yak";
//                itemDef.zoom2d = 2500;
//                itemDef.xan2d = 0;
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 23892;
//                itemDef.womanwear = 23892;
//                itemDef.countObj = null;
//                itemDef.interfaceOptions = new String[] {null,null, null, null, "Drop" };
//                break;


//            case 21726:
//            case 21728:
//                itemDef.stackable = true;
//                break;
//            case 12863:
//                itemDef.interfaceOptions = new String[] { "Open", null, null, null, null};
//                break;
//            case 13092: //this makes crystal halberds wieldable, weird af.
//            case 13093:
//            case 13094:
//            case 13095:
//                break;
//            case 6199:
//                itemDef.colorFind = new int[]{332770, 76770};
//                itemDef.colorReplace = new int[]{22410, 2999};
//                itemDef.inventory_model = 2426;
//                break;
//            case 29968:
//                itemDef.name = "@gre@mystery box";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.zoom2d = 1000;
//                itemDef.createCustomSprite("mystery_box.png");
//                itemDef.stackable = false;
//                break;
//            case 29984:  // slayer helm
//                itemDef.name = "Slayer helm (or)";
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 55594;
//                itemDef.manwear = 55593;//55594
//                itemDef.womanwear = 55593;
//                itemDef.zoom2d = 900;
//                itemDef.xan2d = 0;
//                itemDef.yan2d =  100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.stackable = false;
//                break;
//            case 30009:
//                itemDef.id = 30009;
//                itemDef.name = "@red@ lava cape";
//                itemDef.interfaceOptions = new String[] { null, "Wear", null, null, "Drop" };
//                itemDef.inventory_model = 64389;
//                itemDef.manwear = 64389;
//                itemDef.womanwear = 64389;
//                itemDef.zoom2d = 2750;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 192;
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -20;
//                itemDef.stackable = false;
//                break;
//            case 30031:  // slayer helm
//                itemDef.id = 30031;
//                itemDef.name = "@red@ Burning Helmet Of Slayer";
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 61332;
//                itemDef.manwear = 61332;//55594
//                itemDef.womanwear = 61332;
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 0;
//                itemDef.yan2d =  100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.stackable = false;
//                break;
//            case 30030:
//                itemDef.setDefaults();
//                itemDef.id = 30030;
//                itemDef.inventory_model = 62970;
//                itemDef.name = "@blu@ Blue Betty";
//                itemDef.zoom2d = 1350;
//                itemDef.xan2d = 473;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 62970;
//                itemDef.womanwear = 62970;
////				itemDef.groundActions = new String[5];
////				itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];  /// 1 sec
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30032:
//                itemDef.setDefaults();
//                itemDef.id = 30032;
//                itemDef.inventory_model = 62971;
//                itemDef.name = "@blu@ Blue Betty Pantalones";
//                itemDef.zoom2d = 1350;
//                itemDef.xan2d = 473;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 62971;
//                itemDef.womanwear = 62971;
//                itemDef.interfaceOptions = new String[5];  /// 1 sec
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30042:
//                itemDef.setDefaults();
//                itemDef.id = 30042;
//                itemDef.inventory_model = 59999;
//                itemDef.name = "Dragonball helm";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 59999;
//                itemDef.womanwear = 59999;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29987:
//                itemDef.name = "Primordial boots (or)";
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 61010; //58968
//                itemDef.manwear = 61010;
//                itemDef.womanwear = 61010;
//                itemDef.zoom2d = 700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d =  180;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 30033:
//                itemDef.name = "Boots of Cooleness";
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 65157;
//                itemDef.manwear = 65157;
//                itemDef.womanwear = 65157;
//                itemDef.zoom2d = 700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d =  180;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 30034:
//                itemDef.setDefaults();
//                itemDef.name = "@blu@ Blue betty's cape";
//                itemDef.inventory_model = 65156;
//                itemDef.manwear = 65156;
//                itemDef.womanwear = 65156;
//                itemDef.zoom2d = 2300;
//                itemDef.xan2d = 400;
//                itemDef.yan2d = 1020;
//                itemDef.xOffset2d = 3;
//                itemDef.yOffset2d = 30;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30035:
//                itemDef.setDefaults();
//                itemDef.setDefaults();
//                itemDef.name = "pika jew";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 61181;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 30036:
//                itemDef.setDefaults();
//                itemDef.name = "Someone name it";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 61186;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                //itemDef.maleOffset = 5;
//                break;
//            case 30037:
//                itemDef.setDefaults();
//                itemDef.name = "Charmander";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 63997;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                //itemDef.maleOffset = 5;
//                break;
//            case 30038:
//                itemDef.setDefaults();
//                itemDef.name = "Foxy Lady";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 60781;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                //itemDef.maleOffset = 5; //boobies, mean bulbasor
//                break;
//            case 30039:
//                itemDef.setDefaults();
//                itemDef.name = "boobies, mean bulbasor";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 63998;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                //itemDef.maleOffset = 5; //boobies, mean bulbasor
//                break;
//            case 30040:
//                itemDef.setDefaults();
//                itemDef.id = 30040;
//                itemDef.name = "Halo thnigy";
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 65401;
//                itemDef.manwear = 65401;
//                itemDef.womanwear = 65401;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                //itemDef.maleOffset = 5; //boobies, mean bulbasor
//                break;
//            case 30041:
//                itemDef.setDefaults();
//                itemDef.id = 30041;
//                itemDef.name = "Halo thnigy";
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 65400;
//                itemDef.manwear = 65400;
//                itemDef.womanwear = 65400;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                //itemDef.maleOffset = 5; //boobies, mean bulbasor
//                break;
//            case 30006:
//                itemDef.name = "Charmander O.G.";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 61489;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 30007:
//                itemDef.name = "Charmander Colored";
//                itemDef.interfaceOptions = new String[]{null, null, null, null, "Drop"};
//                itemDef.inventory_model = 61490;
//                itemDef.zoom2d = 3750;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 26504:
//                itemDef.setDefaults();
//                itemDef.name = "<col=65280>Enchanted armadyl chainskirt";
//                itemDef.inventory_model = 28046;
//                itemDef.manwear = 27627;
//                itemDef.womanwear = 27641;
//                itemDef.zoom2d = 1957;
//                itemDef.xan2d = 555;
//                itemDef.yan2d =  2036;
//                itemDef.xOffset2d = -1;
//                itemDef.yOffset2d = -3;
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.colorFind = new int[]{0, -22452, 4550, -22456, -22506, 8650, -22460, -22448};
//                itemDef.colorReplace = new int[]{0, -22452, 4550, -22456, -22506, 374770, -22460, -22448};
//                break;
//            case 26502:
//                itemDef.setDefaults();
//                itemDef.name = "<col=65280>Enchanted armadyl helmet";
//                itemDef.inventory_model = 28043;
//                itemDef.manwear = 27623;
//                itemDef.womanwear = 27639;
//                itemDef.zoom2d = 789;
//                itemDef.xan2d = 66;
//                itemDef.yan2d = 372;
//                itemDef.xOffset2d = 9;
//                itemDef.yOffset2d = 0;
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.colorFind = new int[]{0, -22452, 4550, -22456, -22506, 8650, -22460, -22448};
//                itemDef.colorReplace = new int[]{0, -22452, 4550, -22456, -22506, 374770, -22460, -22448};
//                break;
//            case 21046:
//                itemDef.name = "@cya@Chest rate bonus (+15%)";
//                itemDef.stackable = true;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 11666:
//                itemDef.name = "Full Elite Void Token";
//                itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
//                break;
//            case 8701:
//                itemDef.name = "Full corrupted Void Token";
//                itemDef.inventory_model = 27165;
//                itemDef.zoom2d = 450; /// down makes it bigger
//                itemDef.yan2d = 215;
//                itemDef.xan2d = 300; // down makes it flat
//                itemDef.yOffset2d = 0;  // up goes down
//                itemDef.xOffset2d = 0; /// up goes right
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
//                itemDef.ambient = 15;
//                itemDef.contrast = 25;
//                break;
//            case 8653:
//                itemDef.name = "Full Ornate Void Token";
//                itemDef.inventory_model = 27165;
//                itemDef.zoom2d = 450; /// down makes it bigger
//                itemDef.yan2d = 215;
//                itemDef.xan2d = 300; // down makes it flat
//                itemDef.yOffset2d = 0;  // up goes down
//                itemDef.xOffset2d = 0; /// up goes right
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
//                itemDef.ambient = 15;
//                itemDef.contrast = 25;
//                break;
//            case 13096:
//            case 13097:
//            case 13098:
//            case 13099:
//            case 13100:
//            case 13101:
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, null};
//                break;
//            case 23933:
//                itemDef.name = "Vote crystal";
//                break;
//            case 9698:
//                itemDef.name = "Unfired burning rune";
//                itemDef.createCustomSprite("Unfired_burning_rune.png");
//                break;
//            case 9699:
//                itemDef.name = "Burning rune";
//                itemDef.createCustomSprite("Burning_rune.png");
//                break;
//            case 23778:
//                itemDef.name = "Uncut toxic gem";
//                break;
//            case 22374:
//                itemDef.name = "Hespori key";
//                break;
//            case 23783:
//                itemDef.name = "Toxic gem";
//                break;
//            case 9017:
//                itemDef.name = "Hespori essence";
//                itemDef.interfaceOptions = new String[] {  null, null, null, null, "Drop" };
//                break;
//            case 19473:
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 10556:
//            case 10557:
//            case 10558:
//            case 10559:
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Feature", null, "Drop" };
//                break;
//            case 21898:
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Teleports", "Features", null };
//                break;

//            case 4202:
//                itemDef.name = "@gre@Double XP Ring";
//                itemDef.interfaceOptions = new String[] { "Wear", null, null, null, "Drop" };
//                break;
//
//            case 12873:
//            case 12875:
//            case 12877:
//            case 12879:
//            case 12881:
//            case 12883:
//                itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
//                break;
//            case 23804:
//                itemDef.name = "Imbue Dust";
//                break;
//            case 22517:
//                itemDef.name = "Crystal Shard";
//                break;
//            case 23951:
//                itemDef.name = "Crystalline Key";
//                break;
//            case 691:
//                itemDef.name = "@gre@10,000 FoE Point Certificate";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 692:
//                itemDef.name = "@red@25,000 FoE Point Certificate";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 693:
//                itemDef.name = "@cya@50,000 FoE Point Certificate";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 696:
//                itemDef.name = "@yel@250,000 FoE Point Certificate";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 23877:
//                itemDef.name = "Crystal Shard";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = true;
//                break;
//            case 23943:
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                break;
//            case 2996:
//                itemDef.name = "@red@PKP Ticket";
//                break;
//            case 23776:
//                itemDef.name = "@red@Hunllef's Key";
//                break;
//            case 13148:
//                itemDef.name = "@red@Reset Lamp";
//                break;
//            case 6792:
//                itemDef.name = "@red@Seren's Key";
//                break;
//            case 4185:
//                itemDef.name = "@red@Porazdir's Key";
//                break;
//            case 21880:
//                itemDef.name = "Wrath Rune";
//                itemDef.cost = 1930;
//                break;
//            case 12885:
//            case 13277:
//            case 19701:
//            case 13245:
//            case 12007:
//            case 22106:
//            case 12936:
//            case 24495:
//                itemDef.interfaceOptions = new String[] { null, null, "Open", null, "Drop" };
//                break;
//            case 21262:
//                itemDef.name = "Vote Genie Pet";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Release" };
//                break;
//            case 21817:
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Dismantle", null, null, };
//                break;
//            case 21347:
//                itemDef.interfaceOptions = new String[] { null, null, null, "Chisel-Options", null, };
//                break;
//            case 21259:
//                itemDef.name = "@red@Name Change Scroll";
//                itemDef.interfaceOptions = new String[] { null, null, "Read", null, null, };
//                break;
//            case 22547:
//            case 22552:
//            case 22542:
//                itemDef.interfaceOptions = new String[] { null, null, null, null, null, };
//                break;
//            case 22555:
//            case 22550:
//            case 22545:
//                itemDef.interfaceOptions = new String[] { null, "Wield", "Check", "Uncharge", null, };
//                break;
//            case 732:
//                itemDef.name = "@blu@Imbuedeifer";
//                itemDef.cost = 1930;
//                break;
//            case 21881:
//                itemDef.name = "Wrath Rune";
//                itemDef.cost = 1930;
//                break;
//            case 13226:
//                itemDef.name = "Herb Sack";
//                break;
//            case 3456:
//                itemDef.name = "@whi@Common Raids Key";
//                break;
//            case 3464:
//                itemDef.name = "@pur@Rare Raids Key";
//                break;
//            case 6829:
//                itemDef.name = "@red@YT Video Giveaway Box";
//                itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };
//                break;
//            case 6831:
//                itemDef.name = "@red@YT Video Giveaway Box (t2)";
//                itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };
//
//                break;
//            case 6832:
//                itemDef.name = "@red@YT Stream Giveaway Box";
//                itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };
//
//                break;
//            case 6833:
//                itemDef.name = "@red@YT Stream Giveaway Box (t2)";
//                itemDef.interfaceOptions = new String[] { "Giveaway", null, null, null, "Drop" };
//
//                break;
//            case 13190:
//                itemDef.name = "@yel@100m OSRS GP";
//                itemDef.interfaceOptions = new String[] { "Redeem", null, null, null, "Drop" };
//                break;
//            case 6121:
//                itemDef.name = "Break Vials Instruction";
//                break;
//            case 2528:
//                itemDef.name = "@red@Experience Lamp";
//                break;
//            case 5509:
//                itemDef.name = "Small Pouch";
//                itemDef.interfaceOptions = new String[] { "Fill", "Empty", "Check", null, null };
//                break;
//            case 5510:
//                itemDef.name = "Medium Pouch";
//                itemDef.interfaceOptions = new String[] { "Fill", "Empty", "Check", null, null };
//                break;
//            case 5512:
//                itemDef.name = "Large Pouch";
//                itemDef.interfaceOptions = new String[] { "Fill", "Empty", "Check", null, null };
//                break;
//            case 10724: //full skeleton
//            case 10725:
//            case 10726:
//            case 10727:
//            case 10728:
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
//                break;
//            case 5514:
//                itemDef.name = "Giant Pouch";
//                break;
//            case 22610: //vesta spear
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
//                break;
//            case 22613: //vesta longsword
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
//                break;
//            case 22504: //stat warhammer
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
//                break;
//            case 4224:
//            case 4225:
//            case 4226:
//            case 4227:
//            case 4228:
//            case 4229:
//            case 4230:
//            case 4231:
//            case 4232:
//            case 4233:
//            case 4234:
//            case 4235://crystal sheild
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
//                break;
//            case 4212:
//            case 4214:
//            case 4215:
//            case 4216:
//            case 4217:
//            case 4218:
//            case 4219:
//            case 4220:
//            case 4221:
//            case 4222:
//            case 4223:
//                itemDef.interfaceOptions = new String[] { null, "Wield", null, null, "Drop" };
//                break;
//            case 2841:
//                itemDef.name = "@red@Bonus Exp Scroll";
//                itemDef.interfaceOptions = new String[] { "@yel@Activate", null, null, null, "Drop" };
//                break;
            case 26080:
                itemDef.name = "Double Drop Sigil";
                itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
                break;
            case 26083:
                itemDef.name = "Double XP Sigil";
                itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
                break;
            case 26071:
                itemDef.name = "15% Bonus Damage Sigil";
                itemDef.interfaceOptions = new String[] { "Activate", null, null, null, "Drop" };
                break;
//            case 21791:
//            case 21793:
//            case 21795:
//                itemDef.interfaceOptions = new String[] { null, "Wear", null, null, "Drop" };
//                break;
//            case 19841:
//                itemDef.name = "Master Casket";
//                break;
//            case 21034:
//                itemDef.interfaceOptions = new String[] { "Read", null, null, null, "Drop" };
//                break;
//            case 6830:
//                itemDef.name = "@yel@BETA @blu@BOX";
//                itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
//                break;
//            case 21079:
//                itemDef.interfaceOptions = new String[] { "Read", null, null, null, "Drop" };
//                break;
//            case 22093:
//                itemDef.name = "@gre@Vote Streak Key";
//                break;
//            case 22885:
//                itemDef.name = "@gre@Kronos seed";
//                break;
//            case 23824:
//                itemDef.name = "Slaughter charge";
//                break;
//            case 22883:
//                itemDef.name = "@gre@Iasor seed";
//                break;
//            case 22881:
//                itemDef.name = "@gre@Attas seed";
//                break;
//            case 20906:
//                itemDef.name = "@gre@Golpar seed";
//                break;
//            case 6112:
//                itemDef.name = "@gre@Kelda seed";
//                break;
//            case 20903:
//                itemDef.name = "@gre@Noxifer seed";
//                break;
//            case 20909:
//                itemDef.name = "@gre@Buchu seed";
//                break;
//            case 22869:
//                itemDef.name = "@gre@Celastrus seed";
//                break;
//            case 4205:
//                itemDef.name = "@gre@Consecration seed";
//                itemDef.stackable = true;
//                break;
//            case 11864:
//            case 11865:
//            case 19639:
//            case 19641:
//            case 19643:
//            case 19645:
//            case 19647:
//            case 19649:
//            case 24444:
//            case 24370:
//            case 23075:
//            case 23073:
//            case 21888:
//            case 21890:
//            case 21264:
//            case 21266:
//                itemDef.equipActions[2] = "Log";
//                itemDef.equipActions[1] = "Check";
//                break;
//            case 13136:
//                itemDef.equipActions[2] = "Elidinis";
//                itemDef.equipActions[1] = "Kalphite Hive";
//                break;
//            case 2550:
//                itemDef.equipActions[2] = "Check";
//                break;
//
//            case 1712:
//            case 1710:
//            case 1708:
//            case 1706:
//            case 19707:
//                itemDef.equipActions[1] = "Edgeville";
//                itemDef.equipActions[2] = "Karamja";
//                itemDef.equipActions[3] = "Draynor";
//                itemDef.equipActions[4] = "Al-Kharid";
//                break;
//            case 21816:
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", null, "Drop" };
//                itemDef.equipActions[1] = "Check";
//                itemDef.equipActions[2] = "Toggle-absorption";
//                break;
//            case 2552:
//            case 2554:
//            case 2556:
//            case 2558:
//            case 2560:
//            case 2562:
//            case 2564:
//            case 2566: // Ring of duelling
//                itemDef.equipActions[2] = "Shantay Pass";
//                itemDef.equipActions[1] = "Clan wars";
//                break;
//            case 29952: //done
//                itemDef.setDefaults(); //26864
//                itemDef.name = "jubster";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 26864;
//                itemDef.zoom2d = 3600;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 80;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.countObj = null;
//                break;
//            case 29953: //done
//                itemDef.setDefaults(); //36353
//                itemDef.name = "arc_test";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 36353;
//                itemDef.zoom2d = 3000;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 29954: //done
//                itemDef.setDefaults();//22264,
//                itemDef.name = "Autumn Elemental";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 22264;
//                itemDef.zoom2d = 3575;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 29955: //done
//                itemDef.setDefaults(); //36174
//                itemDef.name = "corkat";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 36174;
//                itemDef.zoom2d = 3900;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 29956: // done
//                itemDef.setDefaults();//36206,
//                itemDef.name = "Cormorant";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 36206;
//                itemDef.zoom2d = 1900;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 29957:  //39159,
//                itemDef.setDefaults();
//                itemDef.name = "daer krand";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 39481;
//                itemDef.zoom2d = 3000;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -6;
//                break;
//            case 29959: // done
//                itemDef.setDefaults(); //10375,
//                itemDef.name = "desert spirit";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 10373;
//                itemDef.zoom2d = 4000;
//                itemDef.xan2d = 500;
//                itemDef.zan2d = 0;
//                break;
//            case 29960: //done
//                itemDef.setDefaults(); //35831
//                itemDef.name = "don't know what";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 35831;
//                itemDef.zoom2d = 5500;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 29961:
//                itemDef.setDefaults();
//                itemDef.name = "Ethereal Lady";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 16468;
//                itemDef.zoom2d = 2500;
//                itemDef.xan2d = 150;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -6;
//                break;
//            case 29962: // done
//                itemDef.setDefaults();
//                itemDef.name = "Light Creature";
//                itemDef.inventory_model = 6541;
//                itemDef.zoom2d = 2000;
//                itemDef.xan2d = 66;
//                itemDef.yan2d = 372;
//                itemDef.xOffset2d = 9;
//                itemDef.yOffset2d = 0;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 29963: //done
//                itemDef.setDefaults(); //31568
//                itemDef.name = "manaical monkey";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.inventory_model = 31568;
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 50;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                break;
//            case 25749: //done
//                itemDef.setDefaults(); //31568
//                itemDef.name = "Lil' Bloat";
//                itemDef.inventory_model = 42287;
//                itemDef.zoom2d = 2500;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 25748: //done
//                itemDef.setDefaults(); //31568
//                itemDef.name = "Lil' Maiden";
//                itemDef.inventory_model = 42288;
//                itemDef.zoom2d = 2500;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 25751: //done
//                itemDef.setDefaults(); //31568
//                itemDef.name = "Lil' Sotetseg";
//                itemDef.inventory_model = 42286;
//                itemDef.zoom2d = 2500;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 25752: //done
//                itemDef.setDefaults(); //31568
//                itemDef.name = "Lil' Xarpus";
//                itemDef.inventory_model = 42284;
//                itemDef.zoom2d = 2500;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 2023: //done
//                itemDef.setDefaults(); //31568
//                itemDef.name = "Daily Rewards Book";
//                itemDef.inventory_model = 42284;
//                itemDef.zoom2d = 2500;
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                break;
//            case 30014:
//                itemDef.setDefaults();
//                itemDef.name = "K'klik";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30015:
//                itemDef.setDefaults();
//                itemDef.name = "Shadow warrior";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30016:
//                itemDef.setDefaults();
//                itemDef.name = "Shadow archer";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30017:
//                itemDef.setDefaults();
//                itemDef.name = "Shadow wizard";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30018:
//                itemDef.setDefaults();
//                itemDef.name = "Healer Death Spawn";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30019:
//                itemDef.setDefaults();
//                itemDef.name = "Holy Death Spawn";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30020:
//                itemDef.setDefaults();
//                itemDef.name = "Corrupt beast";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30021:
//                itemDef.setDefaults();
//                itemDef.name = "Roc";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30022:
//                itemDef.setDefaults();
//                itemDef.name = "@red@Kratos";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30023:
//                itemDef.setDefaults();
//                itemDef.name = "Rain cloud";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 8866:
//                itemDef.name = "Storage chest key (UIM)";
//                itemDef.stackable = true;
//                break;
//            case 8868:
//                itemDef.name = "Perm. storage chest key (UIM)";
//                break;
//            case 771:
//                itemDef.name = "@cya@Ancient branch";
//                break;
//            case 13438:
//                itemDef.name = "Slayer Mystery Chest";
//                itemDef.interfaceOptions = new String[] { "Open", null, null, null, "Drop" };
//                break;
//            case 2399:
//                itemDef.name = "@or2@FoE Mystery Key";
//                break;
//            case 10832:
//                itemDef.name = "Small coin bag";
//                itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
//                break;
//            case 10833:
//                itemDef.name = "Medium coin bag";
//                itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
//                break;
//            case 10834:
//                itemDef.name = "Large coin bag";
//                itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
//                break;
//            case 22316:
//                itemDef.name = "Sword of Skotos";
//                break;
//            case 19942:
//                itemDef.name = "Lil Mimic";
//                break;
////			case 30110:
////				itemDef.setDefaults();
////				itemDef.name = "Dark postie pete";
////				itemDef.description = "Picks up all crystal keys and 25% chance to double.";
////				itemDef.createCustomSprite("dark_Postie_Pete.png");
////				itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
////				itemDef.stackable = false;
////				break;
//
//            case 30122:
//                itemDef.setDefaults();
//                itemDef.name = "@red@Dark kratos";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 30123:
//                itemDef.setDefaults();
//                itemDef.name = "Dark seren";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = false;
//                break;
//            case 23939:
//                itemDef.name = "Seren";
//                break;
//            case 1004:
//                itemDef.name = "@gre@20m Coins";
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 7629:
//                itemDef.name = "@or3@2x Slayer point scroll";
//                itemDef.interfaceOptions = new String[] { null, null, null, null, "Drop" };
//                itemDef.stackable = true;
//                break;
//            case 24460:
//                itemDef.name = "@or3@Faster clues (30 mins)";
//                itemDef.interfaceOptions = new String[] { "Boost", null, null, null, "Drop" };
//                itemDef.stackable = true;
//                break;
//            case 7968:
//                itemDef.name = "@or3@+25% Skilling pet rate (30 mins)";
//                itemDef.interfaceOptions = new String[] { "Boost", null, null, null, "Drop" };
//                itemDef.stackable = true;
//                break;
//            case 8899:
//                itemDef.name = "@gre@50m Coins";
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 4035:
//                itemDef.interfaceOptions = new String[] { "Teleport", null, null, null, null };
//                break;
//            case 10835:
//                itemDef.name = "Buldging coin bag";
//                itemDef.interfaceOptions = new String[] { "Open", null, "Open-All", null, "Drop" };
//                break;
           case 15098:
               itemDef.name = "Dice (up to 100)";
               itemDef.inventory_model = 31223;
                itemDef.zoom2d = 1104;
               itemDef.yan2d = 215;
                itemDef.xan2d = 94;
                itemDef.yOffset2d = -5;
                itemDef.xOffset2d = -18;
                itemDef.interfaceOptions = new String[5];
                itemDef.interfaceOptions[1] = "Public-roll";
                itemDef.interfaceOptions[2] = null;
                itemDef.name = "Dice (up to 100)";
                itemDef.ambient = 15;
                itemDef.contrast = 25;
                break;
//            case 11773:
//            case 11771:
//            case 11770:
//            case 11772:
//                itemDef.ambient += 45;
//                break;
//            case 12792:
//                itemDef.name = "Graceful Recolor Box";
//                itemDef.interfaceOptions = new String[] { null, "Use", null, null, "Drop" };
//                break;
//            case 6769:
//                itemDef.name = "@yel@$5 Scroll";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 2403:
//                itemDef.name = "@yel@$10 Scroll";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 2396:
//                itemDef.name = "@yel@$25 Scroll";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 786:
//                itemDef.name = "@yel@$50 Donator";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 761:
//                itemDef.name = "@yel@$100 Donator";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 607:
//                itemDef.name = "@red@$250 Scroll";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 608:
//                itemDef.name = "@gre@$500 Scroll";
//                itemDef.interfaceOptions = new String[] { "Claim", null, null, null, "Drop" };
//                break;
//            case 1464:
//                itemDef.name = "Vote ticket";
//                break;
//
//            case 30008:
//                itemDef.setDefaults();
//                itemDef.id = 30008;
//                itemDef.name = "Bloody Angel Wings";
//                itemDef.interfaceOptions = new String[] { null, "Wear", null, null, "Drop" };
//                itemDef.zoom2d = 2750;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 192;
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -20;
//                itemDef.inventory_model = 61450;
//                itemDef.manwear =61450;
//                itemDef.womanwear = 61450;
//                break;
//            case 29993:
//                itemDef.setDefaults();
//                itemDef.id = 29993;
//                itemDef.name = "Wings Of Destruction";
//                itemDef.interfaceOptions = new String[] { null, "Wear", null, null, "Drop" };
//                itemDef.zoom2d = 2300;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 192;
//                itemDef.zan2d = 100;
//                itemDef.countObj = null;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 10;
//                itemDef.inventory_model = 65436;
//                itemDef.manwear =65436;
//                itemDef.womanwear = 65436;
//                break;
//            case 25890:
//                itemDef.setDefaults();
//                itemDef.id = 25890;
//                itemDef.name = "Bow of faerdhinen (Cg)";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1570;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 192;
//                itemDef.colorFind = new int[]{-32535, -32541};
//                itemDef.colorReplace = new int[]{21947, 21958};
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 1;
//                itemDef.yOffset2d = 0;
//                itemDef.inventory_model = 42605;
//                itemDef.manwear =42602;
//                itemDef.womanwear = 42602;
//                break;
//            case 25892:
//                itemDef.setDefaults();
//                itemDef.id = 25892;
//                itemDef.name = "Bow of faerdhinen (Cgo)";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1570;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 192;
//                itemDef.colorFind = new int[]{-32535, -32541};
//                itemDef.colorReplace = new int[]{9668, 9804};
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 1;
//                itemDef.yOffset2d = 0;
//                itemDef.inventory_model = 42605;
//                itemDef.manwear = 42602;
//                itemDef.womanwear = 42602;
//                break;
//            case 8815:
//                itemDef.setDefaults();
//                itemDef.id = 8815;
//                itemDef.name = "@red@Blade of saeldor";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.colorFind = new int[]{33001, 32995};
//                itemDef.colorReplace = new int[]{933, 933};
//                itemDef.zan2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.inventory_model = 37980;
//                itemDef.manwear =38270;
//                itemDef.womanwear = 38280;
//                break;
//            case 30010:   ///46705
//                itemDef.setDefaults();
//                itemDef.id = 30010;
//                itemDef.name = "@red@aka Trippy scytheblue";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.zan2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.inventory_model = 61451;
//                itemDef.manwear = 61451;
//                itemDef.womanwear = 61451;
//                break;
//            case 30011:
//                itemDef.setDefaults();
//                itemDef.id = 30011;
//                itemDef.name = "@red@aka baby scythe of carnage";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.zan2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.inventory_model = 60879;
//                itemDef.manwear =60879;
//                itemDef.womanwear = 60879;
//                break;
//            case 30012:
//                itemDef.setDefaults();
//                itemDef.id = 30012;
//                itemDef.name = "@red@aka My bigass Boom Stick";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.zan2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.inventory_model = 61288;
//                itemDef.manwear =61288;
//                itemDef.womanwear = 61288;
//                break;
//            case 30013:
//                itemDef.setDefaults();
//                itemDef.id = 30013;
//                itemDef.name = "@red@aka smooth lover";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.zan2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.inventory_model = 61289;//61451
//                itemDef.manwear =61289;
//                itemDef.womanwear = 61289;
//                break;
//            case 29989:
//                itemDef.setDefaults();
//                itemDef.id = 29989;
//                itemDef.name = "LEGS";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.inventory_model = 65442;
//                itemDef.manwear =65442;
//                itemDef.womanwear = 65442;
//                break;
//            case 29996:
//                itemDef.setDefaults();
//                itemDef.id = 29996;
//                itemDef.inventory_model = 65500;
//                itemDef.name = "Newage pokey";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -10;
//                itemDef.manwear =65500;
//                itemDef.womanwear = 65500;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29994:
//                itemDef.setDefaults();
//                itemDef.id = 29994;
//                itemDef.name = "Newage pokey";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.inventory_model = 65449;
//                itemDef.manwear = 65449;
//                itemDef.womanwear = 65449;
//                break;
//            case 29995:
//                itemDef.setDefaults();
//                itemDef.id = 29995;
//                itemDef.name = "Winged Body";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1876;
//                itemDef.inventory_model = 65334;
//                itemDef.manwear =65334;
//                itemDef.womanwear = 65334;
//                break;
//            case 8813:
//                itemDef.id = 8813;
//                itemDef.setDefaults();
//                itemDef.name = "Corrupted Elite Void top";
//                itemDef.inventory_model = 29170;
//                itemDef.manwear = 29166;
//                itemDef.womanwear = 29169;
//                itemDef.zoom2d = 1221;
//                itemDef.xan2d = 459;
//                itemDef.zan2d =  0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.colorFind = new int[]{127, 119, 123, 111, 115, 103};
//                itemDef.colorReplace = new int[]{933,933, 933, 933, 933,933};
//                break;
//            case 8814:
//                itemDef.id = 8814;
//                itemDef.setDefaults();
//                itemDef.name = "Corrupted Elite Void robe";
//                itemDef.inventory_model = 29171;
//                itemDef.manwear = 29164;
//                itemDef.womanwear = 29167;
//                itemDef.zoom2d = 2105;
//                itemDef.xan2d = 525;
//                itemDef.zan2d =  0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -1;
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.colorFind = new int[]{21563,119, 111, 82, 115, 123, 66,127,21568,21576,21559,21580,21572};
//                itemDef.colorReplace = new int[]{933,933,933,933,933,933,933,933,933, 933, 933, 933,933};
//                break;
//            case 26503:
//                itemDef.id = 26503;
//                itemDef.setDefaults();
//                itemDef.name = "<col=65280>Enchanted armadyl chestplate";
//                itemDef.inventory_model = 28039;
//                itemDef.manwear = 27633;
//                itemDef.womanwear = 27645;
//                itemDef.zoom2d = 789;
//                itemDef.xan2d = 453;
//                itemDef.yan2d =  0;
//                itemDef.xOffset2d = 1;
//                itemDef.yOffset2d = -5;
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.colorFind = new int[]{8658, -22452, 4550, -22440, -22489, 8650, -22460, -22448, -22464};
//                itemDef.colorReplace = new int[]{374770, -22452, 4550, -22440, -22489, 374770, -22460, -22448, -22464};
//                break;
//            case 20201:
//                itemDef.setDefaults();
//                itemDef.id = 20201;
//                itemDef.name = "Bow of faerdhinen";
//                itemDef.interfaceOptions = new String[] { null, "Wear", "Uncharge", "Check", "Drop" };
//                itemDef.zoom2d = 1570;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 192;
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 1;
//                itemDef.yOffset2d = 0;
//                itemDef.inventory_model = 42605;
//                itemDef.manwear = 42602;
//                itemDef.womanwear = 42602;
//                break;
//
//            case 29998:
//                itemDef.setDefaults();
//                itemDef.id = 29998;
//                itemDef.inventory_model = 65523;//65523
//                itemDef.name = "Surge's Mage Body";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 65523;
//                itemDef.womanwear = 65523;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29999:
//                itemDef.setDefaults();
//                itemDef.id = 29999;
//                itemDef.inventory_model = 65524;
//                itemDef.name = "Surge's Mage Skirt";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 65524;
//                itemDef.womanwear = 65524;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30004:
//                itemDef.setDefaults();
//                itemDef.id = 30004;
//                itemDef.inventory_model = 65538;
//                itemDef.name = "Surge's Mage Boots";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 65538;
//                itemDef.womanwear = 65538;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30005:
//                itemDef.setDefaults();
//                itemDef.id = 30005;
//                itemDef.inventory_model = 65544;
//                itemDef.name = "OMG its true, black is bigger";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 65544;
//                itemDef.womanwear = 65544;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30052:
//                itemDef.setDefaults();
//                itemDef.id = 30052;
//                itemDef.inventory_model = 59876;
//                itemDef.name = "bullshitbody";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 59876;
//                itemDef.womanwear = 59876;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30053:
//                itemDef.setDefaults();
//                itemDef.id = 30053;
//                itemDef.inventory_model = 60196;
//                itemDef.name = "bullshit legs";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 60196;
//                itemDef.womanwear = 60196;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30054:
//                itemDef.setDefaults();
//                itemDef.id = 30054;
//                itemDef.inventory_model = 61121;
//                itemDef.name = "bullshit hood";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 61121;
//                itemDef.womanwear = 61121;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30055:
//                itemDef.setDefaults();
//                itemDef.id = 30055;
//                itemDef.inventory_model = 60197;
//                itemDef.name = "bullshit hoodz";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 60197;
//                itemDef.womanwear = 60197;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30056:
//                itemDef.setDefaults();
//                itemDef.id = 30056;
//                itemDef.inventory_model = 58559;
//                itemDef.name = "bullshit staff";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 58559;
//                itemDef.womanwear = 58559;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30057:
//                itemDef.setDefaults();
//                itemDef.id = 30057;
//                itemDef.inventory_model = 60111;
//                itemDef.name = "bullshit shield";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 60111;
//                itemDef.womanwear = 60111;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30058:
//                itemDef.setDefaults();
//                itemDef.id = 30058;
//                itemDef.inventory_model = 60101;
//                itemDef.name = "bullshit cape";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 60101;
//                itemDef.womanwear = 60101;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30059:
//                itemDef.setDefaults();
//                itemDef.id = 30059;
//                itemDef.inventory_model = 58999;
//                itemDef.name = "bullshit capez";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 58999;
//                itemDef.womanwear = 58999;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30060:
//                itemDef.setDefaults();
//                itemDef.id = 30060;
//                itemDef.inventory_model = 61949;
//                itemDef.name = "dragon cape";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 61949;
//                itemDef.womanwear = 61949;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30061:
//                itemDef.setDefaults();
//                itemDef.id = 30061;
//                itemDef.inventory_model = 61050;//58999
//                itemDef.name = "skull kite";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 473;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 61050;
//                itemDef.womanwear = 61050;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;

//            case 8469:
//                itemDef.setDefaults();
//                itemDef.id = 8469;
//                itemDef.inventory_model = 65442;
//                itemDef.name = "Surge legs";
//                itemDef.zoom2d = 1900;
//                itemDef.xan2d = 474;
//                itemDef.zan2d = 2045;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 65442;//65323
//                itemDef.womanwear = 65442;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29980:
//                itemDef.id = 29980;
//                itemDef.name = "@gre@ TierII Box Of Randomness";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55596;
//                itemDef.manwear = 55596;
//                itemDef.womanwear = 55596;
//                itemDef.countObj = null;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 150;
//                itemDef.zan2d = 0;
//                itemDef.zoom2d = 3800;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 28026:
//                itemDef.setDefaults();
//                itemDef.id = 28026;
//                itemDef.inventory_model = 59808;//65434 //59808
//                itemDef.name = "Korasi Sword";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 59808;
//                itemDef.womanwear = 59808;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30063:
//                itemDef.setDefaults();  // adjust sprite
//                itemDef.id = 30063;
//                itemDef.inventory_model = 64091;
//                itemDef.name = "Dragonkin Fireshield";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64092;
//                itemDef.womanwear = 64092;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30064:
//                itemDef.setDefaults();
//                itemDef.id = 30064;
//                itemDef.inventory_model = 60563;
//                itemDef.name = "Masters Cape";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60563;
//                itemDef.womanwear = 60563;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30065:
//                itemDef.setDefaults();
//                itemDef.id = 30065;
//                itemDef.inventory_model = 60562;
//                itemDef.name = "Dragonkin Legs";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60561;
//                itemDef.womanwear = 60561;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30066:
//                itemDef.setDefaults();
//                itemDef.id = 30066;
//                itemDef.inventory_model = 60575;
//                itemDef.name = "Dragonkin Body";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60574;
//                itemDef.womanwear = 60574;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30067:
//                itemDef.setDefaults();
//                itemDef.id = 30067;
//                itemDef.inventory_model = 60672;
//                itemDef.name = "4th Age Kiteshield";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60672;
//                itemDef.womanwear = 60672;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30068:
//                itemDef.setDefaults();
//                itemDef.id = 30068;
//                itemDef.inventory_model = 61281;
//                itemDef.name = "4th age bow";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61287;
//                itemDef.womanwear = 61287;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30069:
//                itemDef.setDefaults();
//                itemDef.id = 30069;
//                itemDef.inventory_model = 62125;
//                itemDef.name = "4th age armadyl godsword";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 62125;
//                itemDef.womanwear = 62125;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30070:
//                itemDef.setDefaults();
//                itemDef.id = 30070;
//                itemDef.inventory_model = 62138;
//                itemDef.name = "DeathStalker Godsword";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 62138;
//                itemDef.womanwear = 62138;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30071:
//                itemDef.setDefaults();
//                itemDef.id = 30071;
//                itemDef.inventory_model = 64116;
//                itemDef.name = "4th age Armadyl Shield";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64116;
//                itemDef.womanwear = 64116;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30072:
//                itemDef.setDefaults();
//                itemDef.id = 30072;
//                itemDef.inventory_model = 62753;
//                itemDef.name = "4th Age Lance";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 62730;
//                itemDef.womanwear = 62730;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30073:
//                itemDef.setDefaults();
//                itemDef.id = 30073;
//                itemDef.inventory_model = 61539;
//                itemDef.name = "4th age staff";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61528;
//                itemDef.womanwear = 61528;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30074:
//                itemDef.setDefaults();
//                itemDef.id = 30074;
//                itemDef.inventory_model = 61828;
//                itemDef.name = "4th age pickaxe";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61828;
//                itemDef.womanwear = 61828;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30075:
//                itemDef.setDefaults();
//                itemDef.id = 30075;
//                itemDef.inventory_model = 63991;
//                itemDef.name = "Lava Wyrm Staff";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 63991;
//                itemDef.womanwear = 63991;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30076:
//                itemDef.setDefaults();
//                itemDef.id = 30076;
//                itemDef.inventory_model = 61280;
//                itemDef.name = "Mastiff Gloves";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61181;
//                itemDef.womanwear = 61181;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30077:
//                itemDef.setDefaults();
//                itemDef.id = 30077;
//                itemDef.inventory_model = 61279;
//                itemDef.name = "Mastique Helm T1";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61184;
//                itemDef.womanwear = 61184;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30078:
//                itemDef.setDefaults();
//                itemDef.id = 30078;
//                itemDef.inventory_model = 61183;
//                itemDef.name = "Mastique Helm T2";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61183;
//                itemDef.womanwear = 61183;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30079:
//                itemDef.setDefaults();
//                itemDef.id = 30079;
//                itemDef.inventory_model = 60281;
//                itemDef.name = "Mastique Helm T3";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61182;
//                itemDef.womanwear = 61182;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30080:
//                itemDef.setDefaults();
//                itemDef.id = 30080;
//                itemDef.inventory_model = 62398;
//                itemDef.name = "Mystique PlateLegs T3";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 62398;
//                itemDef.womanwear = 62398;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30081:
//                itemDef.setDefaults();
//                itemDef.id = 30081;
//                itemDef.inventory_model = 61393; //61275
//                itemDef.name = "Mystique Platebody T3";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61393;
//                itemDef.womanwear = 61393;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30082:
//                itemDef.setDefaults();
//                itemDef.id = 30082;
//                itemDef.inventory_model = 61276;
//                itemDef.name = "Mystique KiteShield T3";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 61190;
//                itemDef.womanwear = 61190;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30083:
//                itemDef.setDefaults();
//                itemDef.id = 30083;
//                itemDef.inventory_model = 63119;
//                itemDef.name = "Saradomins Blessed Holy Book";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 63119;
//                itemDef.womanwear = 63119;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30084:
//                itemDef.setDefaults();
//                itemDef.id = 30084;
//                itemDef.inventory_model = 61776;
//                itemDef.name = "Zaryte Bow";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = 0;
//                itemDef.manwear = 61776;
//                itemDef.womanwear = 61776;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30085:
//                itemDef.setDefaults();
//                itemDef.id = 30085;
//                itemDef.inventory_model = 64199;
//                itemDef.name = "AOD GodSword";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64199;
//                itemDef.womanwear = 64199;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30086:
//                itemDef.setDefaults();
//                itemDef.id = 30086;
//                itemDef.inventory_model = 60133;
//                itemDef.name = "Black AOD Cape";
//                itemDef.zoom2d = 2850; // zoom
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 990;
//                itemDef.zan2d = 10;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.resizeX = 150;
//                itemDef.resizeY = 100;
//                itemDef.manwear = 60133;
//                itemDef.womanwear = 60133;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30087:
//                itemDef.setDefaults();
//                itemDef.id = 30087;
//                itemDef.inventory_model = 60131;
//                itemDef.name = "Black AOD body";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 50;
//                itemDef.resizeX = 150;
//                itemDef.manwear = 60132;
//                itemDef.womanwear = 60131;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30088:
//                itemDef.setDefaults();
//                itemDef.id = 30088;
//                itemDef.inventory_model = 60130;
//                itemDef.name = "Black AOD Legs";
//                itemDef.zoom2d = 1650;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 10;
//                itemDef.resizeX = 155;
//                itemDef.manwear = 60130;
//                itemDef.womanwear = 60130;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30089:
//                itemDef.setDefaults();
//                itemDef.id = 30089;
//                itemDef.inventory_model = 60134;
//                itemDef.name = "Black AOD Helm";
//                itemDef.zoom2d = 700;
//                itemDef.xan2d = 17;
//                itemDef.zan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.resizeX = 155;
//                itemDef.resizeY = 150;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 100;
//                itemDef.manwear = 60134;
//                itemDef.womanwear = 60134;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30090: //
//                itemDef.setDefaults();
//                itemDef.id = 30090;
//                itemDef.inventory_model = 60135;
//                itemDef.name = "Teal AOD cape";
//                itemDef.zoom2d = 2850; // zoom
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 990;
//                itemDef.zan2d = 10;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.resizeX = 150;
//                itemDef.resizeY = 100;
//                itemDef.manwear = 60135;
//                itemDef.womanwear = 60135;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30091:
//                itemDef.setDefaults();
//                itemDef.id = 30091;
//                itemDef.inventory_model = 60137;
//                itemDef.name = "Teal AOD Body";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 50;
//                itemDef.resizeX = 155;
//                itemDef.manwear = 60137;
//                itemDef.womanwear = 60136;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30092:
//                itemDef.setDefaults();
//                itemDef.id = 30092;
//                itemDef.inventory_model = 60138;
//                itemDef.name = "Teal AOD Helm";
//                itemDef.zoom2d = 700;
//                itemDef.xan2d = 17;
//                itemDef.zan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.resizeX = 155;
//                itemDef.resizeY = 150;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 100;
//                itemDef.manwear = 60138;
//                itemDef.womanwear = 60148;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30093:
//                itemDef.setDefaults();
//                itemDef.id = 30093;
//                itemDef.inventory_model = 60139;
//                itemDef.name = "Teal AOD Legs";
//                itemDef.zoom2d = 1650;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 10;
//                itemDef.resizeX = 155;
//                itemDef.manwear = 60139;
//                itemDef.womanwear = 60149;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30094:
//                itemDef.setDefaults();
//                itemDef.id = 30094;
//                itemDef.inventory_model = 60140;
//                itemDef.name = "Red AOD Body";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 50;
//                itemDef.resizeX = 155;
//                itemDef.manwear = 60140;
//                itemDef.womanwear = 60141;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30095:
//                itemDef.setDefaults();
//                itemDef.id = 30095;
//                itemDef.inventory_model = 60142;
//                itemDef.name = "Red AOD Cape";
//                itemDef.zoom2d = 2850; // zoom
//                itemDef.xan2d = 0;  // roates it on a top to bottom as in flattening an object
//                itemDef.yan2d = 990; // rotation side to side as in back of object to front
//                itemDef.zan2d = 10;  // moves top to side as in diagonal
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.resizeX = 150;
//                itemDef.resizeY = 100;
//                itemDef.manwear = 60142;
//                itemDef.womanwear = 60142;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30096:
//                itemDef.setDefaults();
//                itemDef.id = 30096;
//                itemDef.inventory_model = 60143;
//                itemDef.name = "Red AOD Helm";
//                itemDef.zoom2d = 700;
//                itemDef.xan2d = 17;
//                itemDef.zan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.resizeX = 155;
//                itemDef.resizeY = 150;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 100;
//                itemDef.manwear = 60143;
//                itemDef.womanwear = 60152;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30097:
//                itemDef.setDefaults();
//                itemDef.id = 30097;
//                itemDef.inventory_model = 60144;
//                itemDef.name = "Red Aod Legs";
//                itemDef.zoom2d = 1650;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 10;
//                itemDef.resizeX = 155;
//                itemDef.manwear = 60144;
//                itemDef.womanwear = 60153;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30098:
//                itemDef.setDefaults();
//                itemDef.id = 30098;
//                itemDef.inventory_model = 60145;
//                itemDef.name = "AOD Wand";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60145;
//                itemDef.womanwear = 60145;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30099:
//                itemDef.setDefaults();
//                itemDef.id = 30099;
//                itemDef.inventory_model = 64093;
//                itemDef.name = "AOD Mage Power up";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64093;
//                itemDef.womanwear = 64093;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30100:
//                itemDef.setDefaults();
//                itemDef.id = 30100;
//                itemDef.inventory_model = 60146;
//                itemDef.name = "Black AOD Gloves";
//                itemDef.zoom2d = 500;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 120;
//                itemDef.zan2d = 0;
//                itemDef.resizeX = 50;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 40;
//                itemDef.manwear = 60146;
//                itemDef.womanwear = 60146;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30101:
//                itemDef.setDefaults();
//                itemDef.id = 30101;
//                itemDef.inventory_model = 60147;
//                itemDef.name = "Black AOD Boots";
//                itemDef.zoom2d = 690;
//                itemDef.xan2d = 40;
//                itemDef.yan2d = 75;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60147;
//                itemDef.womanwear = 60147;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30102:
//                itemDef.setDefaults();
//                itemDef.id = 30102;
//                itemDef.inventory_model = 60150;
//                itemDef.name = "Teal AOD Gloves";
//                itemDef.zoom2d = 500;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 120;
//                itemDef.zan2d = 0;
//                itemDef.resizeX = 50;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 40;
//                itemDef.manwear = 60150;
//                itemDef.womanwear = 60150;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30103:
//                itemDef.setDefaults();
//                itemDef.id = 30103;
//                itemDef.inventory_model = 60151;
//                itemDef.name = "Teal Aod Boots";
//                itemDef.zoom2d = 690;
//                itemDef.xan2d = 40;
//                itemDef.yan2d = 75;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60151;
//                itemDef.womanwear = 60151;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30104:
//                itemDef.setDefaults();
//                itemDef.id = 30104;
//                itemDef.inventory_model = 60154;
//                itemDef.name = "Red AOD Gloves";
//                itemDef.zoom2d = 500;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 120;
//                itemDef.zan2d = 0;
//                itemDef.resizeX = 50;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 40;
//                itemDef.manwear = 60154;
//                itemDef.womanwear = 60154;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30105:
//                itemDef.setDefaults();
//                itemDef.id = 30105;
//                itemDef.inventory_model = 60155;
//                itemDef.name = "Red AOD Boots";
//                itemDef.zoom2d = 690;
//                itemDef.xan2d = 40;
//                itemDef.yan2d = 75;
//                itemDef.zan2d = 0;
//                itemDef.glowColor = 0xff19;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60155;
//                itemDef.womanwear = 60155;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = null;
//                break;
//            case 30106:
//                itemDef.setDefaults();
//                itemDef.id = 30106;
//                itemDef.inventory_model = 60156;
//                itemDef.name = "Teal AOD Chest";
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 200;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60156;
//                itemDef.womanwear = 60156;
//                itemDef.interfaceOptions = new String[] { "Open", "Open", "View-Loots", "Quick-Open", "Drop" };
//                break;
//            case 30107:
//                itemDef.setDefaults();
//                itemDef.id = 30107;
//                itemDef.inventory_model = 60156;
//                itemDef.name = "Teal AOD Casket";
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 200;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60156;
//                itemDef.womanwear = 60156;
//                itemDef.interfaceOptions = new String[] { "Open", "Open", "View-Loots", "Quick-Open", "Drop" };
//                break;
//            case 30108:
//                itemDef.setDefaults();
//                itemDef.id = 30108;
//                itemDef.inventory_model = 65278;
//                itemDef.name = "Angel Of Death Pet";
//                itemDef.zoom2d = 4500;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 65278;
//                itemDef.womanwear = 65278;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Drop";
//                itemDef.interfaceOptions[2] = null;
//
//                break;
//            case 30109:
//                itemDef.setDefaults();
//                itemDef.id = 30109;
//                itemDef.inventory_model = 60157;
//                itemDef.name = "Black AOD chest";
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 200;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60157;
//                itemDef.womanwear = 60157;
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                break;
//            case 30110:
//                itemDef.setDefaults();
//                itemDef.id = 30110;
//                itemDef.inventory_model = 60158;
//                itemDef.name = "Red AOD chest";
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 200;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60158;
//                itemDef.womanwear = 60158;
//                itemDef.interfaceOptions = new String[] { "Open", "Open", "View-Loots", "Quick-Open", "Drop" };
//                break;
//            case 30111:
//                itemDef.setDefaults();
//                itemDef.id = 30111;
//                itemDef.inventory_model = 60159;
//                itemDef.name = "AOD Loot Chest";
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 0;
//                itemDef.yan2d = 200;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60159;
//                itemDef.womanwear = 60159;
//                itemDef.interfaceOptions = new String[] { "Open", "Open", "View-Loots", "Quick-Open", "Drop" };
//                break;
//            case 30112:
//                itemDef.setDefaults();
//                itemDef.id = 30112;
//                itemDef.inventory_model = 60160;
//                itemDef.name = "AOD T2 Mage Power up";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 60160;
//                itemDef.womanwear = 60160;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30113:
//                itemDef.setDefaults();
//                itemDef.id = 30113;
//                itemDef.inventory_model = 64091;
//                itemDef.name = "NULL NEW ITEM";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64092;
//                itemDef.womanwear = 64092;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30114:
//                itemDef.setDefaults();
//                itemDef.id = 30114;
//                itemDef.inventory_model = 64091;
//                itemDef.name = "NULL NEW ITEM";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64092;
//                itemDef.womanwear = 64092;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30115:
//                itemDef.setDefaults();
//                itemDef.id = 30115;
//                itemDef.inventory_model = 64091;
//                itemDef.name = "NULL NEW ITEM";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64092;
//                itemDef.womanwear = 64092;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30116:
//                itemDef.setDefaults();
//                itemDef.id = 30116;
//                itemDef.inventory_model = 64091;
//                itemDef.name = "NULL NEW ITEM";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64092;
//                itemDef.womanwear = 64092;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30117:
//                itemDef.setDefaults();
//                itemDef.id = 30117;
//                itemDef.inventory_model = 64091;
//                itemDef.name = "NULL NEW ITEM";
//                itemDef.zoom2d = 2400;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = -2;
//                itemDef.yOffset2d = -4;
//                itemDef.manwear = 64092;
//                itemDef.womanwear = 64092;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29997:
//                itemDef.setDefaults();
//                itemDef.id = 29997;
//                itemDef.inventory_model = 65447;//65447
//                itemDef.name = "Big Ass Defender";
//                itemDef.zoom2d = 2500;
//                itemDef.xan2d = 900;
//                itemDef.yan2d = 800;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -10;
//                itemDef.manwear = 65447;
//                itemDef.womanwear = 65447;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28028:
//                itemDef.setDefaults();
//                itemDef.id = 28028;
//                itemDef.inventory_model = 65192;
//                itemDef.name = "Gangsta Bow";
//                itemDef.zoom2d = 3000;
//                itemDef.xan2d = 450; // towards and away
//                itemDef.yan2d = 90; // left to right
//                itemDef.zan2d = 15; // toward and away
//                itemDef.xOffset2d = 40;
//                itemDef.yOffset2d = 0;
//                itemDef.manwear = 65192;
//                itemDef.womanwear = 65192;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 8471:
//                itemDef.setDefaults();
//                itemDef.id = 8471;
//                itemDef.inventory_model = 65326;
//                itemDef.name = "Surge gloves";
//                itemDef.zoom2d = 917;
//                itemDef.xan2d = 420;
//                itemDef.yan2d = 1082;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -1;
//                itemDef.manwear = 65325;
//                itemDef.womanwear = 65325;
//                //itemDef.groundOptions = new String[5];
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 8473:
//                itemDef.setDefaults();
//                itemDef.id = 8473;
//                itemDef.inventory_model = 65327;
//                itemDef.name = "Surge boots";
//                itemDef.zoom2d = 976;
//                itemDef.xan2d = 147;
//                itemDef.yan2d = 279;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 5;
//                itemDef.yOffset2d = -5;
//                itemDef.manwear = 65327;
//                itemDef.womanwear = 65327;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 8475:
//                itemDef.setDefaults();
//                itemDef.id = 8475;
//                itemDef.inventory_model = 65329;
//                itemDef.name = "Surge wand";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.zan2d = 84;
//                itemDef.xOffset2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.cost = 5000000;
//                itemDef.manwear = 65328;
//                itemDef.womanwear = 65328;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 8477:
//                itemDef.setDefaults();
//                itemDef.id = 8477;
//                itemDef.inventory_model = 65329;
//                itemDef.name = "Surge Wand Uncharged";
//                itemDef.zoom2d = 800;
//                itemDef.xan2d = 11;
//                itemDef.yan2d = 152;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 25;
//                itemDef.yOffset2d = 0;
//                itemDef.manwear = 65328;
//                itemDef.womanwear = 65328;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 23890:
//                itemDef.setDefaults();
//                itemDef.id = 23890;
//                itemDef.inventory_model = 38769;
//                itemDef.name = "S";
//                itemDef.colorFind = new int[]{-22440};
//                itemDef.zoom2d = 800;
//                itemDef.xan2d = 11;
//                itemDef.yan2d = 152;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 25;
//                itemDef.yOffset2d = 0;
//                itemDef.manwear = 38104;
//                itemDef.womanwear = 38104;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28033:
//                itemDef.setDefaults();
//                itemDef.id = 28033;
//                itemDef.name = "Divine Spirit Shield";
//                itemDef.zoom2d = 1750;
//                itemDef.inventory_model = 40939; // was a sprite but wouldnt load
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 300;
//                itemDef.zan2d = 100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.ambient = 15;
//                itemDef.contrast = 25;
//                itemDef.cost = 5000000;
//                itemDef.manwear = 40939;
//                itemDef.womanwear = 40939;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28046:
//                itemDef.setDefaults();
//                itemDef.id = 28046;
//                itemDef.inventory_model = 62709;
//                itemDef.name = "pernix body";
//                itemDef.zoom2d = 1350;
//                itemDef.xan2d = 473;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 62744;
//                itemDef.womanwear = 62744;
//                itemDef.interfaceOptions = new String[5];  /// 1 sec
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28045:
//                itemDef.setDefaults();
//                itemDef.id = 28045 ;
//                itemDef.inventory_model = 62693;
//                itemDef.name = "Pernix full helm";
//                itemDef.zoom2d = 550;  // up makes smaller
//                itemDef.xan2d = 300; // spins vertically
//                itemDef.yan2d = 0; //spins item horizontal
//                itemDef.zan2d = 0; // angle
//                itemDef.xOffset2d = 5;  // left to right
//                itemDef.yOffset2d = 5; // up and down
//                itemDef.manwear = 62739;
//                itemDef.womanwear = 62739;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28047:
//                itemDef.setDefaults();
//                itemDef.id = 28047 ;
//                itemDef.inventory_model = 62695;
//                itemDef.name = "Pernix Chaps";
//                itemDef.zoom2d = 1600;  // up makes smaller
//                itemDef.xan2d = 474;
//                itemDef.zan2d = 2045;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 62741;
//                itemDef.womanwear = 62741;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28051:
//                itemDef.setDefaults();
//                itemDef.id = 28051;
//                itemDef.inventory_model = 62710;
//                itemDef.name = "Virtus Mask";
//                itemDef.zoom2d = 550;  // up makes smaller
//                itemDef.xan2d = 300; // spins vertically
//                itemDef.yan2d = 0; //spins item horizontal
//                itemDef.zan2d = 0; // angle
//                itemDef.xOffset2d = 1;  // left to right
//                itemDef.yOffset2d = 9; // up and down
//                itemDef.manwear = 62736;
//                itemDef.womanwear = 62736;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28052:
//                itemDef.setDefaults();
//                itemDef.id = 28052;
//                itemDef.inventory_model = 62704;
//                itemDef.name = "Virtus Robe Top";
//                itemDef.zoom2d = 1000;
//                itemDef.xan2d = 473;
//                itemDef.zan2d = 2042;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 62748;
//                itemDef.womanwear = 62748;
//                //itemDef.groundActions = new String[5];
//                //itemDef.groundActions[2] = "Take";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28053:
//                itemDef.setDefaults();
//                itemDef.id = 28053;
//                itemDef.inventory_model = 62700;
//                itemDef.name = "Virtus Robe Legs";
//                itemDef.zoom2d = 1500;  // up makes smaller
//                itemDef.xan2d = 474;
//                itemDef.zan2d = 2045;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 5;
//                itemDef.manwear = 62742;
//                itemDef.womanwear = 62742;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 28050:
//                itemDef.setDefaults();
//                itemDef.id = 28050 ;
//                itemDef.inventory_model = 56294;
//                itemDef.name = "Chaotic Maul";
//                itemDef.zoom2d = 1400;
//                itemDef.xan2d = 438;
//                itemDef.yan2d = 40;
//                itemDef.zan2d = 84;
//                itemDef.xOffset2d = 15;
//                itemDef.yOffset2d = -3;
//                itemDef.cost = 5000000;
//                itemDef.manwear = 56294;
//                itemDef.womanwear = 56294;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29000:
//                itemDef.setDefaults();
//                itemDef.id = 29000;
//                itemDef.inventory_model = 65320;
//                itemDef.name = "Surge helm";
//                itemDef.zoom2d = 672;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 65319;
//                itemDef.womanwear = 65319;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30043:
//                itemDef.setDefaults();
//                itemDef.id = 30043;
//                itemDef.inventory_model = 60002;
//                itemDef.name = "Dragonball platebody";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60002;
//                itemDef.womanwear = 60002;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30044:
//                itemDef.setDefaults();
//                itemDef.id = 30044;
//                itemDef.inventory_model = 60000;
//                itemDef.name = "Dragonball platelegs";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60000;
//                itemDef.womanwear = 60000;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30045:
//                itemDef.setDefaults();
//                itemDef.id = 30045;
//                itemDef.inventory_model = 60003;
//                itemDef.name = "Dragonball gloves";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60003;
//                itemDef.womanwear = 60003;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30046:
//                itemDef.setDefaults();
//                itemDef.id = 30046;
//                itemDef.inventory_model = 60001;
//                itemDef.name = "Dragonball boots";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60001;
//                itemDef.womanwear = 60001;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30047:
//                itemDef.setDefaults();
//                itemDef.id = 30047;
//                itemDef.inventory_model = 60011;
//                itemDef.name = "Storm Trooper Helm";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60011;
//                itemDef.womanwear = 60011;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30048:
//                itemDef.setDefaults();
//                itemDef.id = 30048;
//                itemDef.inventory_model = 60010;
//                itemDef.name = "Storm Trooper Platebody";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60010;
//                itemDef.womanwear = 60010;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30049:
//                itemDef.setDefaults();
//                itemDef.id = 30049;
//                itemDef.inventory_model = 60009;
//                itemDef.name = "Storm Trooper Platelegs";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 60009;
//                itemDef.womanwear = 60009;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 30050:
//                itemDef.setDefaults();
//                itemDef.id = 30050;
//                itemDef.inventory_model = 64390;
//                itemDef.name = "red cape";
//                itemDef.zoom2d = 1500;
//                itemDef.xan2d = 80;
//                itemDef.zan2d = 2040;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = -3;
//                itemDef.manwear = 64390;
//                itemDef.womanwear = 64390;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29981:
//                itemDef.id = 29981;
//                itemDef.name = "@blu@TierI Box Of Randomness";
//                itemDef.interfaceOptions = new String[] { "Open", "Open", "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55597;
//                itemDef.manwear = 55597;
//                itemDef.womanwear = 55597; // can always remove all customs from dono store real quick to see
//                itemDef.countObj = null; // stackids // since mark changed model part... thiers at least 20 models that wont load now
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 150; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 3800;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 29979:
//                itemDef.id = 29979;
//                itemDef.name = "@red@TierIII Box Of Randomness.";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55595;
//                itemDef.manwear = 55595;
//                itemDef.womanwear = 55595;
//                itemDef.countObj = null;
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 150; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 3800;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 29986:
//                itemDef.id = 29986;
//                itemDef.name = "@gre@Supplies Box";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 59006;
//                itemDef.manwear = 59006;
//                itemDef.womanwear = 59006;
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 50; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 1000;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 29973:
//                itemDef.id = 29973;
//                itemDef.name = "@red@Youtubers Wetdream";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55606;
//                itemDef.manwear = 55606;
//                itemDef.womanwear = 55606;
//                itemDef.countObj = null;
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 0; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 2000;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 29974:
//                itemDef.id = 29974;
//                itemDef.name = "@or2@An Irons Dream";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55569;
//                itemDef.manwear = 55569;
//                itemDef.womanwear = 55569;
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 0; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 2000;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 29972:
//                itemDef.id = 29972;
//                itemDef.name = "@pur@Masters Box Of Grattitude";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55570;
//                itemDef.manwear = 55570;
//                itemDef.countObj = null;
//                itemDef.womanwear = 2500; //27844 //5060
//                itemDef.colorFind = new int[]{5060};
//                itemDef.colorReplace = new int[]{27844};
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 0; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 2000;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//
//            case 27374:
//                itemDef.id = 27374;
//                itemDef.name = "@pur@Masori Assembler";
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                itemDef.inventory_model = 46525;
//                itemDef.manwear = 46502;
//                itemDef.womanwear = 46507; //27844 //5060
//                itemDef.colorFind = new int[]{-27483, -27492, -27500};
//                itemDef.colorReplace = new int[]{-32746, -32626, -32630};
//                itemDef.xan2d = 250; // towards and away
//                itemDef.yan2d = 0; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 1100;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//
//            case 29971:
//                itemDef.id = 29971;
//                itemDef.name = "@pur@Endgame Content OnlyFans";
//                itemDef.interfaceOptions = new String[] { "Open", null, "View-Loots", "Quick-Open", "Drop" };
//                itemDef.inventory_model = 55568;
//                itemDef.manwear = 55568;
//                itemDef.womanwear = 55568;
//                itemDef.countObj = null;
//                itemDef.xan2d = 0; // towards and away
//                itemDef.yan2d = 0; // left to right rotation
//                itemDef.zan2d = 0; // toward and away
//                itemDef.zoom2d = 2000;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                break;
//            case 29985: // missing model
//                itemDef.id = 29985; //twisted bow
//                itemDef.name = "twisted bow (p)";
//                itemDef.inventory_model = 64119;//64119
//                itemDef.manwear = 64119;//64119
//                itemDef.womanwear = 64119;
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 279; // towards and away
//                itemDef.yan2d = 75; // left to right
//                itemDef.zan2d = 100; // toward and away
//                itemDef.countObj = null;
//                itemDef.xOffset2d = 1;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//            case 29988:
//                itemDef.id = 29988;
//                itemDef.name = "Twisted Bow (L)";// twisted bow
//                itemDef.interfaceOptions = new String[]{null, "Wear", null, null, "Drop"};
//                itemDef.inventory_model = 58974;
//                itemDef.manwear = 58973;
//                itemDef.womanwear = 58973;
//                itemDef.zoom2d = 1700;
//                itemDef.xan2d = 279;
//                itemDef.yan2d = 75;
//                itemDef.zan2d = 100;
//                itemDef.countObj = null;
//                itemDef.xOffset2d = 1;
//                itemDef.yOffset2d = 0;
//                itemDef.stackable = false;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                itemDef.interfaceOptions[2] = "Teleports";
//                itemDef.interfaceOptions[3] = "Features";
//                itemDef.interfaceOptions[4] = "Drop";
//                break;
//
//            case 33057:
//                itemDef.setDefaults();
//                itemDef.id = 33057;
//                itemDef.inventory_model = 65273;
//                itemDef.name = "Completionist hood";
//                itemDef.zoom2d = 760;
//                itemDef.xan2d = 11;
//                itemDef.yan2d = 0;
//                itemDef.zan2d = 0;
//                itemDef.xOffset2d = 0;
//                itemDef.yOffset2d = 0;
//                itemDef.manwear = 65292;
//                itemDef.womanwear = 65310;
//                itemDef.interfaceOptions = new String[5];
//                itemDef.interfaceOptions[1] = "Wear";
//                break;
        }
    }
    public static ItemDefinition lookup(int itemId) {
        for (int count = 0; count < 10; count++) {
            if ((cache[count]).id == itemId)
                return cache[count];
        }
        cacheIndex = (cacheIndex + 1) % 10;
        ItemDefinition itemDef = cache[cacheIndex];
        if (itemId > 0)
            item_data.currentPosition = streamIndices[itemId];
        itemDef.id = itemId;
        itemDef.setDefaults();
        itemDef.decode(item_data);

        if (itemDef.noted_item_id != -1) {
            itemDef.toNote();
        }
//        if (newCustomItems(itemId) != null) {
//            return newCustomItems(itemId);
//        }

    customItems(itemId);

        return itemDef;
    }
    public static ItemDefinition copy(ItemDefinition itemDef, int newId, int copyingItemId, String newName, String... actions) {
        ItemDefinition copyItemDef = lookup(copyingItemId);
        itemDef.id = newId;
        itemDef.name = newName;
        itemDef.colorFind = copyItemDef.colorFind;
        itemDef.colorReplace = copyItemDef.colorReplace;
        itemDef.inventory_model = copyItemDef.inventory_model;
        itemDef.manwear = copyItemDef.manwear;
        itemDef.womanwear = copyItemDef.womanwear;
        itemDef.zoom2d = copyItemDef.zoom2d;
        itemDef.xan2d = copyItemDef.xan2d;
        itemDef.yan2d = copyItemDef.yan2d;
        itemDef.xOffset2d = copyItemDef.xOffset2d;
        itemDef.yOffset2d = copyItemDef.yOffset2d;
        itemDef.interfaceOptions = copyItemDef.interfaceOptions;
        itemDef.interfaceOptions = new String[5];
        if (actions != null) {
            for (int index = 0; index < actions.length; index++) {
                itemDef.interfaceOptions[index] = actions[index];
            }
        }
        return itemDef;
    }



    void method2789(ItemDefinition var1, ItemDefinition var2) {
        inventory_model = var1.inventory_model * 1;
        zoom2d = var1.zoom2d * 1;
        xan2d = 1 * var1.xan2d;
        yan2d = 1 * var1.yan2d;
        zan2d = 1 * var1.zan2d;
        xOffset2d = 1 * var1.xOffset2d;
        yOffset2d = var1.yOffset2d * 1;
        colorReplace = var2.colorReplace;
        colorFind = var2.colorFind;
//        originalTextureColors = var2.originalTextureColors;
//        modifiedTextureColors = var2.modifiedTextureColors;
        textureFind = var2.textureFind;
        textureReplace = var2.textureReplace;
        name = var2.name;
        members = var2.members;
        stackable = var2.stackable;
        manwear = 1 * var2.manwear;
        manwear2 = 1 * var2.manwear2;
        manwear3 = 1 * var2.manwear3;
        womanwear = var2.womanwear * 1;
        womanwear2 = var2.womanwear2 * 1;
        womanwear3 = 1 * var2.womanwear3;
        manhead = 1 * var2.manhead;
        manhead2 = var2.manhead2 * 1;
        womanhead = var2.womanhead * 1;
        womanhead2 = var2.womanhead2 * 1;
        team = var2.team * 1;
        options = var2.options;
        interfaceOptions = new String[5];
        equipActions = new String[5];
        if (null != var2.interfaceOptions) {
            for (int var4 = 0; var4 < 4; ++var4) {
                interfaceOptions[var4] = var2.interfaceOptions[var4];
            }
        }

        interfaceOptions[4] = "Discard";
        cost = 0;
    }

    void toPlaceholder(ItemDefinition var1, ItemDefinition var2) {
        inventory_model = var1.inventory_model * 1;
        zoom2d = 1 * var1.zoom2d;
        xan2d = var1.xan2d * 1;
        yan2d = var1.yan2d * 1;
        zan2d = var1.zan2d * 1;
        xOffset2d = 1 * var1.xOffset2d;
        yOffset2d = var1.yOffset2d * 1;
        colorReplace = var1.colorReplace;
        colorFind = var1.colorFind;
        textureFind = var1.textureFind;
        textureReplace = var1.textureReplace;
        stackable = var1.stackable;
        name = var2.name;
        cost = 0;
    }

    public static Sprite getSmallSprite(int itemId) {
        return getSmallSprite(itemId, 1);
    }

    public static Sprite getSmallSprite(int itemId, int stackSize) {

        ItemDefinition itemDef = lookup(itemId);
        if (itemDef.countObj == null)
            stackSize = -1;
        if (stackSize > 1) {
            int stack_item_id = -1;
            for (int j1 = 0; j1 < 10; j1++)
                if (stackSize >= itemDef.countCo[j1] && itemDef.countCo[j1] != 0)
                    stack_item_id = itemDef.countObj[j1];

            if (stack_item_id != -1)
                itemDef = lookup(stack_item_id);
        }
        Model model = itemDef.getModel(1);
        if (model == null)
            return null;
        Sprite sprite = null;
        if (itemDef.noted_item_id != -1) {
            sprite = getSprite(itemDef.unnoted_item_id, 10, -1);
            if (sprite == null)
                return null;
        }
        Sprite enabledSprite = new Sprite(18, 18);
        int centerX = Rasterizer3D.originViewX;
        int centerY = Rasterizer3D.originViewY;
        int[] lineOffsets = Rasterizer3D.scanOffsets;
        int[] pixels = Rasterizer2D.pixels;
        int width = Rasterizer2D.width;
        int height = Rasterizer2D.height;
        int vp_left = Rasterizer2D.leftX;
        int vp_right = Rasterizer2D.bottomX;
        int vp_top = Rasterizer2D.topY;
        int vp_bottom = Rasterizer2D.bottomY;
        Rasterizer3D.world = false;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer2D.initDrawingArea(18, 18, enabledSprite.myPixels);
        Rasterizer2D.drawItemBox(0, 0, 18, 18, 0);
        Rasterizer3D.useViewport();
        int k3 = itemDef.zoom2d;

        int l3 = Rasterizer3D.SINE[itemDef.xan2d] * k3 >> 16;
        int i4 = Rasterizer3D.COSINE[itemDef.xan2d] * k3 >> 16;
        Rasterizer3D.renderOnGpu = true;
        model.renderModel(itemDef.yan2d, itemDef.zan2d, itemDef.xan2d, itemDef.xOffset2d,
                l3 + model.modelBaseY / 2 + itemDef.yOffset2d, i4 + itemDef.yOffset2d);

        enabledSprite.outline(1);

        Rasterizer2D.initDrawingArea(32, 32, enabledSprite.myPixels);

        if (itemDef.noted_item_id != -1) {
            int old_w = sprite.maxWidth;
            int old_h = sprite.maxHeight;
            sprite.maxWidth = 18;
            sprite.maxHeight = 18;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = old_w;
            sprite.maxHeight = old_h;
        }

        sprites.put(enabledSprite, itemId);
        Rasterizer2D.initDrawingArea(height, width, pixels);
        Rasterizer2D.setDrawingArea(vp_bottom, vp_left, vp_right, vp_top);
        Rasterizer3D.originViewX = centerX;
        Rasterizer3D.originViewY = centerY;
        Rasterizer3D.scanOffsets = lineOffsets;
        Rasterizer3D.aBoolean1464 = true;
        Rasterizer3D.world = true;
        if (itemDef.stackable)
            enabledSprite.maxWidth = 18;
        else
            enabledSprite.maxWidth = 18;
        enabledSprite.maxHeight = stackSize;
        return enabledSprite;
    }


    public byte[] customSpriteLocation;
    public byte[] customSmallSpriteLocation;

    public void createCustomSprite(String img) {
        customSpriteLocation = getCustomSprite(img);
    }

    public void createSmallCustomSprite(String img) {
        customSmallSpriteLocation = getCustomSprite(img);
    }


    public byte[] getCustomSprite(String img) {
        String location = (Sprite.location + Configuration.CUSTOM_ITEM_SPRITES_DIRECTORY + img).toLowerCase();
        byte[] spriteData = FileOperations.readFile(location);
        Preconditions.checkState(spriteData != null, "No sprite: " + location);
        return spriteData;
    }

    public static Sprite getSprite(int itemId, int stackSize, int outlineColor) { // show me the model method
        if (outlineColor == 0) {
            Sprite sprite = (Sprite) sprites.get(itemId);
            if (sprite != null && sprite.maxHeight != stackSize && sprite.maxHeight != -1) {

                sprite.unlink();
                sprite = null;
            }
            if (sprite != null)
                return sprite;
        }
        ItemDefinition itemDef = lookup(itemId);
        if (itemDef.countObj == null)
            stackSize = -1;
        if (stackSize > 1) {
            int stack_item_id = -1;
            for (int j1 = 0; j1 < 10; j1++)
                if (stackSize >= itemDef.countCo[j1] && itemDef.countCo[j1] != 0)
                    stack_item_id = itemDef.countObj[j1];

            if (stack_item_id != -1) {
                itemDef = lookup(stack_item_id);
            }
        }
        Model model = itemDef.getModel(1);
        if (model == null)
            return null;
        Sprite sprite = null;
        if (itemDef.noted_item_id != -1) {
            if (IntStream.of(ItemDefinition.customspriteitems).anyMatch(x -> x == itemId)) {
                sprite = new Sprite(itemDef.customSpriteLocation);
            } else {
                sprite = getSprite(itemDef.unnoted_item_id, 10, -1);
            }

            if (sprite == null)
                return null;
        }
        Sprite enabledSprite = new Sprite(32, 32);
        int centerX = Rasterizer3D.originViewX;
        int centerY = Rasterizer3D.originViewY;
        int[] lineOffsets = Rasterizer3D.scanOffsets;
        int[] pixels = Rasterizer2D.pixels;
        int width = Rasterizer2D.width;
        int height = Rasterizer2D.height;
        int vp_left = Rasterizer2D.leftX;
        int vp_right = Rasterizer2D.bottomX;
        int vp_top = Rasterizer2D.topY;
        int vp_bottom = Rasterizer2D.bottomY;
        Rasterizer3D.world = false;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer2D.initDrawingArea(32, 32, enabledSprite.myPixels);
        Rasterizer2D.drawItemBox(0, 0, 32, 32, 0);
        Rasterizer3D.useViewport();
        int k3 = itemDef.zoom2d;
        if (outlineColor == -1)
            k3 = (int) ((double) k3 * 1.5D);
        if (outlineColor > 0)
            k3 = (int) ((double) k3 * 1.04D);
        int l3 = Rasterizer3D.SINE[itemDef.xan2d] * k3 >> 16;
        int i4 = Rasterizer3D.COSINE[itemDef.xan2d] * k3 >> 16;
        Rasterizer3D.renderOnGpu = true;
        model.renderModel(itemDef.yan2d, itemDef.zan2d, itemDef.xan2d, itemDef.xOffset2d,
                l3 + model.modelBaseY / 2 + itemDef.yOffset2d, i4 + itemDef.yOffset2d);

        enabledSprite.outline(1);
        if (outlineColor > 0) {
            enabledSprite.outline(16777215);
        }
        if (outlineColor == 0) {
            enabledSprite.shadow(3153952);
        }

        Rasterizer2D.initDrawingArea(32, 32, enabledSprite.myPixels);

        if (itemDef.noted_item_id != -1) {
            int old_w = sprite.maxWidth;
            int old_h = sprite.maxHeight;
            sprite.maxWidth = 32;
            sprite.maxHeight = 32;
            sprite.drawSprite(0, 0);
            sprite.maxWidth = old_w;
            sprite.maxHeight = old_h;
        }
        if (outlineColor == 0)
            sprites.put(enabledSprite, itemId);
        Rasterizer2D.initDrawingArea(height, width, pixels);
        Rasterizer2D.setDrawingArea(vp_bottom, vp_left, vp_right, vp_top);
        Rasterizer3D.originViewX = centerX;
        Rasterizer3D.originViewY = centerY;
        Rasterizer3D.scanOffsets = lineOffsets;
        Rasterizer3D.aBoolean1464 = true;
        Rasterizer3D.world = true;
        if (itemDef.stackable)
            enabledSprite.maxWidth = 33;
        else
            enabledSprite.maxWidth = 32;
        enabledSprite.maxHeight = stackSize;
        return enabledSprite;
    }

    public static Sprite getSprite(int itemId, int stackSize, int zoom, int outlineColor) {
        ItemDefinition itemDef = lookup(itemId);
        if (itemDef.countObj == null)
            stackSize = -1;
        if (stackSize > 1) {
            int stack_item_id = -1;
            for (int j1 = 0; j1 < 10; j1++)
                if (stackSize >= itemDef.countCo[j1] && itemDef.countCo[j1] != 0)
                    stack_item_id = itemDef.countObj[j1];

            if (stack_item_id != -1)
                itemDef = lookup(stack_item_id);
        }
        Model model = itemDef.getModel(1);
        if (model == null)
            return null;
        Sprite sprite = new Sprite(90, 90);
        int centerX = Rasterizer3D.originViewX;
        int centerY = Rasterizer3D.originViewY;
        int[] lineOffsets = Rasterizer3D.scanOffsets;
        int[] pixels = Rasterizer2D.pixels;
        int width = Rasterizer2D.width;
        int height = Rasterizer2D.height;
        int vp_left = Rasterizer2D.leftX;
        int vp_right = Rasterizer2D.bottomX;
        int vp_top = Rasterizer2D.topY;
        int vp_bottom = Rasterizer2D.bottomY;
        Rasterizer3D.world = false;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer2D.initDrawingArea(90, 90, sprite.myPixels);
        Rasterizer2D.drawItemBox(0, 0, 90, 90, 0);
        Rasterizer3D.useViewport();
        int l3 = Rasterizer3D.SINE[itemDef.xan2d] * zoom >> 15;
        int i4 = Rasterizer3D.COSINE[itemDef.xan2d] * zoom >> 15;
        Rasterizer3D.renderOnGpu = true;

        model.renderModel(itemDef.yan2d, itemDef.zan2d, itemDef.xan2d, itemDef.xOffset2d,
                l3 + model.modelBaseY / 2 + itemDef.yOffset2d, i4 + itemDef.yOffset2d);

        Rasterizer3D.renderOnGpu = false;
        sprite.outline(1);
        if (outlineColor > 0) {
            sprite.outline(16777215);
        }
        if (outlineColor == 0) {
            sprite.shadow(3153952);
        }
        Rasterizer2D.initDrawingArea(90, 90, sprite.myPixels);
        Rasterizer2D.initDrawingArea(height, width, pixels);
        Rasterizer2D.setDrawingArea(vp_bottom, vp_left, vp_right, vp_top);
        Rasterizer3D.originViewX = centerX;
        Rasterizer3D.originViewY = centerY;
        Rasterizer3D.scanOffsets = lineOffsets;
        Rasterizer3D.aBoolean1464 = true;
        Rasterizer3D.world = true;
        if (itemDef.stackable)
            sprite.maxWidth = 33;
        else
            sprite.maxWidth = 32;
        sprite.maxHeight = stackSize;
        return sprite;
    }

    public boolean isDialogueModelCached(int gender) {
        int model_1 = manhead;
        int model_2 = manhead2;
        if (gender == 1) {
            model_1 = womanhead;
            model_2 = womanhead2;
        }
        if (model_1 == -1)
            return true;
        boolean cached = Model.isCached(model_1);
        if (model_2 != -1 && !Model.isCached(model_2))
            cached = false;
        return cached;
    }

    public Model getChatEquipModel(int gender) {
        int dialogueModel = manhead;
        int dialogueHatModel = manhead2;
        if (gender == 1) {
            dialogueModel = womanhead;
            dialogueHatModel = womanhead2;
        }
        if (dialogueModel == -1)
            return null;
        Model dialogueModel_ = Model.getModel(dialogueModel);
        if (dialogueHatModel != -1) {
            Model hatModel_ = Model.getModel(dialogueHatModel);
            Model[] models = {dialogueModel_, hatModel_};
            dialogueModel_ = new Model(2, models);
        }
        if (colorReplace != null) {
            for (int i1 = 0; i1 < colorReplace.length; i1++)
                dialogueModel_.recolor(colorReplace[i1], colorFind[i1]);

        }
        if (textureReplace != null) {
            for (int i1 = 0; i1 < textureReplace.length; i1++)
                dialogueModel_.retexture(textureReplace[i1], textureFind[i1]);
        }
        return dialogueModel_;
    }

    public boolean isEquippedModelCached(int gender) {
        int primaryModel = manwear;
        int secondaryModel = manwear2;
        int emblem = manwear3;
        if (gender == 1) {
            primaryModel = womanwear;
            secondaryModel = womanwear2;
            emblem = womanwear3;
        }
        if (primaryModel == -1)
            return true;
        boolean cached = Model.isCached(primaryModel);
        if (secondaryModel != -1 && !Model.isCached(secondaryModel))
            cached = false;
        if (emblem != -1 && !Model.isCached(emblem))
            cached = false;
        return cached;
    }

    public Model get_equipped_model(int gender) {
        int primaryModel = manwear;
        int secondaryModel = manwear2;
        int emblem = manwear3;

        if (gender == 1) {
            primaryModel = womanwear;
            secondaryModel = womanwear2;
            emblem = womanwear3;
        }

        if (primaryModel == -1)
            return null;
        Model primaryModel_ = Model.getModel(primaryModel);
        if (secondaryModel != -1)
            if (emblem != -1) {
                Model secondaryModel_ = Model.getModel(secondaryModel);
                Model emblemModel = Model.getModel(emblem);
                Model[] models = {primaryModel_, secondaryModel_, emblemModel};
                primaryModel_ = new Model(3, models);
            } else {
                Model model_2 = Model.getModel(secondaryModel);
                Model[] models = {primaryModel_, model_2};
                primaryModel_ = new Model(2, models);
            }
        if (gender == 0 && manwear_offset != 0)
            primaryModel_.offsetBy(0, manwear_offset, 0);
        if (gender == 1 && womanwear_offset != 0)
            primaryModel_.offsetBy(0, womanwear_offset, 0);

        if (colorReplace != null) {
            for (int i1 = 0; i1 < colorReplace.length; i1++)
                primaryModel_.recolor(colorReplace[i1], colorFind[i1]);

        }
        if (textureReplace != null) {
            for (int i1 = 0; i1 < textureReplace.length; i1++)
                primaryModel_.retexture(textureReplace[i1], textureFind[i1]);
        }
        return primaryModel_;
    }

    private void setDefaults() {
        equipActions = new String[]{"Remove", null, "Operate", null, null};
        inventory_model = 0;
        name = null;
        colorReplace = null;
        colorFind = null;
        textureReplace = null;
        textureFind = null;

        zoom2d = 2000;
        xan2d = 0;
        yan2d = 0;
        zan2d = 0;
        xOffset2d = 0;
        yOffset2d = 0;
        stackable = false;
        cost = 1;
        members = false;
        options = null;
        interfaceOptions = null;
        manwear = -1;
        manwear2 = -1;
        manwear_offset = 0;
        womanwear = -1;
        womanwear2 = -1;
        womanwear_offset = 0;
        manwear3 = -1;
        womanwear3 = -1;
        manhead = -1;
        manhead2 = -1;
        womanhead = -1;
        womanhead2 = -1;
        countObj = null;
        countCo = null;
        unnoted_item_id = -1;
        noted_item_id = -1;
        resizeX = 128;
        resizeY = 128;
        resizeZ = 128;
        ambient = 0;
        contrast = 0;
        team = 0;
        glowColor = -1;
    }

    private void copy(ItemDefinition copy) {
        yan2d = copy.yan2d;
        xan2d = copy.xan2d;
        zan2d = copy.zan2d;
        resizeX = copy.resizeX;
        resizeY = copy.resizeY;
        resizeZ = copy.resizeZ;
        zoom2d = copy.zoom2d;
        xOffset2d = copy.xOffset2d;
        yOffset2d = copy.yOffset2d;
        inventory_model = copy.inventory_model;
        stackable = copy.stackable;

    }

    private void toNote() {
        ItemDefinition itemDef = lookup(noted_item_id);
        inventory_model = itemDef.inventory_model;
        zoom2d = itemDef.zoom2d;
        xan2d = itemDef.xan2d;
        yan2d = itemDef.yan2d;

        zan2d = itemDef.zan2d;
        xOffset2d = itemDef.xOffset2d;
        yOffset2d = itemDef.yOffset2d;

        ItemDefinition itemDef_1 = lookup(unnoted_item_id);
        name = itemDef_1.name;
        members = itemDef_1.members;
        cost = itemDef_1.cost;
        stackable = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {

    }

    public int getContrast() {
        return contrast;
    }

    public int getAmbient() {
        return ambient;
    }
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public int getNote() {
        return 0;
    }

    @Override
    public int getLinkedNoteId() {
        return 0;
    }

    @Override
    public int getPlaceholderId() {
        return 0;
    }

    @Override
    public int getPlaceholderTemplateId() {
        return 0;
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public boolean isMembers() {
        return false;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public void setTradeable(boolean yes) {

    }

    @Override
    public int getIsStackable() {
        return 0;
    }

    @Override
    public int getMaleModel() {
        return 0;
    }

    @Override
    public String[] getInventoryActions() {
        return new String[0];
    }

    @Override
    public String[] getGroundActions() {
        return new String[0];
    }

    @Override
    public int getShiftClickActionIndex() {
        return 0;
    }

    @Override
    public void setShiftClickActionIndex(int shiftClickActionIndex) {

    }

    public Model getModel(int stack_size) {
        if (countObj != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int k = 0; k < 10; k++)
                if (stack_size >= countCo[k] && countCo[k] != 0)
                    stack_item_id = countObj[k];
//System.out.println
            if (stack_item_id != -1)
                return lookup(stack_item_id).getModel(1);
        }
        Model model = (Model) models.get(id);
        if (model != null)
            return model;
        model = Model.getModel(inventory_model);
        if (model == null)
            return null;
        if (resizeX != 128 || resizeY != 128 || resizeZ != 128)
            model.scale(resizeX, resizeZ, resizeY);
        if (colorReplace != null) {
            for (int l = 0; l < colorReplace.length; l++)
                model.recolor(colorReplace[l], colorFind[l]);

        }
        if (textureReplace != null) {
            for (int i1 = 0; i1 < textureReplace.length; i1++)
                model.retexture(textureReplace[i1], textureFind[i1]);
        }
        int lightInt = 64 + ambient;
        int lightMag = 768 + contrast;
        model.light(lightInt, lightMag, -50, -10, -50, true);
        model.singleTile = true;
        models.put(model, id);
        return model;
    }

    @Override
    public int getInventoryModel() {
        return 0;
    }

    @Override
    public short[] getColorToReplaceWith() {
        return new short[0];
    }

    @Override
    public short[] getTextureToReplaceWith() {
        return new short[0];
    }

    @Override
    public RSIterableNodeHashTable getParams() {
        return null;
    }

    @Override
    public void setParams(IterableHashTable params) {

    }

    @Override
    public void setParams(RSIterableNodeHashTable params) {

    }

    public Model getUnshadedModel(int stack_size) {
        if (countObj != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int count = 0; count < 10; count++)
                if (stack_size >= countCo[count] && countCo[count] != 0)
                    stack_item_id = countObj[count];

            if (stack_item_id != -1)
                return lookup(stack_item_id).getUnshadedModel(1);
        }
        Model model = Model.getModel(inventory_model);
        if (model == null)
            return null;
        if (colorReplace != null) {
            for (int colorPtr = 0; colorPtr < colorReplace.length; colorPtr++)
                model.recolor(colorReplace[colorPtr], colorFind[colorPtr]);

        }
        return model;
    }
    private void decode(Buffer1 stream) {
        while (true) {
            int opcode = stream.readUnsignedByte();
            if (opcode == 0)
                return;
            if (opcode == 1)
                inventory_model = stream.readUShort();
            else if (opcode == 2)
                name = stream.readString();
            else if (opcode == 3)
           stream.readString();
            else if (opcode == 4)
                zoom2d  = stream.readUShort();
            else if (opcode == 5)
                xan2d   = stream.readUShort();
            else if (opcode == 6)
                yan2d   = stream.readUShort();
            else if (opcode == 7) {
                xOffset2d   = stream.readUShort();
                if (xOffset2d   > 32767)
                    xOffset2d   -= 0x10000;
            } else if (opcode == 8) {
                yOffset2d = stream.readUShort();
                if (     yOffset2d > 32767)
                    yOffset2d -= 0x10000;
            } else if (opcode == 11)
                stackable = true;
            else if (opcode == 12)
                cost  = stream.readDWord();
            else if(opcode == 13)
                this.field2142 = stream.readSignedByte();
            else if(opcode == 14)
                this.field2157 = stream.readSignedByte();
            else if (opcode == 16)
                members = true;
            else if (opcode == 23) {
                manwear  = stream.readUShort();
                manwear_offset = stream.readSignedByte();
                if (     manwear  == 65535)
                    manwear  = -1;
            } else if (opcode == 24)
                manwear2  = stream.readUShort();
            else if (opcode == 25) {
                womanwear = stream.readUShort();
                womanwear_offset = stream.readSignedByte();
                if ( womanwear == 65535)
                    womanwear = -1;
            } else if (opcode == 26)
                womanwear2 = stream.readUShort();
            else if(opcode == 27)
                this.field2158 = stream.readSignedByte();
            else if (opcode >= 30 && opcode < 35) {
                if (options == null)
                    options = new String[5];
                options[opcode - 30] = stream.readString();
                if (options[opcode - 30].equalsIgnoreCase("hidden"))
                    options[opcode - 30] = null;
            } else if (opcode >= 35 && opcode < 40) {
                if (interfaceOptions == null)
                    interfaceOptions = new String[5];
                interfaceOptions[opcode - 35] = stream.readString();
            } else if (opcode == 40) {
                int size = stream.readUnsignedByte();
                colorReplace = new int[size];
                colorFind  = new int[size];
                for (int index = 0; index < size; index++) {
                    colorReplace[index] = stream.readUShort();
                    colorFind [index] = stream.readUShort();
                }
            } else if (opcode == 41) {
                int size = stream.readUnsignedByte();
                textureFind  = new short[size];
                textureReplace  = new short[size];
                for (int index = 0; index < size; index++) {
                    textureFind [index] = (short) stream.readUShort();
                    textureReplace [index] = (short) stream.readUShort();
                }
            }else if (opcode == 42) {
                stream.readUnsignedByte();
            } else if (opcode == 65) {
                tradeable  = true;
            } else if (opcode == 75){
                this.field2182 = stream.readSignedWord();
            } else if (opcode == 78)
                manwear3  = stream.readUShort();
            else if (opcode == 79)
                womanwear3  = stream.readUShort();
            else if (opcode == 90)
                manhead = stream.readUShort();
            else if (opcode == 91)
                womanhead = stream.readUShort();
            else if (opcode == 92)
                manhead2  = stream.readUShort();
            else if (opcode == 93)
                womanhead2 = stream.readUShort();
            else if (opcode == 94)
                stream.readUShort();
            else if (opcode == 95)
                zan2d  = stream.readUShort();
            else if (opcode == 97)
                unnoted_item_id = stream.readUShort();
            else if (opcode == 98)
                noted_item_id  = stream.readUShort();
            else if (opcode >= 100 && opcode < 110) {
                if (countObj == null) {
                    countObj = new int[10];
                    countCo = new int[10];
                }
                countObj [opcode - 100] = stream.readUShort();
                countCo[opcode - 100] = stream.readUShort();
            } else if (opcode == 110)
                resizeX  = stream.readUShort();
            else if (opcode == 111)
                resizeY  = stream.readUShort();
            else if (opcode == 112)
                resizeZ  = stream.readUShort();
            else if (opcode == 113)
                ambient = stream.readSignedByte();
            else if (opcode == 114)
                contrast = stream.readSignedByte() * 5;
            else if (opcode == 115)
                team = stream.readUnsignedByte();
            else if (opcode == 139)
                bought_id = stream.readUShort();
            else if (opcode == 140)
                bought_template_id = stream.readUShort();
            else if (opcode == 148)
              stream.readUShort();
            else if (opcode == 149)
          stream.readUShort();
            else {
                // System.out.println("Error loading item " + id + ", opcode " + opcode);
            }
        }
    }
//        private void decode(Buffer1 buffer) {
//        while (true) {
//            int opcode = buffer.readUnsignedByte();
//            if (opcode == 0)
//                return;
//            if (opcode == 1)
//                inventory_model = buffer.readUShort();
//            else if (opcode == 2)//ahh err
//                name = buffer.readStrings();
//            else if (opcode == 3)
//                buffer.readStrings();
//            else if (opcode == 4)
//                zoom2d = buffer.readUShort();
//            else if (opcode == 5)
//                xan2d = buffer.readUShort();
//            else if (opcode == 6)
//                yan2d = buffer.readUShort();
//            else if (opcode == 7) {
//                xOffset2d = buffer.readUShort();
//                if (xOffset2d > 32767)
//                    xOffset2d -= 0x10000;
//            } else if (opcode == 8) {
//                yOffset2d = buffer.readUShort();
//                if (yOffset2d > 32767)
//                    yOffset2d -= 0x10000;
//            } else if (opcode == 9)
//                buffer.readStrings();
//            else if (opcode == 11)
//                stackable = true;
//            else if (opcode == 12) {
//                cost = buffer.readInt();
//            } else if (opcode == 13) {
//                wearpos = buffer.readUnsignedByte();
//            } else if (opcode == 14) {
//                wearpos2 = buffer.readUnsignedByte();
//            } else if (opcode == 16)
//                members = true;
//            else if (opcode == 23) {
//                manwear = buffer.readUShort();
//                manwear_offset = buffer.readUnsignedByte();
//            } else if (opcode == 24)
//                manwear2 = buffer.readUShort();
//            else if (opcode == 25) {
//                womanwear = buffer.readUShort();
//                womanwear_offset = buffer.readUnsignedByte();
//            } else if (opcode == 26)
//                womanwear2 = buffer.readUShort();
//            else if(opcode == 27)
//                wearpos3 = buffer.readUnsignedByte();
//            else if (opcode >= 30 && opcode < 35) {
//                if (options == null)
//                    options = new String[5];
//                options[opcode - 30] = buffer.readString();
//                if (options[opcode - 30].equalsIgnoreCase("hidden"))
//                    options[opcode - 30] = null;
//            } else if (opcode >= 35 && opcode < 40) {
//                if (interfaceOptions == null)
//                    interfaceOptions = new String[5];
//                interfaceOptions[opcode - 35] = buffer.readString();
//            } else if (opcode == 40) {
//                int length = buffer.readUnsignedByte();
//                colorReplace = new int[length];
//                colorFind = new int[length];
//                for (int index = 0; index < length; index++) {
//                    colorFind[index] = buffer.readUShort();
//                    colorReplace[index] = buffer.readUShort();
//                }
//            } else if (opcode == 41) {
//                int length = buffer.readUnsignedByte();
//                textureFind = new short[length];
//                textureReplace = new short[length];
//                for (int index = 0; index < length; index++) {
//                    textureFind[index] = (short) buffer.readUShort();
//                    textureReplace[index] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 42) {
//                shiftClickIndex = buffer.readUnsignedByte();
//            } else if (opcode == 65) {
//                tradeable = true;
//            } else if (opcode == 75) {
//                weight = buffer.readShort();
//            } else if (opcode == 78)
//                manwear3 = buffer.readUShort();
//            else if (opcode == 79)
//                womanwear3 = buffer.readUShort();
//            else if (opcode == 90)
//                manhead = buffer.readUShort();
//            else if (opcode == 91)
//                womanhead = buffer.readUShort();
//            else if (opcode == 92)
//                manhead2 = buffer.readUShort();
//            else if (opcode == 93)
//                womanhead2 = buffer.readUShort();
//            else if (opcode == 94)
//                category = buffer.readUShort();
//            else if (opcode == 95)
//                zan2d = buffer.readUShort();
//            else if (opcode == 97)
//                unnoted_item_id = buffer.readUShort();
//            else if (opcode == 98)
//                noted_item_id = buffer.readUShort();
//            else if (opcode >= 100 && opcode < 110) {
//                if (countObj == null) {
//                    countObj = new int[10];
//                    countCo = new int[10];
//                }
//                countObj[opcode - 100] = buffer.readUShort();
//                countCo[opcode - 100] = buffer.readUShort();
//            } else if (opcode == 110)
//                resizeX = buffer.readUShort();
//            else if (opcode == 111)
//                resizeY = buffer.readUShort();
//            else if (opcode == 112)
//                resizeZ = buffer.readUShort();
//            else if (opcode == 113)
//                ambient = buffer.readSignedByte();
//            else if (opcode == 114)
//                contrast = buffer.readSignedByte() * 5;
//            else if (opcode == 115)
//                team = buffer.readUnsignedByte();
//            else if (opcode == 139)
//                bought_id = buffer.readUShort();
//            else if (opcode == 140)
//                bought_template_id = buffer.readUShort();
//            else if (opcode == 148)
//                placeholder_id = buffer.readUShort();
//            else if (opcode == 149) {
//                placeholder_template_id = buffer.readUShort();
//            } else if (opcode == 249) {
//                int length = buffer.readUnsignedByte();
//
//                HashMap<Integer, Object> params = new HashMap<>(length);
//
//                for (int i = 0; i < length; i++) {
//                    boolean isString = buffer.readUnsignedByte() == 1;
//                    int key = buffer.read24Int();
//                    Object value;
//
//                    if (isString) {
//                        value = buffer.readString();
//                    } else {
//                        value = buffer.readInt();
//                    }
//
//                    params.put(key, value);
//                }
//                return;
//            }
//            else {
//                System.err.printf("Error unrecognised {Items} opcode: %d%n%n", opcode);
//            }
//        }
//    }
//    public void decode(Buffer1 buffer) {
//        while (true) {
//            int opcode = buffer.readUByte();
//            if (opcode == 0)
//                return;
//            if (opcode == 1)
//                inventory_model = buffer.readUShort();
//            else if (opcode == 2)
//                name = buffer.readString();
//            else if (opcode == 3)
//                buffer.readStrings();
//            else if (opcode == 4)
//                zoom2d  = buffer.readUShort();
//            else if (opcode == 5)
//                xan2d  = buffer.readUShort();
//            else if (opcode == 6)
//                yan2d  = buffer.readUShort();
//            else if (opcode == 7) {
//                xOffset2d  = buffer.readUShort();
//                if (xOffset2d  > 32767)
//                    xOffset2d  -= 0x10000;
//            } else if (opcode == 8) {
//                yOffset2d  = buffer.readUShort();
//                if (yOffset2d  > 32767)
//                    yOffset2d  -= 0x10000;
//            } else if (opcode == 11)
//                stackable = true;
//            else if (opcode == 12)
//                cost = buffer.readInt();
//            else if (opcode == 16)
//                members = true;
//            else if (opcode == 23) {
//                manwear  = buffer.readUShort();
//                manwear_offset  = buffer.readSignedByte();
//            } else if (opcode == 24)
//                manwear2  = buffer.readUShort();
//            else if (opcode == 25) {
//                womanwear  = buffer.readUShort();
//                womanwear_offset  = buffer.readSignedByte();
//            } else if (opcode == 26)
//                womanwear2  = buffer.readUShort();
//            else if (opcode >= 30 && opcode < 35) {
//                if (options == null)
//                    options = new String[5];
//
//                options[opcode - 30] = buffer.readString();
//                if (options[opcode - 30].equalsIgnoreCase("hidden"))
//                    options[opcode - 30] = null;
//
//            } else if (opcode >= 35 && opcode < 40) {
//                if (interfaceOptions  == null)
//                    interfaceOptions  = new String[5];
//
//                interfaceOptions [opcode - 35] = buffer.readString();
//
//            } else if (opcode == 40) {
//                int length = buffer.readUByte();
//                //if models aren't recoloring properly, typically switch the position of src with dst
//                colorReplace  = new int[length];
//                colorFind  = new int[length];
//                for (int index = 0; index < length; index++) {
//                    colorFind[index] = buffer.readUShort();
//                    colorReplace[index] = buffer.readUShort();
//                }
//            } else if (opcode == 41) {
//                int length = buffer.readUByte();
//                textureFind  = new short[length];
//                textureReplace  = new short[length];
//                for (int index = 0; index < length; index++) {
//                    textureFind [index] = (short) buffer.readUShort();
//                    textureReplace [index] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 42) {
//                buffer.readUByte();//shift_menu_index
//            } else if (opcode == 65) {
//                tradeable  = true;
//            } else if (opcode == 78)
//                manwear3  = buffer.readUShort();
//            else if (opcode == 79)
//                womanwear3  = buffer.readUShort();
//            else if (opcode == 90)
//                manhead  = buffer.readUShort();
//            else if (opcode == 91)
//                womanhead  = buffer.readUShort();
//            else if (opcode == 92)
//                manhead2  = buffer.readUShort();
//            else if (opcode == 93)
//                womanhead2  = buffer.readUShort();
//            else if(opcode == 94)
//                buffer.readUShort();
//            else if (opcode == 95)
//                zan2d  = buffer.readUShort();
//            else if (opcode == 97)
//                unnoted_item_id = buffer.readUShort();
//            else if (opcode == 98)
//                noted_item_id = buffer.readUShort();
//            else if (opcode >= 100 && opcode < 110) {
//                if (countObj == null) {
//                    countObj = new int[10];
//                    countCo  = new int[10];
//                }
//                countObj[opcode - 100] = buffer.readUShort();
//                countCo [opcode - 100] = buffer.readUShort();
//            } else if (opcode == 110)
//                resizeX  = buffer.readUShort();
//            else if (opcode == 111)
//                resizeY  = buffer.readUShort();
//            else if (opcode == 112)
//                resizeZ  = buffer.readUShort();
//            else if (opcode == 113)
//                ambient = buffer.readSignedByte();
//            else if (opcode == 114)
//                contrast = buffer.readSignedByte(); //We had this as * 5 but runelite has it without * 5.
//            else if (opcode == 115)
//                team  = buffer.readUByte();
//            else if (opcode == 139)
//                bought_id  = buffer.readUShort();
//            else if (opcode == 140)
//                bought_template_id  = buffer.readUShort();
//            else if (opcode == 148)
//                buffer.readUShort(); // placeholder id
//            else if (opcode == 149) {
//                buffer.readUShort(); // placeholder template
//            } else if (opcode == 150) {
//                buffer.readString();
//            }
//        }
//    }
    @Override
    public int getHaPrice() {
        return 0;
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public void resetShiftClickActionIndex() {

    }

    @Override
    public int getIntValue(int paramID) {
        return 0;
    }

    @Override
    public void setValue(int paramID, int value) {

    }

    @Override
    public String getStringValue(int paramID) {
        return null;
    }

    @Override
    public void setValue(int paramID, String value) {

    }
}