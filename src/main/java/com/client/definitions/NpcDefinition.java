package com.client.definitions;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.IntStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.Arrays;
//
//import org.apache.commons.io.FileUtils;

import com.client.*;
import com.client.model.Npcs;
import com.client.utilities.ColourUtils;
import com.client.utilities.FieldGenerator;
import com.client.utilities.FileOperations;
import com.client.utilities.TempWriter;
import net.runelite.api.HeadIcon;
import net.runelite.api.IterableHashTable;
import net.runelite.rs.api.RSIterableNodeHashTable;
import net.runelite.rs.api.RSNPCComposition;


public final class NpcDefinition implements RSNPCComposition {

	public static NpcDefinition lookup(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].npcId == i)
				return cache[j];

		anInt56 = (anInt56 + 1) % 20;
		NpcDefinition entityDef = cache[anInt56] = new NpcDefinition();
		stream.currentPosition = streamIndices[i];
		entityDef.npcId = i;
		entityDef.readValues(stream);
		if (i == Npcs.BOB_BARTER_HERBS) {
			entityDef.actions = new String[] { "Talk-to", "Prices", "Decant", "Clean", null };
		}
//		if (i == 11840) {
//System.out.println("ready: "+	entityDef.readyanim+" walk: "+	entityDef.walkanim);
//			System.out.println("models: "+ Arrays.toString(entityDef.models));
//		}
		if(i == 7284){//gertrude has no stand anim? that's why it's invisible?
			entityDef.readyanim = 6471;//808 is default stand
			entityDef.walkanim = 6471;//819 default walk
		}
//		if(i == 8402){//gertrude has no stand anim? that's why it's invisible?
//			entityDef.readyanim = 808;//808 is default stand
//			entityDef.walkanim = 819;//819 default walk
//		}

		if (i == 15580) {//durial 321
			NpcDefinition elfinlocks = lookup(3014);

			entityDef.models = new int[] {187 /* green party hat*/,6669,35817
					,216/*face/head*/,13307/*barrows gloves*/,6659/*ahrims robeskirt*/,3704/*climbing boots*/,9347,5409,9638};
			entityDef.originalColors = new int []  { 926/*green partyhat*/,8741,14490,9152,};
			entityDef.newColors = new int [] { 22464/*green partyhat*/,10512,10512, ColourUtils.RGB_to_RS2HSB(new Color(242,245,253).getRGB())};

			entityDef.name = "Durial321";
			entityDef.description = "Bank your items!";
			entityDef.actions = new String[]{"Talk-to", null, null, null, null};
			entityDef.readyanim =elfinlocks.readyanim;//default sit
			entityDef.walkanim = elfinlocks.walkanim;

		}
		if (i == 7204){//donation store
			entityDef.name = "Donation Store";
		}
		if (i == 5792){//donation store
			entityDef.name = "Daily Rewards";
		}
		if (i == 687){//donation store
			entityDef.name = "Consumables";
		}
		if (i == 4306){//donation store
			entityDef.name = "Skill Capes";
		}
		if (i == 4847) {//wizard shop npc
			NpcDefinition elfinlocks = lookup(2995);
			entityDef.readyanim = elfinlocks.readyanim;
			entityDef.walkanim = elfinlocks.walkanim;
			entityDef.name = "Gypsy";
			entityDef.actions = new String[]{"Talk-to", null, null, null, null};
		}
		if (i == 15581) {//ranged store
			NpcDefinition elfinlocks = lookup(2995);
			entityDef.models = elfinlocks.models;
			entityDef.name = "Ranged Store";
			entityDef.actions = new String[]{"Talk-to", null, "Trade", null, null};
			entityDef.readyanim = elfinlocks.readyanim;
			entityDef.walkanim = elfinlocks.walkanim;
			entityDef.size = 1;
			entityDef.combatLevel = 0;
		}
		if (i == 306) {//lumbridge guide
		//	entityDef.name = "lumbridge Guide";
		}
		if (i == 15570) {//wizard shop npc

			entityDef.name = "Wizard Store";
			entityDef.actions = new String[] { "Talk-to", null, "Trade", null, null};
			entityDef.models = new int[] { 2215,181,251,292,170,176,4844,534,202 };
			entityDef.readyanim = 813;
			entityDef.walkanim = 813;
			entityDef.size = 1;
		}
		if (i == 15571) {//warrior shop npc

			entityDef.name = "Equipment Store";
			entityDef.actions = new String[] { "Talk-to", null, "Trade", null, null};
			entityDef.models = new int[] {7608, 249, 7610, 7605, 4924, 524, 517, 254, 185 };
			entityDef.readyanim = 813;
			entityDef.walkanim = 813;
			entityDef.size = 1;
		}
		if (i == 15565) {

			entityDef.name = "Pak Yak";//maybe name it later ?
			entityDef.actions = new String[] { "Talk-To", null,"Pickup", "Storage", null};
			entityDef.models = new int[] { 22165 };
			entityDef.readyanim = 5785;
			entityDef.walkanim = 5781;
			entityDef.npcWidth = 50;
			entityDef.npcHeight = 50;
			entityDef.size = 1;
		}
