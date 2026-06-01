package dev.efl.earthmc.config;

import java.util.ArrayList;
import java.util.List;

public final class EflConfig {
    public List<String> triggerWords = new ArrayList<>(List.of("hut", "hike", "sethike", "set hike", "set hut"));
    public long tackleDelayMillis = 4_000L;
    public long readyVisibleMillis = 4_000L;

    public List<String> triggerWords() {
        return triggerWords;
    }

    public long tackleDelayMillis() {
        return tackleDelayMillis;
    }

    public long readyVisibleMillis() {
        return readyVisibleMillis;
    }
}
