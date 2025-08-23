package dev.fealtous.minorconvenience.convenience.hoppity;

import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class CFOptimizer {


    @SubscribeEvent
    public static void batchRender(ScreenEvent.Render.Post e) {
        if (!(e.getScreen() instanceof ContainerScreen)) return;

    }

    enum CFItem {


        ;
        int index;
        CFItem(int inventoryIndex) {
            index = inventoryIndex;
        }
    }
}
