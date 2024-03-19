package net.runelite.client.plugins.loginsplash;



import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "Login Screen",
        description = "Enable/Disable the login screen splash",
        tags = {"broadcasts"},
        enabledByDefault = true
)
@Slf4j
public class LoginSplashPlugin extends Plugin {
    @com.google.inject.Inject
    private Client client;

    @javax.inject.Inject
    private ClientThread clientThread;

    @Override
    protected void shutDown() throws Exception {
        client.setLoginSplashEnabled(false);
        //  client.setGameState(1);

    }

    @Override
    protected void startUp() throws Exception {
        client.setLoginSplashEnabled(true);
        //    client.setGameState(1);
    }

}