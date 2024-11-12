package me.sallyio.neuronutil.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.Channel;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class TicketManager extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TicketManager.class);

    public String getTicket_category() {
        return ticket_category;
    }

    public TicketManager setTicket_category(String ticket_category) {
        this.ticket_category = ticket_category;
        return this;
    }

    public String getTicket_channel_id() {
        return ticket_channel_id;
    }

    public TicketManager setTicket_channel_id(String ticket_channel_id) {
        this.ticket_channel_id = ticket_channel_id;
        return this;
    }

    private String ticket_channel_id;
    private String ticket_category;
    private Guild guild;

    public String getTicketLog() {
        return ticketLog;
    }

    public TicketManager setTicketLog(String ticketLog) {
        this.ticketLog = ticketLog;
        return this;
    }

    private String ticketLog;

    @Override
    public void onReady(ReadyEvent event) {
        this.guild = event.getJDA().getGuildById(1273163123842351185L);

        TextChannel ticketChannel = guild.getChannelById(TextChannel.class, this.ticket_channel_id);

        if (ticketChannel == null) {
            throw new RuntimeException("Ticket channel not found");
        }
        EmbedBuilder builder = new EmbedBuilder()
                .setColor(0xFFFFFF)
                .setDescription("Please click on button to open ticket base on your need\n\n ⚠\uFE0F ️**OPEN WITHOUT REASON WILL RESULT IN TIMEOUT OR BAN**");
        MessageCreateBuilder messageBuilder = new MessageCreateBuilder();
        messageBuilder.setEmbeds(builder.build());
        messageBuilder.addActionRow(
                Button.primary("ticket:proof", "send proof").withEmoji(Emoji.fromFormatted("\uD83E\uDDFE"))
        );
        ticketChannel.sendMessage(messageBuilder.build()).queue();
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();
        if (id == null) {
            return;
        }
        if (!id.contains("ticket:")) {
            return;
        }
        id = id.replace("ticket:", "");
        if (id.equals("proof")) {
            Category ticketCategory = this.guild.getCategoryById(this.ticket_category);
            if (ticketCategory == null) {
                event.reply("Error creating channel").setEphemeral(true).queue();
                return;
            }
            EnumSet<Permission> permissions = EnumSet.of(Permission.VIEW_CHANNEL, Permission.MESSAGE_SEND, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_HISTORY);
            TextChannel channel = ticketCategory.createTextChannel("proof-" + event.getUser().getEffectiveName())
                    .addMemberPermissionOverride(event.getUser().getIdLong(), permissions, Collections.emptyList())
                    .addPermissionOverride(guild.getPublicRole(), null, permissions)
                    .complete();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            MessageCreateBuilder messageBuilder = new MessageCreateBuilder();

            messageBuilder.setContent(event.getUser().getAsMention());
            embedBuilder
                    .setColor(0xFFFFFF)
                    .setDescription("""
                            Welcome to ticket channel please wait for staff to response
                             ⚠️**DO NOT PING STAFF FOR ANY REASON**
                            
                            You have selected `send proof` So please send proof of purchase here
                            :notepad_spiral: Staff are not able to see this channel this is send proof channel
                            """)
                    .setAuthor(event.getUser().getEffectiveName(), null, event.getUser().getEffectiveAvatarUrl());
            messageBuilder.setEmbeds(embedBuilder.build());
            messageBuilder.setActionRow(
                    Button.danger("ticket:close", "Close").withEmoji(Emoji.fromFormatted("\uD83D\uDCEB"))
            );
            event.reply("Created Ticket channel at " + channel.getAsMention() + " ").setEphemeral(true).queue();
            channel.sendMessage(messageBuilder.build()).queue();
        } else
        if (id.equals("close")) {
            if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
                event.reply("Hey! you can't close the ticket").setEphemeral(true).queue();
                return;
            }
            TextChannel channel = event.getChannel().asTextChannel();
            channel.delete().queue();
            TextChannel logChannel = this.guild.getChannelById(TextChannel.class, this.ticketLog);
            MessageCreateBuilder builder = new MessageCreateBuilder();
            builder.setEmbeds(
                new EmbedBuilder()
                        .setColor(0xFFFFFF)
                        .setDescription("Ticket `" + channel.getName() + "` was closed by " + event.getUser().getAsMention())
                        .addField("Open time", channel.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME), false)
                        .setFooter("Time closed ")
                        .setTimestamp(Instant.now())
                        .build()
            );
            logChannel.sendMessage(builder.build()).queue();
        }
    }
}
