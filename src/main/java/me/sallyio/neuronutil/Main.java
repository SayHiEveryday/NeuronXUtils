package me.sallyio.neuronutil;

import me.sallyio.neuronutil.commands.*;
import me.sallyio.neuronutil.commands.admin.*;
import me.sallyio.neuronutil.commands.misc.*;
import me.sallyio.neuronutil.core.*;
import me.sallyio.neuronutil.common.*;

import net.dv8tion.jda.api.JDA;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CommandManager commandManager = new CommandManager().setPrefix(";");
        commandManager.addCommands(
                new Script(),
                new Ping(),
                new Sysinfo(),
                new ScriptData(),
                new Steal(),
                new Snipe()
        );
        commandManager.addSubcommands(
                new AdminContainer()
        );
        TicketManager ticketManager = new TicketManager()
                .setTicket_category("1305853546595618897")
                .setTicket_channel_id("1305853637435850793")
                .setTicketLog("1305864541720154153");
        final String token = UniversalToken.Bot_DEVELOPMENT;
        Client client = new Client(token, args);
        client.addEventListeners(commandManager);
        client.addEventListeners(new CacheManager());
        client.addEventListeners(new ReplierHandler());
        client.addEventListeners(new ScriptManager());
        client.addEventListeners(ticketManager);
        client.addEventListeners(new PaginationManager());

        JDA jda = client.start();
        jda.awaitReady();
        commandManager.register(jda);


    }
}