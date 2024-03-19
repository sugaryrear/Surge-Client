package net.runelite.client.plugins.npcnames;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "NPC Names",
        description = "Displays the NPC name above them",
        tags = {"npc", "names"},
        enabledByDefault = false
)
@Slf4j

public class NPCNamesPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setNPCNamesEnabled(false);

    }

    @Override
    protected void startUp() throws Exception {
        client.setNPCNamesEnabled(true);
    }

}
