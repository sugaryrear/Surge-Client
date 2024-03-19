package net.runelite.client.plugins.playertile;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Player Tile Indicator",
        description = "Shows your destination tile",
        tags = {"highlight", "minimap", "player", "overlay", "tags"},
        enabledByDefault = false
)
@Slf4j

public class PlayerTilePlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setPlayerTileEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setPlayerTileEnabled(true);
    }

}