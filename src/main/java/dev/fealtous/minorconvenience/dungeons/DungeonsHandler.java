package dev.fealtous.minorconvenience.dungeons;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.fealtous.minorconvenience.utils.InventoryHelper;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static dev.fealtous.minorconvenience.utils.Location.CATACOMBS;
import static dev.fealtous.minorconvenience.utils.RegexUtils.blazeHealthPattern;

public class DungeonsHandler {

    private static final Minecraft mc = Minecraft.getInstance();
    private static LinkedList<Entity> blazes = new LinkedList<>();

    public static void passiveCheck() {
        if (isDungeons()) {
            var b = getBlazes();
            for (Blaze blaze : b) {
                // Turn on blaze solver IF there is a blaze nearby.
                if (blaze.position().distanceTo(mc.player.position()) < 15) {
                    registerBlazes(b);
                    return;
                } else {
                    blazes.clear();
                }
            }
        }
    }

    @SubscribeEvent
    public static void renderBlazeBounding(RenderLevelStageEvent e) {
        if (!blazes.isEmpty() && isDungeons()) {
            if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)) {
                var ps = e.getPoseStack();
                Player p = mc.player;
                ps.pushPose();
                RenderUtils.renderRelativeToPlayer(ps, p);
                MultiBufferSource.BufferSource rb = mc.renderBuffers().bufferSource();
                VertexConsumer vc = rb.getBuffer(RenderType.LINES);
                if (blazes.size() != 1) {
                    LevelRenderer.renderLineBox(ps, vc, blazes.getFirst().getBoundingBox(), 1, 0, 0, 1);
                    LevelRenderer.renderLineBox(ps, vc, blazes.getLast().getBoundingBox(), 0, 1, 0, 1);
                } else {
                    LevelRenderer.renderLineBox(ps, vc, blazes.get(0).getBoundingBox(), 1, 1, 1, 1);
                }
                ps.popPose();
                rb.endBatch(RenderType.LINES);

            }
        }
    }

    @SubscribeEvent
    public static void onBlazeDeath(LivingDeathEvent e) {
        blazes.remove(e.getEntity());
    }
    private static boolean isDungeons() {
        return LocatorUtil.whereAmI().equals(CATACOMBS);
    }

    public static void registerBlazes(List<Blaze> availableBlazes) {
        if (!isDungeons()) return;
        Minecraft mc = Minecraft.getInstance();
        var tmpBlazes = new LinkedList<Pair<Entity, Entity>>();

        //10 blazes per puzzle
        for (Entity entity : availableBlazes) {
            List<Entity> nearby = mc.level.getEntities(entity, entity.getBoundingBox().inflate(1.25));
            if (!nearby.isEmpty()) {
                tmpBlazes.add(new Pair<>(entity, nearby.get(0)));
            }
        }

        tmpBlazes.sort((b1, b2) -> {
            int hp1, hp2;
            var blaze1 = blazeHealthPattern.matcher(b1.getB().getDisplayName().getString());
            var blaze2 = blazeHealthPattern.matcher(b2.getB().getDisplayName().getString());
            if (blaze1.find() && blaze2.find()) {
                String s1 = blaze1.group().replaceAll("[^0-9]", "");
                String s2 = blaze2.group().replaceAll("[^0-9]", "");
                hp1 = Integer.parseInt(s1);
                hp2 = Integer.parseInt(s2);
                if (hp1 > hp2) {
                    return 1;
                } else {
                    return -1;
                }
            } return 0;
        });
        tmpBlazes.forEach((x) -> blazes.add(x.getA()));
    }

    final static String [] weirdoYes = {"The reward is not in my chest!", "At least one of them is lying, and the reward is not in",
            "My chest doesn't have the reward. We are all telling the truth", "My chest has the reward and I'm telling the truth",
            "The reward isn't in any of our chests", "Both of them are telling the truth."};
    @SubscribeEvent
    public static void threeWierdosSolver(ClientChatReceivedEvent e) {
        if (!isDungeons()) return;
        String message = e.getMessage().getString().replaceAll("[^a-zA-Z,.!'\\s]","");
        if (!message.contains("NPC")) return;
        for (String s : weirdoYes) {
            if (message.contains(s)) {
                MutableComponent component = (MutableComponent) e.getMessage();
                component.setStyle(e.getMessage().getStyle().withColor(0xff0000));
                component.getSiblings().forEach((i) -> {
                    ((MutableComponent) i).setStyle(e.getMessage().getStyle().withColor(0xff0000));
                });
                e.setMessage(component);
                break;
            }
        }
    }
    private static List<Blaze> getBlazes() {
        var availableBlazes = new LinkedList<Blaze>();
        Minecraft.getInstance().level.entitiesForRendering().forEach((x) -> {
            if (x instanceof Blaze && !blazes.contains(x)) availableBlazes.add((Blaze) x);
        });
        return availableBlazes;
    }
    public static void resetBlazes() {
        blazes.clear();
    }

    @SubscribeEvent
    public static void terminalSolver(ScreenEvent.Render.Post e) {
        var currentScreen = e.getScreen();
        if (!InventoryHelper.isChestScreen(currentScreen)) return;
        var title = currentScreen.getTitle().getString().trim();
        if (title.contains("starts")) {
            var split = title.split("'");
            InventoryHelper.chestInventory(currentScreen).forEach((x) -> {
                if (x.hasItem() && x.getItem().getDisplayName().getString().startsWith(split[1])) {
                    RenderUtils.slotHighlight(e.getGuiGraphics(), ((ContainerScreen) currentScreen), x.x, x.y, RenderUtils.HIGHLIGHT);
                }
            });
        }
    }
    static HashMap<MapDecoration, PlayerInfo> playerHeadsToRender = new HashMap<>();
    static int tickTimer = 300;
    static final NamedGuiOverlay overlayNo = GuiOverlayManager.getOverlays().get(25);
    static final NamedGuiOverlay overlayYes = GuiOverlayManager.getOverlays().get(0);
    static final int HOTBAR_LAST = 8;
    @SubscribeEvent
    public static void mapRender(RenderGuiOverlayEvent.Post e) {
        /*if (true) {
            if (!playerHeadsToRender.isEmpty()) playerHeadsToRender.clear();
            tickTimer = 300;
            return;
        }*/
        // Wait what the fuck did I do this for?
        // It should be if YES, render... whatever

        if (!e.getOverlay().equals(overlayYes)) return;

        var mapItemstack = ((Inventory) Minecraft.getInstance().player.inventoryMenu.slots.get(45).container).items.get(HOTBAR_LAST);
        var mapItem = mapItemstack.getItem();


        if (mapItem instanceof MapItem) {
            var mc = Minecraft.getInstance();
            var buffer = mc.renderBuffers().bufferSource();
            var pose = e.getGuiGraphics().pose();
            int mapId = MapItem.getMapId(mapItemstack);
            var mapdata = MapItem.getSavedData(mapId, mc.level);

            if (tickTimer-- == 0) {
                for (PlayerInfo listedOnlinePlayer : mc.player.connection.getListedOnlinePlayers()) {
                    var str = mc.gui.getTabList().getNameForDisplay(listedOnlinePlayer).getString();
                    //if (str.matches(".*\\[\\d+].+\\()")) {
                        //System.out.println(str);
                    //}
                    //mc.level.getEntity()
                    //listedOnlinePlayer.getProfile().getId()
                }


                tickTimer = 300;
            }

            double screenRelationX = 960.0 / 1920.0;
            double translationX = screenRelationX * mc.getWindow().getWidth();
            double screenRelationY = 0.5f;
            int offset = 5;
            float scale = 1.5f; // todo add config scaling
            if (e.getOverlay().equals(GuiOverlayManager.getOverlays().get(0))) {
                pose.pushPose();
                pose.translate(translationX - MapItem.IMAGE_WIDTH - offset, offset, 0);
                mc.gameRenderer.getMapRenderer().render(pose,
                        buffer,
                        mapId,
                        mapdata,
                        true,
                        mc.getEntityRenderDispatcher().getPackedLightCoords(mc.player, e.getPartialTick()));
                pose.popPose();
            }

            if (e.getOverlay().equals(GuiOverlayManager.getOverlays().get(25))) {
                pose.pushPose();
                playerHeadsToRender.forEach((deco, player) -> {
                    int xpos = (deco.x() + 128) / 2;
                    int ypos = (deco.y() + 128) / 2;
                    PlayerFaceRenderer.draw(e.getGuiGraphics(), player.getSkin(), (int) (translationX - MapItem.IMAGE_WIDTH + xpos - 10), ypos - 4, 8);
                });
                pose.popPose();
            }
        }

    }

    public static void chestRender(ScreenEvent.Render.Post e) {
        var currentScreen = e.getScreen();
        if (!InventoryHelper.isChestScreen(currentScreen)) return;
        List<Slot> slots = InventoryHelper.chestInventory(currentScreen);
        slots.forEach((x) -> {
            if (x.hasItem()) RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) currentScreen,
                    x.x, x.y, RenderUtils.COLOR_STANDARD);
        });
    }
}
