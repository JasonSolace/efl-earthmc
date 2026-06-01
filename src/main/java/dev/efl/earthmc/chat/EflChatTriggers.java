package dev.efl.earthmc.chat;

import dev.efl.earthmc.config.EflConfigStore;
import dev.efl.earthmc.timer.PlayTimer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

import java.util.Locale;

public final class EflChatTriggers {
    private static final String LOCAL_CHAT_PREFIX = "[local]";

    private EflChatTriggers() {
    }

    public static void register(EflConfigStore configStore, PlayTimer playTimer) {
        ClientReceiveMessageEvents.CHAT.register((message, signedMessage, sender, params, receptionTimestamp) -> {
            String chatLine = message.getString();
            if (!isLocalChat(chatLine)) {
                return;
            }

            String normalizedMessage = normalize(chatLine);

            for (String trigger : configStore.config().triggerWords()) {
                if (containsTrigger(normalizedMessage, normalize(trigger))) {
                    playTimer.start();
                    return;
                }
            }
        });
    }

    public static String normalize(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private static boolean isLocalChat(String chatLine) {
        return chatLine.trim().toLowerCase(Locale.ROOT).startsWith(LOCAL_CHAT_PREFIX);
    }

    private static boolean containsTrigger(String normalizedMessage, String normalizedTrigger) {
        if (normalizedTrigger.isEmpty()) {
            return false;
        }

        String paddedMessage = " " + normalizedMessage + " ";
        String paddedTrigger = " " + normalizedTrigger + " ";
        return paddedMessage.contains(paddedTrigger);
    }
}
