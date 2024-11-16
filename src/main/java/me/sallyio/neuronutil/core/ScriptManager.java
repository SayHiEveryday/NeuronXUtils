package me.sallyio.neuronutil.core;

import me.sallyio.neuronutil.common.ScriptHandler;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.List;

public class ScriptManager extends ListenerAdapter {

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        String[] command = event.getFullCommandName().split(" ");
        if (!command[0].equals("script")) return;

        ScriptHandler sh = new ScriptHandler("ScriptData.json");
        List<Command.Choice> choices = new ArrayList<>();
        sh.getAllKeys()
                .stream().filter(p -> p.contains(event.getOption("script", OptionMapping::getAsString)))
                .forEach((v) -> {
                    choices.add(new Command.Choice(v, v));
                });
        event.replyChoices(choices).queue();
    }
}
