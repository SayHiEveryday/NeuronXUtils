package me.sallyio.neuronutil.commands.misc;

import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Steal extends BaseCommand {
    public Steal() {
        this
                .setName("steal")
                .setDescription("abc");
    }

    @Override
    public void executePrefix(@NotNull MessageReceivedEvent event, String[] args) {
        if (args[1] == null) {
            event.getMessage().reply("Usage [prefix]steal [emoji|sticker] [emoji or sticker to steal]").queue();
            return;
        }
        switch (args[1].toLowerCase()) {
            case "emoji":
                Pattern pattern = Pattern.compile("<:([a-zA-Z0-9_]+):(\\d+)>");
                Matcher matcher = pattern.matcher(args[2]);
                if (!matcher.find()) {
                    event.getMessage().reply(args[2] + " is not an emoji").queue();
                    return;
                }
                event.getMessage().reply("https://cdn.discordapp.com/emojis/" + matcher.group(2) + ".png").queue();
                break;
            case "sticker":
                MessageCreateBuilder builder = new MessageCreateBuilder();
                event.getMessage().getStickers().forEach((v) -> {
                    builder.addContent(v.getIcon().getUrl());
                });
                event.getMessage().reply(builder.build()).queue();
                break;
            default:
                event.getMessage().reply("Invalid type please provide emoji or sticker").queue();
                break;
        }

    }
}
