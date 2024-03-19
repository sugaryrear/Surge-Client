package net.runelite.client.plugins.broadcasts;



import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Broadcasts",
        description = "Enable/Disable ingame broadcasts",
        tags = {"broadcasts"},
        enabledByDefault = true
)
@Slf4j

public class BroadcastsPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setbroadcastsEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setbroadcastsEnabled(true);
    }

}