package me.sallyio.neuronutil.core;

import me.sallyio.neuronutil.entities.DeleteMessageCache;
import me.sallyio.neuronutil.entities.MessageCache;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CacheManager extends ListenerAdapter {
    @Override
    public void onMessageDelete(@NotNull MessageDeleteEvent event) {
        try {
            DeleteMessageCache cache = DeleteMessageCache.getInstance();
            Message messageCache = MessageCache.getInstance().get(event.getChannel().getId());
            cache.put(event.getChannel().getId(), messageCache);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        try {
            MessageCache cache = MessageCache.getInstance();
            cache.put(event.getChannel().getId(), event.getMessage());
        } catch ( Exception ignored) {

        }
    }
}
