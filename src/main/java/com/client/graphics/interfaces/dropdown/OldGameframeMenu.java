package com.client.graphics.interfaces.dropdown;

import com.client.Client;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class OldGameframeMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
            Client.getUserSettings().setOldGameframe(optionSelected == 0);
			Client.instance.loadTabArea();
			Client.instance.drawTabArea();
    }
}
