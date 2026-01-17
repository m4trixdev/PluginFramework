package dev.m4trix.framework.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class TextUtil {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY_SERIALIZER = 
            LegacyComponentSerializer.legacyAmpersand();

    private TextUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Component color(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        return LEGACY_SERIALIZER.deserialize(text);
    }

    public static Component miniMessage(String text) {
        if (text == null || text.isEmpty()) {
            return Component.empty();
        }
        return MINI_MESSAGE.deserialize(text);
    }

    public static String strip(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return LegacyComponentSerializer.legacySection()
                .serialize(color(text));
    }

    public static String toLegacy(Component component) {
        if (component == null) {
            return "";
        }
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}