package me.sallyio.neuronutil.commands.admin;

import me.sallyio.PandaKey.rest.RestClient;
import me.sallyio.neuronutil.common.UniversalToken;
import me.sallyio.neuronutil.entities.SubcommandContainer;

public class AdminContainer extends SubcommandContainer {
    public AdminContainer() {
        super("admin", "Command that admin related");
        final RestClient client = new RestClient(UniversalToken.Panda_KEY);
        this
                .addCommand(new ResetHwid())
                .addCommand(new Genkey(client));
    }
}
