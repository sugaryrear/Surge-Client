package com.client.definitions;

import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSBuffer;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSObjectComposition;
import org.apache.commons.lang3.StringUtils;

import com.client.AnimFrame;
import com.client.Client;
import com.client.ReferenceCache;
import com.client.Model;
import com.client.OnDemandFetcher;
import com.client.Buffer;
import com.client.FileArchive;

public final class ObjectDefinition implements RSObjectComposition {
    public static int totalObjects = 60000;

    public static void init(FileArchive streamLoader) {
        stream = new Buffer(streamLoader.readFile("loc.dat"));
        Buffer stream = new Buffer(streamLoader.readFile("loc.idx"));
        totalObjects = stream.readUShort();
      //  totalObjects = 60000;
        streamIndices = new int[totalObjects+20000];
        int i = 2;
        for (int j = 0; j < totalObjects; j++) {
            streamIndices[j] = i;
            i += stream.readUShort();
        }
        cache = new ObjectDefinition[20];
        for (int k = 0; k < 20; k++)
            cache[k] = new ObjectDefinition();


        System.out.println("Loaded: " + totalObjects + " objects");
    }

    public static final int ENTER_NEXT_ROOM_OBJECT_ID = 415844;//45844

    public static final int ENTER_NEXT_ROOM_OBJECT_ID1 = 45131;

    public static final int ENTER_NEXT_ROOM_OBJECT_ID2 = 45128;

    public static final int ENTER_FINAL_ROOM_OBJECT_ID = 32_751;

    public static final int BOSS_GATE_OBJECT_ID = 32_755;

    public static final int TREASURE_ROOM_ENTRANCE_OBJECT_ID = 43898;
    public static ObjectDefinition lookup(int i) {
        if (i > streamIndices.length)
            i = streamIndices.length - 2;
//
//        if (i == 25913 || i == 25916 || i == 25917)
//            i = 15552;

        for (int j = 0; j < 20; j++)
            if (cache[j].type == i)
                return cache[j];

        cacheIndex = (cacheIndex + 1) % 20;
        ObjectDefinition objectDef = cache[cacheIndex];
        stream.currentPosition = streamIndices[i];
        objectDef.type = i;
        objectDef.setDefaults();
        objectDef.decode(stream);
//        if (i >= 26281 && i <= 26290) {
//            objectDef.actions = new String[] { "Choose", null, null, null, null };
//        }
        switch (i) {
            case 23709://red version of the box
                objectDef.name = "Rejuvination Chest";
               // objectDef.modelIds = new int[] { 15885 };
//                type.ambientLighting = 30;
//                type.actions = new String[] { "Open", null, null, null, null };
                 objectDef.scaleX = 220;
                objectDef.scaleY = 220;
                objectDef.scaleZ = 220;
                objectDef.length = 2;
                objectDef.width = 2;
                break;

            case 20134:
                objectDef.name = "Task Board";
                // objectDef.modelIds = new int[] { 15885 };
//                type.ambientLighting = 30;
//                type.actions = new String[] { "Open", null, null, null, null };
                objectDef.actions = new String[] { "Open", "Progress", null, null, null };
               // objectDef.scaleX = 220;
              //  objectDef.scaleY = 220;
               // objectDef.scaleZ = 220;
                //objectDef.length = 2;
               // objectDef.width = 2;
                break;

            case 39483:
             objectDef.contouredGround = false;
                objectDef.obstructsGround = false;
                objectDef.occludes=false;
                break;
            case 39550:
                objectDef.isInteractive = false;
                break;
//            case 23710:
//                objectDef.name = "Box of Health2";
//                objectDef.models = new int[] { 15885 };
//                objectDef.ambient = 30;
//                objectDef.actions = new String[] { "Open", null, null, null, null };
//                objectDef.scaleX = 200;
//                objectDef.scaleY = 200;
//                objectDef.scaleZ = 200;
//                break;
//            case 50412:
//                objectDef.supportItems = 0;
//                objectDef.models = new int[] { 15885, };
//             //   objectDef.type = 50411;
//                objectDef.isInteractive = true;
//                objectDef.blockWalk = true;
//                objectDef.contouredGround = true;
//                objectDef.blocksProjectile = false;
//                objectDef.ambient = 30;
//                objectDef.name = "Rejuvination Chest";
//                objectDef.actions = new String[] { "Restore", null, null, null, null };
//                break;
            case 50411:
                objectDef.supportItems = 0;
                objectDef.models = new int[] { 4455, };
                objectDef.type = 50411;
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Stairs";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                break;

            case 14833:
                ObjectDefinition elfinlocks = lookup(14834);
                objectDef.shapes = elfinlocks.shapes;
                objectDef.models = elfinlocks.models;
                objectDef.isInteractive = elfinlocks.isInteractive;
                objectDef.blockWalk = elfinlocks.blockWalk;
                objectDef.contouredGround = elfinlocks.contouredGround;
                objectDef.blocksProjectile = elfinlocks.blocksProjectile;
                objectDef.name = elfinlocks.name;
                objectDef.actions = elfinlocks.actions;
                objectDef.length = elfinlocks.length;
                objectDef.width = elfinlocks.width;
                objectDef.sharelight = elfinlocks.sharelight;
                objectDef.occludes = elfinlocks.occludes;
                objectDef.readyanim = elfinlocks.readyanim;
                objectDef.decorDisplacement = elfinlocks.decorDisplacement;

                objectDef.ambient = elfinlocks.ambient;
                objectDef.contrast = elfinlocks.contrast;

                objectDef.scaleX = elfinlocks.scaleX;

                objectDef.scaleY = elfinlocks.scaleY;
                objectDef.scaleZ = elfinlocks.scaleZ;

                objectDef.removeClipping = elfinlocks.removeClipping;

                objectDef.supportItems = elfinlocks.supportItems;
                objectDef.varpID = elfinlocks.varpID;

                objectDef.varbitID = elfinlocks.varbitID;

                objectDef.recolorToFind  = elfinlocks.recolorToFind ;
                objectDef.recolorToReplace  = elfinlocks.recolorToReplace ;
                objectDef.textureFind   = elfinlocks.textureFind  ;
                objectDef.textureReplace   = elfinlocks.textureReplace  ;
                objectDef.category    = elfinlocks.category   ;   objectDef.inverted     = elfinlocks.inverted    ;
                objectDef.mapscene     = elfinlocks.mapscene    ;   objectDef.blocksides      = elfinlocks.blocksides     ;

                objectDef.translateX      = elfinlocks.translateX     ;   objectDef.translateY       = elfinlocks.translateY      ;
                objectDef.translateZ      = elfinlocks.translateZ     ;   objectDef.blocksides      = elfinlocks.blocksides     ;
                objectDef.minimapFunction       = elfinlocks.minimapFunction      ;   objectDef.randomAnimStart        = elfinlocks.randomAnimStart       ;

                break;
            case 2030:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Furnace";
                objectDef.actions = new String[] { "Smelt", null, null, null, null };
                break;
            case 16465:
            case 33113:
            case 32751:
            case 32755:
            case TREASURE_ROOM_ENTRANCE_OBJECT_ID:
            case ENTER_NEXT_ROOM_OBJECT_ID:
            case ENTER_NEXT_ROOM_OBJECT_ID1:
            case ENTER_NEXT_ROOM_OBJECT_ID2:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
//                objectDef.name = "Furnace";
//                objectDef.actions = new String[] { "Smelt", null, null, null, null };
                break;
            case 26461:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
//                objectDef.name = "Furnace";
//                objectDef.actions = new String[] { "Smelt", null, null, null, null };
                break;
            case 31858:
                objectDef.actions = new String[] { "Normal", "Ancient", "Lunar", "Arceeus", null };
                break;
            case 2114:
            case 2119:
                objectDef.models = new int[] { 4453, };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Stairs";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                break;
                case 733:
                objectDef.models = new int[] { 2131, 25625 };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Web";
                objectDef.actions = new String[] { "Slash", null, null, null, null };
                break;
            case 2120:
                objectDef.models = new int[] { 4455, };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Stairs";
                objectDef.actions = new String[] { "Climb-down", null, null, null, null };
                break;
            case 30191:
                objectDef.models = new int[] { 32941, };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Ladder";
                objectDef.actions = new String[] { "Climb-down", null, null, null, null };
                break;
            case 30192:
                objectDef.models = new int[] { 32940, };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Ladder";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                break;
        // case 23363:
            case 31558:
       //     case 31557:
            case 43868:
            case 14314:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Stairs";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                break;
            case 26380:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Pillar";
                objectDef.actions = new String[] { "Grapple", null, null, null, null };
                break;
            case 26561:
                case 26560:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Rock";
                objectDef.actions = new String[] { "Climb", null, null, null, null };
                break;
            case 4004:

                objectDef.name = "Well of Good Will";
                objectDef.actions = new String[] { "Donate", null, null, null, null };
                break;
            case 26370:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Rope";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                break;
                        case 10177:
                objectDef.name = "Iron Ladder";
                objectDef.actions = new String[] { "Climb", "Climb up", "Climb down", null, null };
                            objectDef.isInteractive = true;
                            objectDef.blockWalk = true;
                            objectDef.contouredGround = true;
                            objectDef.blocksProjectile = false;
                break;
            case 31561:
                objectDef.name = "Pillar";
                objectDef.actions = new String[] { "Jump", null, null, null, null };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                break;
            case 10193:
            case 10196:
            case 10198:
            case 10200:
            case 10201:
            case 10202:
            case 10203:
            case 10206:
            case 10207:
            case 10211:
            case 10214:
            case 10216:
            case 10229:
            case 10194:
case 10217:
case 10226:
case 10228:
                objectDef.name = "Iron Ladder";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                break;
            case 10195:
            case 10197:
            case 10199:
            case 10204:
            case 10205:
            case 10208:
            case 10209:
            case 10212:
            case 10213:
            case 10215:
case 10218:
            case 10227:
case 10225:
                objectDef.name = "Iron Ladder";
                objectDef.actions = new String[] { "Climb-down", null, null, null, null };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                break;
            case 26417:
            case 26418:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Entrance";
                objectDef.actions = new String[] { "Tie-rope", null, null, null, null };
          break;
            case 30282:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "The Inferno";
                objectDef.actions = new String[] { "Jump-into", null, null, null, null };
                break;
            case 26518:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Ice bridge";
                objectDef.actions = new String[] { "Cross", null, null, null, null };
                break;
            case 42840:
            case 42841:
            case 42931:
            case 42932:
            case 42933:
            case 42934:
            case 42966:

                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;

                break;
            case 12365:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Portal";
                objectDef.actions = new String[] { "Enter", null, null, null, null };
                break;
            case 4570:
            case 15648:
                //    case 26418:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Staircase";
                objectDef.actions = new String[] { "Climb-down", null, null, null, null };
                break;
            case 4380:
                //    case 26418:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Iron ladder";
                objectDef.actions = new String[] { "Climb-down", null, null, null, null };
                break;
            case 4569:
                case 15645:
                //    case 26418:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Staircase";
                //objectDef.actions = new String[] { "Climb-down", null, null, null, null };
                break;
            case 4412:
case 4413:
                //    case 26418:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Iron ladder";
                objectDef.actions = new String[] { "Climb-up", null, null, null, null };
                break;
            case 4543:
            case 4544:
                //    case 26418:
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Strange wall";
                objectDef.actions = new String[] { "Study", null, null, null, null };
                break;
            case 20667:
            case 20668:
            case 20669:
            case 20670:
            case 20671:
            case 20672:
               // objectDef.models = new int[] { 6614, };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Staircase";
                objectDef.actions = new String[]{ "Climb up", null, null, null, null};
                break;

            //gnome agility log
            case 23145:
                objectDef.models = new int[] { 1115, };
                objectDef.isInteractive = true;
                objectDef.blockWalk = true;
                objectDef.contouredGround = true;
                objectDef.blocksProjectile = false;
                objectDef.name = "Log";
                objectDef.actions = new String[]{ "Cross", null, null, null, null};
                break;
            case 23609:
                objectDef.name = "Hole";
                objectDef.actions = new String[] { "Climb-down", "Look-inside", null, null, null };
                break;
            case 20377:
                objectDef.name = "Ancient Altar";
                objectDef.actions = new String[] { "Pray", "Switch prayer book", null, null, null };
                break;
            case 36201: // Raids 1 lobby entrance
                objectDef.actions = new String[]{ "Enter", null, null, null, null};
                break;

            case 37743: // nightmare good flower
                objectDef.readyanim = 8617;
                break;
            case 37740: // nightmare bad flower
                objectDef.readyanim = 8623;
                break;
            case 37738: // nightmare spore spawn
                objectDef.readyanim = 8630;
                break;
//
            case 37341:
                objectDef.name = "Hunllef's Chest";
                objectDef.actions = new String[] { "Unlock", null, null, null, null };
                break;

            case 32572:
                objectDef.actions = new String[5];
                objectDef.actions[0] = "Bank";
                objectDef.name = "Group chest";
                break;

            case 1750:
                objectDef.models = new int[] { 8131, };
                objectDef.name = "Willow";
                objectDef.length = 2;
                objectDef.width = 2;
                objectDef.ambient = 25;
                objectDef.actions = new String[] { "Chop down", null, null, null, null };
                objectDef.mapscene = 3;
                break;


            case 1751:
                objectDef.models = new int[] { 8037, 8040, };
                objectDef.name = "Oak";
                objectDef.length = 3;
                objectDef.width = 3;
                objectDef.ambient = 25;
                objectDef.actions = new String[] { "Chop down", null, null, null, null };
                objectDef.mapscene = 1;
                break;

            case 13287:
                objectDef.name = "Storage chest (UIM)";
                objectDef.description = "A chest to store items for UIM.";
                break;

            case 4873:
                objectDef.name = "Wilderness Lever";
                objectDef.length = 3;
                objectDef.width = 3;
                objectDef.ambient = 25;
                objectDef.actions = new String[] { "Enter Deep Wildy", null, null, null, null };
                objectDef.mapscene = 3;
                break;
            case 29735:
                objectDef.name = "Basic Slayer Dungeon";
                break;


            case 18826:
            case 18819:
            case 18818:
                objectDef.length = 3;
                objectDef.width = 3;
                objectDef.scaleZ = 200; // Width
                objectDef.scaleX = 200; // Thickness
                objectDef.scaleY = 100; // Height
                break;


            case 11700:
                objectDef.models = new int[] { 4086 };
                objectDef.name = "Venom";
                objectDef.length = 3;
                objectDef.width = 3;
                objectDef.blockWalk = false;
                objectDef.contouredGround = true;
                objectDef.readyanim = 1261;
                objectDef.recolorToFind = new short[] { 31636 };
                objectDef.recolorToReplace = new short[] { 10543 };
                objectDef.scaleX = 160;
                objectDef.scaleY = 160;
                objectDef.scaleZ = 160;
                objectDef.actions = new String[5];
                // objectDef.description = new String(
                // "It's a cloud of venomous smoke that is extremely toxic.");
                break;

            case 11601: // 11601
                objectDef.textureFind = new short[] { 2 };
                objectDef.textureReplace = new short[] { 46 };
                break;
        }
    if (Client.debugModels) {

            if (objectDef.name == null || objectDef.name.equalsIgnoreCase("null"))
                objectDef.name = "test";

            objectDef.isInteractive = true;
           objectDef.actions = new String[]{ "Climb up", null, null, null, null};
            //its not enough to add interactive also got add actions
  }
        return objectDef;
    }

