package dev.fealtous.minorconvenience.convenience;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.utils.RenderUtils;
import dev.fealtous.minorconvenience.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ExperimentsHandler {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final int INDICATOR = 49;
    private static boolean inSequencer = false;
    private static boolean inChrono = false;
    private static boolean chronoTrigger = false;
    private static int lastClicked = 0;
    private static Slot[] clickInOrderSlots = new Slot[54];
    private static int current = 0;


    @SubscribeEvent
    public static void renderNext(ScreenEvent.Render.Post e) {
        if (!(e.getScreen() instanceof ContainerScreen)) return;
        Container container = ((ContainerScreen) e.getScreen()).getMenu().getContainer();
        if (e.getScreen().getTitle().getString().trim().startsWith("Ultrasequencer (")) {

            if (container.getItem(INDICATOR).getDisplayName().getString().matches(".*Timer.*")) {
                inSequencer = true;
                var next = clickInOrderSlots[lastClicked];
                if (next == null) return;
                RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) e.getScreen(), next.x, next.y, RenderUtils.HIGHLIGHT);
            } else {
                inSequencer = false;
            }
        }
        if (e.getScreen().getTitle().getString().trim().startsWith("Chrono")) {
            if (container.getItem(INDICATOR).getDisplayName().getString().matches(".*Timer.*")) {
                current = 0;
                inChrono = true;
                var next = clickInOrderSlots[lastClicked];
                if (next == null) return;
                RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) e.getScreen(), next.x, next.y, RenderUtils.HIGHLIGHT);
                RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) e.getScreen(), next.x, next.y+16, RenderUtils.HIGHLIGHT);
            } else {
                inChrono = false;
            }
        }

    }

    @SubscribeEvent
    public static void detect(TickEvent.ClientTickEvent e) {
        if (e.phase != TickEvent.Phase.START) return;
        if (mc.screen instanceof ContainerScreen screen) {
            List<Slot> slots = screen.getMenu().slots;
            Container container = screen.getMenu().getContainer();
            String name = screen.getTitle().getString();
            if (name.startsWith("Ultrasequencer (")) {
                if (container.getItem(INDICATOR).getDisplayName().getString().matches(".*Remember.*")) {
                    lastClicked = 0;
                    for (int i = 0; i < container.getContainerSize(); i++) {
                        var itemname = container.getItem(i).getDisplayName().getString().replaceAll("[^0-9]", "");
                        if (itemname.matches("[0-9]+")) {
                            clickInOrderSlots[Integer.parseInt(itemname)-1] = slots.get(i);
                        }
                    }
                }
            }
            if (name.startsWith("Chrono")) {
                if (container.getItem(INDICATOR).getDisplayName().getString().matches(".*Remember.*")) {
                    lastClicked = 0;
                    if (chronoTrigger) {
                        for (int i = 0; i < container.getContainerSize(); i++) {
                            var item = container.getItem(i);
                            if (item.isEnchanted()) {
                                clickInOrderSlots[current++] = slots.get(i);
                                break;
                            }
                        }
                        chronoTrigger = false;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void soundTrigger(PlaySoundEvent e) {
        if (!inChrono && e.getName().equals("block.note_block.pling")) {
            chronoTrigger = true;
        }
    }

    @SubscribeEvent
    public static void itemDetect(ItemStackedOnOtherEvent e) {
        if (!e.getPlayer().level().isClientSide()) return;
        if (!e.getSlot().equals(clickInOrderSlots[lastClicked])) {
            return;
        }
        if (inSequencer || inChrono) {
            lastClicked++;
        }
    }


    private static void debug(String msg, Object data) {
        LogUtils.getLogger().debug(msg + ": " + data);
    }

    @SubscribeEvent
    public static void onOpen(ScreenEvent.Opening e) {
        clickInOrderSlots = new Slot[54];
        lastClicked = 0;
    }



}
