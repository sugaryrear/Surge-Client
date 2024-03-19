package com.client.graphics.interfaces.builder.impl.tasks;

import com.client.graphics.interfaces.RSInterface;

class TaskDiariesSubInterface extends TaskInterfaceSubBuilder {

    public TaskDiariesSubInterface(int baseInterfaceId, TaskInterfaceSub sub) {
        super(baseInterfaceId, sub);
    }

    @Override
    public void build() {
        addInterfaceContainer(nextInterface(), 155, 200, 550);//change stuff here about width height scrollmax - nvm
        getSub().setEntryContainer(RSInterface.get(lastInterface()));
        child(0, 0);
    }
}