    public static void dumpList() {
        try {
            FileWriter fw = new FileWriter("./temp/" + "object_data2.json");
            fw.write("[\n");
            for (int i = 8551; i < 8553; i++) {
                ObjectDefinition def = ObjectDefinition.lookup(i);
                String output = "[\"" + StringUtils.join(def.actions, "\", \"") + "\"],";

                String finalOutput = "	{\n" + "		\"id\": " + def.type + ",\n		" + "\"name\": \"" + def.name
                        + "\",\n		\"models\": " + Arrays.toString(def.models) + ",\n		\"actions\": "
                        + output.replaceAll(", \"\"]", ", \"Examine\"]").replaceAll("\"\"", "null")
                        .replace("[\"null\"]", "[null, null, null, null, \"Examine\"]")
                        .replaceAll(", \"Remove\"", ", \"Remove\", \"Examine\"")
                        + "	\n		\"width\": " + def.scaleZ + "\n	},";
                fw.write(finalOutput.replaceAll("\"name\": \"null\",", "\"name\": null,"));
                fw.write(System.getProperty("line.separator"));
         //       if(recolorT)
             //   System.out.println("rec: "+def.recolorToFind.toString()+" re: "+def.recolorToReplace.toString());
                System.out.println("int: "+def.isInteractive +" ");
                System.out.println("items: "+def.supportItems+" ");
                if(def.shapes != null)
                System.out.println("shapes : "+def.shapes.toString()+" ");
                System.out.println("w : "+def.blockWalk+" ");
                System.out.println("readyanim  : "+def.readyanim+" ");
                System.out.println("varbitID : "+def.varbitID+" ");
                System.out.println(" varpID   : "+def. varpID +" ");
                if(def.configs != null)
                System.out.println(" configs   : "+def. configs.toString() +" ");

            }
            fw.write("]");
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setDefaults() {
        models = null;
        shapes = null;
        name = null;
        description = null;
        recolorToFind = null;
        recolorToReplace = null;
        textureFind = null;
        textureReplace = null;
        length = 1;
        width = 1;
        blockWalk = true;
        blocksProjectile = true;
        isInteractive = false;
        contouredGround = false;
        sharelight = false;
        occludes = false;
        readyanim = -1;
        decorDisplacement = 16;
        ambient = 0;
        contrast = 0;
        actions = null;
        minimapFunction = -1;
        mapscene = -1;
        inverted = false;
        castsShadow = true;
        scaleX = 128;
        scaleY = 128;
        scaleZ = 128;
        blocksides = 0;
        translateX = 0;
        translateY = 0;
        translateZ = 0;
        obstructsGround = false;
        removeClipping = false;
        supportItems = -1;
        varbitID = -1;
        varpID = -1;
        configs = null;
    }

    public void method574(OnDemandFetcher class42_sub1) {
        if (models == null)
            return;
        for (int j = 0; j < models.length; j++)
            class42_sub1.method560(models[j] & 0xffff, 0);
    }

    public static void nullLoader() {
        baseModels = null;
        recent_models = null;
        streamIndices = null;
        cache = null;
        stream = null;
    }


    public boolean modelTypeCached(int i) {
        if (shapes == null) {
            if (models == null)
                return true;
            if (i != 10)
                return true;
            boolean flag1 = true;
            Model model = (Model) ObjectDefinition.recent_models.get(type);
            for (int k = 0; k < models.length; k++)
                flag1 &= Model.isCached(models[k] & 0xffff);

            return flag1;
        }
        Model model = (Model) ObjectDefinition.recent_models.get(type);
        for (int j = 0; j < shapes.length; j++)
            if (shapes[j] == i)
                return Model.isCached(models[j] & 0xffff);

        return true;
    }

    public Model modelAt(int type, int orientation, int aY, int bY, int cY, int dY, int frameId, AnimationDefinition seqtype) {
        Model model = model(type, frameId, orientation, seqtype);
        if (model == null)
            return null;
        if (contouredGround || sharelight)
            model = new Model(contouredGround, sharelight, model);
        if (contouredGround) {
            int y = (aY + bY + cY + dY) / 4;
            for (int vertex = 0; vertex < model.vertex_count; vertex++) {
                int x = model.verticesX[vertex];
                int z = model.verticesZ[vertex];
                int l2 = aY + ((bY - aY) * (x + 64)) / 128;
                int i3 = dY + ((cY - dY) * (x + 64)) / 128;
                int j3 = l2 + ((i3 - l2) * (z + 64)) / 128;
                model.verticesY[vertex] += j3 - y;
            }

            model.normalise();
            model.resetBounds();
        }
        return model;
    }

    public boolean modelCached() {
        if (models == null)
            return true;
        boolean flag1 = true;
        for (int i = 0; i < models.length; i++)
            flag1 &= Model.isCached(models[i] & 0xffff);
        return flag1;
    }

    public ObjectDefinition method580() {
        int i = -1;
        if (varpID != -1) {
            VarBit varBit = VarBit.cache[varpID];
            int j = varBit.setting;
            int k = varBit.start;
            int l = varBit.end;
            int i1 = Client.BIT_MASKS[l - k];
            i = clientInstance.variousSettings[j] >> k & i1;
        } else if (varbitID != -1)
            i = clientInstance.variousSettings[varbitID];
        int var3;
        if (i >= 0 && i < configs.length)
            var3 = configs[i];
        else
            var3 = configs[configs.length - 1];
        return var3 == -1 ? null : lookup(var3);
    }

    public Model model(int j, int frame, int l, AnimationDefinition seqtype) {
        Model model = null;
        long l1;
        if (shapes == null) {
            if (j != 10)
                return null;
            l1 = (long) ((type << 6) + l) + ((long) (frame + 1) << 32);
            Model model_1 = (Model) recent_models.get(l1);
            if (model_1 != null) {
                return model_1;
            }
            if (models == null)
                return null;
            boolean flag1 = inverted ^ (l > 3);
            int k1 = models.length;
            for (int i2 = 0; i2 < k1; i2++) {
                int l2 = models[i2];
                if (flag1)
                    l2 += 0x10000;
                model = (Model) baseModels.get(l2);
                if (model == null) {
                    model = Model.getModel(l2 & 0xffff);
                    if (model == null)
                        return null;
                    if (flag1)
                        model.mirror();
                    baseModels.put(model, l2);
                }
                if (k1 > 1)
                    aModelArray741s[i2] = model;
            }

            if (k1 > 1)
                model = new Model(k1, aModelArray741s);
        } else {
            int i1 = -1;
            for (int j1 = 0; j1 < shapes.length; j1++) {
                if (shapes[j1] != j)
                    continue;
                i1 = j1;
                break;
            }

            if (i1 == -1)
                return null;
            l1 = (long) ((type << 8) + (i1 << 3) + l) + ((long) (frame + 1) << 32);
            Model model_2 = (Model) recent_models.get(l1);
            if (model_2 != null) {
                return model_2;
            }
            if (models == null) {
                return null;
            }
            int j2 = models[i1];
            boolean flag3 = inverted ^ (l > 3);
            if (flag3)
                j2 += 0x10000;
            model = (Model) baseModels.get(j2);
            if (model == null) {
                model = Model.getModel(j2 & 0xffff);
                if (model == null)
                    return null;
                if (flag3)
                    model.mirror();
                baseModels.put(model, j2);
            }
        }
        boolean flag;
        flag = scaleX != 128 || scaleY != 128 || scaleZ != 128;
        boolean flag2;
        flag2 = translateX != 0 || translateY != 0 || translateZ != 0;
        Model model_3 = new Model(recolorToFind == null,
                AnimFrame.noAnimationInProgress(frame), l == 0 && frame == -1 && !flag
                && !flag2, textureFind == null, model);
        if (frame != -1) {
            model_3.apply_label_groups();
            model_3.animate_either(seqtype, frame);
            model_3.face_label_groups = null;
            model_3.vertex_label_groups = null;
        }
        while (l-- > 0)
            model_3.rotate90Degrees();

        if (recolorToFind != null) {
            for (int k2 = 0; k2 < recolorToFind.length; k2++)
                model_3.recolor(recolorToFind[k2],
                        recolorToReplace[k2]);

        }
        if (textureFind != null) {
            for (int k2 = 0; k2 < textureFind.length; k2++)
                model_3.retexture(textureFind[k2],
                        textureReplace[k2]);

        }
        if (flag)
            model_3.scale(scaleX, scaleZ, scaleY);
        if (flag2)
            model_3.offsetBy(translateX, translateY, translateZ);
        model_3.light(85 + ambient, 768 + contrast, -50, -10, -50, !sharelight);
        if (supportItems == 1)
            model_3.itemDropHeight = model_3.modelBaseY;
        recent_models.put(model_3, l1);
        return model_3;
    }

    public void decode(Buffer buffer) {
        int prev_op = -1;
        while(true) {
            int opcode = buffer.get_unsignedbyte();

            if (opcode == 0) {
                break;
            } else if (opcode == 1) {
                int len = buffer.get_unsignedbyte();
                if (len > 0) {
                    if (models == null) {
                        shapes = new int[len];
                        models = new int[len];

                        for (int i = 0; i < len; i++) {
                            models[i] = buffer.readUShort();
                            shapes[i] = buffer.get_unsignedbyte();
                        }
                    } else {
                        buffer.currentPosition += len * 3;
                    }
                }
            } else if (opcode == 2) {
                name = buffer.readString();
            } else if (opcode == 5) {
                int len = buffer.get_unsignedbyte();
                if (len > 0) {
                    if (models == null) {
                        shapes = null;
                        models = new int[len];
                        for (int i = 0; i < len; i++) {
                            models[i] = buffer.readUShort();
                        }
                    } else {
                        buffer.currentPosition += len * 2;
                    }
                }
            } else if (opcode == 14) {
                length = buffer.get_unsignedbyte();
            } else if (opcode == 15) {
                width = buffer.get_unsignedbyte();
            } else if (opcode == 17) {
                blockWalk = false;
                blocksProjectile = false;
            } else if (opcode == 18) {
                blocksProjectile = false;
            } else if (opcode == 19) {
                isInteractive = (buffer.get_unsignedbyte() == 1);
            } else if (opcode == 21) {
                contouredGround = true;
            } else if (opcode == 22) {
                sharelight = true;
            } else if (opcode == 23) {
                occludes = true;
            } else if (opcode == 24) {
                readyanim = buffer.readUShort();
                if (readyanim == 0xFFFF) {
                    readyanim = -1;
                }
            } else if (opcode == 27) {
                blockWalk = true;
            } else if (opcode == 28) {
                decorDisplacement = buffer.get_unsignedbyte();
            } else if (opcode == 29) {
                ambient = buffer.readSignedByte();
            } else if (opcode == 39) {
                contrast = buffer.readSignedByte() * 25;
            } else if (opcode >= 30 && opcode < 35) {
                if (actions == null) {
                    actions = new String[5];
                }
                actions[opcode - 30] = buffer.readString();
                if (actions[opcode - 30].equalsIgnoreCase("Hidden")) {
                    actions[opcode - 30] = null;
                }
            } else if (opcode == 40) {
                int len = buffer.get_unsignedbyte();
                recolorToFind = new short[len];
                recolorToReplace = new short[len];
                for (int i = 0; i < len; i++) {
                    recolorToFind[i] = (short) buffer.readUShort();
                    recolorToReplace[i] = (short) buffer.readUShort();
                }
            } else if (opcode == 41) {
                int len = buffer.get_unsignedbyte();
                textureFind = new short[len];
                textureReplace = new short[len];
                for (int i = 0; i < len; i++) {
                    textureFind[i] = (short) buffer.readUShort();
                    textureReplace[i] = (short) buffer.readUShort();
                }
            } else if (opcode == 61) {
                category = buffer.readUShort();
            } else if (opcode == 62) {
                inverted = true;
            } else if (opcode == 64) {
                castsShadow = false;
            } else if (opcode == 65) {
                scaleX = buffer.readUShort();
            } else if (opcode == 66) {
                scaleY = buffer.readUShort();
            } else if (opcode == 67) {
                scaleZ = buffer.readUShort();
            } else if (opcode == 68) {
                mapscene = buffer.readUShort();
            } else if (opcode == 69) {
                blocksides = buffer.get_unsignedbyte();
            } else if (opcode == 70) {
                translateX = buffer.readUShort();
            } else if (opcode == 71) {
                translateY = buffer.readUShort();
            } else if (opcode == 72) {
                translateZ = buffer.readUShort();
            } else if (opcode == 73) {
                obstructsGround = true;
            } else if (opcode == 74) {
                removeClipping = true;
            } else if (opcode == 75) {
                supportItems = buffer.get_unsignedbyte();
            } else if (opcode == 77 || opcode == 92) {
                varpID = buffer.readUShort();

                if (varpID == 0xFFFF) {
                    varpID = -1;
                }

                varbitID = buffer.readUShort();

                if (varbitID == 0xFFFF) {
                    varbitID = -1;
                }

                int value = -1;

                if (opcode == 92) {
                    value = buffer.readUShort();

                    if (value == 0xFFFF) {
                        value = -1;
                    }
                }

                int len = buffer.get_unsignedbyte();

                configs = new int[len + 2];
                for (int i = 0; i <= len; ++i) {
                    configs[i] = buffer.readUShort();
                    if (configs[i] == 0xFFFF) {
                        configs[i] = -1;
                    }
                }
                configs[len + 1] = value;
            } else if (opcode == 78) {
                ambientSoundID = buffer.readUShort(); // ambient sound id
                ambientSoundRange = buffer.get_unsignedbyte();
            } else if (opcode == 79) {
                ambientSoundMin = buffer.readUShort();
                ambientSoundMax = buffer.readUShort();
                ambientSoundRange = buffer.get_unsignedbyte();

                int length = buffer.get_unsignedbyte();
                int[] anims = new int[length];

                for (int index = 0; index < length; ++index)
                {
                    anims[index] = buffer.readUShort();
                }
                ambientSoundIds = anims;
            } else if (opcode == 81) {
                buffer.get_unsignedbyte();
            } else if (opcode == 82) {
                minimapFunction = buffer.readUShort();
            } else if (opcode == 89) {
                randomAnimStart = true;
            } else if (opcode == 249) {
                int length = buffer.get_unsignedbyte();

                Map<Integer, Object> params = new HashMap<>(length);
                for (int i = 0; i < length; i++)
                {
                    boolean isString = buffer.get_unsignedbyte() == 1;
                    int key = buffer.read24Int();
                    Object value;

                    if (isString) {
                        value = buffer.readString();
                        System.out.println(value);
                    } else {
                        value = buffer.readInt();
                    }

                    params.put(key, value);
                }

                this.params = params;
            } else {
                System.err.printf("Error unrecognised {Loc} opcode: %d last %d%n%n", opcode, prev_op);

            }
            prev_op = opcode;

        }

        if (name != null && !name.equals("null")) {
            isInteractive = models != null && (shapes == null || shapes[0] == 10);
            if (actions != null)
                isInteractive = true;
        }

        if (removeClipping) {
            blockWalk = false;
            blocksProjectile = false;
        }

        if (supportItems == -1) {
            supportItems = blockWalk ? 1 : 0;
        }
    }

//    public void decode(Buffer buffer) {
//        int prev_op = -1;
//        while(true) {
//            int opcode = buffer.get_unsignedbyte();
//
//            if (opcode == 0) {
//                break;
//            } else if (opcode == 1) {
//                int len = buffer.get_unsignedbyte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = new int[len];
//                        models = new int[len];
//
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUShort();
//                            shapes[i] = buffer.get_unsignedbyte();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 3;
//                    }
//                }
//            } else if (opcode == 2) {
//                name = buffer.readString();
//            } else if (opcode == 5) {
//                int len = buffer.get_unsignedbyte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = null;
//                        models = new int[len];
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUShort();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 2;
//                    }
//                }
//            } else if (opcode == 14) {
//                length = buffer.get_unsignedbyte();
//            } else if (opcode == 15) {
//                width = buffer.get_unsignedbyte();
//            } else if (opcode == 17) {
//                blockWalk = false;
//                blocksProjectile = false;
//            } else if (opcode == 18) {
//                blocksProjectile = false;
//            } else if (opcode == 19) {
//                isInteractive = (buffer.get_unsignedbyte() == 1);
//            } else if (opcode == 21) {
//                contouredGround = true;
//            } else if (opcode == 22) {
//                sharelight = true;
//            } else if (opcode == 23) {
//                occludes = true;
//            } else if (opcode == 24) {
//                readyanim = buffer.readUShort();
//                if (readyanim == 0xFFFF) {
//                    readyanim = -1;
//                }
//            } else if (opcode == 27) {
//                blockWalk = true;
//            } else if (opcode == 28) {
//                decorDisplacement = buffer.get_unsignedbyte();
//            } else if (opcode == 29) {
//                ambient = buffer.readSignedByte();
//            } else if (opcode == 39) {
//                contrast = buffer.readSignedByte() * 25;
//            } else if (opcode >= 30 && opcode < 35) {
//                if (actions == null) {
//                    actions = new String[5];
//                }
//                actions[opcode - 30] = buffer.readString();
//                if (actions[opcode - 30].equalsIgnoreCase("Hidden")) {
//                    actions[opcode - 30] = null;
//                }
//            } else if (opcode == 40) {
//                int len = buffer.get_unsignedbyte();
//                recolorToFind = new short[len];
//                recolorToReplace = new short[len];
//                for (int i = 0; i < len; i++) {
//                    recolorToFind[i] = (short) buffer.readUShort();
//                    recolorToReplace[i] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 41) {
//                int len = buffer.get_unsignedbyte();
//                textureFind = new short[len];
//                textureReplace = new short[len];
//                for (int i = 0; i < len; i++) {
//                    textureFind[i] = (short) buffer.readUShort();
//                    textureReplace[i] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 61) {
//                category = buffer.readUShort();
//            } else if (opcode == 62) {
//                inverted = true;
//            } else if (opcode == 64) {
//                castsShadow = false;
//            } else if (opcode == 65) {
//                scaleX = buffer.readUShort();
//            } else if (opcode == 66) {
//                scaleY = buffer.readUShort();
//            } else if (opcode == 67) {
//                scaleZ = buffer.readUShort();
//            } else if (opcode == 68) {
//                mapscene = buffer.readUShort();
//            } else if (opcode == 69) {
//                blocksides = buffer.get_unsignedbyte();
//            } else if (opcode == 70) {
//                translateX = buffer.readUShort();
//            } else if (opcode == 71) {
//                translateY = buffer.readUShort();
//            } else if (opcode == 72) {
//                translateZ = buffer.readUShort();
//            } else if (opcode == 73) {
//                obstructsGround = true;
//            } else if (opcode == 74) {
//                removeClipping = true;
//            } else if (opcode == 75) {
//                supportItems = buffer.get_unsignedbyte();
//            } else if (opcode == 77 || opcode == 92) {
//                varpID = buffer.readUShort();
//
//                if (varpID == 0xFFFF) {
//                    varpID = -1;
//                }
//
//                varbitID = buffer.readUShort();
//
//                if (varbitID == 0xFFFF) {
//                    varbitID = -1;
//                }
//
//                int value = -1;
//
//                if (opcode == 92) {
//                    value = buffer.readUShort();
//
//                    if (value == 0xFFFF) {
//                        value = -1;
//                    }
//                }
//
//                int len = buffer.get_unsignedbyte();
//
//                configs = new int[len + 2];
//                for (int i = 0; i <= len; ++i) {
//                    configs[i] = buffer.readUShort();
//                    if (configs[i] == 0xFFFF) {
//                        configs[i] = -1;
//                    }
//                }
//                configs[len + 1] = value;
//            } else if (opcode == 78) {
//                ambientSoundID = buffer.readUShort(); // ambient sound id
//                ambientSoundRange = buffer.get_unsignedbyte();
//            } else if (opcode == 79) {
//                ambientSoundMin = buffer.readUShort();
//                ambientSoundMax = buffer.readUShort();
//                ambientSoundRange = buffer.get_unsignedbyte();
//
//                int length = buffer.get_unsignedbyte();
//                int[] anims = new int[length];
//
//                for (int index = 0; index < length; ++index)
//                {
//                    anims[index] = buffer.readUShort();
//                }
//                ambientSoundIds = anims;
//            } else if (opcode == 81) {
//                buffer.get_unsignedbyte();
//            } else if (opcode == 82) {
//                minimapFunction = buffer.readUShort();
//            } else if (opcode == 89) {
//                randomAnimStart = true;
//            } else if (opcode == 249) {
//                int length = buffer.get_unsignedbyte();
//
//                Map<Integer, Object> params = new HashMap<>(length);
//                for (int i = 0; i < length; i++)
//                {
//                    boolean isString = buffer.get_unsignedbyte() == 1;
//                    int key = buffer.read24Int();
//                    Object value;
//
//                    if (isString) {
//                        value = buffer.readString();
//                        System.out.println(value);
//                    } else {
//                        value = buffer.readInt();
//                    }
//
//                    params.put(key, value);
//                }
//
//                this.params = params;
//            } else {
//                System.err.printf("Error unrecognised {Loc} opcode: %d last %d%n%n", opcode, prev_op);
//
//            }
//            prev_op = opcode;
//
//        }
//
//        if (name != null && !name.equals("null")) {
//            isInteractive = models != null && (shapes == null || shapes[0] == 10);
//            if (actions != null)
//                isInteractive = true;
//        }
//
//        if (removeClipping) {
//            blockWalk = false;
//            blocksProjectile = false;
//        }
//
//        if (supportItems == -1) {
//            supportItems = blockWalk ? 1 : 0;
//        }
//    }
    public int category;
    public int[] ambientSoundIds;
    private int ambientSoundId;
    private boolean randomAnimStart;
    private int anInt2112;
    private int anInt2113;
    public int ambientSoundID;
    public int anInt2083;
    private Map<Integer, Object> params = null;


//    public void decode(Buffer stream) {
//        int flag = -1;
//        do {
//            int type = stream.readUnsignedByte();
//            if (type == 0)
//                break;
//            if (type == 1) {
//                int len = stream.readUnsignedByte();
//                if (len > 0) {
//                    if (models == null || lowMem) {
//                        shapes = new int[len];
//                        models = new int[len];
//                        for (int k1 = 0; k1 < len; k1++) {
//                            models[k1] = stream.readUShort();
//                            shapes[k1] = stream.readUnsignedByte();
//                        }
//                    } else {
//                        stream.currentPosition += len * 3;
//                    }
//                }
//            } else if (type == 2)
//                name = stream.readString();
//            else if (type == 3)
//                description = stream.readString();
//            else if (type == 5) {
//                int len = stream.readUnsignedByte();
//                if (len > 0) {
//                    if (models == null || lowMem) {
//                        shapes = null;
//                        models   = new int[len];
//                        for (int l1 = 0; l1 < len; l1++)
//                            models [l1] = stream.readUShort();
//                    } else {
//                        stream.currentPosition += len * 2;
//                    }
//                }
//            } else if (type == 14)
//                length = stream.readUnsignedByte();
//            else if (type == 15)
//                width = stream.readUnsignedByte();
//            else if (type == 17)
//                blockWalk  = false;
//            else if (type == 18)
//                blocksProjectile = false;
//            else if (type == 19)
//                isInteractive = (stream.readUnsignedByte() == 1);
//            else if (type == 21)
//                contouredGround  = true;
//            else if (type == 22)
//                sharelight  = true;
//            else if (type == 23)
//                occludes  = true;
//            else if (type == 24) { // Object Animations
//                readyanim = stream.readUShort();
//                if ( readyanim == 65535)
//                    readyanim = -1;
//            } else if (type == 28)
//                decorDisplacement = stream.readUnsignedByte();
//            else if (type == 29)
//                ambient = stream.readSignedByte();
//            else if (type == 39)
//                contrast = stream.readSignedByte();
//            else if (type >= 30 && type < 39) {
//                if (actions == null)
//                    actions = new String[9];
//                actions[type - 30] = stream.readString();
//                if (actions[type - 30].equalsIgnoreCase("hidden"))
//                    actions[type - 30] = null;
//            } else if (type == 40) {
//                int i1 = stream.readUnsignedByte();
//                recolorToFind = new short[i1];
//                recolorToReplace = new short[i1];
//                for (int i2 = 0; i2 < i1; i2++) {
//                    recolorToFind[i2] = (short) stream.readUShort();
//                    recolorToReplace[i2] = (short) stream.readUShort();
//                }
//            } else if (type == 41) {
//                int i1 = stream.readUnsignedByte();
//                textureFind  = new short[i1];
//                textureReplace  = new short[i1];
//                for (int i2 = 0; i2 < i1; i2++) {
//                    textureFind [i2] = (short) stream.readUShort();
//                    textureReplace [i2] = (short) stream.readUShort();
//                }
//            } else if (type == 61) {
//                stream.readUShort();
//            } else if (type == 62)
//                inverted = true;
//            else if (type == 64)
//                castsShadow = false;
//            else if (type == 65)
//                scaleX = stream.readUShort();
//            else if (type == 61)
//                stream.readUShort();
//            else if (type == 66)
//                scaleY = stream.readUShort();
//            else if (type == 67)
//                width = stream.readUShort();
//            else if (type == 68) {
//                mapscene  = stream.readUShort();
//            } else if (type == 69)
//                blocksides = stream.readUnsignedByte();
//            else if (type == 70)
//                translateX = stream.readSignedWord();
//            else if (type == 71)
//                translateY  = stream.readSignedWord();
//            else if (type == 72)
//                translateZ = stream.readSignedWord();
//            else if (type == 73)
//                obstructsGround = true;
//            else if (type == 74)
//                removeClipping = true;
//            else if (type == 75)
//                supportItems = stream.readUnsignedByte();
//            else if (type == 77 || type == 92) {
//                varpID  = stream.readUShort();
//                if (   varpID  == 65535)
//                    varpID  = -1;
//                varbitID  = stream.readUShort();
//                if (   varbitID  == 65535)
//                    varbitID  = -1;
//                int var3 = -1;
//                if(type == 92)
//                    var3 = stream.readUShort();
//                int j1 = stream.readUnsignedByte();
//                configs  = new int[j1 + 2];
//                for (int j2 = 0; j2 <= j1; j2++) {
//                    configs [j2] = stream.readUShort();
//                    if (   configs [j2] == 65535)
//                        configs [j2] = -1;
//                }
//                configs [j1 + 1] = var3;
//            } else if(type == 78) {//ambient sound
//                stream.readUShort();
//                stream.readUnsignedByte();
//            } else if(type == 79) {
//                stream.currentPosition+= 5;
//                int len = stream.readUnsignedByte();
//                stream.currentPosition += len * 2;
//            } else if(type == 81) {
//                stream.readUnsignedByte();
//            } else if(type == 82) {
//                minimapFunction  = stream.readUShort();
//            } else if (type == 249) {
//                int length = stream.readUnsignedByte();
//
//                for (int i = 0; i < length; i++) {
//                    boolean isString = stream.readUnsignedByte() == 1;
//                    int key = stream.read24BitInt();
//
//                    if (isString) {
//                        stream.readOSRSString();
//                    } else {
//                        stream.read24BitInt();
//                    }
//                }
//            }
//        } while (true);
//        if (flag == -1 && name != "null" && name != null) {
//            isInteractive = models  != null && (shapes == null || shapes[0] == 10);
//            if (actions != null)
//                isInteractive = true;
//        }
//        if (removeClipping) {
//            blockWalk  = false;
//            blocksProjectile = false;
//        }
//        if (supportItems == -1)
//            supportItems =       blockWalk  ? 1 : 0;
//    }

//    private void decode(Buffer buffer) {
//        int actionIndex = -1;
//        while (true) {
//            int opcode = buffer.readUnsignedByte();
//            if (opcode == 0) {
//                break;
//            }
//            if (opcode == 1) {
//                int len = buffer.readUnsignedByte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = new int[len];
//                        models = new int[len];
//
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUnsignedShort();
//                            shapes[i] = buffer.readUnsignedByte();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 3;
//                    }
//                }
//            } else if (opcode == 2) {
//                name = buffer.readString();
//            } else if (opcode == 3) {
//                //	description = buffer.readString();
//            } else if (opcode == 5) {
//                int len = buffer.readUnsignedByte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = null;
//                        models = new int[len];
//
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUnsignedShort();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 2;
//                    }
//                }
//            } else if (opcode == 14) {
//                width = buffer.readUnsignedByte();
//            } else if (opcode == 15) {
//                length = buffer.readUnsignedByte();
//            } else if (opcode == 17) {
//                blockWalk = false;
//                blocksProjectile = false;
//            } else if (opcode == 18) {
//                blocksProjectile = false;
//            } else if (opcode == 19) {
//                actionIndex = buffer.readUnsignedByte();
//                if (actionIndex == 1) {
//                    isInteractive = true;
//                }
//            } else if (opcode == 21) {
//                contouredGround = true;
//            } else if (opcode == 22) {
//                sharelight = true;
//            } else if (opcode == 23) {
//                occludes  = true;
//            } else if (opcode == 24) {
//                readyanim  = buffer.readUnsignedShort();
//                if (readyanim  == 65535) {
//                    readyanim  = -1;
//                }
//            } else if (opcode == 28) {
//                decorDisplacement  = buffer.readUnsignedByte();
//            } else if (opcode == 29) {
//                ambient = buffer.readSignedByte();
//            } else if (opcode == 39) {
//                contrast = buffer.readSignedByte();
//            } else if (opcode >= 30 && opcode < 35) {
//                if (actions == null) {
//                    actions = new String[5];
//                }
//                actions[opcode - 30] = buffer.readString();
//                if (actions[opcode - 30].equalsIgnoreCase("hidden")) {
//                    actions[opcode - 30] = null;
//                }
//            } else if (opcode == 40) {
//                int length = buffer.readUnsignedByte();
//                recolorToFind  = new short[length];
//                recolorToReplace  = new short[length];
//                for (int i = 0; i < length; i++) {
//                    recolorToFind [i] = (short) buffer.readShort();
//                    recolorToReplace [i] = (short) buffer.readShort();
//                }
//            } else if (opcode == 41) {
//                int length = buffer.readUnsignedByte();
//                textureFind  = new short[length];
//                textureReplace  = new short[length];
//                for (int i = 0; i < length; i++) {
//                    textureFind [i] = (short) buffer.readShort();
//                    textureReplace [i] = (short) buffer.readShort();
//                }
//            } else if (opcode == 61) {
//                buffer.readUnsignedShort();
//            } else if (opcode == 62) {
//                inverted  = true;
//            } else if (opcode == 64) {
//                castsShadow  = false;
//            } else if (opcode == 65) {
//                scaleX  = buffer.readUnsignedShort();
//            } else if (opcode == 66) {
//                scaleY  = buffer.readUnsignedShort();
//            } else if (opcode == 67) {
//                scaleZ  = buffer.readUnsignedShort();
//            } else if (opcode == 68) {
//                mapscene  = buffer.readUnsignedShort();
////                if (mapsceneId > 103) {
////                    //System.out.println("bro? " + objectId + " object has mapscene " + mapsceneId);
////                    mapsceneId = -1;
////                }
//            } else if (opcode == 69) {
//                blocksides = buffer.readSignedByte();
//            } else if (opcode == 70) {
//                translateX  = buffer.readUnsignedShort();
//            } else if (opcode == 71) {
//                translateY  = buffer.readUnsignedShort();
//            } else if (opcode == 72) {
//                translateZ  = buffer.readUnsignedShort();
//            } else if (opcode == 73) {
//                obstructsGround = true;
//            } else if (opcode == 74) {
//                removeClipping = true;
//            } else if (opcode == 75) {
//                supportItems = buffer.readUnsignedByte();
//            } else if (opcode == 77) {
//                varbitID  = buffer.readUnsignedShort();
//                if (varbitID  == 0xFFFF) {
//                    varbitID  = -1;
//                }
//
//                varpID  = buffer.readUnsignedShort();
//                if (varpID  == 0xFFFF) {
//                    varpID  = -1;
//                }
//
//                int length = buffer.readUnsignedByte();
//                configs  = new int[length + 2];
//                for (int i = 0; i <= length; ++i) {
//                    configs [i] = buffer.readUnsignedShort();
//                    if (configs [i] == 0xFFFF) {
//                        configs [i] = -1;
//                    }
//                }
//                configs [length + 1] = -1;
//            } else if (opcode == 78) {
//                buffer.readUnsignedShort();
//                buffer.readUnsignedByte();
//            } else if (opcode == 79) {
//                buffer.readUnsignedShort();
//                buffer.readUnsignedShort();
//                buffer.readUnsignedByte();
//
//                int length = buffer.readUnsignedByte();
//                for (int i = 0; i < length; ++i) {
//                    buffer.readUnsignedShort();
//                }
//            } else if (opcode == 81) {
//                buffer.readUnsignedByte();
//            } else if (opcode == 82) {
//                minimapFunction  = buffer.readUnsignedShort();
//            } else if (opcode == 89) {
//                // randomize anim start
//            } else if (opcode == 92) {
//                varbitID  = buffer.readUnsignedShort();
//                if (varbitID  == 0xFFFF) {
//                    varbitID  = -1;
//                }
//
//                varpID  = buffer.readUnsignedShort();
//                if (varpID  == 0xFFFF) {
//                    varpID  = -1;
//                }
//
//                int var = buffer.readUnsignedShort();
//                if (var == 0xFFFF) {
//                    var = -1;
//                }
//
//                int length = buffer.readUnsignedByte();
//                configs  = new int[length + 2];
//
//                for (int i = 0; i <= length; ++i) {
//                    configs [i] = buffer.readUnsignedShort();
//                    if (configs [i] == 0xFFFF) {
//                        configs [i] = -1;
//                    }
//                }
//
//                configs [length + 1] = var;
//            } else if (opcode == 249) {
//                int length = buffer.readUnsignedByte();
//                for (int i = 0; i < length; i++) {
//                    boolean isString = buffer.readUnsignedByte() == 1;
//                    buffer.readUnsignedTriByte();
//                    if (isString) {
//                        buffer.readString();
//                    } else {
//                        buffer.readInt();
//                    }
//                }
//            }
//        }
//
//        if (actionIndex == -1) {
//            isInteractive = models != null && (shapes == null || shapes[0] == 10);
//            if (actions != null) {
//                isInteractive = true;
//            }
//        }
//
//        if (removeClipping) {
//            blockWalk = false;
//            blocksProjectile = false;
//        }
//
//        if ( supportItems == -1) {
//            supportItems = blockWalk ? 1 : 0;
//        }
//    }
//    public void decode(Buffer buffer) {
//        int prev_op = -1;
//        while(true) {
//            int opcode = buffer.get_unsignedbyte();
//
//            if (opcode == 0) {
//                break;
//            } else if (opcode == 1) {
//                int len = buffer.get_unsignedbyte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = new int[len];
//                        models = new int[len];
//
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUShort();
//                            shapes[i] = buffer.get_unsignedbyte();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 3;
//                    }
//                }
//            } else if (opcode == 2) {
//                name = buffer.readString();
//            } else if (opcode == 5) {
//                int len = buffer.get_unsignedbyte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = null;
//                        models = new int[len];
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUShort();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 2;
//                    }
//                }
//            } else if (opcode == 14) {
//                length = buffer.get_unsignedbyte();
//            } else if (opcode == 15) {
//                width = buffer.get_unsignedbyte();
//            } else if (opcode == 17) {
//                blockWalk = false;
//                blocksProjectile = false;
//            } else if (opcode == 18) {
//                blocksProjectile = false;
//            } else if (opcode == 19) {
//                isInteractive = (buffer.get_unsignedbyte() == 1);
//            } else if (opcode == 21) {
//                contouredGround = true;
//            } else if (opcode == 22) {
//                sharelight = true;
//            } else if (opcode == 23) {
//                occludes = true;
//            } else if (opcode == 24) {
//                readyanim = buffer.readUShort();
//                if (readyanim == 0xFFFF) {
//                    readyanim = -1;
//                }
//            } else if (opcode == 27) {
//                blockWalk = true;
//            } else if (opcode == 28) {
//                decorDisplacement = buffer.get_unsignedbyte();
//            } else if (opcode == 29) {
//                ambient = buffer.readSignedByte();
//            } else if (opcode == 39) {
//                contrast = buffer.readSignedByte() * 25;
//            } else if (opcode >= 30 && opcode < 35) {
//                if (actions == null) {
//                    actions = new String[5];
//                }
//                actions[opcode - 30] = buffer.readString();
//                if (actions[opcode - 30].equalsIgnoreCase("Hidden")) {
//                    actions[opcode - 30] = null;
//                }
//            } else if (opcode == 40) {
//                int len = buffer.get_unsignedbyte();
//                recolorToFind = new short[len];
//                recolorToReplace = new short[len];
//                for (int i = 0; i < len; i++) {
//                    recolorToFind[i] = (short) buffer.readUShort();
//                    recolorToReplace[i] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 41) {
//                int len = buffer.get_unsignedbyte();
//                textureFind = new short[len];
//                textureReplace = new short[len];
//                for (int i = 0; i < len; i++) {
//                    textureFind[i] = (short) buffer.readUShort();
//                    textureReplace[i] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 61) {
//                category = buffer.readUShort();
//            } else if (opcode == 62) {
//                inverted = true;
//            } else if (opcode == 64) {
//                castsShadow = false;
//            } else if (opcode == 65) {
//                scaleX = buffer.readUShort();
//            } else if (opcode == 66) {
//                scaleY = buffer.readUShort();
//            } else if (opcode == 67) {
//                scaleZ = buffer.readUShort();
//            } else if (opcode == 68) {
//                mapscene = buffer.readUShort();
//            } else if (opcode == 69) {
//                blocksides = buffer.get_unsignedbyte();
//            } else if (opcode == 70) {
//                translateX = buffer.readUShort();
//            } else if (opcode == 71) {
//                translateY = buffer.readUShort();
//            } else if (opcode == 72) {
//                translateZ = buffer.readUShort();
//            } else if (opcode == 73) {
//                obstructsGround = true;
//            } else if (opcode == 74) {
//                removeClipping = true;
//            } else if (opcode == 75) {
//                supportItems = buffer.get_unsignedbyte();
//            } else if (opcode == 77 || opcode == 92) {
//                varpID = buffer.readUShort();
//
//                if (varpID == 0xFFFF) {
//                    varpID = -1;
//                }
//
//                varbitID = buffer.readUShort();
//
//                if (varbitID == 0xFFFF) {
//                    varbitID = -1;
//                }
//
//                int value = -1;
//
//                if (opcode == 92) {
//                    value = buffer.readUShort();
//
//                    if (value == 0xFFFF) {
//                        value = -1;
//                    }
//                }
//
//                int len = buffer.get_unsignedbyte();
//
//                configs = new int[len + 2];
//                for (int i = 0; i <= len; ++i) {
//                    configs[i] = buffer.readUShort();
//                    if (configs[i] == 0xFFFF) {
//                        configs[i] = -1;
//                    }
//                }
//                configs[len + 1] = value;
//            } else if (opcode == 78) {
//                ambientSoundID = buffer.readUShort(); // ambient sound id
//                ambientSoundRange = buffer.get_unsignedbyte();
//            } else if (opcode == 79) {
//                ambientSoundMin = buffer.readUShort();
//                ambientSoundMax = buffer.readUShort();
//                ambientSoundRange = buffer.get_unsignedbyte();
//
//                int length = buffer.get_unsignedbyte();
//                int[] anims = new int[length];
//
//                for (int index = 0; index < length; ++index)
//                {
//                    anims[index] = buffer.readUShort();
//                }
//                ambientSoundIds = anims;
//            } else if (opcode == 81) {
//                buffer.get_unsignedbyte();
//            } else if (opcode == 82) {
//                minimapFunction = buffer.readUShort();
//            } else if (opcode == 89) {
//                randomAnimStart = true;
//            } else if (opcode == 249) {
//                int length = buffer.get_unsignedbyte();
//
//                Map<Integer, Object> params = new HashMap<>(length);
//                for (int i = 0; i < length; i++)
//                {
//                    boolean isString = buffer.get_unsignedbyte() == 1;
//                    int key = buffer.read24Int();
//                    Object value;
//
//                    if (isString) {
//                        value = buffer.readString();
//                        System.out.println(value);
//                    } else {
//                        value = buffer.readInt();
//                    }
//
//                    params.put(key, value);
//                }
//
//                this.params = params;
//            } else {
//                System.err.printf("Error unrecognised {Loc} opcode: %d last %d%n%n", opcode, prev_op);
//
//            }
//            prev_op = opcode;
//
//        }
//
//        if (name != null && !name.toLowerCase().contains("null")) {
//            isInteractive = models != null && (shapes == null || shapes[0] == 10);
//            if (actions != null)
//                isInteractive = true;
//        }
//
//        if (removeClipping) {
//            blockWalk = false;
//            blocksProjectile = false;
//        }
//
//        if (supportItems == -1) {
//            supportItems = blockWalk ? 1 : 0;
//        }
//    }

    private int ambientSoundMin;
    private int ambientSoundMax;
//    public int ambientSoundID;
    public int ambientSoundRange;
//    private Map<Integer, Object> params = null;
//
//    public void decode(Buffer buffer) {
//        int prev_op = -1;
//        while(true) {
//            int opcode = buffer.get_unsignedbyte();
//
//            if (opcode == 0) {
//                break;
//            } else if (opcode == 1) {
//                int len = buffer.get_unsignedbyte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = new int[len];
//                        models = new int[len];
//
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUShort();
//                            shapes[i] = buffer.get_unsignedbyte();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 3;
//                    }
//                }
//            } else if (opcode == 2) {
//                name = buffer.readString();
//            } else if (opcode == 5) {
//                int len = buffer.get_unsignedbyte();
//                if (len > 0) {
//                    if (models == null) {
//                        shapes = null;
//                        models = new int[len];
//                        for (int i = 0; i < len; i++) {
//                            models[i] = buffer.readUShort();
//                        }
//                    } else {
//                        buffer.currentPosition += len * 2;
//                    }
//                }
//            } else if (opcode == 14) {
//                length = buffer.get_unsignedbyte();
//            } else if (opcode == 15) {
//                width = buffer.get_unsignedbyte();
//            } else if (opcode == 17) {
//                blockWalk = false;
//                blocksProjectile = false;
//            } else if (opcode == 18) {
//                blocksProjectile = false;
//            } else if (opcode == 19) {
//                isInteractive = (buffer.get_unsignedbyte() == 1);
//            } else if (opcode == 21) {
//                contouredGround = true;
//            } else if (opcode == 22) {
//                sharelight = true;
//            } else if (opcode == 23) {
//                occludes = true;
//            } else if (opcode == 24) {
//                readyanim = buffer.readUShort();
//                if (readyanim == 0xFFFF) {
//                    readyanim = -1;
//                }
//            } else if (opcode == 27) {
//                blockWalk = true;
//            } else if (opcode == 28) {
//                decorDisplacement = buffer.get_unsignedbyte();
//            } else if (opcode == 29) {
//                ambient = buffer.readSignedByte();
//            } else if (opcode == 39) {
//                contrast = buffer.readSignedByte() * 25;
//            } else if (opcode >= 30 && opcode < 35) {
//                if (actions == null) {
//                    actions = new String[5];
//                }
//                actions[opcode - 30] = buffer.readString();
//                if (actions[opcode - 30].equalsIgnoreCase("Hidden")) {
//                    actions[opcode - 30] = null;
//                }
//            } else if (opcode == 40) {
//                int len = buffer.get_unsignedbyte();
//                recolorToFind = new short[len];
//                recolorToReplace = new short[len];
//                for (int i = 0; i < len; i++) {
//                    recolorToFind[i] = (short) buffer.readUShort();
//                    recolorToReplace[i] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 41) {
//                int len = buffer.get_unsignedbyte();
//                textureFind = new short[len];
//                textureReplace = new short[len];
//                for (int i = 0; i < len; i++) {
//                    textureFind[i] = (short) buffer.readUShort();
//                    textureReplace[i] = (short) buffer.readUShort();
//                }
//            } else if (opcode == 61) {
//                category = buffer.readUShort();
//            } else if (opcode == 62) {
//                inverted = true;
//            } else if (opcode == 64) {
//                castsShadow = false;
//            } else if (opcode == 65) {
//                scaleX = buffer.readUShort();
//            } else if (opcode == 66) {
//                scaleY = buffer.readUShort();
//            } else if (opcode == 67) {
//                scaleZ = buffer.readUShort();
//            } else if (opcode == 68) {
//                mapscene = buffer.readUShort();
//            } else if (opcode == 69) {
//                blocksides = buffer.get_unsignedbyte();
//            } else if (opcode == 70) {
//                translateX = buffer.readUShort();
//            } else if (opcode == 71) {
//                translateY = buffer.readUShort();
//            } else if (opcode == 72) {
//                translateZ = buffer.readUShort();
//            } else if (opcode == 73) {
//                obstructsGround = true;
//            } else if (opcode == 74) {
//                removeClipping = true;
//            } else if (opcode == 75) {
//                supportItems = buffer.get_unsignedbyte();
//            } else if (opcode == 77 || opcode == 92) {
//                varpID = buffer.readUShort();
//
//                if (varpID == 0xFFFF) {
//                    varpID = -1;
//                }
//
//                varbitID = buffer.readUShort();
//
//                if (varbitID == 0xFFFF) {
//                    varbitID = -1;
//                }
//
//                int value = -1;
//
//                if (opcode == 92) {
//                    value = buffer.readUShort();
//
//                    if (value == 0xFFFF) {
//                        value = -1;
//                    }
//                }
//
//                int len = buffer.get_unsignedbyte();
//
//                configs = new int[len + 2];
//                for (int i = 0; i <= len; ++i) {
//                    configs[i] = buffer.readUShort();
//                    if (configs[i] == 0xFFFF) {
//                        configs[i] = -1;
//                    }
//                }
//                configs[len + 1] = value;
//            } else if (opcode == 78) {
//                ambientSoundID = buffer.readUShort(); // ambient sound id
//                ambientSoundRange = buffer.get_unsignedbyte();
//            } else if (opcode == 79) {
//                ambientSoundMin = buffer.readUShort();
//                ambientSoundMax = buffer.readUShort();
//                ambientSoundRange = buffer.get_unsignedbyte();
//
//                int length = buffer.get_unsignedbyte();
//                int[] anims = new int[length];
//
//                for (int index = 0; index < length; ++index)
//                {
//                    anims[index] = buffer.readUShort();
//                }
//                ambientSoundIds = anims;
//            } else if (opcode == 81) {
//                buffer.get_unsignedbyte();
//            } else if (opcode == 82) {
//                minimapFunction = buffer.readUShort();
//            } else if (opcode == 89) {
//                randomAnimStart = true;
//            } else if (opcode == 249) {
//                int length = buffer.get_unsignedbyte();
//
//                Map<Integer, Object> params = new HashMap<>(length);
//                for (int i = 0; i < length; i++)
//                {
//                    boolean isString = buffer.get_unsignedbyte() == 1;
//                    int key = buffer.read24Int();
//                    Object value;
//
//                    if (isString) {
//                        value = buffer.readString();
//                        System.out.println(value);
//                    } else {
//                        value = buffer.readInt();
//                    }
//
//                    params.put(key, value);
//                }
//
//                this.params = params;
//            } else {
//                System.err.printf("Error unrecognised {Loc} opcode: %d last %d%n%n", opcode, prev_op);
//
//            }
//            prev_op = opcode;
//
//        }
//
//        if (name != null && !name.equals("null")) {
//            isInteractive = models != null && (shapes == null || shapes[0] == 10);
//            if (actions != null)
//                isInteractive = true;
//        }
//
//        if (removeClipping) {
//            blockWalk = false;
//            blocksProjectile = false;
//        }
//
//        if (supportItems == -1) {
//            supportItems = blockWalk ? 1 : 0;
//        }
//    }

    private ObjectDefinition() {
        type = -1;
    }

    private short[] textureFind;
    private short[] textureReplace;
    public boolean obstructsGround;
    @SuppressWarnings("unused")
    private int contrast;
    @SuppressWarnings("unused")
    private int ambient;
    private int translateX;
    public String name;
    private int scaleZ;
    private static final Model[] aModelArray741s = new Model[4];
    public int length;
    private int translateY;
    public int minimapFunction;
    private short[] recolorToReplace;
    private int scaleX;
    public int varbitID;
    private boolean inverted;
    public static boolean lowMem;
    private static Buffer stream;
    public int type;
    public static int[] streamIndices;
    public boolean blocksProjectile;
    public int mapscene;
    public int configs[];
    public int supportItems;
    public int width;
    public boolean contouredGround;
    public boolean occludes;
    public static Client clientInstance;
    private boolean removeClipping;
    public boolean blockWalk;
    public int blocksides;
    private boolean sharelight;
    private static int cacheIndex;
    private int scaleY;
    public int[] models;
    public int varpID;
    public int decorDisplacement;
    private int[] shapes;
    public String description;
    public boolean isInteractive;
    public boolean castsShadow;
    public static ReferenceCache recent_models = new ReferenceCache(30);
    public int readyanim;
    private static ObjectDefinition[] cache;
    private int translateZ;
    private short[] recolorToFind;
    public static ReferenceCache baseModels = new ReferenceCache(500);
    public String actions[];

    @Override
    public int getAccessBitMask() {
        return blocksides;
    }

    @Override
    public int getIntValue(int paramID) {
        Object r = params.get(paramID);
        if(r instanceof Integer) {
            return (int) r;
        }
        return -1;
    }

    @Override
    public void setValue(int paramID, int value) {
        params.put(paramID, value);
    }

    @Override
    public String getStringValue(int paramID) {
        Object r = params.get(paramID);
        if(r instanceof String) {
            return (String) r;
        }
        return "";
    }

    @Override
    public void setValue(int paramID, String value) {
        params.put(paramID, value);
    }

    @Override
    public int getId() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getActions() {
        return actions;
    }

    @Override
    public int getMapSceneId() {
        return mapscene;
    }

    @Override
    public int getMapIconId() {
        return minimapFunction;
    }

    @Override
    public int[] getImpostorIds() {
        return configs;
    }

    @Override
    public RSObjectComposition getImpostor() {
        return method580();
    }

    @Override
    public RSIterableNodeHashTable getParams() {
        return null;// client doesnt have iterablenodehashtable
    }

    @Override
    public void setParams(IterableHashTable params) {

    }

    @Override
    public void setParams(RSIterableNodeHashTable params) {

    }

    @Override
    public void decodeNext(RSBuffer buffer, int opcode) {

    }

    @Override
    public int[] getModelIds() {
        return models;
    }

    @Override
    public void setModelIds(int[] modelIds) {
        this.models = new int[modelIds.length];
        System.arraycopy(modelIds, 0, this.models, 0, modelIds.length);
    }

    @Override
    public int[] getModels() {
        return shapes;
    }

    @Override
    public void setModels(int[] models) {
        this.shapes = new int[models.length];
        System.arraycopy(models, 0, this.shapes, 0, models.length);
    }

    @Override
    public boolean getObjectDefinitionIsLowDetail() {
        return lowMem;
    }

    @Override
    public int getSizeX() {
        return length;
    }

    @Override
    public void setSizeX(int sizeX) {
        this.length = sizeX;
    }

    @Override
    public int getSizeY() {
        return width;
    }

    @Override
    public void setSizeY(int sizeY) {
        this.width = sizeY;
    }

    @Override
    public int getInteractType() {
        return blockWalk ? 1 : 0;
    }

    @Override
    public void setInteractType(int interactType) {
        this.blockWalk = interactType == 1;
    }

    @Override
    public boolean getBoolean1() {
        return blocksProjectile;
    }

    @Override
    public void setBoolean1(boolean boolean1) {
        this.blocksProjectile = boolean1;
    }

    @Override
    public int getInt1() {
        return 0;
    }

    @Override
    public void setInt1(int int1) {
   //     this.isInteractive = int1 == 1;
    }

    @Override
    public int getInt2() {
        return decorDisplacement;
    }

    @Override
    public void setInt2(int int2) {
        this.decorDisplacement = int2;
    }

    @Override
    public int getClipType() {
        return contouredGround ? 0 : 1;
    }

    @Override
    public void setClipType(int clipType) {
        this.contouredGround = clipType == 0;
    }

    @Override
    public boolean getNonFlatShading() {
        return sharelight;
    }

    @Override
    public void setNonFlatShading(boolean nonFlatShading) {
        this.sharelight = nonFlatShading;
    }

    @Override
    public void setModelClipped(boolean modelClipped) {
        occludes = modelClipped;
    }

    @Override
    public boolean getModelClipped() {
        return occludes;
    }

    @Override
    public int getAnimationId() {
        return readyanim;
    }

    @Override
    public void setAnimationId(int animationId) {
        this.readyanim = animationId;
    }

    @Override
    public int getAmbient() {
        return ambient;
    }

    @Override
    public void setAmbient(int ambient) {
        this.ambient = ambient;
    }

    @Override
    public int getContrast() {
        return contrast;
    }

    @Override
    public void setContrast(int contrast) {
        this.contrast = contrast;
    }

    @Override
    public short[] getRecolorFrom() {
        return recolorToFind;
    }

    @Override
    public void setRecolorFrom(short[] recolorFrom) {
        this.recolorToFind = new short[recolorFrom.length];
        System.arraycopy(recolorFrom, 0, this.recolorToFind, 0, recolorFrom.length);
    }

    @Override
    public short[] getRecolorTo() {
        return recolorToReplace;
    }

    @Override
    public void setRecolorTo(short[] recolorTo) {
        this.recolorToReplace = new short[recolorTo.length];
        System.arraycopy(recolorTo, 0, this.recolorToReplace, 0, recolorTo.length);
    }

    @Override
    public short[] getRetextureFrom() {
        return textureFind;
    }

    @Override
    public void setRetextureFrom(short[] retextureFrom) {
        this.textureFind = new short[retextureFrom.length];
        System.arraycopy(retextureFrom, 0, this.textureFind, 0, retextureFrom.length);
    }

    @Override
    public short[] getRetextureTo() {
        return textureReplace;
    }

    @Override
    public void setRetextureTo(short[] retextureTo) {
        this.textureReplace = new short[retextureTo.length];
        System.arraycopy(retextureTo, 0, this.textureReplace, 0, retextureTo.length);
    }

    @Override
    public void setIsRotated(boolean rotated) {
        this.inverted = rotated;
    }

    @Override
    public boolean getIsRotated() {
        return inverted;
    }

    @Override
    public void setClipped(boolean clipped) {
        this.castsShadow = clipped;
    }

    @Override
    public boolean getClipped() {
        return castsShadow;
    }

    @Override
    public void setMapSceneId(int mapSceneId) {
        this.mapscene = mapSceneId;
    }

    @Override
    public void setModelSizeX(int modelSizeX) {
        this.scaleX = modelSizeX;
    }

    @Override
    public int getModelSizeX() {
        return scaleX;
    }

    @Override
    public void setModelHeight(int modelHeight) {
        this.scaleY = modelHeight;
    }

    @Override
    public void setModelSizeY(int modelSizeY) {
        this.scaleZ = modelSizeY;
    }

    @Override
    public void setOffsetX(int modelSizeY) {
        this.translateX = modelSizeY;
    }

    @Override
    public void setOffsetHeight(int offsetHeight) {
        this.translateY = offsetHeight;
    }

    @Override
    public void setOffsetY(int offsetY) {
        this.translateZ = offsetY;
    }

    @Override
    public void setInt3(int int3) {
        this.supportItems = int3;
    }

    @Override
    public void setInt5(int int5) {
        this.ambientSoundMin = int5;
    }

    @Override
    public void setInt6(int int6) {
        this.ambientSoundMax = int6;
    }

    @Override
    public void setInt7(int int7) {
        this.ambientSoundRange = int7;
    }

    @Override
    public void setBoolean2(boolean boolean2) {
        this.obstructsGround = boolean2;
    }

    @Override
    public void setIsSolid(boolean isSolid) {
        this.removeClipping = isSolid;
    }

    @Override
    public void setAmbientSoundId(int ambientSoundId) {
        this.ambientSoundID = ambientSoundId;
    }

    @Override
    public void setSoundEffectIds(int[] soundEffectIds) {
        this.ambientSoundIds = new int[soundEffectIds.length];
        System.arraycopy(soundEffectIds, 0, this.ambientSoundIds, 0, soundEffectIds.length);
    }

    @Override
    public int[] getSoundEffectIds() {
        return ambientSoundIds;
    }

    @Override
    public void setMapIconId(int mapIconId) {
        this.minimapFunction = mapIconId;
    }

    @Override
    public void setBoolean3(boolean boolean3) {
        this.randomAnimStart = !boolean3;
    }

    @Override
    public void setTransformVarbit(int transformVarbit) {
        this.varbitID = transformVarbit;
    }

    @Override
    public int getTransformVarbit() {
        return varbitID;
    }

    @Override
    public void setTransformVarp(int transformVarp) {
        this.varpID = transformVarp;
    }

    @Override
    public int getTransformVarp() {
        return varpID;
    }

    @Override
    public void setTransforms(int[] transforms) {
        this.configs = new int[transforms.length];
        System.arraycopy(transforms, 0, this.configs, 0, transforms.length);
    }

    @Override
    public int[] getTransforms() {
        return configs;
    }
}