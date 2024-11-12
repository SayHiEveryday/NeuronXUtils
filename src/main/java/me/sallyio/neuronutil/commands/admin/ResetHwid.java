package me.sallyio.neuronutil.commands.admin;

import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ResetHwid extends BaseCommand {
    public ResetHwid() {
        this
                .setName("resethwid")
                .setDescription("ResetHwid By key")
                .addOption(new OptionData(OptionType.STRING, "key", "key to reset", true))
                .adminOnly();
    }

    @Override
    public void executeSlash(@NotNull SlashCommandInteractionEvent event) {
        String key = event.getOption("key").getAsString();
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://pandadevelopment.net/api/reset-hwid?service=phoenixcore&key=" + key))
                    .GET()
                    .build();
            String body = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            event.reply(body).setEphemeral(true).queue();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            event.reply("There was an error trying to reset a hardware identifier").setEphemeral(true).queue();
        }

    }
}
