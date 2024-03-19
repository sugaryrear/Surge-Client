package com.client.graphics.interfaces.dropdown;

import com.client.Configuration;
import com.client.graphics.interfaces.MenuItem;
import com.client.graphics.interfaces.RSInterface;

public class PlayerAttackOptionMenu implements MenuItem {
    @Override
    public void select(int optionSelected, RSInterface rsInterface) {
        Configuration.playerAttackOptionPriority = optionSelected;
    }
}
