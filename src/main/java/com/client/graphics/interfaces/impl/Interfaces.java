package com.client.graphics.interfaces.impl;

import com.client.Client;
import com.client.Configuration;
import com.client.Sprite;
import com.client.TextDrawingArea;

import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.impl.GroupIronmanBank;
import com.client.graphics.interfaces.builder.impl.LeaderboardInterface;
import com.client.graphics.interfaces.builder.impl.NotificationTab;
import com.client.graphics.interfaces.builder.impl.PerduLostPropertyShop;
import com.client.graphics.interfaces.builder.impl.PlayerPartyInterface;
import com.client.graphics.interfaces.builder.impl.QuestInterface;
import com.client.graphics.interfaces.builder.impl.WildWarning;
import com.client.graphics.interfaces.builder.impl.tasks.TaskInterface;
import com.client.graphics.interfaces.builder.impl.tob.TobFoodChest;
import com.client.graphics.interfaces.builder.impl.tob.TobPartyOverlay;
import com.client.graphics.interfaces.builder.impl.tob.TobRewardsInterface;
import com.client.graphics.interfaces.builder.impl.TombsOfAmascut.TombsOfAmascutFoodChest;
import com.client.graphics.interfaces.builder.impl.TombsOfAmascut.TombsOfAmascutRewardsInterface;
import com.client.graphics.interfaces.daily.DailyRewards;
import com.client.graphics.interfaces.dropdown.KeybindingMenu;
import com.client.graphics.interfaces.eventcalendar.EventCalendar;
import com.client.graphics.interfaces.settings.SettingsInterface;
import net.runelite.api.util.Text;

public final class Interfaces extends RSInterface {

	public static final int CLOSE_BUTTON_SMALL = 37302;
	public static final int CLOSE_BUTTON_SMALL_HOVER = 37303;
	public static final int SHOP_CONTAINER = 64016;
	public static void newAutocast(TextDrawingArea[] tda){
		//make sure configvalue to activate is '1'  - its how they all are.
		addConfigButton(24111, 333, 32, 33, "Emotes/EMOTE", 41, 47, "Defensive autocast", 1, 1, 109);
		RSInterface confix = interfaceCache[349];
		//RSInterface the333interface = interfaceCache[333];
		//the333interface.childY[11] = 80;
		RSInterface inter = get(24111);

		RSInterface fixgenie = get(2812);
		//fixgenie.sprite1 = confix.sprite1;
		fixgenie.sprite2 = confix.sprite2;

		RSInterface thetextforspell = get(18584);
		RSInterface thetextforspelld = get(24113);
		thetextforspelld.centerText = true;
		thetextforspell.centerText = true;
		inter.sprite1 = confix.sprite1;
		inter.sprite2 = confix.sprite2;
				//		Tab.sprite1 = imageLoader(bID, bName);
		//		Tab.sprite2 = imageLoader(bID2, bName);
		addSprites(18583, "Interfaces/Autocasts/SPRITE",
				780, //the default book
				328,327,326,325,
				336,335,334,333,
				340,339,338,337,
				332,331,330,329,
				15,17,19,21,
				23,26,29,32,
				35,38,40,44,
				46,48,51,52,
				362,363,364,365
		);
		addSprites(24114, "Interfaces/Autocasts/SPRITE",
				780, //the default book
				328,327,326,325,
				336,335,334,333,
				340,339,338,337,
				332,331,330,329,
				15,17,19,21,
				23,26,29,32,
				35,38,40,44,
				46,48,51,52,
				362,363,364,365

		);
        RSInterface rsinterface = interfaceCache[1829];
        rsinterface.childY[4] = 45;
        rsinterface.childY[5] = 45;
        rsinterface.childY[6] = 45;
        rsinterface.childY[7] = 45;

        rsinterface.childY[8] = 80;
        rsinterface.childY[9] = 80;
        rsinterface.childY[10] = 80;
        rsinterface.childY[11] = 80;

        rsinterface.childY[12] = 120;
        rsinterface.childY[13] = 120;
        rsinterface.childY[14] = 120;
        rsinterface.childY[15] = 120;
		//System.out.println("l: "+rsinterface.children.length);
	//	setChildren(rsinterface.children.length, rsinterface);
	//	RSInterface inter = get(328);
		addSpellSmaller(42988, 556, 21880, 7, 1, 30005, 28226, 80, "Wind Surge",
				"A very high level Air missile", tda, 0, 7,5);
		addSpellSmall(42989, 555, 556, 21880, 10, 7, 1, 30004, 30005, 28226, 84, "Water Surge",
				"A very high level Water missile", tda, 2, 10, 5);
		addSpellSmall(42990, 557, 556, 21880, 10, 7, 1, 30006, 30005, 28226, 89, "Earth Surge",
				"A very high level Earth missile", tda, 4, 10, 5);

		addSpellSmall(42991, 554, 556, 21880, 10, 7, 1, 30003, 30005, 28226, 94, "Fire Surge",
				"A very high level Fire missile", tda, 6, 10, 5);
		RSInterface.expandChildren(4, rsinterface);
		rsinterface.child( rsinterface.children.length - 4, 42991, 141, 155);//fire surge
		rsinterface.child( rsinterface.children.length - 3, 42990, 101, 155);//earth surge
		rsinterface.child( rsinterface.children.length - 2, 42989, 61, 155);//water surge
		rsinterface.child( rsinterface.children.length - 1, 42988, 21, 155);//air surge

	}
	public static TaskInterface taskInterface;
	public static void Curses(TextDrawingArea[] TDA) {
		RSInterface Interface = addTabInterface(27674);
		int index = 0;
		setChildren(71, Interface);
		addText(99501, "99/99", 0xFF981F, false, false, -1, TDA, 1);
		/*Top Row*/
		addPrayer(99503, 0, 713, 49, 7, "Protect Item", 99542);
		setBounds(99503, 4, 6, index, Interface);index++;//Glow
		setBounds(99504, 8, 8, index, Interface);index++;//Icon
		addPrayer(99505, 0, 714, 49, 4, "Sap Warrior", 99544);
		setBounds(99505, 40, 5, index, Interface);index++;//Glow
		setBounds(99506, 45, 10, index, Interface);index++;//Icon
		addPrayer(99507, 0, 715, 51, 5, "Sap Ranger", 99546);
		setBounds(99507, 76, 5, index, Interface);index++;//Glow
		setBounds(99508, 80, 10, index, Interface);index++;//Icon
		addPrayer(99509, 0, 716, 53, 3, "Sap Mage", 99548);
		setBounds(99509, 113, 5, index, Interface);index++;//Glow
		setBounds(99510, 116, 8, index, Interface);index++;//Icon
		addPrayer(99511, 0, 717, 55, 2, "Sap Spirit", 99550);
		setBounds(99511, 150, 5, index, Interface);index++;//Glow
		setBounds(99512, 155, 10, index, Interface);index++;//Icon
		/*2nd Row*/
		addPrayer(99513, 0, 718, 58, 18, "Berserker", 99552);
		setBounds(99513, 4, 45, index, Interface);index++;//Glow
		setBounds(99514, 5, 47, index, Interface);index++;//Icon child 11
		addPrayer(99515, 0, 719, 61, 15, "Deflect Summoning", 99554);
		setBounds(99515, 39, 45, index, Interface);index++;//Glow
		setBounds(99516, 42, 47, index, Interface);index++;//Icon
		addPrayer(99517, 0, 720, 64, 17, "Deflect Magic", 99556);
		setBounds(99517, 76, 45, index, Interface);index++;//Glow
		setBounds(99518, 79, 48, index, Interface);index++;//Icon
		addPrayer(99519, 0, 721, 67, 16, "Deflect Missiles", 99558);
		setBounds(99519, 113, 45, index, Interface);index++;//Glow
		setBounds(99520, 116, 48, index, Interface);index++;//Icon
		addPrayer(99521, 0, 722, 70, 6, "Deflect Melee", 99560);
		setBounds(99521, 151, 45, index, Interface);index++;//Glow
		setBounds(99522, 154, 48, index, Interface);index++;//Icon
		/*3rd Row*/
		addPrayer(99523, 0, 723, 73, 9, "Leech Attack", 99562);
		setBounds(99523, 4, 82, index, Interface);index++;//Glow
		setBounds(99524, 6, 86, index, Interface);index++;//Icon
		addPrayer(99525, 0, 724, 75, 10, "Leech Ranged", 99564);
		setBounds(99525, 40, 82, index, Interface);index++;//Glow
		setBounds(99526, 42, 86, index, Interface);index++;//Icon
		addPrayer(99527, 0, 725, 77, 11, "Leech Magic", 99566);
		setBounds(99527, 77, 82, index, Interface);index++;//Glow
		setBounds(99528, 79, 86, index, Interface);index++;//Icon
		addPrayer(99529, 0, 726, 79, 12, "Leech Defence", 99568);
		setBounds(99529, 114, 83, index, Interface);index++;//Glow
		setBounds(99530, 119, 87, index, Interface);index++;//Icon
		addPrayer(99531, 0, 727, 81, 13, "Leech Strength", 99570);
		setBounds(99531, 153, 83, index, Interface);index++;//Glow
		setBounds(99532, 156, 86, index, Interface);index++;//Icon
		/*Bottom Row*/
		addPrayer(99533, 0, 728, 83, 14, "Leech Energy", 99572);
		setBounds(99533, 2, 120, index, Interface);index++;//Glow child 30
		setBounds(99534, 5, 123, index, Interface);index++;//Icon
		addPrayer(99535, 0, 729, 85, 19, "Leech Special Attack", 99574);
		setBounds(99535, 40, 120, index, Interface);index++;//Glow
		setBounds(99536, 43, 123, index, Interface);index++;//Icon
		addPrayer(99537, 0, 730, 88, 1, "Wrath", 99576);
		setBounds(99537, 78, 120, index, Interface);index++;//Glow
		setBounds(99538, 82, 122, index, Interface);index++;//Icon
		addPrayer(99539, 0, 731, 91, 8, "Soul Split", 99578);
		setBounds(99539, 114, 120, index, Interface);index++;//Glow
		setBounds(99540, 116, 123, index, Interface);index++;//Icon
		addPrayer(99541, 0, 732, 94, 0, "Turmoil", 99580);
		setBounds(99541, 151, 120, index, Interface);index++;//Glow
		setBounds(99542, 153, 124, index, Interface);index++;//Icon
		/*Prayer Icon/Text*/
		addSprite(99502, 0, "Curses/ICON");
		setBounds(99501, 85, 225, index, Interface);index++;//Text child 40
		setBounds(99502, 65, 221, index, Interface);index++;//Icon 41
		/*Tooltips/Hover Boxes*/

		addPrayerHover_curse(TDA, 1, 99642,
				"Level 50\\nProtect Item\\nKeep 1 extra item if you die", 38,38,0,0);//the 38,38 and others dont matter only last 2 number matter
		addPrayerHover_curse(TDA, 1, 99544,
				"Level 50\\nSap Warrior\\nDrains 10% of enemy Attack,\\nStrength and Defence,\\nincreasing to 20% over time", 100,150,-25,0);
		addPrayerHover_curse(TDA, 1, 99546,
				"Level 52\\nSap Ranger\\nDrains 10% of enemy Ranged\\nand Defence, increasing to 20%\\nover time", 100, 175,-80,0);
		addPrayerHover_curse(TDA, 1, 99548,
				"Level 54\\nSap Mage\\nDrains 10% of enemy Magic\\nand Defence, increasing to 20%\\nover time", 100, 175,-100,0);
		addPrayerHover_curse(TDA, 1, 99550,
				"Level 56\\nSap Spirit\\nDrains enemy special attack\\nenergy", 100, 175,-130,0);//5
		addPrayerHover_curse(TDA, 1, 99552,
				"Level 59\\nBerserker\\nBoosted stats last 15% longer", 100, 175,0,10);
		addPrayerHover_curse(TDA, 1, 99554,
				"Level 62\\nDeflect Summoning\\nReduces damage dealt from\\nSummoning scrolls, prevents the\\nuse of a familiar's special\\nattack, and can deflect some of\\ndamage back to the attacker", 125, 175,-30,0);
		addPrayerHover_curse(TDA, 1, 99556,
				"Level 65\\nDeflect Magic\\nProtects against magical attacks\\nand can deflect some of the\\ndamage back to the attacker", 100, 175,-80,0);
		addPrayerHover_curse(TDA, 1, 99558,
				"Level 68\\nDeflect Missiles\\nProtects against ranged attacks\\nand can deflect some of the\\ndamage back to the attacker", 100, 175,-100,0);
		addPrayerHover_curse(TDA, 1, 99560,
				"Level 71\\nDeflect Melee\\nProtects against melee attacks\\nand can deflect some of the\\ndamage back to the attacker", 100, 175,-140,0);//5
		addPrayerHover_curse(TDA, 1, 99562,
				"Level 74\\nLeech Attack\\nBoosts Attack by 5%, increasing\\nto 10% over time, while draining\\nenemy Attack by 10%,\\nincreasing to 25% over time", 100, 175,0,0);


		addPrayerHover_curse(TDA, 1, 99564,"Level 76\\nLeech Ranged\\nBoosts Ranged by 5%, increasing\\nto 10% over time,\\nwhile draining enemy Ranged by\\n10%, increasing to 25% over\\ntime", 113, 175,-25,0);


		addPrayerHover_curse(TDA, 1, 99566, "Level 78\\nLeech Magic\\nBoosts Magic by 5%, increasing\\nto 10% over time, while draining\\nenemy Magic by 10%, increasing\\nto 25% over time", 100, 175,-80,0);
		addPrayerHover_curse(TDA, 1, 99568,"Level 80\\nLeech Defence\\nBoosts Defence by 5%, increasing\\nto 10% over time,\\nwhile draining enemy Defence by\\n10%, increasing to 25% over\\ntime", 113, 180,-110,0);


		addPrayerHover_curse(TDA, 1, 99570, "Level 82\\nLeech Strength\\nBoosts Strength by 5%, increasing\\nto 10% over time,\\nwhile draining enemy Strength by\\n10%, increasing to 25% over\\ntime", 113, 180,-140,0);//5

		addPrayerHover_curse(TDA, 1,99572, "Level 84\\nLeech Energy\\nDrains enemy run energy, while\\nincreasing your own", 113, 180,0,0);
		addPrayerHover_curse(TDA, 1,99574, "Level 86\\nLeech Special Attack\\nDrains enemy special attack\\nenergy, while increasing your\\nown", 113, 180,-25,0);
		addPrayerHover_curse(TDA, 1,99576, "Level 89\\nWrath\\nInflicts damage to nearby\\ntargets if you die", 113, 180,-80,0);
		addPrayerHover_curse(TDA, 1,99578, "Level 92\\nSoul Split\\n1/4 of damage dealt is\\nalso removed from\\nopponent's Prayer and added to\\nyour Hitpoints", 113, 180,-110,0);

		addPrayerHover_curse(TDA, 1,99580, "Level 95\\nTurmoil\\nIncreases Attack and Defence\\nby 15%, plus 15% of enemy's\\nlevel, and Strength by 23% plus\\n10% of enemy's level", 113, 180,-140,0);


	addSprite(99680, 1194, "Curses/SPRITE");
		hoverButton(99681, "Hide stat adjustments", 1195,1195, "Hide stat adjustments",  0, 0xff981f, 0xFFFFFF, true, "/Curses/SPRITE");

		addText(99682, "0%", TDA, 0, 0xff981f, true, false);
		addText(99683, "0%", TDA, 0, 0xff981f, true, false);
		addText(99684, "0%", TDA, 0, 0xff981f, true, false);
		addText(99685, "0%", TDA, 0, 0xff981f, true, false);
		addText(99686, "0%", TDA, 0, 0xff981f, true, false);


		setBounds(99680, 0,155, index, Interface);index++;//stat adjustments 42
		setBounds(99681, 0,200, index, Interface);index++;//hide stat adjustment 43
		setBounds(127_893, 62,239, index, Interface);index++;
		setBounds(127_894,95,244, index, Interface);index++;
		setBounds(99682, 20,185, index, Interface);index++;//0%
		setBounds(99683, 58, 185, index, Interface);index++;//0%
		setBounds(99684, 94, 185, index, Interface);index++;//0%
		setBounds(99685, 133, 185, index, Interface);index++;//0%
		setBounds(99686, 172, 185, index, Interface);index++;//0%
		setBounds(99642, 0, 0, index, Interface);index++;//protect item
		setBounds(99544, 40, 0, index, Interface);index++;
		setBounds(99546, 81, 0, index, Interface);index++;
		setBounds(99548, 121, 0, index, Interface);index++;


		setBounds(99550, 162, 0, index, Interface);index++;


		setBounds(99552, 0, 40, index, Interface);index++;
		setBounds(99554, 40, 40, index, Interface);index++;
		setBounds(99556, 81, 40, index, Interface);index++;
		setBounds(99558, 121, 40, index, Interface);index++;
		setBounds(99560, 162, 40, index, Interface);index++;
		setBounds(99562, 0, 81, index, Interface);index++;
		setBounds(99564, 40, 81, index, Interface);index++;
		setBounds(99566, 81, 81, index, Interface);index++;
		setBounds(99568, 121, 81, index, Interface);index++;
		setBounds(99570, 162, 81, index, Interface);index++;


		setBounds(99572, 0, 120, index, Interface);index++;
		setBounds(99574, 40, 120, index, Interface);index++;
		setBounds(99576, 81, 120, index, Interface);index++;
		setBounds(99578, 121, 120, index, Interface);index++;
		setBounds(99580, 162, 120, index, Interface);index++;


		hoverButton_fromfile(127_893, "Switch", 2011,2012, "", RSInterface.newFonts[0], 0xff981f, 0xffffff, true, "Interfaces/buttons/SPRITE");
		addText(127_894, "Switch", defaultTextDrawingAreas,0, 0xFFFFFF, true, true);

//		setBounds(127_893, 62,239, index, Interface);index++;
//		setBounds(127_894,95,244, index, Interface);index++;

//		setBounds(99680, 10, 160, index, Interface);index++;//stat adjustments
//		setBounds(99681, 10, 160, index, Interface);index++;//hide stat adjustment
//
//		setBounds(99682, 10, 160, index, Interface);index++;//hide stat adjustment
//		setBounds(99683, 10, 160, index, Interface);index++;//hide stat adjustment
//		setBounds(99684, 10, 160, index, Interface);index++;//hide stat adjustment
//		setBounds(99685, 10, 160, index, Interface);index++;//hide stat adjustment
//		setBounds(99686, 10, 160, index, Interface);index++;//hide stat adjustment
	}




	private Interfaces() {

	}

