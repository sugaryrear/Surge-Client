package net.runelite.client.plugins.oldframe;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "2006 Frame",
        description = "Changes to the 2006 game frame",
        tags = {"2006", "old frame"},
        enabledByDefault = false
)
@Slf4j
public class OldframePlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setOldframeEnabled(false);


    }

    @Override
    protected void startUp() throws Exception {
        client.setOldframeEnabled(true);

    }

}