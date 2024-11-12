package me.sallyio.neuronutil.commands.admin;

import me.sallyio.neuronutil.common.ScriptHandler;
import me.sallyio.neuronutil.entities.BaseCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;


public class ScriptData extends BaseCommand {
    public ScriptData() {
        this
                .setName("data")
                .setDescription("data")
                .adminOnly();
    }

    @Override
    public void executePrefix(@NotNull MessageReceivedEvent event, String[] args) {
        if (args.length < 5) {
            event.getMessage().reply("Invalid command input").queue();
            return;
        }

        String action = args[1];
        String type = args[2];
        String key = args[3];
        String data = args[4];

        if (action == null || type == null || key == null || data == null) {
            event.getMessage().reply("Invalid command input").queue();
        }
        ScriptHandler handler = new ScriptHandler("ScriptData.json");
        switch (action) {
            case "set":
                switch (type) {
                    case "status":
                        handler.updateAvailability(key,Boolean.parseBoolean(data));
                        event.getMessage().reply(handler.getEntry(key).toPrettyString()).queue();
                        break;
                    case "script":
                        handler.updateScript(key,data);
                        event.getMessage().reply(handler.getEntry(key).toPrettyString()).queue();
                        break;
                    default:
                        event.getMessage().reply("Invalid type").queue();
                }
                break;
            case "get":
                event.getMessage().reply(handler.getEntry(key).toPrettyString()).queue();
                break;
            case "new":
                handler.putEntry(key, true, data);
                event.getMessage().reply(handler.getEntry(key).toPrettyString()).queue();
                break;
            default:
                event.getMessage().reply("Invalid action").queue();
        }

    }
}
