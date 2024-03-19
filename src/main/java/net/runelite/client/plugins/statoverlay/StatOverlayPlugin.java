package net.runelite.client.plugins.statoverlay;


import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Restore Overlay",
        description = "Shows how much time is left until your stats are reduced",
        tags = {"stat", "overlay", "restore", "overlay"},
        enabledByDefault = true
)
@Slf4j

public class StatOverlayPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setstatoverlayEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setstatoverlayEnabled(true);
    }

}