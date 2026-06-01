package dev.efl.earthmc.hud;

import dev.efl.earthmc.config.EflConfigStore;
import dev.efl.earthmc.timer.PlayTimer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public final class EflTimerHud {
    private static final int PADDING_X = 6;
    private static final int PADDING_Y = 4;
    private static final int HEIGHT = 18;
    private static final int LOCKED_COLOR = 0x9CA92323;
    private static final int READY_COLOR = 0x9C228B3A;
    private static final int BORDER_COLOR = 0xB8FFFFFF;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    private EflTimerHud() {
    }

    public static void register(EflConfigStore configStore, PlayTimer playTimer) {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> render(drawContext, configStore, playTimer));
    }

    private static void render(GuiGraphics drawContext, EflConfigStore configStore, PlayTimer playTimer) {
        long tackleDelayMillis = configStore.config().tackleDelayMillis();
        long readyVisibleMillis = configStore.config().readyVisibleMillis();
        if (!playTimer.isVisible(tackleDelayMillis, readyVisibleMillis)) {
            return;
        }

        Minecraft client = Minecraft.getInstance();
        Font font = client.font;
        long elapsedMillis = playTimer.elapsedMillis();
        boolean ready = elapsedMillis >= tackleDelayMillis;
        String text = ready ? "Blitz now!" : "Blitz in " + formatSeconds(tackleDelayMillis - elapsedMillis);
        int width = font.width(text) + PADDING_X * 2;
        int centerX = drawContext.guiWidth() / 2;
        int x = centerX - width / 2;
        int y = drawContext.guiHeight() - 62;
        int backgroundColor = ready ? READY_COLOR : LOCKED_COLOR;

        drawContext.fill(x, y, x + width, y + HEIGHT, backgroundColor);
        drawContext.fill(x, y, x + width, y + 1, BORDER_COLOR);
        drawContext.fill(x, y + HEIGHT - 1, x + width, y + HEIGHT, BORDER_COLOR);
        drawContext.fill(x, y, x + 1, y + HEIGHT, BORDER_COLOR);
        drawContext.fill(x + width - 1, y, x + width, y + HEIGHT, BORDER_COLOR);
        drawContext.drawString(font, text, x + PADDING_X, y + PADDING_Y, TEXT_COLOR);
    }

    private static String formatSeconds(long millis) {
        double seconds = Math.max(0L, millis) / 1_000.0D;
        return String.format("%.1fs", seconds);
    }
}
