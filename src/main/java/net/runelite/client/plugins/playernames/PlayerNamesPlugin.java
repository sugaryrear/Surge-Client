package net.runelite.client.plugins.playernames;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Player Names",
        description = "Displays player names above their character",
        tags = {"player", "names"},
        enabledByDefault = false
)
@Slf4j

public class PlayerNamesPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setPlayerNamesEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setPlayerNamesEnabled(true);
    }

}