//		if (i == 15565) {
//
//			entityDef.name = "Pack Yak";//maybe name it later ?
//			entityDef.actions = new String[] { "Pickup", null, "Storage", null, null};
//			entityDef.models = new int[] { 22165 };
//			entityDef.readyanim = 5785;
//			entityDef.walkanim = 5781;
//			entityDef.npcWidth = 50;
//			entityDef.npcHeight = 50;
//			entityDef.size = 1;
//		}
		if (i == 15566) {//baby pack yack

			entityDef.name = "Pack Yak";
			entityDef.actions = new String[] { null, null, null, null, null};
			entityDef.models = new int[] { 22165 };
			entityDef.readyanim = 5785;
			entityDef.walkanim = 5781;
			entityDef.npcWidth = 50;
			entityDef.npcHeight = 50;
			entityDef.size = 1;
		}
		if (i == 15567) {//bigger pack yack

			entityDef.name = "Pack Yak";
			entityDef.actions = new String[] { null, null, null, null, null};
			entityDef.models = new int[] { 22165 };
			entityDef.readyanim = 5785;
			entityDef.walkanim = 5781;
			entityDef.npcWidth = 80;
			entityDef.npcHeight = 80;
			entityDef.size = 1;
		}
		if (i == 15568) {//bigger pack yack

			entityDef.name = "Pack Yak";
			entityDef.actions = new String[] { null, null, null, null, null};
			entityDef.models = new int[] { 22165 };
			entityDef.readyanim = 5785;
			entityDef.walkanim = 5781;
			entityDef.npcWidth = 100;
			entityDef.npcHeight = 100;
			entityDef.size = 1;
		}

		if (i == 15572) {//teen pack yak

			entityDef.name = "Pack Yak";
			entityDef.actions = new String[] { "Talk-To", null,"Pickup", "Storage", null};
			entityDef.models = new int[] { 22165 };
			entityDef.readyanim = 5785;
			entityDef.walkanim = 5781;
			entityDef.npcWidth = 70;
			entityDef.npcHeight = 70;
			entityDef.size = 1;
		}
		if(i == 390){
			entityDef.name = "Remi";
			entityDef.actions = new String[] { "Talk-to", null, "Trade", null, null};
		}
		if (i == 15573) {//adult pack yak

			entityDef.name = "Pack Yak";
			entityDef.actions = new String[] { "Talk-To", null,"Pickup", "Storage", null};
			entityDef.models = new int[] { 22165 };
			entityDef.readyanim = 5785;
			entityDef.walkanim = 5781;
			entityDef.npcWidth = 100;
			entityDef.npcHeight = 100;
			entityDef.size = 1;
		}


		if (i == 9460 || i == 1150 || i == 2912 || i == 2911 || i == 2910 || i == 6481
				|| i == 3500 || i == 9459 || i == 9457 || i == 9458) {
			// Setting combat to zero to npcs that can't be attacked
			entityDef.combatLevel = 0;
		}


		if (i == 8184) {
			entityDef.name = "Theatre Of Blood Wizard";
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Teleport";
		}


		if (i == 8026) {
			entityDef.combatLevel = 392;
		}
		if (i == 7913) {
			entityDef.combatLevel = 0;
			entityDef.name = "Ironman shop keeper";
			entityDef.description = "A shop specifically for iron men.";

		}



		if (i == 8026 || i == 8027 || i == 8028) {
			entityDef.size = 9;
		}


		if(i==3257){
			entityDef.combatLevel = 0;
			entityDef.actions = new String[] { "Trade", null, null, null, null };
		}


		if(i==1520){
			entityDef.combatLevel = 0;
			entityDef.actions = new String[] { "Small Net", null, "Harpoon", null, null };
		}
		if(i==8920){

			entityDef.actions = new String[] { null, "Attack", null, null, null };
		}


		if(i == 2108){
			entityDef.combatLevel = 0;
			entityDef.name = "Vote";
			entityDef.actions = new String[] { "Open-Shop", null, null, null, null };
		}


		if (i==7413) {
			entityDef.name = "Combat Dummy";
			entityDef.actions[0] = null;
		}



		if(i==4921){
			entityDef.combatLevel = 0;
			entityDef.name = "Supplies";
			entityDef.actions = new String[] { "Trade", null, null, null, null };
		}

		if (i == 11704) {
			entityDef.name = "@red@Crondis";
			entityDef.combatLevel = 1000;
			entityDef.drawmapdot = true;
			entityDef.readyanim = 808;
			entityDef.walkanim = 8843;
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { null, "Attack", null, null, null };
		}
		if (i == 8026) {
			entityDef.name = "Vorkath";
			// entityDef.combatLevel = 732;
			entityDef.models = new int[] { 35023 };
			entityDef.readyanim = 7946;
			entityDef.drawmapdot = true;
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Poke", null, null, null, null };
			entityDef.npcWidth = 100;
			entityDef.npcHeight = 100;//46191
		}
		if (i == 11654) {
			entityDef.name = "Apmeken";
			// entityDef.combatLevel = 732;
			entityDef.models = new int[] { 46191 };
			entityDef.readyanim = 808;
			entityDef.drawmapdot = true;
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { null, "Attack", null, null, null };
			entityDef.npcWidth = 100;
			entityDef.npcHeight = 100;//46191
		}
		if (i == 11655) {
			entityDef.name = "Pyramids Of Doom";
			entityDef.models = new int[] { 46210 };
			entityDef.readyanim = 9524;
			entityDef.walkanim = 9524;
			entityDef.drawmapdot = true;
			entityDef.originalColors = new int[]{8136, 8128};
			entityDef.newColors = new int[]{960, 960};
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { null, "Attack", null, null, null };
		}
		if (i == 7852 || i == 7853 || i == 7884) {//Dawn
			entityDef.readyanim = 7775;
			entityDef.walkanim = 7775;
		}
		if (i == 5518) {
			entityDef.readyanim = 185;
		}
		if (i == 8019) {
			entityDef.readyanim = 185;
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Trade";
		}

		if (i == 6088) {
			entityDef.readyanim = 185;
			entityDef.actions = new String[5];
			entityDef.actions[0] = "Talk-to";
			entityDef.actions[2] = "Travel";
		}
