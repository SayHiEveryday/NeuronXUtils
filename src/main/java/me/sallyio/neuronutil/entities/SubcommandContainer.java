package me.sallyio.neuronutil.entities;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;

import java.util.HashMap;
import java.util.Map;

public class SubcommandContainer {
    private String name;
    private String description;

    private Map<String, BaseCommand> baseCommands = new HashMap<>();
    public SubcommandContainer(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public SubcommandContainer addCommand(BaseCommand cmd) {
        this.baseCommands.put(cmd.getName(),cmd);
        return this;
    }

    public BaseCommand getCommand(String name) {
        return this.baseCommands.get(name);
    }

    public CommandData build() {
        CommandDataImpl data = new CommandDataImpl(this.name,this.description);
        this.baseCommands.forEach((i,v) -> {
            SubcommandData subdata = new SubcommandData(v.getName(), v.getDescription());
            if (!v.getOption().isEmpty()) {
                subdata.addOptions(v.getOption());
            }
            data.addSubcommands(subdata);
        });
        return data;

    }
}
