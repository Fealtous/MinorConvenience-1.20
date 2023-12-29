package dev.fealtous.minorconvenience.convenience;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.utils.RenderUtils;
import dev.fealtous.minorconvenience.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ExperimentsHandler {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final int INDICATOR = 49;
    private static int lastClicked = 0;
    private static Slot[] clickInOrderSlots = new Slot[36];


    @SubscribeEvent
    public static void onGuiRender(ScreenEvent.Render.Post e) {
        if (e.getScreen().getTitle().getString().trim().startsWith("Ultrasequencer (")) {
            List<Slot> slots = ((ContainerScreen) e.getScreen()).getMenu().slots;
            if (slots.size() > 48 && slots.get(INDICATOR).hasItem()) {
                if (slots.get(INDICATOR).getItem().getDisplayName().getString().contains("Timer")) {
                    lastClicked = 0;
                    for (Slot slot : clickInOrderSlots) {
                        if (slot != null && slot.hasItem() && TextUtils.cleanColorCodes(slot.getItem().getDisplayName().getString()).matches("\\d+")) {
                            lastClicked++;
                        }
                    }
                    if (clickInOrderSlots[lastClicked] != null) {
                        Slot nextSlot = clickInOrderSlots[lastClicked];
                        RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) e.getScreen(), nextSlot.x, nextSlot.y, 0xe0099099);
                    }
                }
            }
        }

    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        if (mc.screen instanceof ContainerScreen) {
            List<Slot> slots = ((ContainerScreen) mc.screen).getMenu().slots;
            String name = mc.screen.getTitle().getString();
            if (name.startsWith("Ultrasequencer (")) {
                if (slots.get(INDICATOR).hasItem() && slots.get(INDICATOR).getItem().getDisplayName().getString().contains("Remember")) {
                    for (Slot slot : slots) {
                        if (!slot.hasItem()) continue;
                        String itemname = TextUtils.cleanColorCodes(slot.getItem().getDisplayName().getString());
                        if (itemname.matches("\\d+")) {
                            clickInOrderSlots[Integer.parseInt(itemname) - 1] = slot;
                        }
                    }
                }
            }
        }
    }
    private static void debug(String msg, Object data) {
        LogUtils.getLogger().debug(msg + ": " + data);
    }
    @SubscribeEvent
    public static void onOpen(ScreenEvent.Opening e) {
        clickInOrderSlots = new Slot[36];
    }


}
