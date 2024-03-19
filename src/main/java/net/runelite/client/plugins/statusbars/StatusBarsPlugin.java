package net.runelite.client.plugins.statusbars;



import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Status Bars",
        description = "Shows your health and prayer status",
        tags = {"status", "bars", "prayer", "hp"},
        enabledByDefault = false
)
@Slf4j

public class StatusBarsPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setStatusBarsEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setStatusBarsEnabled(true);
    }

}