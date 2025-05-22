package dev.fealtous.minorconvenience.convenience.chat;

import net.minecraft.network.chat.Component;

public interface ChatParser {
    default boolean handleMessage(Component msg) {
        return false;
    };
}
