package me.sallyio.neuronutil.commands.admin;

import me.sallyio.PandaKey.rest.RestClient;
import me.sallyio.neuronutil.entities.SubcommandContainer;

public class AdminContainer extends SubcommandContainer {
    public AdminContainer() {
        super("admin", "Command that admin related");
        this
                .addCommand(new ResetHwid());
    }
}