//		if (i == 1434 || i == 876 || i == 1612) {//gnome fix
//			entityDef.readyanim = 185;
//		}
		if (i >= 7354 && i <=7367) {
			entityDef.combatLevel = 0;
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Talk-to", null, "Pick-Up", "Metamorphosis", null };
		}
		if (i == 5559 || i == 5560) {
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Talk-to", null, "Pick-Up", "Metamorphosis", null };
		}
		if (i == 2149 || i == 2150 || i == 2151 || i == 2148) {
			entityDef.name = "Trading Clerk";
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Bank", null, "Trading Post", null, null };
		}



		if(i == 8688){
			entityDef.combatLevel = 0;
			entityDef.name = "Supplies";
			entityDef.actions = new String[] { "Talk-to", null, null, null, null };
		}

		if (i == 4325) {
			entityDef.name = "Light Creature";
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Pick-Up", "Talk-To", null, null, null };
			entityDef.npcWidth = 125; //WIDTH
			entityDef.npcHeight = 125; // HEIGHT
			entityDef.originalColors = null;
			entityDef.newColors = null;
			entityDef.combatLevel = 0;
			entityDef.readyanim = 2051;
			entityDef.walkanim = 2051;
		}
		if (i == 7118) {
			entityDef.name = "manaical monkey";
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Pick-Up", "Talk-To", null, null, null };
			entityDef.npcWidth = 200; //WIDTH
			entityDef.npcHeight = 200; // HEIGHT
			entityDef.originalColors = null;
			entityDef.newColors = null;
			entityDef.combatLevel = 0;
			entityDef.readyanim = 1386;
			entityDef.walkanim = 1382;
		}
		if (i == 11412) {
			entityDef.name = "Xarpus";
			entityDef.models = new int[] { 62389 }; //35383
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Pick-Up", "Talk-To", null, null, null };
			entityDef.npcWidth = 75; //WIDTH
			entityDef.npcHeight = 75; // HEIGHT
			entityDef.originalColors = null;
			entityDef.newColors = null;
			entityDef.combatLevel = 0;
			entityDef.readyanim = 8058;
			entityDef.walkanim = 8058;
			entityDef.models = new int[] { 62389 };
		}
		if (i == 11413) {
			entityDef.name = "Maiden";
			entityDef.models = new int[] { 35385 };
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Pick-Up", "Talk-To", null, null, null };
			entityDef.npcWidth = 70; //WIDTH
			entityDef.npcHeight = 70; // HEIGHT
			entityDef.originalColors = null;
			entityDef.newColors = null;
			entityDef.combatLevel = 0;
			entityDef.readyanim = 8090;
			entityDef.walkanim = 8090;
			entityDef.models = new int[] { 35385 };
		}
		if (i == 11414) {
			entityDef.name = "Sotetseg";
			entityDef.models = new int[] { 35403 };
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { "Pick-Up", "Talk-To", null, null, null };
			entityDef.npcWidth = 57; //WIDTH
			entityDef.npcHeight = 57; // HEIGHT
			entityDef.originalColors = null;
			entityDef.newColors = null;
			entityDef.combatLevel = 0;
			entityDef.readyanim = 8137;
			entityDef.walkanim = 8136;
			entityDef.models = new int[] { 35403 };
		}




		if (i == 8027) {
			entityDef.name = "Vorkath";
			entityDef.combatLevel = 732;
			entityDef.models = new int[] { 35023 };
			entityDef.readyanim = 7950;
			entityDef.drawmapdot = true;
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { null, null, null, null, null };
			entityDef.npcWidth = 100;
			entityDef.npcHeight = 100;
		}
		if (i == 8028) {
			entityDef.name = "Vorkath";
			entityDef.combatLevel = 732;
			entityDef.models = new int[] { 35023 };
			entityDef.readyanim = 7948;
			entityDef.drawmapdot = true;
			entityDef.actions = new String[5];
			entityDef.actions = new String[] { null, "Attack", null, null, null };
			entityDef.npcWidth = 100;
			entityDef.npcHeight = 100;
		}
		if(i==7144){
			entityDef.headicon_prayer = 0;
		}
		if(i==963){
			entityDef.headicon_prayer = 6;
		}
		if(i==7145){
			entityDef.headicon_prayer = 1;
		}
		if(i==7146){
			entityDef.headicon_prayer = 2;
		}
		if (entityDef.name != null && entityDef.name.toLowerCase().contains("chinchompa") && !entityDef.name.toLowerCase().contains("baby")) {
			entityDef.actions = new String[5];
		}
		return entityDef;
	}
	public static int totalAmount;

	public static void dump() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./npc_defs.txt"))) {
			for (int i = 0; i < 70_000; i++) {
				try {
					NpcDefinition def = NpcDefinition.lookup(i);
					if (def != null) {
						writer.write("Npc=" + i);
						writer.newLine();
						writer.write("Stand animation=" + def.readyanim);
						writer.newLine();
						writer.write("Walk animation=" + def.walkanim);
						writer.newLine();
						writer.newLine();
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void unpackConfig(FileArchive streamLoader) {
		stream = new Buffer(streamLoader.readFile("npc.dat"));
		Buffer stream = new Buffer(streamLoader.readFile("npc.idx"));
		totalAmount = stream.readUShort();
		streamIndices = new int[totalAmount+10000];
		int i = 2;
		for (int j = 0; j < totalAmount; j++) {
			streamIndices[j] = i;
			i += stream.readUShort();
		}

		cache = new NpcDefinition[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new NpcDefinition();
		for (int index = 0; index < totalAmount+10000; index++) {
			NpcDefinition ed = lookup(index);
			if (ed == null)
				continue;
			if (ed.name == null)
				continue;
		}
		if (Configuration.dumpDataLists) {
			NpcDefinitionDumper.dump();

			TempWriter writer2 = new TempWriter("npc_fields");
			FieldGenerator generator = new FieldGenerator(writer2::writeLine);
			IntStream.range(0, 100_000).forEach(id -> {
				try {
					NpcDefinition definition = NpcDefinition.lookup(id);
					generator.add(definition.name, id);
				} catch (Exception e) {
				}
			});
			writer2.close();
		}
		System.out.println("Loaded: " + totalAmount + " npcs");
	}



	public int category;
//
//
//
//    private void readValues(Buffer buffer) {
//        while (true) {
//            int opcode = buffer.get_unsignedbyte();
//            if (opcode == 0)
//                return;
//            if (opcode == 1) {
//                int j = buffer.get_unsignedbyte();
//                models = new int[j];
//                for (int j1 = 0; j1 < j; j1++)
//                    models[j1] = buffer.readUShort();
//
//            } else if (opcode == 2)
//                name = buffer.readJagexString();
//            else if (opcode == 12)
//                size = buffer.readSignedByte();
//            else if (opcode == 13)
//                readyanim = buffer.readUShort();
//            else if (opcode == 14)
//                walkanim = buffer.readUShort();
//            else if (opcode == 15)
//                readyanim_l = buffer.readUShort();
//            else if (opcode == 16)
//                readyanim_r = buffer.readUShort();
//            else if (opcode == 17) {
//                walkanim = buffer.readUShort();
//                walkanim_b = buffer.readUShort();
//                walkanim_l = buffer.readUShort();
//                walkanim_r = buffer.readUShort();
//                if (walkanim_b == 65535) {
//                    walkanim_b = -1;
//                }
//                if (walkanim_l == 65535) {
//                    walkanim_l = -1;
//                }
//                if (walkanim_r == 65535) {
//                    walkanim_r = -1;
//                }
//            } else if (opcode == 18) {
//                category = buffer.readUShort();
//            } else if (opcode >= 30 && opcode < 35) {
//                if (actions == null)
//                    actions = new String[10];
//                actions[opcode - 30] = buffer.readString();
//                if (actions[opcode - 30].equalsIgnoreCase("hidden"))
//                    actions[opcode - 30] = null;
//            } else if (opcode == 40) {
//                int k = buffer.get_unsignedbyte();
//                originalColors = new int[k];
//                newColors = new int[k];
//                for (int k1 = 0; k1 < k; k1++) {
//                    originalColors[k1] = buffer.readUShort();
//                    newColors[k1] = buffer.readUShort();
//                }
//            } else if (opcode == 41) {
//                int k = buffer.get_unsignedbyte();
//                originalTextures = new short[k];
//                newTextures = new short[k];
//                for (int k1 = 0; k1 < k; k1++) {
//                    originalTextures[k1] = (short) buffer.readUShort();
//                    newTextures[k1] = (short) buffer.readUShort();
//                }
//
//            } else if (opcode == 60) {
//                int l = buffer.get_unsignedbyte();
//                chatheadModels = new int[l];
//                for (int l1 = 0; l1 < l; l1++)
//                    chatheadModels[l1] = buffer.readUShort();
//
//            } else if (opcode == 93)
//                drawmapdot = false;
//            else if (opcode == 95)
//                combatLevel = buffer.readUShort();
//            else if (opcode == 97)
//                npcHeight = buffer.readUShort();
//            else if (opcode == 98)
//                npcWidth = buffer.readUShort();
//            else if (opcode == 99)
//                visible = true;
//            else if (opcode == 100)
//                ambient = buffer.readSignedByte();
//            else if (opcode == 101)
//                contrast = buffer.readSignedByte();
//            else if (opcode == 102)
//                headicon_prayer = buffer.readUShort();
//            else if (opcode == 103)
//                turnspeed = buffer.readUShort();
//            else if (opcode == 106 || opcode == 118) {
//                multivarbit = buffer.readUShort();
//                if (multivarbit == 65535)
//                    multivarbit = -1;
//                multivarp = buffer.readUShort();
//                if (multivarp == 65535)
//                    multivarp = -1;
//                int var3 = -1;
//                if (opcode == 118)
//                    var3 = buffer.readUShort();
//                int i1 = buffer.get_unsignedbyte();
//                multi = new int[i1 + 2];
//                for (int i2 = 0; i2 <= i1; i2++) {
//                    multi[i2] = buffer.readUShort();
//                    if (multi[i2] == 65535)
//                        multi[i2] = -1;
//                }
//                multi[i1 + 1] = var3;
//            } else if (opcode == 107) {
//                active = false;
//            } else if (opcode == 109) {
//                this.smoothwalk = false;
//            } else if (opcode == 111) {
//                this.is_follower = true;
//            } else if (opcode == 114) {
//                this.runanim = buffer.get_unsignedshort();
//            } else if (opcode == 115) {
//                this.runanim = buffer.get_unsignedshort();
//                this.runanim_b = buffer.get_unsignedshort();
//                this.runanim_l = buffer.get_unsignedshort();
//                this.runanim_r = buffer.get_unsignedshort();
//            } else if (opcode == 116) {
//                this.crawlanim = buffer.get_unsignedshort();
//            } else if (opcode == 117) {
//                this.crawlanim = buffer.get_unsignedshort();
//                this.crawlanim_b = buffer.get_unsignedshort();
//                this.crawlanim_l = buffer.get_unsignedshort();
//                this.crawlanim_r = buffer.get_unsignedshort();
//            } else if (opcode == 249) {
//                int length = buffer.get_unsignedbyte();
//
//                params = new HashMap<>(length);
//
//                for (int i = 0; i < length; i++) {
//                    boolean isString = buffer.get_unsignedbyte() == 1;
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
//            }
//        }
//    }
private void readValues(Buffer buffer) {
	while (true) {
		int opcode = buffer.get_unsignedbyte();
		if (opcode == 0)
			return;
		if (opcode == 1) {
			int j = buffer.get_unsignedbyte();
			models = new int[j];
			for (int j1 = 0; j1 < j; j1++)
				models[j1] = buffer.readUShort();

		} else if (opcode == 2)
			name = buffer.readJagexString();
		else if (opcode == 12)
			size = buffer.readSignedByte();
		else if (opcode == 13)
			readyanim = buffer.readUShort();
		else if (opcode == 14)
			walkanim = buffer.readUShort();
		else if (opcode == 15)
			readyanim_l = buffer.readUShort();
		else if (opcode == 16)
			readyanim_r = buffer.readUShort();
		else if (opcode == 17) {
			walkanim = buffer.readUShort();
			walkanim_b = buffer.readUShort();
			walkanim_l = buffer.readUShort();
			walkanim_r = buffer.readUShort();
			if (walkanim_b == 65535) {
				walkanim_b = -1;
			}
			if (walkanim_l == 65535) {
				walkanim_l = -1;
			}
			if (walkanim_r == 65535) {
				walkanim_r = -1;
			}
		} else if (opcode == 18) {
			category = buffer.readUShort();
		} else if (opcode >= 30 && opcode < 35) {
			if (actions == null)
				actions = new String[10];
			actions[opcode - 30] = buffer.readString();
			if (actions[opcode - 30].equalsIgnoreCase("hidden"))
				actions[opcode - 30] = null;
		} else if (opcode == 40) {
			int k = buffer.get_unsignedbyte();
			originalColors = new int[k];
			newColors = new int[k];
			for (int k1 = 0; k1 < k; k1++) {
				originalColors[k1] = buffer.readUShort();
				newColors[k1] = buffer.readUShort();
			}
		} else if (opcode == 41) {
			int k = buffer.get_unsignedbyte();
			originalTextures = new short[k];
			newTextures = new short[k];
			for (int k1 = 0; k1 < k; k1++) {
				originalTextures[k1] = (short) buffer.readUShort();
				newTextures[k1] = (short) buffer.readUShort();
			}

		} else if (opcode == 60) {
			int l = buffer.get_unsignedbyte();
			chatheadModels = new int[l];
			for (int l1 = 0; l1 < l; l1++)
				chatheadModels[l1] = buffer.readUShort();

		} else if (opcode == 93)
			drawmapdot = false;
		else if (opcode == 95)
			combatLevel = buffer.readUShort();
		else if (opcode == 97)
			npcHeight = buffer.readUShort();
		else if (opcode == 98)
			npcWidth = buffer.readUShort();
		else if (opcode == 99)
			visible = true;
		else if (opcode == 100)
			ambient = buffer.readSignedByte();
		else if (opcode == 101)
			contrast = buffer.readSignedByte();
		else if (opcode == 102)
			headicon_prayer = buffer.readUShort();
		else if (opcode == 103)
			turnspeed = buffer.readUShort();
		else if (opcode == 106 || opcode == 118) {
			multivarbit = buffer.readUShort();
			if (multivarbit == 65535)
				multivarbit = -1;
			multivarp = buffer.readUShort();
			if (multivarp == 65535)
				multivarp = -1;
			int var3 = -1;
			if (opcode == 118)
				var3 = buffer.readUShort();
			int i1 = buffer.get_unsignedbyte();
			multi = new int[i1 + 2];
			for (int i2 = 0; i2 <= i1; i2++) {
				multi[i2] = buffer.readUShort();
				if (multi[i2] == 65535)
					multi[i2] = -1;
			}
			multi[i1 + 1] = var3;
		} else if (opcode == 107) {
			active = false;
		} else if (opcode == 109) {
			this.smoothwalk = false;
		} else if (opcode == 111) {
			this.is_follower = true;
		} else if (opcode == 114) {
			this.runanim = buffer.get_unsignedshort();
		} else if (opcode == 115) {
			this.runanim = buffer.get_unsignedshort();
			this.runanim_b = buffer.get_unsignedshort();
			this.runanim_l = buffer.get_unsignedshort();
			this.runanim_r = buffer.get_unsignedshort();
		} else if (opcode == 116) {
			this.crawlanim = buffer.get_unsignedshort();
		} else if (opcode == 117) {
			this.crawlanim = buffer.get_unsignedshort();
			this.crawlanim_b = buffer.get_unsignedshort();
			this.crawlanim_l = buffer.get_unsignedshort();
			this.crawlanim_r = buffer.get_unsignedshort();
		} else if (opcode == 249) {
			int length = buffer.get_unsignedbyte();

			params = new HashMap<>(length);

			for (int i = 0; i < length; i++) {
				boolean isString = buffer.get_unsignedbyte() == 1;
				int key = buffer.read24Int();
				Object value;


				if (isString) {
					value = buffer.readString();
				} else {
					value = buffer.readInt();
				}

				params.put(key, value);
			}
		}
	}
}
    public Model method160() {
		if (multi != null) {
			NpcDefinition entityDef = get_multi_npctype();
			if (entityDef == null)
				return null;
			else
				return entityDef.method160();
		}
		if (chatheadModels == null) {
			return null;
		}
		boolean flag1 = false;
		for (int i = 0; i < chatheadModels.length; i++)
			if (!Model.isCached(chatheadModels[i]))
				flag1 = true;

		if (flag1)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[chatheadModels.length];
		for (int j = 0; j < chatheadModels.length; j++)
			aclass30_sub2_sub4_sub6s[j] = Model.getModel(chatheadModels[j]);

		Model model;
		if (aclass30_sub2_sub4_sub6s.length == 1)
			model = aclass30_sub2_sub4_sub6s[0];
		else
			model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);

		if (originalColors != null)
			for (int k = 0; k < originalColors.length; k++)
				model.recolor(originalColors[k], newColors[k]);


		if (originalTextures != null)
			for (int k = 0; k < originalTextures.length; k++)
				model.retexture(originalTextures[k], newTextures[k]);

		return model;
	}

	public NpcDefinition get_multi_npctype() {
		int j = -1;
		if (multivarbit != -1 && multivarbit <= 2113) {
			VarBit varBit = VarBit.cache[multivarbit];
			int k = varBit.setting;
			int l = varBit.start;
			int i1 = varBit.end;
			int j1 = Client.BIT_MASKS[i1 - l];
			j = clientInstance.variousSettings[k] >> l & j1;
		} else if (multivarp != -1)
			j = clientInstance.variousSettings[multivarp];
		int var3;
		if (j >= 0 && j < multi.length)
			var3 = multi[j];
		else
			var3 = multi[multi.length - 1];
		return var3 == -1 ? null : lookup(var3);
	}

	public Model get_animated_entity_model(int primary_index, AnimationDefinition primary_seq, int[] ai, int secondary_index, AnimationDefinition secondary_seq) {
		if (multi != null) {
			NpcDefinition entityDef = get_multi_npctype();
			if (entityDef == null)
				return null;
			else
				return entityDef.get_animated_entity_model(primary_index, primary_seq, ai, secondary_index, secondary_seq);
		}
		Model model = (Model) mruNodes.get(npcId);
		if (model == null) {
			boolean flag = false;
			for (int i1 = 0; i1 < models.length; i1++)
				if (!Model.isCached(models[i1]))
					flag = true;

			if (flag)
				return null;
			Model aclass30_sub2_sub4_sub6s[] = new Model[models.length];
			for (int j1 = 0; j1 < models.length; j1++)
				aclass30_sub2_sub4_sub6s[j1] = Model.getModel(models[j1]);

			if (aclass30_sub2_sub4_sub6s.length == 1)
				model = aclass30_sub2_sub4_sub6s[0];
			else
				model = new Model(aclass30_sub2_sub4_sub6s.length, aclass30_sub2_sub4_sub6s);
			if (originalColors != null) {
				for (int k1 = 0; k1 < originalColors.length; k1++)
					model.recolor(originalColors[k1], newColors[k1]);

			}
			if (originalTextures != null) {
				for (int i1 = 0; i1 < originalTextures.length; i1++)
					model.retexture(originalTextures[i1], newTextures[i1]);
			}

			model.light(64 + ambient, 850 + contrast, -30, -50, -30, true);
			// model.method479(84 + npcWidth, 1000 + npcHeight
			//, -90, -580, -90, true);
			mruNodes.put(model, npcId);
		}

		Model model_1;
		if (primary_seq != null && secondary_seq != null) {
			model_1 = primary_seq.animate_multiple(model, primary_index, secondary_seq, secondary_index);
		} else if (primary_seq != null) {
			model_1 = primary_seq.animate_either(model, primary_index);
		} else if (secondary_seq != null) {
			model_1 = secondary_seq.animate_either(model, secondary_index);
		} else {
			model_1 = model.bake_shared_animation_model(true);
		}
		if (npcHeight != 128 || npcWidth != 128)
			model_1.scale(npcHeight, npcHeight, npcWidth);
		model_1.calculateBoundsCylinder();
		model_1.face_label_groups = null;
		model_1.vertex_label_groups = null;
		if (size == 1)
			model_1.singleTile = true;
		return model_1;
	}

	private NpcDefinition() {

		walkanim_r = -1;
		multivarbit = -1;
		walkanim_b = -1;
		multivarp = -1;
		combatLevel = -1;
		anInt64 = 1834;
		walkanim = 819;
		size = 1;
		headicon_prayer = -1;
		readyanim = 808;
		npcId = -1L;
		turnspeed = 32;
		walkanim_l = -1;
		active = true;
		npcWidth = 128;
		drawmapdot = true;
		npcHeight = 128;
		visible = false;
	}

	@Override
	public String toString() {
		return "NpcDefinition{" +
				"npcId=" + npcId +
				", combatLevel=" + combatLevel +
				", name='" + name + '\'' +
				", actions=" + Arrays.toString(actions) +
				", walkingAnimation=" + walkanim +
				", size=" + size +
				", standingAnimation=" + readyanim +
				", childrenIDs=" + Arrays.toString(multi) +
				", models=" + Arrays.toString(models) +
				'}';
	}

	public static void nullLoader() {
		mruNodes = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public static void dumpList() {
//		NpcDefinition definition = lookup(965);
//		System.out.println("models: "+ Arrays.toString(definition.models));

//		try {
//			File file = new File("./temp/npc_list.txt");
//
//			if (!file.exists()) {
//				file.createNewFile();
//			}
//
//			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
//				for (int i = 0; i < totalAmount; i++) {
//					NpcDefinition definition = lookup(i);
//					if (definition != null) {
//						writer.write("npc = " + i + "\t" + definition.name + "\t" + definition.combatLevel + "\t"
//								+ definition.readyanim + "\t" + definition.walkanim + "\t");
//						writer.newLine();
//					}
//				}
//			}
//
//			System.out.println("Finished dumping npc definitions.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static void dumpSizes() {
		try {
			File file = new File(System.getProperty("user.home") + "/Desktop/npcSizes 143.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
				for (int i = 0; i < totalAmount; i++) {
					NpcDefinition definition = lookup(i);
					if (definition != null) {
						writer.write(i + "	" + definition.size);
						writer.newLine();
					}
				}
			}

			System.out.println("Finished dumping npc definitions.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public HashMap<Integer, Object> params;
	public boolean smoothwalk = true;
	public boolean is_follower = false;
	public int runanim_r = -1;
	public int readyanim_r = -1;
	public int readyanim_l = -1;
	public int walkanim_b = -1;

	public int walkanim_r = -1;
	public int runanim = -1;
	public int crawlanim = -1;
	public int runanim_l = -1;
	public int crawlanim_b = -1;
	public int runanim_b = -1;
	public int combatlevel = -1;
	public int crawlanim_r = -1;
	public int crawlanim_l = -1;


	public static int anInt56;
	public int multivarbit;

	public int multivarp;
	public static Buffer stream;
	public int combatLevel;
	public final int anInt64;
	public String name;
	public String actions[];
	public int walkanim;
	public byte size;
	public int[] newColors;
	public static int[] streamIndices;
	public int[] chatheadModels;
	public int headicon_prayer;
	public int[] originalColors;
	public short[] originalTextures, newTextures;
	/**
	 * standing anim
	 */
	public int readyanim;
	public long npcId;
	public int turnspeed;
	public static NpcDefinition[] cache;
	public static Client clientInstance;
	public int walkanim_l;
	public boolean active;
	public int ambient;
	public int npcWidth;
	public boolean drawmapdot;
	public int multi[];
	public String description;
	public int npcHeight;
	public int contrast;
	public boolean visible;
	public int[] models;
	public static ReferenceCache mruNodes = new ReferenceCache(70);
	public int[] anIntArray76;

	@Override
	public HeadIcon getOverheadIcon() {
		return null;
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

	@Override
	public String getName() {
		return null;
	}

	@Override
	public int[] getModels() {
		return new int[0];
	}

	@Override
	public String[] getActions() {
		return new String[0];
	}

	@Override
	public boolean isClickable() {
		return false;
	}

	@Override
	public boolean isFollower() {
		return false;
	}

	@Override
	public boolean isInteractible() {
		return false;
	}

	@Override
	public boolean isMinimapVisible() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public int getCombatLevel() {
		return 0;
	}

	@Override
	public int[] getConfigs() {
		return new int[0];
	}

	@Override
	public RSNPCComposition transform() {
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public int getRsOverheadIcon() {
		return 0;
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
}