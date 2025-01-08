package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.MinorConvenience;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyBindingHandlers {

    public static KeyMapping COPY_TOGGLE = new KeyMapping(
            "key."+ MinorConvenience.MODID +".test",
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.categories.misc"
    );

    public static void registerClientShit(RegisterKeyMappingsEvent e) {
        e.register(KeyBindingHandlers.COPY_TOGGLE);
    }
}
