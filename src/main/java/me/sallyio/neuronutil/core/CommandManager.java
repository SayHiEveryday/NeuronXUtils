package me.sallyio.neuronutil.core;

import me.sallyio.neuronutil.entities.BaseCommand;
import me.sallyio.neuronutil.entities.SubcommandContainer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager extends ListenerAdapter {
    private final Map<String, BaseCommand> commandMap = new HashMap<>();
    private final Map<String, SubcommandContainer> subcommandMap = new HashMap<>();
    private String prefix;

    private boolean isAdministrator(SlashCommandInteractionEvent event) {
        Role role = event.getGuild().getRoleById("1273172033966178324");
        return event.getMember().getRoles().contains(role);
    }
    private boolean isAdministrator(MessageReceivedEvent event) {
        Role role = event.getGuild().getRoleById("1273172033966178324");
        return event.getMember().getRoles().contains(role);
    }
    private boolean isSally(SlashCommandInteractionEvent event) {
        return event.getUser().getId().equals("698851209032761384");
    }
    private boolean isSally(MessageReceivedEvent event) {
        return event.getAuthor().getId().equals("698851209032761384");
    }
    private boolean isVip(SlashCommandInteractionEvent event) {
        Role role = event.getGuild().getRoleById("1273167148990074932");
        return event.getMember().getRoles().contains(role);
    }
    private boolean isVip(MessageReceivedEvent event) {
        Role role = event.getGuild().getRoleById("1273167148990074932");
        return event.getMember().getRoles().contains(role);
    }

    private boolean isAbleToExecute(BaseCommand cmd, SlashCommandInteractionEvent event) {
        if (cmd.isAdminOnly() && !isAdministrator(event)) {
            return false;
        }
        if (cmd.isSallyOnly() && !isSally(event)) {
            return false;
        }
        return !cmd.isVipOnly() || isVip(event);
    }

    private boolean isAbleToExecute(BaseCommand cmd, MessageReceivedEvent event) {
        if (cmd.isAdminOnly() && !isAdministrator(event)) {
            return false;
        }
        if (cmd.isSallyOnly() && !isSally(event)) {
            return false;
        }
        return !cmd.isVipOnly() || isVip(event);
    }



    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        BaseCommand command = (event.getSubcommandName() != null)
                ? this.subcommandMap.get(event.getName()).getCommand(event.getSubcommandName())
                : this.commandMap.get(event.getName());

        if (command == null) {
            event.reply("Command `" + event.getName() + "` not found ").setEphemeral(true).queue();
            return;
        }

        if (!this.isAbleToExecute(command, event)) {
            event.reply("You are not allowed to use this command!").setEphemeral(true).queue();
            return;
        }

        command.executeSlash(event);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split(" ");
        if (!args[0].startsWith(this.prefix) || event.getAuthor().isBot()) return;

        String cmdName = args[0].replace(this.prefix, "");
        BaseCommand cmd = this.commandMap.get(cmdName);

        if (cmd == null) {
            event.getMessage().reply("Command `" + cmdName + "` not found").queue();
            return;
        }

        if (!this.isAbleToExecute(cmd, event)) {
            event.getMessage().reply("You are not allowed to use this command!").queue();
            return;
        }

        cmd.executePrefix(event, args);

    }

    public CommandManager addCommands(BaseCommand... command) {
        for (BaseCommand scm : command) {
            commandMap.put(scm.getName(),scm);
        }
        return this;
    }
    public CommandManager addSubcommands(SubcommandContainer... subcommand) {
        for (SubcommandContainer scm : subcommand) {
            subcommandMap.put(scm.getName(), scm);
        }
        return this;
    }
    private static boolean isSlashCommandAvailable(BaseCommand obj) {
        try {
            Class<?> clazz = obj.getClass();

            return clazz.getMethod("executeSlash", SlashCommandInteractionEvent.class).getDeclaringClass().equals(clazz);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }
    public void register(JDA jda) {
        List<CommandData> commandList = new ArrayList<>();
        this.commandMap.forEach((i,v) -> {
            if (isSlashCommandAvailable(v)) commandList.add(v.buildSlashCommand());;
        });
        this.subcommandMap.forEach((i,v) -> {
            commandList.add(v.build());
        });
        jda.updateCommands().addCommands(commandList).queue();
    }

    public String getPrefix() {
        return prefix;
    }

    public CommandManager setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }
}