	public static void loadInterfaces() {
 	//	fireofExchange(defaultTextDrawingAreas);
		wrathRune();

		battlePass(defaultTextDrawingAreas);
		//new SettingsInterface().load(defaultTextDrawingAreas);//

		shopWidget(defaultTextDrawingAreas);

		keybinding(defaultTextDrawingAreas);
		ancients(defaultTextDrawingAreas);
		//helpDatabaseComponent(defaultTextDrawingAreas);
		//helpComponent(defaultTextDrawingAreas);
		new Bank().bank(defaultTextDrawingAreas);
		bankPin(defaultTextDrawingAreas);


		//teleportationInterface2(defaultTextDrawingAreas);
		SettingsTabWidget.widget(defaultTextDrawingAreas);//
		emoteTab();
		bountyHunterWidget(defaultTextDrawingAreas);
		godWars(defaultTextDrawingAreas);
		prayerBook(defaultTextDrawingAreas);
		equipmentScreen(defaultTextDrawingAreas);
		presetInterface(defaultTextDrawingAreas);
		equipmentTab(defaultTextDrawingAreas);
		itemsOnDeath(defaultTextDrawingAreas);
		Pestpanel(defaultTextDrawingAreas);
		Pestpanel2(defaultTextDrawingAreas);
		configureLunar(defaultTextDrawingAreas);
	//	achievements(defaultTextDrawingAreas);
		updateShopWidget(defaultTextDrawingAreas);
		initializeTitleWidget(defaultTextDrawingAreas);
		initializeCommandHelp();
		addPestControlRewardWidget(defaultTextDrawingAreas);
		addAntibotWidget(defaultTextDrawingAreas);

		barrowsKillcount(defaultTextDrawingAreas);
		lootingBag(defaultTextDrawingAreas);
		lootingBagAdd(defaultTextDrawingAreas);
		pakYak(defaultTextDrawingAreas);//

		runePouch(defaultTextDrawingAreas);
		quickPrayers(defaultTextDrawingAreas);
		listings(defaultTextDrawingAreas);
		tradingpost(defaultTextDrawingAreas);
		tradingSelect(defaultTextDrawingAreas);
		offer(defaultTextDrawingAreas);
		tradingSelected(defaultTextDrawingAreas);
		skotizo(defaultTextDrawingAreas);
		slayerOverlay(defaultTextDrawingAreas);
		prestigeInterface(defaultTextDrawingAreas);
		expLock(defaultTextDrawingAreas);
		skillsTab(defaultTextDrawingAreas);
		normals(defaultTextDrawingAreas);
		tournamentInterface(defaultTextDrawingAreas);
		tourneyJoinInterface(defaultTextDrawingAreas);
		collectionLog(defaultTextDrawingAreas);
		votePanel(defaultTextDrawingAreas);
		pollInterface(defaultTextDrawingAreas);
        pollResults(defaultTextDrawingAreas);
		SlayerRewards.initializeInterfaces(defaultTextDrawingAreas);
		new LootViewer().load(defaultTextDrawingAreas);
		new QuestTab().load(defaultTextDrawingAreas);

		fixDefensiveAutocast();
		EventCalendar.getCalendar().load(defaultTextDrawingAreas);
		Nightmare.instance.load(defaultTextDrawingAreas);
		staffSpecialBar();
	//	Autocast.getSingleton().load();
		questInterface(defaultTextDrawingAreas);
		DailyRewards.get().load();
		//StarterInterface.get().load();
		SpawnContainer.get().load();
		KillLog.get().load();
		WogwInterface.get().load();
		DonatorRewards.getInstance().load();

		groupInformation(defaultTextDrawingAreas);
		groupLeaderboard(defaultTextDrawingAreas);

		new MonsterDropViewer().OsDropViewer(defaultTextDrawingAreas);
dropTable(defaultTextDrawingAreas);
		new TobPartyOverlay().build();
		new TobRewardsInterface().build();
		new TombsOfAmascutRewardsInterface().build();
		new TombsOfAmascutFoodChest().build();
		new TobFoodChest().build();
		new GroupIronmanBank().build();
		new PlayerPartyInterface().build();
		new WildWarning().build();
		new PerduLostPropertyShop().build();
		new LeaderboardInterface().build();
		new QuestInterface().build();
		NewTele.unpack(defaultTextDrawingAreas);
		safeBox(defaultTextDrawingAreas);
		newFriendsList(defaultTextDrawingAreas);
		newWepSideBar(defaultTextDrawingAreas);
		newWepSideBar1(defaultTextDrawingAreas);
		newWepSideBar2(defaultTextDrawingAreas);
		newWepSideBar3(defaultTextDrawingAreas);
		newWepSideBar4(defaultTextDrawingAreas);
		newWepSideBar5(defaultTextDrawingAreas);
		newWepSideBar6(defaultTextDrawingAreas);
		newWepSideBar7(defaultTextDrawingAreas);
		newWepSideBar8(defaultTextDrawingAreas);
		newWepSideBar9(defaultTextDrawingAreas);
		newWepSideBar10(defaultTextDrawingAreas);
		newWepSideBar11(defaultTextDrawingAreas);
		newWepSideBar12(defaultTextDrawingAreas);
		newWepSideBar13(defaultTextDrawingAreas);
		newWepSideBar14(defaultTextDrawingAreas);
		newWepSideBar23(defaultTextDrawingAreas);
		profileTab(defaultTextDrawingAreas);
		mainPanel(defaultTextDrawingAreas);
		statisticspanel(defaultTextDrawingAreas);
		OSRSLogout(defaultTextDrawingAreas);
		ironman_widget_new(defaultTextDrawingAreas);
		regular_selection_panel(defaultTextDrawingAreas);
		OSRSSKillInterface(defaultTextDrawingAreas);
		OSRSQuestTabWidget.unpack(defaultTextDrawingAreas);//important that this is before the taskInterface.


		taskInterface = new TaskInterface();
		taskInterface.actions.loadAchievements();
		taskInterface.build();


		PriceChecker(defaultTextDrawingAreas);
		clanChatSetup(defaultTextDrawingAreas);
		homewordschange(defaultTextDrawingAreas);
		taskwidget(defaultTextDrawingAreas);
		Curses(defaultTextDrawingAreas);
		mysteryBox(defaultTextDrawingAreas);
        notiTab(defaultTextDrawingAreas);
		mailbox(defaultTextDrawingAreas);
		membershipbenefits(defaultTextDrawingAreas);
		upgradenow(defaultTextDrawingAreas);
		genieLamp();
		slayerPartner(defaultTextDrawingAreas);
		//quickinterface(defaultTextDrawingAreas);		quickinterface2(defaultTextDrawingAreas);
		newAutocast(defaultTextDrawingAreas);
		barrowsReward(defaultTextDrawingAreas);
		unpack_clanchat(defaultTextDrawingAreas);
		arceeusspellbook(defaultTextDrawingAreas);
	}
//	public static void quickinterface(TextDrawingArea[] tda){
//		RSInterface tab = addInterface(118_854);
//		hoverButton_fromfile(118_855, "Select", 1337,1337, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "Slayer interface/Unlock/1/unlock");
//		addText(118_856, "@lre@Unlocks Curses in the\\n @lre@prayer tab @red@(1000 points)", tda, 0, 0x46320a, true, true);
//		addText(118_857, "@lre@Ancient Curses", tda, 1, 0x46320a, true, true);
//
//		tab.totalChildren(3);
//		tab.child(0, 118_855, 10, 10);
//		tab.child(1,118_857, 150,25);
//		tab.child(2,118_856, 75,60);
//
//	}
//	public static void quickinterface2(TextDrawingArea[] tda){
//		RSInterface tab = addInterface(119_854);
//		hoverButton_fromfile(119_855, "Select", 1337,1337, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "Slayer interface/Unlock/2/unlock");
//		addText(119_856, "@lre@Unlocks Curses in the\\n @lre@prayer tab @red@(1000 points)", tda, 0, 0x46320a, true, true);
//		addText(119_857, "@lre@Ancient Curses", tda, 1, 0x46320a, true, true);
//
//		tab.totalChildren(3);
//		tab.child(0, 119_855, 10, 10);
//		tab.child(1,119_857, 150,25);
//		tab.child(2,119_856, 75,60);
//
//	}
    public static void OSRSSKillInterface(TextDrawingArea[] tda){

        //addSprite(8715, 1231, "Interfaces/Skillguide/SPRITE");
        addSprite(8715, 810, "Interfaces/SkillGuide/SPRITE");

//	hoverButton10(8812, "Close", 555, 556, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"/Interfaces/Skillguide/SPRITE");
        //  hoverButton10(8812, "Close", 569,570, "", tda,1, 16711680, 0xffffff, false);
       // closeButton(8812,815,816,false);
		closeButtonLarge_new(8812);
        addText(8716, "Attack", tda, 3, 0x46320a, true, false);
        addClickableText(8846, "Bronze", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8823, "Iron", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8824, "Steel", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8827, "Black", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8837, "White", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8840, "Mithril", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8843, "Adamant", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8859, "Rune", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8862, "Dragon", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(8865, "Barrows", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(15303, "Magic", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(15306,"Equipment", "Open subsection", tda, 3,0x46320a, true, false,55);
        addClickableText(15309, "Milestones", "Open subsection", tda, 3,0x46320a, true, false,55);

        addText(8849,"Weapons", tda, 3,0x46320a, true, false);


		RSInterface rsinterface = interfaceCache[8714];
		rsinterface.childX[0] = 0;//the background
		rsinterface.childY[0] = 0;
		rsinterface.childX[1] = 180;//skill name
		rsinterface.childY[1] = 15;
		rsinterface.childX[2] = 13;//the item list
		rsinterface.childY[2] = 80;
		rsinterface.childX[6] = 470;//close button
		rsinterface.childY[6] = 5;
		rsinterface.childX[3] = -1300;
		rsinterface.childY[3] = 80;
		rsinterface.childX[4] = -1300;
		rsinterface.childY[4] = 80;
		rsinterface.childX[5] = -1300;
		rsinterface.childY[5] = 80;
		rsinterface.childX[13] = 180;//the current category
		rsinterface.childY[13] = 32;
		rsinterface.childX[7] = 390;//iron
		rsinterface.childY[7] = 40;
		rsinterface.childX[12] = 395;//bronze
		rsinterface.childY[12] = 30;
		rsinterface.childX[8] = 395;//black
		rsinterface.childY[8] = 20;
		rsinterface.childX[9] = 395;//white
		rsinterface.childY[9] = 20;
		rsinterface.childX[10] = 395;//mithril
		rsinterface.childY[10] = 20;
		rsinterface.childX[11] = 395;//adamant
		rsinterface.childY[11] = 20;
		rsinterface.childX[14] = 395;//rune
		rsinterface.childY[14] = 120;
		rsinterface.childX[15] = 395;//dragon
		rsinterface.childY[15] = 67;
		rsinterface.childX[16] = 395;//barrows
		rsinterface.childY[16] = 67;
		rsinterface.childX[17] = 395;//magic
		rsinterface.childY[17] = 170;
		rsinterface.childX[18] = 395;//equipment
		rsinterface.childY[18] = 100;
		rsinterface.childX[19] = 395;//milestones
		rsinterface.childY[19] = 118;

		RSInterface rsinterfacescroll = interfaceCache[8717];
		rsinterfacescroll.width = 290;
		rsinterfacescroll.newScroller = true;

		RSInterface firstcategory = interfaceCache[8844];
		firstcategory.childX[0] = -1110;//the background
		firstcategory.childY[0] = 0;
//	firstcategory.childX[1] = 410;//the text
//	firstcategory.childY[1] = 65;

		RSInterface bigbox = interfaceCache[8813];
		for(int i = 0; i < 9; i++){
			bigbox.childX[i] = -1110;//the background
			bigbox.childY[i] = 0;
		}
		bigbox.childX[9] = 13;//the background
		bigbox.childY[9] = 35;
		bigbox.childX[10] = 15;//the background
		bigbox.childY[10] = 50;
		RSInterface bigbox2 = interfaceCache[8828];
		for(int i = 0; i < 8; i++){
			bigbox2.childX[i] = -1110;//the background
			bigbox2.childY[i] = 0;
		}
		RSInterface bigbox3 = interfaceCache[8850];
		for(int i = 0; i < 8; i++){
			bigbox3.childX[i] = -1110;//the background
			bigbox3.childY[i] = 0;
		}

		RSInterface blackbox = interfaceCache[8825];

		blackbox.childX[0] = -1110;//the background
		blackbox.childY[0] = 0;

		RSInterface adamantbox = interfaceCache[8825];

		adamantbox.childX[0] = -1110;//the background
		adamantbox.childY[0] = 0;

		RSInterface blackbox3 = interfaceCache[8841];

		blackbox3.childX[0] = -1110;//the background
		blackbox3.childY[0] = 0;

		RSInterface mithbox = interfaceCache[8838];

		mithbox.childX[0] = -1110;//the background
		mithbox.childY[0] = 0;

		RSInterface runebox = interfaceCache[8860];

		runebox.childX[0] = -1110;//the background
		runebox.childY[0] = 0;

		RSInterface dragonbox = interfaceCache[8863];

		dragonbox.childX[0] = -1110;//the background
		dragonbox.childY[0] = 0;

		RSInterface magicbox = interfaceCache[15294];
		for(int i = 0; i < 8; i++){
			magicbox.childX[i] = -1110;//the background
			magicbox.childY[i] = 0;
		}

		RSInterface equipmentbox = interfaceCache[15304];

		equipmentbox.childX[0] = -1110;//the background
		equipmentbox.childY[0] = 0;


		RSInterface milestonesbox = interfaceCache[15307];

		milestonesbox.childX[0] = -1110;//the background
		milestonesbox.childY[0] = 0;
    }

	public static void battlePass(TextDrawingArea[] ryan) {
		int interfaceId = 44400;
		int itemId = 44440;
		int finalItemId = 44460;
		RSInterface tab = addInterface(interfaceId);
		int index = 1;
		String dir = "/Interfaces/RaffleReward/SPRITE";
		addSprite(interfaceId + index++, 0, dir);
		configHoverButton1(interfaceId + index, "Close", dir, 5, 6, 6, 6, false, new int[] { interfaceId + index++ });
		addText(interfaceId + index++, "BattlePass", ryan, 2, 0xff981f, true, true);
		addText(interfaceId + index++, "Info", ryan, 2, 0xff981f, true, true);
		addText(interfaceId + index++, "Current Tier", ryan, 1, 0xff981f, false, true);
		addText(interfaceId + index++, "Current XP:", ryan, 1, 0xff981f, false, true);
		addText(interfaceId + index++, "XP To Next Tier:", ryan, 1, 0xff981f, false, true);
		addSprite(interfaceId + index++, 3, dir);
		addSprite(interfaceId + index++, 3, dir);
		addText(interfaceId + index++, "@red@Current", ryan, 2, 0xff981f, true, true);
		addText(interfaceId + index++, "@red@Next", ryan, 2, 0xff981f, true, true);
		addSprite(interfaceId + index++, 4, dir);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 1, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 2, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 3, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 4, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 5, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 6, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 7, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 8, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 9, 5, 900);
		addConfigButton(interfaceId + index++, interfaceId, 1, 2, dir, 71, 75, "", 10, 5, 900);


		for(int k = 1; k < 11; k++)
			addText(interfaceId + index++, "Reward", ryan, 2, 0xff981f, true, true);

		int basex = 60, basey = 24;
		index = 0;
		tab.totalChildren(34);
		tab.child(index++, interfaceId + index, basex, basey);
		tab.child(index++, interfaceId + index, basex + 364, basey + 7);
		tab.child(index++, interfaceId + index, basex + 194, basey+ 7);
		tab.child(index++, interfaceId + index, basex + 106, basey + 200);
		tab.child(index++, interfaceId + index, basex + 16, basey + 204 + 14);
		tab.child(index++, interfaceId + index, basex + 16, basey + 204 + 28);
		tab.child(index++, interfaceId + index, basex + 16, basey + 204 + 42);
		tab.child(index++, interfaceId + index, basex + 200, basey + 201);
		tab.child(index++, interfaceId + index, basex + 200 + 105, basey + 201);
		tab.child(index++, interfaceId + index, basex + 233, basey + 203);
		tab.child(index++, interfaceId + index, basex + 233 + 105, basey + 203);
		tab.child(index++, interfaceId + index, basex + 240 + 32, basey + 222);

		tab.child(index++, interfaceId + index, basex + 10, basey + 35);
		tab.child(index++, interfaceId + index, basex + 84, basey + 35);
		tab.child(index++, interfaceId + index, basex + 158, basey + 35);
		tab.child(index++, interfaceId + index, basex + 232, basey + 35);
		tab.child(index++, interfaceId + index, basex + 306, basey + 35);

		tab.child(index++, interfaceId + index, basex + 10, basey + 113);
		tab.child(index++, interfaceId + index, basex + 84, basey + 113);
		tab.child(index++, interfaceId + index, basex + 158, basey + 113);
		tab.child(index++, interfaceId + index, basex + 232, basey + 113);
		tab.child(index++, interfaceId + index, basex + 306, basey + 113);

		tab.child(index++, interfaceId + index, basex + 44, basey + 37);
		tab.child(index++, interfaceId + index, basex + 118, basey + 37);
		tab.child(index++, interfaceId + index, basex + 192, basey + 37);
		tab.child(index++, interfaceId + index, basex + 266, basey + 37);
		tab.child(index++, interfaceId + index, basex + 340, basey + 37);

		tab.child(index++, interfaceId + index, basex + 44, basey + 115);
		tab.child(index++, interfaceId + index, basex + 118, basey + 115);
		tab.child(index++, interfaceId + index, basex + 192, basey + 115);
		tab.child(index++, interfaceId + index, basex + 266, basey + 115);
		tab.child(index++, interfaceId + index, basex + 340, basey + 115);

		tab.child(index++, itemId, basex + 10, basey + 35);
		tab.child(index++, finalItemId, basex + 200, basey + 202);

		RSInterface items = addInterface(itemId);
		items.totalChildren(10);
		for(int x = 0; x < 2; x++) {
			itemGroup(itemId + 1 + x, 5, 2, 43, 48, true, true);
			interfaceCache[itemId + 1 + x].contentType = 206;
			items.child(x, itemId + 1 + x, 18, 27);
		}
		RSInterface finalitem = addInterface(finalItemId);
		finalitem.totalChildren(2);
		itemGroup(finalItemId + 1, 1, 1, 1, 1, true, true);
		interfaceCache[finalItemId + 1].contentType = 206;
		finalitem.child(0, finalItemId + 1, 17, 20);
		itemGroup(finalItemId + 2, 1, 1, 1, 1, true, true);
		interfaceCache[finalItemId + 2].contentType = 206;
		finalitem.child(1, finalItemId + 2, 123, 20);

	}


	public static void PriceChecker(TextDrawingArea[] tda) {
		RSInterface rsi = addInterface(43933);
	String dir = "/Interfaces/pricechecker/SPRITE";
		addSprite(59245, 548,dir);
		//	addPriceChecker(18246, new String[] { "Take 1", "Take 5", "Take 10", "Take All", "Take X" });
//		hoverButton10(28547, "Close", 1, 2, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"/Interfaces/PriceChecker/BUTTON");
//		hoverButton10(28548, "Close", 1, 2, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"/Interfaces/PriceChecker/BUTTON");
		hoverButton_fromfile(28547, "Close", 0,1, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "/Interfaces/buttons/close");

		rsi.totalChildren(7);
		rsi.child(0, 59245, 15, 17);
		rsi.child(1, 28547, 470,25);
		addText(28350, "Total value:", tda, 0, 0xFFFFFF, false, true);
		rsi.child(2, 28350, -2275, -7795);  // Open Text
		addText(28351, "Total value: 0", tda, 0, 0xFFFFFF, true, true);
		rsi.child(3, 28351, 335, 306);
		addText(28352, "Click on items in your inventory to check their values", tda, 0, 0xFFFFFF, false, true);
		rsi.child(4, 28352, 120, 160);

		rsi.child(5, 60015, 30, 65);
		int xDiff = 90;
		int yDiff = 70;
//		hoverButton10(28549, "Deposit Inventory", 248, 249, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"/Interfaces/PriceChecker/SPRITE");
		//hoverButton10FromCache(28549, "Deposit Inventory", 551,552, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"/Interfaces/PriceChecker/SPRITE");
		hoverButton_fromfile(28549, "Deposit Inventory", 181,182, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);

		rsi.child(6, 28549, 420, 288);

		RSInterface scrollz = addInterface(60015);
		scrollz.totalChildren(92);//67

		addPriceChecker(28546, new String[] { "Take 1", "Take 5", "Take 10", "Take All", "Take X" });
		//addItemContainer(28546, 6, 5, 60, 50, false,new String[] { "Take 1", "Take 5", "Take 10", "Take All", "Take X" });


		//addItem(28546, new String[] { "Add 1", "Add 5", "Add 10", "Add 60", "Add X" });



		scrollz.child(0, 28546, 10, 1); // the item container interface
		addText(21835, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(1, 21835, -730, 98); //just filler to get to 67 frames
		addText(21835, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(2, 21835, -730, 105);
		addText(21835, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(3, 21835, -6125, 85);
		addText(21835, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(4, 21835, -7125, 98);
		addText(21835, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(5, 21835, -6125, 105);
		addText(21283, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(6, 21283, -6125, 105);

		addText(28353, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(7, 28353, 10, 40); // 2 x 5
		addText(28354, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(8, 28354, 10, 52); // total
		addText(28355, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(9, 28355, 10, 57);
		addText(28356, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(10, 28356, 10+xDiff, 40);
		addText(28357, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(11, 28357, 10+xDiff, 52);
		addText(28358, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(12, 28358, 125, 105);
		addText(28359, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(13, 28359, 10+(xDiff)*2, 40);
		addText(28360, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(14, 28360, 10+(xDiff)*2, 52);
		addText(28361, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(15, 28361, 210, 105);
		addText(28362, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(16, 28362, 10+(xDiff)*3, 40);
		addText(28363, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(17, 28363, 10+(xDiff)*3, 52);
		addText(28364, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(18, 28364, 305, 105);
		addText(28365, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(19, 28365, 10+(xDiff)*4, 40);
		addText(28366, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(20, 28366, 10+(xDiff)*4, 52);
		addText(28367, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(21, 28367, 395, 105);

		addText(28368, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(22, 28368, 10, 40+yDiff);
		addText(28369, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(23, 28369, 10, 52+yDiff);
		addText(28370, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(24, 28370, 10, 165);
		addText(28371, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(25, 28371, 10+(xDiff), 40+yDiff);
		addText(28372, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(26, 28372, 10+(xDiff), 52+yDiff);
		addText(28373, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(27, 28373, 125, 165);
		addText(28374, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(28, 28374, 10+(xDiff)*2, 40+yDiff);
		addText(28375, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(29, 28375, 10+(xDiff)*2, 52+yDiff);
		addText(28376, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(30, 28376, 210, 165);
		addText(28377, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(31, 28377, 10+(xDiff)*3, 40+yDiff);
		addText(28378, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(32, 28378, 10+(xDiff)*3, 52+yDiff);
		addText(28379, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(33, 28379, 305, 165);
		addText(28380, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(34, 28380, 10+(xDiff)*4, 40+yDiff);
		addText(28381, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(35, 28381, 10+(xDiff)*4, 52+yDiff);
		addText(28382, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(36, 28382, 395, 165);


		addText(28383, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(37, 28383, 10,  40+(yDiff)*2);
		addText(28384, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(38, 28384, 10,  52+(yDiff)*2);
		addText(28385, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(39, 28385, 30, 225);
		addText(28386, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(40, 28386, 10+(xDiff), 40+(yDiff)*2);
		addText(28387, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(41, 28387, 10+(xDiff), 52+(yDiff)*2);
		addText(28388, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(42, 28388, 125, 225);
		addText(28389, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(43, 28389, 10+(xDiff)*2, 40+(yDiff)*2);
		addText(28390, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(44, 28390, 10+(xDiff)*2, 52+(yDiff)*2);
		addText(28391, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(45, 28391, 210, 225);
		addText(28392, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(46, 28392, 10+(xDiff)*3, 40+(yDiff)*2);
		addText(28393, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(47, 28393, 10+(xDiff)*3, 52+(yDiff)*2);
		addText(28394, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(48, 28394, 305, 225);
		addText(28395, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(49, 28395, 10+(xDiff)*4, 40+(yDiff)*2);
		addText(28396, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(50, 28396, 10+(xDiff)*4, 52+(yDiff)*2);
		addText(28397, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(51, 28397, 395, 225);

		addText(28398, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(52, 28398, 10, 40+(yDiff)*3);
		addText(28399, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(53, 28399, 10, 52+(yDiff)*3);
		addText(28400, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(54, 28400, 30, 280+5);
		addText(28401, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(55, 28401, 10+(xDiff), 40+(yDiff)*3);
		addText(28402, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(56, 28402, 10+(xDiff), 52+(yDiff)*3);
		addText(28403, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(57, 28403, 125, 280+5);
		addText(28404, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(58, 28404, 10+(xDiff)*2, 40+(yDiff)*3);
		addText(28405, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(59, 28405, 10+(xDiff)*2, 52+(yDiff)*3);
		addText(28406, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(60, 28406, 220, 280+5);
		addText(28407, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(61, 28407, 10+(xDiff)*3, 40+(yDiff)*3);
		addText(28408, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(62, 28408, 10+(xDiff)*3, 52+(yDiff)*3);
		addText(28409, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(63, 28409, 305, 280+5);
		addText(28410, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(64, 28410, 10+(xDiff)*4,  40+(yDiff)*3);
		addText(28411, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(65, 28411, 10+(xDiff)*4,  52+(yDiff)*3);
		addText(28412, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(66, 28412, 395, 280+5);

		addText(28413, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(67, 28413, 10, 40+(yDiff)*4);
		addText(28414, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(68, 28414, 10, 52+(yDiff)*4);
		addText(28415, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(69, 28415, 30, 280+5);
		addText(28416, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(70, 28416, 10+(xDiff), 40+(yDiff)*4);
		addText(28417, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(71, 28417, 10+(xDiff), 52+(yDiff)*4);
		addText(28418, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(72, 28418, 125, 280+5);
		addText(28419, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(73, 28419, 10+(xDiff)*2, 40+(yDiff)*4);
		addText(28420, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(74, 28420, 10+(xDiff)*2, 52+(yDiff)*4);
		addText(28421, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(75, 28421, 220, 280+5);
		addText(28422, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(76, 28422, 10+(xDiff)*3, 40+(yDiff)*4);
		addText(28423, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(77, 28423, 10+(xDiff)*3, 52+(yDiff)*4);
		addText(28424, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(78, 28424, 305, 280+5);
		addText(28425, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(79, 28425, 10+(xDiff)*4,  40+(yDiff)*4);
		addText(28426, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(80, 28426, 10+(xDiff)*4,  52+(yDiff)*4);
		addText(28427, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(81, 28427, 395, 280+5);

		addText(28428, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(82, 28428, 10, 40+(yDiff)*5);
		addText(28429, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(83, 28429, 10, 52+(yDiff)*5);
		addText(28430, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(84, 28430, 30, 280+5);
		addText(28431, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(85, 28431, 10+(xDiff), 40+(yDiff)*5);
		addText(28432, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(86, 28432, 10+(xDiff), 52+(yDiff)*5);
		addText(28433, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(87, 28433, 125, 280+5);
		addText(28434, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(88, 28434, 10+(xDiff)*2, 40+(yDiff)*5);
		addText(28435, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(89, 28435, 10+(xDiff)*2, 52+(yDiff)*5);
		addText(28436, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(90, 28436, 10+(xDiff)*4,  52+(yDiff)*5);
		addText(28437, "", tda, 0, 0xFFFFFF, false, true);scrollz.child(91, 28437, 395, 280+5);
		scrollz.height = 215;
		scrollz.width = 440;
		scrollz.scrollMax = 415;
	}
	public static void addPriceChecker(int index, String[] options) {

		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
//		rsi.inv = new int[30];
//		rsi.invStackSizes = new int[30];
		rsi.inventoryAmounts = new int[30];
		rsi.inventoryItemId = new int[30];
		rsi.spritesY = new int[20];
		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		for (int i = 0; i < options.length; i++) {
			if (options[i] == null) {
				continue;
			}
			rsi.actions[i] = options[i];
		}

		rsi.centerText = true;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.invSpritePadX = 60;
		rsi.invSpritePadY = 40;
		rsi.height = 6; //the amount of y coordinates
		rsi.width = 5; //the amount of x coordinates
		rsi.parentID = 28546;
		rsi.id = 28546;
		rsi.type = 2;


	}

	private static void RandomTeleInt(TextDrawingArea[] tda) {
		//Main Interface ID, and calling the addinterface method
		RSInterface tab = RSInterface.addInterface(13400);

		//Scroll bar Interface ID.
		RSInterface scrollTableft = RSInterface.addInterface(13430);
		int xOffset = 4;
		int yOffset = -8;

		//Scroll bar for drops
		RSInterface scrollTabright = RSInterface.addInterface(13498);

		//Main Interface Sprites
		RSInterface.addSprite(13401, new Sprite("teleport/background 0"));

		//Interface Text
		RSInterface.addText(13402, "Preview", tda, 2, 0xFF9900, true, true);
		RSInterface.addText(13403, "Description", tda, 2, 0xFF9900, true, true);
		RSInterface.addText(13404, "Drops", tda, 2, 0xFF9900, true, true);
		RSInterface.addText(13405, "Boss Names", tda, 2, 0xFF9900, true, true);

		//Category Buttons
		RSInterface.addButton(13406, 1, "teleport/button", "Select");
		RSInterface.addButton(13407, 1, "teleport/button", "Select");
		RSInterface.addButton(13408, 1, "teleport/button", "Select");
		RSInterface.addButton(13409, 1, "teleport/button", "Select");
		RSInterface.addButton(13410, 1, "teleport/button", "Select");
		RSInterface.addButton(13411, 1, "teleport/button", "Select");

		//Category Text
		RSInterface.addText(13412, "Bosses", tda, 0, 0xFF9900, true, true);
		RSInterface.addText(13413, "Monsters", tda, 0, 0xFF9900, true, true);
		RSInterface.addText(13414, "Wilderness", tda, 0, 0xFF9900, true, true);
		RSInterface.addText(13415, "Skilling", tda, 0, 0xFF9900, true, true);
		RSInterface.addText(13416, "Minigames", tda, 0, 0xFF9900, true, true);
		RSInterface.addText(13417, "Cities", tda, 0, 0xFF9900, true, true);

		//Description Text.
		RSInterface.addText(13418, "Name:", tda, 1, 0xFF9900, true, true);
		RSInterface.addText(13419, "Hitpoints:", tda, 1, 0xFF9900, true, true);
		RSInterface.addText(13420, "Recom Team Size:", tda, 1, 0xFF9900, true, true);
		RSInterface.addText(13421, "Attack Styles:", tda, 1, 0xFF9900, true, true);
		RSInterface.addText(13422, "Difficulty:", tda, 1, 0xFF9900, true, true);

		//Teleport Button
		RSInterface.addButton(13423,0,"teleport/teleport","Teleport");

		//Description Text.
		RSInterface.addText(13424, "1:", tda, 1, 0xfff000, true, true);
			RSInterface.addText(13425, "2:", tda, 1, 0xfff000, true, true);
		RSInterface.addText(13426, "3:", tda, 1, 0xfff000, true, true);
		RSInterface.addText(13427, "4:", tda, 1, 0xfff000, true, true);
		RSInterface.addText(13428, "5:", tda, 1, 0xfff000, true, true);

		RSInterface.addText(13429, "Teleport", tda, 2, 0xfff000, true, true);

		addItemContainer(13499, 8, 5, 8, 8, false,
				null, null, null, null, null);

		//Adds the NPC Onto the interface.
//		RSInterface.addPet(13500);

		//Positionining and Interface child
		RSInterface.setChildren(31, tab);
		tab.child(0, 13401, 7, 10);
		tab.child(1, 13402, 215, 68);
		tab.child(2, 13403, 405, 68);
		tab.child(3, 13404, 210, 244);
		tab.child(4, 13405, 85, 50);

		tab.child(5, 13406, 23, 17);
		tab.child(6, 13407, 102, 17);
		tab.child(7, 13408, 181, 17);
		tab.child(8, 13409, 260, 17);
		tab.child(9, 13410, 339, 17);
		tab.child(10, 13411, 419, 17);

		tab.child(11, 13412, 56, 23);
		tab.child(12, 13413, 134, 23);
		tab.child(13, 13414, 215, 23);
		tab.child(14, 13415, 295, 23);
		tab.child(15, 13416, 374, 23);
		tab.child(16, 13417, 454, 23);

		//Scrollbar positioning
		tab.child(17, 13430, 20 + xOffset, 82 + yOffset);

		//Description Text Positioning
		tab.child(18, 13418, 357, 90);
		tab.child(19, 13419, 366, 105);
		tab.child(20, 13420, 387, 120);
		tab.child(21, 13421, 378, 135);
		tab.child(22, 13422, 368, 150);

		//Teleport Button Positioning
		tab.child(23, 13423, 155, 208);


		//Text Input fields Positoning
		tab.child(24, 13424, 420, 90);
		tab.child(25, 13425, 410, 105);
		tab.child(26, 13426, 450, 120);
		tab.child(27, 13427, 441, 135);
		tab.child(28, 13428, 430, 150);
		tab.child(29, 13429, 220, 212);
		tab.child(30, 13498, 150, 265);



		//Scoll bar size, witdh and scrolling size.
		scrollTableft.width = 101;
		scrollTableft.height = 247;
		scrollTableft.scrollMax = 450;

		//Scroll bar size, width and scrolling size.
		scrollTabright.width = 334;
		scrollTabright.height = 60;
		scrollTabright.scrollMax = 200;

		int npcList = 50;
		int y = 1;

		for (int i = 0; i < npcList; i++) {
			RSInterface.addClickableText(13431 + i, "Teleport Name", "Teleport",tda, 1,0xFFA500, false, false, 150, 15);
		}

		RSInterface.setChildren(npcList, scrollTableft);

		for (int i = 0; i < npcList; i++) {
			scrollTableft.child(i, 13431 + i, 0, y);
			y += 15;
		}


		RSInterface.setChildren(1, scrollTabright);

		scrollTabright.child(0, 13499, 20, 10);

	}

	public static void teleportationInterface2(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(63000);
		rsInterface.width = 530;
		addSprite(63001, 0, "interfaces/teleInterface/background");
		addSprite(63002, 0, "interfaces/teleInterface/icon");
		addSpriteOnHover(63121, "interfaces/teleInterface/close", 0, 1, 16, 16, "", -1, 3);

		//addSprite(63117, 0, "interfaces/teleInterface/box");
		RSInterface teleportName = addText(63119, "Teleport Name", tda, 0, 0xc99f38, true, true);//tele name
		teleportName.width = 128;
		teleportName.height = 15;
		teleportName.centerVertically = true;


		addText(63230, "Line 1 text here", tda, 0, 0xFF981F, false, true);//tele name
		addText(63231, "Line 2 text here", tda, 0, 0xFF981F, false, true);//tele name
		addText(63232, "Line 3 text here", tda, 0, 0xFF981F, false, true);//tele name
		//addText(63233, "Line 3 text here", tda, 0, 0xFF981F, false, true);//tele name
		RSInterface infoText = addText(63129, "Information", tda, 0, 0xc99f38, false, true);//tele name
		infoText.height = 20;
		int buttonWidth = 73, buttonHeight = 17;
		addClickableText1(63055, "Monsters", "Monster & Slayer Teleports", tda, 0, 0xFF981F, true, true, buttonWidth, buttonHeight);
		addClickableText1(63056, "Bosses", "Boss Teleports", tda, 0, 0xFF981F, true, true, buttonWidth, buttonHeight);
		addClickableText1(63057, "Minigames", "Minigame Teleports", tda, 0, 0xFF981F, true, true, buttonWidth, buttonHeight);
		addClickableText1(63058, "Skilling", "Skilling Teleports", tda, 0, 0xFF981F, true, true, buttonWidth, buttonHeight);
		addClickableText1(63059, "Wilderness", "Wilderness Teleports", tda, 0, 0xFF981F, true, true, buttonWidth, buttonHeight);
		addClickableText1(63060, "Cities", "City & Donator Teleports", tda, 0, 0xFF981F, true, true, buttonWidth, buttonHeight);

		addClickableText1(63120, "Select Teleport", "Teleport to selected location", tda, 0, 0xFF981F, true, true, 160, 25);//confirm text
		addText(63123, "", tda, 1, 0xFF981F, false, true);//rightside name

		int itemListX = 348;
		int itemListY = 228;
		RSInterface lootRewards = addTabInterface(63114);

		lootRewards.height = 51;
		lootRewards.width = 123;
		lootRewards.contentType = 3000;
		lootRewards.totalChildren(1);
		RSInterface inventoryWidget = RSInterface.addInterface(63115);
		inventoryWidget.type = 2;
		inventoryWidget.width = 4;
		inventoryWidget.height = 30;

		inventoryWidget.inventoryItemId = new int[inventoryWidget.width * inventoryWidget.height];
		inventoryWidget.inventoryAmounts = new int[inventoryWidget.width * inventoryWidget.height];
		inventoryWidget.spritesX = new int[inventoryWidget.width * inventoryWidget.height];
		inventoryWidget.spritesY = new int[inventoryWidget.width * inventoryWidget.height];

		lootRewards.child(0, 63115, 8, 5);


		addPet(63116);

		RSInterface scroller = addTabInterface(63004);//teleports scrollbar
		scroller.height = 235;
		scroller.width = 303;
		scroller.contentType = 63000;

		int buttonCount = 20;

		scroller.totalChildren(buttonCount * 4);

		int firstRowX = 26;
		int secondRowX = 163;
		int startingButtonY = 10;
		int startingId = 63005;
		int startingTextId = 63131;
		int clickableButtonWidth = 126;
		int childId = 0;
		Sprite selectedTeleportSprite = imageLoader(2, "interfaces/teleInterface/teleport");
		for(int i = 0;i<buttonCount;i++) {
			RSInterface leftBtn = addSpriteOnHover(startingId, "interfaces/teleInterface/teleport", 0, 1, 126, 35, "", -1, 0);
			leftBtn.backgroundSprites = new Sprite[] { selectedTeleportSprite, leftBtn.sprite1 };
			setBounds(startingId, firstRowX, startingButtonY, childId++, scroller);//teleports row 1


			RSInterface text = addClickableText1(startingTextId, "", "", tda, 0, 0xFF981F, true, true, clickableButtonWidth, 35);
			text.centerVertically = true;
			text.underSpriteWidget = startingId;
			setBounds(startingTextId++, firstRowX, startingButtonY, childId++, scroller);//teleports row 1

			startingId++;

			RSInterface rightBtn = addSpriteOnHover(startingId, "interfaces/teleInterface/teleport", 0, 1, 126, 35, "", -1, 0);
			rightBtn.backgroundSprites = new Sprite[] { selectedTeleportSprite, rightBtn.sprite1};

			setBounds(startingId, secondRowX, startingButtonY, childId++, scroller);//teleports row 1

			text = addClickableText1(startingTextId, "", "", tda, 0, 0xFF981F, true, true, clickableButtonWidth, 35);
			text.centerVertically = true;
			text.underSpriteWidget = startingId;

			setBounds(startingTextId++, secondRowX, startingButtonY, childId++, scroller);//teleports row 1

			startingId++;

			startingButtonY += 40;
		}



		scroller.scrollMax = startingButtonY + 10;

		setChildren(19, rsInterface);

		setBounds(63001, 5, 10, 0, rsInterface);//background
		setBounds(63002, 14, 17, 1, rsInterface);//Monsters sprite
		setBounds(63121, 488, 18, 2, rsInterface);//exit sprite
		setBounds(63004, 6, 79, 3, rsInterface);//scroll bar

		int categoryTextY = 53;

		setBounds(63055, 20, categoryTextY, 4, rsInterface);//Category Text// monsters
		setBounds(63056, 103, categoryTextY, 5, rsInterface);//bosses
		setBounds(63057, 183, categoryTextY - 1, 6, rsInterface);//minigames
		setBounds(63058, 264, categoryTextY - 1, 7, rsInterface);//skilling
		setBounds(63059, 345, categoryTextY, 8, rsInterface);//wilderness
		setBounds(63060, 425, categoryTextY, 9, rsInterface);//cities

		setBounds(63114, itemListX, itemListY, 10, rsInterface);//Loot rewards overlay
		setBounds(63116, itemListX, 70 + 15, 11, rsInterface);//Loot rewards text

		//setBounds(63117, 337, 76, 12, rsInterface);//npc
		//setBounds(63118, 348, 107, 13, rsInterface);//overlay
		setBounds(63119, 355, 89, 12, rsInterface);//tele name
		setBounds(63120, 337, 290, 13, rsInterface);//confirm teleport button

		setBounds(63123, 366, 94, 14, rsInterface);//rightside name

		//Info text
		setBounds(63230, 355, 224, 15, rsInterface);
		setBounds(63231, 355, 241, 16, rsInterface);
		setBounds(63232, 355, 258, 17, rsInterface);


		setBounds(63129, 373, 202, 18, rsInterface);//Info

		//setBounds(63004, 55, 70, 3, rsInterface);//Monsters Text
		//setBounds(63005, 135, 70, 4, rsInterface);//Bosses Text

	}


	public static void taskwidget(TextDrawingArea[] font) {
		RSInterface widget = addInterface(54731);
		addSprite(54732, 1421,  "tasks/SPRITE");
		addText(54733, "Task Manager", font, 2, 16751360, true, true);
		addText(54734, "Task Information", font, 1, 16751360, false, true);
		addText(54735, "Potential Rewards", font, 1, 16751360, false, true);

		RSInterface scroll = addInterface(54737);
		scroll.totalChildren(3);
		int x = 5;
		int y = 5;
		int offset = 15;

			addText(54738, "what to do", font, 0, 0xffffff, false, false);
			scroll.child(0, 54738, 0, 15);

		addText(54739, "Requirements", font, 0, 16751360, false, false);
		scroll.child(1, 54739, 10, 33);

		addText(54740, "Requirements what to do", font, 0, 0xffffff, false, false);
		scroll.child(2, 54740, 30, 50);


		RSInterface itemContainer = addInterface(54758);
		itemContainer.scrollMax = 1150;
		itemContainer.width = 335;
		itemContainer.height = 75;
		itemContainer.totalChildren(1);

		addItemContainer(54759, 7,30,7,8, false);
		itemContainer.child(0, 54759, 0, 0);

		addText(54762, "0% (0/100)", font, 0, 0, true, false);
		addProgressBar2021(54760, 270, 15, 0);

		addSprite(54764, 2097, "tasks/SPRITE");

		hoverButton_fromfile(54763, "Claim", 2110,2111,"", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "tasks/SPRITE");

		addText(54766, "<col=ffff00>Collect", font, 0, 0, true, true);
		//closeButton(54767, 2095, 2096, false);
		closeButtonSmall(54767);
		widget.totalChildren(12);
		widget.child(0, 54732, 20, 10);
		widget.child(1, 54733, 263, 20);
		widget.child(2, 54734, 210, 60);
		widget.child(3, 54735, 210, 210);
		widget.child(4, 54737, 210, 70);
		widget.child(5, 54758, 130, 235);
		widget.child(6, 54760, 120, 140);
		widget.child(7, 54762, 260, 143);
		widget.child(8, 54763, 225, 165);
		widget.child(9, 54764, 228, 170);
		widget.child(10, 54766, 270,172);
		widget.child(11, 54767, 470, 20);

	}
	public static void slayerPartner(TextDrawingArea[] TDA) {
		RSInterface Interface = addInterface(96500);

		addSprite(96499, 2, "Interfaces/slayerpartner/SPRITE");
		addSprite(96501, 1, "Interfaces/slayerpartner/SPRITE");

		closeButtonLarge(96503);
		hoverButton_fromfile(96504, "New partner", 686,685, "New partner", RSInterface.newFonts[0], 0xff981f, 0xffffff, true, "Interfaces/buttons/SPRITE");
		addText(96505, "(none)", TDA, 1, 0xff9933, false, true);

		setChildren(5, Interface);
		setBounds(96499, 100, 100, 0, Interface);
		setBounds(96501, 100, 100, 1, Interface);
		//setBounds(96502, 18, 62, 1, Interface);
		setBounds(96503, 375,106, 2, Interface);
		setBounds(96504, 180,258, 3, Interface);
		setBounds(96505, 210,140, 4, Interface);


	}

		public static void questInterface(TextDrawingArea[] tda) {
		RSInterface widget = interfaceCache[8134];
		// setChildren(4, widget);
		//addSprite(8135, 0, "Interfaces/GuideBook/SPRITE");
		addSprite(8135,0, "quest/questbg");
			//		addSprite(8136, 1, "quest/questbg");
	//	setBounds(8135 ,15, 25, 0, widget);
		setBounds(8135 ,18, 5, 0, widget);

			hoverButton_fromfile(8136, "Close", 0,1, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "quest/close");

		setBounds(8136 ,440, 70, 1, widget);
		setBounds(8143 ,85, 98, 2, widget);
			widget.centerText = true;

			RSInterface.interfaceCache[8143].newScroller = true;
			RSInterface.interfaceCache[8143].height = 210;
//		Interface.height = 217;
			RSInterface.interfaceCache[8143].width = 370;
			RSInterface.interfaceCache[8143].scrollMax = 318;
			addText(8144, "Quest Text", tda, 3, 0x800000, true, false);
			setBounds(8144 ,260,17,  3, widget);//the header text
	}


	//this is a wild way to add stuff to an existing interface holy fuck. just think about it.

	public static void staffSpecialBar() {
		RSInterface inter = get(328);
		RSInterface.expandChildren(1, inter);
		inter.child( inter.children.length - 1, 12323, 17, 201);
	}
	public static void testdiary(TextDrawingArea[] tda) {
		RSInterface inter = addInterface(89000);
		//addSprite(89001, 0, "Interfaces/FireOfExchange/SPRITE");
		RSInterface inter2 = addInterface(89001);
		inter.totalChildren(1);
		setBounds(89001, 0, 0, 0, inter);
	//	setBounds(89001, 1, 0, 0, inter);
	}
    public static void fireofExchange(TextDrawingArea[] tda) {
        RSInterface inter = addInterface(33400);
        addSprite(33401, 0, "Interfaces/FireOfExchange/SPRITE");
        addText(33402, "Fire of Exchange", tda, 2, 0xff9933, true, true);
		addHoverButton(33932, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 33932, 3);
		addHoveredButton(33933, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 33933);
		addSprite(33406, 1, "Interfaces/FireOfExchange/SPRITE");
		addText(33407, "Exchange Value:", tda, 2, 0xff9933, true, true);
		addText(33408, "Total Exchange Points:", tda, 2, 0xff9933, true, true);
		addText(33409, "0", tda, 2, 0xff0000, true, true);
		addText(33410, "00", tda, 2, 0xff0000, true, true);
		addButton(33411, 1, "Interfaces/FireOfExchange/BUTTON", "Open Exchange Store");
		addButton(47003, 527, "Interfaces/MysteryBox/SPRITE", "Close");
		addText(33412, "Exchange Store", tda, 2, 0xffffff, true, true);
		addButton(33413, 2, "Interfaces/FireOfExchange/ARROW", "Exchange Item");
        inter.totalChildren(14);
        setBounds(33401, 88, 77, 0, inter);
        setBounds(33402, 260, 92, 1, inter);
		setBounds(33932, 402, 93, 3, inter);
		setBounds(33933, 402, 93, 4, inter);
		setBounds(33406, 146, 139, 5, inter);
		setBounds(33407, 166, 185, 6, inter);
		setBounds(33408, 332, 139, 7, inter);
		setBounds(33409, 166, 205, 8, inter);
		setBounds(33410, 332, 159, 9, inter);
		setBounds(33411, 261, 197, 10, inter);
		setBounds(33412, 332, 206, 11, inter);
        addItemContainer(33403, 1, 1, 0, 0, false);
		setBounds(33403, 150, 142, 12, inter);
		setBounds(33413, 201, 146, 13, inter);


        inter = addInterface(33404);
        inter.totalChildren(1);
        addItemContainer(33405, 4, 7, 10, 4, true,
				"Offer");
        setBounds(33405, 16, 8, 0, inter);


    }

	public static void pollInterface(TextDrawingArea tda[]) {
		RSInterface rsi = addTabInterface(21406);


		Sprite BG = new Sprite("/notifications_tab/bg");


		addSprite(100_551, BG);
		closeButtonSmall(100_552);

		addText(121408, "Poll", tda, 2, 0xFF9300, true, true);
		addText(21408, "Question here", tda, 0, 0xFF9300, true, true);//the question
		hoverButton_fromfile(100_553, "Vote", 0,1, "Vote", RSInterface.newFonts[1], 0xFF9300, 0xffffff, true, "Interfaces/PollTab/BUTTON");
		addText(21499, "Time Left to Vote: 13 Hours", tda, 0, 0xFF9300, true, true);//21409

		RSInterface scroll = addInterface(21407);
		int childId = 21409;
		scroll.width = 165;
		scroll.height = 258;



//		for(int i = 0; i < 2; i++) {
//			addText(childId++, "" + (i + 1) + ")", tda, 0, 0xFF9300, true, true);
//			addConfigButton(childId++, 618, 2, 4, "Interfaces/Ironman/IMAGE", 17, 17, "Select", 1, 1, 30 + i);
//			addText(childId++, "yes/no", tda, 0, 0xFF9300, false, true);
//		}
		for(int i = 0; i < 5; i++) {
			addText(childId++, "", tda, 0, 0xFF9300, true, true);
			addConfigButton(childId++, 618, 2, 4, "Interfaces/Ironman/IMAGE", 17, 17, "Select", 1, 1, 30 + i);
			addText(childId++, "Give bronze dagger\\na spec", tda, 0, 0xFF9300, false, true);
		}
//		addHoverButton(childId++, "Interfaces/PollTab/BUTTON", 0, 150, 35, "Vote", -1, childId, 1);
//		addHoveredButton(childId++, "Interfaces/PollTab/BUTTON", 1, 150, 35, childId++);
//       // addText(childId++, "Vote", tda, 2, 0xFF9300, true, true);
//        hoverButton_fromfile(childId++, "Vote", 0,1, "Vote", RSInterface.newFonts[1], 0xFF9300, 0xffffff, true, "Interfaces/PollTab/BUTTON");

	//	scroll.totalChildren(6);
		scroll.totalChildren(15);
		childId = 21409;
		int frame = 0;

		//scroll.child(frame++, childId++, 80, 25);

		int startX = 15;
		int startY = 50;
//		for(int i = 0; i < 2; i++) {
//			scroll.child(frame++, childId++, startX, startY);
//			scroll.child(frame++, childId++, startX + 10, startY - 2);
//			scroll.child(frame++, childId++, startX + 35, startY);
//			startY += 22;
//		}
		for(int i = 0; i < 5; i++) {
			scroll.child(frame++, childId++, startX, startY);
			scroll.child(frame++, childId++, startX + 10, startY - 2);
			scroll.child(frame++, childId++, startX + 35, startY);
			startY += 22;
		}
		//scroll.child(frame++, childId++, 5, 165);
//		scroll.child(frame++, childId++, 5, 1650);
		//childId++;
       // scroll.child(frame++, childId++, 5, 165);

		rsi.totalChildren(7);
        rsi.child(2, 21407, 16, 0);
		rsi.child(3, 121408, 96, 6);
		rsi.child(4, 21408, 96, 40);
		rsi.child(0, 100_551, 12,-2);
		rsi.child(1, 100_552, 157,5);
		rsi.child(5, 100_553, 21,170);
		rsi.child(6, 21499, 90, 25);

	}

	public static void pollResults(TextDrawingArea tda[]) {
        RSInterface rsi = addInterface(21429);

        int childId = 21430;
        addText(childId++, "Current Votes:", tda, 2, 0xFF9300, true, true);

        for(int i = 0; i < 5; i++) {
            addText(childId++, "", tda, 0, 0xFF9300, true, true);
        }

        for(int i = 0; i < 5; i++) {
            addText(childId++, "", tda, 0, 0xFF9300, true, true);
        }

		childId = 21466;
		addText(childId++, "The Question", tda, 0, 0xFF9300, true, true);
		addText(childId++, "The time remaining", tda, 0, 0xFF9300, true, true);
        rsi.totalChildren(childId - 21430 );
        childId = 21430;
        int frame = 0;

        rsi.child(frame++, childId++, 96, 70);

        int startY = 90;
        for(int i = 0; i < 5; i++) {
            rsi.child(frame++, childId++, 96, startY);
            startY += 20;
        }

        startY = 150;
        for(int i = 0; i < 5; i++) {
            rsi.child(frame++, childId++, 96, startY);
            startY += 20;
        }
		childId = 21466;
	//	addText(childId++, "The Question", tda, 0, 0xFF9300, true, true);
		rsi.child(frame++, childId++, 96, 30);//the question
		rsi.child(frame++, childId++, 96, 10);//time remaining
    }
	public static void fixDefensiveAutocast() {
		RSInterface rsi = interfaceCache[24111];
		rsi.anIntArray212 = new int[1];
		rsi.anIntArray212[0] = 1;
		rsi.anIntArray245 = new int[1];
		rsi.anIntArray245[0] = 1;
		rsi.scripts = new int[1][3];
		rsi.scripts[0][0] = 5;
		rsi.scripts[0][1] = Configs.AUTOCAST_DEFENCE_CONFIG;
		rsi.scripts[0][2] = 0;
		rsi.ignoreConfigClicking = true;
	}

	public static void mysteryBox(TextDrawingArea[] tda) {
		RSInterface iface = addInterface(47000);
		/* Base interface */
		//addSpriteLoader(47001, 1073);
		addSprite(47001, 1073, "Interfaces/MysteryBox/SPRITE");
		//addSprite(65001, 0, "Interfaces/Teleporting/Background");
		addText(47002, "Mystery Box", tda, 2, 0xFFA500, true, true);
	//	addButton(47003, 527, "Interfaces/MysteryBox/SPRITE", "Close");
		closeButtonSmall(47003);
	//	addButton(47004, 810, "Interfaces/MysteryBox/SPRITE", "Spin!");
		hoverButton_fromfile(47004, "Spin", 810,525, "Spin!", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "Interfaces/MysteryBox/SPRITE");


		addText(47005, "@gre@Spin!", tda, 2, 0xFFA500, true, true);
		addSprite(47006, 530, "Interfaces/MysteryBox/SPRITE");
		addSprite(47007, 531, "Interfaces/MysteryBox/SPRITE");
		addText(47008, "Feeling lucky?", tda, 2, 0xFFA500, true, true);
		addText(47009, "Sacrifice your box for a chance at something rare!", tda, 1, 0xFFA500, true, true);
		addSprite(47010, 528, "Interfaces/MysteryBox/SPRITE");
		addSprite(47011, 533, "Interfaces/MysteryBox/SPRITE");


		setChildren(13, iface);
		setBounds(47001, 10, 10, 0, iface);
		setBounds(47002, 253, 13, 1, iface);
		setBounds(47003, 473, 14, 2, iface);
		setBounds(47004, 218, 256, 3, iface);
		setBounds(47005, 253, 2630, 4, iface);
		setBounds(47006, 17, 185, 5, iface);
		setBounds(47007, 33, 65, 6, iface);
		setBounds(47008, 253, 78, 7, iface);
		setBounds(47009, 253, 108, 8, iface);
		// Boxes
		setBounds(47200, 10, 187, 9, iface);
		// Items
		setBounds(47100, 17, 192, 10, iface);
		// Item selector
		setBounds(47010, 252, 187, 11, iface);
		setBounds(47011, 10, 185, 12, iface);

		/* Boxes */
		RSInterface box = addInterface(47200);
		box.width = 480;
		setChildren(Client.BOXES64, box);
		// 64 boxes in each sprite
		int x = 0;
		for(int i=0; i<Client.BOXES64; i++){
			//addSpriteLoader(47201, 1076);
			addSprite(47201, 532, "Interfaces/MysteryBox/SPRITE");
			//addSprite(47201, 0, "");
			setBounds(47201, 0 + x, 0, i, box);
			x += 2880;
		}

		/* Items */
		RSInterface scroll = addInterface(47100);
		scroll.width = 474;
		addToItemGroup(47101, 1750, 1, 13, 10, false, null, null, null);
		setChildren(1, scroll);
		setBounds(47101, 0, 0, 0, scroll);
	}
	public static void genieLamp() {
		RSInterface tab = addInterface(80000);

		addButton(80001, 122 , "Interfaces/genielamp/sprite", "Construction");
		addSprite(80002, 2035, "Interfaces/genielamp/SPRITE");

		addButton(80003, 122 , "Interfaces/genielamp/sprite", "Hunter");
		addSprite(80004, 2034, "Interfaces/genielamp/SPRITE");


		//addButton(80001, 2034, "Select");
		//addButton(80002, 2035, "Select");



		tab.totalChildren(5);
		tab.child(0, 2808, 0,0);//the original genie interface
		tab.child(1, 80001, 370,175);//the background sprite
		tab.child(2, 80002, 370,175);//construction
		tab.child(3, 80003, 370,212);//the background sprite
		tab.child(4, 80004, 370,212);//hunter


	}
	public static void wrathRune() {
		RSInterface rune = addTabInterface(28226);
		rune.totalChildren(1);
		addSprite(28228, 0, "Magic/wrath");
		setBounds(28228, 0, 0, 0, rune);
	}
	public static void arceeusspellbook(TextDrawingArea[] tda){
		RSInterface rune = addTabInterface(23100);
		rune.totalChildren(7);
//		addSpellSmall2_3(80101, 563, 566, 555, 554, 2, 2, 4, 5, 30012, 30015, 30004, 30003, 1, "Home Teleport,
//				"Teleports you home", tda, 10, 7, 5);
//		addSpellLarge2(13674 + 975, 563, 560, 562, 1, 1, 1, 30012, 30009, 30011, 84, "Teleport to Bounty\\nTarget",
//				"Teleports you near your Bounty\\nHunter Target", tda, 8, 7, 5);
//
//		addSpellSmall2(22674 + 975, 565, 566, 564, 20, 20, 1, 30014, 30015, 30013, 92, "Lvl-7 Enchant",
//				"For use on zenyte jewellery", tda, 12, 16, 2); // 16 spell useable on is for magic on inventory item

//		drawRune(30003, 1, "Fire");
//		drawRune(30004, 2, "Water");
//		drawRune(30005, 3, "Air");
//		drawRune(30006, 4, "Earth");
//		drawRune(30007, 5, "Mind");
//		drawRune(30008, 6, "Body");
//		drawRune(30009, 7, "Death");
//		drawRune(30010, 8, "Nature");
//		drawRune(30011, 9, "Chaos");
//		drawRune(30012, 10, "Law");
//		drawRune(30013, 11, "Cosmic");
//		drawRune(30014, 12, "Blood");
//		drawRune(30015, 13, "Soul");
//		drawRune(30016, 14, "Astral");
		addSprite(84099, 0, "Magic/bg");
		addSpellwithoutrunes(84101, 0, "Home Teleport", 	"Teleports you home",tda,  100, 7,5);//was 10,2


		addSpellLarge2Runes(84110, 557, 563, 563, 2, 1, 1, 30006, 30012, 30012, 84, "Teleport to Arceeus\\nLibrary",
				"Teleports the player north of\\nthe Arceeus Library", tda, 110, 7, 5);



		addSpellSmaller(84120, 559, 561, 4, 2, 30008, 30010, 15, "Basic Reanimation",
				"Reanimates a weak level monster.", tda, 120, 16, 2);


		setBounds(84099, 5,1100, 0, rune);
		setBounds(84101, 4, 10, 1, rune);
		setBounds(84102, 10, 25, 2, rune);//the hover


		setBounds(84110, 44, 10, 3, rune);
		setBounds(84111, 10, 25, 4, rune);//the hover
		setBounds(84120, 84, 10, 5, rune);

		setBounds(84121, 10, 25, 6, rune);
	}
	public static void normals(TextDrawingArea[] tda) {
		RSInterface p = addTabInterface(938);
		RSInterface rsinterface = interfaceCache[1151];
		RSInterface rsinterface2 = interfaceCache[12424];
		rsinterface2.height = 250;

			// earth wave
			rsinterface2.childX[36] = 96;
			rsinterface2.childY[36] = 168;
			
			// enfeeble
			rsinterface2.childX[46] = 120;
			rsinterface2.childY[46] = 168;
			
			// teleother lumbridge
			rsinterface2.childX[53] = 144;
			rsinterface2.childY[53] = 168;
			
			// fire wave
			rsinterface2.childX[37] = 1;
			rsinterface2.childY[37] = 192;
			
			// entangle
			rsinterface2.childX[50] = 23;
			rsinterface2.childY[50] = 192;
			
			// stun
			rsinterface2.childX[47] = 47;
			rsinterface2.childY[47] = 193;
			
			// charge
			rsinterface2.childX[41] = 71;
			rsinterface2.childY[41] = 192;
			
			// teleother falador
			rsinterface2.childX[54] = 120;
			rsinterface2.childY[54] = 192;
			
			// teleblock
			rsinterface2.childX[55] = 0;
			rsinterface2.childY[55] = 218;
			
			// lvl-6 enchant
			rsinterface2.childX[57] = 47;
			rsinterface2.childY[57] = 218;
			
			// teleother camelot
			rsinterface2.childX[56] = 71;
			rsinterface2.childY[56] = 218;

		rsinterface.childY[1] = 12;
		rsinterface.childX[1] = 14;
		addSpellSmall2_3(31674 + 975, 563, 566, 555, 554, 2, 2, 4, 5, 30012, 30015, 30004, 30003, 68, "Teleport to Kourend",
				"Teleports you to Kourend", tda, 10, 7, 5);
		addSpellLarge2(13674 + 975, 563, 560, 562, 1, 1, 1, 30012, 30009, 30011, 84, "Teleport to Bounty\\nTarget",
				"Teleports you near your Bounty\\nHunter Target", tda, 8, 7, 5);

		addSpellSmall2(22674 + 975, 565, 566, 564, 20, 20, 1, 30014, 30015, 30013, 92, "Lvl-7 Enchant",
				"For use on zenyte jewellery", tda, 12, 16, 2); // 16 spell useable on is for magic on inventory item

		addSpellSmaller(22644 + 975, 556, 21880, 7, 1, 30005, 28226, 80, "Wind Surge",
				"A very high level Air missile", tda, 0, 2, 2);//was 10,2
		
		addSpellSmall(22658 + 975, 555, 556, 21880, 10, 7, 1, 30004, 30005, 28226, 84, "Water Surge",
				"A very high level Water missile", tda, 2, 2, 2);
		
		 addSpellSmall(22628 + 975, 557, 556, 21880, 10, 7, 1, 30006, 30005, 28226, 89, "Earth Surge",
				"A very high level Earth missile", tda, 4, 2, 2);
		
		addSpellSmall(22608 + 975, 554, 556, 21880, 10, 7, 1, 30003, 30005, 28226, 94, "Fire Surge",
				"A very high level Fire missile", tda, 6, 2, 2);

		setChildren(15, p);
		setBounds(31674 + 975, 84, 178, 0, p);
		setBounds(13674 + 975, 35, 228, 1, p);
		setBounds(22674 + 975, 132, 227, 2, p);
		setBounds(22644 + 975, 108, 202, 3, p);
		setBounds(22658 + 975, 156, 202, 4, p);
		setBounds(22628 + 975, 108, 227, 5, p);
		setBounds(22608 + 975, 156, 227, 6, p);
		setBounds(1151, 0, 0, 7, p);
		setBounds(22609 + 975, 5, 5, 8, p);
		setBounds(22629 + 975, 5, 5, 9, p);
		setBounds(22659 + 975, 5, 5, 10, p);
		setBounds(22645 + 975, 5, 5, 11, p);
		setBounds(31675 + 975, 5, 5, 12, p);
		setBounds(13675 + 975, 5, 5, 13, p);
		setBounds(22675 + 975, 5, 5, 14, p);
	}

	public static void closeButton(int id, int sprite2, int sprite1) {
		RSInterface tab = addInterface(id);
		tab.atActionType = 0;
		tab.type = 0;
		tab.sprite2 = Client.cacheSprite3[sprite2];
		tab.sprite1 = Client.cacheSprite3[sprite1];
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite2.myHeight;
		// tab.toggled = false;
		// tab.spriteOpacity = 255;d
	}

	public static void createSkillHover(int id, int x) {
		RSInterface hover = addInterface(id);
		hover.inventoryhover = false;
		hover.type = 8;
		hover.message = "TESTING!" + x;
		hover.contentType = x;
		hover.width = 60;
		hover.height = 32;
	}

	public static void mainPanelButtons(int button) {
		switch (button) {
			case 80100:
			case 80101:
			case 80102:
				mainpanelswitchsettings(button);
				break;
		}
	}
	public static void mainpanelswitchsettings(int button) {
		int tab =  (button - 80100);
		int[] tabs = new int[] {80130,80150,80200};
		interfaceCache[51500].children[4] = tabs[tab];
	}
	public static void membershipbenefits(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(113_000);
		addSprite(113_001, 0, "/Interfaces/membershipbenefits/SPRITE");
	//	closeButtonSmall(113_002);//return button
		hoverButton_fromfile(113_002, "Info", 4539,4540, "", RSInterface.newFonts[0], 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");

		setChildren(14, tab);
		setBounds(113_001, 9,60, 0, tab);
		setBounds(113_002, 15,-2000, 1, tab);


		setBounds(113_200, 10,1000, 2, tab);//lets use this as the arrow lol
		setBounds(113_201, 30,1000, 3, tab);
		setBounds(113_202, 50,1000, 4, tab);
		setBounds(113_203, 70,1000, 5, tab);
		setBounds(113_204, 90,1000, 6, tab);


		setBounds(113_004, 13,63,7, tab);//the scroll


		setBounds(113_205, 10+15,40, 8, tab);
		setBounds(113_206, 40+15,40, 9, tab);
		setBounds(113_207, 70+15,40, 10, tab);
		setBounds(113_208, 100+15,40, 11, tab);
		setBounds(113_209, 130+15,40, 12, tab);


		setBounds(113_210, 95,4, 13, tab);
		//	REGULAR_DONATOR(5, "1B1ABC"),
		//	EXTREME_DONOR(7, "118120", REGULAR_DONATOR),
		//	LEGENDARY_DONATOR(9, "6D0000", EXTREME_DONOR),
		//	DIAMOND_CLUB(17, "005C6D", LEGENDARY_DONATOR),
		//	ONYX_CLUB(18, "4a4a4a", DIAMOND_CLUB),

	//	hoverButton(113_200, "Regular", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		addSprite(113_200, 202, "/Interfaces/membershipbenefits/SPRITE");

		hoverButton(113_201, "Extreme", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_202, "Legendary", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_203, "Diamond", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_204, "Onyx", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");


//		addSprite(113_205, 11, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_206, 12, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_207, 13, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_208, 4, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_209, 15, "/Interfaces/membershipbenefits/SPRITE");



		hoverButton(113_205, "Regular", 11,11, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_206, "Extreme", 12,12, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_207, "Legendary", 13,13, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_208, "Diamond", 4,4, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton(113_209, "Onyx", 15,15, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");


		addText(113_210, "Regular Donator", tda, 2, 0xFE9624, true, true);
		RSInterface scroll = addInterface(113_004);

		scroll.width = 151;
		scroll.height = 150;
		scroll.scrollMax = 326;
		scroll.totalChildren(40);
				int y=0;
		for(int i = 0; i < 20; i++) {
			addSprite(113_005+i, i % 2 == 0 ? 2100 : 2101, "/Interfaces/membershipbenefits/SPRITE");

			scroll.child(i, 113_005+i, 0, y);
			y+=22;
		}
		y=4;
		for(int i = 0; i < 20; i++) {
			addText(113_100+i, "Increased to 8 gear slots.", tda, 0, 0xFE9624, false, true);

			scroll.child(20+i, 113_100+i, 3, y);
			y+=22;
		}



	}

	public static void upgradenow(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(114_000);
//		addSprite(113_001, 0, "/Interfaces/membershipbenefits/SPRITE");
//		closeButtonSmall(113_002);

		setChildren(10, tab);
		setBounds(114_001, 75,170-10, 0, tab);//bottom sprite
		setBounds(114_002, 75,30-10, 1, tab);//top sprite

		setBounds(114_003, 87,68-10, 2, tab);//the sprite bar empty
		setBounds(114_004, 87,68-10, 3, tab);//the sprite bar filling

		setBounds(114_005, 85,170, 4, tab);//reach from sprite
		setBounds(114_006, 85,38-10, 5, tab);//reach to sprite
		setBounds(114_007, 65,204, 6, tab);//coin icon
		setBounds(114_008, 165,0, 7, tab);//question mark icon
		setBounds(114_009,35,2, 8, tab);//next rank words
		setBounds(114_010, 83,203,9, tab);//total donated words

		addSprite(114_001, 101, "/Interfaces/membershipbenefits/SPRITE");
		addSprite(114_002, 100, "/Interfaces/membershipbenefits/SPRITE");
		addSprite(114_003, 102, "/Interfaces/membershipbenefits/SPRITE");
		addSprite(114_004, 103, "/Interfaces/membershipbenefits/SPRITE");

		addClickableSprites(114_005, "Rank", "Interfaces/membershipbenefits/SPRITE",104,1100,1200,1300,1400,1500);//reach from
		addClickableSprites(114_006, "Rank", "Interfaces/membershipbenefits/SPRITE",104,1100,1200,1300,1400,1500);//reach to


		addSprite(114_007, 106, "/Interfaces/membershipbenefits/SPRITE");
		hoverButton_fromfile(114_008, "Info", 2521,2522, "", RSInterface.newFonts[0], 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
		addText(114_009, "Next rank:", tda, 2, 0xFE9624, false, true);
		addText(114_010, "Total donated: 0 / 25", tda, 1, 0xFE9624, false, true);
	//	addClickableSprites(114_001, "Rank", "Interfaces/membershipbenefits/SPRITE",100,11,12,13,4,15);//reach to

		//addClickableSprites(114_002, "Rank", "Interfaces/membershipbenefits/SPRITE",101,11,12,13,4,15);//reach from
//
//
//		setBounds(113_200, 10,0, 2, tab);
//		setBounds(113_201, 30,0, 3, tab);
//		setBounds(113_202, 50,0, 4, tab);
//		setBounds(113_203, 70,0, 5, tab);
//		setBounds(113_204, 90,0, 6, tab);
//
//
//		setBounds(113_004, 14,26,7, tab);
//		setBounds(113_205, 10,0, 8, tab);
//		setBounds(113_206, 30,0, 9, tab);
//		setBounds(113_207, 50,0, 10, tab);
//		setBounds(113_208, 70,0, 11, tab);
//		setBounds(113_209, 90,0, 12, tab);
		//	REGULAR_DONATOR(5, "1B1ABC"),
		//	EXTREME_DONOR(7, "118120", REGULAR_DONATOR),
		//	LEGENDARY_DONATOR(9, "6D0000", EXTREME_DONOR),
		//	DIAMOND_CLUB(17, "005C6D", LEGENDARY_DONATOR),
		//	ONYX_CLUB(18, "4a4a4a", DIAMOND_CLUB),

//		hoverButton(113_200, "Regular", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
//		hoverButton(113_201, "Extreme", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
//		hoverButton(113_202, "Legendary", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
//		hoverButton(113_203, "Diamond", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
//		hoverButton(113_204, "Onyx", 2,8, "",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/membershipbenefits/SPRITE");
//
//
//		addSprite(113_205, 11, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_206, 12, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_207, 13, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_208, 4, "/Interfaces/membershipbenefits/SPRITE");
//		addSprite(113_209, 15, "/Interfaces/membershipbenefits/SPRITE");



//		RSInterface scroll = addInterface(113_004);
//
//		scroll.width = 155;
//		scroll.height = 184;
//		scroll.scrollMax = 326;
//		scroll.totalChildren(40);
//		int y=0;
//		for(int i = 0; i < 20; i++) {
//			addSprite(113_005+i, i % 2 == 0 ? 2100 : 2101, "/Interfaces/membershipbenefits/SPRITE");
//
//			scroll.child(i, 113_005+i, 0, y);
//			y+=22;
//		}
//		y=3;
//		for(int i = 0; i < 20; i++) {
//			addText(113_100+i, "Increased to 8 gear slots.", tda, 0, 0xFE9624, false, true);
//
//			scroll.child(20+i, 113_100+i, 3, y);
//			y+=22;
//		}



	}
	public static void mailbox(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(111_000);
		addSprite(111_001, 0, "/Interfaces/mailbox/SPRITE");
		addText(111_002, "Mailbox", tda, 2, 0xFE9624, true, true);
		closeButtonSmall(111_003);

		addSprite(111_406, 2149, "/Interfaces/mailbox/SPRITE");
		addSprite(111_407, 2149, "/Interfaces/mailbox/SPRITE");
		//int itemScrollId = nextChildId;
		RSInterface scroll = addInterface(111_004);
	//	tab.child(4, 111_003, 232, 114);


		scroll.width = 172;
		scroll.height = 212;
		scroll.scrollMax = 326;
		scroll.totalChildren(100);
		int x = 150;
		int y = 30;
		setChildren(6, tab);

		setBounds(111_001, 0+x,0+y, 0, tab);
		setBounds(111_002,112+x,14+y, 1, tab);
		setBounds(111_003, 197+x,10+y, 2,tab);
		setBounds(111_004, 17+x,38+y,3,tab);

		setBounds(111_406, 61+x,14+y,4,tab);
		setBounds(111_407, 150+x,14+y,5,tab);
y=2;
		for(int i = 0; i < 20; i++) {
			hoverButton_fromfile(111_005+i, "Claim", i % 2 == 0 ?1 : 2,i % 2 == 0 ?1 : 2, "", RSInterface.newFonts[0], 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/mailbox/SPRITE");//20

			itemGroup(21614+i, 1, 1, 1, 1, false, true);
			addSprite(111_100+i, i % 2 == 0 ? 6 : 7, "/Interfaces/mailbox/SPRITE");
			addText(111_200+i, "From: Runescape", tda, 0, 0xFE9624, false, true);
			addText(111_300+i, "Thanks for donating!", tda, 0, 0xFFFFFF, false, true);
		//	addText(111_002, "Mailbox", tda, 2, 0xFE9624, true, true);
			scroll.child(i, 111_005+i, 0, y);
			scroll.child(20+i, 21614+i, 0+2, y+2);
			scroll.child(40+i, 111_100+i, 0+38, y);
			scroll.child(60+i, 111_200+i, 0+40, y+3);
			scroll.child(80+i, 111_300+i, 0+40, y+12);
			y+=41;
		}



	}
	public static void mainPanel(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(80130);

		setChildren(10, tab);
		addText(80112, "Account", tda, 2, 0xFE9624, true, true);
		addText(80113, "Membership: none", tda, 1, 0xFE9624, true, true);
		addText(80114, "Name:", tda, 2, 0xFE9624, true, true);
		addText(80125, "Inbox: 0 unread messages", tda, 1, 0xFE9624, false, true);


//		addSprite(80104, 2046, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80105, 2047, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80106, 2048, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80107, 2049, "/Interfaces/playerpanel/SPRITE");

//		addHoverText(80108, "Upgrade Now", "Upgrade Now", tda, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
//		addHoverText(80109, "View Benefits", "View Benefits", tda, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
//		addHoverText(80110, "View Inbox", "View Inbox", tda, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
//		addHoverText(80111, "Name Changer", "Name Changer", tda, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
		hoverButton(80104, "Upgrade Now", 2046,2046, "Upgrade Now",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80105, "View Benefits", 2047,2047, "View Benefits",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80106, "View Inbox", 2048,2048, "View Inbox",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80107, "Statistics", 2049,2049, "Statistics",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80197, "Change Name", 2060,2060, "Change Name",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		// addText(32581, "Collections\\nLogged:", tda, 0,0xFF981F, true, true);


		setBounds(80104, 30,45, 0, tab);
		setBounds(80105, 30,80, 1, tab);
		setBounds(80106, 30,135, 2, tab);
		setBounds(80107, 30,195, 3, tab);

		setBounds(80112, 95,0, 4, tab);
		setBounds(80113, 100,20, 5, tab);
		setBounds(80114, 100,165, 6, tab);
		setBounds(80125, 23,113, 7, tab);
		setBounds(82021, 0,0,8, tab);
		setBounds(80197,  30,165,9, tab);
	}


	public static void profileTab(TextDrawingArea[] fonts) {
		RSInterface tab = addInterface(51500);

		//these buttons do not change when you click a panel
//		configHoverButton(80100, "Select",  2045,2045,2044,2044, false, new int[] { 80101,80102 });
//		configHoverButton(80101, "Select",  2045,2045,2044,2044, false, new int[] { 80100,80102});
//		configHoverButton(80102, "Select", 2045,2045,2044,2044,false, new int[] { 80100,80101 });
		String dir = "/Interfaces/playerpanel/SPRITE";
		configHoverButton1(80100, "Select", dir, 2045,2045,2044,2044, false, new int[] { 80101,80102});
		configHoverButton1(80101, "Select", dir, 2045,2045,2044,2044, false, new int[] {  80100,80102});
		configHoverButton1(80102, "Select", dir, 2045,2045,2044,2044, false, new int[] { 80100,80101});


		addSprite(80120, 2050, "/Interfaces/playerpanel/SPRITE");
		addSprite(80121, 2051, "/Interfaces/playerpanel/SPRITE");
		addSprite(80122, 2052, "/Interfaces/playerpanel/SPRITE");

		addSprite(80103, 2037, "/Interfaces/playerpanel/SPRITE");


		tab.totalChildren(8);


		tab.child(0, 80103, 0,29);//the line
		tab.child(1, 80100, 4,4);
		tab.child(2, 80101, 67,4);
		tab.child(3, 80102, 130,4);


		tab.child(4, 80130, 0,35);//the child that changes

		tab.child(5, 80120, 27,8);
		tab.child(6, 80121, 86,8);
		tab.child(7, 80122, 150,8);

		RSInterface secondpanel = addInterface(80150);
		secondPanel(secondpanel,fonts);
		RSInterface thirdpanel = addInterface(80200);
		thirdPanel(thirdpanel,fonts);


	}
//	public static void secondPanel(RSInterface tab, TextDrawingArea[] font) {
//
//		setChildren(10, tab);
//		addText(80151, "Community", font, 2, 0xFE9624, true, true);
//		addText(80152, "Current poll: @red@Inactive", font, 1, 0xFE9624, false, true);
//		addText(80153, "Latest Update", font, 1, 0xFE9624, true, true);
//
//
//		addSprite(80154, 2054, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80155, 2055, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80156, 2056, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80157, 2057, "/Interfaces/playerpanel/SPRITE");
//
//		addHoverText(80158, "View History", "View History", font, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
//		addHoverText(80159, "View Newspost", "View Newspost", font, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
//		addHoverText(80160, "View Archive", "View Archive", font, 0, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
//
//
//
//		setBounds(80151, 95,0, 0, tab);
//		setBounds(80152,40,25, 1, tab);
//		setBounds(80153, 95,130, 2, tab);
//		setBounds(80154, 30,50, 3, tab);
//
//		setBounds(80155, 30,80, 4, tab);
//		setBounds(80156, 30,150, 5, tab);
//		setBounds(80157, 30,185, 6, tab);
//		setBounds(80158, 23,87, 7, tab);
//
//		setBounds(80159, 23,158, 8, tab);
//		setBounds(80160,23,193, 9, tab);
//
//	}
public static void secondPanel(RSInterface tab, TextDrawingArea[] font) {

	setChildren(8, tab);
	addText(80151, "Community", font, 2, 0xFE9624, true, true);
	addText(80152, "Current poll: @red@Inactive", font, 1, 0xFE9624, false, true);
	addText(80153, "Latest Update", font, 1, 0xFE9624, true, true);


	//addSprite(80154, 2054);
	hoverButton(80154, "View Poll", 2059,2059, "View Poll",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
	hoverButton(80155, "View History", 2055,2055, "View History",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
	hoverButton(80156, "View Newspost", 2056,2056, "View Newspost",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
	hoverButton(80157, "View Archive", 2057,2057, "View Archive",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
	addSprite(80159, 2054, "/Interfaces/playerpanel/SPRITE");


	setBounds(80151, 95,0, 0, tab);
	setBounds(80152,40,25, 1, tab);
	setBounds(80153, 95,130, 2, tab);
	setBounds(80159, 30,50, 3, tab);
	setBounds(80154, 30,50, 4, tab);

	setBounds(80155, 30,80, 5, tab);
	setBounds(80156, 30,150, 6, tab);
	setBounds(80157, 30,185, 7, tab);
}
	public static void thirdPanel(RSInterface tab, TextDrawingArea[] font) {


		setChildren(8, tab);
		addText(80201, "Useful Links", font, 2, 0xFE9624, true, true);

//		addSprite(80202, 2058, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80203, 2058, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80204, 2058, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80205, 2058, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80206, 2058, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80207, 2058, "/Interfaces/playerpanel/SPRITE");
//		addSprite(80202, 2058);
//		addSprite(80203, 2058);
//		addSprite(80204, 2058);
//		addSprite(80205, 2058);
//		addSprite(80206, 2058);
//		addSprite(80207, 2058);
		//addHoverText(TASK_BUTTON, "Task", "Task", tda, 0, 0xFF9900, true, true, 148, 0xC0C0C0);


		hoverButton(80203, "Donate", 2058,2058, "Donate",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80204, "Discord", 2058,2058, "Discord",0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80205, "Hiscores", 2058,2058, "Hiscores", 0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton(80206, "Wiki", 2058,2058, "Wiki",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		//overButton(80213, "Vote", 2058,2058, "Vote",  0, 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");

		hoverButton_fromfile(80202, "Website", 2058,2058, "Website", RSInterface.newFonts[0], 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");

        hoverButton_fromfile(80213, "Vote", 2058,2058, "Vote", RSInterface.newFonts[0], 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");
		hoverButton_fromfile(89213, "Boosts", 2058,2058, "Boosts", RSInterface.newFonts[0], 0xFFFFFF, 0xc0c0c0, true, "/Interfaces/playerpanel/SPRITE");



		setBounds(80201, 95,0, 0, tab);
		setBounds(80202,5,25, 1, tab);
		setBounds(80203, 100,25, 2, tab);
		setBounds(80204, 5,60, 3, tab);

		setBounds(80205, 100,60, 4, tab);
		setBounds(80206, 5,95, 5, tab);
	//	setBounds(80207, 10000,95, 6, tab);
//		setBounds(80208,  -27,30, 7, tab);
//
//		setBounds(80209, 67,30, 8, tab);
//		setBounds(80210, -27,64, 9, tab);
//		setBounds(80211, 67,68, 10, tab);
//		setBounds(80212, -27,103, 11, tab);
		setBounds(80213, 100,95, 6, tab);
		setBounds(89213, 5,130, 7, tab);
	}
	public static void statisticspanel(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(82021);

		setChildren(5, tab);

		darken(82022, 190, 228, 0x000000, (byte) 100);//ok so you need to make sure this is the ENTIER w/h of whatever the fk u are putting ON TOP OF IT.
		addSprite(82023, 2455, "/Interfaces/playerpanel/SPRITE");
		closeButtonSmall(82024);
		addText(82028, "Statistics", tda, 2, 0xFE9624, true, true);
		RSInterface scroll = addTabInterface(82025);

		scroll.width = 118;
		scroll.height = 77;
		scroll.scrollMax = 150;
		scroll.totalChildren(18);//for now? or just max atm ?
//donator
		//pkp
		//vote
		//slayer
		//boss points

			int y = 0;
		for(int i = 0; i < 6; i++) {
			addSprite(82030+i, 3000+i, "/Interfaces/playerpanel/SPRITE");
			//addText(82030+i, "Slayer Points", tda, 0, 0xFE9624, true, true);
			scroll.child(i, 82030+i, 10, y);
			y+=22;
		}
		 y = 0;
		for(int i = 0; i < 6; i++) {
			//addSprite(82030+i, i % 2 == 0 ? 2100 : 2101, "/Interfaces/membershipbenefits/SPRITE");
			addText(82040+i, "i", tda, 0, 0xFFFFFF, false, true);
			scroll.child(i+6, 82040+i, 30, y);
			y+=22;
		}		y=3;
		String text = "";
		for(int i = 0; i < 6; i++) {
			text = i == 0 ? " Donator points" : i == 1 ? "Pk Points" : i == 2 ? "Vote points" : i == 3 ? "Slayer points" : i == 4 ? "Boss Points" :  "PC Points";
			//addSprite(82030+i, i % 2 == 0 ? 2100 : 2101, "/Interfaces/membershipbenefits/SPRITE");
			addPrayerHover_curse(tda, 1, 82080+i,
					text, 30,14,0,0);//the 38,38 and others dont matter only last 2 number matter
			scroll.child(i+12, 82080+i, 0, y);
			y+=22;
		}
		setBounds(82022, 0,-4, 0, tab);//darken
		setBounds(82023,23,45, 1, tab);//the interface
		setBounds(82024, 144,52, 2, tab);//close button
		setBounds(82028, 94,52, 3, tab);//statistics text
		setBounds(82025, 25,77, 4, tab);//scroll

	}


	public static void OSRSLogout(TextDrawingArea[] fonts) {

		RSInterface logout = interfaceCache[2449];
		logout.totalChildren(14);
		setBounds(2455, 3 + 20, 210, 5, logout);
		setBounds(2454, 27 + 20, 210, 4, logout);
		setBounds(2453, 53 + 20, 210, 3, logout);
		setBounds(2456, 83 + 20, 210, 6, logout);
		setBounds(2457, 111 + 20, 210, 7, logout);
		addText(2450, "Did you enjoy playing", fonts, 1, 0xFF9300, true, true);
		setBounds(2450, 95, 14, 0, logout);
		addText(2451, "Runescape today?", fonts, 1, 0xFF9300, true, true);
		setBounds(2451, 94, 32, 1, logout);
		hoverButton(2458, "Click here to logout", 2, 2, "Click here to logout",  2, 0xFFFFFF, 0xFF0000, true, "/Interfaces/BUTTONS/LOGOUT");
		//addHoverText(2458, "Click here to logout", "Logout", fonts, 2, 0xF8F0DD, true, true, 148, 36, 0xFF0000);
		setBounds(2458, 25,212, 8, logout);

		addSprite(2452, 805,"/Interfaces/logouttab/sprite");
		setBounds(2452, 25, 168, 2, logout);

		addHoverText(12454, "World Switcher", "World Switcher", fonts, 2, 0xF8F0DD, true, true, 148, 36, 0xC0C0C0);
		setBounds(12454, 21, 176, 9, logout);

		addText(12495, "Use the buttons below to", fonts, 1, 0xFF9300, true, true);
		setBounds(12495, 94, 121, 10, logout);

		addText(12456, "logout or switch worlds safely.", fonts, 1, 0xFF9300, true, true);
		setBounds(12456, 94, 139, 11, logout);

//		configHoverButtonSpriteOutline(4141, "Thumbs Up", 806, 809, 807, false, 4, 15, 0, 4142);
//		configHoverButtonSpriteOutline(4142, "Thumbs Down", 806, 809, 808, false, 4, 15, 0, 4141);
		String dir = "/Interfaces/logouttab/SPRITE";
		configHoverButton1(41411, "Thumbs Up", dir, 806, 806,8060,8060, false, new int[] { 41421});
		configHoverButton1(41421, "Thumbs Down", dir, 810, 810,809,809, false, new int[] { 41411});
		//addText(4141, "", fonts, 1, 0xFF9300, true, true);
		setBounds(41411, 28, 62, 12, logout);
		setBounds(41421, 100, 62, 13, logout);


	}

public static void modepanelButtons(int button) {
	switch (button) {
		case 42880:
		case 42881:
			modepanelswitchsettings(button);
			break;
	}
}
	public static void modepanelswitchsettings(int button) {
		int tab =  (button - 42880);
		int[] tabs = new int[] {42882,42883};
		interfaceCache[42400].children[2] = tabs[tab];
	}

	    private static void ironman_widget_new(TextDrawingArea[] font) {
        RSInterface widget = addInterface(42400);
		String dir = "starter_interface/SPRITE";
        addSprite(42444, 1766,dir);
        addSprite(42401, 1766,dir);


		configHoverButton1(42880, "Select", dir, 2082,2082,2081,2081,  false, new int[] { 42881});
		configHoverButton1(42881, "Select", dir, 2087,2087,2086,2086,  false, new int[] { 42880});


//			configHoverButton(42880, "Select",  2082,2082,2081,2081, false, new int[] { 42881 });
//        configHoverButton(42881, "Select",  2087,2087,2086,2086, false, new int[] { 42880});


		addText(42895, "Solo Modes", font, 2, 0xFF981F, true, true);

        addText(42896, "Group Modes", font, 2, 0xFF981F, true, true);
   //     hoverButton10(42419,"Confirm", 1767, 1768, "", font, 1, 0xdb9c22,0xdb9c22,true);

			hoverButton_fromfile(42419, "confirm", 1767, 1768, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);

        setChildren(8, widget);
        widget.child(1, 42444, 1400,25);
        widget.child(0, 42401, 14,25);
        widget.child(2, 42882, 0,35);//the child that changes
        widget.child(3, 42880, 22,74);
        widget.child(4, 42881, 125,74);
        widget.child(5, 42895, 70,77);
        widget.child(6, 42896, 174,77);
        widget.child(7, 42419, 330,265);
		RSInterface secondpanel = addInterface(42883);
        group_selection_panel(secondpanel,font);

    }
	    private static Sprite button = new Sprite("starter_interface/button");
    private static Sprite buttonSelected = new Sprite("starter_interface/button_selected");
	    private static void regular_selection_panel(TextDrawingArea[] font){
			RSInterface widget = addTabInterface(42882);

        setChildren(9, widget);
			String dir = "starter_interface/SPRITE";
        addSprite(42488, 2083,dir);
//        addClickableSprites(42423, "Toggle", 490, 491, 547);
//        addClickableSprites(42402, "Toggle", 490, 491, 547);
//        addClickableSprites(42403, "Toggle", 490, 491, 547);
//        addClickableSprites(42405, "Toggle", 490, 491, 547);

			addConfigButton(42423, button, buttonSelected, "standard", 0, Configs.GAME_MODE_SETUP_SELECTION, 4);
            get(42423).ignoreConfigClicking = true;

			addConfigButton(42402, button, buttonSelected, "ironman", 2, Configs.GAME_MODE_SETUP_SELECTION, 4);
			get(42402).ignoreConfigClicking = true;

			addConfigButton(42403, button, buttonSelected, "hcim", 3, Configs.GAME_MODE_SETUP_SELECTION, 4);
			get(42403).ignoreConfigClicking = true;

			addConfigButton(42405, button, buttonSelected, "uim", 4, Configs.GAME_MODE_SETUP_SELECTION, 4);
			get(42405).ignoreConfigClicking = true;

			addText(80_300, "None", font, 0, 0xffffff, false, true);
			addText(80_301, "Standard Ironman", font, 0, 0xffffff, false, true);
			addText(80_302, "Hardcore Ironman", font, 0, 0xffffff, false, true);
			addText(80_303, "Ultimate Ironman", font, 0, 0xffffff, false, true);

        setBounds(42488, 22, 60, 0, widget);
        setBounds(42402, 27, 98, 1, widget);
        setBounds(42403, 27, 140, 2, widget);
        setBounds(42423, 27, 66, 3, widget);

        setBounds(42405, 27, 185, 4, widget);

			setBounds(80_300, 47,60, 5, widget);
			setBounds(80_301, 47, 91, 6, widget);
			setBounds(80_302, 47, 130, 7, widget);
			setBounds(80_303, 47, 178, 8, widget);

    }
	//interface id: 42883
	private static void group_selection_panel(RSInterface tab,TextDrawingArea[] font){
		setChildren(4, tab);
		String dir = "starter_interface/SPRITE";
		addSprite(42884, 2084,dir);
		addText(42885, "Group Ironman", font, 0, 0xffffff, true, true);
		addText(42890, "Regular Ironman...but in a group!", font, 0, 0xFF9300, true, true);


		addConfigButton(42889, button, buttonSelected, "uim", 6, Configs.GAME_MODE_SETUP_SELECTION, 4);
		get(42889).ignoreConfigClicking = true;



		setBounds(42884, 22,60, 0, tab);
		setBounds(42885, 90,70, 1, tab);
		setBounds(42889, 30,70, 2, tab);
		setBounds(42890,140,81, 3, tab);
	}
private static final String[] names = { "Attack", "Hitpoints", "Mining", "Strength", "Agility", "Smithing", "Defence",
		"Herblore", "Fishing", "Ranged", "Thieving", "Cooking", "Prayer", "Crafting", "Firemaking", "Magic",
		"Fletching", "Woodcutting", "Runecraft", "Slayer", "Farming", "Construction", "Hunter" };

	private static int correctPositions1(int id) {
		switch (id) {
			case 0:
				return 3;
			case 3:
				return 3;
			case 2:
				return 5;
			case 4:
				return 4;
			case 5:
				return 5;
			case 6:
			case 21:
			case 22:
				return 3;
			case 10:
			case 17:
				return 5;
			case 11:
				return 6;
			case 13:
			case 14:
			case 16:
				return 5;
		}
		return 4;
	}

	private static void skillsTab(TextDrawingArea[] font) {
		int[] firstRow = { -1,

				/** First column  (enx. index 9) **/
				14918, 14919, 14920, 14921, 14922, 14923, 14924, 14925

		};
		int[] secondRow = {

				14926, 14927, 14928, 14929, 14930, 14931, 14932, 14933

		};
		int[] thirdRow = {


				14934, 14935, 14936, 14937, 14938, 14939, 14940

		};
		RSInterface widget = addTabInterface(13917);//10000

		int skillTabChild = 0;
		int x = 1;
		int y = 1;
		widget.totalChildren(119);

		for (int i = 0; i < 24; i++) {
			if (i <= 22) {
				//addButton(10001 + i, 10000, 61, 32, 775, 775, 10151 + i, "View <col=ff9040>" + names[i] + " <col=ffffff>guide");

				addButton(10001 + i, 775 , "Interfaces/skillsprites/sprite", "View <col=ff9040>" + names[i] + " <col=ffffff>guide");
			//	addSprite(10031 + i, 777 + i);
				addSprite(10031 + i, 777 + i, "/Interfaces/skillsprites/sprite");

			} else if (i >= 23) {
				//addSprite(10001 + i, 776);
				addSprite(10001 + i, 776, "/Interfaces/skillsprites/sprite");

				addText(71120, "Total level:", font, 0, 0xffff00, true, true);
				addText(71121, "", font, 0, 0xffff00, true, true);
			}
			if (x < 180) {
				widget.child(skillTabChild++, 10001 + i, x, y);
				if (i < 23) {
					widget.child(skillTabChild++, 10031 + i, x + correctPositions1(i), y + 4);
				}
			} else {
				x = 1;
				y += 32;
				widget.child(skillTabChild++, 10001 + i, x, y);
				if (i < 23) {
					widget.child(skillTabChild++, 10031 + i, x + correctPositions1(i), y + 4);
				}
			}
			x += 63;
		}
		widget.child(skillTabChild++, 71120, 158, 231);
		widget.child(skillTabChild++, 71121, 158, 241);
		widget.child(skillTabChild++, 4004, 33, 5);
		widget.child(skillTabChild++, 4005, 45, 17);
		widget.child(skillTabChild++, 4006, 33, 5 + 32);
		widget.child(skillTabChild++, 4007, 45, 17 + 32);
		widget.child(skillTabChild++, 4008, 33, 5 + 64);
		widget.child(skillTabChild++, 4009, 45, 17 + 64);
		widget.child(skillTabChild++, 4010, 33, 5 + 96);
		widget.child(skillTabChild++, 4011, 45, 17 + 96);
		widget.child(skillTabChild++, 4012, 33, 5 + 128);
		widget.child(skillTabChild++, 4013, 45, 17 + 128);
		widget.child(skillTabChild++, 4014, 33, 5 + 160);
		widget.child(skillTabChild++, 4015, 45, 17 + 160);
		widget.child(skillTabChild++, 4016, 96, 5);
		widget.child(skillTabChild++, 4017, 108, 17);
		widget.child(skillTabChild++, 4018, 96, 5 + 32);
		widget.child(skillTabChild++, 4019, 108, 17 + 32);
		widget.child(skillTabChild++, 4020, 96, 5 + 64);
		widget.child(skillTabChild++, 4021, 108, 17 + 64);
		widget.child(skillTabChild++, 4022, 96, 5 + 96);
		widget.child(skillTabChild++, 4023, 108, 17 + 96);
		widget.child(skillTabChild++, 4024, 96, 5 + 128);
		widget.child(skillTabChild++, 4025, 108, 17 + 128);
		widget.child(skillTabChild++, 4026, 96, 5 + 160);
		widget.child(skillTabChild++, 4027, 108, 17 + 160);
		widget.child(skillTabChild++, 4028, 159, 5);
		widget.child(skillTabChild++, 4029, 171, 17);
		widget.child(skillTabChild++, 4030, 159, 5 + 32);
		widget.child(skillTabChild++, 4031, 171, 17 + 32);
		widget.child(skillTabChild++, 4032, 159, 5 + 64);
		widget.child(skillTabChild++, 4033, 171, 17 + 64);
		widget.child(skillTabChild++, 4034, 159, 5 + 96);
		widget.child(skillTabChild++, 4035, 171, 17 + 96);
		widget.child(skillTabChild++, 4036, 159, 5 + 128);
		widget.child(skillTabChild++, 4037, 171, 17 + 128);
		widget.child(skillTabChild++, 4038, 159, 5 + 160);
		widget.child(skillTabChild++, 4039, 171, 17 + 160);
		widget.child(skillTabChild++, 4152, 33, 5 + 192);
		widget.child(skillTabChild++, 4153, 45, 17 + 192);
		widget.child(skillTabChild++, 12166, 96, 5 + 192);
		widget.child(skillTabChild++, 12167, 108, 17 + 192);
		widget.child(skillTabChild++, 13926, 159, 5 + 192);
		widget.child(skillTabChild++, 13927, 171, 17 + 192);
		widget.child(skillTabChild++, 18799, 33, 5 + 224);
		widget.child(skillTabChild++, 18800, 45, 17 + 224);
		widget.child(skillTabChild++, 18797, 96, 5 + 224);
		widget.child(skillTabChild++, 18798, 108, 17 + 224);
		for (int i = 1; i < firstRow.length; i++) {//length is 8
			createSkillHover(firstRow[i], 205 + i);
			setBounds(firstRow[i], 0, 2 + (32 * i) - 33, skillTabChild++, widget);
		}
		for (int i = 0; i < secondRow.length; i++) {
			createSkillHover(secondRow[i], 214 + i);
			setBounds(secondRow[i], 64, 2 + (32 * i) - 1, skillTabChild++, widget);
		}
		for (int i = 0; i < thirdRow.length; i++) {
			createSkillHover(thirdRow[i], 223 + i);
			setBounds(thirdRow[i], 127, 2 + (32 * i) - 1, skillTabChild++, widget);
		}
		createSkillHover(14941, 831);
		setBounds(14941, 127, 225, skillTabChild++, widget);
	}


	/**
	 * @author Grant_ | www.rune-server.ee/members/grant | 3/22/2020
	 */
	public static void votePanel(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(24127);
		int childId = 24128;

		addSprite(childId++, 0, "Interfaces/VotePanel/BACKGROUND");
		addHoverButton(childId++, "Interfaces/CollectionLog/CLOSE", 0, 16, 16, "Close", -1, childId, 1);
		addHoveredButton(childId++, "Interfaces/CollectionLog/CLOSE", 1, 16, 16, childId++);

		addText(childId++, "Day Streak Points:", tda, 0, 0xFF9300, false, true);
		addText(childId++, "6", tda, 0, 0xFFFFFF, true, true);
		addText(childId++, "1", tda, 0, 0xFFFFFF, true, true);

		addText(childId++, "Vote Panel", tda, 2, 0xFF9300, true, true);

		addText(childId++, "", tda, 0, 0xFF9300, true, true);

		addText(childId++, "Current Day Streak: @whi@4", tda, 2, 0xFF9300, true, true);

		addSprites(childId++, "Interfaces/VotePanel/PIPE_UP", 0, 1, 2);
		addSprites(childId++, "Interfaces/VotePanel/PIPE_DOWN", 0, 1, 2);
		addSprites(childId++, "Interfaces/VotePanel/PIPE_UP", 0, 1, 2);
		addSprites(childId++, "Interfaces/VotePanel/PIPE_DOWN", 0, 1, 2);

		addSprites(childId++, "Interfaces/VotePanel/PENTA", 0, 1);
		addSprites(childId++, "Interfaces/VotePanel/PENTA", 0, 1);
		addSprites(childId++, "Interfaces/VotePanel/PENTA", 0, 1);
		addSprites(childId++, "Interfaces/VotePanel/PENTA", 0, 1);
		addSprites(childId++, "Interfaces/VotePanel/PENTA", 0, 1);

		//Top pentagon labels
		int[] days = {4, 8};
		String[] circleTexts = {"+3", "+5"};
		for(int i = 0; i < 2; i++) {
			addSprite(childId++, 0, "Interfaces/VotePanel/CIRCLE");
			addText(childId++, "@gre@Day " + days[i], tda, 0, 0xFF9300, true, true);
			addText(childId++, circleTexts[i], tda, 0, 0xFFFFFF, true, true);
		}

		//Bottom pentagon labels
		days = new int[]{2, 6};
		circleTexts = new String[]{"+2", "+4"};
		for(int i = 0; i < 2; i++) {
			addSprite(childId++, 0, "Interfaces/VotePanel/CIRCLE");
			addText(childId++, "@gre@Day " + days[i], tda, 0, 0xFF9300, true, true);
			addText(childId++, circleTexts[i], tda, 0, 0xFFFFFF, true, true);
		}

		//Custom last pentagon
		addText(childId++, "@gre@Day 10", tda, 0, 0xFFFFFF, true, true);
		addSprite(childId++, 1, "Interfaces/VotePanel/CIRCLE");
		addText(childId++, "+1", tda, 0, 0xFFFFFF, true, true);
		addSprite(childId++, 0, "Interfaces/VotePanel/BOX");
		addSprite(childId++, 1, "Interfaces/VotePanel/BOX");

		String[] buttonText = {"30 Min. Bonus XP", "10% DR Boost 1HR", "Vote Crystal", "Ultra M. Box"};
		int[] amounts = {2, 2, 1, 5};
		for(int i = 0; i < 4; i++) {
			addHoverButton(childId++, "Interfaces/VotePanel/BUTTON", 0, 139, 28, buttonText[i], -1, childId, 1);
			addHoveredButton(childId++, "Interfaces/VotePanel/BUTTON", 1, 139, 28, childId++);
			//System.out.println("ID: " + childId);
			addText(childId++, buttonText[i], tda, 0, 0xFF9933, true, true);
			addSprite(childId++, i == 3 ? 1 : 0, "Interfaces/VotePanel/CIRCLE");
			addText(childId++, amounts[i] + "", tda, 0, 0xFFFFFF, true, true);
		}

		addSprite(childId++, 2, "Interfaces/VotePanel/BOX");
		addText(childId++, "Point Store", tda, 0, 0xFF9933, true, true);

		addText(childId++, "Top 3 Weekly Voters:", tda, 2, 0xFF9300, true, true);

		String[] names = {"1. Billy [12]", "2. Grant [7]", "3. Noah [3]"};
		for(int i = 0; i < 3; i++) {
			addText(childId++, names[i], tda, 0, 0xFF9300, false, true);
			addText(childId++, "Prize:", tda, 0, 0xFF9300, false, true);
		}

		addHoverButton(childId++, "Interfaces/VotePanel/BUTTON", 2, 103, 26, "Claim Prize", -1, childId, 1);
		addHoveredButton(childId++, "Interfaces/VotePanel/BUTTON", 3, 103, 26, childId++);

		addText(childId++, "Claim Prize", tda, 2, 0xFF9300, true, true);
		addText(childId++, "Top 3 Voters reset in:\\n4 days, 3 hrs, 2 min", tda, 0, 0xFFFFFF, true, true);

		addToItemGroup(childId++, 1, 3, 0, 0, false);

		widget.totalChildren(childId - 24128 - 6);
		childId = 24128;
		int frame = 0;

		widget.child(frame++, childId++, 15, 30); //Background

		widget.child(frame++, childId++, 476, 39); //Close button
		widget.child(frame++, childId++, 476, 39);
		childId++;

		widget.child(frame++, childId++, 30, 42); //Day streak title
		widget.child(frame++, childId++, 139, 42); //Blue count
		widget.child(frame++, childId++, 160, 42); //Red count

		widget.child(frame++, childId++, 267, 40); //Title

		widget.child(frame++, childId++, 410, 42); //Vote Key

		widget.child(frame++, childId++, 163, 70); //Current Day streak

		int startX = 55;
		int startY = 125;
		for(int i = 0; i < 4; i++) {
			widget.child(frame++, childId++, startX, startY); //Pipe
			startX += 60;
			if (i == 1) {
				startX = 148;
			}
		}

		widget.child(frame++, childId++, 47, 164); //Pentagon
		widget.child(frame++, childId++, 95, 102); //Pentagon
		widget.child(frame++, childId++, 140, 164); //Pentagon
		widget.child(frame++, childId++, 185, 102); //Pentagon
		widget.child(frame++, childId++, 234, 164); //Pentagon

		startX = 115;
		startY = 89;
		//Top labels
		for(int i = 0; i < 2; i++) {
			widget.child(frame++, childId++, startX - 7, startY + 25);
			widget.child(frame++, childId++, startX, startY);
			widget.child(frame++, childId++, startX, startY + 28);

			startX += 90;
		}

		startX = 68;
		startY = 209;
		//Bottom labels
		for(int i = 0; i < 2; i++) {
			widget.child(frame++, childId++, startX - 8, startY - 32);
			widget.child(frame++, childId++, startX, startY);
			widget.child(frame++, childId++, startX, startY - 28);

			startX += 92;
		}

		//Custom last label
		widget.child(frame++, childId++, 254, 209);
		widget.child(frame++, childId++, 240, 173);
		widget.child(frame++, childId++, 247, 176);
		widget.child(frame++, childId++, 249, 184);
		widget.child(frame++, childId++, 255, 168);

		startX = 22;
		startY = 240;
		for(int i = 0; i < 4; i++) {
			widget.child(frame++, childId++, startX, startY);
			widget.child(frame++, childId++, startX, startY);
			childId++;

			widget.child(frame++, childId++, startX + 68, startY + 8);
			widget.child(frame++, childId++, startX + 117, startY + 4);
			widget.child(frame++, childId++, startX + 126, startY + 8);

			startX += 139;
			if (i == 1) {
				startX = 22;
				startY += 28;
			}
		}

		widget.child(frame++, childId++, 169, 272); //M box icon
		widget.child(frame++, childId++, 163, 227); //Point store text

		widget.child(frame++, childId++, 400, 70);
		startX = 314;
		startY = 103;
		for(int i = 0; i < 3; i++) {
			widget.child(frame++, childId++, startX, startY);
			widget.child(frame++, childId++, startX + 100, startY);
			startY += 30;
		}

		widget.child(frame++, childId++, 347, 209);
		widget.child(frame++, childId++, 347, 209);
		childId++;

		widget.child(frame++, childId++, 400, 215);
		widget.child(frame++, childId++, 400, 252);

		widget.child(frame++, childId++, 449, 93); //Items
	}

	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/7/19
	 * @param tda
	 */
	public static void collectionLog(TextDrawingArea[] tda) {
		//Config = 519
		RSInterface widget = addInterface(23110);
		int childId = 23111;

		addSprite(childId++, 0, "Interfaces/CollectionLog/BACKGROUND");//23112

		addText(childId++, "Collection Log", tda, 2, 0xFF9300, true, true);//23113

		addHoverButton(childId++, "Interfaces/CollectionLog/CLOSE", 0, 16, 16, "Close", -1, childId, 1);//23114
		addHoveredButton(childId++, "Interfaces/CollectionLog/CLOSE", 1, 16, 16, childId++);
		closeButtonSmall(10601);

		addConfigButton(childId++, 618, 0, 1, "Interfaces/CollectionLog/TAB", 96, 20, "Select Bosses", 1, 1, 519);
		addText(childId++, "Bosses", tda, 1, 0xFF9300, true, true);

		addText(childId++, "Boss Name Here", tda, 2, 0xFF9300, false, true);
		addText(childId++, "Obtained: @red@10/11", tda, 0, 0xFF9300, false, true);
		addText(childId++, "Boss Name count: @whi@125", tda, 0, 0xFF9300, true, true);

		RSInterface tableView = addInterface(childId++);
		int scrollChildId = childId;
		int scrollFrame = 0;
		tableView.width = 187;
		tableView.height = 246;
		tableView.scrollMax = 750;
		tableView.totalChildren(50 * 2);

		int scrollX = 0;
		int scrollY = 0;
		for(int i = 0; i < 50; i++) {
			if (i % 2 == 0) {
				addConfigButton(scrollChildId, 23122, 0, 1, "Interfaces/CollectionLog/CELL", 187, 15, "Select Boss", 1, 1, 520 + i);
			} else {
				addConfigButton(scrollChildId, 23122, 2, 1, "Interfaces/CollectionLog/CELL", 187, 15, "Select Boss", 1, 1, 520 + i);
			}
			tableView.child(scrollFrame++, scrollChildId++, scrollX, scrollY);

			addText(scrollChildId, "Boss Name Here", tda, 1, 0xFF9300, false, true);
			tableView.child(scrollFrame++, scrollChildId++, scrollX + 4, scrollY);

			scrollY += 15;
		}

		widget.totalChildren(childId - 23111 - 1 + (8) + 2);
		childId = 23111;
		int frame = 0;

		widget.child(frame++, childId++, 9, 11);//Background
		widget.child(frame++, childId++, 257, 20);//Title

		widget.child(frame++, childId++, 482, 206);
		widget.child(frame++, childId++, 482, 20);
		widget.child(frame++, 10601, 482, 20);
		childId++;

		widget.child(frame++, childId++, 19, 47);
		widget.child(frame++, childId++, 60, 50);

		widget.child(frame++, childId++, 230, 70);
		widget.child(frame++, childId++, 230, 94);
		widget.child(frame++, childId++, 433, 94);

		widget.child(frame++, childId++, 20, 68);

		//Had to add these on
		int nextChildId = scrollChildId;
		addConfigButton(nextChildId, 23111, 0, 1, "Interfaces/CollectionLog/TAB", 96, 20, "Select Wilderness", 1, 1, 571);
		widget.child(frame++, nextChildId++, 19 + 96, 47);
		addText(nextChildId, "Wilderness", tda, 1, 0xFF9300, true, true);
		widget.child(frame++, nextChildId++, 60 + 96, 50);

		addConfigButton(nextChildId, 23111, 0, 1, "Interfaces/CollectionLog/TAB", 96, 20, "Select Raids", 1, 1, 572);
		widget.child(frame++, nextChildId++, 19 + 96 + 96, 47);
		addText(nextChildId, "Raids", tda, 1, 0xFF9300, true, true);
		widget.child(frame++, nextChildId++, 60 + 96 + 96, 50);

		addConfigButton(nextChildId, 23111, 0, 1, "Interfaces/CollectionLog/TAB", 96, 20, "Select Minigames", 1, 1, 573);
		widget.child(frame++, nextChildId++, 19 + 96 + 96 + 96, 47);
		addText(nextChildId, "Minigames", tda, 1, 0xFF9300, true, true);
		widget.child(frame++, nextChildId++, 60 + 96 + 96 + 96, 50);

		addConfigButton(nextChildId, 23111, 0, 1, "Interfaces/CollectionLog/TAB", 96, 20, "Select Clue Scroll", 1, 1, 574);
		widget.child(frame++, nextChildId++, 19 + 96 + 96 + 96 + 96, 47);
		addText(nextChildId, "Other", tda, 1, 0xFF9300, true, true);
		widget.child(frame++, nextChildId++, 60 + 96 + 96 + 96 + 96, 50);

		int itemScrollId = nextChildId;
		RSInterface scroll = addInterface(itemScrollId);
		widget.child(frame++, itemScrollId++, 232, 114);
		int itemScrollFrame = 0;
		scroll.width = 250;
		scroll.height = 200;
		scroll.scrollMax = 2000;
		scroll.totalChildren(1);

		addToItemGroup(itemScrollId, 6, 33, 9, 6, false, null, null, null);
		scroll.child(itemScrollFrame++, itemScrollId++, 0, 0);
	}
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/6/19
	 * @param tda
	 */
	public static void tourneyJoinInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(270);
		int childId = 271;
		
		addSprite(childId++, 0, "Interfaces/Tourney/BACKGROUND");
		addText(childId++,
				"\\n" +
						"\\n" +
						"\\n" +
						"Outlast is a safe PvP tournament.\\n" +
				"\\n" +
				"Earn weapon and armour upgrades by killing targets. \\n" +
				"Once the 3 minute timer runs out, the fog damage will begin.\\n" +
				"Good luck!", tda, 2, 0xFF9300, true, true);
		
		addHoverButton(childId++, "Interfaces/Tourney/BUTTON", 0, 136, 32, "Fight", -1, childId, 1);
		addHoveredButton(childId++, "Interfaces/Tourney/BUTTON", 0, 135, 32, childId++);

		addHoverButton(childId++, "Interfaces/Tourney/BUTTON", 0, 136, 32, "Exit", -1, childId, 1);
		addHoveredButton(childId++, "Interfaces/Tourney/BUTTON", 0, 135, 32, childId++);
		
		addText(childId++, "Fight", tda, 2, 0x00FF00, true, true);
		addText(childId++, "Exit", tda, 2, 0xFF0000, true, true);
		
		widget.totalChildren(childId - 271 - 2);
		childId = 271;
		int frame = 0;
		
		widget.child(frame++, childId++, 11, 11);
		widget.child(frame++, childId++, 257, 100);
		
		widget.child(frame++, childId++, 100, 255);
		widget.child(frame++, childId++, 100, 255);
		childId++;
		
		widget.child(frame++, childId++, 285, 255);
		widget.child(frame++, childId++, 285, 255);
		childId++;
		
		widget.child(frame++, childId++, 167, 263);
		widget.child(frame++, childId++, 352, 263);
	}
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 10/4/19
	 * @param tda
	 */
	public static void tournamentInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(264);
		int childId = 265;
		
		addText(childId++, "Outlast", tda, 2, 0xFFFFFF, true, true);
		addText(childId++, "Time Left: 5 min", tda, 1, 0xFFFFFF, true, true);
		addText(childId++, "Current Players", tda, 1, 0xFFFFFF, true, true);
		
		widget.totalChildren(childId - 265);
		childId = 265;
		int frame = 0;
		widget.child(frame++, childId++, 251, 5);
		widget.child(frame++, childId++, 251, 17);
		widget.child(frame++, childId++, 251, 29);
	}
	
	/**
	 * @author Grant_ | www.rune-server.ee/members/grant_ | 9/29/19
	 * @param tda
	 */
//	public static void presetInterface(TextDrawingArea[] tda) {
//		RSInterface widget = addInterface(21553);
//		int childId = 21554;
//
//		addSprite(childId++, 0, "Presets/BACKGROUND");
//
//		for(int i = 0; i < 4; i++) {
//			addConfigButton(childId++, 21553, 0, 1, "Presets/BUTTON", 118, 22, "Select Preset", 1, 1, 899 + i);
//			addText(childId++, "Default " + i, tda, 1, 0xFF9300, false, true);
//		}
//
//		addText(childId++, "Pre-made Presets", tda, 1, 0xFFFFFF, true, true);
//		addText(childId++, "Customs", tda, 1, 0xFFFFFF, true, true);
//		addText(childId++, "Current Set", tda, 1, 0xFFFFFF, true, true);
//
//		addHoverButton(childId++, "Presets/BUTTON", 2, 81, 28, "Load", -1, childId, 1);
//		addHoveredButton(childId++, "Presets/BUTTON", 2, 81, 28, childId++);
//
//		addHoverButton(childId++, "Presets/BUTTON", 2, 81, 28, "Save", -1, childId, 1);
//		addHoveredButton(childId++, "Presets/BUTTON", 2, 81, 28, childId++);
//
//		addText(childId++, "Load", tda, 2, 0xFF9300, true, true);
//		addText(childId++, "Save", tda, 2, 0xFF9300, true, true);
//
//		addText(childId++, Configuration.CLIENT_TITLE + " Presets", tda, 2, 0xFF9300, true, true);
//
//		addHoverButton(childId++, "/Interfaces/Exp Lock/SPRITE", 1, 16, 16, "Close", -1, childId, 1);
//		addHoveredButton(childId++, "/Interfaces/Exp Lock/SPRITE", 2, 16, 16, childId++);
//
//		addToItemGroup(childId++, 4, 7, 6, 6, true, "Remove", null, null);//21572
//
//		/*
//		 * Note: Using a modified childId to avoid overridden child members
//		 */
//		int overlayModificationChildId = 569;
//		int overlayModificationConfig = 61;
//		for(int i = 0; i < 11; i++) {
//			addConfigButton(overlayModificationChildId++, 21553, 1, 0, "Presets/OVERLAY", 36, 36, new String[] {}, 1, overlayModificationConfig + i);
//		}
//
//		for(int i = 0; i < 11; i++) {
//			addToItemGroup(childId++, 1, 1, 0, 0, true, "Remove", null, null);
//		}
//
//		RSInterface scroll = addInterface(childId++);
//
//		int scrollChildId = childId;
//		int scrollFrame = 0;
//		scroll.width = 100;
//		scroll.height = 140;
//		scroll.scrollMax = 220;
//		scroll.totalChildren(30);
//		int scrollX = 0;
//		int scrollY = 0;
//
//		for(int i = 0; i < 10; i++) {
//			addConfigButton(scrollChildId, 21553, 0, 1, "Presets/BUTTON", 118, 22, new String[] { "Delete Preset", "Select Preset" }, 1, 903 + i);
//			scroll.child(scrollFrame++, scrollChildId++, scrollX, scrollY);
//
//			addText(scrollChildId, "Preset Name " + i, tda, 1, 0xFF9300, false, true);
//			scroll.child(scrollFrame++, scrollChildId++, scrollX + 1, scrollY + 3);
//
//			scrollY += 22;
//		}
//
//		childId = scrollChildId;
//		addHoverButton(childId++, "Presets/EDIT", 0, 14, 12, "Edit Preset Name", -1, childId, 1);
//		addHoveredButton(childId++, "Presets/EDIT", 0, 14, 12, childId++);
//
//		int additionChildId = 251;
//		addHoverButton(additionChildId++, "Presets/BUTTON", 3, 35, 35, "Switch Spellbook", -1, additionChildId, 1);
//		addHoveredButton(additionChildId++, "Presets/BUTTON", 3, 35, 35, additionChildId++);
//		addSprites(additionChildId++, "Presets/BOOK", new int[]{0, 1, 2});
//
//		widget.totalChildren(childId - 21554 + 4 + (11) + (3));
//
//		overlayModificationChildId = 569;
//		childId = 21554;
//		int frame = 0;
//
//		widget.child(frame++, childId++, 11, 11);
//
//		int startX = 1900;
//		int startY = 61;
//		for(int i = 0; i < 4; i++) {
//			widget.child(frame++, childId++, startX, startY);
//
//			widget.child(frame++, childId++, startX + 1, startY + 3);
//
//			startY += 22;
//		}
//
//		widget.child(frame++, childId++, 75, 4500);//default presets
//		widget.child(frame++, childId++, 75, 1510);
//		widget.child(frame++, childId++, 228, 450);
//
//		widget.child(frame++, childId++, 143, 278);
//		widget.child(frame++, childId++, 143, 278);
//		childId++;
//
//		widget.child(frame++, childId++, 230, 278);
//		widget.child(frame++, childId++, 230, 278);
//		childId++;
//
//		widget.child(frame++, childId++, 182, 284);
//		widget.child(frame++, childId++, 270, 284);
//		widget.child(frame++, childId++, 247, 21);
//
//		widget.child(frame++, childId++, 474, 20);
//		widget.child(frame++, childId++, 474, 20);
//		childId++;
//
//		widget.child(frame++, childId++, 326, 48);
//
//		//Equipment Overlays
//		widget.child(frame++, overlayModificationChildId++, 211, 68);
//		widget.child(frame++, overlayModificationChildId++, 170, 107);
//		widget.child(frame++, overlayModificationChildId++, 211, 107);
//		widget.child(frame++, overlayModificationChildId++, 252, 107);
//		widget.child(frame++, overlayModificationChildId++, 211, 146);
//		widget.child(frame++, overlayModificationChildId++, 155, 146);
//		widget.child(frame++, overlayModificationChildId++, 267, 146);
//		widget.child(frame++, overlayModificationChildId++, 211, 186);
//		widget.child(frame++, overlayModificationChildId++, 211, 226);
//		widget.child(frame++, overlayModificationChildId++, 155, 226);
//		widget.child(frame++, overlayModificationChildId++, 267, 226);
//
//		//Equipment
//		widget.child(frame++, childId++, 214, 70);
//		widget.child(frame++, childId++, 172, 109);
//		widget.child(frame++, childId++, 214, 109);
//		widget.child(frame++, childId++, 255, 109);
//		widget.child(frame++, childId++, 214, 148);
//		widget.child(frame++, childId++, 158, 148);
//		widget.child(frame++, childId++, 270, 148);
//		widget.child(frame++, childId++, 214, 188);
//		widget.child(frame++, childId++, 214, 228);
//		widget.child(frame++, childId++, 158, 228);
//		widget.child(frame++, childId++, 270, 228);
//		System.out.println("id: "+frame);
//		widget.child(frame++, childId++, 22, 60);
//	//	System.out.println("id: "+frame);
//		childId = scrollChildId;
//
//		widget.child(frame++, childId++, 301, 46);
//		widget.child(frame++, childId++, 301, 46);
//
//		additionChildId = 251;
//		//button
//		widget.child(frame++, additionChildId++, 276, 64);
//		widget.child(frame++, additionChildId++, 276, 64);
//		additionChildId++;
//		//icon
//		widget.child(frame++, additionChildId++, 281, 70);
//	}
	public static void presetInterface(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(21553);
		int childId = 21554;

		addSprite(childId++, 0, "Presets/BACKGROUND");

		for(int i = 0; i < 4; i++) {
			addConfigButton(childId++, 21553, 0, 1, "Presets/BUTTON", 118, 22, "Select Preset", 1, 1, 899 + i);
			addText(childId++, "Default " + i, tda, 1, 0xFF9300, false, true);
		}

		addText(childId++, "Pre-made Presets", tda, 1, 0xFFFFFF, true, true);
		addText(childId++, "Customs", tda, 1, 0xFFFFFF, true, true);
		addText(childId++, "Current Set", tda, 1, 0xFFFFFF, true, true);

		addHoverButton(childId++, "Presets/BUTTON", 2, 81, 28, "Load", -1, childId, 1);
		addHoveredButton(childId++, "Presets/BUTTON", 2, 81, 28, childId++);

		addHoverButton(childId++, "Presets/BUTTON", 2, 81, 28, "Save", -1, childId, 1);
		addHoveredButton(childId++, "Presets/BUTTON", 2, 81, 28, childId++);

		addText(childId++, "Load", tda, 2, 0xFF9300, true, true);
		addText(childId++, "Save", tda, 2, 0xFF9300, true, true);

		addText(childId++, Configuration.CLIENT_TITLE + " Presets", tda, 2, 0xFF9300, true, true);

		addHoverButton(childId++, "/Interfaces/Exp Lock/SPRITE", 1, 16, 16, "Close", -1, childId, 1);
		addHoveredButton(childId++, "/Interfaces/Exp Lock/SPRITE", 2, 16, 16, childId++);

		addToItemGroup(childId++, 4, 7, 6, 6, true, "Remove", null, null);

		/*
		 * Note: Using a modified childId to avoid overridden child members
		 */
		int overlayModificationChildId = 569;
		int overlayModificationConfig = 61;
		for(int i = 0; i < 11; i++) {
			addConfigButton(overlayModificationChildId++, 21553, 1, 0, "Presets/OVERLAY", 36, 36, new String[] {}, 1, overlayModificationConfig + i);
		}

		for(int i = 0; i < 11; i++) {
			addToItemGroup(childId++, 1, 1, 0, 0, true, "Remove", null, null);
		}

		RSInterface scroll = addInterface(childId++);
		int scrollChildId = childId;
		int scrollFrame = 0;
		scroll.width = 100;
		scroll.height = 140;
		scroll.scrollMax = 220;
		scroll.totalChildren(30);
		int scrollX = 0;
		int scrollY = 0;

		for(int i = 0; i < 10; i++) {
			addConfigButton(scrollChildId, 21553, 0, 1, "Presets/BUTTON", 118, 22, new String[] { "Delete Preset", "Select Preset" }, 1, 903 + i);
			scroll.child(scrollFrame++, scrollChildId++, scrollX, scrollY);

			addText(scrollChildId, "Preset Name " + i, tda, 1, 0xFF9300, false, true);
			scroll.child(scrollFrame++, scrollChildId++, scrollX + 1, scrollY + 3);

			scrollY += 22;
		}

		childId = scrollChildId;
		addHoverButton(childId++, "Presets/EDIT", 0, 14, 12, "Edit Preset Name", -1, childId, 1);
		addHoveredButton(childId++, "Presets/EDIT", 0, 14, 12, childId++);

		int additionChildId = 251;
		addHoverButton(additionChildId++, "Presets/BUTTON", 3, 35, 35, "Switch Spellbook", -1, additionChildId, 1);
		addHoveredButton(additionChildId++, "Presets/BUTTON", 3, 35, 35, additionChildId++);
		addSprites(additionChildId++, "Presets/BOOK", new int[]{0, 1, 2});

		widget.totalChildren(childId - 21554 + 4 + (11) + (3));

		overlayModificationChildId = 569;
		childId = 21554;
		int frame = 0;

		widget.child(frame++, childId++, 11, 11);

		int startX = 19;
		int startY = 61;
		for(int i = 0; i < 4; i++) {
			widget.child(frame++, childId++, startX, startY);

			widget.child(frame++, childId++, startX + 1, startY + 3);

			startY += 22;
		}

		widget.child(frame++, childId++, 75, 45);
		widget.child(frame++, childId++, 75, 151);
		widget.child(frame++, childId++, 228, 45);

		widget.child(frame++, childId++, 143, 278);
		widget.child(frame++, childId++, 143, 278);
		childId++;

		widget.child(frame++, childId++, 230, 278);
		widget.child(frame++, childId++, 230, 278);
		childId++;

		widget.child(frame++, childId++, 182, 284);
		widget.child(frame++, childId++, 270, 284);
		widget.child(frame++, childId++, 247, 21);

		widget.child(frame++, childId++, 474, 20);
		widget.child(frame++, childId++, 474, 20);
		childId++;

		widget.child(frame++, childId++, 326, 48);

		//Equipment Overlays
		widget.child(frame++, overlayModificationChildId++, 211, 68);
		widget.child(frame++, overlayModificationChildId++, 170, 107);
		widget.child(frame++, overlayModificationChildId++, 211, 107);
		widget.child(frame++, overlayModificationChildId++, 252, 107);
		widget.child(frame++, overlayModificationChildId++, 211, 146);
		widget.child(frame++, overlayModificationChildId++, 155, 146);
		widget.child(frame++, overlayModificationChildId++, 267, 146);
		widget.child(frame++, overlayModificationChildId++, 211, 186);
		widget.child(frame++, overlayModificationChildId++, 211, 226);
		widget.child(frame++, overlayModificationChildId++, 155, 226);
		widget.child(frame++, overlayModificationChildId++, 267, 226);

		//Equipment
		widget.child(frame++, childId++, 214, 70);
		widget.child(frame++, childId++, 172, 109);
		widget.child(frame++, childId++, 214, 109);
		widget.child(frame++, childId++, 255, 109);
		widget.child(frame++, childId++, 214, 148);
		widget.child(frame++, childId++, 158, 148);
		widget.child(frame++, childId++, 270, 148);
		widget.child(frame++, childId++, 214, 188);
		widget.child(frame++, childId++, 214, 228);
		widget.child(frame++, childId++, 158, 228);
		widget.child(frame++, childId++, 270, 228);

		widget.child(frame++, childId++, 19, 168);

		childId = scrollChildId;

		widget.child(frame++, childId++, 301, 46);
		widget.child(frame++, childId++, 301, 46);

		additionChildId = 251;
		//button
		widget.child(frame++, additionChildId++, 276, 64);
		widget.child(frame++, additionChildId++, 276, 64);
		additionChildId++;
		//icon
		widget.child(frame++, additionChildId++, 281, 70);
	}
	public static void notiTab(TextDrawingArea[] tda){
		RSInterface Interface = addTabInterface(31_000);
        String button_dir = "/interfaces/buttons/SPRITE";
		Sprite BG = new Sprite("/notifications_tab/bg");


		addSprite(100_001, BG);
        addText(100_002, "Broadcasts", tda, 0, 0xFF9300, false, true);
		addConfigButton(100_003, 100_000, 1212, 1213, button_dir, 17, 17, "Toggle", 0, 1, Configs.NOTIFICATION_BROADCAST);
		closeButtonSmall(100_004);


		Interface.totalChildren(4);
		Interface.child2(0, 100_001, 11,18);
		Interface.child2(1, 100_002, 40,50);
		Interface.child2(2, 100_003, 125,46);
		Interface.child2(3, 100_004, 157,24);
    }
	public static void equipmentScreen(TextDrawingArea[] wid) {
		RSInterface Interface = RSInterface.interfaceCache[1644];
		addButton(19144, 140, "Show Equipment Stats");
		removeSomething(19145);
		removeSomething(19146);
		removeSomething(19147);
		setBounds(19145, 40, 210, 24, Interface);
		setBounds(19146, 40, 210, 25, Interface);
		setBounds(19147, 40, 210, 26, Interface);



		RSInterface tab = addTabInterface(15106);
	String dir = "/equipment/sprite";
		addSprite(15107, 116, dir);

		//addSprite3(15107, 116);
		closeButtonLarge(15210);


		addText(15111, "Equip Your Character...", wid, 2, 0xFF9300, false, true);
		addText(15112, "Attack bonus", wid, 2, 0xFF9300, false, true);
		addText(15113, "Defence bonus", wid, 2, 0xFF9300, false, true);
		addText(15114, "Other bonuses", wid, 2, 0xFF9300, false, true);

		addText(21451, "Ranged Strength:", wid, 1, 0xFF9300, false, true);
		addText(21452, "Magic Damage:", wid, 1, 0xFF9300, false, true);
		addText(1686, "Melee Strength:", wid, 1, 0xFF9300, false, true);
		addText(15122, "0 kg", wid, 1, 0xff981f, false, true);
		addText(16117, "ranged strength", wid, 1, 0xFF9300, false, true);

		for (int i = 1675; i <= 1684; i++) {
			textSize(i, wid, 1);
		}
		textSize(1686, wid, 1);
		textSize(1687, wid, 1);

	addChar(15125);
		tab.totalChildren(51);
		tab.child2(0, 15107, 5, 5);
		tab.child2(1, 15210, 477, 12);
		tab.child2(2, 15211, 477, 1200);
		tab.child2(3, 15111, 15, 15);
		int Child = 4;
		int Y = 69;
		int xOff = 332;
		int xOff2 = 323;
		int yOff = 7;
		int yOff2 = 8;
		for (int i = 1675; i <= 1679; i++) {
			tab.child2(Child, i, 20 + xOff, Y - yOff2);
			Child++;
			Y += 14;
		}

		tab.child2(9, 1680, 20 + xOff, 161 - yOff2);
		tab.child2(10, 1681, 20 + xOff, 177 - yOff2);
		tab.child2(11, 1682, 20 + xOff, 192 - yOff2);
		tab.child2(12, 1683, 20 + xOff, 207 - yOff2);
		tab.child2(13, 1684, 20 + xOff, 221 - yOff2);
		tab.child2(14, 1686, 20 + xOff, 262 - yOff2);
		tab.child2(15, 4883, 192, 2100);//oh you fucker. thats why npc head was on it lmfao
		tab.child2(16, 15112, 16 + xOff, 55 - yOff2);
		tab.child2(18, 15113, 16 + xOff, 147 - yOff2);
		tab.child2(19, 15114, 16 + xOff, 248 - yOff2);
		tab.child2(20, 1645, 104 + 295 - xOff2, 149 - 52 + yOff);
		tab.child2(21, 1646, 399 - xOff2, 163 + yOff);
		tab.child2(22, 1647, 399 - xOff2, 163 + yOff);
		tab.child2(23, 1648, 399 - xOff2, 58 + 146 + yOff);
		tab.child2(24, 1649, 26 + 22 + 297 - xOff2 - 2, 110 - 44 + 118 - 13 + 5 + yOff);
		tab.child2(25, 1650, 321 - xOff2 + 22, 58 + 154 + yOff);
		tab.child2(26, 1651, 321 - xOff2 + 134, 58 + 118 + yOff);
		tab.child2(27, 1652, 321 - xOff2 + 134, 58 + 154 + yOff);
		tab.child2(28, 1653, 321 - xOff2 + 48, 58 + 81 + yOff);
		tab.child2(29, 1654, 321 - xOff2 + 107, 58 + 81 + yOff);
		tab.child2(30, 1655, 321 - xOff2 + 58, 58 + 42 + yOff);
		tab.child2(31, 1656, 321 - xOff2 + 112, 58 + 41 + yOff);
		tab.child2(32, 1657, 321 - xOff2 + 78, 58 + 4 + yOff);
		tab.child2(33, 1658, 321 - xOff2 + 37, 58 + 43 + yOff);
		tab.child2(34, 1659, 321 - xOff2 + 78, 58 + 43 + yOff);
		tab.child2(35, 1660, 321 - xOff2 + 119, 58 + 43 + yOff);
		tab.child2(36, 1661, 321 - xOff2 + 22, 58 + 82 + yOff);
		tab.child2(37, 1662, 321 - xOff2 + 78, 58 + 82 + yOff);
		tab.child2(38, 1663, 321 - xOff2 + 134, 58 + 82 + yOff);
		tab.child2(39, 1664, 321 - xOff2 + 78, 58 + 122 + yOff);
		tab.child2(40, 1665, 321 - xOff2 + 78, 58 + 162 + yOff);
		tab.child2(41, 1666, 321 - xOff2 + 22, 58 + 162 + yOff);
		tab.child2(42, 1667, 321 - xOff2 + 134, 58 + 162 + yOff);
		tab.child2(43, 1688, 50 + 297 - xOff2 - 2, 110 - 13 + 5 + yOff);
		tab.child(47, 15122, 91, 277); // Weight text

		addSprite(70009, 33, dir);
		addText(70010, "0.0%", wid, 1, 0xff981f, false, true);
		tab.child(48, 70009, 68,299); // drop rate sprite
		tab.child(49, 70010, 93,304); // drop rate text

		// Maxhits

		tab.child2(44, 16117, 87, 283); 

		for (int i = 1675; i <= 1684; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xFF9300;
			rsi.centerText = false;
		}
		for (int i = 1686; i <= 1687; i++) {
			RSInterface rsi = interfaceCache[i];
			rsi.textColor = 0xFF9300;
			rsi.centerText = false;
		}

		tab.child2(44, 21451, 352, 270);
		tab.child2(45, 21452, 352, 286);
		tab.child2(46, 15125, 192, 180);
		tab.child2(17, 1687, 352, 302);
		hoverButton_fromfile(90504, "Presets", 659,660, "Presets", RSInterface.newFonts[0], 0xff981f, 0xffffff, true, "Interfaces/buttons/SPRITE");
		tab.child2(50, 90504, 26,38);
	}

	public static void expLock(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(37500);
		String dir = "/Interfaces/Exp Lock/SPRITE";
		String dir2 = "/Interfaces/Prestige/PlayerStats/skills/IMG";
		addSprite(37501, 0, dir);
		addHoverButton(37502, dir, 1, 16, 16, "Close", -1, 37503, 1);
		addHoveredButton(37503, dir, 2, 16, 16, 37504);
		addText(37505, "Exp Lock Manager", tda, 2, 0xFFA500, true, true);
		int x = 150, y = 10;
		tab.totalChildren(5);
		tab.child(0, 37501, x, y);
		tab.child(1, 37502, 180 + x, 4 + y);
		tab.child(2, 37503, 180 + x, 4 + y);
		tab.child(3, 37505, 100 + x, 5 + y);
		tab.child(4, 37510, 10 + x, 30 + y);

		final String[] skillNames = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer", "Magic" };

		RSInterface over = addInterface(37510);
		addSprite(37527, 5, dir);
		int xx = 0, yy = 0;
		over.totalChildren(42);
		for (int i = 0; i < 7; i++) {
			addButton(37511 + i, 3, dir, "Toggle @lre@" + skillNames[i]);
			addSprite(37519 + i, i, dir2);
			addText(37528 + i, Client.capitalize(skillNames[i]) + ":", tda, 0, 16748608, false, true);
			addText(37536 + i, "@gre@Unlocked", tda, 0, 16777215, false, true);
			addText(37544 + i, "@gre@99", tda, 0, 16777215, false, true);
			int[] g = centerSkillSprite(interfaceCache[37519 + i].sprite1);
			over.child(i, 37511 + i, xx, yy);
			over.child(i + 7, 37519 + i, xx + g[0], yy + g[1]);
			over.child(i + 14, 37527, xx + 32, yy + 7);
			over.child(i + 21, 37528 + i, xx + 35, yy + 10);
			over.child(i + 28, 37536 + i, xx + 125, yy + 10);
			over.child(i + 35, 37544 + i, xx + 90, yy + 10);
			xx += 117;
			if (xx == 117) {
				xx = 0;
				yy += 39;
			}
		}
	}

	public static void prestigeInterface(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(37300);
		String dir = "/Interfaces/Prestige/SPRITE";
		String dir2 = "/Interfaces/Prestige/PlayerStats/skills/IMG";
		addSprite(37301, 0, dir);
		addHoverButton(37302, dir, 1, 16, 16, "Close", -1, 37303, 1);
		addHoveredButton(37303, dir, 2, 16, 16, 37304);
		get(37302).atActionType = 3;
		get(37303).atActionType = 3;

		addText(37305, "Prestige Manager", tda, 2, 0xFFA500, true, true);
		addSprite(37306, 6, dir);
		addText(37307, "Attack:", tda, 2, 0xFFA500, true, true);
		addText(37308, "Current Prestige: @whi@0", tda, 0, 16748608, false, true);
		addText(37309, "Reward: @whi@1000 Points", tda, 0, 16748608, false, true);
		addText(37390, "Can Prestige: @whi@...", tda, 0, 16748608, false, true);
		addHoverButton(37391, dir, 7, 80, 31, "Prestige selected skill", 0, 37392, 1);
		addHoveredButton(37392, dir, 8, 80, 31, 37393);
		addText(37394, "Prestige", tda, 2, 16777215, true, true);
		int x = 10, y = 10;
		tab.totalChildren(13);
		tab.child(0, 37301, x, y);
		tab.child(1, 37302, 463 + x, 4 + y);
		tab.child(2, 37303, 463 + x, 4 + y);
		tab.child(3, 37305, 243 + x, 5 + y);
		tab.child(4, 37310, 10 + x, 30 + y);
		tab.child(5, 37306, 258 + x, 222 + y);
		tab.child(6, 37307, 366 + x, 226 + y);
		tab.child(7, 37308, 263 + x, 242 + y);
		tab.child(8, 37309, 263 + x, 257 + y);
		tab.child(9, 37390, 263 + x, 273 + y);
		tab.child(10, 37391, 389 + x, 255 + y);
		tab.child(11, 37392, 389 + x, 255 + y);
		tab.child(12, 37394, 429 + x, 262 + y);

		final String[] skillNames = { "attack", "defence", "strength", "hitpoints", "ranged", "prayer", "magic",
				"cooking", "woodcutting", "fletching", "fishing", "firemaking", "crafting", "smithing", "mining",
				"herblore", "agility", "thieving", "slayer", "farming", "runecraft", "hunter", "-unused-", "-unused-",
				"-unused-" };

		RSInterface over = addInterface(37310);
		addSprite(37359, 5, dir);
		int xx = 0, yy = 0, line = 0;
		over.totalChildren(110);
		for (int i = 0; i < 22; i++) {
			addButton(37311 + i, 3, dir, "Select @lre@" + skillNames[i]);
			addSprite(37335 + i, i, dir2);
			addText(37360 + i, Client.capitalize(skillNames[i]), tda, 0, 16748608, false, true);
			addText(37400 + i, "0", tda, 0, 16777215, false, true);
			int[] g = centerSkillSprite(interfaceCache[37335 + i].sprite1);
			int bonusY = 0;
			if (xx == 0 || xx == 117)
				bonusY = 5 * line;
			over.child(i, 37311 + i, xx, yy + bonusY);
			over.child(i + 22, 37335 + i, xx + g[0], yy + g[1] + bonusY);
			over.child(i + 44, 37359, xx + 32, yy + 7 + bonusY);
			over.child(i + 66, 37360 + i, xx + 35, yy + 10 + bonusY);
			over.child(i + 88, 37400 + i, xx + 102, yy + 10 + bonusY);
			xx += 117;
			if (xx == 468) {
				xx = 0;
				yy += 39;
				line++;
			}
		}
	}

	public static int[] centerSkillSprite(Sprite s) {
		int x = 15, y = 15;
		x -= (s.myWidth / 2);
		y -= (s.myHeight / 2);
		return new int[] { x, y };
	}

	public static void slayerOverlay(TextDrawingArea[] tda) {
		RSInterface rsInterface = addInterface(35424);
		addText(35425, "Abyssal Demon: 5", tda, 1, 0xF7FE2E, true, true);
		setChildren(1, rsInterface);
		//really good way of doing stuff for resized!
		rsInterface.child(0, 35425,
				!Client.instance.isResized() ? 250 : (Client.canvasWidth - 300), 10); // Assignment
	}

	private static void skotizo(TextDrawingArea[] tda) {
		RSInterface tab = addInterface(29230);
		tab.totalChildren(5);
		addSprite(29231, 0, "Interfaces/Skotizo/SKOTIZOM");
		tab.child(0, 29231, 36, 141);
		addSprites(29232, "Interfaces/Skotizo/SKOTIZO", 3, 0);
		tab.child(1, 29232, 56, 128);
		addSprites(29233, "Interfaces/Skotizo/SKOTIZO", 3, 0);
		tab.child(2, 29233, 56, 193);
		addSprites(29234, "Interfaces/Skotizo/SKOTIZO", 3, 0);
		tab.child(3, 29234, 23, 160);
		addSprites(29235, "Interfaces/Skotizo/SKOTIZO", 3, 0);
		tab.child(4, 29235, 88, 160);
	}

	public static void tradingSelect(TextDrawingArea[] tda) {
		RSInterface Interface = addTabInterface(48599);
		RSInterface history = addTabInterface(48635);
		RSInterface offers = addTabInterface(48950);

		addSprite(48601, 7, "Trading/SPRITE");
		addHover(48602, 3, 0, 48603, 1, "Bank/BANK", 17, 17, "Close Window");
		addHovered(48603, 2, "Bank/BANK", 17, 17, 48604);

		addText(48605, "Trading Post", 0xff9933, true, true, -1, tda, 2);

		addText(48606, "Coffer", 0xff9933, true, true, -1, tda, 2);
		addHoverButton(48607, "Trading/SPRITE", 8, 143, 35, "Claim money", 0, 48608, 1);
		addHoveredButton(48608, "Trading/SPRITE", 9, 143, 35, 48609);
		addText(48610, "", 0xff9933, true, true, -1, tda, 0);

		addText(48611, "Search for...", 0xff9933, true, true, -1, tda, 2);
		addHoverButton(48612, "Trading/SPRITE", 10, 72, 32, "Search for a item", 0, 48613, 1);
		addHoveredButton(48613, "Trading/SPRITE", 11, 72, 32, 48614);

		addHoverButton(48615, "Trading/SPRITE", 10, 72, 32, "Search for a player", 0, 48616, 1);
		addHoveredButton(48616, "Trading/SPRITE", 11, 72, 32, 48617);

		addHoverButton(48618, "Trading/SPRITE", 10, 72, 32, "Recent offers", 0, 48619, 1);
		addHoveredButton(48619, "Trading/SPRITE", 11, 72, 32, 48620);

		addHoverButton(48621, "Trading/SPRITE", 12, 150, 35, "Click", 0, 48622, 1);
		addHoveredButton(48622, "Trading/SPRITE", 13, 150, 35, 48623);

		addText(48624, "History", 0xff9933, true, true, -1, tda, 2);
		addText(48625, "My Offers...", 0xff9933, true, true, -1, tda, 2);

		addText(48626, "Item", 0xff9933, true, true, -1, tda, 2);
		addText(48627, "Player", 0xff9933, true, true, -1, tda, 2);
		addText(48628, "Recent", 0xff9933, true, true, -1, tda, 2);
		addText(48629, "Cancel Listing", 0xff9933, true, true, -1, tda, 2);

		setChildren(25, Interface);

		setBounds(48601, 8, 10, 0, Interface); // Base
		setBounds(48602, 471, 18, 1, Interface); // Close
		setBounds(48603, 471, 18, 2, Interface);

		setBounds(48605, 262, 20, 3, Interface); // Title

		setBounds(48606, 40, 50, 4, Interface); // Coffer text
		setBounds(48607, 19, 68, 5, Interface); // Coffer Button
		setBounds(48608, 19, 68, 6, Interface);
		setBounds(48610, 75, 80, 7, Interface); // Amount in coffer

		setBounds(48611, 59, 250, 8, Interface); // Text search for...
		setBounds(48612, 19, 270, 9, Interface); // Item Button
		setBounds(48613, 19, 270, 10, Interface);

		setBounds(48615, 96, 270, 11, Interface); // Player Button
		setBounds(48616, 96, 270, 12, Interface);

		setBounds(48618, 173, 270, 13, Interface); // Recent Button
		setBounds(48619, 173, 270, 14, Interface);

		setBounds(48621, 293, 270, 15, Interface); // Long button
		setBounds(48622, 293, 270, 16, Interface);

		setBounds(48624, 42, 104, 17, Interface); // Text History
		setBounds(48625, 289, 50, 18, Interface); // TextMy offers

		setBounds(48626, 55, 279, 19, Interface); // Text Item
		setBounds(48627, 132, 279, 20, Interface); // Text Player
		setBounds(48628, 208, 279, 21, Interface); // Text Recent
		setBounds(48629, 367, 279, 22, Interface); // Text List

		setBounds(48635, 30, 128, 23, Interface); // Interface history
		setBounds(48950, 260, 75, 24, Interface); // Interface my offers

		setChildren(20, history);
		for (int i = 48636, y = 5, id = 0; i < 48656; i++) {
			addText(i, "", 0xff9933, true, true, -1, tda, 0);
			setBounds(i, 94, y, id, history);
			if (id == 1 || id == 3 || id == 5 || id == 7 || id == 9 || id == 11 || id == 13 || id == 15 || id == 17)
				y += 20;
			else
				y += 10;
			id++;
		}

		history.scrollMax = 300;
		history.width = 193;
		history.height = 116;

		addText(48951, "Select an item", 0xff9933, true, true, -1, tda, 2);
		addText(48952, "from your", 0xff9933, true, true, -1, tda, 2);
		addText(48953, "inventory.", 0xff9933, true, true, -1, tda, 2);

		setChildren(3, offers);

		setBounds(48951, 108, 66, 0, offers); // Name
		setBounds(48952, 108, 80, 1, offers); // Price
		setBounds(48953, 108, 94, 2, offers); // sold

		// offers.scrollMax = 300;
		offers.width = 203;
		offers.height = 181;
	}

	public static void tradingpost(TextDrawingArea[] tda) {
		RSInterface Interface = addTabInterface(48600);
		RSInterface history = addTabInterface(48635);
		RSInterface offers = addTabInterface(48786);

		addSprite(48601, 7, "Trading/SPRITE");
		addHover(48602, 3, 0, 48603, 1, "Bank/BANK", 17, 17, "Close Window");
		addHovered(48603, 2, "Bank/BANK", 17, 17, 48604);

		addText(48605, "Trading Post", 0xff9933, true, true, -1, tda, 2);

		addText(48606, "Coffer", 0xff9933, true, true, -1, tda, 2);
		addHoverButton(48607, "Trading/SPRITE", 8, 143, 35, "Claim money", 0, 48608, 1);
		addHoveredButton(48608, "Trading/SPRITE", 9, 143, 35, 48609);
		addText(48610, "Empty", 0xff9933, true, true, -1, tda, 0);

		addText(48611, "Search for...", 0xff9933, true, true, -1, tda, 2);
		addHoverButton(48612, "Trading/SPRITE", 10, 72, 32, "Search for a item", 0, 48613, 1);
		addHoveredButton(48613, "Trading/SPRITE", 11, 72, 32, 48614);

		addHoverButton(48615, "Trading/SPRITE", 10, 72, 32, "Search for a player", 0, 48616, 1);
		addHoveredButton(48616, "Trading/SPRITE", 11, 72, 32, 48617);

		addHoverButton(48618, "Trading/SPRITE", 10, 72, 32, "Recent offers", 0, 48619, 1);
		addHoveredButton(48619, "Trading/SPRITE", 11, 72, 32, 48620);

		addHoverButton(48621, "Trading/SPRITE", 12, 150, 35, "Click", 0, 48622, 1);
		addHoveredButton(48622, "Trading/SPRITE", 13, 150, 35, 48623);

		addText(48624, "History", 0xff9933, true, true, -1, tda, 2);
		addText(48625, "My Offers...", 0xff9933, true, true, -1, tda, 2);

		addText(48626, "Item", 0xff9933, true, true, -1, tda, 2);
		addText(48627, "Player", 0xff9933, true, true, -1, tda, 2);
		addText(48648, "Recent", 0xff9933, true, true, -1, tda, 2);
		addText(48630, "List item for sale", 0xff9933, true, true, -1, tda, 2);

		setChildren(25, Interface);

		setBounds(48601, 8, 10, 0, Interface); // Base
		setBounds(48602, 471, 18, 1, Interface); // Close
		setBounds(48603, 471, 18, 2, Interface);

		setBounds(48605, 262, 20, 3, Interface); // Title

		setBounds(48606, 40, 50, 4, Interface); // Coffer text
		setBounds(48607, 19, 68, 5, Interface); // Coffer Button
		setBounds(48608, 19, 68, 6, Interface);
		setBounds(48610, 75, 80, 7, Interface); // Amount in coffer

		setBounds(48611, 59, 250, 8, Interface); // Text search for...
		setBounds(48612, 19, 270, 9, Interface); // Item Button
		setBounds(48613, 19, 270, 10, Interface);

		setBounds(48615, 96, 270, 11, Interface); // Player Button
		setBounds(48616, 96, 270, 12, Interface);

		setBounds(48618, 173, 270, 13, Interface); // Recent Button
		setBounds(48619, 173, 270, 14, Interface);

		setBounds(48621, 293, 270, 15, Interface); // Long button
		setBounds(48622, 293, 270, 16, Interface);

		setBounds(48624, 42, 104, 17, Interface); // Text History
		setBounds(48625, 289, 50, 18, Interface); // TextMy offers

		setBounds(48626, 55, 279, 19, Interface); // Text Item
		setBounds(48627, 132, 279, 20, Interface); // Text Player
		setBounds(48628, 208, 279, 21, Interface); // Text Recent
		setBounds(48630, 367, 279, 22, Interface); // Text List

		setBounds(48635, 30, 128, 23, Interface); // Interface history
		setBounds(48786, 260, 75, 24, Interface); // Interface my offers

		setChildren(40, history);
		for (int i = 48636, y = 5, id = 0; i < 48676; i++) {
			addText(i, "", 0xff9933, true, true, -1, tda, 0);
			setBounds(i, 94, y, id, history);
			if (id == 1 || id == 3 || id == 5 || id == 7 || id == 9 || id == 11 || id == 13 || id == 15 || id == 17 || id == 19 || id == 21 || id == 23 || id == 25 || id == 27 || id == 29 || id == 31 || id == 33 || id == 35 || id == 37 || id == 39)
				y += 20;
			else
				y += 10;
			id++;
		}

		history.scrollMax = 5000;
		history.width = 193;
		history.height = 116;

		addListing(48847, false);
		addText(48788, "", 0xff9933, true, true, -1, tda, 0);
		addText(48789, "", 0xff9933, true, true, -1, tda, 0);
		addText(48790, "", 0xff9933, true, true, -1, tda, 0);

		addText(48792, "", 0xff9933, true, true, -1, tda, 0);
		addText(48793, "", 0xff9933, true, true, -1, tda, 0);
		addText(48794, "", 0xff9933, true, true, -1, tda, 0);

		addText(48796, "", 0xff9933, true, true, -1, tda, 0);
		addText(48797, "", 0xff9933, true, true, -1, tda, 0);
		addText(48798, "", 0xff9933, true, true, -1, tda, 0);

		addText(48800, "", 0xff9933, true, true, -1, tda, 0);
		addText(48801, "", 0xff9933, true, true, -1, tda, 0);
		addText(48802, "", 0xff9933, true, true, -1, tda, 0);

		addText(48804, "", 0xff9933, true, true, -1, tda, 0);
		addText(48805, "", 0xff9933, true, true, -1, tda, 0);
		addText(48806, "", 0xff9933, true, true, -1, tda, 0);

		addText(48808, "", 0xff9933, true, true, -1, tda, 0);
		addText(48809, "", 0xff9933, true, true, -1, tda, 0);
		addText(48810, "", 0xff9933, true, true, -1, tda, 0);

		addText(48812, "", 0xff9933, true, true, -1, tda, 0);
		addText(48813, "", 0xff9933, true, true, -1, tda, 0);
		addText(48814, "", 0xff9933, true, true, -1, tda, 0);

		addText(48816, "", 0xff9933, true, true, -1, tda, 0);
		addText(48817, "", 0xff9933, true, true, -1, tda, 0);
		addText(48818, "", 0xff9933, true, true, -1, tda, 0);

		addText(48820, "", 0xff9933, true, true, -1, tda, 0);
		addText(48821, "", 0xff9933, true, true, -1, tda, 0);
		addText(48822, "", 0xff9933, true, true, -1, tda, 0);

		addText(48824, "", 0xff9933, true, true, -1, tda, 0);
		addText(48825, "", 0xff9933, true, true, -1, tda, 0);
		addText(48826, "", 0xff9933, true, true, -1, tda, 0);

		addText(48828, "", 0xff9933, true, true, -1, tda, 0);
		addText(48829, "", 0xff9933, true, true, -1, tda, 0);
		addText(48830, "", 0xff9933, true, true, -1, tda, 0);

		addText(48832, "", 0xff9933, true, true, -1, tda, 0);
		addText(48833, "", 0xff9933, true, true, -1, tda, 0);
		addText(48834, "", 0xff9933, true, true, -1, tda, 0);

		addText(48836, "", 0xff9933, true, true, -1, tda, 0);
		addText(48837, "", 0xff9933, true, true, -1, tda, 0);
		addText(48838, "", 0xff9933, true, true, -1, tda, 0);

		addText(48840, "", 0xff9933, true, true, -1, tda, 0);
		addText(48841, "", 0xff9933, true, true, -1, tda, 0);
		addText(48842, "", 0xff9933, true, true, -1, tda, 0);

		addText(48844, "", 0xff9933, true, true, -1, tda, 0);
		addText(48845, "", 0xff9933, true, true, -1, tda, 0);
		addText(48846, "", 0xff9933, true, true, -1, tda, 0);

		setChildren(46, offers);

		setBounds(48788, 118, 10, 0, offers); // Name
		setBounds(48789, 51, 33, 1, offers); // Price
		setBounds(48790, 118, 23, 2, offers); // sold

		setBounds(48792, 118, 57, 3, offers); // Name y + 49
		setBounds(48793, 51, 80, 4, offers); // Price y + 48
		setBounds(48794, 118, 70, 5, offers); // sold y + 49

		setBounds(48796, 118, 104, 6, offers); // Name y + 49
		setBounds(48797, 51, 127, 7, offers); // Price y + 28
		setBounds(48798, 118, 117, 8, offers); // sold y + 49

		setBounds(48800, 118, 151, 9, offers); // Name y + 49
		setBounds(48801, 51, 174, 10, offers); // Price y + 28
		setBounds(48802, 118, 164, 11, offers); // sold y + 49

		setBounds(48804, 118, 198, 12, offers); // Name y + 49
		setBounds(48805, 51, 221, 13, offers); // Price y + 28
		setBounds(48806, 118, 211, 14, offers); // sold y + 49

		setBounds(48808, 118, 245, 15, offers); // Name y + 49
		setBounds(48809, 51, 268, 16, offers); // Price y + 48
		setBounds(48810, 118, 258, 17, offers); // sold y + 49

		setBounds(48812, 118, 292, 18, offers); // Name y + 49
		setBounds(48813, 51, 315, 19, offers); // Price y + 48
		setBounds(48814, 118, 305, 20, offers); // sold y + 49

		setBounds(48816, 118, 339, 21, offers); // Name y + 49
		setBounds(48817, 51, 362, 22, offers); // Price y + 48
		setBounds(48818, 118, 352, 23, offers); // sold y + 49

		setBounds(48820, 118, 386, 24, offers); // Name y + 49
		setBounds(48821, 51, 409, 25, offers); // Price y + 48
		setBounds(48822, 118, 399, 26, offers); // sold y + 49

		setBounds(48824, 118, 433, 27, offers); // Name y + 47
		setBounds(48825, 51, 456, 28, offers); // Price y + 47 fout
		setBounds(48826, 118, 446, 29, offers); // sold y + 47

		setBounds(48828, 118, 480, 30, offers); // Name y + 49
		setBounds(48829, 51, 503, 31, offers); // Price y + 48
		setBounds(48830, 118, 493, 32, offers); // sold y + 49

		setBounds(48832, 118, 527, 33, offers); // Name y + 49
		setBounds(48833, 51, 550, 34, offers); // Price y + 48
		setBounds(48834, 118, 540, 35, offers); // sold y + 49

		setBounds(48836, 118, 574, 36, offers); // Name y + 49
		setBounds(48837, 51, 597, 37, offers); // Price y + 48
		setBounds(48838, 118, 587, 38, offers); // sold y + 49

		setBounds(48840, 118, 621, 39, offers); // Name y + 49
		setBounds(48841, 51, 644, 40, offers); // Price y + 48
		setBounds(48842, 118, 634, 41, offers); // sold y + 49

		setBounds(48844, 118, 668, 42, offers); // Name y + 49
		setBounds(48845, 51, 691, 43, offers); // Price y + 48
		setBounds(48846, 118, 681, 44, offers); // sold y + 49

		setBounds(48847, 35, 2, 45, offers);

		offers.scrollMax = 710;
		offers.width = 203;
		offers.height = 181;
	}

	public static void offer(TextDrawingArea[] tda) {

		RSInterface rsi = addTabInterface(48500);
		addOffer(48501);
		rsi.totalChildren(1);
		rsi.child(0, 48501, 16, 8);
	}

	public static void addOffer(int index) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.inventoryAmounts = new int[30];
		rsi.inventoryItemId = new int[30];
		rsi.spritesY = new int[20];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];

		rsi.actions[0] = "Offer 1";
		rsi.actions[1] = "Offer 5";
		rsi.actions[2] = "Offer 10";
		rsi.actions[3] = "Offer All";
		rsi.actions[4] = "Offer X";
		rsi.centerText = true;
		rsi.aBoolean227 = false;
		rsi.aBoolean235 = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.aBoolean259 = true;
		rsi.textShadow = false;
		rsi.invSpritePadX = 10;
		rsi.invSpritePadY = 4;
		rsi.height = 7;
		rsi.width = 4;
		rsi.parentID = 48501;
		rsi.id = 48500;
		rsi.type = 2;
	}

	public static void tradingSelected(TextDrawingArea[] tda) {
		RSInterface Interface = addTabInterface(48598);
		RSInterface history = addTabInterface(48635);
		RSInterface offers = addTabInterface(48960);

		addSprite(48601, 7, "Trading/SPRITE");
		addHover(48602, 3, 0, 48603, 1, "Bank/BANK", 17, 17, "Close Window");
		addHovered(48603, 2, "Bank/BANK", 17, 17, 48604);

		addText(48605, "Trading Post", 0xff9933, true, true, -1, tda, 2);

		addText(48606, "Coffer", 0xff9933, true, true, -1, tda, 2);
		addHoverButton(48607, "Trading/SPRITE", 8, 143, 35, "Claim money", 0, 48608, 1);
		addHoveredButton(48608, "Trading/SPRITE", 9, 143, 35, 48609);
		addText(48610, "Empty", 0xff9933, true, true, -1, tda, 0);

		addText(48611, "Search for...", 0xff9933, true, true, -1, tda, 2);
		addHoverButton(48612, "Trading/SPRITE", 10, 72, 32, "Search for a item", 0, 48613, 1);
		addHoveredButton(48613, "Trading/SPRITE", 11, 72, 32, 48614);

		addHoverButton(48615, "Trading/SPRITE", 10, 72, 32, "Search for a player", 0, 48616, 1);
		addHoveredButton(48616, "Trading/SPRITE", 11, 72, 32, 48617);

		addHoverButton(48618, "Trading/SPRITE", 10, 72, 32, "Recent offers", 0, 48619, 1);
		addHoveredButton(48619, "Trading/SPRITE", 11, 72, 32, 48620);

		addHoverButton(48621, "Trading/SPRITE", 12, 150, 35, "Click", 0, 48622, 1);
		addHoveredButton(48622, "Trading/SPRITE", 13, 150, 35, 48623);

		addText(48624, "History", 0xff9933, true, true, -1, tda, 2);
		addText(48625, "My Offers...", 0xff9933, true, true, -1, tda, 2);

		addText(48626, "Item", 0xff9933, true, true, -1, tda, 2);
		addText(48627, "Player", 0xff9933, true, true, -1, tda, 2);
		addText(48628, "Recent", 0xff9933, true, true, -1, tda, 2);
		addText(48629, "Cancel Listing", 0xff9933, true, true, -1, tda, 2);

		setChildren(25, Interface);

		setBounds(48601, 8, 10, 0, Interface); // Base
		setBounds(48602, 471, 18, 1, Interface); // Close
		setBounds(48603, 471, 18, 2, Interface);

		setBounds(48605, 262, 20, 3, Interface); // Title

		setBounds(48606, 40, 50, 4, Interface); // Coffer text
		setBounds(48607, 19, 68, 5, Interface); // Coffer Button
		setBounds(48608, 19, 68, 6, Interface);
		setBounds(48610, 75, 80, 7, Interface); // Amount in coffer

		setBounds(48611, 59, 250, 8, Interface); // Text search for...
		setBounds(48612, 19, 270, 9, Interface); // Item Button
		setBounds(48613, 19, 270, 10, Interface);

		setBounds(48615, 96, 270, 11, Interface); // Player Button
		setBounds(48616, 96, 270, 12, Interface);

		setBounds(48618, 173, 270, 13, Interface); // Recent Button
		setBounds(48619, 173, 270, 14, Interface);

		setBounds(48621, 293, 270, 15, Interface); // Long button
		setBounds(48622, 293, 270, 16, Interface);

		setBounds(48624, 42, 104, 17, Interface); // Text History
		setBounds(48625, 289, 50, 18, Interface); // TextMy offers

		setBounds(48626, 55, 279, 19, Interface); // Text Item
		setBounds(48627, 132, 279, 20, Interface); // Text Player
		setBounds(48628, 208, 279, 21, Interface); // Text Recent
		setBounds(48629, 367, 279, 22, Interface); // Text List

		setBounds(48635, 30, 128, 23, Interface); // Interface history
		setBounds(48960, 260, 75, 24, Interface); // Interface my offers

		setChildren(30, history);
		for (int i = 48636, y = 5, id = 0; i < 48666; i++) {
			addText(i, "", 0xff9933, true, true, -1, tda, 0);
			setBounds(i, 94, y, id, history);
			if (id == 1 || id == 3 || id == 5 || id == 7 || id == 9 || id == 11 || id == 13 || id == 15 || id == 17 ||
					id == 19 || id == 21 || id == 23 || id == 25 || id == 27)
				y += 20;
			else
				y += 10;
			id++;
		}

		history.scrollMax = 500;
		history.width = 193;
		history.height = 116;

		addSprite(48961, 14, "Trading/SPRITE");
		addItemOnInterface(48962);
		addText(48963, "", 0xff9933, true, true, -1, tda, 2);
		addText(48964, "", 0xff9933, true, true, -1, tda, 1);
		addText(48965, "", 0xff9933, true, true, -1, tda, 1);
		addText(48966, "", 0xff9933, true, true, -1, tda, 0); // Guide: 50M
		addText(48967, "", 0xff9933, true, true, -1, tda, 0); // Listed: 0

		addHoverButton(48968, "Trading/SPRITE", 10, 72, 32, "Set price", 0, 48969, 1);
		addHoveredButton(48969, "Trading/SPRITE", 11, 72, 32, 48970);

		// addHoverButton(48971, "Trading/SPRITE", 10, 72, 32, "Set quantity", 0, 48972,
		// 1);
		// addHoveredButton(48972, "Trading/SPRITE", 11, 72, 32, 48973);

		addHoverButton(48974, "Trading/SPRITE", 10, 72, 32, "Confirm offer", 0, 48975, 1);
		addHoveredButton(48975, "Trading/SPRITE", 11, 72, 32, 48976);

		addText(48977, "Set", 0xff9933, true, true, -1, tda, 2);
		addText(48978, "price", 0xff9933, true, true, -1, tda, 2);
		// addText(48979, "Set", 0xff9933, true, true, -1, tda, 2);
		// addText(48980, "quantity", 0xff9933, true, true, -1, tda, 2);
		addText(48981, "Confirm", 0xff9933, true, true, -1, tda, 2);

		setChildren(14, offers);

		setBounds(48961, 0, 0, 0, offers); // background
		setBounds(48962, 91, 11, 1, offers); // item
		setBounds(48963, 108, 50, 2, offers); // name
		setBounds(48964, 108, 70, 3, offers); // price ea
		setBounds(48965, 108, 85, 4, offers); // quantity
		setBounds(48966, 175, 5, 5, offers); // guide
		setBounds(48967, 175, 20, 6, offers); // listed

		setBounds(48968, 72, 105, 7, offers); // Set price button
		setBounds(48969, 72, 105, 8, offers);

		// setBounds(48971, 114, 105, 9, offers); //Set quantity button
		// setBounds(48972, 114, 105, 10, offers);

		setBounds(48974, 72, 142, 9, offers); // confirm button
		setBounds(48975, 72, 142, 10, offers);

		setBounds(48977, 107, 108, 11, offers); // set
		setBounds(48978, 107, 118, 12, offers); // price
		// setBounds(48979, 149, 108, 13, offers); //set
		// setBounds(48980, 149, 118, 14, offers); //quantity

		setBounds(48981, 107, 151, 13, offers); // confirm

		// offers.scrollMax = 300;
		offers.width = 219;
		offers.height = 181;
	}

	public static void addItemOnInterface(int i) {
		RSInterface rsinterface = interfaceCache[i] = new RSInterface();
		rsinterface.actions = new String[5];
		rsinterface.spritesX = new int[20];
		rsinterface.inventoryAmounts = new int[30];
		rsinterface.inventoryItemId = new int[30];
		rsinterface.spritesY = new int[20];
		rsinterface.children = new int[0];
		rsinterface.childX = new int[0];
		rsinterface.childY = new int[0];
		rsinterface.spritesY[0] = 0;
		// rsinterface.invStackSizes[0] = 0;
		for (int i2 = 0; i2 < 30; i2++)
			rsinterface.inventoryAmounts[i2] = 0;
		rsinterface.inventoryItemId[0] = 0;
		rsinterface.spritesX[0] = 0;
		rsinterface.actions[0] = "Cancel";
		rsinterface.centerText = false;
		rsinterface.aBoolean227 = false;
		rsinterface.aBoolean235 = false;
		rsinterface.usableItemInterface = false;
		rsinterface.isInventoryInterface = false;
		rsinterface.aBoolean259 = false;
		rsinterface.textShadow = false;
		rsinterface.width = 6;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.invSpritePadX = 24;
		rsinterface.parentID = i;
		rsinterface.invSpritePadY = 24;
		rsinterface.type = 2;
		rsinterface.height = 5;
	}

	public static void listings(TextDrawingArea[] tda) {
		
		RSInterface Interface = addTabInterface(48000);
		RSInterface main = addTabInterface(48020);

		setChildren(18, Interface);
		addSprite(48001, 0, "Trading/SPRITE");

		addHover(48002, 3, 0, 48003, 1, "Bank/BANK", 17, 17, "Close Window");
		addHovered(48003, 2, "Bank/BANK", 17, 17, 48004);
		addHoverButton(48005, "Trading/SPRITE", 1, 88, 30, "Go back", 0, 48006, 1);
		addHoveredButton(48006, "Trading/SPRITE", 2, 88, 30, 48007);
		addHoverButton(48008, "Trading/SPRITE", 10, 72, 35, "Prev Page", 0, 48009, 1);
		addHoveredButton(48009, "Trading/SPRITE", 11, 72, 35, 48010);
		addHoverButton(48011, "Trading/SPRITE", 10, 72, 35, "Next Page", 0, 48012, 1);
		addHoveredButton(48012, "Trading/SPRITE", 11, 72, 35, 48013); // 48045

		addText(48046, "Prev Page", 0xff9933, true, true, -1, tda, 1);
		addText(48047, "Next Page", 0xff9933, true, true, -1, tda, 1);

		addText(48014, "Quantity", tda, 0, 0xff981f, false, false);
		addText(48015, "Name", tda, 0, 0xff981f, false, false);
		addText(48016, "Price", tda, 0, 0xff981f, false, false);
		addText(48017, "Seller", tda, 0, 0xff981f, false, false);
		addText(48018, "Total Sold", tda, 0, 0xff981f, false, false);
		// addText(48019, "Title", tda, 2, 0xff981f, true, true);
		addText(48019, "Trading post", 0xff9933, true, true, -1, tda, 2); // 262, 20

		setBounds(48001, 9, 2, 0, Interface);
		setBounds(48002, 471, 12, 1, Interface); // Close
		setBounds(48003, 471, 12, 2, Interface);
		setBounds(48005, 19, 281, 3, Interface); // Go back
		setBounds(48006, 19, 281, 4, Interface);
		setBounds(48008, 331, 281, 5, Interface); // Modify
		setBounds(48009, 331, 281, 6, Interface);
		setBounds(48011, 413, 281, 7, Interface); // Refresh
		setBounds(48012, 413, 281, 8, Interface);

		setBounds(48014, 23, 42, 9, Interface); // Quantity
		setBounds(48015, 117, 42, 10, Interface); // Name
		setBounds(48016, 217, 42, 11, Interface); // Price
		setBounds(48017, 317, 42, 12, Interface); // Seller
		setBounds(48018, 414, 42, 13, Interface); // Total sold
		setBounds(48019, 262, 12, 14, Interface); // Titel
		setBounds(48020, 21, 50, 15, Interface); // Scroll

		setBounds(48046, 366, 288, 16, Interface); // Prev page
		setBounds(48047, 449, 288, 17, Interface); // Next Page

//		addListing(48021, true);
//		for (int i = 48022; i < 48046; i++) {
//			addText(i, "", tda, 0, 0xff981f, true, false);
//		}
		/*
		 * addItemOnInterface(28021); addText(28022, "Abyssal whip", tda, 0, 0xff981f,
		 * true, false); addText(28023, "100,000", tda, 0, 0xff981f, true, false);
		 * addText(28024, "Nighel", tda, 0, 0xff981f, true, false); addTextButton(28025,
		 * "Buy", "Buy", 0xFF981F, false, true, tda, 0, 400);
		 * 
		 * addItemOnInterface(28026); addText(28027, "Granite maul", tda, 0, 0xff981f,
		 * true, false); addText(28028, "10,000", tda, 0, 0xff981f, true, false);
		 * addText(28029, "Leroy the noob", tda, 0, 0xff981f, true, false);
		 * addTextButton(28030, "Buy", "Buy", 0xFF981F, false, true, tda, 0, 400);
		 */

		setChildren(1001, main);
		
		int childId = 26022;
		int frame = 0;
		
		addToItemGroup(childId, 1, 250, 0, 7, true, "Buy 1", "Buy 5", "Buy 10", "Buy All", "Buy X", "View History");
		setBounds(childId++, 5, 10, frame++, main); // Item
		
		for(int i = 0; i < 250; i++) {
			addText(childId, "", tda, 0, 0xff981f, true, false);
			setBounds(childId++, 109, 20 + (i * 39), frame++, main); // Name
			
			addText(childId, "", tda, 0, 0xff981f, true, false);
			setBounds(childId++, 210, 20 + (i * 39), frame++, main); // Amount
			
			addText(childId, "", tda, 0, 0xff981f, true, false);
			setBounds(childId++, 310, 20 + (i * 39), frame++, main); // Seller
			
			addText(childId, "", tda, 0, 0xff981f, true, false);
			setBounds(childId++, 418, 20 + (i * 39), frame++, main); // Buy
		}
		//System.out.println("CHILD:  + "  + childId);

//		setBounds(48021, 5, 10, 0, main); // Item
//
//		setBounds(48022, 109, 20, 1, main); // Name
//		setBounds(48023, 210, 20, 2, main); // Amount
//		setBounds(48024, 310, 20, 3, main); // Seller
//		setBounds(48025, 418, 20, 4, main); // Buy
//
//		setBounds(48026, 109, 59, 5, main); // Name
//		setBounds(48027, 210, 59, 6, main); // Amount
//		setBounds(48028, 310, 59, 7, main); // Seller
//		setBounds(48029, 418, 59, 8, main); // Buy
//
//		setBounds(48030, 109, 95, 9, main); // Name
//		setBounds(48031, 210, 95, 10, main); // Amount
//		setBounds(48032, 310, 95, 11, main); // Seller
//		setBounds(48033, 418, 95, 12, main); // Buy
//
//		setBounds(48034, 109, 131, 13, main); // Name
//		setBounds(48035, 210, 131, 14, main); // Amount
//		setBounds(48036, 310, 131, 15, main); // Seller
//		setBounds(48037, 418, 131, 16, main); // Buy
//
//		setBounds(48038, 109, 170, 17, main); // Name
//		setBounds(48039, 210, 170, 18, main); // Amount
//		setBounds(48040, 310, 170, 19, main); // Seller
//		setBounds(48041, 418, 170, 20, main); // Buy
//
//		setBounds(48042, 109, 204, 21, main); // Name
//		setBounds(48043, 210, 204, 22, main); // Amount
//		setBounds(48044, 310, 204, 23, main); // Seller
//		setBounds(48045, 418, 204, 24, main); // Buy

		main.width = 445;
		main.height = 227;
		main.scrollMax = 39 * 250;
	}

	public static void addListing(int index, boolean search) {
		RSInterface rsi = interfaceCache[index] = new RSInterface();
		rsi.actions = new String[10];
		rsi.spritesX = new int[20];
		rsi.inventoryAmounts = new int[30];
		rsi.inventoryItemId = new int[30];
		rsi.spritesY = new int[20];

		rsi.children = new int[0];
		rsi.childX = new int[0];
		rsi.childY = new int[0];
		if (!search)
			rsi.actions[0] = "Cancel";
		else {
			rsi.actions[0] = "Buy 1";
			rsi.actions[1] = "Buy 5";
			rsi.actions[2] = "Buy 10";
			rsi.actions[3] = "Buy All";
			rsi.actions[4] = "Buy X";
		}
		rsi.centerText = true;
		rsi.aBoolean227 = false;
		rsi.aBoolean235 = false;
		rsi.usableItemInterface = false;
		rsi.isInventoryInterface = false;
		rsi.aBoolean259 = true;
		rsi.textShadow = false; // 42, 82
		if (!search) {
			rsi.invSpritePadX = 0;
			rsi.invSpritePadY = 15;
		} else {
			rsi.invSpritePadX = 0;
			rsi.invSpritePadY = 5;
		}
		if (!search)
			rsi.height = 15;
		else
			rsi.height = 8;
		rsi.width = 1;
		rsi.parentID = index + 1;
		rsi.id = index;
		rsi.type = 2;
	}

	public static void helpDatabaseComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(59550);
		addSprite(59551, 8, "Interfaces/HelpInterface/IMAGE");
		addHoverButton(59552, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 59553, 3);
		addHoveredButton(59553, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 59554);
		addText(59555, "Bug Database", tda, 2, 0xFF981F, true, true);
		addText(59556, "Username/Date", tda, 1, 0xFF981F, false, true);
		addText(59557, "Line2", tda, 1, 0xFF981F, true, true);
		addText(59558, "Line3", tda, 1, 0xFF981F, true, true);
		addText(59559, "Line4", tda, 1, 0xFF981F, true, true);
		addText(59560, "Line5", tda, 1, 0xFF981F, true, true);

		setChildren(10, widget);
		setBounds(59551, 0, 2, 0, widget);
		setBounds(59552, 375, 8, 1, widget);
		setBounds(59553, 375, 8, 2, widget);
		setBounds(59570, 120, 30, 3, widget);
		setBounds(59555, 256, 8, 4, widget);
		setBounds(59556, 20, 225, 5, widget);
		setBounds(59557, 256, 245, 6, widget);
		setBounds(59558, 256, 265, 7, widget);
		setBounds(59559, 256, 285, 8, widget);
		setBounds(59560, 256, 305, 9, widget);

		RSInterface scroll = addInterface(59570);
		scroll.scrollMax = 400;
		scroll.width = 255;
		scroll.height = 170;
		setChildren(60, scroll);

		int yPosition = 0;
		int index = 0;
		for (int i = 0; i < 80; i += 4) {
			addText(59573 + i, "", tda, 1, 0xFF981F, false, true);
			addButton(59574 + i, 10, "Interfaces/HelpInterface/IMAGE", 13, 10, "View Request", 1);
			addButton(59575 + i, 9, "Interfaces/HelpInterface/IMAGE", 16, 15, "Remove Request", 1);
			setBounds(59573 + i, 4, yPosition + 3, index++, scroll);
			setBounds(59574 + i, 225, yPosition + 5, index++, scroll);
			setBounds(59575 + i, 240, yPosition + 3, index++, scroll);
			yPosition += 20;
		}
	}

	private static void barrowsKillcount(TextDrawingArea[] tda) {
		RSInterface barrow = addInterface(27500);
		addText(27501, "Brothers", tda, 2, 0xFD851A, true, true);
		addText(27502, "Ahrim", tda, 0, 0xFD851A, true, true);
		addText(27503, "Dharok", tda, 0, 0xFD851A, true, true);
		addText(27504, "Guthan", tda, 0, 0xFD851A, true, true);
		addText(27505, "Karil", tda, 0, 0xFD851A, true, true);
		addText(27506, "Torag", tda, 0, 0xFD851A, true, true);
		addText(27507, "Verac", tda, 0, 0xFD851A, true, true);
		addText(27508, "Killcount", tda, 2, 0xFD851A, true, true);
		addText(27509, "0", tda, 0, 0xFD851A, true, true);
		setChildren(9, barrow);
		setBounds(27501, 470, 42, 0, barrow);
		for (int index = 1; index < 7; index++)
			setBounds(27501 + index, 470, 45 + index * 14, index, barrow);
		setBounds(27508, 470, 15, 7, barrow);
		setBounds(27509, 470, 30, 8, barrow);
	}

//	public static void achievements(TextDrawingArea[] tda) {
//		RSInterface rsi = addInterface(49000);
//		addSprite(49001, 1, "Interfaces/Achievements/IMAGE");
//		addHoverButton(49002, "Interfaces/Achievements/IMAGE", 15, 16, 16, "Close Window", -1, 49003, 3);
//		addHoveredButton(49003, "Interfaces/Achievements/IMAGE", 16, 16, 16, 49004);
//		addConfigButton(49005, 49000, 12, 20, "Interfaces/Achievements/IMAGE", 71, 29, "Tier Tier I", 1, 1, 800);
//		addConfigButton(49006, 49000, 13, 20, "Interfaces/Achievements/IMAGE", 71, 29, "Tier Tier II", 1, 1, 801);
//		addConfigButton(49007, 49000, 14, 20, "Interfaces/Achievements/IMAGE", 71, 29, "View Tier III", 1, 1, 802);
//		addSprite(49014, 11, "Interfaces/Achievements/IMAGE");
//		addText(49016, "1000", tda, 0, 0xff981f, true, true);
//		addText(49017, "Tier I", tda, 0, 0xff981f, false, true);
//		addText(49018, "Tier II", tda, 0, 0xff981f, false, true);
//		addText(49019, "Tier III", tda, 0, 0xff981f, false, true);
//		addText(49020, "100", tda, 0, 0xff981f, false, true);
//
//		setChildren(14, rsi);
//		setBounds(49001, 0, 0, 0, rsi);
//		setBounds(49002, 490, 6, 1, rsi);
//		setBounds(49003, 490, 6, 2, rsi);
//
//		setBounds(49005, 15, 10, 3, rsi);
//		setBounds(49006, 90, 10, 4, rsi);
//		setBounds(49007, 165, 10, 5, rsi);
//
//		setBounds(49014, 415, 14, 6, rsi);
//		setBounds(49016, 443, 19, 7, rsi);
//		setBounds(49017, 37, 19, 8, rsi);
//		setBounds(49018, 111, 19, 9, rsi);
//		setBounds(49019, 184, 19, 10, rsi);
//
//		setBounds(49100, 3, 48, 11, rsi);
//		setBounds(51100, 3, 48, 12, rsi);
//		setBounds(53100, 3, 48, 13, rsi);
//
//		RSInterface scroll = addInterface(49100);
//		setChildren(800, scroll);
//		scroll.scrollMax = 6502;
//		scroll.height = 281;
//		scroll.width = 486;
//		int y = 0;
//		for (int i = 0; i < 100; i++) {
//			addSprite(49101 + i, 10, "Interfaces/Achievements/IMAGE");
//			addSprite(49201 + i, 2, "Interfaces/Achievements/IMAGE");
//			addSprite(49301 + i, 5, "Interfaces/Achievements/IMAGE");
//			addText(49401 + i, "", tda, 2, 0xFFFFFF, true, true);
//			addText(49501 + i, "", tda, 2, 0xFFFFFF, false, true);
//			addText(49601 + i, "", tda, 2, 0x425619, false, true);
//			addSprite(49701 + i, 6, "Interfaces/Achievements/IMAGE");
//			addText(49801 + i, "0/1", tda, 1, 0xFFFFFF, true, true);
//			setBounds(49101 + i, 1, y, i, scroll);
//			setBounds(49201 + i, 8, y + 5, 100 + i, scroll);
//			setBounds(49301 + i, 430, y + 12, 200 + i, scroll);
//			setBounds(49401 + i, 448, y + 24, 300 + i, scroll);
//			setBounds(49501 + i, 65, y + 9, 400 + i, scroll);
//			setBounds(49601 + i, 65, y + 24, 500 + i, scroll);
//			setBounds(49701 + i, 65, y + 41, 600 + i, scroll);
//			setBounds(49801 + i, 160, y + 43, 700 + i, scroll);
//			y += 65;
//		}
//		RSInterface tier2 = addInterface(51100);
//		setChildren(800, tier2);
//		tier2.scrollMax = 6502;
//		tier2.height = 281;
//		tier2.width = 486;
//		y = 0;
//		for (int i = 0; i < 100; i++) {
//			addSprite(51101 + i, 10, "Interfaces/Achievements/IMAGE");
//			addSprite(51201 + i, 3, "Interfaces/Achievements/IMAGE");
//			addSprite(51301 + i, 5, "Interfaces/Achievements/IMAGE");
//			addText(51401 + i, "", tda, 2, 0xFFFFFF, true, true);
//			addText(51501 + i, "", tda, 2, 0xFFFFFF, false, true);
//			addText(51601 + i, "", tda, 2, 0x425619, false, true);
//			addSprite(51701 + i, 6, "Interfaces/Achievements/IMAGE");
//			addText(51801 + i, "0/1", tda, 1, 0xFFFFFF, true, true);
//			setBounds(51101 + i, 1, y, i, tier2);
//			setBounds(51201 + i, 8, y + 5, 100 + i, tier2);
//			setBounds(51301 + i, 430, y + 12, 200 + i, tier2);
//			setBounds(51401 + i, 448, y + 24, 300 + i, tier2);
//			setBounds(51501 + i, 65, y + 9, 400 + i, tier2);
//			setBounds(51601 + i, 65, y + 24, 500 + i, tier2);
//			setBounds(51701 + i, 65, y + 41, 600 + i, tier2);
//			setBounds(51801 + i, 160, y + 43, 700 + i, tier2);
//			y += 65;
//		}
//		RSInterface tier3 = addInterface(53100);
//		setChildren(800, tier3);
//		tier3.scrollMax = 6502;
//		tier3.height = 281;
//		tier3.width = 486;
//		y = 0;
//		for (int i = 0; i < 100; i++) {
//			addSprite(53101 + i, 10, "Interfaces/Achievements/IMAGE");
//			addSprite(53201 + i, 4, "Interfaces/Achievements/IMAGE");
//			addSprite(53301 + i, 5, "Interfaces/Achievements/IMAGE");
//			addText(53401 + i, "", tda, 2, 0xFFFFFF, true, true);
//			addText(53501 + i, "", tda, 2, 0xFFFFFF, false, true);
//			addText(53601 + i, "", tda, 2, 0x425619, false, true);
//			addSprite(53701 + i, 6, "Interfaces/Achievements/IMAGE");
//			addText(53801 + i, "0/1", tda, 1, 0xFFFFFF, true, true);
//			setBounds(53101 + i, 1, y, i, tier3);
//			setBounds(53201 + i, 8, y + 5, 100 + i, tier3);
//			setBounds(53301 + i, 430, y + 12, 200 + i, tier3);
//			setBounds(53401 + i, 448, y + 24, 300 + i, tier3);
//			setBounds(53501 + i, 65, y + 9, 400 + i, tier3);
//			setBounds(53601 + i, 65, y + 24, 500 + i, tier3);
//			setBounds(53701 + i, 65, y + 41, 600 + i, tier3);
//			setBounds(53801 + i, 160, y + 43, 700 + i, tier3);
//			y += 65;
//		}
//	}



	private static void teleportInterface(TextDrawingArea[] textDrawingAreas) {

		int interfaceId = 39700;//checkinterface
		int child = 0;
		RSInterface interfaces = RSInterface.addInterface(interfaceId);
		interfaceId++;
		RSInterface.setChildren(33, interfaces);
		int xOffset = 80;
		int yOffset = 40;

		RSInterface.addSprite(interfaceId, 609, "Interfaces/Presets/SPRITE"); // Background.
		RSInterface.setBounds(interfaceId, 9 + xOffset, 21 + yOffset, child, interfaces);
		interfaceId++;
		child++;

		RSInterface.addText(interfaceId, "Teleports", textDrawingAreas, 2, 0xff981f, false);
		RSInterface.setBounds(interfaceId, 150 + xOffset, 30 + yOffset, child, interfaces);
		interfaceId++;
		child++;

		Object[][] teleportSelections = {
				{"Monsters", 1616},
				{"Bosses", 1617},
				{"Wilderness", 1613},
				{"Skilling", 1614},
				{"Minigames", 1618},
				{"Cities", 1615},
                {"Donator", 1619},
		};

		int yExtra = -1, count = 0;
		for (int index = 0; index < 7; index++) {
			String name = (String) teleportSelections[index][0];
			int buttonId = (int) teleportSelections[index][1];
			RSInterface.addHoverButtonLatest("Interfaces/Presets/SPRITE",interfaceId, interfaceId + 1, interfaceId + 2, count % 2 == 0 ? 614 : 614,
					610, 89, 26, "Choose");
			count++;
			RSInterface.addSprite(interfaceId, 612, "Interfaces/Presets/SPRITE");
			RSInterface.setBounds(interfaceId, 16 + xOffset, 57 + yOffset + yExtra, child, interfaces);
			child++;
			RSInterface.setBounds(interfaceId + 1, 16 + xOffset, 56 + yOffset + yExtra, child, interfaces);
			child++;
			interfaceId += 3;
			RSInterface.addClickableText(interfaceId, name,"Choose", textDrawingAreas, 1, 0xff981f, false,false,60, 16);
			RSInterface.setBounds(interfaceId, 40 + xOffset, 61 + yOffset + yExtra, child, interfaces);
			interfaceId++;
			child++;
			Sprite selectionIcon = new Sprite("Interfaces/teleport_interface/SPRITE " + buttonId);
			RSInterface.addSprite(interfaceId, selectionIcon);
			RSInterface.setBounds(interfaceId, 18 + xOffset - (selectionIcon.myWidth / 2) + 10,
					59 + yOffset + yExtra - (selectionIcon.myHeight / 2) + 9, child, interfaces);
			interfaceId++;
			child++;
			yExtra += 25;
		}
		yExtra = 0;
interfaceId+=5000;
		//RSInterface.addHoverButtonLatest("Interfaces/Presets/SPRITE",interfaceId, interfaceId + 1, interfaceId + 2, 148, 149, 15, 15,
		//		"Close Window");
		RSInterface.setBounds(CLOSE_BUTTON_SMALL, 317 + xOffset, 30 + yOffset, child, interfaces);
		RSInterface.setBounds(CLOSE_BUTTON_SMALL_HOVER, 317 + xOffset, 30 + yOffset, child + 1, interfaces);
		child += 2;
		interfaceId += 3;

		RSInterface scrollTab1 = RSInterface.addInterface(interfaceId);
		RSInterface.setBounds(interfaceId, 111 + xOffset, 56 + yOffset, child, interfaces); // scrollTab1
		interfaceId++;
		child++;

		int teleportAmount = 40;
		scrollTab1.width = 209;
		scrollTab1.height = 174;
		scrollTab1.scrollMax = 625;
		scrollTab1.totalChildren(teleportAmount * 3);
		int increaseY = 0, childNew = 0;
		for (int i = 0; i < teleportAmount; i++) {
			
			RSInterface.addHoverButtonLatest("Interfaces/Presets/SPRITE",interfaceId, interfaceId + 1, interfaceId + 2, count % 2 == 0 ? 613 : 967,
					611, 224, 26, "Teleport");
			count++;
			scrollTab1.child(childNew, interfaceId, 0, increaseY);
			childNew++;
			scrollTab1.child(childNew, interfaceId + 1, 0, increaseY);
			childNew++;
			interfaceId += 3;

			RSInterface.addText(interfaceId, ""+interfaceId, textDrawingAreas, 1, 0xff981f, true);
			scrollTab1.child(childNew, interfaceId, 104, increaseY + 4);
			increaseY += 25;
			interfaceId++;
			childNew++;
		}

		interfaces.setNewButtonClicking();
		scrollTab1.setNewButtonClicking();
	}

	public static void keybindingDropdown(int id, int width, int defaultOption, String[] options, MenuItem menuItem,
			boolean inverted) {
		RSInterface widget = addInterface(id);
		widget.type = TYPE_KEYBINDS_DROPDOWN;
		widget.dropdown = new DropdownMenu(width, true, defaultOption, options, menuItem);
		widget.atActionType = AT_ACTION_TYPE_OPTION_DROPDOWN;
		widget.inverted = inverted;
	}

	public static void keybinding(TextDrawingArea[] tda) {
		RSInterface tab = addTabInterface(53000);

		addSettingsSprite(53001, 63);
		addText(53002, "Keybinding", tda, 2, 0xff8a1f, false, true);
		closeButton(53003, 83, 84);

		hoverButton(Keybinding.RESTORE_DEFAULT, "Restore Defaults", 80, 81, "Restore Defaults", rsFont, 0xff8a1f,
				0xff8a1f, true);

		addText(53005, "Esc closes current interface", tda, 1, 0xff8a1f, false, true);
		configButton(Keybinding.ESCAPE_CONFIG, "Select", 62, 61);

		tab.totalChildren(48);
		int childNum = 0;

		setBounds(53001, 5, 17, childNum++, tab);
		setBounds(53002, 221, 27, childNum++, tab);
		setBounds(39021, 479, 24, childNum++, tab);
		setBounds(Keybinding.RESTORE_DEFAULT, 343, 275, childNum++, tab);
		setBounds(53005, 59, 284, childNum++, tab);
		setBounds(Keybinding.ESCAPE_CONFIG, 35, 285, childNum++, tab);

		/* Tabs and dropdowns */

		int x = 31;
		int y = 63;
		childNum = 47;

		for (int i = 0; i < 14; i++, y += 43) {

			addSettingsSprite(53007 + 3 * i, 64 + i);
			configButton(53008 + 3 * i, "", 79, 78);

			boolean inverted = i == 3 || i == 4 || i == 8 || i == 9 || i == 13;
			keybindingDropdown(53009 + 3 * i, 86, 0, Keybinding.OPTIONS, new KeybindingMenu(), inverted);

			setBounds(Keybinding.MIN_FRAME - 2 + 3 * i, x + stoneOffset(64 + i, true), y + stoneOffset(64 + i, false),
					childNum--, tab);
			setBounds(Keybinding.MIN_FRAME - 1 + 3 * i, x, y, childNum--, tab);
			setBounds(Keybinding.MIN_FRAME + 3 * i, x + 39, y + 4, childNum--, tab);

			if (i == 4 || i == 9) {
				x += 160;
				y = 20;
			}
		}
	}

	public static int stoneOffset(int spriteId, boolean xOffset) {
		Sprite stone = Client.cacheSprite3[79];
		Sprite icon = Client.cacheSprite3[spriteId];

		if (xOffset) {
			return (stone.myWidth / 2) - icon.myWidth / 2;
		}
		return (stone.myHeight / 2) - icon.myHeight / 2;
	}

	public static void clanChatSetup(TextDrawingArea[] tda) {
		RSInterface rsi = addInterface(18300);
		rsi.totalChildren(12 + 15);
		int count = 0;
		/* Background */
		addSprite(18301, 1, "/Interfaces/Clan Chat/sprite");
		rsi.child(count++, 18301, 14, 18);
		/* Close button */
		addButton(48302, 0, "/Interfaces/Clan Chat/close", "Close");
		interfaceCache[48302].atActionType = 3;
		rsi.child(count++, 48302, 475, 26);
		/* Clan Setup title */
		addText(18303, "Clan Setup", tda, 2, 0xFF981F, true, true);
		rsi.child(count++, 18303, 256, 26);
		/* Setup buttons */
		String[] titles = { "Clan name:", "Who can enter chat?", "Who can talk on chat?", "Who can kick on chat?",
				"Who can ban on chat?" };
		String[] defaults = { "Chat Disabled", "Anyone", "Anyone", "Anyone", "Anyone" };
		String[] whoCan = { "Anyone", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General",
				"Only Me" };
		for (int index = 0, id = 18304, y = 50; index < titles.length; index++, id += 3, y += 40) {
			addButton(id == 18304 ? 18904 : id, 2, "/Interfaces/Clan Chat/sprite", "hi");
			interfaceCache[id == 18304 ? 18904 : id].atActionType = 0;//this is why it wont display? and instead says 18904?
			if (index > 0) {
				interfaceCache[id == 18304 ? 18904 : id].actions = whoCan;
			} else {
				interfaceCache[id == 18304 ? 18904 : id].actions = new String[] { "Change title", "Delete clan" };
				;
			}
			addText(id == 18304 ? 28301 : id + 1, titles[index], tda, 0, 0xFF981F, true, true);
			addText(id == 18304 ? 28306 : id + 2, defaults[index], tda, 1, 0xFFFFFF, true, true);
			rsi.child(count++, id == 18304 ? 18904 : id, 25, y);
			rsi.child(count++, id == 18304 ? 28301 : id + 1, 100, y + 4);
			rsi.child(count++, id == 18304 ? 28306 : id + 2, 100, y + 17);
		}
		/* Table */
		addSprite(18319, 5, "/Interfaces/Clan Chat/sprite");
		rsi.child(count++, 18319, 197, 70);
		/* Labels */
		int id = 18320;
		int y = 74;
		addText(id, "Ranked Members", tda, 2, 0xFF981F, false, true);
		rsi.child(count++, id++, 202, y);
		addText(id, "Banned Members", tda, 2, 0xFF981F, false, true);
		rsi.child(count++, id++, 339, y);
		/* Ranked members list */
		RSInterface list = addInterface(id++);
		int lines = 100;
		list.totalChildren(lines);
		String[] ranks = { "Demote", "Recruit", "Corporal", "Sergeant", "Lieutenant", "Captain", "General", "Owner" };
		list.childY[0] = 2;
		// System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			addText(index, "", tda, 1, 0xffffff, false, true);
			interfaceCache[index].actions = ranks;
			list.children[index - id] = index;
			list.childX[index - id] = 2;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id - 1] + 14 : 0);
		}
		id += lines;
		list.width = 119;
		list.height = 210;
		list.scrollMax = 2000;
		rsi.child(count++, list.id, 199, 92);
		/* Banned members list */
		list = addInterface(id++);
		list.totalChildren(lines);
		list.childY[0] = 2;
		// System.out.println(id);
		for (int index = id; index < id + lines; index++) {
			if (index == 18470) {
				index++;
				id++;
			}
			addText(index, "", tda, 1, 0xffffff, false, true);
			interfaceCache[index].actions = new String[] { "Unban" };
			list.children[index - id] = index;
			list.childX[index - id] = 0;
			list.childY[index - id] = (index - id > 0 ? list.childY[index - id - 1] + 14 : 0);
		}
		id += lines;
		list.width = 119;
		list.height = 210;
		list.scrollMax = 2000;
		rsi.child(count++, list.id, 339, 92);
		/* Table info text */
		y = 47;
		addText(id, "You can manage both ranked and banned members here.", tda, 0, 0xFF981F, true, true);
		rsi.child(count++, id++, 337, y);
		addText(id, "Right click on a name to edit the member.", tda, 0, 0xFF981F, true, true);
		rsi.child(count++, id++, 337, y + 11);
		/* Add ranked member button */
		y = 75;
		addButton(id, 0, "/Interfaces/Clan Chat/plus", "Add ranked member");
		interfaceCache[id].atActionType = 5;
		rsi.child(count++, id++, 319, y);
		/* Add banned member button */
		addButton(id, 0, "/Interfaces/Clan Chat/plus", "Add banned member");
		interfaceCache[id].atActionType = 5;
		rsi.child(count++, id++, 459, y);

		/* Hovers */
//		int[] clanSetup = { 48302, 18304, 18307, 18310, 18313, 18316, 18526, 18527 };
//		String[] names = { "close", "sprite", "sprite", "sprite", "sprite", "sprite", "plus", "plus" };
//		int[] ids = { 1, 3, 3, 3, 3, 3, 1, 1 };
//		for (int index = 0; index < clanSetup.length; index++) {
//			rsi = interfaceCache[clanSetup[index]];
//			rsi.disabledHover = imageLoader(ids[index], "/Interfaces/Clan Chat/" + names[index]);
//		}
	}

	public static void Pestpanel(TextDrawingArea[] tda) {
		RSInterface RSinterface = addTab(21119);

		addText(21120, "Next Departure:", 0xCCCBCB, false, true, 52, tda, 1);
		addText(21121, "Players Ready:", 0x5BD230, false, true, 52, tda, 1);
		addText(21122, "(Need 5 to 25 players)", 0xDED36A, false, true, 52, tda, 1);
		addText(21123, "Pest Points:", 0x99FFFF, false, true, 52, tda, 1);
		int last = 4;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];

		setBounds(21120, 5, 5, 0, RSinterface);
		setBounds(21121, 5, 20, 1, RSinterface);
		setBounds(21122, 5, 35, 2, RSinterface);
		setBounds(21123, 5, 50, 3, RSinterface);

		RSInterface rsi = interfaceCache[6114] = new RSInterface();
		rsi.type = 4;
		rsi.width = 390;
		rsi.centerText = true;
	}

	public static void Pestpanel2(TextDrawingArea[] tda) {
		RSInterface RSinterface = addInterface(21100);
		addSprite(21101, 0, "Interfaces/Pest Control/PEST1");
		addSprite(21102, 1, "Interfaces/Pest Control/PEST1");
		addSprite(21103, 2, "Interfaces/Pest Control/PEST1");
		addSprite(21104, 3, "Interfaces/Pest Control/PEST1");
		addSprite(21105, 4, "Interfaces/Pest Control/PEST1");
		addSprite(21106, 5, "Interfaces/Pest Control/PEST1");
		addText(21107, "", 0xCC00CC, false, true, 52, tda, 1);
		addText(21108, "", 0x0000FF, false, true, 52, tda, 1);
		addText(21109, "", 0xFFFF44, false, true, 52, tda, 1);
		addText(21110, "", 0xCC0000, false, true, 52, tda, 1);
		addText(21111, "", 0x99FF33, false, true, 52, tda, 1);// w purp
		addText(21112, "", 0x99FF33, false, true, 52, tda, 1);// e blue
		addText(21113, "", 0x99FF33, false, true, 52, tda, 1);// se yel
		addText(21114, "", 0x99FF33, false, true, 52, tda, 1);// sw red
		addText(21115, "200", 0x99FF33, false, true, 52, tda, 1);// attacks
		addText(21116, "", 0x99FF33, false, true, 52, tda, 1);// knights hp
		addText(21117, "Time Remaining:", 0xFFFFFF, false, true, 52, tda, 0);
		addText(21118, "", 0xFFFFFF, false, true, 52, tda, 0);
		int last = 18;
		RSinterface.children = new int[last];
		RSinterface.childX = new int[last];
		RSinterface.childY = new int[last];
		setBounds(21101, 361, 27, 0, RSinterface);
		setBounds(21102, 396, 27, 1, RSinterface);
		setBounds(21103, 436, 27, 2, RSinterface);
		setBounds(21104, 474, 27, 3, RSinterface);
		setBounds(21105, 3, 21, 4, RSinterface);
		setBounds(21106, 3, 50, 5, RSinterface);
		setBounds(21107, 371, 60, 6, RSinterface);
		setBounds(21108, 409, 60, 7, RSinterface);
		setBounds(21109, 443, 60, 8, RSinterface);
		setBounds(21110, 479, 60, 9, RSinterface);
		setBounds(21111, 362, 14, 10, RSinterface);
		setBounds(21112, 398, 14, 11, RSinterface);
		setBounds(21113, 436, 14, 12, RSinterface);
		setBounds(21114, 475, 14, 13, RSinterface);
		setBounds(21115, 32, 32, 14, RSinterface);
		setBounds(21116, 32, 62, 15, RSinterface);
		setBounds(21117, 8, 88, 16, RSinterface);
		setBounds(21118, 87, 88, 17, RSinterface);
	}

	public static void godWars(TextDrawingArea[] tda) {
//		RSInterface rsinterface = addTabInterface(16210);
//		addText(16211, "NPC killcount", tda, 0, 0xff9040, true, true);
//		addText(16212, "Armadyl kills", tda, 0, 0xff9040, true, true);
//		addText(16213, "Bandos kills", tda, 0, 0xff9040, true, true);
//		addText(16214, "Saradomin kills", tda, 0, 0xff9040, true, true);
//		addText(16215, "Zamorak kills", tda, 0, 0xff9040, true, true);
//		addText(16216, "0", tda, 0, 0x66FFFF, true, true);// armadyl
//		addText(16217, "0", tda, 0, 0x66FFFF, true, true);// bandos
//		addText(16218, "0", tda, 0, 0x66FFFF, true, true);// saradomin
//		addText(16219, "0", tda, 0, 0x66FFFF, true, true);// zamorak
//		rsinterface.scrollMax = 0;
//		rsinterface.children = new int[9];
//		rsinterface.childX = new int[9];
//		rsinterface.childY = new int[9];
//		rsinterface.children[0] = 16211;
//		rsinterface.childX[0] = -52 + 375 + 30;
//		rsinterface.childY[0] = 7;
//		rsinterface.children[1] = 16212;
//		rsinterface.childX[1] = -52 + 375 + 30;
//		rsinterface.childY[1] = 30;
//		rsinterface.children[2] = 16213;
//		rsinterface.childX[2] = -52 + 375 + 30;
//		rsinterface.childY[2] = 44;
//		rsinterface.children[3] = 16214;
//		rsinterface.childX[3] = -52 + 375 + 30;
//		rsinterface.childY[3] = 58;
//		rsinterface.children[4] = 16215;
//		rsinterface.childX[4] = -52 + 375 + 30;
//		rsinterface.childY[4] = 73;
//		rsinterface.children[5] = 16216;
//		rsinterface.childX[5] = -52 + 460 + 60;
//		rsinterface.childY[5] = 31;
//		rsinterface.children[6] = 16217;
//		rsinterface.childX[6] = -52 + 460 + 60;
//		rsinterface.childY[6] = 45;
//		rsinterface.children[7] = 16218;
//		rsinterface.childX[7] = -52 + 460 + 60;
//		rsinterface.childY[7] = 59;
//		rsinterface.children[8] = 16219;
//		rsinterface.childX[8] = -52 + 460 + 60;
//		rsinterface.childY[8] = 74;
		RSInterface godwars = addInterface(16210);
		setChildren(11, godwars);
		addText(70211, "NPC Killcount", tda, 0, 0xFD851A, false, true);
		addText(70212, "Armadyl", tda, 0, 0xFD851A, false, true);
		addText(70213, "Bandos", tda, 0, 0xFD851A, false, true);
		addText(70214, "Saradomin", tda, 0, 0xFD851A, false, true);
		addText(70215, "Zamorak", tda, 0, 0xFD851A, false, true);
		addText(70216, "Zaros", tda, 0, 0xFD851A, false, true);
		addText(70217, "0", tda, 0, 0x66FFFF, false, true);
		addText(70218, "0", tda, 0, 0x66FFFF, false, true);
		addText(70219, "0", tda, 0, 0x66FFFF, false, true);
		addText(70220, "0", tda, 0, 0x66FFFF, false, true);
		addText(70221, "0", tda, 0, 0x66FFFF, false, true);

		for (int index = 0; index < 11; index++) {
			setBounds(70211 + index, index > 5 ? 480 : 400, index > 5 ? 50 + (index-5)*13:  50 + index * 13, index, godwars);
		}
	}

	public static final int BEGIN_READING_PRAYER_INTERFACE = 6;// Amount of total custom prayers we've added
	public static final int CUSTOM_PRAYER_HOVERS = 3; // Amount of custom prayer hovers we've added

	public static final int TYPE_SPRITE = 5;

	public static void addPrayerHover(TextDrawingArea[] tda, int idx, int ID, String hover, int xOffset, int yOffset) {
		// Adding hover box
		RSInterface p = addTabInterface(ID);
		p.inventoryhover = false;
		p.parentID = 5608;
		p.type = 8;
		p.width = 40;
		p.height = 32;
		p.hoverText = p.message = hover;
		p.textDrawingAreas = tda[idx];
		p.hoverXOffset = xOffset;
		p.hoverYOffset = yOffset;
		p.regularHoverBox = true;//not being used
	}
	public static void addPrayerHover_curse(TextDrawingArea[] tda, int idx, int ID, String hover, int w, int h,int xOffset, int yOffset) {
		// Adding hover box
		RSInterface p = addTabInterface(ID);
		p.inventoryhover = false;
		p.parentID = 27674;
		p.type = 8;
		p.width = 38;
		p.height = 40;
		p.hoverText = p.message = hover;
		p.textDrawingAreas = tda[idx];
		p.hoverXOffset = xOffset;
		p.hoverYOffset = yOffset;
		p.regularHoverBox = true;//not being used
	}

	/**
	 *
	 * make sure to change w and h to be your "area" that you want to be in when hovering over said "area"
	 * @param tda
	 * @param idx
	 * @param ID
	 * @param w
	 * @param h
	 * @param hover
	 * @param xOffset
	 * @param yOffset
	 */
	public static void addHover(TextDrawingArea[] tda, int idx, int ID, int w, int h, String hover,int xOffset, int yOffset) {
		// Adding hover box
		RSInterface p = addTabInterface(ID);
		p.inventoryhover = false;
		p.parentID = ID;
		p.type = 8;
		p.width = w;//how wide it can be within ur mouse
		p.height = h;//how tall it can be within ur mouse
		p.hoverText = p.message = hover;
		p.textDrawingAreas = tda[idx];
		p.hoverXOffset = xOffset;
		p.hoverYOffset = yOffset;
		p.regularHoverBox = true;//not being used
	}
	public static void prayerBook(TextDrawingArea[] tda) {

		//cheapest hax ever
		RSInterface thelogin = interfaceCache[15767];
		//Interface.children[4].sp = 15102;
		thelogin.childX[4] = 10;
		thelogin.childY[4] = 350;
		thelogin.childX[5] = 600;
		thelogin.childY[5] = 350;
//		thelogin.childX[5] = 200;
//		thelogin.childY[5] = 300;
	addSprite(15772, 0, "Interfaces/loginsplash/SPRITE");// cheap hax pumpking 1959
		addSprite(15773, 1, "Interfaces/loginsplash/SPRITE");// cheap hax
		RSInterface tab = addTabInterface(15608);
		tab.totalChildren(12);
		RSInterface prayerBook = interfaceCache[5608];

		// Switches the Chivalry & Piety positions.
		/** Chivalry **/
		int xMinus = 8;
		int minusX = 37;
		prayerBook.childX[50] = 41 - minusX;
		prayerBook.childY[50] = 195 - xMinus+8;
		prayerBook.childX[51] = 48 - minusX;
		prayerBook.childY[51] = 197 - xMinus+8;
		prayerBook.childX[63] = 41 - minusX;
		prayerBook.childY[63] = 200 - xMinus+8;
		prayerBook.childX[37] = 60;//the icon
		prayerBook.childY[37] = 225;//the icon
		prayerBook.childX[36] = 80;//the 1/1
		prayerBook.childY[36] = 226;//the 1/1
		/** Piety **/
		prayerBook.childX[52] = 41;
		prayerBook.childY[52] = 195 - xMinus+8;
		prayerBook.childX[53] = 43;
		prayerBook.childY[53] = 206 - xMinus+8;
		prayerBook.childX[64] = 48;
		prayerBook.childY[64] = 197 - xMinus+8;

		addPrayer(39401, 0, 708, 55, 26, "Preserve");
		addPrayer(39404, 0, 710, 74, 27, "Rigour");
		addPrayer(39407, 0, 712, 77, 28, "Augury");
		addPrayerHover(tda, 1, 39409,
				"Level 77\\nAugury\\nIncreases your Magic attack\\nby 25% and your defence by\\n25%", -110, -100);
		addPrayerHover(tda, 1, 39403, "Level 55\\nPreserve\\nBoosted stats last 20%\nlonger.", -135, -60);
		addPrayerHover(tda, 1, 39406,
				"Level 74\\nRigour\\nIncreases your Ranged attack\\nby 20% and damage by 23%,\\nand your defence by 25%",
				-70, -100);

		hoverButton_fromfile(127_890, "Switch", 2011,2012, "", RSInterface.newFonts[0], 0xff981f, 0xffffff, true, "Interfaces/buttons/SPRITE");
		//addConfigButton(id+1, 64000,766, 767, "Interfaces/buttons/SPRITE", 76,22, "Select", 0,1,configval);
		addText(127_891, "Switch", defaultTextDrawingAreas,0, 0xFFFFFF, true, true);


		setBounds(39401, 152, 158 - xMinus, 0, tab);
		setBounds(39404, 78, 195 - xMinus-5, 1, tab);
		setBounds(39407, 115, 195 - xMinus-5, 2, tab);
		setBounds(39402, 154, 158 - xMinus, 3, tab);
		setBounds(39405, 81, 198 - xMinus, 4, tab);
		setBounds(39408, 118, 198 - xMinus, 5, tab);
		setBounds(5608, 0, 0 - xMinus, 6, tab);
		setBounds(39403, 154, 158 - xMinus, 7, tab);
		setBounds(39406, 84, 198 - xMinus, 8, tab);
		setBounds(39409, 120, 198 - xMinus, 9, tab);
		setBounds(127_890, 62,237, 10, tab);
		setBounds(127_891,95,241, 11, tab);
	}

	public static void ancients(TextDrawingArea[] tda) {
		RSInterface p = addTabInterface(838);
		RSInterface rsinterface = interfaceCache[12855];
		// rsinterface.childX[6] = 10;
		rsinterface.childY[22] = 153;
		rsinterface.childX[22] = 18;
		rsinterface.childY[30] = 153;
		rsinterface.childX[30] = 65;
		rsinterface.childY[44] = 153; // ANNAKARL
		rsinterface.childX[44] = 112;
		rsinterface.childY[46] = 180; // GHORROCK
		rsinterface.childX[46] = 65;
		rsinterface.childY[7] = 181; // ICE BARRAGE
		rsinterface.childX[7] = 18;
		rsinterface.childY[15] = 153; // BLOOD BARRAGE
		rsinterface.childX[15] = 152;

		addSpellBig2(34674, 563, 560, 562, 10, 10, 10, 30009, 30009, 30011, 85, "Teleport to Target",
				"Teleports you near your Bounty\\nHunter Target", tda, 9, 7, 5);
		setChildren(3, p);
		setBounds(12855, 0, 0, 0, p);
		setBounds(34674, 150, 123, 1, p);
		setBounds(34675, 5, -5, 2, p);
	}

	public static void configureLunar(TextDrawingArea[] TDA) {
		constructLunar();
		homeTeleport();
		drawRune(30003, 1, "Fire");
		drawRune(30004, 2, "Water");
		drawRune(30005, 3, "Air");
		drawRune(30006, 4, "Earth");
		drawRune(30007, 5, "Mind");
		drawRune(30008, 6, "Body");
		drawRune(30009, 7, "Death");
		drawRune(30010, 8, "Nature");
		drawRune(30011, 9, "Chaos");
		drawRune(30012, 10, "Law");
		drawRune(30013, 11, "Cosmic");
		drawRune(30014, 12, "Blood");
		drawRune(30015, 13, "Soul");
		drawRune(30016, 14, "Astral");
		addLunar3RunesSmallBox(30017, 9075, 554, 555, 0, 4, 3, 30003, 30004, 64, "Bake Pie",
				"Bake pies without a stove", TDA, 0, 16, 2);
		addLunar2RunesSmallBox(30025, 9075, 557, 0, 7, 30006, 65, "Cure Plant", "Cure disease on farming patch", TDA, 1,
				4, 2);
		addLunar3RunesBigBox(30032, 9075, 564, 558, 0, 0, 0, 30013, 30007, 65, "Monster Examine",
				"Detect the combat statistics of a\\nmonster", TDA, 2, 2, 2);
		addLunar3RunesSmallBox(30040, 9075, 564, 556, 0, 0, 1, 30013, 30005, 66, "NPC Contact",
				"Speak with varied NPCs", TDA, 3, 0, 2);
		addLunar3RunesSmallBox(30048, 9075, 563, 557, 0, 0, 9, 30012, 30006, 67, "Cure Other", "Cure poisoned players",
				TDA, 4, 8, 2);
		addLunar3RunesSmallBox(30056, 9075, 555, 554, 0, 2, 0, 30004, 30003, 67, "Humidify",
				"fills certain vessels with water", TDA, 5, 0, 5);
		addLunar3RunesSmallBox(30064, 9075, 563, 557, 1, 0, 1, 30012, 30006, 68, "Moonclan Teleport",
				"Teleports you to moonclan island", TDA, 6, 0, 5);
		addLunar3RunesBigBox(30075, 9075, 563, 557, 1, 0, 3, 30012, 30006, 69, "Tele Groun Moonclan",
				"Teleports players to Moonclan\\nisland", TDA, 7, 0, 5);
		addLunar3RunesSmallBox(30083, 9075, 563, 557, 1, 0, 5, 30012, 30006, 70, "Ourania Teleport",
				"Teleports you to ourania rune altar", TDA, 8, 0, 5);
		addLunar3RunesSmallBox(30091, 9075, 564, 563, 1, 1, 0, 30013, 30012, 70, "Cure Me", "Cures Poison", TDA, 9, 0,
				5);
		addLunar2RunesSmallBox(30099, 9075, 557, 1, 1, 30006, 70, "Hunter Kit", "Get a kit of hunting gear", TDA, 10, 0,
				5);
		addLunar3RunesSmallBox(30106, 9075, 563, 555, 1, 0, 0, 30012, 30004, 71, "Waterbirth Teleport",
				"Teleports you to Waterbirth island", TDA, 11, 0, 5);
		addLunar3RunesBigBox(30114, 9075, 563, 555, 1, 0, 4, 30012, 30004, 72, "Tele Group Waterbirth",
				"Teleports players to Waterbirth\\nisland", TDA, 12, 0, 5);
		addLunar3RunesSmallBox(30122, 9075, 564, 563, 1, 1, 1, 30013, 30012, 73, "Cure Group",
				"Cures Poison on players", TDA, 13, 0, 5);
		addLunar3RunesBigBox(30130, 9075, 564, 559, 1, 1, 4, 30013, 30008, 74, "Stat Spy",
				"Cast on another player to see their\\nskill levels", TDA, 14, 8, 2);
		addLunar3RunesBigBox(30138, 9075, 563, 554, 1, 1, 2, 30012, 30003, 74, "Barbarian Teleport",
				"Teleports you to the Barbarian\\noutpost", TDA, 15, 0, 5);
		addLunar3RunesBigBox(30146, 9075, 563, 554, 1, 1, 5, 30012, 30003, 75, "Tele Group Barbarian",
				"Teleports players to the Barbarian\\noutpost", TDA, 16, 0, 5);
		addLunar3RunesSmallBox(30154, 9075, 554, 556, 1, 5, 9, 30003, 30005, 76, "Superglass Make",
				"Make glass without a furnace", TDA, 17, 16, 2);
		addLunar3RunesSmallBox(30162, 9075, 563, 555, 1, 1, 3, 30012, 30004, 77, "Khazard Teleport",
				"Teleports you to Port khazard", TDA, 18, 0, 5);
		addLunar3RunesSmallBox(30170, 9075, 563, 555, 1, 1, 7, 30012, 30004, 78, "Tele Group Khazard",
				"Teleports players to Port khazard", TDA, 19, 0, 5);
		addLunar3RunesBigBox(30178, 9075, 564, 559, 1, 0, 4, 30013, 30008, 78, "Dream",
				"Take a rest and restore hitpoints 3\\n times faster", TDA, 20, 0, 5);
		addLunar3RunesSmallBox(30186, 9075, 557, 555, 1, 9, 4, 30006, 30004, 79, "String Jewellery",
				"String amulets without wool", TDA, 21, 0, 5);
		addLunar3RunesLargeBox(30194, 9075, 557, 555, 1, 9, 9, 30006, 30004, 80, "Stat Restore Pot\\nShare",
				"Share a potion with up to 4 nearby\\nplayers", TDA, 22, 0, 5);
		addLunar3RunesSmallBox(30202, 9075, 554, 555, 1, 6, 6, 30003, 30004, 81, "Magic Imbue",
				"Combine runes without a talisman", TDA, 23, 0, 5);
		addLunar3RunesBigBox(30210, 9075, 561, 557, 2, 1, 14, 30010, 30006, 82, "Fertile Soil",
				"Fertilise a farming patch with super\\ncompost", TDA, 24, 4, 2);
		addLunar3RunesBigBox(30218, 9075, 557, 555, 2, 11, 9, 30006, 30004, 83, "Boost Potion Share",
				"Shares a potion with up to 4 nearby\\nplayers", TDA, 25, 0, 5);
		addLunar3RunesSmallBox(30226, 9075, 563, 555, 2, 2, 9, 30012, 30004, 84, "Fishing Guild Teleport",
				"Teleports you to the fishing guild", TDA, 26, 0, 5);
		addSpellBig2(30234, 563, 560, 562, 1, 1, 1, 30009, 30012, 30004, 84, "Teleport to Target",
				"Teleports you near your Bounty\\nHunter Target", TDA, 9, 7, 5);
		addLunar3RunesSmallBox(30242, 9075, 557, 561, 2, 14, 0, 30006, 30010, 85, "Plank Make", "Turn Logs into planks",
				TDA, 28, 16, 5);
		/******** Cut Off Limit **********/
		addLunar3RunesSmallBox(30250, 9075, 563, 555, 2, 2, 9, 30012, 30004, 86, "Catherby Teleport",
				"Teleports you to Catherby", TDA, 29, 0, 5);
		addLunar3RunesSmallBox(30258, 9075, 563, 555, 2, 2, 14, 30012, 30004, 87, "Tele Group Catherby",
				"Teleports players to Catherby", TDA, 30, 0, 5);
		addLunar3RunesSmallBox(30266, 9075, 563, 555, 2, 2, 7, 30012, 30004, 88, "Ice Plateau Teleport",
				"Teleports you to Ice Plateau", TDA, 31, 0, 5);
		addLunar3RunesBigBox(30274, 9075, 563, 555, 2, 2, 15, 30012, 30004, 89, "Tele Group Ice\\n Plateau",
				"\\nTeleports players to Ice Plateau", TDA, 32, 0, 5);
		addLunar3RunesBigBox(30282, 9075, 563, 561, 2, 1, 0, 30012, 30010, 90, "Energy Transfer",
				"Spend hitpoints and Energy to\\n give another player \\nhitpoints and run energy", TDA, 33, 8, 2);
		addLunar3RunesBigBox(30290, 9075, 563, 565, 2, 2, 0, 30012, 30014, 91, "Heal Other",
				"Transfer up to 75% of hitpoints\\n to another player", TDA, 34, 8, 2);
		addLunar3RunesBigBox(30298, 9075, 560, 557, 2, 1, 9, 30009, 30006, 92, "Vengeance Other",
				"Allows another player to rebound\\ndamage to an opponent", TDA, 35, 8, 2);
		addLunar3RunesSmallBox(30306, 9075, 560, 557, 3, 1, 9, 30009, 30006, 93, "Vengeance",
				"Rebound damage to an opponent", TDA, 36, 0, 5);
		addLunar3RunesBigBox(30314, 9075, 565, 563, 3, 2, 5, 30014, 30012, 94, "Heal Group",
				"Transfer up to 75% of hitpoints to a group", TDA, 37, 0, 5);
		addLunar3RunesBigBox(30322, 9075, 564, 563, 2, 1, 0, 30013, 30012, 95, "Spellbook Swap",
				"Change to another spellbook for 1\\nspell cast", TDA, 38, 0, 5);
	}

	public static void constructLunar() {
		RSInterface Interface = addInterface(29999);
		setChildren(80, Interface); // 71
		int[] Cid = { 30000, 30017, 30025, 30032, 30040, 30048, 30056, 30064, 30075, 30083, 30091, 30099, 30106, 30114,
				30122, 30130, 30138, 30146, 30154, 30162, 30170, 30178, 30186, 30194, 30202, 30210, 30218, 30226, 30234,
				30242, 30250, 30258, 30266, 30274, 30282, 30290, 30298, 30306, 30314, 30322, 30001, 30018, 30026, 30033,
				30041, 30049, 30057, 30065, 30076, 30084, 30092, 30100, 30107, 30115, 30123, 30131, 30139, 30147, 30155,
				30163, 30171, 30179, 30187, 30195, 30203, 30211, 30219, 30227, 30235, 30243, 30251, 30259, 30267, 30275,
				30283, 30291, 30299, 30307, 30315, 30323 };

		int[] xCord = { 11, 40, 71, 103, 135, 165, 8, 39, 71, 103, 135, 165, 12, 42, 71, 103, 135, 165, 14, 42, 71, 101,
				135, 168, 11, 42, 74, 103, 135, 164, 10, 42, 71, 103, 136, 165, 13, 42, 71, 104, 6, 5, 5, 5, 5, 5, 5, 5,
				5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5 };

		int[] yCord = { 9, 9, 12, 10, 12, 10, 39, 39, 39, 39, 39, 37, 68, 68, 68, 68, 68, 68, 97, 97, 97, 97, 98, 98,
				125, 124, 125, 125, 125, 126, 155, 155, 155, 155, 155, 155, 185, 185, 184, 184, 184, 176, 176, 163, 176,
				176, 176, 176, 163, 176, 176, 176, 176, 163, 176, 163, 163, 163, 176, 176, 176, 163, 176, 149, 176, 163,
				163, 176, 149, 176, 176, 176, 176, 176, 9, 9, 9, 9, 9, 9 };

		for (int i = 0; i < Cid.length; i++) {
			setBounds(Cid[i], xCord[i], yCord[i], i, Interface);
		}
	}

	public static void emoteTab() {
		RSInterface tab = addTabInterface(147);
		RSInterface scroll = addTabInterface(148);
		tab.totalChildren(1);
		tab.child(0, 148, 0, 1);
		addButton(168, 0, "Emotes/EMOTE", "Yes");
		addButton(169, 1, "Emotes/EMOTE", "No");
		addButton(164, 2, "Emotes/EMOTE", "Bow");
		addButton(165, 3, "Emotes/EMOTE", "Angry");
		addButton(162, 4, "Emotes/EMOTE", "Think");
		addButton(163, 5, "Emotes/EMOTE", "Wave");
		addButton(13370, 6, "Emotes/EMOTE", "Shrug");
		addButton(171, 7, "Emotes/EMOTE", "Cheer");
		addButton(167, 8, "Emotes/EMOTE", "Beckon");
		addButton(170, 9, "Emotes/EMOTE", "Laugh");
		addButton(83366, 10, "Emotes/EMOTE", "Jump for Joy");
		addButton(83368, 11, "Emotes/EMOTE", "Yawn");
		addButton(166, 12, "Emotes/EMOTE", "Dance");
		addButton(13363, 13, "Emotes/EMOTE", "Jig");
		addButton(13364, 14, "Emotes/EMOTE", "Spin");
		addButton(13365, 15, "Emotes/EMOTE", "Headbang");
		addButton(161, 16, "Emotes/EMOTE", "Cry");
		addButton(11100, 17, "Emotes/EMOTE", "Blow kiss");
		addButton(13362, 18, "Emotes/EMOTE", "Panic");
		addButton(13367, 19, "Emotes/EMOTE", "Raspberry");
		addButton(172, 20, "Emotes/EMOTE", "Clap");
		addButton(13369, 21, "Emotes/EMOTE", "Salute");
		addButton(13383, 22, "Emotes/EMOTE", "Goblin Bow");
		addButton(13384, 23, "Emotes/EMOTE", "Goblin Salute");
		addButton(667, 24, "Emotes/EMOTE", "Glass Box");
		addButton(6503, 25, "Emotes/EMOTE", "Climb Rope");
		addButton(6506, 26, "Emotes/EMOTE", "Lean On Air");
		addButton(666, 27, "Emotes/EMOTE", "Glass Wall");
		addButton(38464, 28, "Emotes/EMOTE", "Zombie Walk");
		addButton(38465, 29, "Emotes/EMOTE", "Zombie Dance");
		addButton(15166, 30, "Emotes/EMOTE", "Scared");
		addButton(18686, 31, "Emotes/EMOTE", "Rabbit Hop");
		addConfigButton(154, 147, 32, 33, "Emotes/EMOTE", 41, 47, "Skillcape Emote", 0, 1, 700);
		scroll.totalChildren(33);
		scroll.child(0, 168, 10, 7);
		scroll.child(1, 169, 54, 7);
		scroll.child(2, 164, 98, 14);
		scroll.child(3, 165, 137, 7);
		scroll.child(4, 162, 9, 56);
		scroll.child(5, 163, 48, 56);
		scroll.child(6, 13370, 95, 56);
		scroll.child(7, 171, 137, 56);
		scroll.child(8, 167, 7, 105);
		scroll.child(9, 170, 51, 105);
		scroll.child(10, 83366, 95, 104);
		scroll.child(11, 83368, 139, 105);
		scroll.child(12, 166, 6, 154);
		scroll.child(13, 13363, 50, 154);
		scroll.child(14, 13364, 90, 154);
		scroll.child(15, 13365, 135, 154);
		scroll.child(16, 161, 8, 204);
		scroll.child(17, 11100, 51, 203);
		scroll.child(18, 13362, 99, 204);
		scroll.child(19, 13367, 137, 203);
		scroll.child(20, 172, 10, 253);
		scroll.child(21, 13369, 53, 253);
		scroll.child(22, 13383, 88, 258);
		scroll.child(23, 13384, 138, 252);
		scroll.child(24, 667, 2, 303);
		scroll.child(25, 6503, 49, 302);
		scroll.child(26, 6506, 93, 302);
		scroll.child(27, 666, 137, 302);
		scroll.child(28, 38464, 9, 352);
		scroll.child(29, 38465, 50, 352);
		scroll.child(30, 15166, 94, 356);
		scroll.child(31, 18686, 141, 353);
		scroll.child(32, 154, 5, 401);
		scroll.width = 173;
		scroll.height = 258;
		scroll.scrollMax = 450;
	}

	public static void createInterface(int id) {
		RSInterface rsi = interfaceCache[id] = new RSInterface();
	}

	public static void addHoverButton_sprite_loader(int i, int spriteId, int width, int height, String text,
			int contentType, int hoverOver, int aT) {// hoverable
		// button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.hoverType = hoverOver;
		tab.sprite1 = Client.cacheSprite1[spriteId];
		tab.sprite2 = Client.cacheSprite1[spriteId];
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void addHoveredButton_sprite_loader(int i, int spriteId, int w, int h, int IMAGEID) {// hoverable
		// button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.hoverType = -1;
		tab.scrollMax = 0;
		addHoverImage_sprite_loader(IMAGEID, spriteId);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addSprite3(int childId, int spriteId) {
		RSInterface rsi = interfaceCache[childId] = new RSInterface();
		rsi.id = childId;
		rsi.parentID = childId;
		rsi.type = 5;
		rsi.atActionType = 0;
		rsi.contentType = 0;
		rsi.sprite1 = Client.cacheSprite3[spriteId];
		rsi.sprite2 = Client.cacheSprite3[spriteId];
		rsi.width = rsi.sprite1.myWidth;
		rsi.height = rsi.sprite2.myHeight - 2;
	}

	public static void addButton(int id, int sid, String tooltip) {
		RSInterface tab = interfaceCache[id] = new RSInterface();
		tab.id = id;
		tab.parentID = id;
		tab.type = 5;
		tab.atActionType = 1;
		tab.contentType = 0;
		tab.opacity = (byte) 0;
		tab.hoverType = 52;
		tab.sprite1 = Client.cacheSprite1[sid];// imageLoader(sid, spriteName);
		tab.sprite2 = Client.cacheSprite1[sid];// imageLoader(sid, spriteName);
		tab.width = tab.sprite1.myWidth;
		tab.height = tab.sprite1.myHeight;
		tab.tooltip = tooltip;
	}

	public static void addHoverImage_sprite_loader(int i, int spriteId) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.hoverType = 52;
		tab.sprite1 = Client.cacheSprite1[spriteId];
		tab.sprite2 = Client.cacheSprite1[spriteId];
	}

	public static void addHoverImage_sprite_loader3(int i, int spriteId) {
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = 0;
		tab.contentType = 0;
		tab.width = 512;
		tab.height = 334;
		tab.opacity = 0;
		tab.hoverType = 52;
		tab.sprite1 = Client.cacheSprite3[spriteId];
		tab.sprite2 = Client.cacheSprite3[spriteId];
	}
	private static void homewordschange(TextDrawingArea[] font) {
		RSInterface lvltext = RSInterface.interfaceCache[21756]; //level 0: home teleport
		RSInterface bottomwordstext =  RSInterface.interfaceCache[21757]; //blah blah

		RSInterface lvltext_normal =  RSInterface.interfaceCache[19220]; //level 0: home teleport
		RSInterface bottomwordstext_normal =  RSInterface.interfaceCache[19222]; //blah blah


		lvltext.message = "Teleports you Home";
		lvltext_normal.message = "Teleports you Home";

		bottomwordstext.message = "Has a 30 second cooldown.\\n";
		bottomwordstext_normal.message = "Has a 30 second cooldown.\\n ";
	}
	public static void addHoveredButton_sprite_loader3(int i, int spriteId, int w, int h, int IMAGEID) {// hoverable
		// button
		RSInterface tab = addTabInterface(i);
		tab.parentID = i;
		tab.id = i;
		tab.type = 0;
		tab.atActionType = 0;
		tab.width = w;
		tab.height = h;
		tab.isMouseoverTriggered = true;
		tab.opacity = 0;
		tab.hoverType = -1;
		tab.scrollMax = 0;
		addHoverImage_sprite_loader3(IMAGEID, spriteId);
		tab.totalChildren(1);
		tab.child(0, IMAGEID, 0, 0);
	}

	public static void addHoverButton_sprite_loader3(int i, int spriteId, int width, int height, String text,
			int contentType, int hoverOver, int aT) {// hoverable
		// button
		RSInterface tab = addTabInterface(i);
		tab.id = i;
		tab.parentID = i;
		tab.type = 5;
		tab.atActionType = aT;
		tab.contentType = contentType;
		tab.opacity = 0;
		tab.hoverType = hoverOver;
		tab.sprite1 = Client.cacheSprite3[spriteId];
		tab.sprite2 = Client.cacheSprite3[spriteId];
		tab.width = width;
		tab.height = height;
		tab.tooltip = text;
	}

	public static void equipmentTab(TextDrawingArea[] wid) {
		RSInterface Interface = interfaceCache[1644];
		addSprite(15101, 0, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15102, 1, "Interfaces/Equipment/bl");// cheap hax
		addSprite(15109, 2, "Interfaces/Equipment/bl");// cheap hax
		createInterface(21338);
		createInterface(21344);
		createInterface(21342);
		createInterface(21341);
		createInterface(21340);
		createInterface(15103);
		createInterface(15104);
		Interface.children[24] = 15102;
		Interface.childX[24] = 110;
		Interface.childY[24] = 205;
		Interface.children[25] = 15109;
		Interface.childX[25] = 39;
		Interface.childY[25] = 240;
		Interface.children[26] = 27650;
		Interface.childX[26] = 0;
		Interface.childY[26] = 0;
		Interface = addInterface(27650);
		
		addHoverButton(27651, "/Equipment/SPRITE", 6, 40, 40, "Price-checker", 550, 27652, 5);
		addHoveredButton(27652, "/Equipment/SPRITE", 7, 40, 40, 27658);
		
		addHoverButton(27653, "/Equipment/SPRITE", 8, 40, 40, "Show Equipment Stats", 550, 27655, 5);
		addHoveredButton(27655, "/Equipment/SPRITE", 9, 40, 40, 27665);

		addHoverButton(27654, "/Equipment/SPRITE", 10, 40, 40, "Show items kept on death", -1, 27657, 1);
		addHoveredButton(27657, "/Equipment/SPRITE", 11, 40, 40, 27666);
		
		addHoverButton(27660, "/Equipment/SPRITE", 12, 40, 40, "Call follower", -1, 27661, 1);
		addHoveredButton(27661, "/Equipment/SPRITE", 13, 40, 40, 27662);

		setChildren(8, Interface);
		setBounds(27651, 53, 205, 0, Interface);
		setBounds(27652, 53, 205, 1, Interface);
		setBounds(27653, 8, 205, 2, Interface);
		setBounds(27654, 98, 205, 3, Interface);
		setBounds(27655, 8, 205, 4, Interface);
		setBounds(27657, 98, 205, 5, Interface);

		setBounds(27660, 98 + 45, 205, 6, Interface);
		setBounds(27661, 98 + 45, 205, 7, Interface);
	}

	public static void itemsOnDeath(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(17100);
		addSprite(17101, new Sprite("Equipment/SPRITE 2"));

		addHover(17102, 3, 0, 10601, 1, "Interfaces/Equipment/SPRITE", 17, 17, "Close Window");
		//addHovered(10601, 3, "Interfaces/Equipment/SPRITE", 17, 17, 10602);

		closeButtonSmall(10601);
		addText(17103, "Items Kept On Death", wid, 2, 0xff981f);
		addText(17104, "Items you will keep:", wid, 1, 0xff981f);
		addText(17105, "Items you will lose:", wid, 1, 0xff981f);
		addText(17106, "On unsafe death:", wid, 1, 0xff981f);
		addText(17107, "Max items kept on death:", wid, 1, 0xffcc33);
		addText(17108, "", wid, 1, 0xffcc33);
		rsinterface.scrollMax = 0;
		rsinterface.isMouseoverTriggered = false;
		rsinterface.children = new int[12];
		rsinterface.childX = new int[12];
		rsinterface.childY = new int[12];

		rsinterface.children[0] = 17101;
		rsinterface.childX[0] = 7;
		rsinterface.childY[0] = 8;
		rsinterface.children[1] = 17102;
		rsinterface.childX[1] = 480;
		rsinterface.childY[1] = 1700;
		rsinterface.children[2] = 17103;
		rsinterface.childX[2] = 185;
		rsinterface.childY[2] = 18;
		rsinterface.children[3] = 17104;
		rsinterface.childX[3] = 22;
		rsinterface.childY[3] = 50;
		rsinterface.children[4] = 17105;
		rsinterface.childX[4] = 22;
		rsinterface.childY[4] = 110;
		rsinterface.children[5] = 17106;
		rsinterface.childX[5] = 347;
		rsinterface.childY[5] = 47;
		rsinterface.children[6] = 17107;
		rsinterface.childX[6] = 349;
		rsinterface.childY[6] = 270;
		rsinterface.children[7] = 17108;
		rsinterface.childX[7] = 398;
		rsinterface.childY[7] = 298;
		rsinterface.children[8] = 17115;
		rsinterface.childX[8] = 348;
		rsinterface.childY[8] = 64;
		rsinterface.children[9] = 10494;
		rsinterface.childX[9] = 26;
		rsinterface.childY[9] = 74;
		rsinterface.children[10] = 10600;
		rsinterface.childX[10] = 26;
		rsinterface.childY[10] = 133;
		rsinterface.children[11] = 10601;
		rsinterface.childX[11] = 480;
		rsinterface.childY[11] = 17;
		interfaceCache[10494].invSpritePadX = 8;
		interfaceCache[10600].invSpritePadX = 6;
		interfaceCache[10600].invSpritePadY = 6;
		itemsOnDeathDATA(wid);
	}

	public static void itemsOnDeathDATA(TextDrawingArea[] wid) {
		RSInterface rsinterface = addInterface(17115);

		rsinterface.parentID = 17115;
		rsinterface.id = 17115;
		rsinterface.type = 0;
		rsinterface.atActionType = 0;
		rsinterface.contentType = 0;
		rsinterface.width = 130;
		rsinterface.height = 197;
		rsinterface.aByte254 = 0;
		rsinterface.mOverInterToTrigger = -1;
		rsinterface.scrollMax = 199;
		rsinterface.totalChildren(1);
		addStringContainer(17109, 17115, Client.instance.newSmallFont,
				false, RSInterface.DEFAULT_TEXT_COLOR, true, 12, "testingy");
		rsinterface.child(0, 17109, 0, 16);

	}
	public static void unpack_clanchat(TextDrawingArea[] font) {
		clanChatTab(font);
		clanChat2ndtab(font);
		clanChat3rdtab(font);
		clanChat4thtab(font);
		//editClan(font);
	}
	public static void settings_clanchat(int button) {
		switch (button) {
			case 80051:
			case 80052:
			case 80053:
			case 80054:
				switchSettings_clanchat(button);
				break;
		}
	}
	public static void switchSettings_clanchat(int button) {
		int tab =  (button - 80051);
		int[] tabs = new int[] {18128,93800,93850,93900};//93800 all same need diffirentiate a little
		Client.instance.tabInterfaceIDs[7] = tabs[tab];//way better than the cache method below.
		//      cache[80051].children[4] = tabs[tab];
	}

	public static void clanChatTab(TextDrawingArea[] tda) {
		String dir = "/Clan Chat/new/SPRITE";
		RSInterface tab = addTabInterface(18128);

		configHoverButton1(80051, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80052,80053,80054 });
		configHoverButton1(80052, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80051,80053,80054});
		configHoverButton1(80053, "Select",  dir,2038,2038,2039,2039,false, new int[] { 80051,80052,80054 });
		configHoverButton1(80054, "Select", dir, 2038,2038,2039,2039,false, new int[] { 80051,80052,80053});

		addSprite(80055, 2040,dir);
		addSprite(80056, 2041,dir);
		addSprite(80057, 2042,dir);
		addSprite(80058, 2043,dir);

		addHoverButton(18129, "/Clan Chat/SPRITE", 6, 72, 32, "Join Clan", 550, 18130, 5);
		addHoveredButton(18130, "/Clan Chat/SPRITE", 7, 72, 32, 18131);
		addHoverButton(18132, "/Clan Chat/SPRITE", 6, 72, 32, "Clan Setup", -1, 18133, 5);
		addHoveredButton(18133, "/Clan Chat/SPRITE", 7, 72, 32, 18134);
		addText(18135, "Join Clan", tda, 0, 0xff9b00, true, true);
		addText(18136, "Clan Setup", tda, 0, 0xff9b00, true, true);
		addSprite(18137, 37, "/Clan Chat/SPRITE");
		addText(18138, "Clan Chat", tda, 1, 0xff9b00, true, true);
		addText(18139, "Talking in: Not in clan", tda, 0, 0xff9b00, false, true);
		addText(18140, "Owner: None", tda, 0, 0xff9b00, false, true);
		addSprite(16126, 2037, dir);//the line
		addSprite(33804, 196, dir);//the bg'
		addText(64890, "time", tda, 0, 0xff9b00, true, true);
		 addText(33815, "0/100", tda, 2, 0xFE9900, false, true);
		tab.totalChildren(23);
		tab.child(0, 33804, 6,58);//the bg
		tab.child(1, 16126, 0,24);//the line
		tab.child(2, 18137, 0, 6200);
		tab.child(3, 18143, 12,76);
		tab.child(4, 18129, 15, 226);
		tab.child(5, 18130, 15, 226);
		tab.child(6, 18132, 103, 226);
		tab.child(7, 18133, 103, 226);
		tab.child(8, 18135, 51, 237);
		tab.child(9, 18136, 139, 237);
		tab.child(10, 18138, 95, 1000);
		tab.child(11, 18139, 10, 42);
		tab.child(12, 18140, 25, 3800);
		tab.child(13, 80051, 4,4);
		tab.child(14, 80052, 50,4);
		tab.child(15, 80053, 96,4);
		tab.child(16, 80054, 140,4);

		tab.child(17, 80055, 18,6);
		tab.child(18, 80056, 65,6);
		tab.child(19, 80057, 112,6);
		tab.child(20, 80058, 157,6);
		tab.child(21, 33815, 60,26);
		tab.child(22, 64890, 150,42);
		/* Text area */
		RSInterface list = addTabInterface(18143);
		list.totalChildren(100);
		for (int i = 18144; i <= 18244; i++) {
			addText(i, "", tda, 0, 0xffffff, false, true);
		}
		for (int id = 18144, i = 0; id <= 18243 && i <= 99; id++, i++) {
			interfaceCache[id].actions = new String[] { "Edit Rank", "Kick", "Ban" };
			list.children[i] = id;
			list.childX[i] = 7;
			for (int id2 = 18144, i2 = 1; id2 <= 18243 && i2 <= 99; id2++, i2++) {
				list.childY[0] = 0;
				list.childY[i2] = list.childY[i2 - 1] + 15;
			}
		}
//		list.height = 158;
//		list.width = 174;
//		list.scrollMax = 1405;

		list.scrollPosition = 0;
	//	list.contentType = 0;
		list.width = 150;
		list.height = 140;
		list.scrollMax = 1490;
	}
	public static void clanChat2ndtab(TextDrawingArea[] font) {
		RSInterface widget = addInterface(93800);

		String dir = "/Clan Chat/new/SPRITE";
		addSprite(33804, 196,dir);
		addSprite(80050, 2037,dir);


		configHoverButton1(80051, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80052,80053,80054 });
		configHoverButton1(80052, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80051,80053,80054});
		configHoverButton1(80053, "Select",  dir,2038,2038,2039,2039,false, new int[] { 80051,80052,80054 });
		configHoverButton1(80054, "Select", dir, 2038,2038,2039,2039,false, new int[] { 80051,80052,80053});

		addSprite(80055, 2040,dir);
		addSprite(80056, 2041,dir);
		addSprite(80057, 2042,dir);
		addSprite(80058, 2043,dir);




		addText(98433, "Coming soon!", font, 2, 0xFE9900, false, true);
		widget.totalChildren(11);

		widget.child(0, 33804, 6,5800);




		widget.child(1, 98433, 60,26);


		widget.child(2, 80050, 0,24);
		widget.child(3, 80051, 4,4);
		widget.child(4, 80052, 50,4);
		widget.child(5, 80053, 96,4);
		widget.child(6, 80054, 140,4);


		widget.child(7, 80055, 18,6);
		widget.child(8, 80056, 65,6);
		widget.child(9, 80057, 112,6);
		widget.child(10, 80058, 157,6);
	}

	public static void clanChat3rdtab(TextDrawingArea[] font) {
		RSInterface widget = addInterface(93850);

		String dir = "/Clan Chat/new/SPRITE";
		addSprite(33804, 196,dir);
		addSprite(80050, 2037,dir);


		configHoverButton1(80051, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80052,80053,80054 });
		configHoverButton1(80052, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80051,80053,80054});
		configHoverButton1(80053, "Select",  dir,2038,2038,2039,2039,false, new int[] { 80051,80052,80054 });
		configHoverButton1(80054, "Select", dir, 2038,2038,2039,2039,false, new int[] { 80051,80052,80053});

		addSprite(80055, 2040,dir);
		addSprite(80056, 2041,dir);
		addSprite(80057, 2042,dir);
		addSprite(80058, 2043,dir);




		addText(98434, "Coming soon!", font, 2, 0xFE9900, false, true);
		widget.totalChildren(11);

		widget.child(0, 33804, 6,5800);




		widget.child(1, 98434, 60,26);


		widget.child(2, 80050, 0,24);
		widget.child(3, 80051, 4,4);
		widget.child(4, 80052, 50,4);
		widget.child(5, 80053, 96,4);
		widget.child(6, 80054, 140,4);


		widget.child(7, 80055, 18,6);
		widget.child(8, 80056, 65,6);
		widget.child(9, 80057, 112,6);
		widget.child(10, 80058, 157,6);
	}
	public static void clanChat4thtab(TextDrawingArea[] font) {
		RSInterface widget = addInterface(93900);

		String dir = "/Clan Chat/new/SPRITE";
		addSprite(33804, 196,dir);
		addSprite(80050, 2037,dir);


		configHoverButton1(80051, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80052,80053,80054 });
		configHoverButton1(80052, "Select",  dir, 2038,2038,2039,2039, false, new int[] { 80051,80053,80054});
		configHoverButton1(80053, "Select",  dir,2038,2038,2039,2039,false, new int[] { 80051,80052,80054 });
		configHoverButton1(80054, "Select", dir, 2038,2038,2039,2039,false, new int[] { 80051,80052,80053});

		addSprite(80055, 2040,dir);
		addSprite(80056, 2041,dir);
		addSprite(80057, 2042,dir);
		addSprite(80058, 2043,dir);




		//addText(98435, "Coming soon!", font, 2, 0xFE9900, false, true);
		widget.totalChildren(11);

		widget.child(0, 33804, 6,5800);




		widget.child(1, 30370, 0,0);//change this


		widget.child(2, 80050, 0,24);
		widget.child(3, 80051, 4,4);
		widget.child(4, 80052, 50,4);
		widget.child(5, 80053, 96,4);
		widget.child(6, 80054, 140,4);


		widget.child(7, 80055, 18,6);
		widget.child(8, 80056, 65,6);
		widget.child(9, 80057, 112,6);
		widget.child(10, 80058, 157,6);
	}
	public static void cataTele(TextDrawingArea[] tda) {
		RSInterface inter = addInterface(33900);
		addSprite(33901, 0, "Interfaces/Prestige/SPRITE");
		addText(33902, "Catacombs Teleports", tda, 2, 0xff9933, true, true);
		addClickableText(33903, "1. Abyssal Demon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33904, "2. Ankou", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33905, "3. Black Demons", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33906, "4. Bronze Dragon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33907, "5. Brutal Black Dragon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33908, "6. Brutal Blue Dragon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33909, "7. Brutal Red Dragon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33910, "8. Cyclops", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33911, "9. Dagannoth", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33912, "10. Dust Devil", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33913, "11. Deviant Spectre", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33914, "12. Fire Giant", "Teleport", tda, 1, 0xff9933, false, true,130);
		addClickableText(33915, "13. Ghost", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33916, "14. Greater Demon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33917, "15. Greater Nechryael", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33918, "16. Hellhound", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33919, "17. Hill Giant", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33920, "18. Iron Dragon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33921, "19. King Sand Crab", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33922, "20. Magic Axe", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33923, "21. Moss Giant", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33924, "22. Mutated Bloodveld", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33925, "23. Possessed Pickaxe", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33926, "24. Steel Dragon", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33927, "25. Shade", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33928, "26. Skeleton", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33929, "27. Twisted Banshee", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33930, "28. Warped Jelly", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addClickableText(33931, "29. Dark Beast", "Teleport", tda, 1, 0xff9933, false, true, 130);
		addHoverButton(33932, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 33932, 3);
		addHoveredButton(33933, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 33933);

		inter.totalChildren(34);
		setBounds(33901, 15, 15, 0, inter);
		setBounds(33902, 250, 19, 1, inter);
		setBounds(33903, 50, 50, 2, inter);
		setBounds(33904, 50, 70, 3, inter);
		setBounds(33905, 50, 90, 4, inter);
		setBounds(33906, 50, 110, 5, inter);
		setBounds(33907, 50, 130, 6, inter);
		setBounds(33908, 50, 150, 7, inter);
		setBounds(33909, 50, 170, 8, inter);
		setBounds(33910, 50, 190, 9, inter);
		setBounds(33911, 50, 210, 10, inter);
		setBounds(33912, 50, 230, 11, inter);
		setBounds(33913, 200, 50, 12, inter);
		setBounds(33914, 200, 70, 13, inter);
		setBounds(33915, 200, 90, 14, inter);
		setBounds(33916, 200, 110, 15, inter);
		setBounds(33917, 200, 130, 16, inter);
		setBounds(33918, 200, 150, 17, inter);
		setBounds(33919, 200, 170, 18, inter);
		setBounds(33920, 200, 190, 19, inter);
		setBounds(33921, 200, 210, 20, inter);
		setBounds(33922, 200, 230, 21, inter);
		setBounds(33923, 350, 50, 22, inter);
		setBounds(33924, 350, 70, 23, inter);
		setBounds(33925, 350, 90, 24, inter);
		setBounds(33926, 350, 110, 25, inter);
		setBounds(33927, 350, 130, 26, inter);
		setBounds(33928, 350, 150, 27, inter);
		setBounds(33929, 350, 170, 28, inter);
		setBounds(33930, 350, 190, 29, inter);
		setBounds(33931, 350, 210, 30, inter);
		setBounds(33932, 479, 18, 31, inter);
		setBounds(33933, 479, 18, 32, inter);
	}

	public static void groupLeaderboard(TextDrawingArea[] tda) {
		RSInterface inter = addInterface(39900);
		addSprite(39901, 0, "Interfaces/groupleaderboard/background");
		addText(39902, "$3000 GIM Contest Leaderboard", tda, 2, 0xff9933, true, true);
		addHoverButton(39903, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 50639, 3);
		addHoveredButton(39904, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 50640);
		//1st Text Group, top left
		addText(39905, "Collection Log Entries", tda, 1, 0xff9933, true, true);
		addText(39906, "1. Team 1", tda, 0, 0xffffff, true, true);
		addText(39907, "2. Team 2", tda, 0, 0xffffff, true, true);
		addText(39908, "3. Team 3", tda, 0, 0xffffff, true, true);
		//2nd Text Group, top right
		addText(39909, "Pets in Bank", tda, 1, 0xff9933, true, true);
		addText(39910, "1. Team 1", tda, 0, 0xffffff, true, true);
		addText(39911, "2. Team 2", tda, 0, 0xffffff, true, true);
		addText(39912, "3. Team 3", tda, 0, 0xffffff, true, true);
		//3rd Text Group, bottom left
		addText(39913, "Earned Exchange Points", tda, 1, 0xff9933, true, true);
		addText(39914, "1. Team 1", tda, 0, 0xffffff, true, true);
		addText(39915, "2. Team 2", tda, 0, 0xffffff, true, true);
		addText(39916, "3. Team 3", tda, 0, 0xffffff, true, true);
		//4th Text Group, bottom right
		addText(39917, "ToB Completions", tda, 1, 0xff9933, true, true);
		addText(39918, "1. Team 1", tda, 0, 0xffffff, true, true);
		addText(39919, "2. Team 2", tda, 0, 0xffffff, true, true);
		addText(39920, "3. Team 3", tda, 0, 0xffffff, true, true);

		inter.totalChildren(20);
		//window and title
		setBounds(39901, 71, 46, 0, inter);
		setBounds(39902, 253, 55, 1, inter);
		//close
		setBounds(39903, 404, 55, 2, inter);
		setBounds(39904, 404, 55, 3, inter);
		//1st text group, top left
		setBounds(39905, 164, 100, 4, inter);
		setBounds(39906, 164, 120, 5, inter);
		setBounds(39907, 164, 140, 6, inter);
		setBounds(39908, 164, 160, 7, inter);
		//2nd Text Group, top right
		setBounds(39909, 340, 100, 8, inter);
		setBounds(39910, 340, 120, 9, inter);
		setBounds(39911, 340, 140, 10, inter);
		setBounds(39912, 340, 160, 11, inter);
		//3rd Text Group, bottom left
		setBounds(39913, 164, 190, 12, inter);
		setBounds(39914, 164, 210, 13, inter);
		setBounds(39915, 164, 230, 14, inter);
		setBounds(39916, 164, 250, 15, inter);
		//4th Text Group, bottom right
		setBounds(39917, 340, 190, 16, inter);
		setBounds(39918, 340, 210, 17, inter);
		setBounds(39919, 340, 230, 18, inter);
		setBounds(39920, 340, 250, 19, inter);
	}

	public static void groupInformation(TextDrawingArea[] tda) {
		RSInterface inter = addInterface(50635);
		addSprite(50636, 0,"Interfaces/groupinfo/background");
		addText(50637, "Group Information", tda, 2, 0xff9933, true, true);
		addHoverButton(50638, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 50639, 3);
		addHoveredButton(50639, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 50640);
		inter.totalChildren(5);
		//window and title
		setBounds(50636, 140, 50, 0, inter);
		setBounds(50637, 263, 60, 1, inter);
		//close
		setBounds(50638, 359, 60, 2, inter);
		setBounds(50639, 359, 60, 3, inter);

		//scroll window
		setBounds(50641, 147, 85, 4, inter);

		RSInterface scroll = addInterface(50641);
		scroll.width = 214;
		scroll.height = 190;
		scroll.scrollMax = 230;
		setChildren(300, scroll);
		int rows = 8;
		for (int i = 0, n = 0; i < rows * 2; i += 2, n +=1) {
			//ROWS
			if (n % 2 == 0) {
				addSprite(50642 + i, 0, "Interfaces/groupinfo/rows");
			} else {
				addSprite(50642 + i, 1, "Interfaces/groupinfo/rows");
			}
			setBounds(50642 + i, 0, i / 2 * 24, i, scroll);
			//Type Text
			String[] Types = { "Total Level", "Total Xp", "GP Value", "Exchange Points", "Boss Points", "Slayer Points", "PK Points", "Vote Points"};
			String type = "Type";
			if (n < Types.length) {
				type = Types[n];
			}
			addText(50642 + i + 1, type, tda, 0, 0xFF981F, false, true);
			setBounds(50642 + i + 1, 10, 6 + (i / 2 * 24), i + 1, scroll);
			//Value Text
			addLeftText(50665 + i + 1, 0, 0xFFFFFF, "Value" + (1 + i / 2));
			setBounds(50665 + i + 1, 200, 6 + (i / 2 * 24), i + (rows * 2), scroll);
		}

	}

	public static void initializeTitleWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(53501);
		addSprite(53502, 0, "Interfaces/Titles/IMAGE");
		addSprite(53503, 6, "Interfaces/Titles/IMAGE");
		drawRoundedRectangle(53505, 200, 130, 0x000000, (byte) 30, true, true);
		//addButton(53506, 1, "Interfaces/Titles/IMAGE", "Close", 3, 52);
		closeButtonSmall(53506);
		addButton(53508, 7, "Interfaces/Titles/IMAGE", "Ok");
		addText(53511, "Lorem ipsum dolor sit amet,\\n"
						+ "consectetur adipiscing elit,\\n"
						+ "sed do eiusmod tempor incididunt\\n"
						+ "ut labore et dolore magna aliqua.\\n"
						+ "fUt enim ad minim veniam, quis\\n"
						+ "nostrud exercitation ullamco \\n"
						+ "laboris nisi ut aliquip ex ea\\n" + "commodo consequat.",
				tda, 1, 0xFF981F, false, true);
		addText(53512, "Purchase", tda, 1, 0xFF981F, true, true);
		drawRoundedRectangle(53513, 140, 22, 0x000000, (byte) 30, true, true);
		addText(53514, "$", tda, 2, 0xFF981F, false, true);
		addText(53515, "45,000GP", tda, 1, 0xFF981F, false, true);
		addButton(53516, 9, "Interfaces/Titles/IMAGE", "Information");
		addSprites(53517, "Interfaces/Titles/IMAGE", 11, 12, 13);
		setChildren(15, widget);
		setBounds(53502, 56, 45, 0, widget);
		setBounds(53503, 250, 80, 1, widget);
		setBounds(53505, 223, 90, 2, widget);
		setBounds(53506, 435, 51, 3, widget);
		setBounds(53508, 277, 250, 4, widget);
		setBounds(53511, 230, 100, 5, widget);
		setBounds(53512, 320, 255, 6, widget);
		setBounds(53513, 250, 224, 7, widget);
		setBounds(53514, 255, 228, 8, widget);
		setBounds(53515, 268, 228, 9, widget);
		setBounds(53516, 418, 51, 10, widget);
		setBounds(53517, 370, 221, 11, widget);
		setBounds(53549, 61, 51, 12, widget);
		setBounds(53530, 0, 0, 13, widget);
		setBounds(53535, 0, 0, 14, widget);

		RSInterface scroll = addInterface(53549);
		scroll.width = 114;
		scroll.height = 230;
		scroll.scrollMax = 750;
		setChildren(70, scroll);

		for (int i = 0; i < 70; i += 2) {
			addClickableSprites(53550 + i, "View", "Interfaces/Titles/IMAGE",
					3, 4, 5, 14);
			addText(53550 + i + 1, "Entry " + (1 + i / 2), tda, 1, 0xFF981F,
					false, true);
			setBounds(53550 + i, 0, i / 2 * 22, i, scroll);
			setBounds(53550 + i + 1, 4, 3 + (i / 2 * 22), i + 1, scroll);
		}

		widget = addInterface(53530);
		setChildren(1, widget);
		addText(53531, "Selected Title", tda, 2, 0xFF981F, true, true);
		setBounds(53531, 320, 66, 0, widget);

		widget = addInterface(53535);
		setChildren(1, widget);
		addInputField(53536, 16, 0xFF981F, "Custom title", 120, 22, false,
				true, "[A-Za-z0-9 ]");
		setBounds(53536, 262, 58, 0, widget);
	}

	public static final void initializeCommandHelp() {
		for (int childId : interfaceCache[8143].children) {
			interfaceCache[childId].message = "";
		}
	}

	public static void updateShopWidget(TextDrawingArea[] tda) {
		RSInterface widget = interfaceCache[3824];
		int[] childrenId = new int[widget.children.length + 1];
		int[] childrenX = new int[widget.children.length + 1];
		int[] childrenY = new int[widget.children.length + 1];
		for (int i = 0; i < widget.children.length; i++) {
			childrenId[i] = widget.children[i];
			childrenX[i] = widget.childX[i];
			childrenY[i] = widget.childY[i];
		}
		setChildren(93, widget);

		for (int i = 0; i < widget.children.length; i++) {
			setBounds(childrenId[i], childrenX[i], childrenY[i], i, widget);
		}
		setBounds(28050, 0, 0, 92, widget);

		RSInterface subWidget = addInterface(28050);
		setChildren(2, subWidget);
		addSprite(28051, 2, "Interfaces/BountyHunter/IMAGE");
		addText(28052, "PKP:", tda, 1, 0xFFFF00, false, true);
		setBounds(28051, 20, 30, 0, subWidget);
		setBounds(28052, 48, 30, 1, subWidget);
	}

	public static void shopWidget(TextDrawingArea[] tda) { // did it happen before the client change? nope client sided sooo here
		// Interface background
		RSInterface widget = addInterface(64000);
		setChildren(8, widget);
		addSprite(64001, 1, "Interfaces/Shop/IMAGE");
		//addHoverButton(64002, "Interfaces/Shop/IMAGE", 2, 21, 21, "Close Window", 201, 64002, 5);

		closeButtonSmall(64002);
		addText(64003, "Tony's store", tda, 2, 0xFFA500, true, true);
		setBounds(64001, 10, 10, 0, widget);
		setBounds(64015, 20, 45, 1, widget);//move this based on store
		setBounds(64002, 485, 20, 2, widget);
		setBounds(64003, 255, 20, 3, widget);
		setBounds(64017, 0, 0, 4, widget);
		setBounds(65017, 30,48, 5, widget);
		setBounds(65020, 110,48, 6, widget);
		setBounds(65023, 190,48, 7, widget);



		// Scroll
		RSInterface scroll = addInterface(64015);
		setChildren(86, scroll);
		scroll.height = 252;//change based on store
		scroll.width = 465;
		scroll.scrollMax = 800;

		// Item container
		addToItemGroup(SHOP_CONTAINER, 10, 40, 14, 16, true, "Value", "Buy 1", "Buy 5", "Buy 10", "Buy X", null);
		setBounds(SHOP_CONTAINER, 8, 8, 0, scroll);
		interfaceCache[SHOP_CONTAINER].invAlwaysInfinity = true;
//int frame = 0;
//int child = 123_000;
		int y = 41;
		int x = 20;
		for(int i = 0 ; i < 85; i++){
			addText(127_000+i, "100k", tda, 0, 0xFFFFFF, true, true);


			if(i == 0 || i == 10 || i == 20 || i == 30 || i == 40 || i == 50 || i == 60  || i == 70){
			x=22;
			}
			if(i == 1 || i == 11 || i == 21 || i == 31 || i == 41  || i == 51  || i == 61 || i == 71){
				x=70;
			}
			if(i == 2 || i == 12 || i == 22 || i == 32 || i == 42  || i == 52  || i == 62 || i == 72){
				x=115;
			}
			if(i == 3 || i == 13|| i == 23 || i == 33 || i == 43 || i == 53 || i == 63|| i == 73){
				x=160;
			}
			if(i == 4 || i == 14 || i == 24 || i == 34 || i == 44 || i == 54 || i == 64|| i == 74){
				x=208;
			}
			if(i == 5 || i == 15 || i == 25 || i == 35 || i == 45  || i == 55  || i == 65 || i == 75){
				x=254;
			}

			if(i == 6 || i == 16 || i == 26 || i == 36 || i == 46 || i == 56 || i == 66 || i == 76){
				x=300;
			}
			if(i == 7 || i == 17|| i == 27 || i == 37 || i == 47  || i == 57  || i == 67 || i == 77){
				x=345;
			}
			if(i == 8 || i == 18 || i == 28 || i == 38 || i == 48 || i == 58 || i == 68 || i == 78){
				x=390;
			}
			if(i == 9 || i == 19 || i == 29 || i == 39 || i == 49 || i == 59 || i == 69 || i == 79){
				x = 435;
				//y+=50;
			}
			setBounds(127_000+i, x,y,1+i, scroll);
			if(i == 9 || i == 19 || i == 29 || i == 39 || i == 49 || i == 59 || i == 69 || i == 79){
				//x = 435;
				y+= i == 49 ? 40 : 50;
			}
		}
//		addText(123_000, "100gp", tda, 0, 0xFFFFFF, true, true);
//		setBounds(123_000,  8, 8,1, scroll);


		// Bounty hunter widget
		RSInterface subWidget = addInterface(64017);
		setChildren(2, subWidget);
		addSprite(64018, 2, "Interfaces/BountyHunter/IMAGE");
		addText(64019, "Blood Money Store", tda, 1, 0xFFFF00, false, true);
		setBounds(64018, 20, 20, 0, subWidget);
		setBounds(64019, 48, 20, 1, subWidget);


		// shop options for some shops
 		addShopButtonInterface(65017,"runes", 1391);
		addShopButtonInterface(65020,"weapons", 1392);
		addShopButtonInterface(65023,"armor", 1393);
		//setChildren(2, shopoptions);
		//addConfigButton(65018, 64000,766, 767, "Interfaces/buttons/SPRITE", 17, 17, "Select", 0,1,1391);
	//	addText(64019, "bears", tda,0, 0xFFFFFF, true, true);


		//addConfigButton(65019, 64000,766, 767, "Interfaces/buttons/SPRITE", 17, 17, "Select", 0,1,1391);



//		setBounds(65018, 0, 0, 0, shopoptions);
//		setBounds(65018, 0, 0, 0, shopoptions);
//	setBounds(65019, 50, 0, 1, shopoptions);
	}

	public static void bountyHunterWidget(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(28000);
		addTransparentSprite(28001, 1, "Interfaces/BountyHunter/IMAGE", 20);
		addText(28003, "Target:", tda, 0, 0xFFFF00, false, true);
		addText(28004, "Abnant", tda, 1, 0xFFFFFF, true, true);
		addText(28005, "Lvl 1-4, Cmb 70", tda, 0, 0xCC0000, true, true);
		addText(28006, "Wealth: V. Low", tda, 0, 0xFFFF00, true, true);
		setChildren(15, widget);
		setBounds(28001, 320, 15, 0, widget);
		setBounds(28003, 440, 18, 1, widget);
		setBounds(28004, 458, 31, 2, widget);
		setBounds(28005, 458, 47, 3, widget);
		setBounds(28006, 359, 47, 4, widget);

		/** TODO WIldy Skull **/
		setBounds(196, Client.instance.isResized() ? 600 : 420, Client.instance.isResized() ? 186 : 286, 5, widget);

		addText(250, "", tda, 1, 0xFFFF00, true, true);
		RSInterface skullWidget = RSInterface.interfaceCache[196];
		int[] backupX = skullWidget.childX;
		int[] backupY = skullWidget.childY;

		skullWidget.children = new int[3];
		skullWidget.childX = new int[3];
		skullWidget.childY = new int[3];

		skullWidget.totalChildren(3);
		skullWidget.child(0, 194, backupX[0], backupY[0]);
		skullWidget.child(1, 195, backupX[1], backupY[1]);
		skullWidget.child(2, 250, 29, 24);

		skullWidget.width *= 1.5;

		setBounds(28030, 345, 25, 6, widget);
		setBounds(28032, 345, 25, 7, widget);
		setBounds(28034, 345, 25, 8, widget);
		setBounds(28036, 345, 25, 9, widget);
		setBounds(28038, 345, 25, 10, widget);
		setBounds(28040, 345, 25, 11, widget);
		setBounds(28020, 0, 5, 12, widget);
		setBounds(28070, 0, 5, 13, widget);

		RSInterface sprite;
		int imageId = 2;
		for (int i = 0; i < 12; i += 2) {
			sprite = addInterface(28030 + i);
			addSprite(28031 + i, imageId++, "Interfaces/BountyHunter/IMAGE");
			setChildren(1, sprite);
			setBounds(28031 + i, 0, 0, 0, sprite);
		}

		RSInterface statistics = addInterface(28020);
		setChildren(9, statistics);
		addTransparentSprite(28021, 0, "Interfaces/BountyHunter/IMAGE", 20);
		addText(28022, "Current  Record", tda, 0, 0xFFFF00, false, true);
		addText(28023, "Rogue:", tda, 0, 0xFFFF00, false, true);
		addText(28024, "Hunter:", tda, 0, 0xFFFF00, false, true);
		addText(28025, "1", tda, 0, 0xFFFF00, true, true);
		addText(28026, "2", tda, 0, 0xFFFF00, true, true);
		addText(28027, "3", tda, 0, 0xFFFF00, true, true);
		addText(28028, "4", tda, 0, 0xFFFF00, true, true);
		addSprite(28029, 8, "Interfaces/BountyHunter/IMAGE");

		setBounds(28021, 340, 58, 0, statistics);
		setBounds(28022, 420, 60, 1, statistics);
		setBounds(28023, 375, 73, 2, statistics);
		setBounds(28024, 375, 87, 3, statistics);
		setBounds(28025, 440, 73, 4, statistics);
		setBounds(28026, 440, 87, 5, statistics);
		setBounds(28027, 481, 73, 6, statistics);
		setBounds(28028, 481, 87, 7, statistics);
		setBounds(28029, 347, 74, 8, statistics);

		RSInterface timerWidget = addInterface(28070);
		addTransparentSprite(28071, 10, "Interfaces/BountyHunter/IMAGE", 20);
		addText(28072, "0:59", tda, 0, 0xff9040, true, true);
		setChildren(2, timerWidget);
		setBounds(28071, 293, 10, 0, timerWidget);
		setBounds(28072, 307, 27, 1, timerWidget);
	}

	public static void helpComponent(TextDrawingArea[] tda) {
		RSInterface widget = addInterface(59525);
		addSprite(59526, 1, "Interfaces/HelpInterface/IMAGE");
		addInputField(59527, 200, 0xFF981F, "Describe the bug you've experienced. (200 characters max)", 430, 28, false,
				false, "[A-Za-z0-9 .,]");
		addText(59528, "Help Request", tda, 2, 0xFF981F, true, true);
		addText(59529,
				"You are only allowed to request help when you need it. Situations such as being stuck,\\n"
						+ "threatened by another player, problems with a donation, or experiencing a bug are all\\n"
						+ "viable uses of the help system. Improper use of this system may lead to punishment.",
				tda, 0, 0xFF981F, false, true);
		addHoverButton(59530, "Interfaces/HelpInterface/IMAGE", 2, 16, 16, "Close", -1, 59531, 3);
		addHoveredButton(59531, "Interfaces/HelpInterface/IMAGE", 3, 16, 16, 59532);
		setChildren(6, widget);
		setBounds(59526, 33, 106, 0, widget);
		setBounds(59527, 40, 192, 1, widget);
		setBounds(59528, 256, 113, 2, widget);
		setBounds(59529, 40, 135, 3, widget);
		setBounds(59530, 456, 112, 4, widget);
		setBounds(59531, 456, 112, 5, widget);
	}

	public static void barrowsReward(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(85400);
				final int startX = 117;
		final int startY = 67;

		addSprite(64501, 487, "Interfaces/barrows/SPRITE");
		closeButtonLarge(64502);
		// Item container
		addToItemGroup(64503, 6, 2, 5, 5, false, "", "", "");

		addText(64504, "Barrows Chest", wid, 2, 0xFF981F, true, true);


		setChildren(4, tab);
		setBounds(64501, startX, startY, 0, tab);
		setBounds(64502,  startX + 252, startY + 7, 1, tab);
		setBounds(64503,  startX + 100, startY + 48,2, tab);
		setBounds(64504,  startX + 140, startY + 11,3, tab);


	}
//	public static void unpack(AdvancedFont[] font) {
//		final Widget widget = addInterface(25400);
//
//		final int startX = 117;
//		final int startY = 67;
//
//		setChildren(4, widget);
//
//		addSpriteLoader(25400 + 1, 487);
//		setBounds(25400 + 1, startX, startY, 0, widget);
//
//		closeButton(25400 + 2, 142, 143, false);
//
//		setBounds(25400 + 2, startX + 252, startY + 7, 1, widget);
//
//		addText(25400 + 5, "Barrows Chest", 0xFF981F, true, true, 52, font, 2);
//		setBounds(25400 + 5, startX + 140, startY + 11, 2, widget);
//
//		addContainer(25400 + 6, TYPE_CONTAINER, 15, 15, 10, 8, 0, false, true, true);
//		setBounds(25400 + 6, startX + 100, startY + 48, 3, widget);
//	}
	public static void bankPin(TextDrawingArea[] wid) {
		RSInterface tab = addInterface(59500);
		addSprite(59501, 0, "Interfaces/Bankpin/IMAGE");
		addText(59502, "Account Pin", wid, 2, 0xFF981F, true, true);
		addText(59503, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59504, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59505, "Enter your 4 digit code", wid, 1, 0xFF981F, true, true);
		addText(59506, "Press enter to submit", wid, 2, 0xFF981F, true, true);
		addInputField(59507, 8, 0xFF981F, "", 100, 24, true);
		addHoverButton(59508, "Interfaces/Bankpin/IMAGE", 1, 16, 16, "Close", 375, 59509, 3);
		addHoveredButton(59509, "Interfaces/Bankpin/IMAGE", 2, 16, 16, 59510);
		setChildren(9, tab);
		setBounds(59501, 256 - 140, 120, 0, tab);
		setBounds(59502, 256, 132, 1, tab);
		setBounds(59503, 256, 150, 2, tab);
		setBounds(59504, 256, 165, 3, tab);
		setBounds(59505, 256, 180, 4, tab);
		setBounds(59506, 256, 200, 5, tab);
		setBounds(59507, 256 - 50, 220, 6, tab);
		setBounds(59508, 374, 127, 7, tab);
		setBounds(59509, 374, 127, 8, tab);
	}

}