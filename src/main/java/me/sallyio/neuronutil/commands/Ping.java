package me.sallyio.neuronutil.commands;

import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public class Ping extends BaseCommand {
    public Ping() {
        this
                .setName("ping")
                .setDescription("return latency");
    }

    @Override
    public void executeSlash(@NotNull SlashCommandInteractionEvent event) {
        long latency = event.getJDA().getGatewayPing();
        event.reply("bot's gateway latency is `" + latency + "` " ).queue();
    }

    @Override
    public void executePrefix(@NotNull MessageReceivedEvent event, String[] args) {
        long latency = event.getJDA().getGatewayPing();
        event.getMessage().reply("bot's gateway latency is `" + latency + "` " ).queue();
    }
}
