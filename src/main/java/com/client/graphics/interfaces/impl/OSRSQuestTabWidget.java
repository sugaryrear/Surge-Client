package com.client.graphics.interfaces.impl;


import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;

public class OSRSQuestTabWidget extends RSInterface {
    public static void unpack(TextDrawingArea[] fonts) {
        mainTab(fonts);
        playerPanel(fonts);
        mainDiaryTab(fonts);
    }
    public static void settings(int button) {
        switch (button) {
            case 10404:
            case 10405:
            case 10406:
            case 10407://first one
                switchSettings(button);
                break;
        }
    }

    public static void switchSettings(int button) {
        int tab =  (button - 10404);
        int[] tabs = new int[] {32541,32551,32561,32571};
        interfaceCache[10220].children[5] = tabs[tab];
    }

public static String dir = "Interfaces/osrsquesttab/SPRITE";
    public static void mainTab(TextDrawingArea[] tda) {
        RSInterface tab = addTabInterface(10220);

        addSprite(80140, 2037, dir);

        configHoverButton1(10407, "Select", dir, 1916,1917,1918,1918, false, new int[] { 10404, 10405, 10406 });
        configHoverButton1(10404, "Select", dir, 1919,1920,1921,1921, false, new int[] { 10407, 10405, 10406 });
        configHoverButton1(10405, "Select", dir,1922,1923,1924,1924,false, new int[] { 10407, 10404, 10406 });
        configHoverButton1(10406, "Select",dir, 1925,1926,1927,1927,false, new int[] { 10407, 10404, 10405 });

        tab.totalChildren(6);
        tab.child(0, 80140, 0,23); //begin buttons
        tab.child(1, 10407, 7, 4); //begin buttons
        tab.child(2, 10404, 52, 3);
        tab.child(3, 10405, 97, 3);
        tab.child(4, 10406, 140, 3);
        tab.child(5, 32571, 0, 12);//this is the child that changes - see initially with nothing pressed its 32571

        RSInterface questtab = addInterface(32541);
        RSInterface diarytab = addInterface(32551);
        RSInterface minigamestab = addInterface(32561);
        questTab(questtab);
        diaryTab(diarytab , tda);
        minigamesTab(minigamestab);
    }
    public static void diaryTab(RSInterface achievetab,TextDrawingArea[] tda) {
        setChildren(1,achievetab);
        setBounds(29465, 0, 20, 0, achievetab);
    }
    public static void questTab(RSInterface questtab) {
        setChildren(3, questtab);


        addSprite(32542, 1915,dir);
        setBounds(32542, 3, 31, 0, questtab);
        setBounds(639, 0, 35, 1, questtab);
        setBounds(640, 5, 14, 2, questtab);

        RSInterface.interfaceCache[7332].message = "Black Knight's Fortress";
        RSInterface.interfaceCache[639].height = 205;

RSInterface rsi = RSInterface.interfaceCache[639];
	for(int i = 0 ; i < rsi.children.length; i++){
	rsi.childX[i] = 500;
	rsi.childY[i]  = 0;
}

rsi.childX[69] = 12;
	rsi.childY[69]  = 10;

        rsi.childX[101] = 12;
        rsi.childY[101]  = 34;

        rsi.childX[66] = 12;
        rsi.childY[66]  = 22;
	
    }
    public static void mainDiaryTab(TextDrawingArea[] tda) {//button:83097
        RSInterface interface_ = addTabInterface(29465);

      // addSprite(29466, 1, "Interfaces/Diary/BACKGROUND"); //frame
       addSprite(29466, 1940,dir);

//		hoverButton10(29467, "Quests", 2, 22, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"Interfaces/QuestTab/QUEST");
        // hoverButton10(29467, "Quests", 554,555, "", RSInterface.newFonts[1], 16711680, 0xffffff, false,"Interfaces/QuestTab/QUEST");

        addText(29469, "Diaries completed: ", tda, 2, 0xff9933, false, true);
//
//        addDiarySlot(29471, tda, "Ardougne");
//        addDiarySlot(29481, tda, "Desert");
//        addDiarySlot(29491, tda, "Falador");
//        addDiarySlot(29501, tda, "Fremennik");
//        addDiarySlot(29511, tda, "Kandarin");
//        addDiarySlot(29521, tda, "Karamja");
//        addDiarySlot(29531, tda, "Lumbridge & Draynor");
//        addDiarySlot(29541, tda, "Varrock");
//        addDiarySlot(29551, tda, "Western Provinces");
//        addDiarySlot(29561, tda, "Wilderness");
//        addDiarySlot(29571, tda, "Morytania");
//        addDiarySlot(63571, tda, "Kourend & Kebos");

        int intChild = 0;
        RSInterface widgetforquesttab = addInterface(29470);
        interface_.totalChildren(3);
        interface_.child(intChild++, 29470, 10, 21);//the inside interface  - child 0
      interface_.child(intChild++, 29469, 8,-2);//the "achievement diary" text
        interface_.child(intChild++, 29466, 3,15);//frame

//
//        Widget scrolls = addInterface(29470);
//
//        scrolls.width = 157;
//        scrolls.height = 195;
//        scrolls.scrollMax = 352;
//
//        scrolls.totalChildren(84);
//
//        int id=29471;
//        int y = -5;
//        int x = 3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);//easy
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);//md
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);//hard
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);//elite
//
//        id=29481;
//        y=28-5;
//        //x+=3;
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29491;
//        y=56-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//
//        id=29501;
//        y=84-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29511;
//        y=112-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29521;
//        y=140-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//
//        id=29531;
//        y=168-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29541;
//        y=196-5;
//        //	x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29551;
//        y=224-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29561;
//        y=252-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=29571;
//        y=280-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);
//
//        id=63571;
//        y=308-5;
//        //x+=3;
//
//        scrolls.child(intChild2++, id+1, 4, 7+y);
//        scrolls.child(intChild2++, id+2, 4+x, 9+y);
//        scrolls.child(intChild2++, id+3, 152+x, 9+y);
//        scrolls.child(intChild2++, id+4, 4+x, 25+y);
//        scrolls.child(intChild2++, id+5, 40+x, 25+y);
//        scrolls.child(intChild2++, id+6, 76+x, 25+y);
//        scrolls.child(intChild2++, id+7, 112+x, 25+y);


    }
    public static void playerPanel(TextDrawingArea[] tda) {
        RSInterface dailytaskstab = addTabInterface(32571);

        setChildren(15, dailytaskstab);

        //addSprite(32119, 0, "Interfaces/playerInfoTab/SPRITE");
        addSprite(32119, 19388,dir);

        addText(32580, "Name", tda, 2, 0xFF981F, true, true);
        addText(32572, "Combat level:", tda, 0,0x00b200, false, true);
     //   hoverButton_fromfile(72651, "Select", 1,0, "Teleports", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "Interfaces/Generic/button");
        addText(32573, "total level:", tda, 0,0x00b200, true, true);
        addText(32574, "total exp:", tda, 0,0x00b200, true, true);

        // hoverButton_fromfile(72651, "Select", 1,0, "Vote", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);


        //setBounds(72650, 8,20, 0, minigamestab);

        hoverButton_fromfile(68900, "Select", 2018,2019, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);
        addText(32575, "quests completed amt", tda, 0,0x00b200, true, true);
        hoverButton_fromfile(78901, "Select", 2020,2021, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);

    //    interfaceCache[78901].newButtonClicking = true;
     //   hoverButton10(78901, "Select", 2020,2021, "", tda,1, 0xFF981F, 0xffffff, false);
        addText(32576, "collection log amt", tda, 0,0x00b200, true, true);

        hoverButton_fromfile(78902, "Select", 2022,2023, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);
        interfaceCache[78901].newButtonClicking = true;
 //       hoverButton10(78902, "Select", 2022,2023, "", tda,1, 0xFF981F, 0xffffff, false);
        addText(32577, "combat achievement amt", tda, 0,0x00b200, true, true);
    //    hoverButton10(78904, "Select", 2026,2027, "", tda,1, 0xFF981F, 0xffffff, false);
        hoverButton_fromfile(78904, "Select", 2026,2027, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);
       // interfaceCache[78904].newButtonClicking = true;
        //addClickableText(32578, "click to reveal playtime", "Select", tda, 0,0xFF981F, false, true, 100);
        hoverButton_fromfile(78903, "Select", 2024,2025, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, dir);

        interfaceCache[78903].newButtonClicking = true;
  //     hoverButton10(78903, "Select", 2024,2025, "", tda,1, 0xFF981F, 0xffffff, false);

        addText(32590, "npc drops.", tda, 0,0x00b200, true, true);
 //       addClickableText(32590, "npc drops amt", "Select", tda, 0,0x00b200, true, true, 40);
       addText(32578, "Time played: Click to reveal.", tda, 0,0xFF981F, true, true);

        // addText(32581, "Collections\\nLogged:", tda, 0,0xFF981F, true, true);

        setBounds(32119, 4, 32, 0, dailytaskstab);
        setBounds(68900,4,106, 1, dailytaskstab);
        setBounds(78901,97,106, 2, dailytaskstab);
        setBounds(78902,4,162, 3, dailytaskstab);
        setBounds(78903,97,162, 4, dailytaskstab);
        setBounds(78904,4,218, 5, dailytaskstab);
      //  setBounds(72651, 11,30, 6, dailytaskstab);
        setBounds(32572,55,55, 6, dailytaskstab);
        setBounds(32573, 158,55, 7, dailytaskstab);//total lvl
        setBounds(32574, 140,85, 8, dailytaskstab);//total xp
        setBounds(32575, 60,140, 9, dailytaskstab);//quest completed amt
        setBounds(32576, 155,140, 10, dailytaskstab);
        setBounds(32577, 60,196, 11, dailytaskstab);

        setBounds(32578, 100,226, 12, dailytaskstab);
        setBounds(32580, 100,13, 13, dailytaskstab);
        setBounds(32590, 150,196, 14, dailytaskstab);
    }
    public static void minigamesTab(RSInterface minigamestab) {
        setChildren(1, minigamestab);
        //	addSprite(32566, 0, "Interfaces/minigamesTab/SPRITE");
        //    addSprite(32566, 1933);

        //  addText(32565, "Minigames", tda, 2, 0xFF981F, false, true);
        // addText(32567, "Choose a selection from the\\n dropdown menu", tda, 1, 0xFF981F, true, true);
        // addText(32568, "Suggested players:", tda, 0, 0xFF981F, false, true);
        //addText(32569, "- - -", tda, 0, 0xFF981F, false, true);

        //hoverButton10(32564, "Teleport", 0, 1, "", RSInterface.newFonts[1], 0xFF981F, 0xffffff, false,"/Interfaces/minigamesTab/TELEPORT");
        //  hoverButton10(32564, "Select", 448,447, "Vote", tda,1, 0xFF981F, 0xffffff, true);

        // hoverButton10(32565, "Select", 448,447, "Donate", tda,1, 0xFF981F, 0xffffff, true);
        //hoverButton10(32566, "Select", 448,447, "Leaderboards", tda,1, 0xFF981F, 0xffffff, true);
        // hoverButton10(32567, "Select", 448,447, "Latest Updates", tda,1, 0xFF981F, 0xffffff, true);


//        hoverButton10(32564, "Teleport", 1934,1935, "", tda,1, 0xFF981F, 0xffffff, false);

//		dropdownMenu(32563, 172,-2, new String[]{"Barrows", "Chambers of Xeric", "Duel Arena", "Fight Pits", "Pest Control", "Mage Arena",
//				"Inferno", "Shayzien Assault", "Warriors Guild", "The Gauntlet", "Theatre of Blood"}, Dropdown.MINIGAMES, tda, 1);
//		hoverButton10(32564, "Teleport", 2, 1, "Teleport", RSInterface.newFonts[1], 0xFF981F, 0xffffff, false,"/Interfaces/Buttons/HOVERBUTTON");
        //	addText(32567, "Bosses", tda, 2, 0xFF981F, false, true);
//		dropdownMenu(32562, 172,-2, new String[]{"Abyssal Sire", "Corporeal Beast", "Cerberus", "Dagannoth Kings", "The Nightmare", "God Wars",
//				"Kraken", "Vorkath", "Zulrah", "Barrelchest"}, Dropdown.BOSSES, tda, 1);
        //addText(32568, "Training", tda, 2, 0xFF981F, false, true);
//        dropdownMenu(32570, 172, -2,new String[]{"Rock Crabs", "Slayer Tower", "Chasm of Fire", "Desert Bandits", "Ancient Cavern",
//                "Fremennik Dungeon", "Kourend Catacombs", "Karuulm Dungeon", "Lithkren Vault"
//                }, Dropdown.TRAINING, tda, 1);

//		dropdownMenu(57910, 172, -2,new String[]{"East Drags", "West Drags", "Chaos Temple", "Revenant Cave", "Demonic Ruins", "Last Man Standing", "Forgotten Cemetary"
//		}, Dropdown.PK_LOCATIONS, tda, 1);
String dir =        "Interfaces/Generic/button";

        RSInterface widgetforquesttab = addInterface(72650);

        setBounds(72650, 10,21, 0, minigamestab);



    }

}
