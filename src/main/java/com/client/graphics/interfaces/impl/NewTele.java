package com.client.graphics.interfaces.impl;

import com.client.TextDrawingArea;
import com.client.graphics.interfaces.RSInterface;

public class NewTele extends RSInterface {
    static int teleportbutton = 88101;
    static   int textbutton = 88101 + 30 ;
    static int descriptionbutton = 88101 + 30 + 30;
    static int favoritebutton = 88101 + 30 + 30 + 30;


    static int hoverinterface = 2080;

    public static void unpack(TextDrawingArea[] font) {
        teleportInterface(font);
    }
    public static String[] CATEGORY_NAMES = new String[]{"Favorites", "Training", "Dungeons", "Bosses", "Skilling", "Minigames", "Wilderness", "Cities"};
    private static void teleportInterface(TextDrawingArea[] font) {
        RSInterface main = addInterface(88000);
        main.totalChildren(31);

        addSprite(88001, 2061, "Interfaces/newtele/SPRITE");
        main.child(0, 88001, 1, 40);//main interface
        hoverButton_fromfile(88002, "", 1016, 1017, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "interfaces/newtele/SPRITE");
        main.child(1, 88002, 482, 50);//close button

        addText(88032, "Teleporter", font, 2, 0xFF981F, false, true);
        main.child(2, 88032, 205, 47);

        main.child(3, 88050, 142,82); // scroll
        RSInterface scroll = addInterface(88050);

        scroll.scrollMax = 1040;
        scroll.width = 340;
        scroll.height = 227;
        setChildren(120, scroll);
        int xpos = -10;
        int ypos = 0;
        int start = 0;

        for(int i = 0 ; i < 30; i ++){
            //   hoverButton10(teleportbutton + i, "Select", i % 2 == 0 ? 2075 : 2076, hoverinterface, "", font, 1, 0xdb9c22, 0xdb9c22, true, "/Interfaces/newtele/SPRITE");
            hoverButton_fromfile(teleportbutton + i, "", i % 2 == 0 ? 2075 : 2076, hoverinterface, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "interfaces/newtele/SPRITE");

//            addHoverButton(teleportbutton + i, "/Interfaces/newtele/SPRITE", i % 2 == 0 ? 2075 : 2076, 40, 40, "category", 550, teleportbutton + i + 200, 5);
//            addHoveredButton(teleportbutton + i + 200, "/Interfaces/newtele/SPRITE", hoverinterface, 40, 40, 88400+i);
            setBounds(teleportbutton + i, xpos, ypos + 1, i + start, scroll);
//            setBounds(teleportbutton + i + 200, xpos, ypos + 1, i + 30 + 30 + 30 + 30 +start, scroll);

            addText(textbutton+i, "Agility: Gnome Course", font, 2, 0xFF981F, false, true);
            setBounds(textbutton+i, xpos+18, ypos + 6, i + 30 + start, scroll);//the text

            addText(descriptionbutton+i, "Beginner course.", font, 1, 0xffffff, false, true);
            setBounds(descriptionbutton+i, xpos+18, ypos + 22, i + 30 + 30 + start, scroll);//description

            addClickableSprites(favoritebutton+i, "Toggle", "/Interfaces/newtele/SPRITE",2077, 2078);

            setBounds(favoritebutton+i, xpos+330, ypos + 10, i + 30 + 30 + 30 + start, scroll);//favorite button
            //  start++;
            ypos += 36;
        }
        int yPos = 81;//80
        for (int i = 0; i < CATEGORY_NAMES.length; i++) {
//            addHoverButton(88005 + i, "/Interfaces/newtele/SPRITE", 2064, 40, 40, "category", 550, 88400+i, 5);
//            addHoveredButton(88400+i, "/Interfaces/newtele/SPRITE", 2065, 40, 40, 88500+i);
            hoverButton_fromfile(88005 + i, "", 2064, 2065, "", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "interfaces/newtele/SPRITE");


            main.child(i + 4, 88005 + i, 13, yPos);
//            main.child(i + 4 + (CATEGORY_NAMES.length*3), 88400 + i, 13, yPos);
            //  hoverButton10(88005 + i, "Select", 2064, 2065, "", font, 1, 0xdb9c22, 0xdb9c22, true,"/Interfaces/newtele/SPRITE");

            addSprite(88014+i, 2066+i, "Interfaces/newtele/SPRITE");
            main.child(i + 4 + CATEGORY_NAMES.length, 88014 + i, 18, yPos + 6);
            addText(88023 + i, CATEGORY_NAMES[i], font, 2, 0xFF981F, false, true);
//            main.child(i + 4, 88005 + i, 13, yPos);
//            main.child(i + 4 + (CATEGORY_NAMES.length*3), 88300 + i, 13, yPos);

            main.child(i + 4 + (CATEGORY_NAMES.length*2), 88023 + i, 40, yPos + 5);
            yPos += 27;
        }


    }
}
