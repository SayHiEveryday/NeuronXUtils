package me.sallyio.neuronutil.commands;

import me.sallyio.neuronutil.common.ScriptHandler;
import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

public class Script extends BaseCommand {
    public Script() {
        this
                .setName("script")
                .setDescription("Get your script");
        OptionData data = new OptionData(OptionType.STRING, "script", "search script", true)
                .setAutoComplete(true);
//        new ScriptHandler("ScriptData.json").getAllKeys().forEach(i -> {
//            data.addChoice(i,i);
//        });
        this.addOption(data);
    }

    @Override
    public void executeSlash(@NotNull SlashCommandInteractionEvent event) {
        String scriptName = event.getOption("script").getAsString();
        ScriptHandler handler = new ScriptHandler("ScriptData.json");
        if (!handler.getEntry(scriptName).get("available").asBoolean()) {
            event.reply("Script is currently unavailable").setEphemeral(true).queue();
            return;
        }
        event.replyEmbeds(new EmbedBuilder()
                .setDescription("```lua"
                        + "\n" + handler.getEntry(scriptName).get("script").textValue() + "\n"
                        + "```"
                )
                .addField("Mobile Copy", handler.getEntry(scriptName).get("script").textValue(), false)
                .setColor(0xFFFFFF)
                .build()
        ).setEphemeral(true).queue();
    }
}
