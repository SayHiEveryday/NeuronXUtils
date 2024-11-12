package me.sallyio.neuronutil.common;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReplierHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw();
        if (content.contains("where") && content.contains("script")) {
            event.getMessage().reply("Please read rule <#1273163664387739710> No.17\nOr you can use `/script` to get script").queue();
            return;
        }
    }
}
