package me.sallyio.neuronutil.common;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReplierHandler extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String content = event.getMessage().getContentRaw().toLowerCase();
        if (content.contains("where") && content.contains("script")) {
            event.getMessage().reply("Please read rule <#1273163664387739710> No.17\nOr you can use `/script` to get script").queue();
            return;
        } else
        if ((content.contains("send") || content.contains("give")) && content.contains("script")) {
            event.getMessage().reply("Try </script:1303337755388022856>!\nNote that most script is in the main loader.").queue();
            return;
        }
    }
}
