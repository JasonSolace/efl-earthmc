package dev.efl.earthmc.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.efl.earthmc.EflEarthmcClient;
import dev.efl.earthmc.chat.EflChatTriggers;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class EflConfigStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<String> DEFAULT_TRIGGERS = List.of("hut", "hike", "sethike", "set hike", "set hut");

    private final Path configPath = FabricLoader.getInstance().getConfigDir().resolve("efl-earthmc.json");
    private EflConfig config = new EflConfig();

    public EflConfig config() {
        return config;
    }

    public void load() {
        if (Files.exists(configPath)) {
            try (Reader reader = Files.newBufferedReader(configPath)) {
                EflConfig loaded = GSON.fromJson(reader, EflConfig.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (IOException exception) {
                EflEarthmcClient.LOGGER.warn("Unable to read EFL config. Defaults will be used.", exception);
                config = new EflConfig();
            }
        }

        sanitize();
        save();
    }

    public boolean addTrigger(String trigger) {
        String cleaned = trigger.trim();
        if (cleaned.isEmpty()) {
            return false;
        }

        Set<String> normalizedTriggers = normalizedTriggerSet();
        if (!normalizedTriggers.add(EflChatTriggers.normalize(cleaned))) {
            return false;
        }

        config.triggerWords.add(cleaned);
        save();
        return true;
    }

    public boolean removeTrigger(String trigger) {
        String normalized = EflChatTriggers.normalize(trigger);
        boolean removed = config.triggerWords.removeIf(existing -> EflChatTriggers.normalize(existing).equals(normalized));
        if (removed) {
            sanitize();
            save();
        }

        return removed;
    }

    public void resetTriggers() {
        config.triggerWords = new ArrayList<>(DEFAULT_TRIGGERS);
        save();
    }

    private void sanitize() {
        if (config.triggerWords == null || config.triggerWords.isEmpty()) {
            config.triggerWords = new ArrayList<>(DEFAULT_TRIGGERS);
        }

        Set<String> seen = new LinkedHashSet<>();
        List<String> sanitizedTriggers = new ArrayList<>();

        for (String trigger : config.triggerWords) {
            if (trigger == null || trigger.trim().isEmpty()) {
                continue;
            }

            String normalized = EflChatTriggers.normalize(trigger);
            if (seen.add(normalized)) {
                sanitizedTriggers.add(trigger.trim());
            }
        }

        config.triggerWords = sanitizedTriggers.isEmpty() ? new ArrayList<>(DEFAULT_TRIGGERS) : sanitizedTriggers;
        mergeMissingDefaults();
        if (config.tackleDelayMillis <= 0L) {
            config.tackleDelayMillis = 4_000L;
        }
        if (config.readyVisibleMillis < 0L) {
            config.readyVisibleMillis = 4_000L;
        }
    }

    private Set<String> normalizedTriggerSet() {
        Set<String> normalizedTriggers = new LinkedHashSet<>();
        for (String trigger : config.triggerWords) {
            normalizedTriggers.add(EflChatTriggers.normalize(trigger));
        }
        return normalizedTriggers;
    }

    private void mergeMissingDefaults() {
        Set<String> normalizedTriggers = normalizedTriggerSet();
        for (String trigger : DEFAULT_TRIGGERS) {
            if (normalizedTriggers.add(EflChatTriggers.normalize(trigger))) {
                config.triggerWords.add(trigger);
            }
        }
    }

    private void save() {
        try {
            Files.createDirectories(configPath.getParent());
            try (Writer writer = Files.newBufferedWriter(configPath)) {
                GSON.toJson(config, writer);
            }
        } catch (IOException exception) {
            EflEarthmcClient.LOGGER.warn("Unable to save EFL config.", exception);
        }
    }
}
