package me.sallyio.neuronutil.entities;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.dv8tion.jda.internal.interactions.command.CommandImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommand {
    private String name;
    private String description;
    private final List<OptionData> option = new ArrayList<>();
    private boolean adminCommand = false;
    private boolean vipCommand = false;
    private boolean salCommand = false;

    public List<OptionData> getOption() {
        return option;
    }
    public String getDescription() {
        return description;
    }

    public BaseCommand setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public BaseCommand setName(String name) {
        this.name = name;
        return this;
    }

    public BaseCommand adminOnly() {
        this.adminCommand = true;
        return this;
    }

    public boolean isAdminOnly() {
        return this.adminCommand;
    }

    public BaseCommand vipOnly() {
        this.vipCommand = true;
        return this;
    }

    public boolean isVipOnly() {
        return this.vipCommand;
    }

    public BaseCommand sallyOnly() {
        this.salCommand = true;
        return this;
    }

    public boolean isSallyOnly() {
        return this.salCommand;
    }

    public BaseCommand addOption(OptionData... optionData) { option.addAll(List.of(optionData)); return this; }
    public void executePrefix(@NotNull MessageReceivedEvent event, String[] args) {
        event.getMessage().reply("Hey <@" + event.getAuthor().getId() + ">, we do not have this command available with the prefix, but we also have it as a slash command.").queue();
    }
    public void executeSlash(@NotNull SlashCommandInteractionEvent event) {
        event.reply("How do you interact with this command? Please contact developer").setEphemeral(true).queue();
    }

    public CommandDataImpl buildSlashCommand() {
        CommandDataImpl data = new CommandDataImpl(this.getName(), this.getDescription());
        if (!this.getOption().isEmpty()) {
            data.addOptions(this.getOption());
        }
        return data;
    }
}