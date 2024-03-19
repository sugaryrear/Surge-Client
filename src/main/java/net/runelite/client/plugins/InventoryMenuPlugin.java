package net.runelite.client.plugins;



import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Inventory Menu",
        description = "Displays hoverable menus over items in your inventory",
        tags = {"inventory","items"},
        enabledByDefault = true
)
@Slf4j

public class InventoryMenuPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setInventoryMenuEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setInventoryMenuEnabled(true);
    }

}