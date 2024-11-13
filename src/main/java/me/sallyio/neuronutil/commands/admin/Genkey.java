package me.sallyio.neuronutil.commands.admin;

import me.sallyio.PandaKey.model.GenerateKeyModel;
import me.sallyio.PandaKey.rest.RestClient;
import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;

public class Genkey extends BaseCommand {
    private final RestClient restClient;
    public Genkey(RestClient restClient) {
        this.restClient = restClient;
        this
                .setName("genkey")
                .setDescription("Generate Key")
                .addOption(
                        new OptionData(OptionType.INTEGER, "expires", "Expiration from now", true)
                                .addChoice("1d", 1)
                                .addChoice("2d", 2)
                                .addChoice("3d", 3)
                                .addChoice("5d", 5)
                                .addChoice("7d", 7)
                                .addChoice("14d", 14)
                                .addChoice("30d", 30)
                                .addChoice("never", 32767),
                        new OptionData(OptionType.INTEGER, "amount", "Amount to generate", true),
                        new OptionData(OptionType.BOOLEAN, "premium", "is Premium?", true),
                        new OptionData(OptionType.STRING, "note", "Note?", false)
                )
                .adminOnly();

    }

    @Override
    public void executeSlash(@NotNull SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        try {
            Integer days = event.getOption("expires", OptionMapping::getAsInt);
            Integer amount = event.getOption("amount", OptionMapping::getAsInt);
            Boolean isPremium = event.getOption("premium", OptionMapping::getAsBoolean);
            String note = event.getOption("note", OptionMapping::getAsString);

            if (amount <= 0) {
                event.getHook().sendMessage("Amount can't be 0 or lower than 0").queue();
                return;
            }
            if (note == null) {
                note = "no note";
            }
            GenerateKeyModel model = this.restClient.generateKey(
                    LocalDate.now().plusDays(days),
                    amount,
                    isPremium,
                    note
            );
            StringBuilder builder = new StringBuilder("--Neuron keys--\n");
            MessageCreateBuilder messageBuilder = new MessageCreateBuilder();

            File file = File.createTempFile("key-", ".txt");

            model.getGeneratedKeys().forEach((v) -> {
                builder.append(v.getValue()).append("\n");
            });

            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(builder.toString());

            messageBuilder.addFiles(FileUpload.fromData(file));
            messageBuilder.setContent(model.getMessage());

            event.getHook().sendMessage(messageBuilder.build()).queue();

            bw.close();
            file.delete();
        } catch (Exception e) {
            event.getHook().sendMessage("There was an error trying to generate key").queue();
        }
    }
}
