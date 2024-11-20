package me.sallyio.neuronutil.entities.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.sallyio.neuronutil.entities.Pagination;

import java.time.Duration;

public class PaginationCache {
    private static final PaginationCache INSTANCE = new PaginationCache(); // Singleton pattern

    private final LoadingCache<String, Pagination> cache;

    private PaginationCache() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .build(this::loadValue);
    }

    public static PaginationCache getInstance() {
        return INSTANCE;
    }

    private Pagination loadValue(String key) {
        return cache.get(key);
    }

    public void put(String key, Pagination value) {
        cache.put(key, value);
    }

    public Pagination get(String key) {
        return cache.get(key);
    }

    public Pagination getWithDefault(String key, Pagination defaultValue) {
        return cache.get(key, k -> defaultValue);
    }
}
