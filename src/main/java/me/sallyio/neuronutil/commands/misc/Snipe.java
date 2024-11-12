package me.sallyio.neuronutil.commands.misc;

import me.sallyio.neuronutil.entities.BaseCommand;
import me.sallyio.neuronutil.entities.DeleteMessageCache;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

public class Snipe extends BaseCommand {
    public Snipe() {
        this
                .setName("snipe")
                .setDescription("snipe just that");
    }

    @Override
    public void executePrefix(@NotNull MessageReceivedEvent event, String[] args) {
        DeleteMessageCache cache = DeleteMessageCache.getInstance();
        Message message = cache.get(event.getChannel().getId());

        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (message == null) {
            event.getMessage().reply("No recent message delete found for this channel").queue();
            return;
        }
        if (message.isVoiceMessage()) {
            event.getMessage().reply("I can't snipe a voice message").queue();
            return;
        }
        embedBuilder.setDescription(message.getContentRaw())
                .setColor(0xFFFFFF)
                .setAuthor(message.getAuthor().getEffectiveName(),null, message.getAuthor().getAvatarUrl())
                .setFooter("Sent on: " + message.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        messageBuilder.addEmbeds(embedBuilder.build());
        if (!message.getAttachments().isEmpty()) {
            message.getAttachments().forEach((v) -> {
                if (v.isImage() || v.isVideo()) {
                    messageBuilder.addEmbeds(new EmbedBuilder().setImage(v.getUrl()).setDescription(v.getDescription()).build());
                }
            });
        }
        if (!message.getEmbeds().isEmpty()) {
            message.getEmbeds().forEach(messageBuilder::addEmbeds);
        }
        event.getMessage().reply(messageBuilder.build()).queue();
    }

    @Override
    public void executeSlash(SlashCommandInteractionEvent event) {
        DeleteMessageCache cache = DeleteMessageCache.getInstance();
        Message message = cache.get(event.getChannel().getId());

        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (message == null) {
            event.reply("No recent message delete found for this channel").setEphemeral(true).queue();
            return;
        }
        if (message.isVoiceMessage()) {
            event.reply("I can't snipe a voice message").setEphemeral(true).queue();
            return;
        }
        embedBuilder.setDescription(message.getContentRaw())
                .setColor(0xFFFFFF)
                .setAuthor(message.getAuthor().getEffectiveName(), message.getAuthor().getAvatarUrl())
                .setFooter("Sent on: " + message.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME));
        messageBuilder.addEmbeds(embedBuilder.build());
        if (!message.getAttachments().isEmpty()) {
            message.getAttachments().forEach((v) -> {
                if (v.isImage() || v.isVideo()) {
                    messageBuilder.addEmbeds(new EmbedBuilder().setImage(v.getUrl()).setDescription(v.getDescription()).setColor(0xFFFFFF).build());
                }
            });
        }
        if (!message.getEmbeds().isEmpty()) {
            message.getEmbeds().forEach(messageBuilder::addEmbeds);
        }
        event.reply(messageBuilder.build()).queue();
    }
}
