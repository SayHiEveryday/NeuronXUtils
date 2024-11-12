package me.sallyio.neuronutil.entities;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import net.dv8tion.jda.api.entities.Message;

import java.time.Duration;

public class MessageCache {
    private static final MessageCache INSTANCE = new MessageCache(); // Singleton pattern

    private final LoadingCache<String, Message> cache;

    private MessageCache() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(2))
                .build(this::loadValue); // Replace with your value loading logic
    }

    public static MessageCache getInstance() {
        return INSTANCE;
    }

    private Message loadValue(String key) {
        return cache.get(key);
    }

    public void put(String key, Message value) {
        cache.put(key, value);
    }

    public Message get(String key) {
        return cache.get(key);
    }
}