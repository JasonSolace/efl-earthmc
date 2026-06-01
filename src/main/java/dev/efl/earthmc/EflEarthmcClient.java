package dev.efl.earthmc;

import dev.efl.earthmc.chat.EflChatTriggers;
import dev.efl.earthmc.command.EflClientCommands;
import dev.efl.earthmc.config.EflConfigStore;
import dev.efl.earthmc.hud.EflTimerHud;
import dev.efl.earthmc.timer.PlayTimer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EflEarthmcClient implements ClientModInitializer {
    public static final String MOD_ID = "efl_earthmc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final EflConfigStore CONFIG_STORE = new EflConfigStore();
    private static final PlayTimer PLAY_TIMER = new PlayTimer();

    @Override
    public void onInitializeClient() {
        CONFIG_STORE.load();
        EflChatTriggers.register(CONFIG_STORE, PLAY_TIMER);
        EflTimerHud.register(CONFIG_STORE, PLAY_TIMER);
        EflClientCommands.register(CONFIG_STORE, PLAY_TIMER);
        LOGGER.info("EFL EarthMC initialized");
    }
}
