package dev.m4trix.framework.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class TimeUtil {

    private TimeUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static long toTicks(Duration duration) {
        return duration.toMillis() / 50;
    }

    public static long toTicks(long time, TimeUnit unit) {
        return unit.toMillis(time) / 50;
    }

    public static Duration fromTicks(long ticks) {
        return Duration.ofMillis(ticks * 50);
    }

    public static String format(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        
        long hours = absSeconds / 3600;
        long minutes = (absSeconds % 3600) / 60;
        long secs = absSeconds % 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes, secs);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, secs);
        } else {
            return String.format("%ds", secs);
        }
    }

    public static String formatCompact(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        
        long hours = absSeconds / 3600;
        long minutes = (absSeconds % 3600) / 60;
        long secs = absSeconds % 60;
        
        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, secs);
        } else {
            return String.format("%02d:%02d", minutes, secs);
        }
    }

    public static Duration parse(String input) {
        if (input == null || input.isEmpty()) {
            return Duration.ZERO;
        }

        long total = 0;
        StringBuilder number = new StringBuilder();
        
        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else if (number.length() > 0) {
                long value = Long.parseLong(number.toString());
                switch (c) {
                    case 'd': total += TimeUnit.DAYS.toSeconds(value); break;
                    case 'h': total += TimeUnit.HOURS.toSeconds(value); break;
                    case 'm': total += TimeUnit.MINUTES.toSeconds(value); break;
                    case 's': total += value; break;
                }
                number.setLength(0);
            }
        }
        
        return Duration.ofSeconds(total);
    }
}