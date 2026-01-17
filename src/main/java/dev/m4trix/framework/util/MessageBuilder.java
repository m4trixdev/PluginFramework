package dev.m4trix.framework.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;

public final class MessageBuilder {

    private final List<Component> components;

    private MessageBuilder() {
        this.components = new ArrayList<>();
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder text(String text) {
        components.add(Component.text(text));
        return this;
    }

    public MessageBuilder text(String text, NamedTextColor color) {
        components.add(Component.text(text, color));
        return this;
    }

    public MessageBuilder colored(String text) {
        components.add(TextUtil.color(text));
        return this;
    }

    public MessageBuilder newline() {
        components.add(Component.newline());
        return this;
    }

    public MessageBuilder space() {
        components.add(Component.space());
        return this;
    }

    public MessageBuilder bold(String text) {
        components.add(Component.text(text).decorate(TextDecoration.BOLD));
        return this;
    }

    public MessageBuilder italic(String text) {
        components.add(Component.text(text).decorate(TextDecoration.ITALIC));
        return this;
    }

    public MessageBuilder underline(String text) {
        components.add(Component.text(text).decorate(TextDecoration.UNDERLINED));
        return this;
    }

    public MessageBuilder component(Component component) {
        if (component != null) {
            components.add(component);
        }
        return this;
    }

    public Component build() {
        if (components.isEmpty()) {
            return Component.empty();
        }
        
        Component result = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            result = result.append(components.get(i));
        }
        
        return result;
    }

    public String buildLegacy() {
        return TextUtil.toLegacy(build());
    }
}