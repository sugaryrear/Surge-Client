package net.runelite.client.plugins.oldframe;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Ground Item Names",
        description = "Turns on gorund item names",
        tags = { "item name"},
        enabledByDefault = false
)
@Slf4j
public class GroundItemNamesPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setgroundnamesEnabled(false);
        //  client.setGameState(1);

    }

    @Override
    protected void startUp() throws Exception {
        client.setgroundnamesEnabled(true);
        //    client.setGameState(1);
    }

}