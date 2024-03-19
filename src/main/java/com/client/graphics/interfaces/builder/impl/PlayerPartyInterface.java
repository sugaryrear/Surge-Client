package com.client.graphics.interfaces.builder.impl;

import com.client.Sprite;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.builder.InterfaceBuilder;

public class PlayerPartyInterface extends InterfaceBuilder {

    private final Sprite background = new Sprite("/Clan Chat/SPRITE 37");
    private final Sprite button = new Sprite("/Clan Chat/Sprite 6");
    private final Sprite divider = new Sprite("/Clan Chat/Sprite 4");

    public PlayerPartyInterface() {//let me guess. it overwrites hella interfaces and thats why spellhovers dont have black background ?
        super(30370);
    }

    @Override
    public void build() {
        addSprite(nextInterface(), background);
        child(0, 62);

        int headerY = 27;
        addText(nextInterface(), 1, RSInterface.DEFAULT_TEXT_COLOR, true, "Chamber of Xeric");
        child(95, headerY);

        addText(nextInterface(), 1, RSInterface.DEFAULT_TEXT_COLOR, true, "Leader: <img=1> Michael");
        child(95, 42);

        // Invite
        int buttonX = 58;
     //   addButton(nextInterface(), button, "Select", 4);
        hoverButton_fromfile(nextInterface(), "Select", 6,7,"", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "/Clan Chat/Sprite");

        child(buttonX, 226);
      //  hoverButton_fromfile(nextInterface(), "Claim", 2110,2111,"", RSInterface.newFonts[1], 0xff981f, 0xffffff, true, "tasks/SPRITE");
        addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, true, "Leave");
        child(buttonX + 36, 237);

        // Divider aka the horizonal lines on top and bottom of the scroll
        RSInterface dividerInterface = addSprite(nextInterface(), divider);
        child(dividerInterface.id, 0, 221);
        child(dividerInterface.id, 0, 59);

        RSInterface container = addInterface(nextInterface());
        child(6, 62);
        container.height = 158;
        container.width = 168;
        container.scrollMax = 1405;
        container.totalChildren(200);
        int childId = 0;

        for (int i = 0; i < 100; i++) {
            RSInterface text = addText(nextInterface(), 0, RSInterface.DEFAULT_TEXT_COLOR, false, "<img=3> User " + i);
            text.width = 174;
            text.height = 12;
            container.child(childId++, lastInterface(), 0, i * 13);
            text.actions = new String[] { "Kick", };
        }

        getRoot().setNewButtonClicking();
    }
}
