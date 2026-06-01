package dev.efl.earthmc.timer;

public final class PlayTimer {
    private long startedAtMillis = -1L;

    public void start() {
        startedAtMillis = System.currentTimeMillis();
    }

    public long elapsedMillis() {
        if (!isRunning()) {
            return 0L;
        }

        return System.currentTimeMillis() - startedAtMillis;
    }

    public boolean isVisible(long tackleDelayMillis, long readyVisibleMillis) {
        if (!isRunning()) {
            return false;
        }

        return elapsedMillis() <= tackleDelayMillis + readyVisibleMillis;
    }

    private boolean isRunning() {
        return startedAtMillis > 0L;
    }
}
