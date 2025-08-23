package dev.fealtous.minorconvenience.convenience.experiments;

import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.ItemStackedOnOtherEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

import java.util.List;
import java.util.Map;

public class ExperimentsHandler {
    private static final Minecraft mc = Minecraft.getInstance();
    private static final int INDICATOR = 49;
    private static boolean renderStage = false;
    public static boolean inExperiments = false;
    private static int lastClicked = 0;
    private static Slot[] ultrasequencerSlots = new Slot[54];
    private static int[] chronoSlots = new int[49];
    private static Map<String, Integer> chronoFormat = null;
    private static int chronoRenderSize = 2;
    private static boolean readyForNext = false;

    public static void init() {
        ScreenEvent.Render.Post.BUS.addListener(ExperimentsHandler::renderNext);
        ScreenEvent.Opening.BUS.addListener(ExperimentsHandler::onOpen);
        TickEvent.ClientTickEvent.Pre.BUS.addListener(ExperimentsHandler::detect);
        ItemStackedOnOtherEvent.BUS.addListener(ExperimentsHandler::itemDetect);
    }

    public static void handleChronoUpdate(ItemStack item, int slotId) {
        if (slotId == INDICATOR) {
            var newRenderStage = item.getDisplayName().getString().matches(".*Timer.*");
            if (!renderStage && newRenderStage) {
                lastClicked = 0;
            }
            renderStage = newRenderStage;
            if (!renderStage) lastClicked = 0;
        }
        if (slotId < 53 && slotId != INDICATOR && !renderStage) {
            if (item.isEnchanted() && readyForNext) {
                readyForNext = false;
                if (chronoFormat == null) return;
                var name = chronoFormat.get(item.getDisplayName().getString());
                if (name != null) {
                    chronoSlots[lastClicked++] = name;
                }
            } else if (!item.isEnchanted()) readyForNext = true;
        }
    }
    @SubscribeEvent
    public static void renderNext(ScreenEvent.Render.Post e) {
        if (!LocatorUtil.isIn(Location.ISLAND)) return;
        if ((e.getScreen() instanceof ContainerScreen scrn)) {
            if (renderStage) {
                if (scrn.getTitle().getString().trim().startsWith("Ultrasequencer (")) {
                    var next = ultrasequencerSlots[lastClicked];
                    if (next == null) return;
                    RenderUtils.slotHighlight(e.getGuiGraphics(), scrn, next.x, next.y, RenderUtils.HIGHLIGHT);
                } else if (scrn.getTitle().getString().trim().startsWith("Chronomatron (")) {
                    var menu = ((ContainerScreen) e.getScreen()).getMenu();
                    for (int i = 0; i < chronoRenderSize; i++) {
                        var res = chronoSlots[lastClicked] + i * 9;
                        if (res % 9 == 0) continue;
                        RenderUtils.slotHighlight(e.getGuiGraphics(), scrn, menu.getSlot(res).x, menu.getSlot(res).y, RenderUtils.HIGHLIGHT);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void detect(TickEvent.ClientTickEvent.Pre e) {
        if (!LocatorUtil.isIn(Location.ISLAND)) return;
        if (mc.screen instanceof ContainerScreen screen) {
            List<Slot> slots = screen.getMenu().slots;
            Container container = screen.getMenu().getContainer();
            String name = screen.getTitle().getString();
            inExperiments = name.startsWith("Ultrasequencer") || name.startsWith("Chronomatron");
            if (!renderStage) {
                if (name.startsWith("Ultrasequencer (")) {
                    lastClicked = 0;
                    for (int i = 0; i < container.getContainerSize(); i++) {
                        var itemname = container.getItem(i).getDisplayName().getString().replaceAll("[^0-9]", "");
                        if (itemname.matches("[0-9]+")) {
                            ultrasequencerSlots[Integer.parseInt(itemname)-1] = slots.get(i);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void itemDetect(ItemStackedOnOtherEvent e) {
        if (renderStage) lastClicked++;
    }

    @SubscribeEvent
    public static void onOpen(ScreenEvent.Opening e) {
        if (!LocatorUtil.isIn(Location.ISLAND)) return;
        ultrasequencerSlots = new Slot[54];
        chronoSlots = new int[49];
        lastClicked = 0;
        String screenName = e.getScreen().getTitle().getString();
        if (screenName.startsWith("Chronomatron (")) {
            switch(screenName.split("\\(")[1].replaceAll("[^A-Za-z]", "")) {
                case "High" -> {chronoFormat = ExperimentConstants.HIGH_FORMAT; chronoRenderSize = 3;}
                case "Grand" -> {chronoFormat = ExperimentConstants.GRAND_FORMAT; chronoRenderSize = 3;}
                case "Supreme" -> {chronoFormat = ExperimentConstants.SUPREME_FORMAT; chronoRenderSize = 3;}
                case "Transcendent" -> {chronoFormat = ExperimentConstants.TRANSCENDENT_FORMAT; chronoRenderSize = 2;}
                case "Metaphysical" -> {chronoFormat = ExperimentConstants.METAPHYSICAL_FORMAT; chronoRenderSize = 2;}
                default -> {chronoFormat = null; chronoRenderSize = 4;}
            }
            renderStage = false;
        } else {
            chronoFormat = null;
        }
    }
}
